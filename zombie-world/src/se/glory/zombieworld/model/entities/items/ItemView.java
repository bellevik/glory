package se.glory.zombieworld.model.entities.items;

import se.glory.zombieworld.model.StageModel;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.ScreenCoordinates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/*
 * Class containing elements shown only when the game is paused.
 * This view is where all managing of items will happen.
 */
public class ItemView {
	
	private ItemContainer[] itemContainers;
	/* The x- and y-coordinates of the bottom-left ItemContainer */
	private int cornerX = 224;
	private int cornerY = 232;
	/* The selected ItemContainer in the main inventory */
	private int currentSelection;
	/* The selected ItemContainer in the QuickSelection */
	private int currentQuickSelection;
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
	
	/*
	 * Repositions elements to fit different screen-sizes. 
	 */
	public void updatePosition() {
		background.setHeight(Gdx.graphics.getHeight());
	}
	
	/*
	 * This method is called every frame from the main render-method.
	 * It handles user-input to determine which ItemContainer's they tap.
	 * It is used for selecting and moving items between the main inventory
	 * and the QuickSelection.
	 */
	public void manageItems() {
		/* Check if the ItemContainer's already are visible to only show them is necessary */
		if(!itemContainers[0].isActorVisible()) {
			background.setVisible(true);
			for(int i = 0; i < itemContainers.length; i++) {
				itemContainers[i].show();
			}
		}
		
		if(Gdx.input.justTouched()) {
			int currentX = ScreenCoordinates.getRealX(Gdx.input.getX());
			int currentY = ScreenCoordinates.getRealY(Gdx.input.getY());
			
			/* Checks if there is an ItemContainer where the user tapped */
			for(int i = 0; i < itemContainers.length; i++) {
				if(currentX > itemContainers[i].getActor().getX() && currentX < itemContainers[i].getActor().getX() + 64
						&& currentY > itemContainers[i].getActor().getY() && currentY < itemContainers[i].getActor().getY() + 64) {
					currentSelection = i;
				}
			}
			
			currentQuickSelection = StageModel.quickSelection.touchedContainer(currentX, currentY);
			
			/*
			 * If both of the clicked ItemContainer's are tapped,
			 * the Item is moved from one to the other.
			 */
			if(currentSelection < 10 && currentQuickSelection < 5) {
				if(itemContainers[currentSelection].getItemImage() != null) {
					StageModel.quickSelection.newItem(currentQuickSelection, itemContainers[currentSelection].getItemImage());
					StageModel.quickSelection.getCurrentImage(currentQuickSelection).setVisible(true);
					itemContainers[currentSelection].deleteItemReference();
					currentSelection = 10;
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
	
	/*
	 * Method used for hiding the ItemContainer's the the game is unpaused.
	 */
	public void hideContainers() {
		background.setVisible(false);
		currentSelection = 10;
		selection.hide();
		if(itemContainers[0].isActorVisible()) {
			for(int i = 0; i < itemContainers.length; i++) {
				itemContainers[i].hide();
			}
		}
	}
}
