package net.supernoobs.nah.listeners;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.supernoobs.nah.Nah;
import net.supernoobs.nah.data.CardCastDeck;
import net.supernoobs.nah.game.Game;
import net.supernoobs.nah.game.GameSettings;
import net.supernoobs.nah.game.User;
import net.supernoobs.nah.game.User.MenuState;
import net.supernoobs.nah.game.cards.WhiteCard;
import net.supernoobs.nah.inventory.Buttons;
import net.supernoobs.nah.inventory.Cards;
import net.supernoobs.nah.inventory.Inventories;

public class InventoryListener implements Listener {
	@EventHandler
	public void inventoryClick(InventoryClickEvent event){
		String inventoryName = event.getInventory().getName();
		if(inventoryName != null) {
			if(!event.getSlotType().equals(SlotType.CONTAINER)){
				return;
			}
			if(inventoryName.equals(Inventories.nahPrefix+"§aPick a Card")) {
				event.setCancelled(true);
				int clickedCard = event.getSlot()-19;
				//Check if the clicked card is within our hand size
				if(0<=clickedCard&&clickedCard<7) {
					User user = Nah.plugin.userManager.getUser(event.getWhoClicked());
					user.playCard(clickedCard);
					return;
				}
			} else if(inventoryName.equals(Inventories.nahPrefix+"§bNoobs Against Humanity")) {
				event.setCancelled(true);
				User user = Nah.plugin.userManager.getUser(event.getWhoClicked());
				if(event.getSlotType() == SlotType.CONTAINER) {
					ItemStack current = event.getCurrentItem();
					if(current.equals(Buttons.BrowseGames())) {
						user.setMenuState(MenuState.BrowseGames);
						user.updateGUI();
						return;
					} else if(current.equals(Buttons.CreateNewGame())) {
						Nah.plugin.gameManager.createGame(user);
						return;
					} else if(current.equals(Buttons.SupportCaHButton())) {
						user.sendMessage("§bWebsite: http://cardsagainsthumanity.com");
						event.getWhoClicked().closeInventory();
						return;
					} else if(current.equals(Buttons.HelpMenuButton(user))) {
						user.getPlayer().performCommand("nah help");
					}
				}
			} else if(inventoryName.equals(Inventories.nahPrefix+ChatColor.DARK_AQUA+"Card Cast Decks")) {
				event.setCancelled(true);
				ItemStack clickedItem = event.getCurrentItem();
				User user = Nah.plugin.userManager.getUser(event.getWhoClicked());
				if(clickedItem.equals(Buttons.backToLobbyButton())){
					user.setMenuState(MenuState.Game);
					user.updateGUI();
					return;
				} else if(clickedItem.equals(Buttons.SettingsMenuButton(user))) {
					user.setMenuState(MenuState.Settings);
					user.updateGUI();
					return;
				} else if (user.isHost()){
					for(CardCastDeck deck:user.getGame().getSettings().getCastDecks()) {
						if(clickedItem.equals(Buttons.removeCardCastDeckButton(deck, user))){
							user.getGame().getSettings().getCastDecks().remove(deck);
							user.getGame().updatePlayerGUIs();
						}
					}
				}
			}
			else if(inventoryName.equals(Inventories.nahPrefix+"§eBrowse Games")) {
				if(event.getSlotType() == SlotType.CONTAINER) {
					event.setCancelled(true);
					User user = Nah.plugin.userManager.getUser(event.getWhoClicked());
					ItemStack clickedItem = event.getCurrentItem();
					if(clickedItem.hasItemMeta()) {
						if(clickedItem.getType().equals(Material.SKULL_ITEM)) {
							SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
							Game joinTarget = Nah.plugin.gameManager.getGame(meta.getOwner());
							joinTarget.joinGame(user);
							user.updateGUI();
							return;
						}
					}
					ItemStack stack = event.getCurrentItem();
					if(stack.equals(Buttons.BackToMainMenu())){
						user.setMenuState(MenuState.MainMenu);
						user.updateGUI();
						return;
					}
				}
			} else if(inventoryName.contains("- Lobby")) {
				User user = Nah.plugin.userManager.getUser(event.getWhoClicked());
				if(user.getGame() == null) {
					return;
				}
				if(event.getSlotType() == SlotType.CONTAINER){
					event.setCancelled(true);
					ItemStack item = event.getCurrentItem();
					if(item.equals(Buttons.LeaveGameButton())) {
						user.getGame().quitGame(user);
						return;
					}
					if (item.equals(Buttons.SettingsMenuButton(user))) {
						user.setMenuState(User.MenuState.Settings);
						user.updateGUI();
						return;
					}
					if(item.equals(Buttons.StartGameButton())) {
						if(user.isHost()) {
							user.getGame().start();
							return;
						}
					}
					if(user.isHost()) {
						if(item.hasItemMeta()) {
							ItemMeta meta = item.getItemMeta();
							if(meta.hasDisplayName()) {
								String playerName = meta.getDisplayName();
								playerName = ChatColor.stripColor(playerName);
								//We don't want to kick the player if the player is the player clicking
								if(playerName.equals(user.getName()))
									return;
								
								User kickUser = Nah.plugin.userManager.getUser(playerName);
								user.getGame().quitGame(kickUser);
								user.getGame().updatePlayerGUIs();
							}
						}
					}
				}
			} else if(inventoryName.equals(Inventories.nahPrefix+"§cCzar - Pick Winner")) {
				User user = Nah.plugin.userManager.getUser(event.getWhoClicked());
				if(event.getSlotType() == SlotType.CONTAINER) {
					event.setCancelled(true);
					int clickedSlot = event.getSlot() - 10;
					if(clickedSlot>-1&&clickedSlot<user.getGame().getPlayedCards().size()) {
						for(List<WhiteCard> testCard:user.getGame().getPlayedCards().values()){
							if(Cards.whiteCard(testCard).equals(event.getCurrentItem())) {
								user.getGame().czarPickWinner(testCard);
								return;
							}
						}
					}
				}
			} else if(inventoryName.equals(Inventories.nahPrefix+"§cCzar - Please Wait")) {
				event.setCancelled(true);
			} else if(inventoryName.equals(Inventories.nahPrefix+"§aPlease Wait While The Cezar Chooses")) {
				event.setCancelled(true);
			} else if(inventoryName.equals(Inventories.nahPrefix+"§eGame Settings")) {
				event.setCancelled(true);
				User user = Nah.plugin.userManager.getUser(event.getWhoClicked());
				if(event.getCurrentItem().equals(Buttons.increaseScoreLimitButton())) {
					GameSettings settings = user.getGame().getSettings();
					settings.setScoreLimit(settings.getScoreLimit()+ 1);
					user.updateGUI();
				} else if(event.getCurrentItem().equals(Buttons.decreaseScoreLimitButton())) {
					GameSettings settings = user.getGame().getSettings();
					settings.setScoreLimit(settings.getScoreLimit() - 1);
					user.updateGUI();
					return;
				} else if(event.getCurrentItem().equals(Buttons.decreaseRoundTimeButton())) {
					GameSettings settings = user.getGame().getSettings();
					if(!event.isShiftClick()) {
						settings.setRoundTime(settings.getRoundTime() - 10);
					} else {
						settings.setRoundTime(settings.getRoundTime() - 1);
					}
					user.updateGUI();
					return;
				} else if(event.getCurrentItem().equals(Buttons.cardCastMenuButton(user))){
					user.setMenuState(MenuState.CardCastSettings);
					user.updateGUI();
					return;
				} else if(event.getCurrentItem().equals(Buttons.increaseRoundTimeButton())) {
					GameSettings settings = user.getGame().getSettings();
					if(!event.isShiftClick()) {
						settings.setRoundTime(settings.getRoundTime() + 10);
					} else {
						settings.setRoundTime(settings.getRoundTime() + 1);
					}
					user.updateGUI();
					return;
				} else if(event.getCurrentItem().equals(Buttons.backToLobbyButton())) {
					user.setMenuState(MenuState.Game);
					user.updateGUI();
					return;
				} else if(event.getCurrentItem().equals(Buttons.deckSettingsButton(user))) {
					user.setMenuState(MenuState.DeckSettings);
					user.updateGUI();
					return;
				}
			} else if(inventoryName.equals(Inventories.nahPrefix+"§eGame Decks")) {
				event.setCancelled(true);
				User user = Nah.plugin.userManager.getUser(event.getWhoClicked());
				ItemStack currentItem = event.getCurrentItem();
				if(currentItem.equals(Buttons.backToLobbyButton())) {
					user.setMenuState(MenuState.Game);
					user.updateGUI();
					return;
				} else if(currentItem.equals(Buttons.SettingsMenuButton(user))) {
					user.setMenuState(MenuState.Settings);
					user.updateGUI();
					return;
				}
				if(user.isHost()){
					if(currentItem.equals(Buttons.nextPageButton())) {
						GameSettings settings = user.getGame().getSettings();
						settings.setCurrentDeckPage(settings.getCurrentDeckPage()+1);
						user.updateGUI();
					} else if(currentItem.equals(Buttons.previousPageButton())) {
						GameSettings settings = user.getGame().getSettings();
						settings.setCurrentDeckPage(settings.getCurrentDeckPage()-1);
						user.updateGUI();
					}
					else if(currentItem.hasItemMeta()) {
						if(currentItem.getItemMeta().hasDisplayName()) {
							String packName = ChatColor.stripColor(currentItem.getItemMeta().getDisplayName());
							user.getGame().getSettings().toggleDeck(packName);
							user.updateGUI();
							return;
						}
					}
				}
			} else if(inventoryName.contains(Inventories.nahPrefix)) {
				event.setCancelled(true);
			}
			//Check if it's canceled for any last button checks. If it's cancelled, it hit an inventory of ours.
			if(event.isCancelled()) {
				if(event.getSlotType().equals(SlotType.CONTAINER)) {
					if(event.getCurrentItem().equals(Buttons.LeaveGameButton())) {
						User user = Nah.plugin.userManager.getUser(event.getWhoClicked());
						user.getGame().quitGame(user);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		String inventoryName = event.getInventory().getName();
		
		if(inventoryName.contains(Inventories.nahPrefix)) {
			User user = Nah.plugin.userManager.getUser(event.getPlayer());
			user.playerClosedInventory();
		}
	}
}
