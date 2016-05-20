package net.supernoobs.nah;

public class Logger {
	public void Log(LogLevel level, String message) {
		if(Nah.plugin.settings.getLoggingLevel() >= level.getLevel()) {
			Nah.plugin.getServer().getLogger().info(message);
		}
	}
	public enum LogLevel {
		CRITICAL(5),
		NORMAL(3),
		EVERYTHING(1);
		private int level;
		LogLevel(int level) { this.level = level; }
		public int getLevel() { return level; }
	}
}
