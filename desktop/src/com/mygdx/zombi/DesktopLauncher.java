package com.mygdx.zombi;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.zombi.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("zombi");
		config.setWindowSizeLimits(0, 0, 1024, 700);
//		config.title="zombi";
//		config.width=1024;
//		config.height=700;
		new Lwjgl3Application(new MyGame(), config);
	}
}
