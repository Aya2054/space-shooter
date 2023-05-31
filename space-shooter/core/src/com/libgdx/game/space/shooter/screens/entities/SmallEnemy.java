package com.libgdx.game.space.shooter.screens.entities;

import com.badlogic.gdx.graphics.Texture;

public class SmallEnemy extends Enemy {

    private float angle = 0f; // Angle de rotation pour le mouvement circulaire
    private final float radius = 100f; // Rayon du cercle de déplacement
    private final float circleX; // Position X du centre du cercle
    private final float circleY; // Position Y du centre du cercle

    public SmallEnemy(float movementSpeed, int health, float xPosition, float yPosition, float width,
                      float height, Texture enemyTexture, float projectileWidth, float projectileHeight,
                      float projectileMovementSpeed, float rateOfFire, Texture projectileTexture) {
        super(movementSpeed, health, xPosition, yPosition, width, height, enemyTexture, projectileWidth,
                projectileHeight, projectileMovementSpeed, rateOfFire, projectileTexture, "Small");
        // Appelle le constructeur de la classe parent (Enemy) avec les paramètres fournis et définit le nom
        // de l'ennemi comme "Small"
        this.circleX = xPosition; // Définit la position X du centre du cercle comme la position actuelle
        this.circleY = yPosition; // Définit la position Y du centre du cercle comme la position actuelle
        this.rateOfFire = 1f; // Définit le taux de tir à 1 (tir régulier)
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime); // Appelle la méthode update de la classe parent (Enemy)
        angle += deltaTime * 1.5; // Incrémente l'angle de rotation en fonction du temps écoulé
        if (angle >= 360f) angle -= 360f; // Réinitialise l'angle à 0 lorsqu'il atteint 360 degrés
    }

    // Fonction de mouvement en modèle de cercle
    @Override
    public void moveOnPattern(float delta) {
        thisRectangle.setPosition((float) (circleX + radius * 1.15 * Math.cos(angle)), // Calcule la nouvelle position X sur le cercle
                (float) (circleY + radius / 3 * Math.sin(angle))); // Calcule la nouvelle position Y sur le cercle
        //Cela déplace l'ennemi verticalemen sur le cercle en fonction de l'angle et du rayon,
        // avec un facteur d'échelle de 1/3 appliqué.
    }

    // Fonction de tir en modèle de cercle
    @Override
    public Projectile[] shootOnPattern() {
        Projectile[] projectiles = new Projectile[1]; // Tableau de projectiles
        projectiles[0] = new Projectile(projectileMovementSpeed, 0,
                thisRectangle.x + thisRectangle.width / 2,
                thisRectangle.y, projectileWidth, projectileHeight,
                projectileTexture); // Projectile tiré en ligne droite vers l'avant
        timeSinceLastShot = 0; // Réinitialise le temps écoulé depuis le dernier tir

        return projectiles; // Retourne le tableau  de projectiles
    }



}