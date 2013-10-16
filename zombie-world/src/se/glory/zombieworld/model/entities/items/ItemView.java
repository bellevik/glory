package se.glory.zombieworld.model.entities.items;

import se.glory.zombieworld.model.StageModel;
import se.glory.zombieworld.model.entities.weapons.EquippableItem;
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
	private CurrentSelection quickSelection;
	private Image background;
	private int tempSelection;

	public ItemView(Stage stage) {
		itemContainers = new ItemContainer[10];

		background = new Image(new Texture(Gdx.files.internal("img/pausMenu.png")));
		stage.addActor(background);
		background.setVisible(false);

		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 2; j++) {
				itemContainers[(j*5)+i] = new ItemContainer(stage, cornerX + 72 * i, cornerY + 72 * j, true);
			}
		}

		currentSelection = 10;
		currentQuickSelection = 5;
		selection = new CurrentSelection(stage, itemContainers[0].getBackground().getX(), itemContainers[0].getBackground().getY(), true);
	}
	
	public int getNumberOfContainers() {
		return itemContainers.length;
	}
	
	/*
	 * Checks if an item exists in the list
	 */
	public boolean existsInList(EquippableItem item) {
		boolean exists = false;
		for(int i = 0; i < itemContainers.length; i++) {
			if(itemContainers[i].getItem() != null) {
				if(itemContainers[i].getItem().getItemName().equals(item.getItemName())) {
					exists = true;
				}
			}
		}
		return exists;
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
		
		/* Checks if the position of the Items are off and corrects them */
		/*if(itemContainers[0].getX() == itemContainers[0].getItem().getIcon().getX()) {
			for(int i = 0; i < itemContainers.length; i++) {
				itemContainers[i].setX(itemContainers[i].getX());
				itemContainers[i].setY(itemContainers[i].getY());
			}
		}*/
		
		/* Check if the ItemContainer's already are visible to only show them is necessary */
		if(!itemContainers[0].isActorVisible()) {
			background.setVisible(true);
			for(int i = 0; i < itemContainers.length; i++) {
				itemContainers[i].show();
			}
			for(int i = 0; i < itemContainers.length; i++) {
				itemContainers[i].setX(itemContainers[i].getX());
				itemContainers[i].setY(itemContainers[i].getY());
			}
		}

		if(Gdx.input.justTouched()) {
			int currentX = ScreenCoordinates.getRealX(Gdx.input.getX());
			int currentY = ScreenCoordinates.getRealY(Gdx.input.getY());
			boolean isOutside = true;

			/* Checks if there is an ItemContainer where the user tapped */
			for(int i = 0; i < itemContainers.length; i++) {
				if(currentX > itemContainers[i].getBackground().getX() && currentX < itemContainers[i].getBackground().getX() + 64
						&& currentY > itemContainers[i].getBackground().getY() && currentY < itemContainers[i].getBackground().getY() + 64) {
					currentSelection = i;
					isOutside = false;
				}
			}

			if(currentQuickSelection < 5) {
				tempSelection = currentQuickSelection;
			} else {
				tempSelection = 5;
			}

			currentQuickSelection = StageModel.quickSelection.touchedContainer(currentX, currentY);

			/* Deselects if you click outside any of the ItemContainer's */
			if(currentQuickSelection == 5 && isOutside == true) {
				currentSelection = 10;
			}

			/*
			 * If both of the clicked ItemContainer's are tapped,
			 * the Item is moved from one to the other.
			 */
			if(currentSelection < 10 && currentQuickSelection < 5) {
				if(itemContainers[currentSelection].getItem() != null && StageModel.quickSelection.getCurrentItem(currentQuickSelection) != null) {
					EquippableItem tempItem = StageModel.quickSelection.getCurrentItem(currentQuickSelection);
					StageModel.quickSelection.deleteItemReference(currentQuickSelection);
					StageModel.quickSelection.newItem(currentQuickSelection, itemContainers[currentSelection].getItem());
					StageModel.quickSelection.getCurrentItem(currentQuickSelection).getIcon().setVisible(true);
					itemContainers[currentSelection].deleteItemReference();
					itemContainers[currentSelection].newItem(tempItem);
					itemContainers[currentSelection].show();
				} else if(itemContainers[currentSelection].getItem() == null && StageModel.quickSelection.getCurrentItem(currentQuickSelection) != null) {
					itemContainers[currentSelection].newItem(StageModel.quickSelection.getCurrentItem(currentQuickSelection));
					StageModel.quickSelection.deleteItemReference(currentQuickSelection);
					itemContainers[currentSelection].show();
				} else if(itemContainers[currentSelection].getItem() != null && StageModel.quickSelection.getCurrentItem(currentQuickSelection) == null) {
					StageModel.quickSelection.newItem(currentQuickSelection, itemContainers[currentSelection].getItem());
					StageModel.quickSelection.getCurrentItem(currentQuickSelection).getIcon().setVisible(true);
					itemContainers[currentSelection].deleteItemReference();
				}
				
				itemContainers[currentSelection].setX(itemContainers[currentSelection].getX());
				itemContainers[currentSelection].setY(itemContainers[currentSelection].getY());

				currentSelection = 10;
				currentQuickSelection = 5;
			} else if(currentSelection < 10 && currentQuickSelection == 5 && tempSelection < 5) {
				if(itemContainers[currentSelection].getItem() == null && StageModel.quickSelection.getCurrentItem(tempSelection) != null) {
					itemContainers[currentSelection].newItem(StageModel.quickSelection.getCurrentItem(tempSelection));
					StageModel.quickSelection.deleteItemReference(tempSelection);
					itemContainers[currentSelection].show();
				} else if(itemContainers[currentSelection].getItem() != null && StageModel.quickSelection.getCurrentItem(tempSelection) != null){
					EquippableItem tempItem = StageModel.quickSelection.getCurrentItem(tempSelection);
					StageModel.quickSelection.deleteItemReference(tempSelection);
					StageModel.quickSelection.newItem(tempSelection, itemContainers[currentSelection].getItem());
					StageModel.quickSelection.getCurrentItem(tempSelection).getIcon().setVisible(true);
					itemContainers[currentSelection].deleteItemReference();
					itemContainers[currentSelection].newItem(tempItem);
					itemContainers[currentSelection].show();
				} else if(itemContainers[currentSelection].getItem() != null && StageModel.quickSelection.getCurrentItem(tempSelection) == null) {
					StageModel.quickSelection.newItem(tempSelection, itemContainers[currentSelection].getItem());
					itemContainers[currentSelection].deleteItemReference();
					StageModel.quickSelection.getCurrentItem(tempSelection).getIcon().setVisible(true);
				}
				
				itemContainers[currentSelection].setX(itemContainers[currentSelection].getX());
				itemContainers[currentSelection].setY(itemContainers[currentSelection].getY());
				
				currentSelection = 10;
				tempSelection = 5;

			} else if(currentSelection == 10 && currentQuickSelection < 5 && tempSelection < 5) {
				if(tempSelection != currentQuickSelection) {
					if(StageModel.quickSelection.getCurrentItem(currentQuickSelection) != null && StageModel.quickSelection.getCurrentItem(tempSelection) != null) {
						EquippableItem tempItem = StageModel.quickSelection.getCurrentItem(currentQuickSelection);
						StageModel.quickSelection.deleteItemReference(currentQuickSelection);
						StageModel.quickSelection.newItem(currentQuickSelection, StageModel.quickSelection.getCurrentItem(tempSelection));
						StageModel.quickSelection.getCurrentItem(currentQuickSelection).getIcon().setVisible(true);
						StageModel.quickSelection.deleteItemReference(tempSelection);
						StageModel.quickSelection.newItem(tempSelection, tempItem);
						StageModel.quickSelection.getCurrentItem(tempSelection).getIcon().setVisible(true);
					} else if(StageModel.quickSelection.getCurrentItem(currentQuickSelection) == null && StageModel.quickSelection.getCurrentItem(tempSelection) != null) {
						StageModel.quickSelection.newItem(currentQuickSelection, StageModel.quickSelection.getCurrentItem(tempSelection));
						StageModel.quickSelection.getCurrentItem(currentQuickSelection).getIcon().setVisible(true);
						StageModel.quickSelection.deleteItemReference(tempSelection);
					} else if(StageModel.quickSelection.getCurrentItem(currentQuickSelection) != null && StageModel.quickSelection.getCurrentItem(tempSelection) == null) {
						StageModel.quickSelection.newItem(tempSelection, StageModel.quickSelection.getCurrentItem(currentQuickSelection));
						StageModel.quickSelection.getCurrentItem(tempSelection).getIcon().setVisible(true);
						StageModel.quickSelection.deleteItemReference(currentQuickSelection);
					}
					tempSelection = 5;
					currentQuickSelection = 5;

				}
			}

		}

		quickSelection = StageModel.quickSelection.getSelector();
		if(currentQuickSelection < 5) {
			quickSelection.setActorPosition(StageModel.quickSelection.getItemContainer(currentQuickSelection).getBackground().getX(),
					StageModel.quickSelection.getItemContainer(currentQuickSelection).getBackground().getY());
			if(!quickSelection.isActorVisible()) {
				quickSelection.show();
			}
		} else {
			if(quickSelection.isActorVisible()) {
				quickSelection.hide();
			}
		}

		if(currentSelection < 10) {
			selection.setActorPosition(itemContainers[currentSelection].getBackground().getX(), itemContainers[currentSelection].getBackground().getY());
			if(!selection.isActorVisible()) {
				selection.show();
			}
			if(currentQuickSelection < 5 && itemContainers[currentSelection].getItem() == null) {
				currentSelection = 10;
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
