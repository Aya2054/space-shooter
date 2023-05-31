package com.libgdx.game.space.shooter.screens.entities;

import com.badlogic.gdx.graphics.Texture;

public class MediumEnemy extends Enemy {

    private float angle = 0f; // Angle de rotation pour le mouvement circulaire
    private final float radius = 100f; // Rayon du cercle pour le mouvement circulaire
    private final float circleX; // Position X du centre du cercle
    private final float circleY; // Position Y du centre du cercle

    public MediumEnemy(float movementSpeed, int health, float xPosition, float yPosition, float width,
                       float height, Texture enemyTexture, float projectileWidth, float projectileHeight,
                       float projectileMovementSpeed, float rateOfFire, Texture projectileTexture) {
        super(movementSpeed, health, xPosition, yPosition, width, height, enemyTexture, projectileWidth,
                projectileHeight, projectileMovementSpeed, rateOfFire, projectileTexture, "Medium");
        this.circleX = xPosition;
        this.circleY = yPosition;
        this.rateOfFire = 1f; // Taux de tir spécifique pour cet ennemi moyen
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime); // Appelle la méthode update de la classe parent (Enemy)
        angle += deltaTime * 1.5; // Incrémente l'angle de rotation en fonction du temps écoulé
        if (angle >= 360f) angle -= 360f; // Réinitialise l'angle à 0 lorsqu'il atteint 360 degrés
    }

    @Override
    public void moveOnPattern(float delta) {
        // Déplace l'ennemi sur un modèle de mouvement circulaire
        thisRectangle.setPosition((float) (circleX + radius * Math.cos(angle)),
                (float) (circleY + radius * Math.sin(angle)));
    }

    @Override
    public Projectile[] shootOnPattern() {
        Projectile[] projectiles = new Projectile[3]; // Génère un tableau de trois projectiles pour l'ennemi
        projectiles[0] = new Projectile(projectileMovementSpeed, 0,
                thisRectangle.x + Math.round(thisRectangle.width * 0.25), // Position x du premier projectile
                thisRectangle.y, projectileWidth, projectileHeight,
                projectileTexture);
        projectiles[1] = new Projectile(projectileMovementSpeed, 0,
                thisRectangle.x + thisRectangle.width - Math.round(thisRectangle.width * 0.25), // Position x du deuxième projectile
                thisRectangle.y, projectileWidth, projectileHeight,
                projectileTexture);
        projectiles[2] = new Projectile(projectileMovementSpeed, 0,
                thisRectangle.x + thisRectangle.width / 2, // Position x du troisième projectile
                thisRectangle.y, projectileWidth, projectileHeight,
                projectileTexture);
        timeSinceLastShot = 0; // Réinitialise le temps écoulé depuis le dernier tir

        return projectiles; // Retourne le tableau de projectiles
    }
}