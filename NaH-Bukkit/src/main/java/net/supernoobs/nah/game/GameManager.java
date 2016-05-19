package net.supernoobs.nah.game;

import java.util.HashMap;
import java.util.Map;

import net.supernoobs.nah.Nah;
import net.supernoobs.nah.game.User.MenuState;

public class GameManager {
	private Map<String,Game> games;
	private int nextGameID;
	public GameManager() {
		games = new HashMap<String,Game>();
		nextGameID = 0;
	}
	
	public Game createGame(User host) {
		if(host.getGame() != null) {
			host.getPlayer().sendMessage("§cYou must leave your current game first");
			return null;
		}
		final Game newGame = new Game(host.getName(),nextGameID);
		games.put(host.getName(), newGame);
		newGame.joinGame(host);
		nextGameID = 1;
		host.updateGUI();
		Nah.plugin.userManager.updateGuiForState(MenuState.BrowseGames);
		return newGame;
	}
	
	public Game getGame(String player){
		return games.get(player);
	}
	
	public boolean removeGame(String player) {
		if(games.containsKey(player)){
			games.remove(player);
			Nah.plugin.userManager.updateGuiForState(MenuState.BrowseGames);
			return true;
		}
		return false;
	}
	
	public Map<String,Game> getGames(){
		return games;
	}
}
