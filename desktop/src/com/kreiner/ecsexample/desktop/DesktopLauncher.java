package com.kreiner.ecsexample.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kreiner.ecsexample.ECSExample;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 550;
		config.height = 550;
		new LwjglApplication(new ECSExample(), config);
	}
}
