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
	
	public QuickSelection(Stage stage) {
		selectionX = (Gdx.graphics.getWidth() - 15 - 196);
		selectionY = (Gdx.graphics.getHeight() - 15 - 64);
		selectionStick = new Joystick(stage, selectionX, selectionY, 64, 64, Constants.TouchpadType.ITEM_SELECTION);
		
		itemContainers = new ItemContainer[5];
		itemContainers[4] = new ItemContainer(stage, selectionX - 32 - 64, selectionY);
		itemContainers[3] = new ItemContainer(stage, selectionX - 8 - 64, selectionY - 8 - 64);
		itemContainers[2] = new ItemContainer(stage, selectionX, selectionY - 16 - 64);
		itemContainers[1] = new ItemContainer(stage, selectionX + 8 + 64, selectionY - 8 - 64);
		itemContainers[0] = new ItemContainer(stage, selectionX + 32 + 64, selectionY);
		
		currentSelection = new CurrentSelection(stage, selectionStick.getTouchpad().getX(), selectionStick.getTouchpad().getY());
	}
	
	public void selectItem() {
		if(selectionStick.getTouchpad().isTouched()) {
			if(!itemContainers[0].isActorVisible()) {
				for(int i = 0; i < itemContainers.length; i++) {
					itemContainers[i].show();
				}
			}
		} else {
			if(itemContainers[0].isActorVisible()) {
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
