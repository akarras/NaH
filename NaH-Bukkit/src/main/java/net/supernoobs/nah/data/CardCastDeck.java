package net.supernoobs.nah.data;

import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.supernoobs.nah.Logger.LogLevel;
import net.supernoobs.nah.Nah;
import net.supernoobs.nah.game.cards.BlackCard;
import net.supernoobs.nah.game.cards.CardDeck;
import net.supernoobs.nah.game.cards.WhiteCard;

public class CardCastDeck extends CardDeck {
	private Set<WhiteCard> whiteCards;
	private Set<BlackCard> blackCards;
	private String name;
	private String description;
	
	public CardCastDeck(){
		
	}
	
	public void parseCards(Reader data) {
		try{
			JsonParser parser = new JsonParser();
			JsonObject base = parser.parse(data).getAsJsonObject();
			JsonElement callsJson = base.get("calls");
			blackCards = new HashSet<BlackCard>();
			for(JsonElement callJson:callsJson.getAsJsonArray()) {
				
				JsonElement blackCardJson = callJson.getAsJsonObject().get("text");
				//Our current variables
				String currentCardText = "";
				int currentCardCount = -1;
				//For every card in the array, add it to our current
				for(JsonElement cardText:blackCardJson.getAsJsonArray()) {
					currentCardText += "_" + cardText.getAsString();
					currentCardCount++;
				}
				//Get rid of the initial _
				currentCardText = currentCardText.substring(1);
				BlackCard card = new BlackCard(currentCardText, currentCardCount);
				blackCards.add(card);
			}
			
			JsonElement responsesJson = base.get("responses");
			whiteCards = new HashSet<WhiteCard>();
			for(JsonElement responseJson:responsesJson.getAsJsonArray()) {
				
				JsonElement textArrayJson = responseJson.getAsJsonObject().get("text");
				String currentCardText = "";
				//Add all the parts of the array to the current text
				for(JsonElement text:textArrayJson.getAsJsonArray()) {
					currentCardText += text.getAsString();
				}
				whiteCards.add(new WhiteCard(currentCardText));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Nah.plugin.nahLogger.Log(LogLevel.CRITICAL, "Failed Parsing Deck");
		}
	}
	
	public void parseInfo(Reader data) {
		JsonParser parser = new JsonParser();
		JsonObject infoObject = parser.parse(data).getAsJsonObject();
		name = infoObject.get("name").getAsString();
		description = infoObject.get("description").getAsString();
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Set<BlackCard> getBlackCards() {
		return blackCards;
	}

	@Override
	public Set<WhiteCard> getWhiteCards() {
		return whiteCards;
	}

}
