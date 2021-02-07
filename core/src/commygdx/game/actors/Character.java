package commygdx.game.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import commygdx.game.syst.MovementSystem;

import java.util.List;

public abstract class Character extends Actor {
    public Sprite sprite;
    public MovementSystem movementSystem;


    public Character(Vector2 position) {

        sprite = new Sprite(getTexture());
        sprite.setSize(150, 170);
        movementSystem = new MovementSystem(position, 8);
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    protected abstract Texture getTexture();

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    @Override
    public void act(float delta) {
        handleMovement();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
    }


    protected abstract void handleMovement();

    @Override
    protected void positionChanged() {
        super.positionChanged();
        sprite.setPosition(getX(), getY());
    }

    /**
     * Checks if the character is colliding with any of the collision boxes
     *
     * @param collisionBoxes The collision boxes the character could be colliding with
     * @return True if the character is colliding with one or more of the boxes, false otherwise
     */
    public void checkCollision(List<Rectangle> collisionBoxes) {
        boolean one = false, two = false, three = false, four = false;
        for (Rectangle collisionBox : collisionBoxes) {
            // Coming from left
            Rectangle rectangle = new Rectangle();
            rectangle.x = sprite.getBoundingRectangle().x - movementSystem.getMovementSpeed() * 4;
            rectangle.y = sprite.getBoundingRectangle().y;
            rectangle.width = sprite.getBoundingRectangle().width;
            rectangle.height = sprite.getBoundingRectangle().height;

            if (rectangle.overlaps(collisionBox)) {
                movementSystem.setLeftCollided(true);
                one = true;
            }

            // coming from right
            rectangle.x = sprite.getBoundingRectangle().x + movementSystem.getMovementSpeed() * 4;
            rectangle.y = sprite.getBoundingRectangle().y;

            if (rectangle.overlaps(collisionBox)) {
                movementSystem.setRightCollided(true);
                two = true;
            }
            // Coming from up
            rectangle.x = sprite.getBoundingRectangle().x;
            rectangle.y = sprite.getBoundingRectangle().y + movementSystem.getMovementSpeed() * 4;

            if (rectangle.overlaps(collisionBox)) {
                movementSystem.setUpCollided(true);
                three = true;
            }

            // Coming from down
            rectangle.x = sprite.getBoundingRectangle().x;
            rectangle.y = sprite.getBoundingRectangle().y - movementSystem.getMovementSpeed() * 4;

            if (rectangle.overlaps(collisionBox)) {
                movementSystem.setDownCollided(true);
                four = true;
            }
        }

        if (!one && !two && !three && !four) {
            movementSystem.setDownCollided(false);
            movementSystem.setUpCollided(false);
            movementSystem.setLeftCollided(false);
            movementSystem.setRightCollided(false);
        }
    }
}

