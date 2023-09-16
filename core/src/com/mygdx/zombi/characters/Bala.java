package com.mygdx.zombi.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.zombi.managers.ResourceManager;
import com.mygdx.zombi.managers.SpriteManager;

public class Bala extends Character{

	private boolean active;
	private float shootTime;
	private float shootDuration;
	private int damage;
	//private int banderaDeColisionBalaZombi;
	private int velocidadBala;
	
	Texture bala;
	
	public Bala(SpriteManager spritemanager) {
		super(spritemanager);
		//altura anchura
		WIDTH=30;
		HEIGHT=15;
		
		velocidadBala=10;
		
		bala= ResourceManager.getTexture("bala");
		
		//dano y duracion de la bala 3
		damage=50;
		shootDuration=3;
			
		rect = new Rectangle(0,0,WIDTH,HEIGHT);
		active=false;
	}
	
	@Override
	public void update(float dt) {
	
		//si la bala esta activa y el tiempo de disparo es mayor que 0
		
		if(active && shootTime > 0){
			//cambio la posicion por el dt y cambio el rectangulo
			this.position.add(this.velocity.x*dt,this.velocity.y*dt);
			//this.position.add(this.velocity.x*dt*10,this.velocity.y*dt);
			
			this.rect.setPosition(this.position);
			//resto el shoot time
			shootTime -=dt;
			//compruebo la colision
			checkCollisions(spriteManager);
		
			//si el shoot time es menor que 0
		}else if(shootTime<=0){
			
			//pongo la colision a false y cambio la badenra a 0
			active = false;
			//banderaDeColisionBalaZombi = 0;
		}
		
	}

	@Override
	public void checkCollisions(SpriteManager spriteManager) {
		// TODO Auto-gerated method stub
		
		//si la bala esta activa compruebo con todos los enemigos en el update
		if(this.active){
			 
			for (Enemy e : spriteManager.enemys) {
				//si colisiono con el rectangulo del jugador y la bandera es 0 
				
				//System.out.println("bandera de colision "+banderaDeColisionBalaZombi);
			
				//if(this.rect.overlaps(e.rect) && banderaDeColisionBalaZombi==0 && e.banderaMuerte==false){
				if(this.rect.overlaps(e.rect) && e.banderaMuerte==false){
					
					//resto vida del enemigo
					//separamos los enemigos para que no haga lo mismo en todos
					if (e.getClass().getName().equals("com.mygdx.zombi.characters.EnemyZombi")) {
						this.active=false;
						e.restarVida(this.damage);
						//cambio banderaa 1
						//banderaDeColisionBalaZombi=1;
					
					}else if (e.getClass().getName().equals("com.mygdx.zombi.characters.EnemyZombiRed")) {
						this.active=false;
						e.restarVida(this.damage);
						//cambio banderaa 1
						//banderaDeColisionBalaZombi=1;
					
					}else if (e.getClass().getName().equals("com.mygdx.zombi.characters.EnemyZombiExplosivo")) {
						this.active=false;
						e.restarVida(this.damage);
						//cambio banderaa 1
						//banderaDeColisionBalaZombi=1;
					
					}else if(e.getClass().getName().equals("com.mygdx.zombi.characters.EnemyWorm")){
						EnemyWorm gusano = (EnemyWorm) e;
						//si el gusano no esta en la superficie no lo podemos matar
						if(gusano.baderaColision==2){
							this.active=false;
							e.restarVida(this.damage);
							//cambio banderaa 1
							//banderaDeColisionBalaZombi=1;
						}else{
							
						}
					}
					
				}
			}
			
			for(RectangleMapObject p: spriteManager.platforms){
				
				if(this.rect.overlaps(p.getRectangle())){
					this.active=false;
				}
			}
			
		}
	}
	
	public void setActive(){
		//pongo la bala activa y le pongo el tiempo de duracion a la bala
		this.active=true;
		shootTime=shootDuration;
	}
	
	public boolean isActive(){
		return this.active;
	}
	
	public Texture getimagen(){
		return bala;
	}

}
