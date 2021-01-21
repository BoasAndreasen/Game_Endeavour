package commygdx.game.syst;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class MovementSystem {

    Collider collider;
    float movementSpeed;
    private int direction;
    private boolean rightCollided, leftCollided, upCollided, downCollided;
    //direction 1=left 2=right 3=up 4=down

    public MovementSystem(Vector2 position, float speed) {
        this.collider = new Collider(position);
        this.movementSpeed = speed;
        this.direction = 0;
    }

    public void updatePos(Vector2 position) {
        this.collider = new Collider(position);
    }

    public void setSpeed(float speed) {
        movementSpeed = speed;
    }

    /**
     * Calculates the position of a character after a left move
     *
     * @return The new position of the character
     */
    public Vector2 left() {
        Vector2 newPos = collider.position;
        if (!leftCollided) {
            rightCollided = false;
            newPos.x -= movementSpeed;
            direction = 1;
        }
        return newPos;
    }

    /**
     * Calculates the position of a character after a right move
     *
     * @return The new position of the character
     */
    public Vector2 right() {
        Vector2 newPos = collider.position;
        if (!rightCollided) {
            leftCollided = false;
            newPos.x += movementSpeed;
            this.direction = 2;
        }

        return newPos;
    }

    /**
     * Calculates the position of a character after a upwards move
     *
     * @return The new position of the character
     */
    public Vector2 up() {
        Vector2 newPos = collider.position;
        if (!upCollided) {
            downCollided = false;
            newPos.y += movementSpeed;
            this.direction = 3;
        }
        return newPos;
    }

    /**
     * Calculates the position of a character after a downwards move
     *
     * @return The new position of the character
     */
    public Vector2 down() {
        Vector2 newPos = collider.position;
        if (!downCollided) {
            upCollided = false;
            newPos.y -= movementSpeed;
            this.direction = 4;
        }
        return newPos;
    }

    public void setRightCollided(boolean collided) {
        this.rightCollided = collided;
    }

    public void setLeftCollided(boolean collided) {
        this.leftCollided = collided;
    }

    public void setUpCollided(boolean collided) {
        this.upCollided = collided;
    }

    public void setDownCollided(boolean collided) {
        this.downCollided = collided;
    }

    public boolean isRightCollided() {
        return rightCollided;
    }

    public boolean isLeftCollided() {
        return leftCollided;
    }

    public boolean isUpCollided() {
        return upCollided;
    }

    public boolean isDownCollided() {
        return downCollided;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }
}
