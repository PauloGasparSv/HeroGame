package paulogaspar.hero.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background{
	
	private TextureRegion region;
	private float[]position;
	private float [] initial_position;
	private int width;
	private int height;
	public boolean active;
	private float scale;
	private float speed;
	
	public Background(TextureRegion region,float x,float y,float scale,float speed){
		this.region = region;
		position = new float[2];
		position[0] = x;
		position[1] = y;
		initial_position = new float[2];
		initial_position[0] = x;
		initial_position[1] = y;
		width = region.getRegionWidth();
		height = region.getRegionHeight();
		active = false;
		this.speed = speed;
		this.scale = scale;
	}
	
	public void update(OrthographicCamera camera){
		if(position[0] +width*scale> camera.position.x - camera.viewportWidth/2 && position[0] < camera.position.x + camera.viewportWidth/2 && 
				position[1] > camera.position.y - height*scale- camera.viewportHeight/2 && position[1] < camera.position.y + camera.viewportHeight/2)active = true;
		else active = false;
		if(speed!=1)position[0] = initial_position[0] + camera.position.x/speed - 400;
	}
	
	public void draw(SpriteBatch batch){
		if(active){
			batch.draw(region,position[0],position[1],0,0,width,height,scale,scale,0);
		}
	}
	
	
}
