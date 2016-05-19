package net.supernoobs.nah.inventory;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.supernoobs.nah.game.User;

public class Players {
	public static ItemStack lobbyPlayer(User user, boolean isHostViewer) {
		ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		SkullMeta meta = (SkullMeta) stack.getItemMeta();
		meta.setOwner(user.getName());
		meta.setDisplayName("§a"+user.getName());
		if(isHostViewer) {
			meta.setLore(Arrays.asList("§aClick to kick"));
		}
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack roundWinner(User user) {
		ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		SkullMeta meta = (SkullMeta) stack.getItemMeta();
		meta.setOwner(user.getName());
		meta.setDisplayName("§a"+user.getName());
		meta.setLore(Arrays.asList("§aWon the round!"));
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack gamePlayer(User user) {
		ArrayList<String> lore = new ArrayList<String>();
		if(user.isHost()) {
			lore.add("§aHost");
		}
		if(user.hasPlayed()) {
			lore.add("§aHas Played");
		} else {
			lore.add("§cHasn't played yet");
		}
		lore.add("Score "+user.getScore());
		
		ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		SkullMeta meta = (SkullMeta) stack.getItemMeta();
		meta.setOwner(user.getName());
		meta.setDisplayName("§a"+user.getName());
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack czarPlayer(User user) {
		ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		SkullMeta meta = (SkullMeta) stack.getItemMeta();
		meta.setOwner(user.getName());
		meta.setDisplayName("§cCzar "+user.getName());
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§cScore "+user.getScore());
		
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
}
