package com.libgdx.game.space.shooter.screens.entities;

import com.badlogic.gdx.graphics.Texture;
public class PowerUp extends BoostEntity {
    public PowerUp(float movementSpeed, float xPosition, float yPosition, float width, float height,
                   Texture boosterTexture) {
        super(movementSpeed, xPosition, yPosition, width, height, boosterTexture);
    }

    // Appelle la méthode levelUp() du joueur pour augmenter son niveau
    @Override
    public void boosterAction(Player player) {
        player.levelUp();
    }

}