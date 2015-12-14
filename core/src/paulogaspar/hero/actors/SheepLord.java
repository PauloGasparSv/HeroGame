package paulogaspar.hero.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class SheepLord extends Actor{

	private TextureRegion current_frame;
	
	private boolean active;
	private boolean show_message;
	private boolean pressing;
	
	private float fly_delta;
	
	private OrthographicCamera camera;
	private King player;
	
	private long timer;
	
	private String []messages;
	private String current_message;
	private int current_message_i;
	private int current_char;
	private BitmapFont font;
	
	public SheepLord(TextureRegion frame,BitmapFont font,OrthographicCamera camera,King player,String []messages,float x,float y){
		this.current_frame = frame;
		position = new float[2];
		position[0] = x;
		position[1] = y;
		this.camera = camera;
		this.player = player;
		
		this.messages = messages;
		
		this.font = font;
		init();
	}
	
	public void init(){
		active = false;
		current_char = 0;
		current_message = "";
		facing_right = true;
		timer = 0;
		fly_delta = 0;
		show_message = false;
		current_message_i = -1;
		pressing = false;
	}
	
	public void update(float delta){
		if(position[0]-20*3 +50*3> camera.position.x - camera.viewportWidth/2 && position[0] < camera.position.x + camera.viewportWidth/2 && 
				position[1] > camera.position.y - 53*3- camera.viewportHeight/2 && position[1] < camera.position.y + camera.viewportHeight/2)active = true;
		else active = false;
	
		if(active){
			if(player.position[0] > position[0]+120)facing_right = true;
			else if(player.position[0] < position[0]-120)facing_right = false;
			fly_delta += delta*5f;
			
			if(player.interact_press &&!pressing&& rect().overlaps(player.rect())){
				show_message = true;
				pressing = true;
				timer = System.currentTimeMillis();
				if(current_message_i < messages.length-1){
					current_message_i++;
					current_message = "";
					current_char = 0;
				}
			}
			if(!player.interact_press && pressing)pressing = false;
			
			if(show_message){
				if(System.currentTimeMillis() - timer > 50 && current_char < messages[current_message_i].length()){
					timer = System.currentTimeMillis();
					current_message +=  messages[current_message_i].charAt(current_char);
					current_char++;
				}
			}
			
		}
		
	}
	
	public void draw(SpriteBatch batch){
		if(active){
			if(current_frame.isFlipX() == facing_right)current_frame.flip(true, false);
			batch.draw(current_frame,position[0],position[1]+(float)Math.sin(fly_delta)*10f,20,0,40,53,3,3,0);
			if(show_message){
				//System.out.println(current_message);
				font.draw(batch,current_message, position[0]-current_message.length()*5+8*3, position[1]+64*3+(float)Math.sin(fly_delta)*10f);
			}
		
		}
	}
	
	public Rectangle rect(){
		return new Rectangle(position[0]-100,position[1]+(float)Math.sin(fly_delta)*10f-80,40*3+120,53*3+80);
	}
	
	public void dispose(){
		
	}
	
}
