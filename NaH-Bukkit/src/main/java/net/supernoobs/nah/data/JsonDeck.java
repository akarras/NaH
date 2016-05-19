package net.supernoobs.nah.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import net.supernoobs.nah.game.cards.BlackCard;
import net.supernoobs.nah.game.cards.CardDeck;
import net.supernoobs.nah.game.cards.WhiteCard;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JsonDeck extends CardDeck {
	private String name;
	private String description;
	private Set<BlackCard> blackCards;
	private Set<WhiteCard> whiteCards;
	public JsonDeck(File jsonFile){
		JsonParser parser = new JsonParser();
		blackCards = new HashSet<BlackCard>();
		whiteCards = new HashSet<WhiteCard>();
		try {
			JsonObject json = parser.parse(new FileReader(jsonFile)).getAsJsonObject();
			name = json.get("name").getAsString();
			description = json.get("description").getAsString();
			JsonArray blackCardJsonArray = json.get("blackCards").getAsJsonArray();
			for(final JsonElement el:blackCardJsonArray) {
				final JsonObject ob = el.getAsJsonObject();
				String cardText = ob.get("text").getAsString();
				int count = ob.get("pick").getAsInt();
				BlackCard card = new BlackCard(cardText,count);
				blackCards.add(card);
			}
			JsonArray jsonWhiteCards = json.get("whiteCards").getAsJsonArray();
			for(final JsonElement jsonCard:jsonWhiteCards){
				WhiteCard card = new WhiteCard(jsonCard.getAsString());
				whiteCards.add(card);
			}
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public JsonDeck(String name, Set<BlackCard> blackCards, Set<WhiteCard> whiteCards) {
		this.name = name;
		this.blackCards = blackCards;
		this.whiteCards = whiteCards;
		this.description = "";
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
