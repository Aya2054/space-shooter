package com.libgdx.game.space.shooter;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.libgdx.game.space.shooter.screens.SpaceShooterGame;

public class DesktopLauncher {
	public static void main(String[] arg) {
		// Configuration de l'application LibGDX pour le bureau
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		// Définition du nombre d'images par seconde souhaité
		config.setForegroundFPS(60);

		// Définition de la taille de la fenêtre
		config.setWindowedMode(256, 608);

		// Définition du titre de la fenêtre
		config.setTitle("Space shooter Game");

		// Création de l'application LibGDX avec la configuration spécifiée et le jeu principal
		new Lwjgl3Application(new SpaceShooterGame(), config);
	}
}
