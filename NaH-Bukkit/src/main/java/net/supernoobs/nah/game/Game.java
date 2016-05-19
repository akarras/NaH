package net.supernoobs.nah.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.md_5.bungee.api.ChatColor;
import net.supernoobs.nah.Nah;
import net.supernoobs.nah.data.JsonDeck;
import net.supernoobs.nah.game.cards.BlackCard;
import net.supernoobs.nah.game.cards.BlackCardStack;
import net.supernoobs.nah.game.cards.WhiteCardStack;
import net.supernoobs.nah.game.cards.WhiteCard;

public class Game implements Runnable {
	private TreeMap<String,User> players;
	private String gameName;
	private int id;
	private WhiteCardStack whiteCards;
	private BlackCardStack blackCards;
	
	private BiMap<String,List<WhiteCard>> playedCards;
	private BlackCard currentBlackCard;
	
	private List<String> previousWinners;
	
	private String currentCzar;
	private GameSettings settings;
	
	private long roundStarted;
	private long roundEndTime;
	private boolean roundWarned;
	
	private int timerTaskId;
	
	private GameState state;
	public Game(String gameName, int id){
		this.id = id;
		this.players = new TreeMap<String,User>();
		this.gameName = gameName;
		state = GameState.LOBBY;
		settings = new GameSettings();
	}
	
	
	public void start(){
		final GameSettings settings = getSettings();
		if(players.size() < GameSettings.MINIMUM_PLAYERS) {
			sendMessage("§cFailed to start, not enough players");
			return;
		}
		if(settings.getDecks().size() == 0) {
			this.sendMessage("§cGame cannot start without a deck");
			return;
		}
		this.previousWinners = new ArrayList<String>();
		this.whiteCards = new WhiteCardStack();
		this.blackCards = new BlackCardStack();
		for(final String deckName:settings.getDecks()) {
			JsonDeck jsonDeck = Nah.plugin.jsonDecks.getJsonDeck(deckName);
			Set<BlackCard> blackCardSet = jsonDeck.getBlackCards();
			Set<WhiteCard> whiteCardSet = jsonDeck.getWhiteCards();
			this.whiteCards.addCards(whiteCardSet);
			this.blackCards.addCards(blackCardSet);
		}
		
		
		for(User user:players.values()) {
			user.gameStart();
		}
		
		this.state = GameState.PLAYERPICK;
		gameStateChanged();
	}


	public void joinGame(User player) {
		players.put(player.getName(),player);
		player.setGame(gameName);
		if(state.equals(GameState.PLAYERPICK)|state.equals(GameState.CZARPICK)) {
			//Game's in progress, we should do something!
			player.gameStart();
			player.drawCard();
		}
		updatePlayerGUIs();
	}


	public void quitGame(User player) {
		players.remove(player.getName());
		if(player.isHost()){
			//Force the other users to quit the game before removing the game from our manager
			for(User removePlayer:players.values()) {
				this.quitGame(removePlayer);
			}
			Bukkit.getScheduler().cancelTask(this.timerTaskId);
		 	Nah.plugin.gameManager.removeGame(player.getName());
		}
		player.setGame(null);
		player.setMenuState(User.MenuState.MainMenu);
		player.updateGUI();
		checkPlayedCards();
		updatePlayerGUIs();
	}


	public User getLastWinner() {
		return players.get(previousWinners.get(previousWinners.size() - 1));
	}
	
	public int getId() {
		return id;
	}
	
	public WhiteCardStack getWhiteCards() {
		return whiteCards;
	}
	public void setWhiteCards(WhiteCardStack whiteCards) {
		this.whiteCards = whiteCards;
	}
	public BlackCardStack getBlackCards() {
		return blackCards;
	}
	public void setBlackCards(BlackCardStack blackCards) {
		this.blackCards = blackCards;
	}
	
	public BiMap<String,List<WhiteCard>> getPlayedCards() {
		return playedCards;
	}
	
	public String getCurrentCzar(){
		return currentCzar;
	}
	
	public String getGameName() {
		return gameName;
	}
	
	/*
	 * Returns true if the card was played successfully
	 */
	public boolean playerPlayCard(WhiteCard card, User player) {
		if(this.playedCards.containsKey(player.getName())) {
			List<WhiteCard> playedCards = this.playedCards.get(player.getName());
			if(playedCards.size() < this.currentBlackCard.getPick()) {
				playedCards.add(card);
				this.playedCards.put(player.getName(), playedCards);
			} else {
				player.sendMessage("§cYou've already played enough cards");
				this.checkPlayedCards();
				return false;
			}
			
		} else {
			List<WhiteCard> newCardList = new ArrayList<WhiteCard>();
			newCardList.add(card);
			this.playedCards.put(player.getName(), newCardList);
		}
		this.checkPlayedCards();
		this.updatePlayerGUIs();
		return true;
	}
	
	/*
	 * Called to check whether the playerPick state should come to an end
	 * and goes to the czar pick state if the conditions are met
	 */
	
	private void checkPlayedCards(){
		for(User player:this.players.values()) {
			if(!player.hasPlayed()&&!player.isCzar()) {
				return;
			}
		}
		//If all players have played, then change the gamestate to allow the czar to pick
		this.state = GameState.CZARPICK;
		this.updatePlayerGUIs();
	}
	
	public boolean czarPickWinner(List<WhiteCard> card) {
		String winner = playedCards.inverse().get(card);
		if(players.containsKey(winner)) {
			User winningUser = players.get(winner);
			winningUser.wonCard(currentBlackCard,card);
			this.previousWinners.add(winningUser.getName());
			sendMessage(ChatColor.GREEN+winningUser.getName()+" won with "+card.get(0).getText());
			if(winningUser.getScore() >= this.getSettings().getScoreLimit()) {
				//We have a weiner!
				currentBlackCard = null;
				this.updateRoundClock(5000);
				state = GameState.SHOWGAMEWINNER;
				gameStateChanged();
				return false;
			}
			currentBlackCard = null;
			this.updateRoundClock(5000);
			state = GameState.SHOWROUNDWINNER;
			gameStateChanged();
		}
		
		return false;
	}
	
	public BlackCard getCurrentBlackCard(){
		return currentBlackCard;
	}
	
	public void gameStateChanged(){
		fillPlayerHands();
		switch(state){
		case CZARPICK:
			//Check that the czar actually has a card to pick
			if(this.playedCards.size() < 1) {
				this.sendMessage("§cNo cards were played for the Czar to choose");
				this.state = GameState.PLAYERPICK;
				this.gameStateChanged();
				return;
			}
			this.updateRoundClock(this.settings.getRoundTime()*1000);
			break;
		case LOBBY:
			break;
		case PLAYERPICK:
			getNextCzar();
			currentBlackCard = blackCards.drawCard();
			playedCards = HashBiMap.create();
			this.updateRoundClock(this.settings.getRoundTime()*1000);
			this.timerTaskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Nah.plugin, this, 20, 20);
			break;
		default:
			break;
			
		}
		updatePlayerGUIs();
	}
	
	private void updatePlayerGUIs(){
		for(User player:players.values()) {
			player.updateGUI();
		}
	}
	
	private void fillPlayerHands() {
		for(User player:players.values()) {
			player.drawCard();
		}
	}
	
	private void getNextCzar(){
		if(currentCzar == null) {
			currentCzar = players.firstKey();
		}
		boolean foundCurrent = false;
		for(String player:players.keySet()){
			if(foundCurrent) {
				this.currentCzar = player;
				foundCurrent = false;
				break;
			}
			if(this.currentCzar.equals(player)) {
				foundCurrent = true;
			}
		}
		//If the current czar was found, but no replacement was found, we need to loop back to the first
		if(foundCurrent) {
			this.currentCzar = this.players.firstKey();
		}
	}
	
	public void sendMessage(String message) {
		for(User player:players.values()) {
			player.sendMessage(message);
		}
	}
	
	public GameState getGameState(){
		return state;
	}
	
	public enum GameState {
		LOBBY,
		CZARPICK,
		SHOWROUNDWINNER,
		PLAYERPICK,
		SHOWGAMEWINNER
	}
	
	public Collection<User> getPlayers(){
		return players.values();
	}

	public GameSettings getSettings() {
		if(settings == null){
			settings = new GameSettings();
		}
		return settings;
	}

	public void setSettings(GameSettings settings) {
		this.settings = settings;
	}

	//Our game timer
	@Override
	public void run() {
		//Current time - End Time
		long timeLeft = this.roundEndTime - new Date().getTime();
		switch(this.state) {
		case CZARPICK:
			if(timeLeft<0) {
				this.sendMessage("§cThe Czar was a lazy slut and didn't pick a winner");
				this.state = GameState.PLAYERPICK;
				this.gameStateChanged();
			} else if(timeLeft<this.settings.roundWarningTime) {
				if(!this.roundWarned) {
					this.sendMessage("§aYou're almost out of time!");
					this.roundWarned = true;
				}
			} else {
				this.roundWarned = false;
			}
			break;
		case LOBBY:
			break;
		case PLAYERPICK:
			if(timeLeft<0) {
				this.sendMessage("§aYour time to pick is up!");
				this.state = GameState.CZARPICK;
				this.gameStateChanged();
			} else if(timeLeft<this.settings.roundWarningTime) {
				if(!this.roundWarned) {
					this.sendMessage("§aYou're almost out of time!");
					this.roundWarned = true;
				}
			} else {
				this.roundWarned = false;
			}
			break;
		case SHOWGAMEWINNER:
			if(timeLeft<0) {
				this.state = GameState.LOBBY;
				this.gameStateChanged();
			}
			break;
		case SHOWROUNDWINNER:
			if(timeLeft<0) {
				this.state = GameState.PLAYERPICK;
				this.gameStateChanged();
			}
			break;
		default:
			break;
		
		}
		/*if(timeLeft < 0) {
			this.state = GameState.CZARPICK;
			this.gameStateChanged();
			Nah.plugin.getServer().getScheduler().cancelTask(this.timerTaskId);
		} else if (timeLeft<3000){
			sendMessage("§aQuick! Play your cards!");
		}*/
	}
	
	private void updateRoundClock(long time) {
		this.roundStarted = new Date().getTime();
		this.roundEndTime = this.roundStarted + time;
	}
}
