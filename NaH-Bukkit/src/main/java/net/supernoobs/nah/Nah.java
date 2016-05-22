package net.supernoobs.nah;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import net.supernoobs.nah.commands.NahCommand;
import net.supernoobs.nah.data.CardCastService;
import net.supernoobs.nah.data.JsonDeckProvider;
import net.supernoobs.nah.game.GameManager;
import net.supernoobs.nah.game.UserManager;
import net.supernoobs.nah.listeners.InventoryListener;
import net.supernoobs.nah.listeners.JoinListener;

public class Nah extends JavaPlugin {
	public static Nah plugin;
	
	public GameManager gameManager;
	public UserManager userManager;
	
	public JsonDeckProvider jsonDecks;
	public CardCastService cardCast;
	
	public Logger nahLogger;
	public Settings settings;
	
	
	@Override
	public void onEnable() {
		plugin = this;
		settings = new Settings();
		
		gameManager = new GameManager();
		userManager = new UserManager();
		
		nahLogger = new Logger();
		
		jsonDecks = new JsonDeckProvider();
		cardCast = new CardCastService();
		
		plugin.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
		plugin.getServer().getPluginManager().registerEvents(new JoinListener(), this);
		
		PluginCommand command = getCommand("nah");
		command.setExecutor(new NahCommand());
	}
	
	@Override
	public void onDisable() {
		//Close player inventories
		userManager.closeMenus();
		Bukkit.getServer().getScheduler().cancelTasks(this);
	}
}
