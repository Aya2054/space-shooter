package com.libgdx.game.space.shooter.screens.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.libgdx.game.space.shooter.screens.SpaceShooterGame;
/*
La caméra du jeu est un concept utilisé dans les jeux vidéo pour déterminer la portion de l'environnement
de jeu qui sera affichée à l'écran. Elle définit la vue que le joueur a sur le monde virtuel du jeu.
La caméra peut être déplacée et orientée pour suivre le personnage jouable ou pour donner
une perspective particulière.

 Elle est de type OrthographicCamera, ce qui signifie qu'elle offre une vue orthographique, c'est-à-dire une vue 2D sans perspective.*/

public class GameCamera {

    public static final GameCamera INSTANCE = new GameCamera(SpaceShooterGame.WINDOW_WIDTH, SpaceShooterGame.WINDOW_HEIGHT);
    private final OrthographicCamera cam; // La caméra orthographique utilisée pour la vue du jeu ::servi à avoir une vue 2D sans perspective

    //contenu du jeu s'adapte à différentes résolutions d'écran et rapports d'aspect.
    private final StretchViewport viewport; // Le viewport étirable utilisé pour ajuster la vue

    private GameCamera(float width, float height) {
        cam = new OrthographicCamera(); // Initialisation de la caméra
        viewport = new StretchViewport(width, height, cam); // Initialisation du viewport étirable avec les dimensions spécifiées
        viewport.apply(); // Appliquer les dimensions du viewport à la caméra
        cam.position.set(width / 2, height / 2, 0); // Positionner la caméra au centre de la fenêtre
        cam.update(); // Mettre à jour la caméra
    }

    public Vector2 getInputInGameWorld() {
        Vector3 inputScreen = new Vector3(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 0);
        // Récupérer les coordonnées de l'entrée utilisateur (clique de souris ou touche)
        // sur l'écran en utilisant les fonctions de la classe Gdx
        Vector3 unprojected = cam.unproject(inputScreen); // Convertir les coordonnées de l'écran en coordonnées
        // dans le monde du jeu en utilisant la caméra
        return new Vector2(unprojected.x, unprojected.y); // Retourner les coordonnées
        // dans le monde du jeu sous forme de Vector2
    }

}
