package paulogaspar.hero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import paulogaspar.hero.actors.King;
import paulogaspar.hero.interactive.Chest;
import paulogaspar.hero.interactive.Sign;
import paulogaspar.hero.interactive.Structure;
import paulogaspar.hero.maps.Background;
import paulogaspar.hero.maps.BackgroundElement;
import paulogaspar.hero.maps.BackgroundMoss;
import paulogaspar.hero.maps.Ground;
import paulogaspar.hero.maps.Ladder;
import paulogaspar.hero.maps.Platform;

public class TestScreen extends Stage implements Screen{
	
	private King king;
	
	private Texture tileset;
	private Texture []layers;
	
	private BitmapFont font;
	
	private Platform[] platforms;
	private BackgroundElement[]bg_elements;
	private Background [] bg;
	private Background [] images;
	private Structure [] structs;
	
	private Ladder[] ladders;
	
	ShapeRenderer s;
	
	public TestScreen(){
		id = -1;	
		batch = new SpriteBatch();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800,600);
		
		font = new BitmapFont(Gdx.files.internal("Font/font_24.fnt"),Gdx.files.internal("Font/font_24.png"),false);
		
		//X: 112 W: 60
		//48x128
		
		layers = new Texture[3];
		layers[0] = new Texture(Gdx.files.internal("layers/parallax-forest-back-trees.png"));
		layers[1] = new Texture(Gdx.files.internal("layers/parallax-forest-middle-trees.png"));
		layers[2] = new Texture(Gdx.files.internal("layers/parallax-forest-front-trees.png"));

		tileset = new Texture(Gdx.files.internal("sheet.png"));
		
		platforms = new Platform[3];
		platforms[0] = new Ground(tileset,-120, -64,16,3,0,null);
		platforms[1] = new Ground(tileset,1020, -64,16,3,0,null);
		platforms[2] = new Ground(tileset,1520, 128,5,4,1,null);
		
		ladders = new Ladder[1];
		ladders[0] = new Ladder(tileset, 1560, 126, 4);
		
		bg = new Background[4];
		bg[0] = new Background(new TextureRegion(layers[0]), 0, 0, 4f,1.2f);
		bg[1] = new Background(new TextureRegion(layers[1]), 0, 0, 3.8f,1.6f);
		bg[2] = new Background(new TextureRegion(layers[1]), 272*3.8f, 0,3.8f,1.6f);
		bg[3] = new Background(new TextureRegion(layers[2]), 800, -150, 4.8f,1.99f);
		//bg[4] = new Background(new TextureRegion(tileset,0,0,112,128), 142, 128, 3.2f,1);
		
		images = new Background[7];
		
		images[0] = new Background(new TextureRegion(tileset,112+7*16,16*5,16,16), 442, 128, 3f,1);
		images[1] = new Background(new TextureRegion(tileset,112+7*16,16*6,16,16), 32, 128, 3f,1);
		images[2] = new Background(new TextureRegion(tileset,112+7*16,16*5,16,16), 102, 128, 3f,1);
		images[3] = new Background(new TextureRegion(tileset,112+8*16,16*6,16,16), 287, 128, 3f,1);
		images[4] = new Background(new TextureRegion(tileset,112+9*16,16*6,16,16), 805, 128, 3f,1);
		images[5] = new Background(new TextureRegion(tileset,112+9*16,16*5,16,16), 768, 128, 3f,1);
		images[6] = new Background(new TextureRegion(tileset,112+8*16,16*5,16,16), 138, 128, 3f,1);


		
		TextureRegion [] moss = new TextureRegion[15];
		moss[0] = new TextureRegion(tileset,112+16*4,16*4,16,16);
		moss[1] = new TextureRegion(tileset,112+16*5,16*4,16,16);
		moss[2] = new TextureRegion(tileset,112+16*6,16*4,16,16);
		moss[3] = new TextureRegion(tileset,112+16*4,16*5,16,16);
		moss[4] = new TextureRegion(tileset,112+16*5,16*5,16,16);
		moss[5] = new TextureRegion(tileset,112+16*6,16*5,16,16);
		moss[6] = new TextureRegion(tileset,112+16*4,16*6,16,16);
		moss[7] = new TextureRegion(tileset,112+16*5,16*6,16,16);
		moss[8] = new TextureRegion(tileset,112+16*6,16*6,16,16);
		moss[9] = new TextureRegion(tileset,112+16*2,16*4,16,16);
		moss[10] = new TextureRegion(tileset,112+16*3,16*4,16,16);
		moss[11] = new TextureRegion(tileset,112+16*2,16*5,16,16);
		moss[12] = new TextureRegion(tileset,112+16*3,16*5,16,16);
		moss[13] = new TextureRegion(tileset,112+16*2,16*6,16,16);
		moss[14] = new TextureRegion(tileset,112+16*3,16*6,16,16);
		
		bg_elements = new BackgroundElement[3];
		bg_elements[0] = new BackgroundMoss(moss, 502, 10, 4, "-1,-1,0,1,1,2,#,-1,-1,3,4,4,5,#,0,1,12,4,13,5,#,3,4,14,4,4,5,#,6,7,7,7,7,8,#");		
		bg_elements[1] = new BackgroundMoss(moss, -40, 396-64*6, 4, "3,4,4,4,4,4,4,4,5,#,3,4,4,4,4,4,4,4,5,#,4,4,9,7,7,7,7,7,8,#,9,7,8,-1,-1,-1,-1,-1,-1,#,5,-1,-1,-1,-1,-1,-1,-1,-1,#,5,-1,-1,-1,-1,-1,-1,-1,-1,#,5,-1,-1,-1,-1,-1,-1,-1,-1,#,5,-1,-1,-1,-1,-1,-1,-1,-1,#,11,2,-1,-1,-1,-1,-1,-1,-1,#,4,5,-1,-1,-1,-1,-1,-1,-1,#");		
		bg_elements[2] = new BackgroundMoss(moss, -48, 400+64*4, 4, "3,4,4,4,4,4,4,4,5,#,3,4,4,4,4,4,4,4,5,#");		

		
		TextureRegion[]chest_region = new TextureRegion[3];
		chest_region[0] = new TextureRegion(tileset,112+16*7,16*4,16,16);
		chest_region[1] = new TextureRegion(tileset,112+16*8,16*4,16,16);
		chest_region[2] = new TextureRegion(tileset,112+16*9,16*4,16,16);

		
		structs = new Structure[3];
		structs[0] = new Sign(new TextureRegion(tileset,112+7*16,16,16,16),1060,128,4,"To  the  Town",font);
		structs[1] = new Chest(chest_region, 1680, 128+64*4, 4);
		structs[2] = new Sign(new TextureRegion(tileset,112+7*16,0,16,16),1740,128+64*4,4,"I  am  way  too  lazy  to  finish  this  chest",font);

		
		initController();			

		king = new King(gamepad,camera);
		
		
		s = new ShapeRenderer();
		
		init();
	}
	
	@Override
	public void init(){
		next_stage = false;
		king.position[0] = 1400;
		king.position[1] = 300;
		camera.translate(1000, 0);
	}

	@Override
	public void update(float delta){
		
		if(gamepad!=null && gamepad.getButton(8)){
			dispose();
			next_stage = true;
			Gdx.app.exit();
		}
		if(gamepad==null && Gdx.input.isKeyJustPressed(Input.Keys.F4)){
			dispose();
			next_stage = true;
			Gdx.app.exit();
		}
		
		
		king.checkPlatform(platforms,delta);
		king.update(delta);
		
		Vector2 translation = new Vector2(0,0);
		if(king.position[0] < camera.position.x -130){
			translation.x = -king.speed[0] * delta;
		}
		else if(king.position[0] > camera.position.x + 120){
			translation.x = king.speed[0] * delta;
		}
		if(king.position[1] > camera.position.y ){
			//if(king.state == king.CLIMBING)translation.y = 140* delta;
			//else translation.y = king.speed[1]*delta*40;

		}
		else if(king.position[1] < camera.position.y -200){
 			//if(king.state == king.CLIMBING)translation.y =-140* delta;
			//else translation.y = king.speed[1]*delta*40;
		}
		if(camera.position.x - 400 + translation.x < 0)translation.x = 0;
		if(camera.position.x - 400 + translation.x > 1240)translation.x = 0;
		if(camera.position.y - 300 + translation.y < 0)translation.y = 0;
		if(camera.position.y - 300 + translation.y > 580)translation.y = 0;
		
		camera.translate(translation);
		
		for(Background b:bg)b.update(camera);
		for(Background b:images)b.update(camera);
		for(Platform p:platforms)p.update(delta, camera);
		for(BackgroundElement e:bg_elements)e.update(camera);
		for(Ladder l:ladders)l.update(camera, king);
		for(Structure s:structs)s.update(camera, king);
		
		if(king.position[1] < -100){
			king.init();
			king.position[1] = 300;
			king.position[0] = 300;
			camera.setToOrtho(false, 800, 600);
		}
		
		/*if(Gdx.input.isKeyPressed(Input.Keys.LEFT))camera.translate(-300*delta, 0);
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))camera.translate(300*delta, 0);
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN))camera.translate(0,-300*delta);
		if(Gdx.input.isKeyPressed(Input.Keys.UP))camera.translate(0,300*delta);*/
		
		camera.update();
	}
	
	@Override
	public void draw(){
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		for(int i = 0; i < bg.length; i++){
			if(i < 4)batch.setColor(new Color(0.6f,1,0.9f,1));
			bg[i].draw(batch);
			batch.setColor(Color.WHITE);
		}
		for(BackgroundElement e:bg_elements)e.draw(batch);
		for(Background b:images)b.draw(batch);
		for(Platform p:platforms)p.draw(batch);
		for(Ladder l:ladders)l.draw(batch);
		for(Structure s:structs)s.draw(batch);
		
		king.draw(batch);
		

		
		batch.end();
		
		/*s.setProjectionMatrix(camera.combined);
		
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		s.begin(ShapeType.Filled);
		
		s.setColor(new Color(1,0,0,0.5f));
		s.rect(king.atkRect().x,king.atkRect().y,king.atkRect().width,king.atkRect().height);
		
		s.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		*/
		
	}
	
	@Override
	public void dispose() {
		for(Texture l:layers)l.dispose();
		font.dispose();
		tileset.dispose();
		king.dispose();
		batch.dispose();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.6f, 0.85f, 0.6f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.graphics.setTitle("Hero FPS: "+Gdx.graphics.getFramesPerSecond());

		if(next_stage)return;
		update(delta);
		if(next_stage)return;
		draw();
	}
	
	@Override
	public void resize(int width, int height) {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}
	@Override
	public void hide() {}
	@Override
	public void show() {}

}
