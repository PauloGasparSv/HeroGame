package paulogaspar.hero.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Ground extends Platform{

	TextureRegion[] tiles;
	private int[] number_of_tiles;
	
	
	public Ground(Texture sheet,int x,int y,int w,int h){
		rect = new Rectangle(x,y,w*64,h*64);
		active = false;
			
		number_of_tiles = new int[2];
		number_of_tiles[0] = w;
		number_of_tiles[1] = h;
		
		tiles = new TextureRegion[6];
		tiles[0] =  new TextureRegion(sheet,112+16,0,16,16);
		tiles[1] =  new TextureRegion(sheet,112,16,16,16);
		tiles[2] = new TextureRegion(sheet,112+16,16,16,16);
		tiles[3] = new TextureRegion(sheet,112+32,16,16,16);
		tiles[4] = new TextureRegion(sheet,112+48,16,16,16);
		tiles[5] = new TextureRegion(sheet,112+80,16,16,16);
		
	}
	
	@Override
	public void update(float delta,OrthographicCamera camera){
		if(rect.x +rect.width> camera.position.x - camera.viewportWidth/2 && rect.x < camera.position.x + camera.viewportWidth/2 && 
				rect.y > camera.position.y - rect.height- camera.viewportHeight/2 && rect.y < camera.position.y + camera.viewportHeight/2)active = true;
		else active = false;
		if(!active) return;
		
		
		
	}
	
	@Override
	public void draw(SpriteBatch batch){
		if(!active)return;
		
		batch.draw(tiles[2],rect.x,rect.y + 64*(number_of_tiles[1]-1),0,0,16,16,4,4,0);
		batch.draw(tiles[3],rect.x+number_of_tiles[0]*64-64,rect.y + 64*(number_of_tiles[1]-1),0,0,16,16,4,4,0);
		
		for(int i = 1; i < number_of_tiles[1]; i++){
			batch.draw(tiles[4],rect.x,rect.y-64*(i-1),0,0,16,16,4,4,0);
			batch.draw(tiles[5],rect.x+number_of_tiles[0]*64-64,rect.y-64*(i-1),0,0,16,16,4,4,0);
		}
		
		for(int i = 1; i < number_of_tiles[0]-1; i++)
			batch.draw(tiles[0],rect.x+64*i,rect.y+64*(number_of_tiles[1]-1),0,0,16,16,4,4,0);
		for(int i = 1; i < number_of_tiles[1]; i++)
			for(int j = 1; j < number_of_tiles[0]-1; j++)
				batch.draw(tiles[1],rect.x+64*j,rect.y-64*(i-1),0,0,16,16,4,4,0);
	}
	
	
}
