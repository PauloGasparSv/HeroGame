package paulogaspar.hero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import paulogaspar.hero.actors.Actor;
import paulogaspar.hero.actors.King;

public class TestScreen extends Stage implements Screen{
	
	private Actor king;
	
	public TestScreen(){
		id = -1;
		batch = new SpriteBatch();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800,600);
		
		
		initController();		

		king = new King(gamepad);
		
		init();
	}
	
	@Override
	public void init(){
		next_stage = false;
	}

	@Override
	public void update(float delta){
		
		king.update(delta);
		
		camera.update();
	}
	
	@Override
	public void draw(){
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		king.draw(batch);
		
		batch.end();
	}
	
	@Override
	public void dispose() {
		king.dispose();
		batch.dispose();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.72f, 0.88f, 0.8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
