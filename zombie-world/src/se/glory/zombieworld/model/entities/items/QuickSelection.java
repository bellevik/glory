package se.glory.zombieworld.model.entities.items;

import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Joystick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class QuickSelection {

	private Joystick selectionStick;
	private float selectionX;
	private float selectionY;
	private ItemContainer[] itemContainers;
	private CurrentSelection currentSelection;
	private double distance = 0;
	
	public QuickSelection(Stage stage) {
		selectionX = (Constants.VIEWPORT_WIDTH - 15 - 196);
		selectionY = (Constants.VIEWPORT_HEIGHT - 15 - 64);
		
		itemContainers = new ItemContainer[5];
		for(int i = 0; i < itemContainers.length; i++) {
			Double radians = Math.toRadians(360 - i * 45);
			//itemContainers[i] = new ItemContainer(stage, (float)(selectionX + Math.cos(radians) * 96), (float)(selectionY + Math.sin(radians) * 96));
			itemContainers[i] = new ItemContainer(stage, (float)(selectionX + Math.cos(radians)), (float)(selectionY + Math.sin(radians)));
		}
		
		selectionStick = new Joystick(stage, selectionX, selectionY, 64, 64, Constants.TouchpadType.ITEM_SELECTION);

		currentSelection = new CurrentSelection(stage, selectionStick.getTouchpad().getX(), selectionStick.getTouchpad().getY());
	}
	
	public void updatePosition() {
		selectionY = (Constants.VIEWPORT_HEIGHT - 15 - 64);
		
		//selectionStick.getTouchpad().setY(selectionY);
	}
	
	public void selectItem() {
		double vertical = Math.pow((selectionStick.getTouchpad().getX() - itemContainers[0].getActorX()), 2);
		double horizontal = Math.pow((selectionStick.getTouchpad().getY() - itemContainers[0].getActorY()), 2);
		
		if(selectionStick.getTouchpad().isTouched()) {
			if(!itemContainers[0].isActorVisible()) {
				for(int i = 0; i < itemContainers.length; i++) {
					itemContainers[i].show();
				}
			}
			
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
			
			if(itemContainers[0].isActorVisible() && distance < 16) {
				for(int i = 0; i < itemContainers.length; i++) {
					itemContainers[i].hide();
				}
			}
		}
		
		if(selectionStick.getTouchpad().getKnobPercentX() != 0 || selectionStick.getTouchpad().getKnobPercentY() != 0) {
			float knobX = selectionStick.getTouchpad().getKnobPercentX();
			float knobY = selectionStick.getTouchpad().getKnobPercentY();
			
			float knobDegree;
			int selection = 5;
			
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
}
