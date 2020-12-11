package commygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import commygdx.game.AuberGame;

import java.awt.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.x=0;
		config.y=0;
		config.width= Toolkit.getDefaultToolkit().getScreenSize().width;
		config.height= Toolkit.getDefaultToolkit().getScreenSize().height;

		new LwjglApplication(new AuberGame(), config);


	}
}
