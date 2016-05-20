package net.supernoobs.nah;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.supernoobs.nah.data.JsonDeckProvider;
import net.supernoobs.nah.game.Game;
import net.supernoobs.nah.game.GameManager;
import net.supernoobs.nah.game.User;
import net.supernoobs.nah.game.UserManager;
import net.supernoobs.nah.game.cards.WhiteCard;
import net.supernoobs.nah.listeners.InventoryListener;

public class Nah extends JavaPlugin {
	public static Nah plugin;
	
	public GameManager gameManager;
	public UserManager userManager;
	
	public JsonDeckProvider jsonDecks;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		gameManager = new GameManager();
		userManager = new UserManager();
		
		jsonDecks = new JsonDeckProvider();
		
		plugin.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
		
	}
	
	@Override
	public void onDisable() {
		//Close player inventories
		userManager.closeMenus();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("nah")) {
			if(sender instanceof Player) {
				User user = userManager.getUser((Player) sender);
				user.updateGUI();
				return true;
			}else {
				sender.sendMessage("§cThis command must be run from the console");
			}
		}
		
		return false;
		
	}
}
