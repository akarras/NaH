package net.supernoobs.nah.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.supernoobs.nah.Nah;

public class JoinListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Nah.plugin.userManager.getUser(event.getPlayer().getName());
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Nah.plugin.userManager.removeUser(event.getPlayer().getName());
	}
}
