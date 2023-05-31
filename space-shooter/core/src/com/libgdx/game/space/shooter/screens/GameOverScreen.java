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

public class GameOverScreen implements Screen {
    //cette classe représente l'affichage du screen lorsque le joueur n'a pas gagné son jeu
    private static final int BANNER_HEIGHT = 100;
    private final float WIDTH;
    private final float HEIGHT;
    SpaceShooterGame game;
    int score, highscore;
    Texture background;
    BitmapFont scoreFont;

    public GameOverScreen(SpaceShooterGame game, int score, float width, float height) {
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
    }

    @Override
    public void render(float delta) {
        // Effacer le tampon de couleur
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Début du dessin des éléments
        game.batch.begin();

        // Dessiner l'image de fond
        game.batch.draw(background, 0, 0, 1600, 900);

        // Dessiner le texte "GameOver"
        GlyphLayout gameOverLayout = new GlyphLayout(scoreFont, "GameOver", Color.WHITE, 0, Align.left, false);
        scoreFont.draw(game.batch, gameOverLayout, WIDTH / 2 - gameOverLayout.width / 2, HEIGHT - BANNER_HEIGHT + 15 * 2);

        // Dessiner le score et le highscore
        GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "Score: " + score, Color.WHITE, 0, Align.left, false);
        GlyphLayout highscoreLayout = new GlyphLayout(scoreFont, "Highscore: " + highscore, Color.WHITE, 0, Align.left, false);
        scoreFont.draw(game.batch, scoreLayout, WIDTH / 2 - scoreLayout.width / 2, HEIGHT - BANNER_HEIGHT - 15 * 2);
        scoreFont.draw(game.batch, highscoreLayout, WIDTH / 2 - highscoreLayout.width / 2, HEIGHT - BANNER_HEIGHT - scoreLayout.height - 15 * 3);

        // Récupérer les coordonnées du toucher
        float touchX = game.cam.getInputInGameWorld().x;
        float touchY = HEIGHT - game.cam.getInputInGameWorld().y;

        // Créer les objets GlyphLayout pour les boutons "Try Again" et "Main Menu"
        GlyphLayout tryAgainLayout = new GlyphLayout(scoreFont, "Try Again");
        GlyphLayout mainMenuLayout = new GlyphLayout(scoreFont, "Main Menu");

        // Calculer les coordonnées du bouton "Main Menu"
        float mainMenuX = WIDTH / 2 - mainMenuLayout.width / 2;
        float mainMenuY = HEIGHT / 2 - mainMenuLayout.height / 2 - tryAgainLayout.height - 15;

        // Vérifier si le toucher est sur le bouton "Main Menu"
        if (touchX >= mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY >= mainMenuY - mainMenuLayout.height && touchY < mainMenuY) {
            mainMenuLayout.setText(scoreFont, "Main Menu", Color.YELLOW, 0, Align.left, false);
        }

        // Vérifier si un toucher est enregistré
        if (Gdx.input.isTouched()) {
            // Si le toucher est sur le bouton "Main Menu"
            if (touchX > mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY > mainMenuY - mainMenuLayout.height && touchY < mainMenuY) {
                // Supprimer les ressources actuelles
                this.dispose();
                game.batch.end();

                // Passer à l'écran du menu principal
                game.setScreen(new MainMenuScreen(game, WIDTH, HEIGHT));
                return;
            }
        }

        // Dessiner les boutons
        scoreFont.draw(game.batch, mainMenuLayout, mainMenuX, mainMenuY);

        // Fin du dessin des éléments
        game.batch.end();
    }


    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }

}
