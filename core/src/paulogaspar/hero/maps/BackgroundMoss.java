package paulogaspar.hero.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BackgroundMoss extends BackgroundElement{	
	
	private int map[][];
	private TextureRegion set[];

	
	public BackgroundMoss(TextureRegion []region,int x,int y,float scale,String map_str){
		super(region[0], x, y, scale);

		this.set = region;
		
		String [] split = map_str.split(",");
		
		int columns = 0;
		int lines = 0;
		boolean end_line = false;
		
		for(int i = 0; i < split.length;i++){
			if(split[i].equals("#")){
				lines++;
				end_line = true;
			}
			else if(!end_line)columns++;
		}
		
		map = new int[lines][columns];
		
		
		int line = 0;
		int col = 0;
		for(int curr = 0; curr < split.length; curr ++){
			if(split[curr].equals("#")){
				line++;
				col = 0;
				}
			else{
				map[line][col] = Integer.parseInt(split[curr]);
				col++;
			}
		}
		
		//this.set = region;
		width = region[0].getRegionWidth()*columns;
		height = region[0].getRegionHeight()*(lines+1);
	}
	
	public void update(OrthographicCamera camera){
		if(position[0] +width*scale> camera.position.x - camera.viewportWidth/2 && position[0] < camera.position.x + camera.viewportWidth/2 && 
				position[1] > camera.position.y - height*scale- camera.viewportHeight/2 && position[1] < camera.position.y + camera.viewportHeight/2)active = true;
		else active = false;
	}
	
	public void draw(SpriteBatch batch){
		if(active){
			for(int line = map.length-1; line > -1; line --){
				for(int col = 0; col < map[0].length; col++){
					if(map[line][col] == -1)continue;
					batch.draw(set[map[line][col]],position[0]+16*col*scale,position[1]+(map.length-line)*16*scale,0,0,16,16,scale,scale,0);
				}
			}
		}
	}
	
	
}
