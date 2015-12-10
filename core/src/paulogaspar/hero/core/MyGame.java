package paulogaspar.hero.core;

import com.badlogic.gdx.Game;

import paulogaspar.hero.screens.TestScreen;

public class MyGame extends Game {
	
	@Override
	public void create () {
		setScreen(new TestScreen());
	}

	@Override
	public void render () {
		super.render();
		
		
	}
}
