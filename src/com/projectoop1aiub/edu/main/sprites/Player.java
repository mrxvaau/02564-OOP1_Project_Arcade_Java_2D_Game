package com.projectoop1aiub.edu.main.sprites;

import com.projectoop1aiub.edu.physics.Anim;

/**
    The Player.
*/
public class Player extends Obstacles
{

    private static final float JUMP_SPEED = -.95f;

    private boolean onGround;

    public Player(Anim left, Anim right, Anim deadLeft, Anim deadRight)
    {
        super(left, right, deadLeft, deadRight);
    }


    public void collideHorizontal() {
        setVelocityX(0);
    }


    public void collideVertical() {
        // check if collided with ground
        if (getVelocityY() > 0) {
            onGround = true;
        }
        setVelocityY(0);
    }


    public void setY(float y) {
        // check if falling
        if (Math.round(y) > Math.round(getY())) {
            onGround = false;
        }
        super.setY(y);
    }


    public void wakeUp() {
        // do nothing
    }


    /**
        Makes the player jump if the player is on the ground or
        if forceJump is true.
    */
    public void jump(boolean forceJump) {
        if (onGround || forceJump) {
            onGround = false;
            setVelocityY(JUMP_SPEED);
        }
    }


    public float getMaxSpeed() {
        return 0.5f;
    }

}
