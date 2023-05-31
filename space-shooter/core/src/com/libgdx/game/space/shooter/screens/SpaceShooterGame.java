package com.libgdx.game.space.shooter.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.libgdx.game.space.shooter.screens.utilities.GameCamera;

import java.util.Random;

public class SpaceShooterGame extends Game {

    // Définition de la hauteur de la fenêtre du jeu
    public static float WINDOW_HEIGHT = 608;

    // Définition de la largeur de la fenêtre du jeu
    public static float WINDOW_WIDTH = 256;

    // Instance d'un objet Random pour générer des nombres aléatoires
    public static Random random = new Random();

    // Objet SpriteBatch pour le rendu graphique des objets
    public SpriteBatch batch;

    // Écran de jeu
    Screen gameScreen;

    // Caméra de jeu
    public GameCamera cam;

    // Méthode appelée lors de la création du jeu
    public void create() {
        // Initialisation du SpriteBatch
        batch = new SpriteBatch();

        // Initialisation de la caméra de jeu
        cam = GameCamera.INSTANCE;

        // Référence au jeu en cours
        SpaceShooterGame game = this;

        // Création de l'écran principal du jeu
        gameScreen = new MainMenuScreen(game, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Définition de l'écran principal comme écran actuel
        setScreen(gameScreen);
    }
}
