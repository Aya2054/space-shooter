package com.libgdx.game.space.shooter.screens.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public abstract class BoostEntity {
    public Rectangle thisRectangle; // Rectangle de collision de l'entité
    float movementSpeed; // Vitesse de déplacement
    Texture boosterTexture; // Textures pour l'entité et la bulle

    public BoostEntity(float movementSpeed, float xPosition, float yPosition, float width, float height,
                       Texture boosterTexture) {
        this.movementSpeed = movementSpeed; // Initialise la vitesse de déplacement
        this.boosterTexture = boosterTexture; // Initialise la texture de l'entité
        this.thisRectangle = new Rectangle(xPosition, yPosition, width, height); // Initialise le rectangle de collision de l'entité
    }

    // C'est une méthode abstraite qui représente l'action du boost sur le joueur
    public abstract void boosterAction(Player player);


    //Déplace l'entité vers le bas en fonction de la vitesse de déplacement
    // et du temps écoulé depuis la dernière mise à jour du jeu (delta)
    public void move(float delta) {
        thisRectangle.setPosition(thisRectangle.x, thisRectangle.y - movementSpeed * delta);
    }

    // Dessine l'entité avec sa texture par Batch qui gère le rendu graphique
    public void draw(Batch batch) {
        batch.draw(boosterTexture, thisRectangle.x, thisRectangle.y, thisRectangle.width, thisRectangle.height);
    }

}