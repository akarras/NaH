package net.supernoobs.nah.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.supernoobs.nah.game.cards.BlackCard;
import net.supernoobs.nah.game.cards.WhiteCard;

public class Cards {
	public static ItemStack whiteCard(WhiteCard card) {
		ItemStack stack = new ItemStack(Material.PAPER,1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("White Card");
		meta.setLore(multiLineCard(card.getText()));
		stack.setItemMeta(meta);
		return stack;
	}
	public static ItemStack whiteCard(List<WhiteCard> cards) {
		if(cards.size() == 1) {
			return whiteCard(cards.get(0));
		}
		ItemStack stack = new ItemStack(Material.PAPER,1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(cards.size()+" White Cards");
		List<String> lore = new ArrayList<String>();
		int cardNumber = 1;
		for(WhiteCard card:cards) {
			lore.addAll(multiLineCard(cardNumber+" "+card.getText()));
			cardNumber++;
		}
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
	public static ItemStack blackCard(BlackCard card) {
		ItemStack stack = new ItemStack(Material.COAL_BLOCK);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§8Black Card");
		List<String> lore = multiLineCard(card.getText());
		if(card.getPick() > 1) {lore.add("Pick "+card.getPick()); }
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
	
	private static List<String> multiLineCard(String text) {
		text.replaceAll("(_)", "____");
		ArrayList<String> list = new ArrayList<String>();
		String currentText = "";
		for(String word:text.split("\\s+")) {
			if(currentText.length() < 15) {
				currentText += word+ " ";
			} else {
				list.add(currentText);
				currentText = word+ " ";
			}
		}
		list.add(currentText);
		return list;
	}
}
