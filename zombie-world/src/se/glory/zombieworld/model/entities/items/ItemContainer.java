package se.glory.zombieworld.model.entities.items;

import se.glory.zombieworld.model.entities.weapons.EquippableItem;
import se.glory.zombieworld.utilities.ScreenCoordinates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

/*
 * Class that will contain an Item.
 * This is used for visually representing an Item in the GUI.
 */
public class ItemContainer {
	private Stage stage;
	private Texture texture;
	private Image background;
	//private Image currentItem;
	
	private EquippableItem item;
	
	private int delay = 0;
	private int delayMax = 30;
	
	public ItemContainer(Stage stage, float x, float y, boolean quick) {
		this.stage = stage;
		
		if(quick) {
			texture = new Texture(Gdx.files.internal("img/itemBase.png"));
		} else {
			texture = new Texture(Gdx.files.internal("img/itemBaseRSquare.png"));
		}
		
		background = new Image(texture);
		
		stage.addActor(background);
		background.setPosition(x, y);
		hide();
	}
	
	public Image getBackground() {
		return background;
	}
	
	public boolean isActorVisible() {
		return background.isVisible();
	}
	
	public float getX() {
		return background.getX();
	}
	
	public float getY() {
		return background.getY();
	}
	
	public EquippableItem getItem() {
		return item;
	}
	
	public void setX(float x) {
		background.setX(x);
		if(item != null) {
			item.getIcon().setX(x + 2);
		}
	}
	
	public void setY(float y) {
		background.setY(y);
		if(item != null) {
			item.getIcon().setY(y + 2);
		}
	}
	
	public void show() {
		background.setVisible(true);
		if(item != null) {
			item.getIcon().setPosition(background.getX(), background.getY());
			item.getIcon().setVisible(true);
		}
	}
	
	public void hide() {
		background.setVisible(false);
		if(item != null) {
			item.getIcon().setVisible(false);
		}
	}
	
	/*
	 * Adds a new Item to the ItemContainer.
	 * If there already is an Item there that one is removed
	 */
	public void newItem(EquippableItem item) {
		this.item = item;
		removeItem();
		stage.addActor(item.getIcon());
		item.getIcon().setPosition(background.getX(), background.getY());
		item.getIcon().setVisible(false);
	}
	
	public void removeItem() {
		Array<Actor> tempArray = stage.getActors();
		if(tempArray.contains(item.getIcon(), true)) {
			stage.getRoot().removeActor(item.getIcon());
		}
	}
	
	/*
	 * Just deletes the reference this ItemContainer has
	 * to it's former Image.
	 */
	public void deleteItemReference() {
		item = null;
	}
	
	/*
	 * Checks if the ItemContainer is touched.
	 */
	public boolean isTouched() {
		if(delay < delayMax) {
			delay += 1;
		}
		int currentX = ScreenCoordinates.getRealX(Gdx.input.getX());
		int currentY = ScreenCoordinates.getRealY(Gdx.input.getY());
		boolean isTouched = Gdx.input.justTouched();
		
		if(currentX > background.getX() && currentX < background.getX() + 32 && currentY > background.getY() && 
				currentY < background.getY() + 32 && isTouched) {
			delay = 0;
			return true;
		} else {
			return false;
		}
	}
}
