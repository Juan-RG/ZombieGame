package com.mygdx.zombi.screens;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.zombi.MyGame;
import com.badlogic.gdx.Screen;



public class ConfigurationScreen implements Screen {

	MyGame game;
    private Stage stage;
    private Preferences prefs;
	
	public ConfigurationScreen(MyGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Pinta el menú
        stage.act();
        stage.draw();
	}

	@Override
	public void resize(int width, int height) {
        //stage.setViewport(width, height);
	}

	@Override
	public void show() {

        loadPreferences();
        loadScreen();
	}

    private void loadScreen() {

        stage = new Stage();

        Table table = new Table(game.getSkin());
        table.setFillParent(true);
        table.center();

        Label title = new Label("Zombi\nAjustes", game.getSkin());
        title.setFontScale(2.5f);

        final CheckBox checkSound = new CheckBox("Sonido", game.getSkin());
        checkSound.setChecked(prefs.getBoolean("sound"));
        checkSound.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                prefs.putBoolean("sound", checkSound.isChecked());
            }
        });
        final CheckBox checkColision = new CheckBox("colisiones", game.getSkin());
        checkColision.setChecked(prefs.getBoolean("colision"));
        checkColision.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                prefs.putBoolean("colision", checkColision.isChecked());
            }
        });

        TextButton exitButton = new TextButton("Menu", game.getSkin());
        exitButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                prefs.flush();
                dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        Label aboutLabel = new Label("Zombi\n(c) Juan Antonio Rodriguez", game.getSkin());
        aboutLabel.setFontScale(1f);

        table.row().height(150);
        table.add(title).center().pad(35f);
        table.row().height(20);
        table.add(checkSound).center().pad(5f);
        table.row().height(40);
        table.add(checkColision).center().pad(5f);
        table.row().height(40);
        table.add(exitButton).center().width(200).pad(5f);
        table.row().height(40);
        table.add(aboutLabel).center().pad(55f);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    private void loadPreferences() {
        prefs = Gdx.app.getPreferences("zombi");

        // Coloca los valores por defecto (para la primera ejecución)
        if (!prefs.contains("sound")) {
            prefs.putBoolean("sound", true);
        }
        if (!prefs.contains("colision"))
            prefs.putBoolean("colision", true);
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
	}
}
