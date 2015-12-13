package paulogaspar.hero.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
	public boolean interact_press;
	public boolean grounded;
	private boolean pressing_jump;
	public boolean wanna_climb;
	private boolean pressing_climb;
	private boolean pressing_attack;
	private boolean play_slash;
	private boolean slash_direction;
	
	public int state;
	public int sub_state;
	
	private float move_delta;
	private float climb_delta;
	private float attack_delta;
	private float slash_delta;
	private float scale_y;
	public float []speed;
	private float [] slash_position;
	
	private long land_timer;
	private long pressing_timer;
	
	//ÐISPOSE
	private Texture sheet;
	private Texture slash_sheet;
	
	//OBJETCTS
	private Animation walk_animation;
	private Animation run_animation;
	private Animation climb_animation;
	private Animation attack_animation;
	private Animation slash_animation;
	
	private TextureRegion idle_region;
	private TextureRegion current_region;
	private TextureRegion jump_prepare;
	private TextureRegion jump_up;
	private TextureRegion jump_down;
	private TextureRegion jump_land;
	
	
	//private OrthographicCamera camera;
	
	private Controller gamepad;
	
	public King(Controller gamepad,OrthographicCamera camera){
		sheet = new Texture(Gdx.files.internal("characters.png"));
		slash_sheet = new Texture(Gdx.files.internal("swoosh.png"));
		
		//this.camera = camera;
		
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
		
		//CREATING ATTACK ANIMATION
		TextureRegion[]atk_region = new TextureRegion[3];
		for(int i = 0; i < 3; i++){
			atk_region[i] = new TextureRegion(sheet,(i+10)*32,32,32,32);
		}
		attack_animation = new Animation(0.08f,atk_region);
		
		//CREATING SLASH ANIMATION
		TextureRegion[]slash_region = new TextureRegion[4];
		for(int i = 0; i < 4; i++){
			slash_region[i] = new TextureRegion(slash_sheet,i*32,0,32,32);
		}
		slash_animation = new Animation(0.06f,slash_region);
		
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
		wanna_climb = false;
		pressing_climb = false;
		pressing_attack = false;
		attack_delta = 0;
		play_slash = false;
		slash_delta = 0;
		slash_direction = true;
		slash_position = new float[2];
		slash_position[0] = 0;
		slash_position[1] = 0;
		interact_press = false;
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
				if(System.currentTimeMillis() - pressing_timer > 800 && 
						System.currentTimeMillis() - pressing_timer < 1800){
					scale_y -= delta*0.1f;
					if(scale_y < 0.5f)scale_y = 0.5f;
				}
			}
			else{
				scale_y = 1;
			}
			
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
		else if(state == ATTACKING){
			attack_delta += delta;
			if(attack_animation.isAnimationFinished(attack_delta)){
				state = IDLE;	
				attack_delta = 0;
			}
		}
		
		if(play_slash){
			slash_delta += delta;
			if(slash_animation.isAnimationFinished(slash_delta)){
				slash_delta = 0;
				play_slash = false;
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
			//speed[1] = 0;
			return;
		}
		for(Platform p:platforms){
			if(p.active && rect().overlaps(p.rect)){
				if(position[0]+ 32 - speed[0]*delta < p.rect.x && facing_right && p.type == 0){
					position[0] = p.rect.x - 36;
					speed[0] = -1;
				}
				else if(position[0] + 4 + speed[0]*delta > p.rect.x + p.rect.width && !facing_right&& p.type == 0){
					position[0] = p.rect.x + p.rect.width;
					speed[0] = 1;
				}
				else if(speed[1] <= 0 && position[1]+22 > p.rect.y + p.rect.height){
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
			
			if(gamepad.getButton(3) && !pressing_jump && state < ATTACKING){
				speed[0] = 220;
				if(state < JUMPING)sub_state = 1;
			}
			else if(!pressing_jump&& state < ATTACKING){
				if(state < JUMPING)sub_state = 0;
				speed[0] = 135;
			}
		}
		else if(direction == PovDirection.east || direction == PovDirection.northEast || direction == PovDirection.southEast || 
				axis > 0.2f){
			facing_right = true;
			if(state == IDLE)state = WALK;
			
			if(gamepad.getButton(3) && !pressing_jump&& state < ATTACKING){
				speed[0] = 220;
				if(state < JUMPING)sub_state = 1;
			}
			else if(!pressing_jump&& state < ATTACKING){
				if(state < JUMPING)sub_state = 0;
				speed[0] = 135;
			}
		}
		else{
			 if(state < JUMPING)state = IDLE;
			speed[0] = 0;
		}
		
		if(direction == PovDirection.north || axisv < -0.4f){
			interact_press = true;
		}
		else{
			interact_press = false;
		}
		
		if(direction == PovDirection.north || direction == PovDirection.south  || axisv < -0.4f || axisv > 0.4f){
			if(!pressing_climb){
				wanna_climb = true;
				pressing_climb = true;
			}
		}
		if((direction == PovDirection.north || axisv < -0.4f) && state == CLIMBING){
			speed[1] = 1;
		}
		else if((direction == PovDirection.south || axisv > 0.4f)&& state == CLIMBING){
			speed[1] = -1;
		}
		else if(state == CLIMBING) speed[1] = 0;
		
		
		if((direction != PovDirection.north && axisv >-0.4f && direction != PovDirection.south && axisv < 0.4f)){
			wanna_climb = false;
			pressing_climb = false;
		}
		
		if(gamepad.getButton(1) && grounded && state < JUMPING && !pressing_attack){
			pressing_attack = true;
			state = ATTACKING;
			sub_state = 0;
			attack_delta = 0;
			slash_delta = 0;
			play_slash = true;
			speed[0] = 0;
			if(facing_right){
				slash_direction = true;
				slash_position[0] = position[0] +36;
				slash_position[1] = position[1];
			}
			else{
				slash_direction = false;
				slash_position[0] = position[0]-36;
				slash_position[1] = position[1];
			}
		}
		if(!gamepad.getButton(1)){
			pressing_attack = false;
		}
		
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
			if(System.currentTimeMillis() - pressing_timer < 1800)speed[1] = 6.2f;
			else speed[1] = 10;
			pressing_timer = 0;
			grounded = false;
			position[1] += 5;
			scale_y = 0;
		}
		
	}
	
	
	
	private void keyboardControl(){
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			facing_right = false;
			if(state  == IDLE)state = WALK;
			
			if(Gdx.input.isKeyPressed(Input.Keys.Z) && !pressing_jump){
				speed[0] = 220;
				if(state < JUMPING)sub_state = 1;
			}
			else if(!pressing_jump){
				if(state < JUMPING)sub_state = 0;
				speed[0] = 135;
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			facing_right = true;
			if(state == IDLE)state = WALK;
			
			if(Gdx.input.isKeyPressed(Input.Keys.Z) && !pressing_jump){
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
		
		if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			if(!pressing_climb){
				wanna_climb = true;
				pressing_climb = true;
			}
		}
		if((Gdx.input.isKeyPressed(Input.Keys.UP)) && state == CLIMBING){
			speed[1] = 1;
		}
		else if((Gdx.input.isKeyPressed(Input.Keys.DOWN))&& state == CLIMBING){
			speed[1] = -1;
		}
		else if(state == CLIMBING) speed[1] = 0;
		
		
		if((!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN))){
			wanna_climb = false;
			pressing_climb = false;
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.X) && grounded && state < JUMPING){
			if(!pressing_jump){
				speed[0] = 0;
				pressing_timer = System.currentTimeMillis();
			}
			pressing_jump = true;
			state = JUMPING;
			sub_state = 0;
		}
		if(!Gdx.input.isKeyPressed(Input.Keys.X) && pressing_jump){
			pressing_jump = false;
			state = JUMPING;
			sub_state = 1;
			if(System.currentTimeMillis() - pressing_timer < 1800)speed[1] = 6.2f;
			else speed[1] = 10;
			pressing_timer = 0;
			grounded = false;
			position[1] += 5;
			scale_y = 0;
		}
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
		else if(state == ATTACKING)current_region = attack_animation.getKeyFrame(attack_delta,false);
		
		
		if(current_region.isFlipX() == facing_right)current_region.flip(true, false);
		
		if(state == JUMPING)batch.setColor(1,1-2f*(1-scale_y),1-2f*(1-scale_y),1);
		
		batch.draw(current_region,position[0],position[1],16,0,32,32,3,3*scale_y,0);
		batch.setColor(Color.WHITE);
		
		if(play_slash){
			current_region = slash_animation.getKeyFrame(slash_delta,false);
			if(current_region.isFlipX() == slash_direction)current_region.flip(true, false);
			batch.draw(current_region,slash_position[0],slash_position[1],16,0,32,32,2,2,0);
		}
		
	}
	
	
	@Override
	public void dispose(){
		walk_animation = null;
		run_animation = null;
		climb_animation = null;
		slash_animation = null;
		
		idle_region = null;
		current_region = null;
		jump_prepare = null;
		jump_up = null;
		jump_down = null;
		jump_land = null;
		
		slash_sheet.dispose();
		sheet.dispose();
	}
	
}
