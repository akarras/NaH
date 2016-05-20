package net.supernoobs.nah.inventory;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import com.google.common.collect.BiMap;

import net.supernoobs.nah.Nah;
import net.supernoobs.nah.data.JsonDeck;
import net.supernoobs.nah.game.Game;
import net.supernoobs.nah.game.GameSettings;
import net.supernoobs.nah.game.User;
import net.supernoobs.nah.game.User.WinningPair;
import net.supernoobs.nah.game.cards.WhiteCard;

public class Inventories {
	public static final String nahPrefix = "§b[NaH] ";
	public static Inventory mainMenu(User user) {
		Inventory gameScreen = Bukkit.createInventory(user.getPlayer(), 27, nahPrefix+"§bNoobs Against Humanity");
		gameScreen.addItem(Buttons.BrowseGames());
		gameScreen.addItem(Buttons.CreateNewGame());
		gameScreen.addItem(Buttons.SupportCaHButton());
		return gameScreen;
	}
	public static Inventory gameSettings(User user) {
		Inventory gameSettings = Bukkit.createInventory(user.getPlayer(), 27, nahPrefix+"§eGame Settings");
		gameSettings.setItem(9, Buttons.currentScoreButton(user));
		gameSettings.setItem(10, Buttons.currentRoundTimeButton(user));
		if(user.isHost()) {
			gameSettings.setItem(0, Buttons.increaseScoreLimitButton());
			gameSettings.setItem(18, Buttons.decreaseScoreLimitButton());
			gameSettings.setItem(1, Buttons.increaseRoundTimeButton());
			gameSettings.setItem(19, Buttons.decreaseRoundTimeButton());
		}
		gameSettings.setItem(15, Buttons.backToLobbyButton());
		gameSettings.setItem(13, Buttons.deckSettingsButton(user));
		return gameSettings;
	}
	public static Inventory gameDeckSettings(User user) {
		Inventory gameDeckSettings = Bukkit.createInventory(user.getPlayer(), 36, nahPrefix+"§eGame Decks");
		GameSettings settings = user.getGame().getSettings();
		String[] packs = (String[]) Nah.plugin.jsonDecks.getAvailablePacks().keySet().
				toArray(new String[Nah.plugin.jsonDecks.getAvailablePacks().keySet().size()]);
		int displaySize = 27;
		int start = settings.getCurrentDeckPage()*displaySize;
		int end = start+displaySize;
		boolean lastPage = false;
		if(packs.length < end) {
			end = packs.length -1;
			lastPage = true;
		}
		for(int x = start; x<end; x++) {
			String pack = packs[x];
			JsonDeck deck = Nah.plugin.jsonDecks.getJsonDeck(pack);
			gameDeckSettings.addItem(Buttons.deckToggleButton(deck, user));
		}
		if(!lastPage) {
			gameDeckSettings.setItem(gameDeckSettings.getSize()-4, Buttons.nextPageButton());
		}
		if(settings.getCurrentDeckPage() > 0) {
			gameDeckSettings.setItem(gameDeckSettings.getSize()-5, Buttons.previousPageButton());
		}
		gameDeckSettings.setItem(gameDeckSettings.getSize()-1,Buttons.backToLobbyButton());
		gameDeckSettings.setItem(gameDeckSettings.getSize()-2,Buttons.SettingsMenuButton(user));
		return gameDeckSettings;
	}
	public static Inventory BrowseGames(User user) {
		Inventory games = Bukkit.createInventory(user.getPlayer(), 27, nahPrefix+"§eBrowse Games");
		for(Game game:Nah.plugin.gameManager.getGames().values()) {
			games.addItem(Buttons.JoinGameButton(game));
		}
		
		games.setItem(games.getSize()-1, Buttons.BackToMainMenu());
		return games;
	}
	
	public static Inventory lobby(User user) {
		Inventory lobby = Bukkit.createInventory(user.getPlayer(), 27, 
				nahPrefix+"§a"+user.getGame().getGameName()+"'s game - Lobby");
		Game game = user.getGame();
		for(User player:game.getPlayers()){
			lobby.addItem(Players.lobbyPlayer(player,user.isHost()));
		}
		lobby.setItem(lobby.getSize()-4, Buttons.SettingsMenuButton(user));
		if(user.isHost()){
			lobby.setItem(lobby.getSize()-2, Buttons.StartGameButton());
		}
		lobby.setItem(lobby.getSize()-1, Buttons.LeaveGameButton());
		return lobby;
	}
	
	public static Inventory pickCardView(User user) {
		//Player Pick View
		Inventory gameBoard = Bukkit.createInventory(user.getPlayer(), 27,nahPrefix+"§aPick a Card");
		return drawGameBoard(user,gameBoard,true);
	}
	
	public static Inventory czarWaitView(User user) {
		Inventory gameBoard = Bukkit.createInventory(user.getPlayer(), 27, nahPrefix+"§cCzar - Please Wait");
		return drawGameBoard(user,gameBoard,true);
	}
	
	public static Inventory czarPickView(User user) {
		Inventory gameBoard = Bukkit.createInventory(user.getPlayer(), 27, nahPrefix+"§cCzar - Pick Winner");
		return drawGameBoard(user,gameBoard,false);
	}
	
	public static Inventory playerWaitView(User user) {
		Inventory gameBoard = Bukkit.createInventory(user.getPlayer(), 27, nahPrefix+"§aPlease Wait While Czar Chooses");
		
		return drawGameBoard(user,gameBoard,false);
	}
	
	public static Inventory roundWinnerView(User user) {
		Inventory gameBoard = Bukkit.createInventory(user.getPlayer(), 27, nahPrefix+"§a"+user.getGame().getLastWinner().getName()+" Won!");
		User lastWinner = user.getGame().getLastWinner();
		gameBoard.setItem(12, Players.roundWinner(lastWinner));
		gameBoard.setItem(13, Cards.comboCard(lastWinner.getLastWinningPlay()));
		//gameBoard.setItem(14, Cards.whiteCard(lastWinner.getLastWinningPlay().getWhiteCards()));
		return gameBoard;
	}
	
	public static Inventory gameWinnerView(User user) {
		Inventory gameBoard = Bukkit.createInventory(user.getPlayer(), 27, nahPrefix+"§a"+user.getGame().getLastWinner().getName()+" won the game!");
		User gameWinner = user.getGame().getLastWinner();
		gameBoard.setItem(4, Players.roundWinner(gameWinner));
		int curSlot = 9;
		for(WinningPair winningPlay:gameWinner.getWinningPlays()) {
			//This will break with a score limit over 9, should look for alternatives.
			gameBoard.setItem(curSlot+9, Cards.whiteCard(winningPlay.getWhiteCards()));
			gameBoard.setItem(curSlot, Cards.blackCard(winningPlay.getBlack()));
			curSlot++;
		}
		return gameBoard;
	}
	
	private static Inventory drawGameBoard(User user, Inventory gameBoard, boolean obfuscate) {
		gameBoard.setItem(4, Cards.blackCard(user.getGame().getCurrentBlackCard()));
		BiMap<List<WhiteCard>,String> playedCards = user.getGame().getPlayedCards().inverse();
		int slot = 10;
		for(List<WhiteCard> cards:playedCards.keySet()) {
			//If the card was played by the player, show it to them, otherwise obfuscate
			String playedBy = playedCards.get(cards);
			if(!user.getName().equals(playedBy)&&obfuscate) {
				gameBoard.setItem(slot, Cards.whiteCard(new WhiteCard("Played by a friend...")));
			} else {
				gameBoard.setItem(slot, Cards.whiteCard(cards));
			}
			slot++;
		}
		for(User player:user.getGame().getPlayers()){
			if(!player.hasPlayed()&&!player.isCzar()){
				gameBoard.setItem(slot, Players.gamePlayer(player));
				slot++;
			}
			if(player.isCzar()) {
				gameBoard.setItem(3, Players.czarPlayer(player));
			}
		}
		
		slot = 19;
		for(WhiteCard card:user.getHand()) {
			gameBoard.setItem(slot,Cards.whiteCard(card));
			slot++;
		}
		gameBoard.setItem(8,Buttons.LeaveGameButton());
		return gameBoard;
	}
}
