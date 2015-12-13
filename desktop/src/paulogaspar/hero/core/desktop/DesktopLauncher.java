package paulogaspar.hero.core.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import paulogaspar.hero.core.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {	
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.useGL30 = false;
		config.resizable = true;
		config.width = 800;
		config.height = 600;
		
		new LwjglApplication(new MyGame(), config);
	}
}