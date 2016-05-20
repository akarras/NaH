package net.supernoobs.nah;

import java.io.File;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Settings {
	private int loggingLevel;
	private int maximumScoreLimit;
	private String defaultSet;
	
	private int maxIdleTime;
	private int defaultIdleTime;
	private int minimumIdleTime;
	
	private int minimumPlayerCount;
	private int maximumPlayerCount;
	
	public Settings() {
		//Check if the file exists or not
		//If not, we should save it from our resources
		File confFile = new File(Nah.plugin.getDataFolder()+File.separator+"config.yml");
		if(!confFile.exists()) {
			Nah.plugin.saveResource("config.yml", false);
		}
		
		FileConfiguration config = Nah.plugin.getConfig();
		loggingLevel = config.getInt("settings.log-level");
		maximumScoreLimit = config.getInt("settings.max-score-limit");
		defaultSet = config.getString("settings.default-set");
		
		ConfigurationSection idleTime = config.getConfigurationSection("settings.idle-timer");
		maxIdleTime = idleTime.getInt("maximum");
		defaultIdleTime = idleTime.getInt("default");
		minimumIdleTime = idleTime.getInt("minimum");
		
		maximumPlayerCount = config.getInt("settings.player-count.maximum");
		minimumPlayerCount = config.getInt("settings.player-count.minimum");
	}
	public int getLoggingLevel(){ return loggingLevel; }
	public int getMaxScoreLimit(){ return maximumScoreLimit; }
	public String getDefaultSet(){ return defaultSet; }
	
	public int getMaxIdleTime() { return maxIdleTime; }
	public int getDefaultIdleTime() { return defaultIdleTime; }
	public int getMinimumIdleTime() { return minimumIdleTime; }
	
	public int getMinumumPlayers() { return minimumPlayerCount; }
	public int getMaximumPlayers() { return maximumPlayerCount; }
}
