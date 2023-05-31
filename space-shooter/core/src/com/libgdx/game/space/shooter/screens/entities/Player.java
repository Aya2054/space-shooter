package com.libgdx.game.space.shooter.screens.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {


    Rectangle hitBox; // Rectangle représentant la hitbox du joueur
    Vector2[] projectilesPosition; // Tableau de positions des projectiles
    int powerLevel; // Niveau de puissance du joueur
    int maxProjectiles; // Nombre maximum de projectiles


    public Player(float movementSpeed, int health, float xPosition, float yPosition, float width, float height, Texture entityTexture, float projectileWidth, float projectileHeight, float projectileMovementSpeed, float rateOfFire, Texture projectileTexture) {
// Constructeur de la classe Player qui initialise les attributs du joueur
        super(movementSpeed, health, xPosition, yPosition, width, height, entityTexture, projectileWidth, projectileHeight, projectileMovementSpeed, rateOfFire, projectileTexture);
        // Crée un rectangle pour la hitbox du joueur
        this.hitBox = new Rectangle(xPosition + width * 2.0F / 5.0F, yPosition + height * 2.0F / 5.0F, width / 2.0F, height / 5.0F);
        // Crée un tableau de positions pour les projectiles (5 maximum) et qui sont tous "null"
        this.projectilesPosition = new Vector2[5];
        // Position du premier des projectiles à (0, 0)
        this.projectilesPosition[0] = new Vector2(0.0F, 0.0F);
        // Positionne la première position des projectiles au-dessus du joueur
        this.projectilesPosition[0].set(this.thisRectangle.x + this.thisRectangle.width / 2.0F, this.thisRectangle.y + this.thisRectangle.height);
        // Initialise le niveau de puissance à 1
        this.powerLevel = 1;
        // Initialise le nombre maximum de projectiles à 1
        this.maxProjectiles = 1;
    }


    // Fonction qui crée un tableau de projectiles en utilisant les positions disponibles
    public Projectile[] shootOnPattern() {
// Crée un tableau de projectiles
        Projectile[] projectiles = new Projectile[this.maxProjectiles];

        // Boucle qui crée un projectile pour chaque position de projectile disponible
        for (int i = 0; i < this.maxProjectiles; ++i) {
        // Initialise un nouvel objet Projectile avec les paramètres appropriés
            projectiles[i] = new Projectile(
                    this.projectileMovementSpeed, // Vitesse de déplacement du projectile
                    0.0F, // Angle initial du projectile
                    this.projectilesPosition[i].x, // Coordonnée x de la position du projectile
                    this.projectilesPosition[i].y, // Coordonnée y de la position du projectile
                    this.projectileWidth, // Largeur du projectile
                    this.projectileHeight, // Hauteur du projectile
                    this.projectileTexture // Texture du projectile
            );
        }

// Réinitialise le temps écoulé depuis le dernier tir
        this.timeSinceLastShot = 0.0F;

        // Retourne les projectiles créés
        return projectiles;
    }



    // Fonction qui permet d'augmenter le niveau de puissance du joueur
    public void levelUp() {
        ++this.powerLevel; // Incrémente le niveau de puissance du joueur
        switch (this.powerLevel) {
            case 2:// Au niveau 2, le joueur peut tirer avec deux projectiles
                this.maxProjectiles = 2; // Met à jour le nombre maximum de projectiles
                // Positionne le premier projectile à gauche du joueur
                this.projectilesPosition[0].set(this.hitBox.x, this.thisRectangle.y + this.thisRectangle.height);
                // Positionne le deuxième projectile à droite du joueur
                this.projectilesPosition[1] = new Vector2(this.hitBox.x + this.hitBox.width, this.thisRectangle.y + this.thisRectangle.height);
                break;
            case 4:
// Au niveau 4, le joueur peut tirer avec trois projectiles
                this.maxProjectiles = 3;
                // Met à jour le nombre maximum de projectiles
                this.projectilesPosition[2] = new Vector2(this.thisRectangle.x + this.thisRectangle.width / 2.0F, this.thisRectangle.y + this.thisRectangle.height);
                // Positionne le troisième projectile au centre du joueur
                break;
            case 6:
// Au niveau 6, le joueur peut tirer avec quatre projectiles
                this.maxProjectiles = 4;
                // Met à jour le nombre maximum de projectiles
                this.projectilesPosition[2].set(this.hitBox.x - this.hitBox.width, this.thisRectangle.y + this.thisRectangle.height);
                // Positionne le troisième projectile à gauche du joueur
                this.projectilesPosition[3] = new Vector2(this.hitBox.x + this.hitBox.width * 2.0F, this.thisRectangle.y + this.thisRectangle.height);
                // Positionne le quatrième projectile à droite du joueur
                break;
            case 9:
// Au niveau 9, le joueur peut tirer avec cinq projectiles
                this.maxProjectiles = 5;
                // Met à jour le nombre maximum de projectiles
                this.projectilesPosition[4] = new Vector2(this.thisRectangle.x + this.thisRectangle.width / 2.0F, this.thisRectangle.y + this.thisRectangle.height);
                // Positionne le cinquième projectile au centre du joueur
        }
    }

}