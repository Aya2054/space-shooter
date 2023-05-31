package com.libgdx.game.space.shooter.screens.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class Projectile {
    // La vitesse de déplacement vertical du projectile
    public float yMovementSpeed;

    // La vitesse de déplacement horizontal du projectile
    public float xMovementSpeed;

    // Le rectangle de collision du projectile
    public Rectangle boundingBox;

    // La texture utilisée pour représenter visuellement le projectile
    Texture projectileTexture;


    //Instanciation du projectile avec ses attributs
    public Projectile(float yMovementSpeed, float xMovementSpeed, float xPosition, float yPosition, float width,
                      float height, Texture projectileTexture) {
        this.yMovementSpeed = yMovementSpeed;
        this.xMovementSpeed = xMovementSpeed;
        // Crée un rectangle de collision pour le projectile avec les coordonnées et les dimensions passé en paramètres
        this.boundingBox = new Rectangle(xPosition, yPosition, width, height);
        this.projectileTexture = projectileTexture;
    }


    // Dessine la texture du projectile aux coordonnées et aux dimensions du rectangle de collision
    public void draw(Batch batch) {
        batch.draw(projectileTexture, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }
}