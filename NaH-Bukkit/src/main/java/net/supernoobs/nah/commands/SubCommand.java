package net.supernoobs.nah.commands;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {
	private String commandName;
	private String[] triggerAliases;
	private int triggerLength;
	protected boolean permissionRequired;
	
	public SubCommand(String command, String[] triggerAliases, int triggerLength) {
		this.commandName = command;
		this.triggerAliases = triggerAliases;
		this.triggerLength = triggerLength;
		this.permissionRequired = false;
	}
	
	public boolean isTriggered(String[] args) {
		String command = "";
		if(triggerLength <= args.length) {
			for(int index = 0; index < triggerLength; index++) {
				command += args[index];
			}
		}
		if(command.toLowerCase().equals(commandName)) {
			return true;
		}
		for(String alias:triggerAliases) {
			if(alias.equals((command.toLowerCase()))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean permissionRequired() {
		return permissionRequired;
	}
	
	public String permission(){
		return "nah."+commandName;
	}
	
	public boolean hasPermission(CommandSender sender){
		return sender.hasPermission(permission());
	}
	
	public String GetName(){
		return commandName;
	}
	
	public abstract String getUsage();
	
	public abstract boolean execute(CommandSender sender, String[] args);
}
