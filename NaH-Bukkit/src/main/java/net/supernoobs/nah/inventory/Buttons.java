package net.supernoobs.nah.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;
import net.supernoobs.nah.data.JsonDeck;
import net.supernoobs.nah.game.Game;
import net.supernoobs.nah.game.GameSettings;
import net.supernoobs.nah.game.User;

public class Buttons {
	public static ItemStack BrowseGames() {
		ItemStack stack = new ItemStack(Material.SLIME_BLOCK);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§eBrowse Games");
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack CreateNewGame() {
		ItemStack stack = new ItemStack(Material.WOOL,1,(short) 5);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§aNew Game");
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack BackToMainMenu() {
		ItemStack stack = new ItemStack(Material.REDSTONE_BLOCK,1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§cBack to Main Menu");
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack SupportCaHButton(){
		ItemStack stack = new ItemStack(Material.BEACON);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§bCards Against Humanity");
		meta.setLore(Arrays.asList("§bThis plugin wouldn't exist", "§bwithout them go buy a deck!",
				"§bhttp://cardsagainsthumanity.com"));
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack JoinGameButton(Game game) {
		ItemStack stack = new ItemStack(Material.SKULL_ITEM,1,(short)3);
		SkullMeta meta = (SkullMeta) stack.getItemMeta();
		meta.setOwner(game.getGameName());
		meta.setDisplayName("§a"+game.getGameName()+"'s game");
		game.getSettings();
		List<String> lore = new ArrayList<String>();
		lore.add("§2"+game.getPlayers().size()+"/"+GameSettings.MAXIMUM_PLAYERS+" Players");
		lore.add("§2Click to join!");
		if(StringUtils.isNotBlank(game.getSettings().getGamePassword())) {
			lore.add("§7Passworded");
		}
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack LeaveGameButton() {
		ItemStack stack = new ItemStack(Material.REDSTONE_BLOCK);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§cLeave Game");
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack StartGameButton() {
		ItemStack stack = new ItemStack(Material.WOOL,1,(short)5);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§aStart Game");
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack SettingsMenuButton(User user) {
		ItemStack stack = new ItemStack(Material.WOOL,1,(short)4);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§eSettings");
		Game game = user.getGame();
		if(game == null) {
			return null;
		}
		GameSettings settings = game.getSettings();
		List<String> lore = new ArrayList<String>();
		lore.add("Score Limit: "+settings.getScoreLimit());
		lore.add("Enabled Decks:");
		if(settings.getDecks().size() == 0) {
			lore.add("No decks enabled");
		}
		for(String deck:settings.getDecks()){
			lore.add(deck);
		}
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack increaseRoundTimeButton() {
		return getWoolWithName("§eIncrease Round Time",(short)5);
	}
	
	public static ItemStack currentRoundTimeButton(User user) {
		ItemStack stack = new ItemStack(Material.WOOL,1,(short)1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§eIdle Time");
		meta.setLore(Arrays.asList("§e"+user.getGame().getSettings().getRoundTime()));
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack decreaseRoundTimeButton() {
		return getWoolWithName("§6Decrease Round Time",(short)14);
	}
	
	public static ItemStack increaseScoreLimitButton() {
		return getWoolWithName("§aIncrease Score Limit",(short)5);
	}
	
	public static ItemStack currentScoreButton(User user) {
		ItemStack stack = new ItemStack(Material.WOOL,1,(short)1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§eCurrent Max Score");
		meta.setLore(Arrays.asList("§e"+user.getGame().getSettings().getScoreLimit()));
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack decreaseScoreLimitButton() {
		return getWoolWithName("§6Decrease Score Limit",(short)14);
	}
	
	/*
	 * 
	 */
	private static ItemStack getWoolWithName(String name, short woolColor){
		ItemStack stack = new ItemStack(Material.WOOL,1,woolColor);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack backToLobbyButton() {
		ItemStack stack = new ItemStack(Material.REDSTONE_BLOCK,1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§cBack to Lobby");
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack deckSettingsButton(User user) {
		ArrayList<String> lore = new ArrayList<String>();
		ItemStack stack = new ItemStack(Material.PAPER);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§6Deck Settings");
		final GameSettings settings = user.getGame().getSettings();
		lore.add(settings.getDecks().size()+ " Decks Enabled");
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack deckToggleButton(JsonDeck deck, User user) {
		ItemStack stack;
		ArrayList<String> lore = new ArrayList<String>();
		//If the deck is enabled, make the item stack green, if not red
		if(user.getGame().getSettings().isDeckEnabled(deck.getName())) {
			stack = new ItemStack(Material.WOOL,1,(short)5);
			lore.add("§aEnabled");
		} else {
			stack = new ItemStack(Material.WOOL,1,(short)14);
			lore.add("§cDisabled");
		}
		lore.addAll(multiLine(deck.getDescription(),15,ChatColor.GOLD));
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD+deck.getName());
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack nextPageButton() {
		ItemStack stack = new ItemStack(Material.ARROW);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§eNext Page");
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack previousPageButton() {
		ItemStack stack = new ItemStack(Material.ARROW);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§ePrevious Page");
		stack.setItemMeta(meta);
		return stack;
	}
	
	private static List<String> multiLine(String text, int length,ChatColor color) {
		text.replaceAll("_", "____");
		ArrayList<String> list = new ArrayList<String>();
		String currentText = color+"";
		for(String word:text.split("\\s+")) {
			if(currentText.length() < length) {
				currentText += word+ " ";
			} else {
				list.add(currentText);
				currentText = color+word+ " ";
			}
		}
		list.add(currentText);
		return list;
	}
}
