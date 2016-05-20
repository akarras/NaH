package net.supernoobs.nah.game.cards;

import org.jsoup.Jsoup;

public class BlackCard implements ICard {
	private String text;
	private int pick;
	
	public BlackCard(String cardText, int pickCount) {
		text = cardText;
		pick = pickCount;
	}
	
	public int getPick() {
		return pick;
	}

	@Override
	public String getText() {
		return Jsoup.parse(text).text();
	}
}
