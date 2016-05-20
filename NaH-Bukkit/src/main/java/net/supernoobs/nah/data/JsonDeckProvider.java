package net.supernoobs.nah.data;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.supernoobs.nah.Nah;
import net.supernoobs.nah.game.cards.BlackCard;
import net.supernoobs.nah.game.cards.WhiteCard;

public class JsonDeckProvider {
	public File jsonFolder = new File(Nah.plugin.getDataFolder()+File.separator+"packs");
	LinkedHashMap<String,JsonDeck> decks;
	public JsonDeckProvider(){
		Nah.plugin.saveResource("packs/all.json", false);
		decks = new LinkedHashMap<String,JsonDeck>();
		JsonParser parser = new JsonParser();
		JsonObject obj = new JsonObject();
		try{
			obj = parser.parse(new FileReader(new File(jsonFolder+File.separator+"all.json"))).getAsJsonObject();
			}
		catch (Exception e) {
			Nah.plugin.getLogger().info("Error parsing decks");
		}
		JsonArray allWhiteCards = obj.get("whiteCards").getAsJsonArray();
		JsonArray allBlackCards = obj.get("blackCards").getAsJsonArray();
		for(JsonElement element:obj.get("order").getAsJsonArray()) {
			String deckName = element.getAsString();
			Nah.plugin.getServer().getLogger().info("Loading "+deckName);
			Set<WhiteCard> whiteCards = new HashSet<WhiteCard>();
			Set<BlackCard> blackCards = new HashSet<BlackCard>();
			JsonObject deck = obj.get(deckName).getAsJsonObject();
			
			for(JsonElement blackCardId:deck.get("black").getAsJsonArray()) {
				JsonObject blackCard = allBlackCards.get(blackCardId.getAsInt()).getAsJsonObject();
				String blackCardText = blackCard.get("text").getAsString();
				int pickCount = blackCard.get("pick").getAsInt();
				blackCards.add(new BlackCard(blackCardText,pickCount));
			}
			
			for(JsonElement whiteCardId:deck.get("white").getAsJsonArray()) {
				String whiteCardText = allWhiteCards.get(whiteCardId.getAsInt()).getAsString();
				whiteCards.add(new WhiteCard(whiteCardText));
			}
			
			String loadedName = deck.get("name").getAsString();
			final JsonDeck parsedDeck = new JsonDeck(loadedName,blackCards,whiteCards);
			decks.put(loadedName, parsedDeck);
		}
		/*for(File file:jsonFolder.listFiles()) {
			JsonDeck deck = new JsonDeck(file);
			try {
			deck.getName();
			deck.getDescription();
			deck.getBlackCards();
			deck.getWhiteCards();
			decks.put(deck.getName(),deck);
			Nah.plugin.getLogger().info("Loaded pack "+file.getName()+" Black Cards:"+deck.getBlackCards().size()+" White Cards:"+deck.getWhiteCards().size());
			} catch (Exception e) {
				Nah.plugin.getLogger().info("Error loading pack"+file.getName());
			}
		}*/
		
	}
	
	public LinkedHashMap<String,JsonDeck> getAvailablePacks(){
		return decks;
	}
	
	public JsonDeck getJsonDeck(String name) {
		return decks.get(name);
	}
	
	public JsonDeck loadJsonDeck(File file) {
		return new JsonDeck(file);
	}
}
