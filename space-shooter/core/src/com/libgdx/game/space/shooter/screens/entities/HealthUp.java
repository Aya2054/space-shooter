package com.libgdx.game.space.shooter.screens.entities;

import com.badlogic.gdx.graphics.Texture;

//Classe de la santé des boots
public class HealthUp extends BoostEntity {


    public HealthUp(float movementSpeed, float xPosition, float yPosition, float width, float height,
                    Texture boosterTexture) {
        super(movementSpeed, xPosition, yPosition, width, height, boosterTexture);
    }

    // Augmente la santé du joueur (points de vie)
    @Override
    public void boosterAction(Player player) {
        player.health++;
    }

}