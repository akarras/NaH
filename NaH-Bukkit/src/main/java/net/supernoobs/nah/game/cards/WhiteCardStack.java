package net.supernoobs.nah.game.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WhiteCardStack {
	List<WhiteCard> cards;
	List<WhiteCard> drawnCards;
	public WhiteCardStack() {
		cards = new ArrayList<WhiteCard>();
		drawnCards = new ArrayList<WhiteCard>();
	}
	
	public WhiteCard drawCard() {
		if(cards.size() == 0) {
			resetDrawnCards();
		}
		int randomCard = new Random().nextInt(cards.size()-1);
		WhiteCard drawnCard = cards.remove(randomCard);
		drawnCards.add(drawnCard);
		return drawnCard;
	}
	
	public void resetDrawnCards() {
		cards = drawnCards;
		drawnCards.removeAll(drawnCards);
	}
	
	public List<WhiteCard> getAllCards() {
		return cards;
	}
	
	public void addCards(Set<WhiteCard> card) {
		cards.addAll(card);
		
	}
}
