package net.supernoobs.nah.commands.subcommands;

import org.bukkit.command.CommandSender;

import net.supernoobs.nah.Nah;
import net.supernoobs.nah.commands.SubCommand;
import net.supernoobs.nah.game.User;

public class CardCastCommand extends SubCommand {

	public CardCastCommand() {
		super("cardcast", new String[]{"cc"}, 1);
		permissionRequired = true;
	}

	@Override
	public String getUsage() {
		return "§b/nah cardcast <code> - Add a CardCast deck to your game";
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if(args.length < 2) {
			sender.sendMessage("§cProper usage: /nah cardcast <code>");
			return true;
		}
		User user = Nah.plugin.userManager.getUser(sender.getName());
		if(user.isHost()) {
			user.getGame().getSettings().addCardCast(args[1]);
		} else {
			user.sendMessage("§cYou must be the host of a game to add a deck");
		}
		return true;
	}

}
