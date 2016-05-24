package net.supernoobs.nah.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.supernoobs.nah.Nah;
import net.supernoobs.nah.game.Game.GameState;
import net.supernoobs.nah.game.cards.BlackCard;
import net.supernoobs.nah.game.cards.WhiteCard;
import net.supernoobs.nah.inventory.Inventories;

public class User {
	private String name;
	private String currentGame;
	private List<WhiteCard> hand;
	private LinkedList<WinningPair> wonCards;
	private final int maxHand = 7;
	private int score;
	private MenuState state;
	private String enteredPassword;
	private boolean inventoryClosed;
	private MenuState lastMenuState;
	private GameState lastGameState;
	private boolean inventoryUpdating;
	
	public User(String player) {
		this.name = player;
		state = MenuState.MainMenu;
		lastMenuState = MenuState.MainMenu;
		lastGameState = GameState.LOBBY;
		inventoryClosed = true;
	}

	public void sendMessage(String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		getPlayer().sendMessage(message);
	}
	
	public String getPassword() { return enteredPassword; }
	public void setPassword(String pass) { 
		sendMessage("§aYou have set your password to "+pass);
		enteredPassword = pass; 
	}
	
	public boolean isHost() {
		if(currentGame == name){
			return true;
		} else {
			return false;
		}
	}

	public Player getPlayer() {
		return Nah.plugin.getServer().getPlayer(name);
	}
	
	public String getName() {
		return name;
	}
	
	public void updateGUI(){
		//Check if the player has manually closed their inventory
		if(inventoryClosed) {
			//Only reopen the inventory if we have a new state
			if(lastMenuState == state) {
				if(isInGame()) {
					if(lastGameState == getGame().getGameState()) {
						return;
					}
				} else {
					return;
				}
			}
		}
		//Signal that the inventory is being updated
		inventoryUpdating = true;
		inventoryClosed = false;
		if(!isInGame()) {
			switch(state) {
			case BrowseGames:
				getPlayer().openInventory(Inventories.BrowseGames(this));
				break;
			case MainMenu:
				getPlayer().openInventory(Inventories.mainMenu(this));
				break;
			default:
				getPlayer().openInventory(Inventories.mainMenu(this));
				break;
			}
			
		} else {
			switch(state) {
			case Settings:
				getPlayer().openInventory(Inventories.gameSettings(this));
				break;
			case DeckSettings:
				getPlayer().openInventory(Inventories.gameDeckSettings(this));
				break;
			case CardCastSettings:
				getPlayer().openInventory(Inventories.cardCastDeckSettings(this));
				break;
			default:
				state = MenuState.Game;
				break;
				
			}
			if(state == MenuState.Game) {
				GameState state = getGame().getGameState();
				if(getGame().getGameState() == GameState.LOBBY){
					getPlayer().openInventory(Inventories.lobby(this));
				} else if(state == GameState.PLAYERPICK){
					if(!isCzar()) {
						getPlayer().openInventory(Inventories.pickCardView(this));
					} else {
						getPlayer().openInventory(Inventories.czarWaitView(this));
					}
				} else if(state == GameState.CZARPICK) {
					if(isCzar()) {
						getPlayer().openInventory(Inventories.czarPickView(this));
					} else {
						getPlayer().openInventory(Inventories.playerWaitView(this));
					}
				} else if(state == GameState.SHOWROUNDWINNER) {
					getPlayer().openInventory(Inventories.roundWinnerView(this));
				} else if(state == GameState.SHOWGAMEWINNER) {
					getPlayer().openInventory(Inventories.gameWinnerView(this));
				}
			}
		}
		lastMenuState = state;
		if(isInGame()) {
			lastGameState = getGame().getGameState();
		}
	}
	
	public void playerClosedInventory() {
		//Catch inventories being closed from state changes
		if(inventoryUpdating) {
			inventoryUpdating = false;
			return;
		}
		inventoryClosed = true;
	}
	
	public void playerRequestedOpen() {
		inventoryClosed = false;
	}
	
	public boolean isCzar(){
		if(getGame().getCurrentCzar().equals(getName())) {
			return true;
		}
		return false;
	}
	
	public MenuState getMenuState() {
		return state;
	}
	
	public void setMenuState(MenuState state) {
		this.state = state;
	}
	
	public enum MenuState {
		MainMenu,
		BrowseGames,
		Settings,
		DeckSettings,
		CardCastSettings,
		Game
	}
	
	public void setGame(String game) {
		currentGame = game;
	}
	
	public Game getGame() {
		return Nah.plugin.gameManager.getGame(currentGame);
	}
	
	public boolean isInGame() {
		if(!StringUtils.isEmpty(currentGame)) {
			if(getGame() == null) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	public void gameStart(){
		hand = new ArrayList<WhiteCard>();
		wonCards = new LinkedList<WinningPair>();
		setScore(0);
	}
	
	public List<WhiteCard> getHand() {
		return hand;
	}
	
	public boolean removeCard(WhiteCard removal) {
		return hand.remove(removal);
	}
	
	public void playCard(int cardIndex) {
		//Get what card the index is
		WhiteCard playedCard = hand.get(cardIndex);
		boolean playSuccessful = getGame().playerPlayCard(playedCard, this);
		if(playSuccessful){
			getPlayer().sendMessage("§aYou have played "+playedCard.getText());
			hand.remove(cardIndex);
		}
		drawCard();
		this.updateGUI();
	}
	
	public void drawCard() {
		while(hand.size() < maxHand) {
			hand.add(getGame().getWhiteCards().drawCard());
		}
	}
	
	public void wonCard(BlackCard blackCardWon, List<WhiteCard> winningHand) {
		score++;
		wonCards.add(new WinningPair(blackCardWon,winningHand));
	}
	
	public WinningPair getLastWinningPlay() {
		return this.wonCards.getLast();
	}
	
	public LinkedList<WinningPair> getWinningPlays() {
		return wonCards;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public boolean hasPlayed() {
		if(getGame().getGameState().equals(GameState.LOBBY)|getGame().getGameState().equals(GameState.SHOWGAMEWINNER)) {
			return false;
		}
		if(getGame().getPlayedCards().containsKey(getName())) {
			List<WhiteCard> cards = getGame().getPlayedCards().get(getName());
			return cards.size() == getGame().getCurrentBlackCard().getPick();
		}
		return false;
	}
	public class WinningPair{
		private List<WhiteCard> whiteCards;
		private BlackCard black;
		public WinningPair(BlackCard black,List<WhiteCard> white) {
			setWhiteCards(white);
			this.setBlack(black);
		}
		public List<WhiteCard> getWhiteCards() {
			return whiteCards;
		}
		public void setWhiteCards(List<WhiteCard> whiteCards) {
			this.whiteCards = whiteCards;
		}
		public BlackCard getBlack() {
			return black;
		}
		public void setBlack(BlackCard black) {
			this.black = black;
		}
		
	}
}
