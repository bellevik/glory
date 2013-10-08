package se.glory.zombieworld.model.entities.items;

import se.glory.zombieworld.utilities.ScreenCoordinates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ItemView {
	ItemContainer[] itemContainers;
	int cornerX = 224;
	int cornerY = 232;
	int currentSelection;
	
	public ItemView(Stage stage) {
		itemContainers = new ItemContainer[10];
		
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 2; j++) {
				itemContainers[(j*5)+i] = new ItemContainer(stage, cornerX + 72 * i, cornerY + 72 * j, false);
				itemContainers[(j*5)+i].show();
			}
		}
	}
	
	public void manageItems() {
		if(Gdx.input.isTouched()) {
			int currentX = ScreenCoordinates.getRealX(Gdx.input.getX());
			int currentY = ScreenCoordinates.getRealY(Gdx.input.getY());
			currentSelection = 10;
			
			for(int i = 0; i < itemContainers.length; i++) {
				if(currentX > itemContainers[i].getActor().getX() && currentX < itemContainers[i].getActor().getX() + 64
						&& currentY > itemContainers[i].getActor().getY() && currentY < itemContainers[i].getActor().getY() + 64) {
					currentSelection = i;
				}
			}
			System.out.println(currentSelection);
		}
		
	}
}
