package com.mygdx.zombi.scenes;


import javax.swing.ImageIcon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.zombi.managers.ResourceManager;
import com.mygdx.zombi.managers.SpriteManager;


public class Hud implements Disposable{

    public Stage stage;
    private Viewport viewport;

    private Integer tiempo;
    private boolean timeUp; 
    private float timeCount;
    public static Integer puntuacion;
    private static Integer vida;

 
    private static Label etValorPuntuacion;
    private static Label etPuntuacion;
    private static Label etvida;
    private Label etValorTiempo;
    private Label etTiempo;
    private Label etImagen;
    private Label etMoneda;
   
    
 
    ResourceManager resourceManager;
    
 

    public Hud(SpriteBatch sb){
    	resourceManager = new ResourceManager();
        //define our tracking variables
        tiempo = 00;
        timeCount = 0;
        puntuacion = 0;
      

        //viewport = new FitViewport(1024,800,new OrthographicCamera());
        viewport = new FitViewport(1024,700);
        stage = new Stage(viewport,sb);
        // creamos una tabla para insertar lo que queremos ver en el hud
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        // etiquetas que se iran modificando los valores
       etValorTiempo = new Label(String.format("%06d", tiempo), new Label.LabelStyle(new BitmapFont(), Color.WHITE)); // el %03 es para que aparezcan 3 digitos
       etValorPuntuacion =new Label(String.format("%06d", puntuacion), new Label.LabelStyle(new BitmapFont(), Color.WHITE)); // el %06 es para que aparezcan 6 digitos
       etvida =new Label(String.format("%03d", vida), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
       // etiquetas de la parte superior
       etTiempo = new Label("TIEMPO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
       etPuntuacion = new Label("PUNTUACION", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
       etMoneda = new Label("VIDA", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
      
      
       	table.add(etPuntuacion).expandX().padTop(10);
        table.add(etTiempo).expandX().padTop(10);
        table.add(etMoneda).expandX().padTop(10);
        table.row();
        table.add(etValorPuntuacion).expandX();
        table.add(etValorTiempo).expandX();
        table.add(etvida).expandX();
        

      
        stage.addActor(table);

    }

    public void update(float dt){
        timeCount += dt;
        if(timeCount > 1){
        	tiempo++;
        	timeCount=0;
        }
        etValorTiempo.setText(String.format("%06d", tiempo));
    }

    public static void addScore(int value){
        puntuacion += value;
        etValorPuntuacion.setText(String.format("%06d", puntuacion));
    }
    
    public static void addVida(int value){
        vida = value;
        etvida.setText(String.format("%03d", vida));
    }

    @Override
    public void dispose() { stage.dispose(); }

    public boolean isTimeUp() { return timeUp; }

	
    
    
    
}