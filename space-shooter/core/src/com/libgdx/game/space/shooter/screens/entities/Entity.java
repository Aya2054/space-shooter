package com.libgdx.game.space.shooter.screens.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Entity {
    public float movementSpeed; // Vitesse de déplacement
    public int health; // Points de vie de l'entité
    public Rectangle thisRectangle; // Rectangle de collision de l'entité
    Texture entityTexture, projectileTexture; // Textures pour l'entité et le projectile
    float projectileWidth, projectileHeight; // Dimensions du projectile
    float projectileMovementSpeed; // Vitesse de déplacement du projectile
    float rateOfFire; // Délai minimum entre 2 tirs successif
    float timeSinceLastShot = 0; // Temps écoulé depuis le dernier tir

    public Entity(float movementSpeed, int health, float xPosition, float yPosition,
                  float width, float height, Texture entityTexture,
                  float projectileWidth, float projectileHeight, float projectileMovementSpeed,
                  float rateOfFire, Texture projectileTexture) {
        this.movementSpeed = movementSpeed; // Initialise la vitesse de déplacement
        this.health = health; // Initialise les points de vie
        this.thisRectangle = new Rectangle(xPosition, yPosition, width, height); // Initialise le rectangle de collision
        this.entityTexture = entityTexture; // Initialise la texture de l'entité
        this.projectileTexture = projectileTexture; // Initialise la texture du projectile
        this.projectileWidth = projectileWidth; // Initialise la largeur du projectile
        this.projectileHeight = projectileHeight; // Initialise la hauteur du projectile
        this.projectileMovementSpeed = projectileMovementSpeed; // Initialise la vitesse de déplacement du projectile
        this.rateOfFire = rateOfFire; // Initialise le délai min entre 2 tirs succeffis
    }

    //Fonction qui met à jour le temps écoulé depuis le dernier tir,et qui est augmenté de deltaTime
    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
    }


    //Fonction qui vérifie si l'entité peut tirer en fonction du taux de tir
    // Si le temps écoulé est supérieur ou égal au taux de tir, cela signifie que l'objet est prêt à tirer, et la fonction retourne true. Sinon, elle retourne
    public boolean canFire() {
        return (timeSinceLastShot - rateOfFire >= 0);
    }


    // Méthode abstraite pour le tir de projectiles
    //Retourner un tableau de projectiles selon le modèle de tir
    public abstract Projectile[] shootOnPattern();



    // Vérifie si le rectangle de collision de l'entité se croise avec un autre rectangle
    public boolean intersects(Rectangle otherRectangle) {
        return thisRectangle.overlaps(otherRectangle);
    }


    //Vérifie si un projectile a touché une entité
    public boolean checkHit() {
        if (health > 0) {
            health--; // Réduit les points de vie de l'entité
            return false;
        }
        return true; // Renvoie vrai si l'entité est détruite
    }

    // Dessine l'entité avec sa texture
    public void draw(Batch batch) {
        batch.draw(entityTexture, thisRectangle.x, thisRectangle.y,
                thisRectangle.width, thisRectangle.height);
    }

    // Déplace l'entité selon les changements de position spécifiés
    public void translate(float xChange, float yChange) {
        thisRectangle.setPosition(thisRectangle.x + xChange, thisRectangle.y + yChange);
    }

}