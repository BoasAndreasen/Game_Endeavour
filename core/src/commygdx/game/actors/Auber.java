package commygdx.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import commygdx.game.TileWorld;
import commygdx.game.input.PlayerInput;
import commygdx.game.stages.Hud;

import java.util.ArrayList;

public class Auber extends Character {
    protected boolean facingRight;
    private boolean invisible;

    // constructor is extended for asssesment 2 to make it easier to initialize
    // after loading from save
    public Auber(Vector2 position, boolean invisible, float speed) {
        super(position);
        shuffle();
        movementSystem.setSpeed(speed);
        facingRight = true;
        this.invisible = invisible;
        checkInvisibleTexture();
    }

    @Override
    protected Texture getTexture() {
        return new Texture(Gdx.files.internal("Characters/auberSprite.png"));
    }

    @Override
    protected void handleMovement() {
        //Left movement
        if (PlayerInput.getDirection() == 1) {
            Vector2 position = movementSystem.left();
            setPosition(position.x, position.y);
            if (facingRight) {
                sprite.flip(true, false);
                facingRight = false;
            }
        }
        //Right movement
        if (PlayerInput.getDirection() == 2) {
            Vector2 position = movementSystem.right();
            setPosition(position.x, position.y);
            if (!facingRight) {
                sprite.flip(true, false);
                facingRight = true;
            }
        }
        //Up movement
        if (PlayerInput.getDirection() == 3) {
            Vector2 position = movementSystem.up();
            setPosition(position.x, position.y);
        }
        //Down movement
        if (PlayerInput.getDirection() == 4) {
            Vector2 position = movementSystem.down();
            setPosition(position.x, position.y);
        }
    }

    /**
     * Checks if the auber is on a teleporter
     *
     * @param tiles Contains the teleporters
     * @return True if the auber is on a teleporter, false otherwise
     */
    public boolean teleportCheck(TileWorld tiles) {
        //check if standing on teleporter
        for (Rectangle rect : tiles.getTeleporters().values()) {
            if (sprite.getBoundingRectangle().contains(rect)) {
                return true;
            }
        }
        return false;
    }

    public String powerUpCheck(TileWorld tiles) {
        Rectangle healthrect = tiles.getHealthpowerUp();
        Rectangle invisrect = tiles.getInvispowerUp();
        Rectangle slowrect = tiles.getSlowpowerUp();
        Rectangle speedrect = tiles.getSpeedpowerUp();
        Rectangle shieldrect = tiles.getShieldpowerUp();

        if (sprite.getBoundingRectangle().contains(slowrect)) {
            return "Slow";
        }
        if (sprite.getBoundingRectangle().contains(speedrect)) {
            return "Speed";
        }
        if (sprite.getBoundingRectangle().contains(shieldrect)) {
            return "Shield";
        }
        if (sprite.getBoundingRectangle().contains(healthrect)) {
            return "Health";
        }
        if (sprite.getBoundingRectangle().contains(invisrect)) {
            return "Invis";
        } else {
            return "null";
        }
    }

    /**
     * Arrests any infiltrators in range of the player
     *
     * @param infiltrators A list of all infiltrators in the game
     * @param hud          The games HUD overlay
     */
    public void arrest(ArrayList<Infiltrator> infiltrators, Hud hud) {
        /*Arrests the infiltrator if in range and puts it in jail
         * @param infiltrators this list of infiltrators that are being checked
         * @hud the hud overlay*/
        if (PlayerInput.arrest()) {
            for (Infiltrator infiltrator : infiltrators) {
                if (Math.abs(infiltrator.getX() - this.getX()) < 100 && Math.abs(infiltrator.getY() - this.getY()) < 100) {
                    infiltrator.arrest(new Vector2((float) Math.random() * 1000 + 1200, (float) Math.random() * 400 + 5400));
                    hud.infiltratorCaught();
                }
            }
        }
    }

    /**
     * NEW FOR ASSESMENT 2: UR_HEAL
     * Damage auber if infiltrators are in range
     *
     * @param infiltrators A list of all infiltrators in the game
     * @param hud          The games HUD overlay
     */
    public void damageAuber(ArrayList<Infiltrator> infiltrators, Hud hud) {
        /* Damage auber if the infiltrators are in range
         * @param infiltrators this list of infiltrators that are being checked
         * @hud the hud overlay*/
        for (Infiltrator infiltrator : infiltrators) {
            if (Math.abs(infiltrator.getX() - this.getX()) < 100 && Math.abs(infiltrator.getY() - this.getY()) < 100) {
                hud.auberDamaged();
            }
        }
    }

    /**
     * moves the camera to the auber when game starts
     */
    public void shuffle() {
        Vector2 position = movementSystem.left();
        setPosition(position.x, position.y);
    }

    /**
     * NEW FOR ASSESMENT 2: UR_POWERUPS
     * Changes auber's texture to invisible
     */
    public void checkInvisibleTexture() {
        if (invisible) {
            sprite.setTexture(new Texture(Gdx.files.internal("Characters/infiltratorInvisibleSprite.png")));
        } else {
            sprite.setTexture(getTexture());
        }
    }

    public boolean getInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }
}
