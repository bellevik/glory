package se.glory.zombieworld.model.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

/*
 * Class that will contain an item available for QuickSelection
 */
public class ItemContainer {
	
	private Stage stage;
	private Texture texture;
	private Image actor;
	private Image currentItem;
	
	public ItemContainer(Stage stage, float x, float y, boolean quick) {
		this.stage = stage;
		
		if(quick) {
			texture = new Texture(Gdx.files.internal("img/itemBase.png"));
		} else {
			texture = new Texture(Gdx.files.internal("img/itemBaseRSquare.png"));
		}
		
		actor = new Image(texture);
		
		stage.addActor(actor);
		actor.setPosition(x, y);
		hide();
	}
	
	public Image getActor() {
		return actor;
	}
	
	public boolean isActorVisible() {
		return actor.isVisible();
	}
	
	public float getActorX() {
		return actor.getX();
	}
	
	public float getActorY() {
		return actor.getY();
	}
	
	public void setActorX(float x) {
		actor.setX(x);
		if(currentItem != null) {
			currentItem.setX(x+16);
		}
	}
	
	public void setActorY(float y) {
		actor.setY(y);
		if(currentItem != null) {
			currentItem.setY(y+16);
		}
	}
	
	public void show() {
		actor.setVisible(true);
		if(currentItem != null) {
			currentItem.setVisible(true);
		}
	}
	
	public void hide() {
		actor.setVisible(false);
		if(currentItem != null) {
			currentItem.setVisible(false);
		}
	}
	
	public void newItem(Image image) {
		currentItem = image;
		removeItem();
		stage.addActor(currentItem);
		currentItem.setVisible(false);
	}
	
	public void removeItem() {
		Array<Actor> tempArray = stage.getActors();
		if(tempArray.contains(currentItem, true)) {
			stage.getRoot().removeActor(currentItem);
		}
	}
}
