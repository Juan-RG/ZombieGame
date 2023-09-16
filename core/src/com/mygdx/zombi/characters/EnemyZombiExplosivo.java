package com.mygdx.zombi.characters;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zombi.managers.ResourceManager;
import com.mygdx.zombi.managers.SpriteManager;
import com.mygdx.zombi.utils.Constants;


public class EnemyZombiExplosivo extends Enemy{
	//estado del enemigo
	public State state;
	
	//Tiempo de ataque del zombi y la bandera para que no quite mas de una vez por golpe
	float tiempoAtaque;
	public int banderaAtaque=0;
	
	boolean banderaColisionConPlayer;
	
	//puntuacion zombi explosivo
	int puntuacionZombiExplosivo=30;

	//contador de muerte
	double contadorMuerte;
	
	//contador para andar si colisiona los zombies con las paredes muchas veces
	int banderaColisionZombi;
	int movimientoTrasColisionPared;

	//contador para que no se atasques
	int contadorColision;
	double contadorReseteoMovimiento;
	
	//variables para que si el zombi colisiona con otro
	int movimientoTrasColisionZombi;
	double tiempoDecolisionzombie;
	
	//contador de colision y cambios de direccion
	public int contadorCambioDireccion=0;
	
	//variable de da�o del zombi
	private int damage=15;
	private int damageExplosion=80;
	
	//texturas de movimiento del enemigo
	private Animation<TextureRegion> runRightAnimation;
	private Animation<TextureRegion> runLeftAnimation;
	private Animation<TextureRegion> runUpAnimation;
	private Animation<TextureRegion> runDownAnimation;
	
	//animacion de parado
	private Animation<TextureRegion> idleAnimation;
	
	//boleean de powerup
	public boolean powerUp;
	
	//animacion de ataque
	private Animation<TextureRegion> attackRight;
	private Animation<TextureRegion> attackLeft;
	private Animation<TextureRegion> attackUp;
	private Animation<TextureRegion> attackDown;
	
	
	//animacion de la explosion
	private Animation<TextureRegion> boom;
	
	//animaciones muerte
	private Animation<TextureRegion> deadRight;
	private Animation<TextureRegion> deadLeft;
	private Animation<TextureRegion> deadUp;
	private Animation<TextureRegion> deadDown;
	//boolean de colision para saber si la caja ataque esta colisionando con el enemigo
	private boolean colision=false;
	
	//Colision para saber cuando tiene que atacar el personaje
	public Collider cajaAtaque;
	
	//Colision para saber cuando un zombi choca con otro
	public Collider cajaColisionZombies;
	
	//bandera para que vaya en direccion derecha e izquierda, arriba y abajo
	public int banderaDireccion=0;
	
	//variables de tama�o del rectangulo de colision
	int anchuraRectangulo;
	int alturaRectangulo;
	
	//circulo colision explosion
	Circle circuloexplosion;
	
	//objeto para pintar
	private ShapeRenderer shapeRenderer;
	
	private Sound sonidoExplosion;
	
	//Colision para saber cuando un zombi choca con otro
	public Collider cajaColisionZombiesCercaEnemigo;
	
	//Estados del enemigo
	public enum State{
		IDLE, RUNRIGHT, RUNLEFT, RUNUP, RUNDOWN, ATTACKRIGHT, ATTACKLEFT, ATTACKUP, ATTACKDOWN, DEADUP, DEADDOWN, DEADRIGHT, DEADLEFT, BOOM
	}

	//constructor
	public EnemyZombiExplosivo(SpriteManager spriteManager) {
		super(spriteManager);
		
		//velocidad del zombi
		WALKING_SPEED=100;
		this.vida=100;
		
	
		
		//pongo el power up a false
		powerUp=false;
		
		//pongo de character si esta vivo o no
		this.dead=false;
		this.banderaMuerte=false;
		
		//inicializo los vectores de posicion y velocidad
		position=new Vector2();
		velocity=new Vector2(0,0);
		
		//inicializo el pintador
		shapeRenderer = new ShapeRenderer();
		
		//hago que el primer estado sea parado
		state=State.IDLE;
		
		//le damos altura y anchura
		alturaRectangulo=78;
		anchuraRectangulo=43;
		
		//cojo el textureAtlas de zombi.pack
		TextureAtlas atlas=ResourceManager.assets.get("characters/zombi_bomba/zombi_bomba.pack", TextureAtlas.class);
		TextureAtlas atlasBomba=ResourceManager.assets.get("characters/zombi_bomba/explosion.pack", TextureAtlas.class);
		
		
		//pongo la animacion de bomba
		boom= new Animation<TextureRegion>(0.8f, atlasBomba.findRegions("explosion"));
		
		//cogemos la posicion de parado en el currentframe
		currentFrame = atlas.findRegion("parado",-1);
		
		//cogemos la anchura  y la altura del frame anterior
		this.WIDTH=currentFrame.getRegionWidth();
		this.HEIGHT=currentFrame.getRegionHeight();
		
		//cogemos las animaciones del zombi corriendo
		runRightAnimation=new Animation<TextureRegion>(0.04f,atlas.findRegions("correr_derecha"));
		runLeftAnimation=new Animation<TextureRegion>(0.04f,atlas.findRegions("correr_izquierda"));
		runUpAnimation= new Animation<TextureRegion>(0.04f, atlas.findRegions("correr_arriba"));
		runDownAnimation= new Animation<TextureRegion>(0.04f, atlas.findRegions("correr_abajo"));
		
		//animaciones del zombi atacando
		attackRight = new Animation<TextureRegion>(0.08f, atlas.findRegions("ataque_derecha"));
		attackLeft = new Animation<TextureRegion>(0.08f, atlas.findRegions("ataque_izquierda"));
		attackUp = new Animation<TextureRegion>(0.08f, atlas.findRegions("ataque_arriba"));
		attackDown = new Animation<TextureRegion>(0.08f, atlas.findRegions("ataque_abajo"));
		
		//animaciones del zombi muriendo
		deadRight = new Animation<TextureRegion>(0.4f, atlas.findRegions("muerte_derecha"));
		deadLeft = new Animation<TextureRegion>(0.4f, atlas.findRegions("muerte_izquierda"));
		deadUp = new Animation<TextureRegion>(0.4f, atlas.findRegions("muerte_arriba"));
		deadDown = new Animation<TextureRegion>(0.4f, atlas.findRegions("muerte_abajo"));
		
		//animacion del zombi parado
		idleAnimation=new Animation<TextureRegion>(0.3f,atlas.findRegions("parado"));
		
		//valores para cuadrar la caja para que colisione mas o menos perfecto 28 y 10 sirven para colocar el rectangulo perfectamente al personaje
		this.rect=new Rectangle(this.position.x+28,this.position.y+10,anchuraRectangulo,alturaRectangulo);
		
		//coloco la caja ataque de la mitad de tama�o del personaje para que cuando colisione ataque
		this.cajaAtaque=new Collider(this.position.x+35,this.position.y+15,this.HEIGHT/2, this.HEIGHT/2);
		this.cajaColisionZombies=new Collider(this.position.x+35,this.position.y+15,this.HEIGHT/4, this.HEIGHT/4);
		
		circuloexplosion = new Circle(-200, -200, 5);
		sonidoExplosion=ResourceManager.getSound("sounds/explosionBomba.mp3");
		
		this.cajaColisionZombiesCercaEnemigo= new Collider(this.position.x-40, this.position.y-40, this.WIDTH*2, this.HEIGHT*2);
	}

	@Override
	public void update(float dt) {
		
		//actualizamos el rectangunlo a la posicion con la suma de x+28 y+ 10 para que se centre lo mejor posible
		this.rect.setPosition(this.position.x+28, this.position.y+10);

		//colocamos la caja para que vaya centrada en el personaje
		this.cajaAtaque.rect.setPosition(this.position.x+24,this.position.y+24);
		this.cajaColisionZombies.rect.setPosition(this.position.x+34,this.position.y+44);
		
		this.cajaColisionZombiesCercaEnemigo.rect.setPosition(this.position.x-50,this.position.y-50);
		
		if (tiempoDecolisionzombie>1) {
			tiempoDecolisionzombie=0;
			banderaColisionZombi=0;
		}
	
		
		//compruebo si esta el estado es muerte para ver si hacemos unas cosas u otras
		switch (this.state) {
			case DEADDOWN:
				contadorMuerte=contadorMuerte+dt;
				if(spriteManager.prefs.getBoolean("sound")==true) {
					sonidoExplosion.play();
				}
				if(contadorMuerte>2){
					contadorMuerte=0;
					
					circuloexplosion.setPosition(this.position.x+this.currentFrame.getRegionWidth()/2, this.position.y+this.currentFrame.getRegionHeight()/2);
					this.state=State.BOOM;
					this.checkCollisions(spriteManager);
					//this.dead=true;
				}
				break;
			case DEADUP:
				contadorMuerte=contadorMuerte+dt;
				if(spriteManager.prefs.getBoolean("sound")==true) {
					sonidoExplosion.play();
				}
				if(contadorMuerte>2){
					contadorMuerte=0;
					circuloexplosion.setPosition(this.position.x+this.currentFrame.getRegionWidth()/2, this.position.y+this.currentFrame.getRegionHeight()/2);
				
					this.state=State.BOOM;
					this.checkCollisions(spriteManager);
					//this.dead=true;
				}
				break;
			case DEADLEFT:
				
				contadorMuerte=contadorMuerte+dt;
				if(spriteManager.prefs.getBoolean("sound")==true) {
					sonidoExplosion.play();
				}
				if(contadorMuerte>2){
					circuloexplosion.setPosition(this.position.x+this.currentFrame.getRegionWidth()/2, this.position.y+this.currentFrame.getRegionHeight()/2);					contadorMuerte=0;
					this.state=State.BOOM;
					this.checkCollisions(spriteManager);
					
					//this.dead=true;
				}
				break;
			case DEADRIGHT:
				contadorMuerte=contadorMuerte+dt;
				if(spriteManager.prefs.getBoolean("sound")==true) {
					sonidoExplosion.play();
				}
				if(contadorMuerte>2){
					contadorMuerte=0;
					
					this.state=State.BOOM;
					circuloexplosion.setPosition(this.position.x+this.currentFrame.getRegionWidth()/2, this.position.y+this.currentFrame.getRegionHeight()/2);					this.state=State.BOOM;
					//this.dead=true;
					this.checkCollisions(spriteManager);
				}
				break;
			case BOOM:
				contadorMuerte=contadorMuerte+dt;
				if(contadorMuerte>3){
					this.dead=true;
				}
				
				break;
				//si no le pongo el tiempo de ataque a 0
			default:
				
				break;
		}
		
	
		
		
		
		//compruebo si esta el estado de ataque y sumo el deltatiem
		switch (this.state) {
			case ATTACKDOWN:
				tiempoAtaque=tiempoAtaque+dt;
				break;
			case ATTACKUP:
				tiempoAtaque=tiempoAtaque+dt;
				break;
			case ATTACKLEFT:
				tiempoAtaque=tiempoAtaque+dt;
				break;
			case ATTACKRIGHT:
				tiempoAtaque=tiempoAtaque+dt;
				break;
				//si no le pongo el tiempo de ataque a 0
			default:
				tiempoAtaque=0;
				break;
		}
		
	if(this.banderaMuerte==false){
		if(banderaColisionZombi==0) {
			//compruebo el valor de x para saber si es menor que mi posicion de x y pongo una bandera de direccion para que solo pueda ir de
			//derecha izquuierda o arriba abajo
			if(spriteManager.player.position.x+ Constants.PLAYER_WIDTH/2<this.position.x && banderaDireccion==0){
				
				//compruebo que la colision esta activa o no para realizar las diferentes acciones
				if(this.colision==false){
					
					//si esta en la misma posicion que x le quito la velocidad en x y cambio la bandera para que vaya de arriba abajo
					if(Math.floor(spriteManager.player.position.x+ Constants.PLAYER_WIDTH/2)==Math.floor(this.position.x)){
						
						banderaDireccion=1;
						this.velocity.x=0;
						
					}else{
						this.velocity.y=0;
						this.velocity.x=-this.WALKING_SPEED;
						
						//cambio los diferentes estados
						this.state=State.RUNLEFT;
					}
				}
				//comprueba la posicion del jugador y va hacia esa direcion de x
			}else if(spriteManager.player.position.x+ Constants.PLAYER_WIDTH/2>this.position.x && banderaDireccion==0){
				
				if(this.colision==false){
					
					if(Math.floor(spriteManager.player.position.x+ Constants.PLAYER_WIDTH/2)==Math.floor(this.position.x)){
						
						this.velocity.x=0;
						banderaDireccion=1;
						
					}else{
						this.velocity.y=0;
						this.velocity.x=this.WALKING_SPEED;
						this.state=State.RUNRIGHT;
	
					}
				}
			
			} else if(spriteManager.player.position.y+ Constants.PLAYER_WIDTH/2<this.position.y && banderaDireccion==1){
			
				if(this.colision==false){
					
					if(Math.floor(spriteManager.player.position.y+ Constants.PLAYER_WIDTH/2)==Math.floor(this.position.y)) {
						
						this.velocity.y=0;
						banderaDireccion=0;
						
					}else {
						this.velocity.x=0;
						this.velocity.y=-this.WALKING_SPEED;
						this.state=State.RUNDOWN;
	
					}
				}
				
			}else if(spriteManager.player.position.y+ Constants.PLAYER_WIDTH/2>this.position.y && banderaDireccion==1){
				
				if(this.colision==false){
					
					if(Math.floor(spriteManager.player.position.y+ Constants.PLAYER_WIDTH/2)==Math.floor(this.position.y)) {
						
						this.velocity.y=0;
						banderaDireccion=0;
						
					}else {
						this.velocity.x=0;
						this.velocity.y=this.WALKING_SPEED;
						this.state=State.RUNUP;
	
					}
				}
				
			}
			
			//hago que si la colision del zombi es 1 que es cuando el zombi hace x colisiones con la pared hago que tenga un movimiento aleatorio a traves de las siguentes puntos
		}else if (banderaColisionZombi==1) {
		
			if(movimientoTrasColisionPared==1) {
				
				this.velocity.y=0;
				this.velocity.x=this.WALKING_SPEED;
				this.state=State.RUNRIGHT;
				
				
				contadorReseteoMovimiento=contadorReseteoMovimiento+dt;
				
			}else if(movimientoTrasColisionPared==2) {
			
				this.velocity.y=0;
				this.velocity.x=-this.WALKING_SPEED;
				this.state=State.RUNLEFT;
				
				
				contadorReseteoMovimiento=contadorReseteoMovimiento+dt;
				
			}else if(movimientoTrasColisionPared==3) {
				
				this.velocity.x=0;
				this.velocity.y=this.WALKING_SPEED;
				this.state=State.RUNUP;
				
				
				contadorReseteoMovimiento=contadorReseteoMovimiento+dt;
				
			}else if(movimientoTrasColisionPared==4) {
				
				this.velocity.x=0;
				this.velocity.y=-this.WALKING_SPEED;
				this.state=State.RUNDOWN;
			
				
				contadorReseteoMovimiento=contadorReseteoMovimiento+dt;
			}
		}else if(banderaColisionZombi==2) {
			if(movimientoTrasColisionZombi==1) {
				
				this.velocity.x=+WALKING_SPEED;
				this.state=State.RUNRIGHT;
				tiempoDecolisionzombie=tiempoDecolisionzombie+dt;
				
			}else if(movimientoTrasColisionZombi==2) {
			
				this.velocity.x=-WALKING_SPEED;
				this.state=State.RUNLEFT;
				tiempoDecolisionzombie=tiempoDecolisionzombie+dt;
				
			}else if(movimientoTrasColisionZombi==3) {
				
				this.velocity.y=+WALKING_SPEED;
				this.state=State.RUNUP;
				tiempoDecolisionzombie=tiempoDecolisionzombie+dt;
				
			}else if(movimientoTrasColisionZombi==4) {
				
				this.velocity.y=-WALKING_SPEED;
				this.state=State.RUNDOWN;
				tiempoDecolisionzombie=tiempoDecolisionzombie+dt;
			}
		}
		
		//compruebo si el usuario colisiona
		this.checkCollisions(spriteManager);
		
		//actualizo las posicion por el Delta Time
		this.position.add(this.velocity.x*dt, this.velocity.y*dt);
	}else{
		
	}
		
	}
	
	public void render(Batch spriteBatch){
		//pintamos el stateTime el timpo en el que se pintan las imagenes
		stateTime+=Gdx.graphics.getDeltaTime();
		
		//tipos de estados para pintar
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
				
			case ATTACKRIGHT: 
				currentFrame=attackRight.getKeyFrame(stateTime,true);
				break;
			case ATTACKDOWN:
				currentFrame = attackDown.getKeyFrame(stateTime,true);
				break;
			case ATTACKUP:
				currentFrame = attackUp.getKeyFrame(stateTime,true);
				break;
			case ATTACKLEFT:
				currentFrame = attackLeft.getKeyFrame(stateTime,true);
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
			case IDLE: 
				currentFrame=idleAnimation.getKeyFrame(stateTime,false);
				break;
			case BOOM: 
				currentFrame=boom.getKeyFrame(stateTime,true);
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
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.rect(this.cajaAtaque.rect.x,this.cajaAtaque.rect.y,this.cajaAtaque.rect.height,this.cajaAtaque.rect.width);
			shapeRenderer.rect(this.cajaColisionZombies.rect.x,this.cajaColisionZombies.rect.y,this.cajaColisionZombies.rect.height,this.cajaColisionZombies.rect.width);
			shapeRenderer.circle(circuloexplosion.x, circuloexplosion.y, circuloexplosion.area()/2);
			shapeRenderer.rect(this.cajaColisionZombiesCercaEnemigo.rect.x,this.cajaColisionZombiesCercaEnemigo.rect.y,this.cajaColisionZombiesCercaEnemigo.rect.height,this.cajaColisionZombiesCercaEnemigo.rect.width);
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
				if(this.rect.x>=p.getRectangle().x +p.getRectangle().getWidth()-5 && this.velocity.x<0){
					
					if(contadorColision>11) {
						this.velocity.x=0;
						
						this.movimientoTrasColisionPared=1;
						contadorColision++;
					}else {
						this.velocity.x=0;
						banderaDireccion=1;
						contadorColision++;
					}
					
				}else if(this.rect.x<p.getRectangle().x && this.velocity.x>0){
					
					if(contadorColision>11) {
						this.velocity.x=0;
					
						this.movimientoTrasColisionPared=2;
						contadorColision++;
					}else {
						this.velocity.x=0;
						banderaDireccion=1;
						contadorColision++;
					}
					
				}else if(this.rect.y>p.getRectangle().y + p.getRectangle().getHeight()-5 && this.velocity.y<0){
					
					if(contadorColision>11) {
						this.velocity.y=0;
						
						this.movimientoTrasColisionPared=3;
						contadorColision++;
					}else {
						this.velocity.y=0;
						banderaDireccion=0;
						contadorColision++;
					}
					
					
				}else if(this.rect.y<p.getRectangle().y && this.velocity.y>0){
				
					if(contadorColision>11) {
						this.velocity.y=0;
						this.movimientoTrasColisionPared=4;
						contadorColision++;
					}else {
						banderaDireccion=0;
						this.velocity.y=0;
						contadorColision++;
					}
				
					
				}
			
			}else {
				if (contadorColision>12) {
					banderaColisionZombi=1;
					
				}
				if(contadorReseteoMovimiento>5) {
					
					contadorReseteoMovimiento=0;
					banderaColisionZombi=0;
					contadorColision=0;
				}
			}
			
		}
		
		if(this.cajaColisionZombiesCercaEnemigo.rect.overlaps(spriteManager.player.rect)){
			banderaColisionConPlayer=true;
		}else{
			banderaColisionConPlayer=false;
		}
		
		for (int i = 0; i < spriteManager.enemys.size;i++) {
			
			
			
			if(spriteManager.enemys.get(i).getClass().getName().equals("com.mygdx.zombi.characters.EnemyZombiExplosivo")){
				EnemyZombiExplosivo enemy = null;
				enemy = (EnemyZombiExplosivo) spriteManager.enemys.get(i);
			
			if(!enemy.equals(this)) {
				if(enemy.cajaColisionZombies.rect.overlaps(this.cajaColisionZombies.rect)) {
					//colisionan rectangulos
					
					if(this.cajaColisionZombies.rect.overlaps(enemy.cajaColisionZombies.rect) && enemy.banderaMuerte==false){
						//hago las mismas comprobaciones que con las paredes pero con el zombi para poder movernos libremente
						if(banderaColisionConPlayer==false){
							if(this.cajaAtaque.rect.x>=enemy.cajaAtaque.rect.x && this.velocity.x<0){
								
								movimientoTrasColisionZombi=1;
								this.velocity.y=0;
								banderaColisionZombi=2;
						
							}else if(this.cajaColisionZombies.rect.x<enemy.cajaColisionZombies.rect.x && this.velocity.x>0 && enemy.banderaMuerte==false){
								
								movimientoTrasColisionZombi=2;
								this.velocity.y=0;
								banderaColisionZombi=2;
							}else if(this.cajaColisionZombies.rect.y>enemy.cajaColisionZombies.rect.y && this.velocity.y<0 && enemy.banderaMuerte==false){
						
								movimientoTrasColisionZombi=3;
								this.velocity.x=0;
								banderaColisionZombi=2;
							}else if(this.cajaColisionZombies.rect.y<enemy.cajaColisionZombies.rect.y && this.velocity.y>0 && enemy.banderaMuerte==false){
						
								this.velocity.x=0;
								movimientoTrasColisionZombi=4;
								banderaColisionZombi=2;
						
							}
						}else{
							if(this.cajaAtaque.rect.x>=enemy.cajaAtaque.rect.x && this.velocity.x<0){
							this.velocity.x=0;
							this.velocity.y=0;
						
							}else if(this.cajaColisionZombies.rect.x<enemy.cajaColisionZombies.rect.x && this.velocity.x>0 && enemy.banderaMuerte==false){
								this.velocity.x=0;
								this.velocity.y=0;
							}else if(this.cajaColisionZombies.rect.y>enemy.cajaColisionZombies.rect.y && this.velocity.y<0 && enemy.banderaMuerte==false){
								this.velocity.x=0;
								this.velocity.y=0;
								
							}else if(this.cajaColisionZombies.rect.y<enemy.cajaColisionZombies.rect.y && this.velocity.y>0 && enemy.banderaMuerte==false){
								this.velocity.x=0;
								this.velocity.y=0;
						
							}
						}
					}
			}else{
				
			}
		
				}
			}else if(spriteManager.enemys.get(i).getClass().getName().equals("com.mygdx.zombi.characters.EnemyZombiRed")){
				EnemyZombiRed enemy = null;
				enemy = (EnemyZombiRed) spriteManager.enemys.get(i);
			
			if(!enemy.equals(this)) {
				if(enemy.cajaColisionZombies.rect.overlaps(this.cajaColisionZombies.rect)) {
					//colisionan rectangulos
					
					if(this.cajaColisionZombies.rect.overlaps(enemy.cajaColisionZombies.rect) && enemy.banderaMuerte==false){
						//hago las mismas comprobaciones que con las paredes pero con el zombi para poder movernos libremente
						if(banderaColisionConPlayer==false){
							if(this.cajaAtaque.rect.x>=enemy.cajaAtaque.rect.x && this.velocity.x<0){
								
								movimientoTrasColisionZombi=1;
								this.velocity.y=0;
								banderaColisionZombi=2;
						
							}else if(this.cajaColisionZombies.rect.x<enemy.cajaColisionZombies.rect.x && this.velocity.x>0 && enemy.banderaMuerte==false){
								
								movimientoTrasColisionZombi=2;
								this.velocity.y=0;
								banderaColisionZombi=2;
							}else if(this.cajaColisionZombies.rect.y>enemy.cajaColisionZombies.rect.y && this.velocity.y<0 && enemy.banderaMuerte==false){
						
								movimientoTrasColisionZombi=3;
								this.velocity.x=0;
								banderaColisionZombi=2;
							}else if(this.cajaColisionZombies.rect.y<enemy.cajaColisionZombies.rect.y && this.velocity.y>0 && enemy.banderaMuerte==false){
						
								this.velocity.x=0;
								movimientoTrasColisionZombi=4;
								banderaColisionZombi=2;
						
							}
						}else{
							if(this.cajaAtaque.rect.x>=enemy.cajaAtaque.rect.x && this.velocity.x<0){
							this.velocity.x=0;
							this.velocity.y=0;
						
							}else if(this.cajaColisionZombies.rect.x<enemy.cajaColisionZombies.rect.x && this.velocity.x>0 && enemy.banderaMuerte==false){
								this.velocity.x=0;
								this.velocity.y=0;
							}else if(this.cajaColisionZombies.rect.y>enemy.cajaColisionZombies.rect.y && this.velocity.y<0 && enemy.banderaMuerte==false){
								this.velocity.x=0;
								this.velocity.y=0;
								
							}else if(this.cajaColisionZombies.rect.y<enemy.cajaColisionZombies.rect.y && this.velocity.y>0 && enemy.banderaMuerte==false){
								this.velocity.x=0;
								this.velocity.y=0;
						
							}
						}
					}
			}else{
				
			}
		
				}
			}else if(spriteManager.enemys.get(i).getClass().getName().equals("com.mygdx.zombi.characters.EnemyZombi")){
				EnemyZombi enemynormal = null;
				enemynormal = (EnemyZombi) spriteManager.enemys.get(i);
				
				if(!enemynormal.equals(this)) {
					if(enemynormal.cajaColisionZombies.rect.overlaps(this.cajaColisionZombies.rect)) {
						//colisionan rectangulos
						
						if(this.cajaColisionZombies.rect.overlaps(enemynormal.cajaColisionZombies.rect) && enemynormal.banderaMuerte==false){
							//hago las mismas comprobaciones que con las paredes pero con el zombi para poder movernos libremente
							if(banderaColisionConPlayer==false){
								if(this.cajaAtaque.rect.x>=enemynormal.cajaAtaque.rect.x && this.velocity.x<0){
									
									movimientoTrasColisionZombi=1;
									this.velocity.y=0;
									banderaColisionZombi=2;
							
								}else if(this.cajaColisionZombies.rect.x<enemynormal.cajaColisionZombies.rect.x && this.velocity.x>0 && enemynormal.banderaMuerte==false){
									
									movimientoTrasColisionZombi=2;
									this.velocity.y=0;
									banderaColisionZombi=2;
								}else if(this.cajaColisionZombies.rect.y>enemynormal.cajaColisionZombies.rect.y && this.velocity.y<0 && enemynormal.banderaMuerte==false){
							
									movimientoTrasColisionZombi=3;
									this.velocity.x=0;
									banderaColisionZombi=2;
								}else if(this.cajaColisionZombies.rect.y<enemynormal.cajaColisionZombies.rect.y && this.velocity.y>0 && enemynormal.banderaMuerte==false){
							
									this.velocity.x=0;
									movimientoTrasColisionZombi=4;
									banderaColisionZombi=2;
							
								}
							}else{
								if(this.cajaAtaque.rect.x>=enemynormal.cajaAtaque.rect.x && this.velocity.x<0){
								this.velocity.x=0;
								this.velocity.y=0;
							
								}else if(this.cajaColisionZombies.rect.x<enemynormal.cajaColisionZombies.rect.x && this.velocity.x>0 && enemynormal.banderaMuerte==false){
									this.velocity.x=0;
									this.velocity.y=0;
								}else if(this.cajaColisionZombies.rect.y>enemynormal.cajaColisionZombies.rect.y && this.velocity.y<0 && enemynormal.banderaMuerte==false){
									this.velocity.x=0;
									this.velocity.y=0;
									
								}else if(this.cajaColisionZombies.rect.y<enemynormal.cajaColisionZombies.rect.y && this.velocity.y>0 && enemynormal.banderaMuerte==false){
									this.velocity.x=0;
									this.velocity.y=0;
							
								}
							}
						}
				}else{
					
				}
			}
			
			}
			
			
		}
	
		//Compruebo si colisionan ambas cahas de ataque para hacer x cosas o no y coloco la colision a true
		if(this.cajaAtaque.rect.overlaps(spriteManager.player.cajaColisionZombi.rect)){
			
			//compruebo si esta tocando la caja de ataque y segun el movimiento hace una posicion u otra
			this.velocity.x=0;
			this.velocity.y=0;
			//pongo la colision a true
			colision=true;
			//hago un swich para saber que ataque tengo que activar
			switch (this.state) {
				case RUNLEFT:
					this.state=State.ATTACKLEFT;
					break;
				case RUNRIGHT:
					this.state=State.ATTACKRIGHT;
					break;
				case RUNDOWN:
					this.state=State.ATTACKDOWN;
					break;
				case RUNUP:
					this.state=State.ATTACKUP;
					break;
	
				default:
					break;
			}
		
			//redonde el numero del ataque para que ataque al 1 segundo de activarse la animacion y hago una bandera para que no ataque mas de 1 vez cuando no debe
			if(Math.floor(tiempoAtaque)==1.0 && banderaAtaque==0){
				switch (this.state) {
					case ATTACKDOWN:
						
						spriteManager.player.velocity.y=spriteManager.player.velocity.y-70;
						spriteManager.player.restarVida(damage);
						banderaAtaque=1;
						break;
						
					case ATTACKUP:
						
							spriteManager.player.velocity.y=spriteManager.player.velocity.y+70;
							spriteManager.player.restarVida(damage);
							banderaAtaque=1;
						break;
						
					case ATTACKLEFT:
						
							spriteManager.player.velocity.x=spriteManager.player.velocity.x-70;
							spriteManager.player.restarVida(damage);
							banderaAtaque=1;
							break;
							
					case ATTACKRIGHT:
						
						spriteManager.player.velocity.x=spriteManager.player.velocity.x+70;
						spriteManager.player.restarVida(damage);
						banderaAtaque=1;
							break;

				default:
					break;
				}
			}else{
				//si el tiempo es superior a 1.00 bajo los tiempos y la bandera de ataque a 0
				if(tiempoAtaque>1.000){
					tiempoAtaque=0;
					banderaAtaque=0;
				}
			}
		//si no false	
		}else{
			
			//si se suelta la colision colocamos el tiempo de ataque a 0 y soltamos la colision y la bandera la colocamos a 0
			tiempoAtaque=0;
			colision=false;
			banderaAtaque=0;
		}
		

		
		if(Intersector.overlaps(this.circuloexplosion, spriteManager.player.rect)) {

			spriteManager.player.restarVida(damageExplosion);
		}

	}
	
	public void restarVida(int damage){
		//hago que la vida baje con el da�o;
		this.vida=this.vida-damage;
		
		if (this.vida<=0) {
			this.spriteManager.hud.addScore(puntuacionZombiExplosivo);
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
				case ATTACKLEFT:
					this.state=State.DEADLEFT;
					break;
				case ATTACKRIGHT:
					this.state=State.DEADRIGHT;
					break;
				case ATTACKDOWN:
					this.state=State.DEADDOWN;
					break;
				case ATTACKUP:
					this.state=State.DEADUP;
					break;
	
				default:
					break;
			}
		}
	
	}

}
