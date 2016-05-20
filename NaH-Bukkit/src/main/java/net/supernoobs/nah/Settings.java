package net.supernoobs.nah;

import org.bukkit.configuration.file.FileConfiguration;

public class Settings {
	int loggingLevel;
	public Settings() {
		FileConfiguration config = Nah.plugin.getConfig();
		loggingLevel = config.getInt("settings.log-level");
	}
	
	public int getLoggingLevel(){ return loggingLevel; }
}
