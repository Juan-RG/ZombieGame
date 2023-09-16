package com.mygdx.zombi.managers;



import java.awt.Point;
import java.nio.file.Watchable;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import com.mygdx.zombi.MyGame;
import com.mygdx.zombi.characters.Enemy;
import com.mygdx.zombi.characters.EnemyWorm;
import com.mygdx.zombi.characters.EnemyZombi;
import com.mygdx.zombi.characters.EnemyZombiExplosivo;
import com.mygdx.zombi.characters.EnemyZombiRed;
import com.mygdx.zombi.characters.Player;
import com.mygdx.zombi.items.Corazon;
import com.mygdx.zombi.items.Helicoptero;
import com.mygdx.zombi.scenes.Hud;
import com.mygdx.zombi.screens.GameOverScreen;
import com.mygdx.zombi.screens.WinerScreen;
import com.mygdx.zombi.utils.Constants;


public class SpriteManager {
	public MyGame game;
	
	//declaro el barch
	public Batch batch;
	
	//declaro la camara
	public OrthographicCamera camera;
	
	//hago un array de plataformas
	public Array<RectangleMapObject> platforms;

	//hago la camara del mapa de tiled
	public OrthogonalTiledMapRenderer mapRenderer;
	public TiledMap map;
	
	//declaro el jugador
	public Player player;
	
	//declaro la musica
	public Music music;
	public Music musicZombi;
	
	//declaro el hub
	public Hud hud;

	//genero array de enemigos
	public Array<Enemy> enemys;
	
	//genero array de corazones
	public Array<Corazon> items;
	
	//genero array de corazones
	public Array<Helicoptero> helicoptero;
	//numero de ronda
	int numRondas=1;
	int banderaMapa;
	
	public Preferences prefs;
	private Sound sonidoHelicoptero;
	
	//numero de monstruos
	int cantidadZombisNormales=14;
	int cantidadZombisRojos=4;
	int cantidadZombisExplosivos=2;
	int cantidadGusanos=1;
	int poscionAumentadax=20;
	int poscionAumentadaY=30;
	
	double banderaTiempoRondas;
	
	public SpriteManager(MyGame game,Hud hud){
		//inicializo
		 prefs = Gdx.app.getPreferences("zombi");
		
		
		this.game=game;
		//inicializo el batch y la camara
		batch=new SpriteBatch();
		
		//le paso el hud
		this.hud=hud;
		
		camera=new OrthographicCamera();
		camera.setToOrtho(false, 1024,700); //Modificar por parámetros
		camera.zoom=2/2f;
		platforms = new Array<RectangleMapObject>();
	
		//inicializo arrays
		enemys=new Array<Enemy>();
	
		//inicializo array de corazones
		items = new Array<Corazon>();
		
		helicoptero = new Array<Helicoptero>();
		//numRondas=1;
		numRondas=1;
		musicZombi=ResourceManager.getMusic("music/ruidoZombi.mp3");
		musicZombi.setLooping(true);
		
		//musica de fondo ruido zombi y juego
		music=ResourceManager.getMusic("music/cancionFondo.mp3");
		
		sonidoHelicoptero=ResourceManager.getSound("sounds/sonidoHelicoptero.mp3");
		music.setLooping(true);
		
	
		//cargoNivel
		loadCurrentLevel();
	}

	public void loadCurrentLevel(){
		banderaMapa=1;
		
		//añadp el mapa y cojo las colisiones
		map = new TmxMapLoader().load("levels/mapa1.tmx");
		
		// Crea el renderizador del tiledmap
		mapRenderer = new OrthogonalTiledMapRenderer(map);
		
		// Hay que utilizar el spritebatch del mapa para pintar el nivel.
		// En caso contrario no ubica ni escala bien al personaje en el mapa
		batch = mapRenderer.getBatch();
		mapRenderer.setView(camera);
		
		//cojo las colisiones
		MapLayer collisionsLayer = map.getLayers().get("colision");
		
		//y las añado al array de plataformas
		platforms.clear();
		for (MapObject object : collisionsLayer.getObjects()) {
		  RectangleMapObject rectangleObject = (RectangleMapObject) object;
		  platforms.add(rectangleObject);		  
		}
		
		//genero el player y lo coloco en una posicion
		player=new Player(this);
		player.position.set(15*Constants.TILE_WIDTH,10*Constants.TILE_HEIGHT);
		
		//limites minimos 30,30
		//maximos 3000,3000
		
		//player.position.set(2500,3000);
		//Enemy1 enem;
		//genero un enemigo y le añado posicion
		//for (int i = 0; i < 30; i++) {
		/**
		EnemyZombi enem;
		
		for (int i = 0; i < 30; i++) {
			enem=new EnemyZombi(this);
			enem.position.x=500+(i*50);
			enem.position0.y=300+(i*70);
			//enem.position.x=500;
			//enem.position.y=300;
			enemys.add(enem);
		}
		
		EnemyZombiRed enem1;
		
		for (int i = 0; i < 10; i++) {
			enem1=new EnemyZombiRed(this);
			enem1.position.x=500+(i*50);
			enem1.position.y=300+(i*70);
			//enem.position.x=500;
			//enem.position.y=300;
			enemys.add(enem1);
		}
	
		
		//EnemyZombiExplosivo em = new EnemyZombiExplosivo(this);
		//em.position.x=500+(50);
		//em.position.y=300+(70);
		//enem.position.x=500;
		//enem.position.y=300;
		//enemys.add(em);
		/**
		//EnemyZombi enem =new EnemyZombi(this);
			//enem.position.x=500;
			//enem.position.y=300;
			//enemys.add(enem);
			
			EnemyWorm worm = new EnemyWorm(this);
			worm.position.x=700;
			worm.position.y=400;
			enemys.add(worm);
	
	**/
		
		//Helicoptero he = new Helicoptero(this);
		
			//	helicoptero.add(he);
		
			loadEnemies();
		if(prefs.getBoolean("sound")==true) {
			music.play();
		}
		
	}
	
	private void loadEnemies() {
		if(numRondas==1){
			int positionx=200;
			int positiony=300;
			EnemyZombi enem;
			for (int i = 0; i < 5; i++) {
				enem= new EnemyZombi(this);
				positionx=positionx+(i*2);
				positiony=positiony+(i*2);
				enem.position.x=positionx;
				enem.position.y=positiony;
				enemys.add(enem);
			}
		}else if(numRondas==2){
			int positionx=700;
			int positiony=2000;
			EnemyZombi enem;
			for (int i = 0; i < 8; i++) {
				enem= new EnemyZombi(this);
				positionx=positionx+(i*2);
				positiony=positiony+(i*2);
				enem.position.x=positionx;
				enem.position.y=positiony;
				enemys.add(enem);
			}
			EnemyWorm worm = new EnemyWorm(this);
			worm.position.x=700;
			worm.position.y=400;
			enemys.add(worm);
		}else if(numRondas==3){
			int positionx=700;
			int positiony=2000;
			EnemyZombi enem;
			for (int i = 0; i < 10; i++) {
				enem= new EnemyZombi(this);
				positionx=positionx+(i*2);
				positiony=positiony+(i*2);
				enem.position.x=positionx;
				enem.position.y=positiony;
				enemys.add(enem);
			}
			EnemyWorm worm = new EnemyWorm(this);
			worm.position.x=700;
			worm.position.y=400;
			enemys.add(worm);
			
			EnemyZombiRed enem1;
			
			for (int i = 0; i < 2; i++) {
				enem1=new EnemyZombiRed(this);
				enem1.position.x=500+(i*50);
				enem1.position.y=300+(i*70);
				enemys.add(enem1);
			}
		}else if(numRondas==4){
			int positionx=2000;
			int positiony=500;
			int positionx1=700;
			int positiony1=400;
			EnemyZombi enem;
			for (int i = 0; i < 10; i++) {
				enem= new EnemyZombi(this);
				positionx1=positionx1+(i*2);
				positiony1=positiony1+(i*2);
				enem.position.x=positionx1;
				enem.position.y=positiony1;
				enemys.add(enem);
			}
			EnemyZombiRed enem1;
			for (int i = 0; i < 3; i++) {
				enem1=new EnemyZombiRed(this);
				enem1.position.x=positionx+(i*50);
				enem1.position.y=positiony+(i*70);
				enemys.add(enem1);
			}
		}else if(numRondas==5){
			
			Helicoptero he = new Helicoptero(this);
			helicoptero.add(he);
			
			if(prefs.getBoolean("sound")==true){
				sonidoHelicoptero.play();
			}
			int positionx=2000;
			int positiony=500;
			int positionx1=700;
			int positiony1=400;
			EnemyZombi enem;
			for (int i = 0; i < 15; i++) {
				enem= new EnemyZombi(this);
				positionx1=positionx1+(i*2);
				positiony1=positiony1+(i*2);
				enem.position.x=positionx1;
				enem.position.y=positiony1;
				enemys.add(enem);
			}
			EnemyZombiRed enem1;
			for (int i = 0; i < 2; i++) {
				enem1=new EnemyZombiRed(this);
				enem1.position.x=positionx+(i*50);
				enem1.position.y=positiony+(i*70);
				enemys.add(enem1);
			}
		}else{
			
			EnemyZombi enem = null;
			 cantidadZombisNormales=cantidadZombisNormales+numRondas;
			for (int i = 0; i < cantidadZombisNormales; i++) {
				enem= new EnemyZombi(this);
			
				int numAleatorioPoscionx = (int) (Math.random()*3000 + 20);
				int numAleatorioPosciony = (int) (Math.random()*3000 + 30);
				if(banderaMapa==1) {
				enem.position.x=numAleatorioPoscionx;
				enem.position.y=numAleatorioPosciony;
				}else {
					boolean banderaPosicion=true;
					while(banderaPosicion) {
				
						if(numAleatorioPoscionx>1400 && numAleatorioPoscionx<2500) {
							if(numAleatorioPosciony>1800 && numAleatorioPosciony<3100) {
								numAleatorioPoscionx = (int) (Math.random()*3000 + 20);
								numAleatorioPosciony = (int) (Math.random()*3000 + 30);
								banderaPosicion=true;
								
							}else {
								enem.position.x=numAleatorioPoscionx;
								enem.position.y=numAleatorioPosciony;
								banderaPosicion=false;
								
							}
						}else {
							enem.position.x=numAleatorioPoscionx;
							enem.position.y=numAleatorioPosciony;
							banderaPosicion=false;
							
						}
					}
					
				}
				}
				enemys.add(enem);
			
			
			EnemyZombiRed enem1;
			cantidadZombisRojos=cantidadZombisRojos+numRondas;
			
			for (int i = 0; i < cantidadZombisRojos; i++) {
				enem1=new EnemyZombiRed(this);
				int numAleatorioPoscionx = (int) (Math.random()*3000 + 20);
				int numAleatorioPosciony = (int) (Math.random()*3000 + 30);
				if(banderaMapa==1) {
					enem1.position.x=numAleatorioPoscionx;
					enem1.position.y=numAleatorioPosciony;
					}else {
						boolean banderaPosicion=true;
						while(banderaPosicion) {
							
							if(numAleatorioPoscionx>1400 && numAleatorioPoscionx<2500) {
								if(numAleatorioPosciony>1800 && numAleatorioPosciony<3100) {
									numAleatorioPoscionx = (int) (Math.random()*3000 + 20);
									numAleatorioPosciony = (int) (Math.random()*3000 + 30);
									banderaPosicion=true;
									
								}else {
									enem1.position.x=numAleatorioPoscionx;
									enem1.position.y=numAleatorioPosciony;
									banderaPosicion=false;
									
								}
							}else {
								enem1.position.x=numAleatorioPoscionx;
								enem1.position.y=numAleatorioPosciony;
								banderaPosicion=false;
								
							}
						}
						
					}
				enemys.add(enem1);
			}
			
			
			EnemyZombiExplosivo enem2;
			cantidadZombisExplosivos=cantidadZombisExplosivos+numRondas;
			for (int i = 0; i < cantidadZombisExplosivos; i++) {
				enem2=new EnemyZombiExplosivo(this);
				int numAleatorioPoscionx = (int) (Math.random()*3000 + 20);
				int numAleatorioPosciony = (int) (Math.random()*3000 + 30);
				if(banderaMapa==1) {
					enem2.position.x=numAleatorioPoscionx;
					enem2.position.y=numAleatorioPosciony;
					}else {
						boolean banderaPosicion=true;
						while(banderaPosicion) {
							
							if(numAleatorioPoscionx>1400 && numAleatorioPoscionx<2500) {
								if(numAleatorioPosciony>1800 && numAleatorioPosciony<3100) {
									numAleatorioPoscionx = (int) (Math.random()*3000 + 20);
									numAleatorioPosciony = (int) (Math.random()*3000 + 30);
									banderaPosicion=true;
									
								}else {
									enem2.position.x=numAleatorioPoscionx;
									enem2.position.y=numAleatorioPosciony;
									banderaPosicion=false;
									
								}
							}else {
								enem2.position.x=numAleatorioPoscionx;
								enem2.position.y=numAleatorioPosciony;
								banderaPosicion=false;
								
							}
						}
						
					}
				
				enemys.add(enem2);
			}
			
			
			EnemyWorm enem3;
			cantidadGusanos=cantidadGusanos+numRondas;
			for (int i = 0; i < cantidadGusanos; i++) {
				enem3=new EnemyWorm(this);
				int numAleatorioPoscionx = (int) (Math.random()*3000 + 20);
				int numAleatorioPosciony = (int) (Math.random()*3000 + 30);
				if(banderaMapa==1) {
					enem3.position.x=numAleatorioPoscionx;
					enem3.position.y=numAleatorioPosciony;
					}else {
						boolean banderaPosicion=true;
						while(banderaPosicion) {
							
							if(numAleatorioPoscionx>1400 && numAleatorioPoscionx<2500) {
								if(numAleatorioPosciony>1800 && numAleatorioPosciony<3100) {
									numAleatorioPoscionx = (int) (Math.random()*3000 + 20);
									numAleatorioPosciony = (int) (Math.random()*3000 + 30);
									banderaPosicion=true;
									
								}else {
									enem3.position.x=numAleatorioPoscionx;
									enem3.position.y=numAleatorioPosciony;
									banderaPosicion=false;
									
								}
							}else {
								enem3.position.x=numAleatorioPoscionx;
								enem3.position.y=numAleatorioPosciony;
								banderaPosicion=false;
								
							}
						}
						
					}
				enemys.add(enem3);
			}
		
		}
		
	}

	public void loadCurrentLevel2(){
		banderaMapa=2;
		//añadp el mapa y cojo las colisiones
		map = new TmxMapLoader().load("levels/level2/mapa2.tmx");
		
		// Crea el renderizador del tiledmap
		mapRenderer = new OrthogonalTiledMapRenderer(map);
		
		// Hay que utilizar el spritebatch del mapa para pintar el nivel.
		// En caso contrario no ubica ni escala bien al personaje en el mapa
		batch = mapRenderer.getBatch();
		mapRenderer.setView(camera);
		
		//cojo las colisiones
		MapLayer collisionsLayer = map.getLayers().get("colision");
		
		platforms.clear();
		//y las añado al array de plataformas
		for (MapObject object : collisionsLayer.getObjects()) {
		  RectangleMapObject rectangleObject = (RectangleMapObject) object;
		  platforms.add(rectangleObject);		  
		}
		
		//genero el player y lo coloco en una posicion
		player=new Player(this);
		player.position.set(15*Constants.TILE_WIDTH,10*Constants.TILE_HEIGHT);
		
		//player.position.set(2300,2800);
		//Enemy1 enem;
		//genero un enemigo y le añado posicion
		//for (int i = 0; i < 30; i++) {
		/**
		EnemyZombi enem;
		
		for (int i = 0; i < 30; i++) {
			enem=new EnemyZombi(this);
			enem.position.x=500+(i*50);
			enem.position.y=300+(i*70);
			//enem.position.x=500;
			//enem.position.y=300;
			enemys.add(enem);
		}
		
		EnemyZombiRed enem1;
		
		for (int i = 0; i < 1; i++) {
			enem1=new EnemyZombiRed(this);
			enem1.position.x=500+(i*50);
			enem1.position.y=300+(i*70);
			//enem.position.x=500;
			//enem.position.y=300;
			enemys.add(enem1);
		}
		
		
		EnemyZombiExplosivo em = new EnemyZombiExplosivo(this);
		em.position.x=500+(50);
		em.position.y=300+(70);
		//enem.position.x=500;
		//enem.position.y=300;
		enemys.add(em);
		
		//EnemyZombi enem =new EnemyZombi(this);
			//enem.position.x=500;
			//enem.position.y=300;
			//enemys.add(enem);
			
			EnemyWorm worm = new EnemyWorm(this);
			worm.position.x=700;
			worm.position.y=400;
			enemys.add(worm);
	
	**/
			//musica de fondo ruido zombi y juego
			if(prefs.getBoolean("sound")==true) {
				music.play();
			}
			
		
		
		
	}
	
	public void update(float dt){
		//actualizp el hub
		this.hud.update(dt);
		
		//si hay enemigos hay suena zombies
		if(enemys.size>0) {
			if(prefs.getBoolean("sound")==true) {
				musicZombi.play();
			}
		}else {
			if(numRondas==10){
				this.game.setScreen(new WinerScreen(this.game,this));
			}
			musicZombi.stop();
			
			banderaTiempoRondas=banderaTiempoRondas+dt;
			if(banderaTiempoRondas>5){
				numRondas++;
				banderaTiempoRondas=0;
				loadEnemies();
			}
			
		}
		
		//comprobamos las entradas de la aplicacion
		handleInput(dt);
		
		//añado el hub la vida del jugador
		this.hud.addVida(player.getVida());
		
		//pasamos el dt a los players y a los enemigos
		player.update(dt);
		for(Enemy enemy : enemys){
			enemy.update(dt);
		}
		
		//actualizamos los corazones
		for(Corazon c : items){
			c.update(dt);
		}
		
		for(Helicoptero c : helicoptero){
			c.update(dt);
		}
	
		//hago que la camara siga al usuario
		camera.position.set(player.position.x, player.position.y, 0);
			
		//actualizo la camras
		camera.update();
		
		
	}
	
	public void draw(){
		//pinto la camara en el batch y actualizo la camra
		batch.setProjectionMatrix(camera.combined);
		camera.update();
		
		//introduzco el mapa en camara y los renderizo
		mapRenderer.setView(camera);
		mapRenderer.render();
		Enemy temp;
		  for(int i=1;i < enemys.size;i++){
	            for (int j=0 ; j < enemys.size- 1; j++){
	                if (enemys.get(j).position.y < enemys.get(j+1).position.y){
	                    temp = enemys.get(j);
	                    enemys.set(j,  enemys.get(j+1));
	                    enemys.set(j+1,temp);
	                }
	            }
	        }
	    
		
		//empezamos el batch del jugador y de los enemigos
		batch.begin();
	
		for(Enemy enem : enemys){
			
			//compruebo la posicion para que no se superponga el zombi al personaje
			if(player.position.y<=enem.position.y){
				
				//si no esta muerto lo pinto
				if (enem.dead==false) {
					enem.render(batch);
					
				}else {
					
					//si esta muerto y es un gusano genero un corazon
					if (enem.getClass().getName().equals("com.mygdx.zombi.characters.EnemyWorm")) {
						
						//si esto da problemas generarlo cuando el gusano muere
						Corazon newCorazon = new Corazon(this);
						newCorazon.posicion(enem.position.x, enem.position.y);
						items.add(newCorazon);
						
						//elimino al enemigo del array
						this.enemys.removeValue(enem, false);
						
					
						
					}else {
						//elimino el enemigo del array
						this.enemys.removeValue(enem, false);
					}
				}
			}
		}
		
		//pinto los corazones si estan activos
		for (Corazon corazon : items) {
			if(corazon.activo==true) {
				corazon.render(batch);
			}else {
				//si no los elimino
				items.removeValue(corazon, false);
			}
		}
		
		for (Helicoptero helicoptero : helicoptero) {
			if(helicoptero.activo==true) {
				helicoptero.render(batch);
			}else {
				//si no los elimino
				this.helicoptero.removeValue(helicoptero, false);
			}
		}
		
		//pinto el juegador
		player.render(batch);
		
		//segundo bucle para mostrarlo correctamente
		for(Enemy enem : enemys){
			
			if(player.position.y>enem.position.y){
				
				//hago lo mismo que en el bucle de arriba
				if (enem.dead==false) {
				
					enem.render(batch);
					
					
				}else {
					if (enem.getClass().getName().equals("com.mygdx.zombi.characters.EnemyWorm")) {
						Corazon newCorazon = new Corazon(this);
						newCorazon.posicion(enem.position.x, enem.position.y);
						items.add(newCorazon);
						
						this.enemys.removeValue(enem, false);
						
					}else {
						this.enemys.removeValue(enem, false);
					}
				}
				
			}
		}
		
	
		batch.end();
	}
	
	private void handleInput(float dt){
		
		//compruebo que teclas se estan pulsando si pulsas spacio compruebo tu ultimo caso y realizo una funcion u otra
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			
			if (player.velocity.x>0 ) {
				player.velocity.x = player.velocity.x-20;
			}else if(player.velocity.x<0){
				player.velocity.x = player.velocity.x+20;
			}
			
			if (player.velocity.y>0 ) {
				player.velocity.y = player.velocity.y-20;
			}else if(player.velocity.y<0){
				player.velocity.y = player.velocity.y+20;
			}
			
			
			switch (player.state) {
				case IDLEDOWN: 
					player.state = Player.State.SHOOTDOWN;
				
					break;
				case IDLEUP: 
					player.state = Player.State.SHOOTUP;
					break;
				case IDLERIGHT: 
					player.state = Player.State.SHOOTRIGT;
					break;
				case IDLELEFT: 
					player.state = Player.State.SHOOTLEFT;
					break;
				case RUNLEFT: 
					player.state = Player.State.SHOOTLEFT;
					player.velocity.x=0;
					break;
				case RUNRIGHT: 
					player.state = Player.State.SHOOTRIGT;
					player.velocity.x=0;
					break;
				case RUNDOWN: 
					player.state = Player.State.SHOOTDOWN;
					player.velocity.y=0;
					break;
				case RUNUP: 
					player.state = Player.State.SHOOTUP;
					player.velocity.y=0;
					break;
				default:
					break;
				}
			//si preto la tecla de izquierda quito la velocidad en y y muevo la x hacia la izquierda lo mismo en lo siguientes casos
		}else if (Gdx.input.isKeyPressed(Keys.LEFT)) {	
			player.velocity.x = -Player.WALKING_SPEED;
			player.velocity.y = 0;
			player.state = Player.State.RUNLEFT;
		}else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			player.velocity.x = Player.WALKING_SPEED;
			player.velocity.y=0;
			player.state = Player.State.RUNRIGHT;
		}else if (Gdx.input.isKeyPressed(Keys.UP)) {
			player.velocity.y = Player.WALKING_SPEED;
			player.velocity.x = 0;

			player.state = Player.State.RUNUP;
		}else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			player.velocity.y = -Player.WALKING_SPEED;
			player.state = Player.State.RUNDOWN;
			player.velocity.x = 0;
		}else {
			//si no pulsa tecla reduzco o agrando la velocidad de la x y lo mismo de y
			if (player.velocity.x>0 ) {
				player.velocity.x = player.velocity.x-20;
			}else if(player.velocity.x<0){
				player.velocity.x = player.velocity.x+20;
			}
			
			if (player.velocity.y>0 ) {
				player.velocity.y = player.velocity.y-20;
			}else if(player.velocity.y<0){
				player.velocity.y = player.velocity.y+20;
			}
			
			//y si esta parado compruebo su ultimo movimiento y pongo un estado u otro
			switch (player.state) {
				case RUNDOWN:
					player.state = Player.State.IDLEDOWN;
					break;
				case RUNUP:
					player.state = Player.State.IDLEUP;
					break;
				case RUNRIGHT:
					player.state = Player.State.IDLERIGHT;
					break;
				case RUNLEFT:
					player.state = Player.State.IDLELEFT;
					break;
				case SHOOTDOWN:
					player.state = Player.State.IDLEDOWN;
					break;
				case SHOOTLEFT:
					player.state = Player.State.IDLELEFT;
					break;
				case SHOOTRIGT:
					player.state = Player.State.IDLERIGHT;
					break;
				case SHOOTUP:
					player.state = Player.State.IDLEUP;
					break;
					
	
				default:
					
					break;
				}
			
		}

	
	}
	
	
}
