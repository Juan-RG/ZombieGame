package com.mygdx.zombi.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zombi.characters.EnemyZombi.State;
import com.mygdx.zombi.managers.ResourceManager;
import com.mygdx.zombi.managers.SpriteManager;
import com.mygdx.zombi.utils.Constants;


public class EnemyWorm extends Enemy{
	public State state;

	//texturas de movimiento del enemigo
	private Animation<TextureRegion> runRightAnimation;
	private Animation<TextureRegion> runLeftAnimation;
	private Animation<TextureRegion> runUpAnimation;
	private Animation<TextureRegion> runDownAnimation;
	
	//animaciones muerte
		private Animation<TextureRegion> deadRight;
		private Animation<TextureRegion> deadLeft;
		private Animation<TextureRegion> deadUp;
		private Animation<TextureRegion> deadDown;
		
		//puntuacion
		int puntuacionMuertegusano=40;
		
		
		//contador de muerte
		double contadorMuerte;
	
	
	//Aparecer gusano
	private Animation<TextureRegion> showWorm;
	
	//texturas de movimiento del enemigo Enterrado
	private Animation<TextureRegion> runBuriedRightAnimation;
	//animacion de parado
	private Animation<TextureRegion> idleAnimation;
	
	//boolean de colision para saber si la caja ataque esta colisionando con el enemigo
	private boolean colision=false;
	//Colision para saber cuando tiene que atacar el personaje
	private Collider cajaAtaque;
	
	//bandera para que vaya en direccion derecha e izquierda, arriba y abajo
	public int banderaDireccion=0;
	public int baderaColision=0;
	
	//variables de tama�o del rectangulo de colision
	int anchuraRectangulo;
	int alturaRectangulo;
	
	//bandera para que solo vaya a veces de derecha a izquierda
	double tiempoMovimientox;
	
	//bandera para colision de gusano
	double tiempoResteoFlagAttak;
	int banderaAtaquegusano;
	
	
	//creo un contador de vida para hacer que aparezca
	double tiempoDeVida;

	

	//creo un contador de tiempo enterrado
	double tiempoEnterrado;
	
	//creo un contador de vida para hacer que aparezca
	double tiempoEnterrradoSinMoverse;
	
	
	//objeto para pintar
	private ShapeRenderer shapeRenderer;
	
	//Estados del enemigo
	public enum State{
		IDLE, RUNRIGHT, RUNLEFT, RUNUP, RUNDOWN, ATTACKRIGHT, ATTACKLEFT, ATTACKUP, ATTACKDOWN, RUNBURIED, SHOW, DEADLEFT, DEADRIGHT, DEADDOWN, DEADUP
	}

	//constructor
	public EnemyWorm(SpriteManager spriteManager) {
		super(spriteManager);
		// TODO Auto-generated constructor stub
		//velocidad del zombi
		//WALKING_SPEED=200;
		WALKING_SPEED=240;
		//inicializo los vectores de posicion y velocidad
		position=new Vector2();
		velocity=new Vector2(0,0);
		//inicializo el pintador
		shapeRenderer = new ShapeRenderer();
		//hago que el primer estado sea parado
		state=State.IDLE;
		this.damage=30;
		int vida=50;
		
	
		
		//bandera de golpeo
		
		
		//le damos altura y anchura
		alturaRectangulo=78;
		anchuraRectangulo=43;
		
		//cojo el textureAtlas de zombi.pack
		TextureAtlas atlas=ResourceManager.assets.get("characters/worm/worm.pack", TextureAtlas.class);
		//cogemos la posicion de parado en el currentframe
		currentFrame = atlas.findRegion("worm_enterrado",1);
		
		//cogemos la anchura  y la altura del frame anterior
		
		this.WIDTH=currentFrame.getRegionWidth();
		this.HEIGHT=currentFrame.getRegionHeight();
		
		//cogemos las animaciones del zombi corriendo
		runRightAnimation=new Animation<TextureRegion>(0.3f,atlas.findRegions("correr_derecha"));
		runLeftAnimation=new Animation<TextureRegion>(0.3f,atlas.findRegions("correr_izquierda"));
		runUpAnimation= new Animation<TextureRegion>(0.3f, atlas.findRegions("correr_arriba"));
		runDownAnimation= new Animation<TextureRegion>(0.3f, atlas.findRegions("correr_abajo"));
		runBuriedRightAnimation =new Animation<TextureRegion>(0.3f, atlas.findRegions("worm_enterrado"));
		
		//hacemos que aparezca
		showWorm =new Animation<TextureRegion>(1.6f, atlas.findRegions("aparecer_worm"));
		
		//animaciones del zombi muriendo
				deadRight = new Animation<TextureRegion>(0.4f, atlas.findRegions("morir_derecha"));
				deadLeft = new Animation<TextureRegion>(0.4f, atlas.findRegions("morir_izquierda"));
				deadUp = new Animation<TextureRegion>(0.4f, atlas.findRegions("morir_arriba"));
				deadDown = new Animation<TextureRegion>(0.4f, atlas.findRegions("morir_abajo"));
		
		
		//animacion del zombi parado
		//idleAnimation=new Animation<TextureRegion>(0.3f,atlas.findRegions("parado"));
		
		//valores para cuadrar la caja para que colisione mas o menos perfecto 28 y 10 sirven para colocar el rectangulo perfectamente al personaje
		this.rect=new Rectangle(this.position.x,this.position.y,WIDTH/2,HEIGHT/2);
		
		//coloco la caja ataque de la mitad de tama�o del personaje para que cuando colisione ataque
		this.cajaAtaque=new Collider(this.position.x+24,this.position.y+10,this.HEIGHT/4, this.HEIGHT/4);
		
	}

	@Override
	public void update(float dt) {
		//actualizamos el rectangunlo a la posicion con la suma de x+28 y+ 10 para que se centre lo mejor posible
		this.rect.setPosition(this.position.x+15, this.position.y);
		//colocamos la caja para que vaya centrada en el personaje
		this.cajaAtaque.rect.setPosition(this.position.x+24,this.position.y+10);
		if(baderaColision==0){
			tiempoEnterrradoSinMoverse=tiempoEnterrradoSinMoverse+dt;
			if(tiempoEnterrradoSinMoverse>30){
				baderaColision=1;
				banderaDireccion=3;
				this.state=State.RUNBURIED;
			}
		}
		
		if(baderaColision==1){
			tiempoEnterrado=tiempoEnterrado+dt;
			System.out.println(tiempoEnterrado);
			if(tiempoEnterrado>15){
				baderaColision=2;
				banderaDireccion=4;
				this.state=State.SHOW;
				banderaAtaquegusano++;
				
			}
		}
		
		switch (this.state) {
		case DEADDOWN:
			contadorMuerte=contadorMuerte+dt;
			if(contadorMuerte>3.7){
				
				this.dead=true;
			}
			break;
		case DEADUP:
			contadorMuerte=contadorMuerte+dt;
			if(contadorMuerte>3.7){
				this.dead=true;
			}
			break;
		case DEADLEFT:
			contadorMuerte=contadorMuerte+dt;
			if(contadorMuerte>3.7){
				this.dead=true;
			}
			break;
		case DEADRIGHT:
			contadorMuerte=contadorMuerte+dt;
			if(contadorMuerte>3.7){
				this.dead=true;
			}
			break;
			//si no le pongo el tiempo de ataque a 0
		default:
			
			break;
	}
		
		if (this.baderaColision==1 && tiempoDeVida<16) {
			tiempoDeVida=tiempoDeVida+dt;
		}
		
		if(banderaAtaquegusano>=1) {
			tiempoResteoFlagAttak=tiempoResteoFlagAttak+dt;
		}
		
		
	
		if(tiempoMovimientox>2 && this.baderaColision==1) {
		
			tiempoMovimientox=0;
			if(this.position.y>spriteManager.player.position.y){
			
				banderaDireccion=4;
			}
			else if(this.position.y<spriteManager.player.position.y){
		
				banderaDireccion=3;
			}
			
			else if(this.position.x>spriteManager.player.position.x){
			
				banderaDireccion=2;
			}
			else if(this.position.x<spriteManager.player.position.x){
		
				banderaDireccion=1;
			}
		}
		
		if(baderaColision==0 && this.banderaMuerte==false){
			this.velocity.x=0;
			this.velocity.y=0;
		}else if(baderaColision==1 && this.banderaMuerte==false){
			
			if(banderaDireccion==1){
				tiempoMovimientox=tiempoMovimientox+dt;
				
				velocity.x=+WALKING_SPEED;
			}else if(banderaDireccion==2){
				tiempoMovimientox=tiempoMovimientox+dt;
				velocity.y=0;
				velocity.x=-WALKING_SPEED;
			}else if(banderaDireccion==3){
				velocity.x=0;
				velocity.y=+WALKING_SPEED;
			}else if(banderaDireccion==4){
				velocity.x=0;
				velocity.y=-WALKING_SPEED;
			}
	
		}else if(baderaColision==2 && this.banderaMuerte==false){
			/**
			if(banderaDireccion==1){
			
				velocity.x=+WALKING_SPEED;
				velocity.y=0;
				this.state=State.RUNRIGHT;
				if(Math.floor(this.position.x)==Math.floor(spriteManager.player.position.x) && this.position.y<spriteManager.player.position.y){
					
					banderaDireccion=3;
				}else if(Math.floor(this.position.x)==Math.floor(spriteManager.player.position.x) && this.position.y>spriteManager.player.position.y){
					
					banderaDireccion=4;
				}
				
			}else if(banderaDireccion==2 && this.banderaMuerte==false){
			
				velocity.y=0;
				velocity.x=-WALKING_SPEED;
				this.state=State.RUNLEFT;
				
				
				if(Math.floor(this.position.x)==Math.floor(spriteManager.player.position.x) && this.position.y<spriteManager.player.position.y){
					
					banderaDireccion=3;
				}else if(Math.floor(this.position.x)==Math.floor(spriteManager.player.position.x) && this.position.y>spriteManager.player.position.y){
					
					banderaDireccion=4;
				}
			}else if(banderaDireccion==3 && this.banderaMuerte==false){
			
				velocity.x=0;
				velocity.y=+WALKING_SPEED;
				this.state=State.RUNUP;
			}else if(banderaDireccion==4 && this.banderaMuerte==false){
				
				velocity.x=0;
				velocity.y=-WALKING_SPEED;
				this.state=State.RUNDOWN;
			}
			*/
			
			if(banderaDireccion==1){
				
				velocity.x=+WALKING_SPEED;
				velocity.y=0;
				this.state=State.RUNRIGHT;
				if(this.position.x>spriteManager.player.position.x+5 && this.position.x<spriteManager.player.position.x+(spriteManager.player.rect.getWidth()-5) && this.position.y<spriteManager.player.position.y){
					
					banderaDireccion=3;
				}else if(this.position.x>spriteManager.player.position.x && this.position.x<spriteManager.player.position.x+(spriteManager.player.rect.getWidth()-5)  && this.position.y>spriteManager.player.position.y){
					
					banderaDireccion=4;
				}
				
			}else if(banderaDireccion==2 && this.banderaMuerte==false){
			
				velocity.y=0;
				velocity.x=-WALKING_SPEED;
				this.state=State.RUNLEFT;
				
				
				if(this.position.x>spriteManager.player.position.x+5 && this.position.x<spriteManager.player.position.x+(spriteManager.player.rect.getWidth()-5) && this.position.y<spriteManager.player.position.y){
					
					banderaDireccion=3;
				}else if(this.position.x>spriteManager.player.position.x && this.position.x<spriteManager.player.position.x+(spriteManager.player.rect.getWidth()-5)  && this.position.y>spriteManager.player.position.y){
					
					banderaDireccion=4;
				}
			}else if(banderaDireccion==3 && this.banderaMuerte==false){
			
				velocity.x=0;
				velocity.y=+WALKING_SPEED;
				this.state=State.RUNUP;
			}else if(banderaDireccion==4 && this.banderaMuerte==false){
				
				velocity.x=0;
				velocity.y=-WALKING_SPEED;
				this.state=State.RUNDOWN;
			}
			
		
	
		}

		//compruebo si el usuario colisiona
		this.checkCollisions(spriteManager);
		//actualizo las posicion por el Delta Time
		this.position.add(this.velocity.x*dt, this.velocity.y*dt);
		
	}
	
	public void render(Batch spriteBatch){
		//pintamos el stateTime el timpo en el que se pintan las imagenes
		stateTime+=Gdx.graphics.getDeltaTime();
		
		//tipos de estados
		switch(state){
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
			case RUNBURIED:
				currentFrame=runBuriedRightAnimation.getKeyFrame(stateTime,true);
				break;
			case SHOW:
				currentFrame=showWorm.getKeyFrame(stateTime,false);
				break;
			case DEADDOWN: 
				currentFrame=deadDown.getKeyFrame(stateTime,false);
				break;
			case DEADUP: 
				currentFrame=deadUp.getKeyFrame(stateTime,false);
				break;
			case DEADRIGHT: 
				currentFrame=deadRight.getKeyFrame(stateTime,false);
				break;
			case DEADLEFT: 
				currentFrame=deadLeft.getKeyFrame(stateTime,false);
				break;
		}
		//pinto el enemigo y cierro el spriteBach
		spriteBatch.draw(currentFrame, position.x, position.y, this.WIDTH, this.HEIGHT);
		spriteBatch.end();
		if(spriteManager.prefs.getBoolean("colision")==true) {
		//habro el pintador y pinto los dos recuadros de colision
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.rect(this.rect.x, this.rect.y,this.rect.width,this.rect.height);
		//if(this.cajaAtaque.isActive()){
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(this.cajaAtaque.rect.x,this.cajaAtaque.rect.y,this.cajaAtaque.rect.height,this.cajaAtaque.rect.width);
	
		//cierro el pintador y abro el spritebach
		shapeRenderer.end();
		}
		
		spriteBatch.begin();
		
	}
	
	@Override
	public void checkCollisions(SpriteManager spriteManager) {
		
		//Compruebo la colision con las plataformas
		for(RectangleMapObject p: spriteManager.platforms){
			//si colisiona con algun rectangulo
			if(this.rect.overlaps(p.getRectangle())){
				//cambio la direccion si colisiona con alguna caja para poder salir de la colision
				if(this.rect.x>=p.getRectangle().x +p.getRectangle().getWidth()-5 && this.velocity.x<0 && this.baderaColision==1){
					
					this.velocity.x=0;
					banderaDireccion=3;
					
				
					
				}else if(this.rect.x<p.getRectangle().x && this.velocity.x>0 && this.baderaColision==1){
					
					this.velocity.x=0;
					banderaDireccion=4;
					
					
					
				}else if(this.rect.y>p.getRectangle().y + p.getRectangle().getHeight()-5 && this.velocity.y<0 && this.baderaColision==1){
					
					this.velocity.y=0;
					banderaDireccion=2;
					
				}else if(this.rect.y<p.getRectangle().y && this.velocity.y>0 && this.baderaColision==1){
					
					banderaDireccion=1;
					this.velocity.y=0;
					
				}
				
				
				if(this.rect.x>=p.getRectangle().x +p.getRectangle().getWidth()-5 && this.velocity.x<0 && this.baderaColision==2){
				
					this.position.x=this.position.x+10;
					this.velocity.x=0;
					if(this.position.y<spriteManager.player.position.y) {
						
					banderaDireccion=3;
					}else {
					
						banderaDireccion=4;
					}
					
					
				}else if(this.rect.x<p.getRectangle().x && this.velocity.x>0 && this.baderaColision==2){
					
					this.velocity.x=0;
					if(this.position.y>spriteManager.player.position.y) {
						
						banderaDireccion=4;
					}else {
					
					
						banderaDireccion=3;
					}
				
					
				}else if(this.rect.y>p.getRectangle().y + p.getRectangle().getHeight()-5 && this.velocity.y<0 && this.baderaColision==2){
			
					this.velocity.y=0;
					banderaDireccion=1;
					
					
				}else if(this.rect.y<p.getRectangle().y && this.velocity.y>0 && this.baderaColision==2){
				
					this.velocity.y=0;
					banderaDireccion=2;
					
					
				}
			
			}else{
			
			}
			
		}
	
		//Compruebo si colisionan ambas cahas de ataque para hacer x cosas o no y coloco la colision a true
		if(this.rect.overlaps(spriteManager.player.cajaColisionGusano.rect)){
			if(baderaColision==0){
				baderaColision=1;
				banderaDireccion=3;
				this.state=State.RUNBURIED;
			}
			if(baderaColision==1 && tiempoDeVida>15){
				baderaColision=2;
				banderaDireccion=4;
				this.state=State.SHOW;
				banderaAtaquegusano++;
			}
			
		}else{
			
			
			
		}
				
	if(this.rect.overlaps(spriteManager.player.cajaColisionGusano.rect) && this.baderaColision==2 && banderaAtaquegusano==0) {
		if(this.rect.y<spriteManager.player.cajaColisionGusano.rect.y && this.velocity.y>0) {
			spriteManager.player.restarVida(damage);
			spriteManager.player.velocity.x=+400;
			banderaAtaquegusano++;
			
		}else if(this.rect.y>spriteManager.player.cajaColisionGusano.rect.y && this.velocity.y<0 && banderaAtaquegusano==0) {
			spriteManager.player.velocity.x=-400;
			spriteManager.player.restarVida(damage);
			banderaAtaquegusano++;
		}
	}else {
		if (tiempoResteoFlagAttak>2) {
			banderaAtaquegusano=0;
			tiempoResteoFlagAttak=0;
		}
		
	}
		
	}
	
	public void restarVida(int damage){
		//hago que la vida baje con el da�o;
		this.vida=this.vida-damage;

		if (this.vida<=0) {
			this.velocity.y=0;
			this.velocity.x=0;
			this.spriteManager.hud.addScore(puntuacionMuertegusano);
			this.banderaMuerte=true;
			
			switch (this.state) {
				case RUNLEFT:
					this.state=State.DEADLEFT;
					break;
				case RUNRIGHT:
					this.state=State.DEADRIGHT;
					break;
				case RUNDOWN:
					this.state=State.DEADDOWN;
					break;
				case RUNUP:
					this.state=State.DEADUP;
					break;
				default:
					break;
			}
		}
	
	}
	

}
