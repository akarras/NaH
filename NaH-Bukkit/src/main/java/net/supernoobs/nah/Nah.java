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
				if(args.length > 0) {
					if(args[0].equalsIgnoreCase("create")) {
						User user = userManager.getUser((Player) sender);
						gameManager.createGame(user);
						sender.sendMessage("§aGame created!");
						return true;
					}
					else if(args[0].equalsIgnoreCase("join")) {
						if(args.length > 1) {
							User user = userManager.getUser((Player) sender);
							Game game = gameManager.getGame(args[1]);
							if(game == null) {
								sender.sendMessage(String.format("§4%s not found!",args[1]));
								return true;
							}
							game.joinGame(user);
							sender.sendMessage("§aSuccessful joined the game!");
							return true;
						}
						
					} else if(args[0].equalsIgnoreCase("leave")) {
						User user = userManager.getUser((Player) sender);
						user.getGame().quitGame(user);
						sender.sendMessage("§cLeft Game!");
						return true;
					} else if(args[0].equalsIgnoreCase("start")) {
						User user = userManager.getUser((Player) sender);
						if(user.isHost()) {
							user.getGame().start();
							sender.sendMessage("§aGame has started");
							return true;
						} else {
							sender.sendMessage("§cOnly the host may start the game");
							return true;
						}
					}
					else if(args[0].equalsIgnoreCase("printcards")) {
						User user = userManager.getUser((Player) sender);
						//sender.sendMessage(user.getGame().getWhiteCards().drawCard().getText());
						sender.sendMessage("Black:"+user.getGame().getCurrentBlackCard().getText());
						for(WhiteCard card:user.getHand()) {
							sender.sendMessage(card.getText());
						}
						return true;
					}
					
				} else {
					User user = userManager.getUser((Player) sender);
					user.updateGUI();
					return true;
				}
			}else {
				sender.sendMessage("§cThis command must be run from the console");
			}
		}
		
		return false;
		
	}
}
