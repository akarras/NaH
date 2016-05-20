package net.supernoobs.nah;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import net.supernoobs.nah.Logger.LogLevel;

public class Settings {
	private int loggingLevel;
	private int maximumScoreLimit;
	public Settings() {
		//Check if the file exists or not
		//If not, we should save it from our resources
		File confFile = new File(Nah.plugin.getDataFolder()+File.separator+"config.yml");
		if(!confFile.exists()) {
			Nah.plugin.saveResource("config.yml", false);
			Nah.plugin.nahLogger.Log(LogLevel.NORMAL, "Saved default config");
		}
		
		FileConfiguration config = Nah.plugin.getConfig();
		loggingLevel = config.getInt("settings.log-level");
		maximumScoreLimit = config.getInt("settings.max-score-limit");
	}
	
	public int getLoggingLevel(){ return loggingLevel; }
	public int getMaxScoreLimit(){ return maximumScoreLimit; }
}
