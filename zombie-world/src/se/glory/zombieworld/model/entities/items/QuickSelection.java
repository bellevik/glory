package se.glory.zombieworld.model.entities.items;

import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Joystick;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/*
 * Class representing the QuickSelection-menu.
 * This is where the user can select items quickly
 * without the need to pause the game.
 */
public class QuickSelection {

	private Joystick selectionStick;
	private float selectionX;
	private float selectionY;
	private ItemContainer[] itemContainers;
	private CurrentSelection currentSelection;
	private double distance = 0;
	private int selection;
	
	public QuickSelection(Stage stage) {
		selectionX = (Constants.VIEWPORT_WIDTH - 15 - 196);
		selectionY = (Constants.VIEWPORT_HEIGHT - 15 - 64);
		
		itemContainers = new ItemContainer[5];
		for(int i = 0; i < itemContainers.length; i++) {
			Double radians = Math.toRadians(360 - i * 45);
			itemContainers[i] = new ItemContainer(stage, (float)(selectionX + Math.cos(radians)), (float)(selectionY + Math.sin(radians)), true);
		}
		
		//itemContainers[0].newItem(new Image(new Texture(Gdx.files.internal("img/human.png"))));
		
		selectionStick = new Joystick(stage, selectionX, selectionY, 64, 64, Constants.TouchpadType.ITEM_SELECTION);

		currentSelection = new CurrentSelection(stage, selectionStick.getTouchpad().getX(), selectionStick.getTouchpad().getY(), true);
	}
	
	public CurrentSelection getSelector() {
		return currentSelection;
	}
	
	public ItemContainer getItemContainer(int index) {
		return itemContainers[index];
	}
	
	public void deleteItemReference(int index) {
		itemContainers[index].deleteItemReference();
	}
	
	public Image getCurrentImage(int index) {
		return itemContainers[index].getItemImage();
	}
	
	public void changeImage(int pos, Image image) {
		itemContainers[pos].newItem(image);
	}

	/*
	 * Repositions elements to fit different screen-sizes. 
	 */
	public void updatePosition() {
		selectionY = (Constants.VIEWPORT_HEIGHT - 15 - 64);
		selectionStick.getTouchpad().setY(selectionY);
	}
	
	/*
	 * Adds a new Item to the selected ItemContainer.
	 */
	public void newItem(int index, Image image) {
		itemContainers[index].newItem(image);
		//System.out.println(itemContainers[0].isTouched());
	}
	
	/*
	 * Loops through all it's ItemContainers to determine which
	 * one is tapped.
	 */
	public int touchedContainer(float x, float y) {
		int touched = 5;
		for(int i = 0; i < itemContainers.length; i++) {
			if(x > itemContainers[i].getActor().getX() && x < itemContainers[i].getActor().getX() + 64
					&& y > itemContainers[i].getActor().getY() && y < itemContainers[i].getActor().getY() + 64) {
				touched = i;
			}
		}
		return touched;
	}
	
	// Method gets called every render from GameScreen
	// This is used to show the current available items and also select them
	/*
	 * This method gets called every frame from the main render-method.
	 * It handles user-input to determine if the QuickSelection-stick is used.
	 * With information on how the selectionStick is touched and dragged
	 * it is determined which ItemContainer to select.
	 */
	public void selectItem() {
		double vertical = Math.pow((selectionStick.getTouchpad().getX() - itemContainers[0].getActorX()), 2);
		double horizontal = Math.pow((selectionStick.getTouchpad().getY() - itemContainers[0].getActorY()), 2);
		
		/* Shows and quickly moves the ItemContainers to the correct position */
		if(selectionStick.getTouchpad().isTouched()) {
			// Only shows the ItemContainers that are hidden
			if(!itemContainers[0].isActorVisible()) {
				for(int i = 0; i < itemContainers.length; i++) {
					itemContainers[i].show();
				}
			}
			
			/* Increases the distance between the selection-stick and the ItemContainers */
			if(Math.abs(Math.sqrt(vertical + horizontal)) < 95) {
				distance += 16;
			}
			for(int i = 0; i < itemContainers.length; i++) {
				Double radians = Math.toRadians(360 - i * 45);
				itemContainers[i].setActorX((float)(selectionX + Math.cos(radians) * distance));
				itemContainers[i].setActorY((float)(selectionY + Math.sin(radians) * distance));
			}
			
		} else {
			
			/* Decreses the distance between the selection-stick and the ItemContainers */
			if(Math.abs(Math.sqrt(vertical + horizontal)) > 16) {
				distance -= 16;
			} else {
				distance = 0;
			}
			for(int i = 0; i < itemContainers.length; i++) {
				Double radians = Math.toRadians(360 - i * 45);
				itemContainers[i].setActorX((float)(selectionX + Math.cos(radians) * distance));
				itemContainers[i].setActorY((float)(selectionY + Math.sin(radians) * distance));
			}
			/* Only hides the ItemContainers that are visible */
			if(itemContainers[0].isActorVisible() && distance < 16) {
				for(int i = 0; i < itemContainers.length; i++) {
					itemContainers[i].hide();
				}
			}
		}
		
		/* Used to determine the fingers position relative to the selection-stick */
		if(selectionStick.getTouchpad().getKnobPercentX() != 0 || selectionStick.getTouchpad().getKnobPercentY() != 0) {
			float knobX = selectionStick.getTouchpad().getKnobPercentX();
			float knobY = selectionStick.getTouchpad().getKnobPercentY();
			
			float knobDegree;
			selection = 5;
			
			if (knobY >= 0) {
				knobDegree = -(int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
			} else {
				knobDegree = (int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
			}
			
			if((knobDegree <= 0 && knobDegree > -30) || (knobDegree <= -150 && knobDegree > -180)) {
				knobDegree = Math.abs(knobDegree);
			}
			
			if (knobDegree >= 0 && knobDegree < 180) {
				selection = (int) (knobDegree / 36);
				
				/* Changes the equipped item of the player according to the position in the quickselection menu */
				WorldModel.player.changeEquippedItem(selection);
				
				currentSelection.setActorPosition(itemContainers[selection].getActorX(), itemContainers[selection].getActorY());
				
				if(!currentSelection.isActorVisible()) {
					currentSelection.show();
				}
			} else {
				selection = 5;
				if(currentSelection.isActorVisible()) {
					currentSelection.hide();
				}
			}
		} else {
			if(currentSelection.isActorVisible()) {
				currentSelection.hide();
			}
		}
	}
	
	/*
	 * This method is used when the game is paused.
	 * While in this 'mode' all ItemContainers are always visible
	 * to make managing the Items easier.
	 */
	public void manageItems() {
		double vertical = Math.pow((selectionStick.getTouchpad().getX() - itemContainers[0].getActorX()), 2);
		double horizontal = Math.pow((selectionStick.getTouchpad().getY() - itemContainers[0].getActorY()), 2);
		
		if(Math.abs(Math.sqrt(vertical + horizontal)) < 95) {
			for(int i = 0; i < itemContainers.length; i++) {
				Double radians = Math.toRadians(360 - i * 45);
				itemContainers[i].setActorX((float)(selectionX + Math.cos(radians) * 96));
				itemContainers[i].setActorY((float)(selectionY + Math.sin(radians) * 96));
				itemContainers[i].show();
			}
		}
		
	}
}
