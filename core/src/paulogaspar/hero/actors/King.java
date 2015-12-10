package paulogaspar.hero.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class King extends Actor {
	public final int IDLE = 0, WALK = 1;
	
	//PRIMITIVES AND ARRAYS
	public int state;
	public int sub_state;
	
	private float walk_delta; 
	
	//ÐISPOSE
	private Texture sheet;
	
	//OBJETCTS
	private Animation walk_animation;
	private Controller gamepad;
	private TextureRegion idle_region;
	private TextureRegion current_region;
	
	public King(Controller gamepad){
		sheet = new Texture(Gdx.files.internal("characters.png"));
		
		//CREATING WALK ANIMATION
		TextureRegion[] walk_region = new TextureRegion[4];
		for(int i = 0; i < 3; i++){
			walk_region[i] = new TextureRegion(sheet,32*(i+1),32,32,32);
		}
		walk_region[3] = new TextureRegion(sheet,0,32,32,32);
		walk_animation = new Animation(0.2f,walk_region);
		
		idle_region = new TextureRegion(sheet,0,32,32,32);
		current_region = idle_region;
		
		this.gamepad = gamepad;
		init();
	}

	@Override
	public void init(){
		walk_delta = 0;
		alive = true;
		position = new float[2];
		position[0] = 0;
		position[1] = 0;
		state = IDLE;
		sub_state = 0;
		facing_right = true;
	}
	
	@Override
	public void update(float delta){
		
		if(gamepad != null)gamepadControl();
		else keyboardControl();
		
		if(state == IDLE){
			walk_delta = 0;
			
		}
		else if(state == WALK){
			walk_delta += delta;
			
			if(facing_right)position[0] += delta * 120;
			else position[0] -= delta * 120;
			
		}
		
	}
	
	private void gamepadControl(){
		PovDirection direction = gamepad.getPov(0);
		if(direction == PovDirection.west || direction == PovDirection.northWest || direction == PovDirection.southWest){
			facing_right = false;
			state = WALK;
		}
		else if(direction == PovDirection.east || direction == PovDirection.northEast || direction == PovDirection.southEast){
			facing_right = true;
			state = WALK;
		}
		else{
			state = IDLE;
		}
		
	}
	
	private void keyboardControl(){
		
	}
	
	@Override
	public void draw(SpriteBatch batch){
		if(state == IDLE)current_region = idle_region;
		else if(state == WALK)current_region = walk_animation.getKeyFrame(walk_delta,true);
		
		if(current_region.isFlipX() == facing_right)current_region.flip(true, false);
		
		batch.draw(current_region,position[0],position[1],16,0,32,32,3,3,0);
	}
	
	@Override
	public Rectangle rect(){
		return new Rectangle(0,0,0,0);
	}
	
	@Override
	public void dispose(){
		walk_animation = null;
		sheet.dispose();
	}
	
}
