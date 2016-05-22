package net.supernoobs.nah.commands.subcommands;

import org.bukkit.command.CommandSender;

import net.supernoobs.nah.Nah;
import net.supernoobs.nah.commands.SubCommand;
import net.supernoobs.nah.game.User;

public class PasswordCommand extends SubCommand {

	public PasswordCommand() {
		super("password", new String[]{"p"},1);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if(args.length<2) {
			sender.sendMessage("§bPlease enter a password");
			sender.sendMessage(getUsage());
			return true;
		}
		String password = args[1];
		User user = Nah.plugin.userManager.getUser(sender.getName());
		//We set the game password if the user is already hosting the game
		if(user.isInGame()) {
			if(user.isHost()) {
				user.getGame().getSettings().setGamePassword(password);
				user.sendMessage("§bGame password set to: "+password);
			}
		} else {
			user.setPassword(password);
			user.sendMessage("§bPassword changed to "+password);
		}
		return true;
	}

	@Override
	public String getUsage() {
		return "§b/nah password <password> - Sets your password";
	}

}
