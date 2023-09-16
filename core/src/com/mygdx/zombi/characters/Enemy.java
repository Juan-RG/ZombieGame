package com.mygdx.zombi.characters;

import com.mygdx.zombi.managers.SpriteManager;

public  abstract class Enemy extends Character{
	public float WALKING_SPEED;
	public int vida;
	public int damage;
	/*Atributos: fuerza, vida, da�o...*/
	
	public Enemy(SpriteManager spriteManager){
		
		super(spriteManager);
	}
	
	public abstract void restarVida(int damage);


}
