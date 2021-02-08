package commygdx.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import commygdx.game.AI.InfiltratorAI;
import commygdx.game.AI.graph.PathGraph;
import commygdx.game.Screens.PlayScreen;
import commygdx.game.ShipSystem;

public class Infiltrator extends Character {
    private final InfiltratorAI ai;
    private boolean isArrested;
    //0=none, 1=invisibility, 2=hallucination 3=shapeshift 4=speed boost
    private final int power;
    private float powerCooldown;
    private float powerDuration;
    private ShipSystem destroyingSystem = null;
    private float destructionTimer = 0;
    private boolean facingRight;

    // constructor is extended for asssesment 2 to make it easier to initialize
    // after loading from save
    public Infiltrator(Vector2 position, int power, PathGraph graph, boolean isArrested,
                       float powerCooldown, float powerDuration, float speed) {
        super(position);
        this.setPosition(position.x, position.y);
        this.power = power;
        this.powerCooldown = powerCooldown;
        this.powerDuration = powerDuration;
        this.isArrested = isArrested;
        this.ai = new InfiltratorAI(graph);
        this.facingRight = true;
        movementSystem.setSpeed(speed);
    }

    @Override
    public void act(float delta) {
        // set system that was being destroyed back to normal if arrested
        if (isArrested) {
            if (destroyingSystem != null) {
                destroyingSystem.setState(0);
            }
            return;
        }
        if (destroyingSystem != null) {
            destructionTimer += delta * 100;
            float TIME_TO_DESTROY = 500f;
            if (destructionTimer > TIME_TO_DESTROY) {
                destroyingSystem.destroy();
                destroyingSystem = null;
            }
        } else {
            ai.update(delta, new Vector2(getX(), getY()));
            super.act(delta);
        }
    }

    /**
     * NEW FOR ASSESMENT 2: UR_SPECIAL_ABILIEIES. Improved powerup and powerDuration consistency and saveability
     *
     * @param screen Playscreen
     * @param room Room
     */
    public void usePower(PlayScreen screen, String room) {
        if (power == 1) {
            sprite.setTexture(new Texture(Gdx.files.internal("Characters/infiltratorInvisibleSprite.png")));
        } else if (power == 2 && !room.equals("infirmary")) {
            screen.setHallucinate(true);
        } else if (power == 3) {
            sprite.setTexture(new Texture(Gdx.files.internal("Characters/npcSprite.png")));
        } else if (power == 4) {
            movementSystem.setSpeed(20f);
        }
        powerDuration += 1;
    }

    /**
     * NEW FOR ASSESMENT 2: UR_SPECIAL_ABILITIES. Improved powerup and powerDuration consistency and saveability
     * Resets enemy to normal after duration is done.
     */
    public void resetPower() {
        sprite.setTexture(new Texture(Gdx.files.internal("Characters/infiltratorSprite.png")));
        powerCooldown = 0;
        powerDuration = 0;
        movementSystem.setSpeed(6f);
    }

    public float getPowerDuration() {
        return powerDuration;
    }

    public float getPowerCooldown() {
        return powerCooldown;
    }

    public int getPower() {
        return power;
    }

    @Override
    protected Texture getTexture() {
        return new Texture(Gdx.files.internal("Characters/infiltratorSprite.png"));
    }

    @Override
    protected void handleMovement() {
        if (ai.left(new Vector2(getX(), getY()), isArrested)) {
            Vector2 pos = movementSystem.left();
            setPosition(pos.x, pos.y);
            if (facingRight) {
                sprite.flip(true, false);
                facingRight = false;
            }
        }
        if (ai.right(new Vector2(getX(), getY()), isArrested)) {
            Vector2 pos = movementSystem.right();
            setPosition(pos.x, pos.y);
            if (!facingRight) {
                sprite.flip(true, false);
                facingRight = true;
            }
        }
        if (ai.up(new Vector2(getX(), getY()), isArrested)) {
            Vector2 pos = movementSystem.up();
            setPosition(pos.x, pos.y);
        }
        if (ai.down(new Vector2(getX(), getY()), isArrested)) {
            Vector2 pos = movementSystem.down();
            setPosition(pos.x, pos.y);
        }
    }

    /**
     * Arrests the infiltrator, sending them to the given coordinates and setting them to arrested mode
     *
     * @param coords The coordinates the infiltrator will be sent to
     */
    public void arrest(Vector2 coords) {
        isArrested = true;
        setPosition(coords.x, coords.y);
    }

    public void updateTimers() {
        powerCooldown += 1;
    }

    public boolean isAvailable() {
        return !(isArrested && destroyingSystem != null);
    }

    /**
     * Begins the destruction process of the given system
     *
     * @param system The system being destroyed
     */
    public void startDestruction(ShipSystem system) {
        destroyingSystem = system;
        destructionTimer = 0;
    }

    public boolean getIsArrested() {
        return isArrested;
    }

    public void setSpeed(float speed) {
        movementSystem.setSpeed(speed);
    }
}


