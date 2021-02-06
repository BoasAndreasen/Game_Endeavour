package commygdx.game.syst;

import com.badlogic.gdx.math.Vector2;

public class MovementSystem {

    Collider collider;
    float movementSpeed;
    private boolean rightCollided, leftCollided, upCollided, downCollided;

    public MovementSystem(Vector2 position, float speed) {
        this.collider = new Collider(position);
        this.movementSpeed = speed;
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

    public float getMovementSpeed() {
        return movementSpeed;
    }
}
