package net.supernoobs.nah.commands.subcommands;

import org.bukkit.command.CommandSender;

import net.supernoobs.nah.Logger;
import net.supernoobs.nah.Nah;
import net.supernoobs.nah.Settings;
import net.supernoobs.nah.commands.SubCommand;
import net.supernoobs.nah.data.CardCastService;
import net.supernoobs.nah.data.JsonDeckProvider;
import net.supernoobs.nah.game.GameManager;
import net.supernoobs.nah.game.UserManager;

public class ReloadCommand extends SubCommand {

	public ReloadCommand() {
		super("reload", new String[]{"r"}, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUsage() {
		return "§b/nah reload - Reloads the plugin";
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if(!hasPermission(sender)) {
			return true;
		}
		Nah.plugin.settings = new Settings();
		
		Nah.plugin.gameManager = new GameManager();
		Nah.plugin.userManager = new UserManager();
		
		Nah.plugin.nahLogger = new Logger();
		
		Nah.plugin.jsonDecks = new JsonDeckProvider();
		Nah.plugin.cardCast = new CardCastService();
		return true;
	}

}
