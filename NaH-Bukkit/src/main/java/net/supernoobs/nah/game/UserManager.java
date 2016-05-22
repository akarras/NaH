package net.supernoobs.nah.game;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.HumanEntity;

import net.supernoobs.nah.game.User.MenuState;

public class UserManager {
	private Map<String,User> users;
	public UserManager() {
		users = new HashMap<String,User>();
	}
	
	public User getUser(String username) {
		if(!users.containsKey(username)) {
			User newUser = new User(username);
			users.put(username, newUser);
			return newUser;
		}
		return users.get(username);
	}
	
	public User getUser(HumanEntity humanEntity) {
		String username = humanEntity.getName();
		return getUser(username);
	}
	
	public void removeUser(String username) {
		if(users.containsKey(username)) {
			User user = users.get(username);
			if(user.isInGame()) {
				user.getGame().quitGame(user);
			}
			users.remove(username);
		}
	}
	
	public void closeMenus() {
		for(User user:users.values()){
			user.getPlayer().closeInventory();
		}
	}
	/*
	 * Triggers an update to with the current Menu State
	 */
	public void updateGuiForState(MenuState state) {
		for(User user:users.values()) {
			if(state == MenuState.BrowseGames) {
				user.updateGUI();
			}
		}
	}
}
