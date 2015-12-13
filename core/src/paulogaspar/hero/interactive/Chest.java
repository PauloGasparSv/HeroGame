package paulogaspar.hero.interactive;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import paulogaspar.hero.actors.King;

public class Chest extends Structure{
	
	private int state;
	
	private float scale;
	private long timer;
	
	private TextureRegion closed_region;
	private TextureRegion item_region;
	private TextureRegion open_region;
	
	public Chest(TextureRegion[] region,float x,float y,float scale){
		this.current_frame = region[0];
		
		closed_region = region[0];
		item_region = region[2];
		open_region = region[1];
		
		rect = new Rectangle(x,y,region[0].getRegionWidth()*scale,region[0].getRegionHeight()*scale);
		this.scale = scale;
		
		state = 0;

		
		init();
	}
	
	public void init(){
		active = false;
		timer = 0;
	}
	
	public void update(OrthographicCamera camera,King king){
		if(rect.x +rect.width> camera.position.x - camera.viewportWidth/2 && rect.x < camera.position.x + camera.viewportWidth/2 && 
				rect.y > camera.position.y - rect.height- camera.viewportHeight/2 && rect.y < camera.position.y + camera.viewportHeight/2)active = true;
		else if(active){
			active = false;
			init();
		}
		
		if(active){
			
			if(king.state == king.ATTACKING&& state == 0 &&  rect.overlaps(king.atkRect())){
				state = 1;
				current_frame = item_region;
			}
			
			
		}
		
	}
	
	public void draw(SpriteBatch batch){
		if(active){
			batch.draw(current_frame,rect.x,rect.y,0,0,16,16,scale,scale,0);
		}
	}
}
