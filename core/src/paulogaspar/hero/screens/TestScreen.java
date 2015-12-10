package paulogaspar.hero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import paulogaspar.hero.actors.King;
import paulogaspar.hero.maps.Ground;
import paulogaspar.hero.maps.Platform;

public class TestScreen extends Stage implements Screen{
	
	private King king;
	
	private Texture tileset;
	
	private Platform[] platforms;
	
	ShapeRenderer s;
	
	public TestScreen(){
		id = -1;	
		batch = new SpriteBatch();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800,600);
		
		//X: 112 W: 60
		//48x128
		
		tileset = new Texture(Gdx.files.internal("sheet.png"));
		
		platforms = new Platform[2];
		platforms[0] = new Ground(tileset,-120, 100,16,1);
		platforms[1] = new Ground(tileset,-40, 164,14,1);

		initController();			

		king = new King(gamepad,camera);
		king.position[0] = 300;
		king.position[1] = 600;
		
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
		
		king.draw(batch);
		for(Platform p:platforms)p.draw(batch);

		
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
