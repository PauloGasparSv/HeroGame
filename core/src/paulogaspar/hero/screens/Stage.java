package paulogaspar.hero.screens;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Stage {
	
	public int id;
	
	public boolean next_stage;
	
	protected SpriteBatch batch;
	protected OrthographicCamera camera;
	protected Controller gamepad;
	
	public void init(){}
	public void update(float delta){}
	public void draw(){}
	
	public void initController(){
		gamepad = null;
		Array<Controller> controllers = Controllers.getControllers();
		if(controllers.size > 0)gamepad = controllers.first();
	}
	
	
}
