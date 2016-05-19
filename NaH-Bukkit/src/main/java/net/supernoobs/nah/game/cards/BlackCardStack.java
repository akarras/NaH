package net.supernoobs.nah.game.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class BlackCardStack {
	List<BlackCard> cards;
	List<BlackCard> drawnCards;
	public BlackCardStack() {
		cards = new ArrayList<BlackCard>();
		drawnCards = new ArrayList<BlackCard>();
	}
	
	public BlackCard drawCard() {
		if(cards.size() == 0) {
			resetDrawnCards();
		}
		int randomCard = new Random().nextInt(cards.size()-1);
		BlackCard drawnCard = cards.remove(randomCard);
		drawnCards.add(drawnCard);
		return drawnCard;
	}
	
	public void resetDrawnCards() {
		cards = drawnCards;
		drawnCards.removeAll(drawnCards);
	}
	
	public List<BlackCard> getAllCards() {
		return cards;
	}
	
	public void addCards(Set<BlackCard> card) {
		cards.addAll(card);
	}
}
