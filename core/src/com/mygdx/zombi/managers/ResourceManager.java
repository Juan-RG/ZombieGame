package com.mygdx.zombi.managers;


import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ResourceManager {
	
	private static Map<String, Texture> textures = new HashMap<String, Texture>();
	public static AssetManager assets=new AssetManager();
	
	public static void loadAllResources(){
		assets.load("music/cancionFondo.mp3",Music.class);
		assets.load("music/ruidoZombi.mp3",Music.class);
		assets.load("sounds/sonidoDisparo.mp3",Sound.class);
		assets.load("sounds/sonidoGolpe.mp3",Sound.class);
		assets.load("sounds/ruidoMuerte.mp3",Sound.class);
		assets.load("sounds/sonidoRecuperacion.mp3",Sound.class);
		assets.load("sounds/sonidoHelicoptero.mp3",Sound.class);
		assets.load("sounds/explosionBomba.mp3",Sound.class);
		assets.load("characters/soldier/soldier.pack",TextureAtlas.class);
		assets.load("characters/zombi/zombi.pack",TextureAtlas.class);
		assets.load("characters/zombi_red/zombi_red.pack",TextureAtlas.class);
		assets.load("characters/zombi_bomba/zombi_bomba.pack",TextureAtlas.class);
		assets.load("characters/zombi_bomba/explosion.pack",TextureAtlas.class);
		assets.load("characters/worm/worm.pack",TextureAtlas.class);
		assets.load("items/corazon.pack",TextureAtlas.class);
		assets.load("items/helicoptero.pack",TextureAtlas.class);
		
		ResourceManager.loadResource("bala", new Texture("items/bulleta.png"));
		
		
	//añadir más elementos
	
	}
	
	public static Texture getTexture(String name) {
		return textures.get(name);
	}
	
	public static boolean update(){
		return assets.update();
	}
	
	public static void loadResource(String name, Texture resource) {
		textures.put(name, resource);
	}
	
	public static TextureAtlas getAtlas(String path){
		return assets.get(path, TextureAtlas.class);
		
	}
	
	public static Sound getSound(String path){
		return assets.get(path, Sound.class);
	}
	
	public static Music getMusic(String path){
		return assets.get(path, Music.class);
	}

	public static void dispose(){
		assets.dispose();
	}
}
