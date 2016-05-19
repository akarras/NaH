package net.supernoobs.nah.game;

import java.util.HashSet;
import java.util.Set;

public class GameSettings {
	public static final int MINIMUM_PLAYERS = 2;
	public static final int MAXIMUM_PLAYERS = 7;
	private Set<String> enabledDecks;
	private int scoreLimit;
	private String gamePassword;
	private int roundTime;
	public final int roundWarningTime = 3000;
	
	
	public GameSettings(){
		enabledDecks = new HashSet<String>();
		setScoreLimit(8);
		enabledDecks.add("Base Set");
		setRoundTime(60);
	}
	public Set<String> getDecks() {
		return enabledDecks;
	}
	
	public boolean isDeckEnabled(String deck){
		if(enabledDecks.contains(deck)) {
			return true;
		}
		return false;
	}
	
	/*
	 * Returns false if removed, true if added
	 */
	public boolean toggleDeck(String deck) {
		if(enabledDecks.contains(deck)) {
			enabledDecks.remove(deck);
			return false;
		} else {
			enabledDecks.add(deck);
			return true;
		}
	}
	public int getScoreLimit() {
		return scoreLimit;
	}
	public void setScoreLimit(int scoreLimit) {
		this.scoreLimit = scoreLimit;
	}
	public String getGamePassword() {
		return gamePassword;
	}
	public void setGamePassword(String gamePassword) {
		this.gamePassword = gamePassword;
	}
	public int getRoundTime() {
		return roundTime;
	}
	public void setRoundTime(int roundTime) {
		this.roundTime = roundTime;
	}
}
