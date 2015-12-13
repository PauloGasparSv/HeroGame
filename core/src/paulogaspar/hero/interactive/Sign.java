package paulogaspar.hero.interactive;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import paulogaspar.hero.actors.King;

public class Sign extends Structure{
	
	private float scale;
	private String text;
	private String current_message;
	private int current_char;
	private BitmapFont font;
	private boolean show_message;
	private long timer;
	
	public Sign(TextureRegion region,float x,float y,float scale,String text,BitmapFont font){
		this.current_frame = region;
		rect = new Rectangle(x,y,region.getRegionWidth()*scale,region.getRegionHeight()*scale);
		this.scale = scale;
		this.text = text;
		this.font = font;
		init();
	}
	
	public void init(){
		active = false;
		current_message = "";
		show_message = false;
		current_char = 0;
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
			if(king.interact_press && !show_message && rect.overlaps(king.rect())){
				show_message = true;
				timer = System.currentTimeMillis();
				System.out.println("Show");
			}
			
			if(show_message){
				System.out.println("A: "+(System.currentTimeMillis() - timer)+" B: "+text.length());
				if(System.currentTimeMillis() - timer > 50 && current_char < text.length()){
					timer = System.currentTimeMillis();
					current_message += text.charAt(current_char);
					System.out.println(text);
					current_char++;
				}
			}
			
		}
		
	}
	
	public void draw(SpriteBatch batch){
		if(active){
			batch.draw(current_frame,rect.x,rect.y,0,0,16,16,scale,scale,0);
			if(show_message){
				//System.out.println(current_message);
				font.draw(batch,current_message, rect.x-current_message.length()*5+8*scale, rect.y+24*scale);
			}
		}
	}
}
