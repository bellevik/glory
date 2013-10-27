package se.glory.zombieworld.ui;

import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Constants.TouchpadType;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/*
 * This class will represent the joysticks available for movement of our player.
 * Libgdx give us a ready-to-use joystick that calculates what direction we move the
 * joystick. With this information we can give the player a speed and rotation.
 */
public class Joystick {
	
	private TouchpadStyle touchpadStyle;
	private Skin touchpadSkin;
	private Drawable touchBackground, touchKnob;
	private Touchpad touchpad;
	private Stage stage;
	
	private float x, y, width, height;
	
	public Joystick (Stage stage, float x, float y, float width, float height, Constants.TouchpadType type) {
		this.stage = stage;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		//Create a touchpad skin    
        touchpadSkin = new Skin();
        
        //Set Base- and Knob-image
        switch (type) {
        case MOVEMENT:			touchpadSkin.add("touchBackground", new Texture("img/joystickBase.png"));
								touchpadSkin.add("touchKnob", new Texture("img/joystickKnob.png"));
								break;
        case FIRE:				touchpadSkin.add("touchBackground", new Texture("img/joystickBase.png"));
								touchpadSkin.add("touchKnob", new Texture("img/joystickKnob.png"));
								break;
        case ITEM_SELECTION:	touchpadSkin.add("touchBackground", new Texture("img/selectionBase.png"));
        						touchpadSkin.add("touchKnob", new Texture("img/selectionKnob.png"));
        						break;
        }
        
        //Create TouchPad Style
        touchpadStyle = new TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(x, y, width, height);
 
        //Create a Stage and add TouchPad
        stage.addActor(touchpad);
	}

	public Touchpad getTouchpad() {
		return touchpad;
	}
	
	public void changeStickBackground(Texture texture) {
		touchpadSkin = new Skin();
		touchpadSkin.add("touchBackground", texture);
		touchpadSkin.add("touchKnob", new Texture("img/selectionKnob.png"));
		
		touchpadStyle = new TouchpadStyle();
		touchBackground = touchpadSkin.getDrawable("touchBackground");
		touchKnob = touchpadSkin.getDrawable("touchKnob");
		
		touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        
        stage.getRoot().removeActor(touchpad);
        touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setBounds(x, y, width, height);
        
        stage.addActor(touchpad);
	}
	
	public void removeStickBackground() {
		touchpadSkin = new Skin();
		touchpadSkin.add("touchBackground", new Texture("img/selectionBase.png"));
		touchpadSkin.add("touchKnob", new Texture("img/selectionKnob.png"));
		
		touchpadStyle = new TouchpadStyle();
		touchBackground = touchpadSkin.getDrawable("touchBackground");
		touchKnob = touchpadSkin.getDrawable("touchKnob");
		
		touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        
        stage.getRoot().removeActor(touchpad);
        touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setBounds(x, y, width, height);
		
        stage.addActor(touchpad);
	}
}