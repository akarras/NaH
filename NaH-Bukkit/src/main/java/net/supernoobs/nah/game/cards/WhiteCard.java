package net.supernoobs.nah.game.cards;

public class WhiteCard implements ICard {
	private final String text;
	public WhiteCard(String cardText) {
		this.text = cardText;
	}
	
	public String getText() {
		return text;
	}
}
