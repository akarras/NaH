package net.supernoobs.nah.game.cards;

import org.jsoup.Jsoup;

public class WhiteCard implements ICard {
	private final String text;
	public WhiteCard(String cardText) {
		this.text = cardText;
	}
	
	public String getText() {
		return Jsoup.parse(text).text();
	}
}
