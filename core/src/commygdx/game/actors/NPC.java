package commygdx.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import commygdx.game.AI.DemoAI;
import commygdx.game.AI.graph.PathGraph;

public class NPC extends Character {
    private final DemoAI ai;
    private boolean facingRight;

    public NPC(Vector2 position, PathGraph graph) {
        super(position);
        this.setPosition(position.x, position.y);
        this.ai = new DemoAI(graph);
        this.facingRight = true;
    }

    @Override
    public void act(float delta) {
        ai.update(delta, getPosition());
        super.act(delta);
    }

    protected Texture getTexture() {
        return new Texture(Gdx.files.internal("Characters/npcSprite.png"));
    }

    protected void handleMovement() {
        Vector2 position;
        if (this.ai.left(this.getPosition())) {
            position = this.movementSystem.left();
            this.setPosition(position.x, position.y);
            if (this.facingRight) {
                this.sprite.flip(true, false);
                this.facingRight = false;
            }
        }

        if (this.ai.right(this.getPosition())) {
            position = this.movementSystem.right();
            this.setPosition(position.x, position.y);
            if (!this.facingRight) {
                this.sprite.flip(true, false);
                this.facingRight = true;
            }
        }

        if (this.ai.up(this.getPosition())) {
            position = this.movementSystem.up();
            this.setPosition(position.x, position.y);
        }

        if (this.ai.down(this.getPosition())) {
            position = this.movementSystem.down();
            this.setPosition(position.x, position.y);
        }

    }

}