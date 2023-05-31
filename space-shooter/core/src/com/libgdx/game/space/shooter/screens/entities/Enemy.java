package com.libgdx.game.space.shooter.screens.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

// Classe abstraite Enemy, qui étend la classe Entity
abstract public class Enemy extends Entity {

    public String name; // Nom de l'ennemi
    Vector2 directionVector; // Vecteur de direction de mouvement de l'ennemi

    public Enemy(float movementSpeed, int health, float xPosition, float yPosition,
                 float width, float height, Texture enemyTexture,
                 float projectileWidth, float projectileHeight, float projectileMovementSpeed,
                 float rateOfFire, Texture projectileTexture, String name) {
        super(movementSpeed, health, xPosition, yPosition, width, height, enemyTexture,
                projectileWidth, projectileHeight, projectileMovementSpeed, rateOfFire,
                projectileTexture);
        directionVector = new Vector2(0, -1); // Initialise le vecteur de direction vers le bas
        this.name = name; // Affecte le nom de l'ennemi
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime); // Appelle la méthode update() de la classe parent (Entity) qui met à jour
        // le temps ecoulé depuis le dernier tir


    }

    abstract public void moveOnPattern(float delta); // Méthode abstraite pour définir le modèle de déplacement de l'ennemi

}