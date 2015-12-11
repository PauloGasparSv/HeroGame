package paulogaspar.hero.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import paulogaspar.hero.maps.Platform;

public class King extends Actor {
	public final int IDLE = 0, WALK = 1,JUMPING = 2,CLIMBING = 3, ATTACKING = 4;
	
	//PRIMITIVES AND ARRAYS	
	public boolean grounded;
	private boolean pressing_jump;
	public boolean pressing_climb;
	
	public int state;
	public int sub_state;
	
	private float move_delta;
	private float climb_delta;
	private float scale_y;
	public float []speed;
	
	private long land_timer;
	private long pressing_timer;
	
	//ÐISPOSE
	private Texture sheet;
	
	//OBJETCTS
	private Animation walk_animation;
	private Animation run_animation;
	private Animation climb_animation;
	
	private TextureRegion idle_region;
	private TextureRegion current_region;
	private TextureRegion jump_prepare;
	private TextureRegion jump_up;
	private TextureRegion jump_down;
	private TextureRegion jump_land;
	
	
	private OrthographicCamera camera;
	
	private Controller gamepad;
	
	public King(Controller gamepad,OrthographicCamera camera){
		sheet = new Texture(Gdx.files.internal("characters.png"));
		
		this.camera = camera;
		
		//CREATING WALKING ANIMATION
		TextureRegion[] walk_region = new TextureRegion[4];
		for(int i = 0; i < 3; i++){
			walk_region[i] = new TextureRegion(sheet,32*(i+1),32,32,32);
		}
		walk_region[3] = new TextureRegion(sheet,0,32,32,32);
		walk_animation = new Animation(0.18f,walk_region);
		
		//CREATING RUNNING ANIMATION
		TextureRegion[] run_region = new TextureRegion[4];
		for(int i = 0; i < 4; i++){
			run_region[i] = new TextureRegion(sheet,32*(i+14),32,32,32);
		}
		run_animation = new Animation(0.11f,run_region);
		
		//CREATING CLIMB ANIMATION
		TextureRegion[]climb_region = new TextureRegion[4];
		for(int i = 0; i < 4; i++){
			climb_region[i] = new TextureRegion(sheet,(i+18)*32,32,32,32);
		}
		climb_animation = new Animation(0.1f,climb_region);
		
		
		idle_region = new TextureRegion(sheet,0,32,32,32);
		current_region = idle_region;
		
		jump_prepare = new TextureRegion(sheet,32*4,32,32,32);
		jump_up = new TextureRegion(sheet,32*5,32,32,32);
		jump_down = new TextureRegion(sheet,32*6,32,32,32);
		jump_land = new TextureRegion(sheet,32*7,32,32,32);
		
		this.gamepad = gamepad;
		init();
	}

	@Override
	public void init(){
		move_delta = 0;
		alive = true;
		position = new float[2];
		speed = new float[2];
		position[0] = 0;
		position[1] = 0;
		speed[0] = 0;
		speed[1] = 0;
		state = IDLE;
		sub_state = 0;
		facing_right = true;
		grounded = false;
		pressing_jump = false;
		land_timer = 0;
		pressing_timer = 0;
		scale_y = 1;
		climb_delta = 0;
	}
	
	@Override
	public void update(float delta){
		if(gamepad != null)gamepadControl();
		else keyboardControl();
		
		if(state == IDLE){
			move_delta  = 0;
			speed[0] = 0;
			
			if(speed[1] < 0){
				state = JUMPING;
				sub_state = 1;
			}
			
		}
		else if(state == WALK){
			move_delta += delta;	
			if(speed[1] < 0){
				state = JUMPING;
				sub_state = 1;
			}
		}
		else if(state == JUMPING){
			if(sub_state != 0 && sub_state != 3 && grounded){
				speed[1] = 0;
				sub_state = 3;
				land_timer = System.currentTimeMillis();
			}
			
			if(sub_state == 3){
				speed[0] = 0;
				if(System.currentTimeMillis() - land_timer > 200){
					state = IDLE;
				}
			}
			
			if(sub_state == 0){
				if(System.currentTimeMillis() - pressing_timer > 800 && System.currentTimeMillis() - pressing_timer < 1800){
					scale_y -= delta*0.1f;
					if(scale_y < 0.5f)scale_y = 0.5f;
				}
			}
			else scale_y = 1;
			
			if(sub_state == 0 && speed[1] > 0)sub_state = 1;
			if(sub_state == 1 && speed[1] < 0)sub_state = 2;
		}
		else if(state == CLIMBING){
			if(speed[1] > 0){
				position[1] += 140*delta;
				climb_delta += delta;
			}
			else if(speed[1] < 0){
				position[1] -= 140*delta;
				climb_delta -= delta;
				if(climb_delta < 0)climb_delta += 6000;
			}
		}
		
		if(!grounded){
			speed[1] -= delta * 10f;
			if(speed[1] < -8)speed[1] = -8;
			position[1] += speed[1]*delta*40f;
		}
		else{
			speed[1] = 0;
		}
		
			
		if(facing_right)position[0] += delta * speed[0];
		else position[0] -= delta * speed[0];
		if(position[0] < 0)position[0] = 0;
		
				
	}
	
	public void checkPlatform(Platform [] platforms,float delta){
		boolean g = false;
		if(state == CLIMBING){
			grounded = true;
			speed[1] = 0;
			return;
		}
		for(Platform p:platforms){
			if(p.active && rect().overlaps(p.rect)){
				if(position[0]+ 32 - speed[0]*delta < p.rect.x && facing_right){
					position[0] = p.rect.x - 36;
					speed[0] = -1;
				}
				else if(position[0] + 4 + speed[0]*delta > p.rect.x + p.rect.width && !facing_right){
					position[0] = p.rect.x + p.rect.width;
					speed[0] = 1;
				}
				else{
					position[1] = p.rect.y + p.rect.height-2;
					g = true;
				}
			}
		}
		grounded = g;
	}
	
	@Override
	public Rectangle rect(){
		if(facing_right)return new Rectangle(position[0]-2,position[1],38,64);
		else return new Rectangle(position[0]-4,position[1],38,64);
	}
		
	private void gamepadControl(){
		PovDirection direction = gamepad.getPov(0);
		float axis = gamepad.getAxis(0);
		float axisv = gamepad.getAxis(1);
		
		if(direction == PovDirection.west || direction == PovDirection.northWest || direction == PovDirection.southWest ||
				axis < -0.2f){
			facing_right = false;
			if(state  == IDLE)state = WALK;
			
			if(gamepad.getButton(3) && !pressing_jump){
				speed[0] = 220;
				if(state < JUMPING)sub_state = 1;
			}
			else if(!pressing_jump){
				if(state < JUMPING)sub_state = 0;
				speed[0] = 135;
			}
		}
		else if(direction == PovDirection.east || direction == PovDirection.northEast || direction == PovDirection.southEast || 
				axis > 0.2f){
			facing_right = true;
			if(state == IDLE)state = WALK;
			
			if(gamepad.getButton(3) && !pressing_jump){
				speed[0] = 220;
				if(state < JUMPING)sub_state = 1;
			}
			else if(!pressing_jump){
				if(state < JUMPING)sub_state = 0;
				speed[0] = 135;
			}
		}
		else{
			 if(state < JUMPING)state = IDLE;
			speed[0] = 0;
		}
		if(direction == PovDirection.north || axisv < -0.4f){
			pressing_climb = true;
		}
		if((direction == PovDirection.north || axisv < -0.4f) && state == CLIMBING){
			speed[1] = 1;
		}
		else if((direction == PovDirection.south || axisv > 0.4f)&& state == CLIMBING){
			speed[1] = -1;
		}
		else if(state == CLIMBING) speed[1] = 0;
		
		
		if(pressing_climb && (direction != PovDirection.north && axisv >-0.4f))pressing_climb = false;
		
		if(gamepad.getButton(2) && grounded && state < JUMPING){
			if(!pressing_jump){
				speed[0] = 0;
				pressing_timer = System.currentTimeMillis();
			}
			pressing_jump = true;
			state = JUMPING;
			sub_state = 0;
		}
		if(!gamepad.getButton(2) && pressing_jump){
			pressing_jump = false;
			state = JUMPING;
			sub_state = 1;
			if(System.currentTimeMillis() - pressing_timer < 1800)speed[1] = 5;
			else speed[1] = 10;
			pressing_timer = 0;
			grounded = false;
			position[1] += 5;
			scale_y = 0;
		}
		
	}
	
	
	
	private void keyboardControl(){
		
	}
	
	@Override
	public void draw(SpriteBatch batch){
		if(state == IDLE)current_region = idle_region;
		else if(state == WALK){
			if(sub_state == 0)current_region = walk_animation.getKeyFrame(move_delta,true);
			else if(sub_state == 1)current_region = run_animation.getKeyFrame(move_delta,true);
		}
		else if(state == JUMPING){
			if(sub_state == 0)current_region = jump_prepare;
			else if(sub_state == 1)current_region = jump_up;
			else if(sub_state == 2)current_region = jump_down;
			else if(sub_state == 3)current_region = jump_land;
		}
		else if(state == CLIMBING) current_region = climb_animation.getKeyFrame(climb_delta,true);
		
		if(current_region.isFlipX() == facing_right)current_region.flip(true, false);
		
		if(state == JUMPING)batch.setColor(1,1-2f*(1-scale_y),1-2f*(1-scale_y),1);
		batch.draw(current_region,position[0],position[1],16,0,32,32,3,3*scale_y,0);
		batch.setColor(Color.WHITE);
	}
	
	
	@Override
	public void dispose(){
		walk_animation = null;
		run_animation = null;
		
		idle_region = null;
		current_region = null;
		jump_prepare = null;
		jump_up = null;
		jump_down = null;
		jump_land = null;
		
		
		sheet.dispose();
	}
	
}
