package se.glory.zombieworld.model.entities.items;

import se.glory.zombieworld.utilities.Constants;

/*
 * This interface will be implemented by all items in the world.
 */
public interface Item {
	public Constants.ItemType getItemType();
	public String getItemName();
}
