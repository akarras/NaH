package net.supernoobs.nah;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

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
		InputStream stream = Nah.plugin.getResource("messages.yml");
		InputStreamReader reader = new InputStreamReader(stream);
		YamlConfiguration defaultConfiguration = YamlConfiguration.loadConfiguration(reader);
		messagesYaml.addDefaults(defaultConfiguration);
		// Load button strings from the messages file
		BrowseGames = readString("buttons.browse-games");
		NewGameButton = readString("buttons.new-game");
		BackToMain = readString("buttons.back-to-main");
		LeaveGame = readString("buttons.leave-game");
		StartGame = readString("buttons.start-game");
		BackToLobby = readString("buttons.back-to-lobby");
		DeckSettings = readString("buttons.deck-settings");
		// Settings menu buttons
		Settings = readString("buttons.settings");
		IncreaseRoundTime = readString("buttons.settings-menu.increase-round-time");
		CurrentRoundTime = readString("buttons.settings-menu.current-round-time");
		DecreaseRoundTime = readString("buttons.settings-menu.decrease-round-time");
		IncreaseScoreLimit = readString("buttons.settings-menu.increase-score-limit");
		CurrentScoreLimit = readString("buttons.settings-menu.current-score-limit");
		DecreaseScoreLimit = readString("buttons.settings-menu.decrease-score-limit");
		
		// Load inventory strings from the messages file
		// TODO Actually add inventory strings
		
		
		// Resave our configuration file in-case the path was not found, the resaved values will then be able to be changed
		try {
			
			messagesYaml.save(messagesFile);
		} catch (Exception e) {
			
		}
	}
	
	// Helper method to translate color codes that may be used in the configuration file as we read
	public String readString(String path){
		//If the path is not found, we should save it from the default config to allow player modification
		if(!messagesYaml.contains(path, true)) {
			messagesYaml.set(path, messagesYaml.getString(path));
		}
		String message = messagesYaml.getString(path);
		message = ChatColor.translateAlternateColorCodes('&', message);
		message = ChatColor.translateAlternateColorCodes('&', message);
		return message;
	}
}
