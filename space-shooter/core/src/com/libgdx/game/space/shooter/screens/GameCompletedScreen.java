package com.libgdx.game.space.shooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

public class GameCompletedScreen implements Screen {
    //cette classe représente l'affichage du screen lorsque le joueur a bien terminé son jeu
    private static final int BANNER_HEIGHT = 100;
    private final float WIDTH;
    private final float HEIGHT;
    SpaceShooterGame game;
    int score, highscore;
    Texture background;
    BitmapFont scoreFont;

    public GameCompletedScreen(SpaceShooterGame game, int score, float width, float height) {
        this.game = game;
        this.score = score;

        WIDTH = width;
        HEIGHT = height;

        Preferences prefs = Gdx.app.getPreferences("spacegame");
        this.highscore = prefs.getInteger("highscore", 0);

        if (score > highscore) {
            prefs.putInteger("highscore", score);
            prefs.flush();
        }

        background = new Texture("desert-background.png");
        scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
    }

    @Override
    public void show() {
        // Méthode appelée lors de l'affichage de l'écran
    }

    @Override
    public void render(float delta) {
        // Efface l'écran avec une couleur noire
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        // Dessine l'arrière-plan
        game.batch.draw(background, 0, 0, 1600, 900);

        // Affiche le message de fin de jeu
        GlyphLayout gameOverLayout = new GlyphLayout(scoreFont, "You saved temrinal dogma!", Color.WHITE, 0, Align.left, false);
        scoreFont.draw(game.batch, gameOverLayout, WIDTH / 2 - gameOverLayout.width / 2, HEIGHT - BANNER_HEIGHT + 15 * 2);

        // Affiche le score et le highscore
        GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "Score: " + score, Color.WHITE, 0, Align.left, false);
        GlyphLayout highscoreLayout = new GlyphLayout(scoreFont, "Highscore: " + highscore, Color.WHITE, 0, Align.left, false);
        scoreFont.draw(game.batch, scoreLayout, WIDTH / 2 - scoreLayout.width / 2, HEIGHT - BANNER_HEIGHT - 15 * 2);
        scoreFont.draw(game.batch, highscoreLayout, WIDTH / 2 - highscoreLayout.width / 2, HEIGHT - BANNER_HEIGHT - scoreLayout.height - 15 * 3);

        // Récupère les coordonnées du toucher de l'utilisateur
        float touchX = game.cam.getInputInGameWorld().x, touchY = HEIGHT - game.cam.getInputInGameWorld().y;

        // Affiche les boutons "Try Again" et "Main Menu"
        GlyphLayout tryAgainLayout = new GlyphLayout(scoreFont, "Try Again");
        GlyphLayout mainMenuLayout = new GlyphLayout(scoreFont, "Main Menu");

        float mainMenuX = WIDTH / 2 - mainMenuLayout.width / 2;
        float mainMenuY = HEIGHT / 2 - mainMenuLayout.height / 2 - tryAgainLayout.height - 15;

        // Change la couleur du texte lorsque le bouton "Main Menu" est survolé
        if (touchX >= mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY >= mainMenuY - mainMenuLayout.height && touchY < mainMenuY)
            mainMenuLayout.setText(scoreFont, "Main Menu", Color.YELLOW, 0, Align.left, false);

        // Vérifie si l'utilisateur a cliqué sur un bouton
        if (Gdx.input.isTouched()) {
            // Redirige vers l'écran du menu principal si le bouton "Main Menu" est cliqué
            if (touchX > mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY > mainMenuY - mainMenuLayout.height && touchY < mainMenuY) {
                this.dispose();
                game.batch.end();
                game.setScreen(new MainMenuScreen(game, WIDTH, HEIGHT));
                return;
            }
        }

        // Dessine les boutons
        scoreFont.draw(game.batch, mainMenuLayout, mainMenuX, mainMenuY);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Méthode appelée lorsque la taille de l'écran est modifiée
    }

    @Override
    public void pause() {
        // Méthode appelée lorsqu'une interruption survient (ex: appuyer sur le bouton Home)
    }

    @Override
    public void resume() {
        // Méthode appelée lorsque l'application reprend après une interruption
    }

    @Override
    public void hide() {
        // Méthode appelée lorsque l'écran n'est plus visible
    }

    @Override
    public void dispose() {
        // Libère les ressources utilisées par l'écran
        background.dispose();
        scoreFont.dispose();
    }
}
