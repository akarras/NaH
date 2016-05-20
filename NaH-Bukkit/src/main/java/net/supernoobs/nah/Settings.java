package net.supernoobs.nah;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import net.supernoobs.nah.Logger.LogLevel;

public class Settings {
	int loggingLevel;
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
	}
	
	public int getLoggingLevel(){ return loggingLevel; }
}
