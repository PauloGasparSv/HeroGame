package paulogaspar.hero.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import paulogaspar.hero.actors.King;

public class Ladder {

	private TextureRegion[] regions;
	private Rectangle rect;
	private int height;
	boolean active;
	boolean holding_player;
	
	public Ladder(Texture txt, float x, float y,int height){
		rect = new Rectangle(x,y,64,height*64);
		
		active = false;
		this.height = height;
		
		regions = new TextureRegion[3];
		regions[0] = new TextureRegion(txt,112,6*16,16,16);
		regions[1] = new TextureRegion(txt,112,5*16,16,16);
		regions[2] = new TextureRegion(txt,112,4*16,16,16);

	}
	
	public void update(OrthographicCamera camera, King player){
		if(rect.x + rect.width> camera.position.x - camera.viewportWidth/2 && rect.x < camera.position.x + camera.viewportWidth/2 && 
				rect.y > camera.position.y - height*64 - camera.viewportHeight/2 && rect.y < camera.position.y + camera.viewportHeight/2)active = true;
		else active = false;
		
		if(active){
			if(player.state == player.CLIMBING && holding_player && (!rect().overlaps(player.rect()) ||  player.position[1] < rect.y )){
				player.state = player.IDLE;
				holding_player = false;
				player.wanna_climb = false;
				if(player.position[1] > rect().y + rect().height - 8){
					player.position[1] += 10;
					player.speed[1] = 4;
				}
			}
			else if(player.wanna_climb && rect().overlaps(player.rect()) && !holding_player && player.position[1] > rect.y - 24){
				player.state = player.CLIMBING;
				player.sub_state = 0;
				holding_player = true;
			}
			
			
		}
		
	}
	
	public Rectangle rect(){
		return new Rectangle(rect.x+16,rect.y+16,rect.width-32,rect.height-32);
	}
	
	public void draw(SpriteBatch batch){
		if(active){
			batch.draw(regions[0],rect.x,rect.y,0,0,16,16,4,4,0);
			for(int i = 1; i < height-1; i++){
				batch.draw(regions[1],rect.x,rect.y+64*i,0,0,16,16,4,4,0);	
			}
			batch.draw(regions[2],rect.x,rect.y+64*(height-1),0,0,16,16,4,4,0);
		}
	}
	
	
}
