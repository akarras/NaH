package net.supernoobs.nah.commands;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.supernoobs.nah.commands.subcommands.CardCastCommand;
import net.supernoobs.nah.commands.subcommands.GameCommand;
import net.supernoobs.nah.commands.subcommands.HelpCommand;
import net.supernoobs.nah.commands.subcommands.PasswordCommand;
import net.supernoobs.nah.commands.subcommands.ReloadCommand;

public class NahCommand implements CommandExecutor {
	private Set<SubCommand> subCommands;
	public NahCommand() {
		subCommands = new HashSet<SubCommand>();
		subCommands.add(new HelpCommand(this));
		subCommands.add(new PasswordCommand());
		subCommands.add(new GameCommand());
		subCommands.add(new CardCastCommand());
		subCommands.add(new ReloadCommand());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command commandExecuted, String label, String[] args) {
		for(SubCommand command:subCommands) {
			if(command.isTriggered(args)){
				//Check if the command requires a permission
				if(command.permissionRequired()) {
					if(command.hasPermission(sender)) {
						//Block the command as they don't have permission
						sender.sendMessage("§cYou lack permission to use this command.");
						return true;
					}
				}
				return command.execute(sender,args);
			}
		}
		return false;
	}
	
	public Set<SubCommand> getSubcommands(){
		return subCommands;
	}
}
