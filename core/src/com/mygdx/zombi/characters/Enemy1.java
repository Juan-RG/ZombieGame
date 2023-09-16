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
import com.mygdx.zombi.managers.ResourceManager;
import com.mygdx.zombi.managers.SpriteManager;
import com.mygdx.zombi.utils.Constants;


public class Enemy1 extends Enemy{
	public State state;

	//texturas de movimiento del enemigo
	private Animation<TextureRegion> runRightAnimation;
	private Animation<TextureRegion> runLeftAnimation;
	private Animation<TextureRegion> runUpAnimation;
	private Animation<TextureRegion> runDownAnimation;
	//animacion de parado
	private Animation<TextureRegion> idleAnimation;
	//animacion de ataque
	private Animation<TextureRegion> attackRight;
	private Animation<TextureRegion> attackLeft;
	private Animation<TextureRegion> attackUp;
	private Animation<TextureRegion> attackDown;
	//boolean de colision para saber si la caja ataque esta colisionando con el enemigo
	private boolean colision=false;
	//Colision para saber cuando tiene que atacar el personaje
	private Collider cajaAtaque;
	
	//bandera para que vaya en direccion derecha e izquierda, arriba y abajo
	public int banderaDireccion=0;
	
	//variables de tama�o del rectangulo de colision
	int anchuraRectangulo;
	int alturaRectangulo;
	
	
	//objeto para pintar
	private ShapeRenderer shapeRenderer;
	
	//Estados del enemigo
	public enum State{
		IDLE, RUNRIGHT, RUNLEFT, RUNUP, RUNDOWN, ATTACKRIGHT, ATTACKLEFT, ATTACKUP, ATTACKDOWN
	}

	//constructor
	public Enemy1(SpriteManager spriteManager) {
		super(spriteManager);
		// TODO Auto-generated constructor stub
		//velocidad del zombi
		WALKING_SPEED=60;
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
		TextureAtlas atlas=ResourceManager.assets.get("characters/zombi/zombi.pack", TextureAtlas.class);
		//cogemos la posicion de parado en el currentframe
		currentFrame = atlas.findRegion("parado",-1);
		
		//cogemos la anchura  y la altura del frame anterior
		this.WIDTH=currentFrame.getRegionWidth();
		this.HEIGHT=currentFrame.getRegionHeight();
		
		//cogemos las animaciones del zombi corriendo
		runRightAnimation=new Animation<TextureRegion>(0.3f,atlas.findRegions("correr_derecha"));
		runLeftAnimation=new Animation<TextureRegion>(0.3f,atlas.findRegions("correr_izquierda"));
		runUpAnimation= new Animation<TextureRegion>(0.3f, atlas.findRegions("correr_arriba"));
		runDownAnimation= new Animation<TextureRegion>(0.3f, atlas.findRegions("correr_abajo"));
		
		//animaciones del zombi atacando
		attackRight = new Animation<TextureRegion>(0.08f, atlas.findRegions("ataque_derecha"));
		attackLeft = new Animation<TextureRegion>(0.08f, atlas.findRegions("ataque_izquierda"));
		attackUp = new Animation<TextureRegion>(0.08f, atlas.findRegions("ataque_arriba"));
		attackDown = new Animation<TextureRegion>(0.08f, atlas.findRegions("ataque_abajo"));
		//animacion del zombi parado
		idleAnimation=new Animation<TextureRegion>(0.3f,atlas.findRegions("parado"));
		
		//valores para cuadrar la caja para que colisione mas o menos perfecto 28 y 10 sirven para colocar el rectangulo perfectamente al personaje
		this.rect=new Rectangle(this.position.x+28,this.position.y+10,anchuraRectangulo,alturaRectangulo);
		
		//coloco la caja ataque de la mitad de tama�o del personaje para que cuando colisione ataque
		this.cajaAtaque=new Collider(this.position.x+35,this.position.y+15,this.HEIGHT/2, this.HEIGHT/2);
		
	}

	@Override
	public void update(float dt) {
		//actualizamos el rectangunlo a la posicion con la suma de x+28 y+ 10 para que se centre lo mejor posible
		this.rect.setPosition(this.position.x+28, this.position.y+10);

		//colocamos la caja para que vaya centrada en el personaje
		this.cajaAtaque.rect.setPosition(this.position.x+24,this.position.y+24);

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
					this.velocity.x=-this.WALKING_SPEED;
					//cambio los diferentes estados
					this.state=State.RUNLEFT;
				}
		
			}
		}else if(spriteManager.player.position.x+ Constants.PLAYER_WIDTH/2>this.position.x && banderaDireccion==0){
			if(this.colision==false){
				if(Math.floor(spriteManager.player.position.x+ Constants.PLAYER_WIDTH/2)==Math.floor(this.position.x)){
					
					this.velocity.x=0;
					banderaDireccion=1;
				}else{
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
					this.velocity.y=this.WALKING_SPEED;
					this.state=State.RUNUP;
				}
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
			case IDLE: 
				currentFrame=idleAnimation.getKeyFrame(stateTime,true);
				break;
			
		}
		//pinto el enemigo y cierro el spriteBach
		spriteBatch.draw(currentFrame, position.x, position.y, this.WIDTH, this.HEIGHT);
		spriteBatch.end();
		
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
					
					this.velocity.x=0;
					banderaDireccion=1;
					
				}else if(this.rect.x<p.getRectangle().x && this.velocity.x>0){
					
					this.velocity.x=0;
					banderaDireccion=1;
					
				}else if(this.rect.y>p.getRectangle().y + p.getRectangle().getHeight()-5 && this.velocity.y<0){
					
					this.velocity.y=0;
					banderaDireccion=0;
					
				}else if(this.rect.y<p.getRectangle().y && this.velocity.y>0){
					
					banderaDireccion=0;
					this.velocity.y=0;
					
				}
			
			}
			
		}
	
		//Compruebo si colisionan ambas cahas de ataque para hacer x cosas o no y coloco la colision a true
		if(this.cajaAtaque.rect.overlaps(spriteManager.player.rect)){
			//compruebo si esta tocando la caja de ataque y segun el movimiento hace una posicion u otra
			this.velocity.x=0;
			this.velocity.y=0;
			colision=true;
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
		//si no false	
		}else{
			
			colision=false;
			
		}
				
	
		
	}

	@Override
	public void restarVida(int damage) {
		// TODO Auto-generated method stub
		
	}
	

}
