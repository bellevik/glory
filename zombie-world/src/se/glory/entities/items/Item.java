package se.glory.entities.items;

import se.glory.utilities.Constants;

/*
 * This interface will be implemented by all items in the world.
 */
public interface Item {
	public Constants.ItemType getItemType();
	public String getItemName();
}
