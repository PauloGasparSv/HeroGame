package paulogaspar.hero.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BackgroundElement {
	
	private TextureRegion region;
	private int[]position;
	private int width;
	private int height;
	public boolean active;
	private float scale;
	
	public BackgroundElement(TextureRegion region,int x,int y,float scale){
		this.region = region;
		position = new int[2];
		position[0] = x;
		position[1] = y;
		width = region.getRegionWidth();
		height = region.getRegionHeight();
		active = false;
		this.scale = scale;
	}
	
	public void update(OrthographicCamera camera){
		if(position[0] +width*scale> camera.position.x - camera.viewportWidth/2 && position[0] < camera.position.x + camera.viewportWidth/2 && 
				position[1] > camera.position.y - height*scale- camera.viewportHeight/2 && position[1] < camera.position.y + camera.viewportHeight/2)active = true;
		else active = false;
	}
	
	public void draw(SpriteBatch batch){
		if(active){
			batch.draw(region,position[0],position[1],0,0,width,height,scale,scale,0);
		}
	}
	
	
}
