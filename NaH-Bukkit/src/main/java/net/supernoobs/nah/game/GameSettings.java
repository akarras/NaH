package net.supernoobs.nah.game;

import java.util.HashSet;
import java.util.Set;

import net.supernoobs.nah.Nah;
import net.supernoobs.nah.Logger.LogLevel;
import net.supernoobs.nah.data.CardCastDeck;

public class GameSettings {
	private Set<String> enabledDecks;
	private int scoreLimit;
	private String gamePassword;
	private int roundTime;
	public final int roundWarningTime = 3000;
	private int currentDeckPage;
	private Set<CardCastDeck> cardCastDecks;
	private String host;
	
	public GameSettings(String host){
		enabledDecks = new HashSet<String>();
		cardCastDecks = new HashSet<CardCastDeck>();
		setScoreLimit(8);
		enabledDecks.add(Nah.plugin.settings.getDefaultSet());
		setRoundTime(Nah.plugin.settings.getDefaultIdleTime());
		this.host = host;
	}
	
	public Game getGame(){
		return Nah.plugin.gameManager.getGame(host);
	}
	
	public User getHost(){
		return Nah.plugin.userManager.getUser(host);
	}
	
	public Set<String> getDecks() {
		return enabledDecks;
	}
	
	public Set<CardCastDeck> getCastDecks() {
		return cardCastDecks;
	}
	
	public Set<CardCastDeck> cardCastDecks() {
		return cardCastDecks;
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
		if(scoreLimit > Nah.plugin.settings.getMaxScoreLimit()) return;
		if(scoreLimit <= 0) return;
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
		if(roundTime > Nah.plugin.settings.getMaxIdleTime()) { return; }
		if(roundTime < Nah.plugin.settings.getMinimumIdleTime()) { return; }
		this.roundTime = roundTime;
	}
	public int getCurrentDeckPage() {
		return currentDeckPage;
	}
	public void setCurrentDeckPage(int currentDeckPage) {
		this.currentDeckPage = currentDeckPage;
	}
	
	public void addCardCast(String cardCastCode) {
		CardCastDeck deck = Nah.plugin.cardCast.DownloadDeck(cardCastCode);
		if(deck != null) {
			cardCastDecks.add(deck);
			Nah.plugin.nahLogger.Log(LogLevel.NORMAL, "Added cardcast deck "
				+deck.getName()+" "+deck.getDescription()+" "
				+deck.getBlackCards().size()+" black cards "
				+deck.getWhiteCards().size()+" white cards");
		} else {
			getHost().sendMessage("§cError getting CardCast deck");
		}
	}
}
