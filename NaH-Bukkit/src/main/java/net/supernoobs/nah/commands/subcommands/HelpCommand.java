package net.supernoobs.nah.commands.subcommands;

import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;
import net.supernoobs.nah.commands.NahCommand;
import net.supernoobs.nah.commands.SubCommand;

public class HelpCommand extends SubCommand {
	NahCommand nahCommand;
	public HelpCommand(NahCommand nah) {
		super("help", new String[]{"h"}, 1);
		nahCommand = nah;
	}

	@Override
	public String getUsage() {
		return ChatColor.AQUA+"/nah help - Shows this page!";
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		sender.sendMessage("§a§m       §a(§bNoobs Against Humanity§a-§bHelp§a)§a§m       ");
		for(SubCommand command:nahCommand.getSubcommands()) {
			sender.sendMessage(command.getUsage());
		}
		return true;
	}

}
