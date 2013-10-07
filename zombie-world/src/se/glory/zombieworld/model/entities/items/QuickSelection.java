package se.glory.zombieworld.model.entities.items;

import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Joystick;
import se.glory.zombieworld.utilities.ScreenCoordinates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

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
		
		// Till Ekman: har ser du hur man lagger in nya bilder
		Texture testTexture = new Texture(Gdx.files.internal("img/player.png"));
		Texture testTexture2 = new Texture(Gdx.files.internal("img/zombie.png"));
		Image testImage = new Image(testTexture);
		Image testImage2 = new Image(testTexture2);
		
		// Lagger till en helt ny bild bara
		itemContainers[0].newItem(testImage);
		// Samma har
		itemContainers[1].newItem(testImage2);
		// Lagger bild2 pa plats 2 istallet
		itemContainers[2].newItem(testImage2);
		// Tar bort den forsta bilden
		itemContainers[0].removeItem();
		// Kor du appen nu ser du att det bara ar tredje som har en bild
		
		selectionStick = new Joystick(stage, selectionX, selectionY, 64, 64, Constants.TouchpadType.ITEM_SELECTION);

		currentSelection = new CurrentSelection(stage, selectionStick.getTouchpad().getX(), selectionStick.getTouchpad().getY());
	}
	
	public void updatePosition() {
		selectionY = (Constants.VIEWPORT_HEIGHT - 15 - 64);
		selectionStick.getTouchpad().setY(selectionY);
	}
	
	// Method gets called every render from GameScreen
	// This is used to show the current available items and also select them
	public void selectItem() {
		double vertical = Math.pow((selectionStick.getTouchpad().getX() - itemContainers[0].getActorX()), 2);
		double horizontal = Math.pow((selectionStick.getTouchpad().getY() - itemContainers[0].getActorY()), 2);
		
		// Shows and quickly moves the ItemContainers to the correct position
		if(selectionStick.getTouchpad().isTouched()) {
			// Only shows the ItemContainers that are hidden
			if(!itemContainers[0].isActorVisible()) {
				for(int i = 0; i < itemContainers.length; i++) {
					itemContainers[i].show();
				}
			}
			
			// Increases the distance between the selection-stick and the ItemContainers
			if(Math.abs(Math.sqrt(vertical + horizontal)) < 95) {
				distance += 16;
			}
			for(int i = 0; i < itemContainers.length; i++) {
				Double radians = Math.toRadians(360 - i * 45);
				itemContainers[i].setActorX((float)(selectionX + Math.cos(radians) * distance));
				itemContainers[i].setActorY((float)(selectionY + Math.sin(radians) * distance));
			}
			
		} else {
			
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
			// Only hides the ItemContainers that are visible
			if(itemContainers[0].isActorVisible() && distance < 16) {
				for(int i = 0; i < itemContainers.length; i++) {
					itemContainers[i].hide();
				}
			}
		}
		
		// Used to determine the fingers position relative to the selection-stick
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
				
				//Changes the equiped item of the player according to the position in the quickselection menu
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
