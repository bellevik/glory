package se.glory.zombieworld.model.entities.items;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class ItemView {
	ItemContainer[] itemContainers;
	
	public ItemView(Stage stage) {
		itemContainers = new ItemContainer[1];
		
		itemContainers[0] = new ItemContainer(stage, 200, 200, false);
		
		itemContainers[0].show();
		
	}
}
