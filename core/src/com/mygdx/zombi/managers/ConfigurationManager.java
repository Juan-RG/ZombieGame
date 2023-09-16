package com.mygdx.zombi.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public class ConfigurationManager {

    private Preferences prefs;

	public ConfigurationManager() {

        prefs = Gdx.app.getPreferences("ZombiCom");
	}

    public boolean isSoundEnabled() {
        return prefs.getBoolean("sound");
    }


}
