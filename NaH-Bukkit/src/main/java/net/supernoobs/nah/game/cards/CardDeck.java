package net.supernoobs.nah.game.cards;

import java.util.Set;

public abstract class CardDeck {
	public abstract String getName();
	public abstract String getDescription();
	public abstract Set<BlackCard> getBlackCards();
	public abstract Set<WhiteCard> getWhiteCards();
}
