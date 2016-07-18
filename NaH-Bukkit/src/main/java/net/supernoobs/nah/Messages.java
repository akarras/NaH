package net.supernoobs.nah;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.md_5.bungee.api.ChatColor;

public class Messages {
	private FileConfiguration messagesYaml;
	// Button strings section
	public static String BrowseGames;
	public static String NewGameButton;
	public static String BackToMain;
	public static String LeaveGame;
	public static String StartGame;
	public static String BackToLobby;
	public static String DeckSettings;
	// Settings menu related strings
	public static String Settings;
	public static String IncreaseRoundTime;
	public static String CurrentRoundTime;
	public static String DecreaseRoundTime;
	public static String IncreaseScoreLimit;
	public static String CurrentScoreLimit;
	public static String DecreaseScoreLimit;
	// Inventory strings section
	public static String placeyMcPlaceholder;
	public Messages() {
		// Load the messages file
		File messagesFile = new File(Nah.plugin.getDataFolder()+File.separator+"messages.yml");
		if(!messagesFile.exists()) {
			Nah.plugin.saveResource("messages.yml", false);
		}
		messagesYaml = YamlConfiguration.loadConfiguration(messagesFile);
		// Load button strings from the messages file
		BrowseGames = readString("buttons.browse-games", "§eBrowse Games");
		NewGameButton = readString("buttons.new-game", "§aNew Game");
		BackToMain = readString("buttons.back-to-main", "§cBack to Main Menu");
		LeaveGame = readString("buttons.leave-game", "§cLeave Game");
		StartGame = readString("buttons.start-game", "§aStart Game");
		BackToLobby = readString("buttons.back-to-lobby", "§cBack to Lobby");
		DeckSettings = readString("buttons.deck-settings", "§6Deck Settings");
		// Settings menu buttons
		Settings = readString("buttons.settings", "§eSettings");
		IncreaseRoundTime = readString("buttons.settings-menu.increase-round-time","§eIncrease Round Time");
		CurrentRoundTime = readString("buttons.settings-menu.current-round-time", "§eIdle Time");
		DecreaseRoundTime = readString("buttons.settings-menu.decrease-round-time", "§6Decrease Round Time");
		IncreaseScoreLimit = readString("buttons.settings-menu.increase-score-limit", "§aIncrease Score Limit");
		CurrentScoreLimit = readString("buttons.settings-menu.current-score-limit", "§eCurrent Max Score");
		DecreaseScoreLimit = readString("buttons.settings-menu.decrease-score-limit", "§6Decrease Score Limit");
		
		// Load inventory strings from the messages file
		// TODO Actually add inventory strings
		
		
		// Resave our configuration file in-case the path was not found, the resaved values will then be able to be changed
		try {
			messagesYaml.save(messagesFile);
		} catch (Exception e) {
			
		}
	}
	
	// Helper method to translate color codes that may be used in the configuration file as we read
	public String readString(String path, String defaultValue){
		String message = messagesYaml.getString(path, defaultValue);
		message = ChatColor.translateAlternateColorCodes('§', message);
		message = ChatColor.translateAlternateColorCodes('&', message);
		return message;
	}
}
