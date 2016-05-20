package net.supernoobs.nah;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.supernoobs.nah.data.CardCastService;
import net.supernoobs.nah.data.JsonDeckProvider;
import net.supernoobs.nah.game.GameManager;
import net.supernoobs.nah.game.User;
import net.supernoobs.nah.game.UserManager;
import net.supernoobs.nah.listeners.InventoryListener;

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
		
	}
	
	@Override
	public void onDisable() {
		//Close player inventories
		userManager.closeMenus();
		Bukkit.getServer().getScheduler().cancelTasks(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("nah")) {
			if(sender instanceof Player) {
				User user = userManager.getUser((Player) sender);
				user.playerRequestedOpen();
				user.updateGUI();
				if(args.length > 0) {
					if(args[0].equals("set")) {
						if(!user.isHost()) {
							sender.sendMessage("§cYou must be the host to run this command");
							return true;
						}
						if(args.length < 3) {
							sender.sendMessage("§aPlease use: /nah set password [password]");
							return true;
						}
						if(args[1].equals("password")) {
							user.getGame().getSettings().setGamePassword(args[2]);
							return true;
						}
						sender.sendMessage("§aUnknown set argument");
					} else if (args[0].equals("password")) {
						user.setPassword(args[1]);
						return true;
					} else if (args[0].equals("cardcast")) {
						if(user.isHost()) {
							user.getGame().getSettings().addCardCast(args[1]);
							user.getGame().updatePlayerGUIs();
						}
					}
				}
				
				return true;
			}else {
				sender.sendMessage("§cThis command cannot be run from the console.");
			}
		}
		
		return false;
		
	}
}
