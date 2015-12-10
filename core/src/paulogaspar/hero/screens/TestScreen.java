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

import paulogaspar.hero.actors.King;
import paulogaspar.hero.maps.BackgroundElement;
import paulogaspar.hero.maps.Ground;
import paulogaspar.hero.maps.Ladder;
import paulogaspar.hero.maps.Platform;

public class TestScreen extends Stage implements Screen{
	
	private King king;
	
	private Texture tileset;
	
	private Platform[] platforms;
	private BackgroundElement[]bg_elements;
	
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
		
		platforms = new Platform[1];
		platforms[0] = new Ground(tileset,-120, 0,16,2);
		
		ladders = new Ladder[1];
		ladders[0] = new Ladder(tileset, 380, 128, 3);
		
		bg_elements = new BackgroundElement[1];
		bg_elements[0] = new BackgroundElement(new TextureRegion(tileset,0,0,112,128), 64, 128, 3.84f);

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
