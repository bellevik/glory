package se.glory.zombieworld.model.entities.items;

import se.glory.zombieworld.utilities.ScreenCoordinates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ItemView {
	private ItemContainer[] itemContainers;
	private int cornerX = 224;
	private int cornerY = 232;
	private int currentSelection;
	private CurrentSelection selection;
	private Image background;
	
	public ItemView(Stage stage) {
		itemContainers = new ItemContainer[10];
		
		background = new Image(new Texture(Gdx.files.internal("img/pausMenu.png")));
		stage.addActor(background);
		background.setVisible(false);
		
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 2; j++) {
				itemContainers[(j*5)+i] = new ItemContainer(stage, cornerX + 72 * i, cornerY + 72 * j, false);
				//itemContainers[(j*5)+i].show();
			}
		}
		
		currentSelection = 10;
		selection = new CurrentSelection(stage, itemContainers[0].getActor().getX(), itemContainers[0].getActor().getY(), false);
		//selection.show();
		
		Texture testTexture = new Texture(Gdx.files.internal("img/player.png"));
		Image testImage = new Image(testTexture);
		itemContainers[0].newItem(testImage);
		
		//itemContainers[0].show();
	}
	
	public void manageItems() {
		
		if(!itemContainers[0].isActorVisible()) {
			background.setVisible(true);
			for(int i = 0; i < itemContainers.length; i++) {
				itemContainers[i].show();
			}
		}
		
		
		if(Gdx.input.justTouched()) {
			int currentX = ScreenCoordinates.getRealX(Gdx.input.getX());
			int currentY = ScreenCoordinates.getRealY(Gdx.input.getY());
			currentSelection = 10;
			
			for(int i = 0; i < itemContainers.length; i++) {
				if(currentX > itemContainers[i].getActor().getX() && currentX < itemContainers[i].getActor().getX() + 64
						&& currentY > itemContainers[i].getActor().getY() && currentY < itemContainers[i].getActor().getY() + 64) {
					currentSelection = i;
				}
			}
		}
		
		if(currentSelection < 10) {
			selection.setActorPosition(itemContainers[currentSelection].getActor().getX(), itemContainers[currentSelection].getActor().getY());
			if(!selection.isActorVisible()) {
				selection.show();
			}

		} else {
			if(selection.isActorVisible()) {
				selection.hide();
			}
		}
		
	}
	
	public void hideContainers() {
		background.setVisible(false);
		if(itemContainers[0].isActorVisible()) {
			for(int i = 0; i < itemContainers.length; i++) {
				itemContainers[i].hide();
			}
		}
	}
}
