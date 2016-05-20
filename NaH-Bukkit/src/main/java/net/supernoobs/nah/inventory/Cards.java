package net.supernoobs.nah.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.supernoobs.nah.game.User;
import net.supernoobs.nah.game.cards.BlackCard;
import net.supernoobs.nah.game.cards.WhiteCard;

public class Cards {
	public static ItemStack whiteCard(WhiteCard card) {
		ItemStack stack = new ItemStack(Material.PAPER,1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("White Card");
		List<String> lore = multiLineCard(ChatColor.WHITE,card.getText());
		meta.setLore(lore);
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
			lore.addAll(multiLineCard(ChatColor.WHITE,cardNumber+" "+card.getText()));
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
		List<String> lore = multiLineCard(ChatColor.GRAY,card.getText());
		if(card.getPick() > 1) {lore.add(ChatColor.RED+"Pick "+card.getPick()); }
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack comboCard(User.WinningPair pair) {
		ItemStack stack = new ItemStack(Material.GOLD_INGOT);
		ItemMeta meta = stack.getItemMeta();
		//Insert our white card directly into the black, onto the blank spaces
		meta.setDisplayName(ChatColor.WHITE+"Combo"+ChatColor.GRAY+"Card");
		String blackText = pair.getBlack().getText();
		for(int current = 0; current<pair.getBlack().getPick(); current++) {
			String whiteCardText = ChatColor.WHITE+pair.getWhiteCards().get(current).getText();
			whiteCardText = whiteCardText.replaceAll("\\s", " "+ChatColor.WHITE);
			int blankIndex = blackText.indexOf("_");
			if(blankIndex > 0) {
				whiteCardText = whiteCardText.replaceAll("\\.", "");
				blackText = new StringBuilder(blackText).replace(blankIndex,blankIndex+1,whiteCardText).toString();
				
			} else {
				blackText += "\n"+whiteCardText;
			}
		}
		List<String> lore = multiLineCard(ChatColor.GRAY,blackText);
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
	
	private static List<String> multiLineCard(ChatColor loreColor,String text) {
		ArrayList<String> list = new ArrayList<String>();
		String currentText = "";
		for(String word:text.split("\\s+")) {
			if(currentText.length() < 20) {
				currentText += word+ " ";
			} else {
				currentText = currentText.replaceAll("_", ChatColor.UNDERLINE+"    "+ChatColor.RESET+loreColor);
				list.add(loreColor+currentText);
				currentText = word+ " ";
			}
		}
		currentText = currentText.replaceAll("_", ChatColor.UNDERLINE+"    "+ChatColor.RESET+loreColor);
		list.add(loreColor+currentText);
		return list;
	}
}
