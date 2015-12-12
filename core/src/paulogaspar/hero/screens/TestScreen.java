package paulogaspar.hero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import paulogaspar.hero.actors.King;
import paulogaspar.hero.maps.Background;
import paulogaspar.hero.maps.BackgroundElement;
import paulogaspar.hero.maps.BackgroundMoss;
import paulogaspar.hero.maps.Ground;
import paulogaspar.hero.maps.Ladder;
import paulogaspar.hero.maps.Platform;

public class TestScreen extends Stage implements Screen{
	
	private King king;
	
	private Texture tileset;
	
	private Platform[] platforms;
	private BackgroundElement[]bg_elements;
	private Background [] bg;
	
	private Ladder[] ladders;
	
	ShapeRenderer s;
	
	public TestScreen(){
		id = -1;	
		batch = new SpriteBatch();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800,600);
		
		//X: 112 W: 60
		//48x128
		
		tileset = new Texture(Gdx.files.internal("sheet.png"));
		
		platforms = new Platform[3];
		platforms[0] = new Ground(tileset,-120, -64,16,3,0,null);
		platforms[1] = new Ground(tileset,120,128,2,3,1,null);
		platforms[2] = new Ground(tileset,1020, -64,16,3,0,null);
		
		ladders = new Ladder[1];
		ladders[0] = new Ladder(tileset, 180, 126, 4);
		
		bg = new Background[1];
		bg[0] = new Background(new TextureRegion(tileset,0,0,112,128), 64, 128, 4.2f,1);

		
		bg_elements = new BackgroundElement[1];
		//bg_elements[0] = 
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
		
	
		bg_elements[0] = new BackgroundMoss(moss, 502, 10, 4, "-1,-1,0,1,1,2,#,-1,-1,3,14,14,5,#,-1,-1,3,4,4,5,#,0,1,12,4,13,5,#,3,4,14,4,4,5,#,6,7,7,7,7,8,#");		
		
		initController();			

		king = new King(gamepad,camera);
		king.position[0] = 300;
		king.position[1] = 300;
		
		s = new ShapeRenderer();
		
		init();
	}
	
	@Override
	public void init(){
		next_stage = false;
	}

	@Override
	public void update(float delta){
		
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
			if(king.state == king.CLIMBING)translation.y = 140* delta;
			else translation.y = king.speed[1]*delta*40;

		}
		else if(king.position[1] < camera.position.y -200){
 			if(king.state == king.CLIMBING)translation.y =-140* delta;
			else translation.y = king.speed[1]*delta*40;
		}
		if(camera.position.x - 400 + translation.x < 0)translation.x = 0;
		if(camera.position.x - 400 + translation.x > 680)translation.x = 0;
		if(camera.position.y - 300 + translation.y < 0)translation.y = 0;
		if(camera.position.y - 300 + translation.y > 580)translation.y = 0;
		
		camera.translate(translation);
		
		for(Background b:bg)b.update(camera);
		for(Platform p:platforms)p.update(delta, camera);
		for(BackgroundElement e:bg_elements)e.update(camera);
		for(Ladder l:ladders)l.update(camera, king);
		
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))camera.translate(-300*delta, 0);
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))camera.translate(300*delta, 0);
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN))camera.translate(0,-300*delta);
		if(Gdx.input.isKeyPressed(Input.Keys.UP))camera.translate(0,300*delta);
		
		camera.update();
	}
	
	@Override
	public void draw(){
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		for(Background b:bg)b.draw(batch);
		for(BackgroundElement e:bg_elements)e.draw(batch);
		
		for(Platform p:platforms)p.draw(batch);
		for(Ladder l:ladders)l.draw(batch);
		
		king.draw(batch);
		

		
		batch.end();
		
		s.setProjectionMatrix(camera.combined);
		
		/*
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		s.begin(ShapeType.Filled);
		
		s.setColor(new Color(1,0,0,0.5f));
		s.rect(king.rect().x,king.rect().y,king.rect().width,king.rect().height);
		
		s.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		*/
		
	}
	
	@Override
	public void dispose() {
		
		tileset.dispose();
		king.dispose();
		batch.dispose();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.6f, 0.64f, 0.41f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.graphics.setTitle("Hero FPS: "+Gdx.graphics.getFramesPerSecond());

		update(delta);
		if(!next_stage)draw();
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
