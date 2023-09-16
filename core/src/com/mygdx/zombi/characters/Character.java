package com.mygdx.zombi.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zombi.managers.SpriteManager;


public abstract class Character {
	public Vector2 position;
	public Vector2 velocity;
	public float WIDTH;
	public float HEIGHT;
	public boolean banderaMuerte;
	public TextureRegion currentFrame;
	//public Texture textura;
	public float stateTime;
	public Rectangle rect;
	public boolean dead;
	public SpriteManager spriteManager;
	
	public Character(SpriteManager spriteManager){
		this.spriteManager=spriteManager;
		this.dead=false;
		this.velocity = new Vector2(0,0);
		this.position = new Vector2(0,0);
		
	}
	
	public void render(Batch batch){
		
		batch.draw(currentFrame,position.x, position.y, WIDTH,HEIGHT);
	}
	/**
	public void renderPowerUp(Batch batch){
		
		batch.draw(currentFrame,position.x, position.y, WIDTH,HEIGHT);
	}
	**/
	
	public abstract void update(float dt);
	
	public abstract void checkCollisions(SpriteManager spriteManager);
	
	public boolean isDead(){return dead;}

}
