package com.mygdx.zombi.screens;





import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.zombi.MyGame;


public class GameOverScreen implements Screen {
	
	final MyGame game;
	OrthographicCamera camera;
	private Stage stage;
	
	public GameOverScreen(MyGame game) {
		this.game = game;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1024, 768);
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(150, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act();
		stage.draw();
		/**
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);;
		
	
		// Muestra un menú de inicio
		game.batch.begin();
		game.font.draw(game.batch, "Fin del juego!!!!", 400, 450);
		game.font.draw(game.batch, "Si quieres jugar otra partida pulsa la tecla 'N'", 400, 440);
		game.font.draw(game.batch, "Pulsa 'ESCAPE' para SALIR", 400, 190);
		game.batch.end();
		
		/*
		 * Si el usuario toca la pantalla se inicia la partida
		 */
		if (Gdx.input.isKeyPressed(Keys.N)) {
			/*
			 * Aquí habrá que reiniciar algunos aspectos del
			 * game de cara a empezar una nueva partida
			 */			
			game.setScreen(new GameScreen(game));
		}
		/*
		 * El usuario pulsa la tecla ESCAPE, se sale del game
		 */
		else if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			dispose();
			System.exit(0);
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		loadScreen();
	}
	
	private void loadScreen() {
		// Grafo de escena que contiene el menú
		stage = new Stage();
					
		// Crea una tabla, donde añadiremos los elementos de menú
		Table table = new Table(game.getSkin());
        table.setFillParent(true);
        table.center();

        // Etiqueta de texto
        Label label = new Label("Fin del juego", game.getSkin());
        label.setFontScale(2.5f);
        Label label1 = new Label("Si quieres jugar otra partida pulsa la tecla 'N'", game.getSkin());
        label.setFontScale(2.5f);
        Label label2 = new Label("Pulsa 'ESCAPE' para SALIR", game.getSkin());
        label.setFontScale(2.5f);
      

        TextButton quitButton = new TextButton("Salir", game.getSkin());
        quitButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                System.exit(0);
            }
        });

        Label aboutLabel = new Label("Zombi\n(c) Juan Antonio Rodriguez", game.getSkin());
        aboutLabel.setFontScale(1f);

        table.row().height(100);
        table.add(label).center().pad(35f);
        table.row().height(40);
        table.add(label1).center().pad(35f);
        table.row().height(40);
        table.add(label2).center().pad(35f);
        table.row().height(40);
        table.add(quitButton).center().width(200).pad(5f);
        table.row().height(40);
        table.add(aboutLabel).center().pad(55f);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		game.dispose();
	}
}
