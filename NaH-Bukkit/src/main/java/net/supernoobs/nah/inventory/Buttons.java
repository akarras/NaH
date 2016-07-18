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
import net.supernoobs.nah.Messages;
import net.supernoobs.nah.Nah;
import net.supernoobs.nah.data.CardCastDeck;
import net.supernoobs.nah.data.JsonDeck;
import net.supernoobs.nah.game.Game;
import net.supernoobs.nah.game.GameSettings;
import net.supernoobs.nah.game.User;

public class Buttons {
	public static ItemStack BrowseGames() {
		ItemStack stack = new ItemStack(Material.SLIME_BLOCK);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(Messages.BrowseGames);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack CreateNewGame() {
		ItemStack stack = new ItemStack(Material.WOOL,1,(short) 5);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(Messages.NewGameButton);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack BackToMainMenu() {
		ItemStack stack = new ItemStack(Material.REDSTONE_BLOCK,1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(Messages.BackToMain);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack SupportCaHButton(){
		ItemStack stack = new ItemStack(Material.BEACON);
		ItemMeta meta = stack.getItemMeta();
		// Removing this is disallowed, as attributing CaH for their game is a must.
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
		lore.add("§2"+game.getPlayers().size()+"/"+Nah.plugin.settings.getMaximumPlayers()+" Players");
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
		meta.setDisplayName(Messages.LeaveGame);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack StartGameButton() {
		ItemStack stack = new ItemStack(Material.WOOL,1,(short)5);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(Messages.StartGame);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack SettingsMenuButton(User user) {
		ItemStack stack = new ItemStack(Material.WOOL,1,(short)4);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(Messages.Settings);
		Game game = user.getGame();
		if(game == null) {
			return null;
		}
		GameSettings settings = game.getSettings();
		List<String> lore = new ArrayList<String>();
		lore.add("Score Limit: "+settings.getScoreLimit());
		lore.add("Idle Time: "+settings.getRoundTime());
		lore.add("Enabled Decks:");
		if(settings.getDecks().size()+settings.getCastDecks().size() == 0) {
			lore.add("No decks enabled");
		}
		for(String deck:settings.getDecks()){
			lore.add(deck);
		}
		for(CardCastDeck deck:settings.getCastDecks()) {
			lore.add(deck.getName());
		}
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack increaseRoundTimeButton() {
		return getWoolWithName(Messages.IncreaseRoundTime,(short)5);
	}
	
	public static ItemStack currentRoundTimeButton(User user) {
		ItemStack stack = new ItemStack(Material.WOOL,1,(short)1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(Messages.CurrentRoundTime);
		meta.setLore(Arrays.asList("§e"+user.getGame().getSettings().getRoundTime()+" Seconds",
				"§eShift+Click to change","§e1 second at a time."));
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack decreaseRoundTimeButton() {
		return getWoolWithName(Messages.DecreaseRoundTime,(short)14);
	}
	
	public static ItemStack increaseScoreLimitButton() {
		return getWoolWithName(Messages.IncreaseScoreLimit,(short)5);
	}
	
	public static ItemStack currentScoreButton(User user) {
		ItemStack stack = new ItemStack(Material.WOOL,1,(short)1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(Messages.CurrentScoreLimit);
		meta.setLore(Arrays.asList("§e"+user.getGame().getSettings().getScoreLimit()));
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack decreaseScoreLimitButton() {
		return getWoolWithName(Messages.DecreaseScoreLimit,(short)14);
	}
	

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
		meta.setDisplayName(Messages.BackToLobby);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack deckSettingsButton(User user) {
		ArrayList<String> lore = new ArrayList<String>();
		ItemStack stack = new ItemStack(Material.PAPER);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(Messages.DeckSettings);
		final GameSettings settings = user.getGame().getSettings();
		lore.add(settings.getDecks().size()+ " Decks Enabled");
		meta.setLore(lore);
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
	
	public static ItemStack removeCardCastDeckButton(CardCastDeck deck, User user) {
		ItemStack stack = new ItemStack(Material.WOOL,1,(short)5);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§a"+deck.getName());
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§cClick to remove");
		lore.addAll(multiLine(deck.getDescription(),20,ChatColor.RED));
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack cardCastMenuButton(User user) {
		ItemStack stack = new ItemStack(Material.WOOL,1,(short)7);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§3Edit CardCast");
		meta.setLore(Arrays.asList("§aAdd CardCast Decks with",
				"§a/nah cardcast <id>",
				"§aGet the id from cardcastgame.com"));
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack HelpMenuButton(User user) {
		ItemStack stack = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§bHelp");
		meta.setLore(Arrays.asList("§bClick to show help menu"));
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack PasswordInfoButton(User user) {
		ItemStack stack = new ItemStack(Material.WOOL, 1,(short)6);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§eGame Password");
		List<String> lore = new ArrayList<String>();
		GameSettings settings = user.getGame().getSettings();
		if(StringUtils.isBlank(settings.getGamePassword())){
			lore.add("§7Use the command");
			lore.add("§7/nah password [password]");
			lore.add("§7To add a password");
		} else {
			lore.add("§7Password: "+settings.getGamePassword());
			lore.add("§7Use /nah password clear");
			lore.add("§7To remove it");
		}
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
