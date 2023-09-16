package com.mygdx.zombi.characters;



import java.util.Iterator;

//import javax.jws.soap.SOAPBinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.zombi.managers.ResourceManager;
import com.mygdx.zombi.managers.SpriteManager;
import com.mygdx.zombi.screens.GameOverScreen;


public class Player extends Character{
	
	
	//varaible de stado del personaje
	public State state;
	
	//variables de tiempo de ataque del jugador
	float tiempoAtaqueDisparo;
	public int banderaAtaqueDisparo=0;
	
	//life of character
	private int vida=100;
	
	//caja de colision zombi
	public Collider cajaColisionZombi;
	
	//caja de colision gusano
	public Collider cajaColisionGusano;
	
	//varaibles de posicion de las cajas
	private int offsetAtaqueX=35;
	private int offsetAtaqueY=30;
	
	//variables de tama�os de las cajas
	private int anchuraAtaque=30;
	private int alturaAtaque=30;
	
	//variables de las balas
	private Array<Bala> balas;
	private int numBalas=10;
	//tiempo que la bala no podra dispararse
	//private float delay=0;
	
	//private float shootduration;
	private int bullet;

	//texture region para cojer todos los posicionamientos
	private TextureRegion idleDown;

	//texture region para mostrar en cada frame
	private TextureRegion currentFrame;//<--------------
	
	//pintador shapeRenderer
	private ShapeRenderer shapeRenderer;

	
	//anmimaciones de parada
	private Animation<TextureRegion> idleDownAnimation;
	private Animation<TextureRegion> idleUpAnimation;
	private Animation<TextureRegion> idleRightAnimation;
	private Animation<TextureRegion> idleLeftAnimation;
	
	//animaciones de correr
	private Animation<TextureRegion> runRightAnimation;
	private Animation<TextureRegion> runLeftAnimation;
	private Animation<TextureRegion> runUpAnimation;
	private Animation<TextureRegion> runDownAnimation;
	
	//animaciones de disparo
	private Animation<TextureRegion> shootUp;
	private Animation<TextureRegion> shootDown;
	private Animation<TextureRegion> shootLeft;
	private Animation<TextureRegion> shootRight;
	
	//velocidad del personaje
	public static float WALKING_SPEED=180.0f;
	
	//sonido disparo
	private Sound sonidoDisparo;
	private Sound sonidoGolpe;
	private Sound sonidoMuerte;
	private Sound sonidoAumentarVida;
	
	
	public enum State{
		IDLEDOWN,IDLEUP,IDLERIGHT,IDLELEFT, RUNRIGHT, RUNLEFT, RUNUP, RUNDOWN, SHOOTUP, SHOOTDOWN, SHOOTLEFT,
		SHOOTRIGT
		
	}
	
	
	public Player(SpriteManager spriteManager){
		super(spriteManager);
		
		//bULLET ES EL CARGADOR, inicialializo array de balas
		bullet=0;
		this.balas = new Array<Bala>();
		Bala bala;
		for(int i=0; i<numBalas-1;i++){
			bala= new Bala(spriteManager);
			balas.add(bala);
		}
		
		//creo el shaprender para pintar
		shapeRenderer = new ShapeRenderer();
		
		//inicializo los vectores de posicion y velocidad
		position=new Vector2();
		velocity=new Vector2();
		
		//pongo en estado parado
		state=State.IDLEDOWN;
		
		//cojo el .pack
		TextureAtlas atlas=ResourceManager.assets.get("characters/soldier/soldier.pack",TextureAtlas.class);
		
		//cojo el idelabajo de cierta posicion
		idleDown=atlas.findRegion("parado_abajo",-1);
		
		//coloco altura y anchura
		WIDTH=idleDown.getRegionWidth();
		HEIGHT=idleDown.getRegionHeight();
		
		//inicializar sonido disparo
		sonidoDisparo=ResourceManager.getSound("sounds/sonidoDisparo.mp3");
		sonidoGolpe=ResourceManager.getSound("sounds/sonidoGolpe.mp3");
		sonidoMuerte=ResourceManager.getSound("sounds/ruidoMuerte.mp3");
		sonidoAumentarVida=ResourceManager.getSound("sounds/sonidoRecuperacion.mp3");
		
		
		
		
		//coloco los sprites de estado parado
		idleDownAnimation=new Animation<TextureRegion>(0.06f,atlas.findRegions("parado_abajo"));
		idleUpAnimation=new Animation<TextureRegion>(0.06f,atlas.findRegions("parado_arriba"));
		idleRightAnimation=new Animation<TextureRegion>(0.06f,atlas.findRegions("parado_derecha"));
		idleLeftAnimation=new Animation<TextureRegion>(0.06f,atlas.findRegions("parado_izquierda"));
		
		//coloco los sprites de correr
		runRightAnimation=new Animation<TextureRegion>(0.06f,atlas.findRegions("correr_derecha"));
		runLeftAnimation=new Animation<TextureRegion>(0.06f,atlas.findRegions("correr_izquierda"));
		runUpAnimation= new Animation<TextureRegion>(0.06f, atlas.findRegions("correr_arriba"));
		runDownAnimation= new Animation<TextureRegion>(0.06f, atlas.findRegions("correr_abajo"));
		
		//coloco los sprites de disparar
		shootUp= new Animation<TextureRegion>(0.09f, atlas.findRegions("disparo_arriba"));
		shootDown = new Animation<TextureRegion>(0.09f, atlas.findRegions("disparo_abajo"));
		shootRight = new Animation<TextureRegion>(0.09f, atlas.findRegions("disparo_derecha"));
		shootLeft = new Animation<TextureRegion>(0.09f, atlas.findRegions("disparo_izquierda"));

		//hago las cajas de colision
		//28 es para pisicionar el cuadrado centrado al personaje y la y para subirlo
		//colision contra paredes
		this.rect=new Rectangle(this.position.x+28,this.position.y+10,43,72);
		
		//colision con zombi
		this.cajaColisionZombi=new Collider(this.position.x,this.position.y+15,this.anchuraAtaque, this.alturaAtaque);
		
		//colision con gusano
		this.cajaColisionGusano=new Collider(this.position.x,this.position.y,this.anchuraAtaque/2, this.alturaAtaque/2);
		
	}
	
	public void render(Batch spriteBatch){
		stateTime+=Gdx.graphics.getDeltaTime();
		//pinto segun el estado del personaje
		
		switch(state){
			case IDLEDOWN: 
				currentFrame=idleDownAnimation.getKeyFrame(stateTime, true);
				break;
			case IDLEUP: 
				currentFrame=idleUpAnimation.getKeyFrame(stateTime,true);
				break;
			case IDLERIGHT: 
				currentFrame=idleRightAnimation.getKeyFrame(stateTime,true);
				break;
			case IDLELEFT: 
				currentFrame=idleLeftAnimation.getKeyFrame(stateTime,true);
				break;
			case RUNLEFT: 
				currentFrame=runLeftAnimation.getKeyFrame(stateTime,true);
				break;
			case RUNRIGHT: 
				currentFrame=runRightAnimation.getKeyFrame(stateTime,true);
				break;
			case RUNDOWN: 
				currentFrame=runDownAnimation.getKeyFrame(stateTime,true);
				break;
			case RUNUP: 
				currentFrame=runUpAnimation.getKeyFrame(stateTime,true);
				break;
			case SHOOTUP: 
				currentFrame=shootUp.getKeyFrame(stateTime,true);
				//shoot();
				break;
			case SHOOTDOWN: 
				currentFrame=shootDown.getKeyFrame(stateTime,true);
				//shoot();
				break;
			case SHOOTLEFT: 
				currentFrame=shootLeft.getKeyFrame(stateTime,true);
				//shoot();
				break;
			case SHOOTRIGT: 
				currentFrame=shootRight.getKeyFrame(stateTime,true);
				//shoot();
				break;
			
		}
		
		//pinto el personaje
		spriteBatch.draw(currentFrame, position.x,position.y,WIDTH,HEIGHT);
		for (Bala bala : balas) {
			if(bala.isActive()){
				
				spriteBatch.draw(bala.getimagen(),bala.rect.x+bala.bala.getWidth()/2,bala.rect.y,bala.bala.getHeight(),bala.bala.getWidth());
			}
		}
		//cierro el bach para pintar con el shaprender
		spriteBatch.end();
		if(spriteManager.prefs.getBoolean("colision")==true) {
			//abro el shaprender y pinto todos rectangunlos
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());
			shapeRenderer.setColor(Color.GREEN);
			
			//pinto las plataformas
			for(RectangleMapObject p: spriteManager.platforms){
				shapeRenderer.rect(p.getRectangle().x,p.getRectangle().y,p.getRectangle().width,p.getRectangle().height);
			}
			
			
			shapeRenderer.rect(this.rect.x, this.rect.y,43,72);
			shapeRenderer.setColor(Color.RED);
				shapeRenderer.rect(this.cajaColisionZombi.rect.x,this.cajaColisionZombi.rect.y,this.cajaColisionZombi.rect.height,this.cajaColisionZombi.rect.width);
				shapeRenderer.rect(this.cajaColisionGusano.rect.x,this.cajaColisionGusano.rect.y,this.cajaColisionGusano.rect.height,this.cajaColisionGusano.rect.width);
		
			//pinto las balas
			for (Bala bala : balas) {
				if(bala.isActive()){
					
					shapeRenderer.rect(bala.rect.x,bala.rect.y,bala.rect.height,bala.rect.width);
				}
			}
			
			//cierro el shaprender 
		shapeRenderer.end();
		}
		//habro el batch
		spriteBatch.begin();
	}
	
	public void update(float dt){
		
		//System.out.println(position.x);
		//System.out.println(position.y);
		//28 es para pisicionar el cuadrado centrado al personaje
		//actualizo el rectangulo normal
		this.rect.setPosition(this.position.x+28, this.position.y+10);
		
		//actualizo el rectangulo de colision zombi
		this.cajaColisionZombi.rect.setPosition(this.position.x+offsetAtaqueX,this.position.y+offsetAtaqueY);
		
		//actualizo el rectangulo de colision con el gusano
		this.cajaColisionGusano.rect.setPosition(this.position.x+offsetAtaqueX+8,this.position.y+14);
		
		//paso el spritemanager al checkcolision para comprobar colisiones
		this.checkCollisions(spriteManager);
		
		//cambio la velocidad de la posicion con el dt y la velocidad
		this.position.add(this.velocity.x*dt,this.velocity.y*dt);
		
		
		//si el tiempo es mayor a 4 lo pongo a 0
		
		if(tiempoAtaqueDisparo>1.1) {
			tiempoAtaqueDisparo=0;
			banderaAtaqueDisparo=0;
		}
		switch (this.state) {
		case SHOOTDOWN:
			tiempoAtaqueDisparo=tiempoAtaqueDisparo+dt;
			if(Math.floor(tiempoAtaqueDisparo*10)==1.0 && banderaAtaqueDisparo==0){
				if(spriteManager.prefs.getBoolean("sound")==true) {
				sonidoDisparo.play();
				}
				shoot();
				banderaAtaqueDisparo=1;
				
			}
			break;
		case SHOOTUP:
			tiempoAtaqueDisparo=tiempoAtaqueDisparo+dt;
			if(Math.floor(tiempoAtaqueDisparo*10)==2.0 && banderaAtaqueDisparo==0){
				if(spriteManager.prefs.getBoolean("sound")==true) {
					sonidoDisparo.play();
					}
				shoot();
				banderaAtaqueDisparo=1;
			}
			break;
		case SHOOTLEFT:
			tiempoAtaqueDisparo=tiempoAtaqueDisparo+dt;
			if(Math.floor(tiempoAtaqueDisparo*10)==2.0 && banderaAtaqueDisparo==0){
				if(spriteManager.prefs.getBoolean("sound")==true) {
					sonidoDisparo.play();
					}
				shoot();
				banderaAtaqueDisparo=1;
			}
			break;
		case SHOOTRIGT:
			tiempoAtaqueDisparo=tiempoAtaqueDisparo+dt;
			if(Math.floor(tiempoAtaqueDisparo*10)==2.0 && banderaAtaqueDisparo==0){
				if(spriteManager.prefs.getBoolean("sound")==true) {
					sonidoDisparo.play();
					}
				shoot();
				banderaAtaqueDisparo=1;
			}
			break;

		default:
			tiempoAtaqueDisparo=0;
			banderaAtaqueDisparo=0;
			break;
		}
		
		//paso a las balas el update para que compruebe
		for (Bala bala : balas) {
			//si la bala esta activada la actualizo
			if(bala.isActive()){
				bala.update(dt);
			}
		}
		//resto el atackdurantion para poder volver a disparar
	//	if(shootduration>0){
	//		shootduration-=dt;
	//	}
		
	}
	

	@Override
	public void checkCollisions(SpriteManager spriteManager) {
		//compruebo las colisiones con el mapa
		for(RectangleMapObject p: spriteManager.platforms){
			if(this.rect.overlaps(p.getRectangle())){
				//compruebo la poscion de la izquierda con el cuadrado y un poco menos y si la velocidad de x es menor que 0
				if(this.rect.x>=p.getRectangle().x  +p.getRectangle().getWidth()-5 && this.velocity.x<0){
					//le pongo la velocidad de x a 0
					this.velocity.x=0;
					//compruebo la poscion de la derecha con el cuadrado  y si la velocidad de x es mayor que 0
				}else if(this.rect.x<p.getRectangle().x && this.velocity.x>0){
					//le pongo la velocidad de x a 0
					this.velocity.x=0;
		
					//compruebo la poscion de abajo con el cuadrado y un poco menos y si la velocidad de y   es menor que 0
				}else if(this.rect.y>p.getRectangle().y + p.getRectangle().getHeight()-5 && this.velocity.y<0){
					//pongo la velocidad de y a 0
					this.velocity.y=0;
			
					//compruebo la poscion de la arriba con el cuadrado  y si la velocidad de y es mayor que 0
				}else if(this.rect.y<p.getRectangle().y && this.velocity.y>0){
					
					this.velocity.y=0;
				}
			//si no colisiona no cambiamos nada	
			}else{
				
			}
			
		}
		//comprobamos la colision con los enemigos zombies
		for (Enemy zombi : spriteManager.enemys) {
		//compruebo si el enemigo es un zombi
			if (zombi.getClass().getName().equals("com.mygdx.zombi.characters.EnemyZombi")) {
				//si es un zombi lo casteo
				EnemyZombi zombie=(EnemyZombi) zombi;
				//compruebo si la caja de colision con el zombi colisiona con la caja de ataque
					if(this.cajaColisionZombi.rect.overlaps(zombie.cajaAtaque.rect) && zombie.banderaMuerte==false){
						//hago las mismas comprobaciones que con las paredes pero con el zombi para poder movernos libremente
						if(this.cajaColisionZombi.rect.x>=zombie.cajaAtaque.rect.x  + zombie.cajaAtaque.rect.getWidth()-5 && this.velocity.x<0){
							this.velocity.x=0;
					
						}else if(this.cajaColisionZombi.rect.x<zombie.cajaAtaque.rect.x && this.velocity.x>0){
							this.velocity.x=0;

						}else if(this.cajaColisionZombi.rect.y>zombie.cajaAtaque.rect.y + zombie.cajaAtaque.rect.getHeight()-5 && this.velocity.y<0){
							this.velocity.y=0;
							
							
						}else if(this.cajaColisionZombi.rect.y<zombie.cajaAtaque.rect.y && this.velocity.y>0){
							this.velocity.y=0;
						}
					}
			}else if (zombi.getClass().getName().equals("com.mygdx.zombi.characters.EnemyZombiRed")) {
				//si es un zombi lo casteo
				EnemyZombiRed zombie=(EnemyZombiRed) zombi;
				//compruebo si la caja de colision con el zombi colisiona con la caja de ataque
					if(this.cajaColisionZombi.rect.overlaps(zombie.cajaAtaque.rect) && zombie.banderaMuerte==false){
						//hago las mismas comprobaciones que con las paredes pero con el zombi para poder movernos libremente
						if(this.cajaColisionZombi.rect.x>=zombie.cajaAtaque.rect.x  + zombie.cajaAtaque.rect.getWidth()-5 && this.velocity.x<0){
							this.velocity.x=0;
					
						}else if(this.cajaColisionZombi.rect.x<zombie.cajaAtaque.rect.x && this.velocity.x>0){
							this.velocity.x=0;

						}else if(this.cajaColisionZombi.rect.y>zombie.cajaAtaque.rect.y + zombie.cajaAtaque.rect.getHeight()-5 && this.velocity.y<0){
							this.velocity.y=0;
							
							
						}else if(this.cajaColisionZombi.rect.y<zombie.cajaAtaque.rect.y && this.velocity.y>0){
							this.velocity.y=0;
						}
					}
			}else if (zombi.getClass().getName().equals("com.mygdx.zombi.characters.EnemyZombiExplosivo")) {
				//si es un zombi lo casteo
				EnemyZombiExplosivo zombie=(EnemyZombiExplosivo) zombi;
				//compruebo si la caja de colision con el zombi colisiona con la caja de ataque
					if(this.cajaColisionZombi.rect.overlaps(zombie.cajaAtaque.rect) && zombie.banderaMuerte==false){
						//hago las mismas comprobaciones que con las paredes pero con el zombi para poder movernos libremente
						if(this.cajaColisionZombi.rect.x>=zombie.cajaAtaque.rect.x  + zombie.cajaAtaque.rect.getWidth()-5 && this.velocity.x<0){
							this.velocity.x=0;
					
						}else if(this.cajaColisionZombi.rect.x<zombie.cajaAtaque.rect.x && this.velocity.x>0){
							this.velocity.x=0;

						}else if(this.cajaColisionZombi.rect.y>zombie.cajaAtaque.rect.y + zombie.cajaAtaque.rect.getHeight()-5 && this.velocity.y<0){
							this.velocity.y=0;
							
							
						}else if(this.cajaColisionZombi.rect.y<zombie.cajaAtaque.rect.y && this.velocity.y>0){
							this.velocity.y=0;
						}
					}
			}
		}
		
		
		
	}
	/**
	 * falta de comentar
	 */
	//metodo para disparar
	public void shoot(){
		
		
			switch (this.state) {
				//segun el caso de la bala le meto un tama�o y una poscion ademas de la velocidad
				case SHOOTDOWN:
					balas.get(bullet).rect.setSize(25, 10);
					balas.get(bullet).position.x=this.position.x+36;
					balas.get(bullet).position.y=this.position.y+20;
					balas.get(bullet).velocity.set(0,800*-1);
					balas.get(bullet).setActive();
					break;
				case SHOOTUP:
					balas.get(bullet).rect.setSize(25, 10);
					balas.get(bullet).position.x=this.position.x+48;
					balas.get(bullet).position.y=this.position.y+40;
					balas.get(bullet).velocity.set(0,800*1);
					balas.get(bullet).setActive();
					break;
				case SHOOTLEFT:
					
					balas.get(bullet).rect.setSize(10, 25);
					balas.get(bullet).position.x=this.position.x+36;
					balas.get(bullet).position.y=this.position.y+40;
					balas.get(bullet).velocity.set(800*-1,0);
					balas.get(bullet).setActive();
					break;
				case SHOOTRIGT:
					
					balas.get(bullet).rect.setSize(10, 25);
					balas.get(bullet).position.x=this.position.x+36;
					balas.get(bullet).position.y=this.position.y+30;
					balas.get(bullet).velocity.set(800*1,0);
					balas.get(bullet).setActive();
					break;
				
				default:
					break;
				
			}
			bullet=(bullet+1)%(numBalas-1);
			//vector ciclico para ir hasta de 0-5 y volver a 0
			
			
			
		//}
	}
	
	//metodo para restar vida al enemigo
	public void restarVida(int damage){
		//hago que la vida baje con el da�o;
		this.vida=this.vida- damage;
		if(spriteManager.prefs.getBoolean("sound")==true) {
			sonidoGolpe.play();
			}
	
		if(this.vida<=0) {
		//realizar la muerte y la pantalla	
			if(spriteManager.prefs.getBoolean("sound")==true) {
				sonidoMuerte.play();
			}
		spriteManager.game.setScreen(new GameOverScreen(spriteManager.game));
		}
	
	}
	
	public void aumentarVida(int vidaRecibida) {
		
		if(spriteManager.prefs.getBoolean("sound")==true) {
			sonidoAumentarVida.play();
		}
		if(this.vida<100) {
			
			this.vida=this.vida+vidaRecibida;
		
			if(this.vida>100) {
				this.vida=100;
			}
		
		}
	}

	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}
	
	
	
	
	

}
