package com.libgdx.game.space.shooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class MainMenuScreen implements Screen {

    private final float WIDTH;
    private final float HEIGHT;
    private static final int EXIT_BUTTON_WIDTH = 100;
    private static final int EXIT_BUTTON_HEIGHT = 100;
    private static final int PLAY_BUTTON_WIDTH = 100;
    private static final int PLAY_BUTTON_HEIGHT = 100;
    private static final int EXIT_BUTTON_Y = 250;
    private static final int PLAY_BUTTON_Y = 100;

    final SpaceShooterGame game;

    Texture logo;
    Texture background;
    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;

    public MainMenuScreen(final SpaceShooterGame game, float width, float height) {
        this.game = game;

        logo = new Texture("logo.png");
        background = new Texture("desert-background.png");
        playButtonActive = new Texture("play_button_active.png");
        playButtonInactive = new Texture("play_button_inactive.png");
        exitButtonActive = new Texture("exit_button_active.png");
        exitButtonInactive = new Texture("exit_button_inactive.png");

        WIDTH = width;
        HEIGHT = height;

        final MainMenuScreen mainMenuScreen = this;

        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                //Exit button
                int x = Math.round(WIDTH / 2 - EXIT_BUTTON_WIDTH / 2);
                if (game.cam.getInputInGameWorld().x < x + EXIT_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && HEIGHT - game.cam.getInputInGameWorld().y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && HEIGHT - game.cam.getInputInGameWorld().y > EXIT_BUTTON_Y) {
                    mainMenuScreen.dispose();
                    Gdx.app.exit();
                }

                //Play game button
                x = Math.round(WIDTH / 2 - PLAY_BUTTON_WIDTH / 2);
                if (game.cam.getInputInGameWorld().x < x + PLAY_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && HEIGHT - game.cam.getInputInGameWorld().y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && HEIGHT - game.cam.getInputInGameWorld().y > PLAY_BUTTON_Y) {
                    mainMenuScreen.dispose();
                    game.setScreen(new GameScreen(game));
                }

                return super.touchUp(screenX, screenY, pointer, button);
            }

        });
    }

    private void setupTextures(int x, int playButtonWidth, int playButtonY, int playButtonHeight, Texture playButtonActive, Texture playButtonInactive) {
        if (game.cam.getInputInGameWorld().x < x + playButtonWidth && game.cam.getInputInGameWorld().x > x && HEIGHT - game.cam.getInputInGameWorld().y < playButtonY + playButtonHeight && HEIGHT - game.cam.getInputInGameWorld().y > playButtonY) {
            game.batch.draw(playButtonActive, x, playButtonY, playButtonWidth, playButtonHeight);
        } else {
            game.batch.draw(playButtonInactive, x, playButtonY, playButtonWidth, playButtonHeight);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        game.batch.draw(background, 0, 0, 1600, 900);

        int x = Math.round(WIDTH / 2 - EXIT_BUTTON_WIDTH / 2);
        setupTextures(x, EXIT_BUTTON_WIDTH, EXIT_BUTTON_Y, EXIT_BUTTON_HEIGHT, exitButtonActive, exitButtonInactive);

        x = Math.round(WIDTH / 2 - PLAY_BUTTON_WIDTH / 2);
        setupTextures(x, PLAY_BUTTON_WIDTH, PLAY_BUTTON_Y, PLAY_BUTTON_HEIGHT, playButtonActive, playButtonInactive);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }
}
