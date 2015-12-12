package paulogaspar.hero.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Ground extends Platform{

	TextureRegion[] tiles;
	private int[] number_of_tiles;
	
	private int [][]map;
	
	
	public Ground(Texture sheet,int x,int y,int w,int h,int type,String style){
		rect = new Rectangle(x,y,w*64,h*64);
		active = false;
			
		this.type = type;
		
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
		
		if(style == null){
			map = new int [number_of_tiles[1]][number_of_tiles[0]];
			
			map[0][0] = 2;
			map[0][map[0].length-1] = 3;
			
			for(int i = 1; i < map[0].length-1;i++){
				map[0][i] = 0;
			}
			for(int i = 1; i < map.length; i++){
				map[i][0] = 4;
				map[i][map[0].length-1] = 5;
			}
			
			for(int i = 1; i < map.length; i++){
				for(int j = 1; j < map[0].length-1; j++){
					map[i][j] = 1;
				}
			}
			
		}
		
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
		
		for(int l = map.length-1; l >-1; l--){
			for(int c = 0; c < map[0].length; c++){
				batch.draw(tiles[map[l][c]],rect.x+c*64,rect.y+64*(map.length-l-1),0,0,16,16,4,4,0);
			}
		}
				
	
		
	}
	
	
}
