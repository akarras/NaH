package net.supernoobs.nah.commands.subcommands;

import org.bukkit.command.CommandSender;

import net.supernoobs.nah.Nah;
import net.supernoobs.nah.commands.SubCommand;
import net.supernoobs.nah.game.User;

public class GameCommand extends SubCommand {

	public GameCommand() {
		super("game", new String[]{"","g"}, 1);
	}

	@Override
	public String getUsage() {
		return "§b/nah (game) - Opens the game menu. (game) optional";
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		User user = Nah.plugin.userManager.getUser(sender.getName());
		user.playerRequestedOpen();
		user.updateGUI();
		return true;
	}

}
