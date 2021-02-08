package commygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import commygdx.game.AuberGame;

//NEW FOR ASSESMENT 2: UR_DIFFICULTIES. Allows for 3 different difficulties (speeds) of the game
public class DifficultyScreen implements Screen {

    private final Texture difftexture;
    private final AuberGame game;
    private final boolean demo;
    private final boolean loadFromSave;

    public DifficultyScreen(AuberGame game, boolean demo, boolean loadFromSave) {
        this.game = game;
        this.demo = demo;
        this.loadFromSave = loadFromSave;
        difftexture = new Texture("diffsc.png");
        OrthographicCamera gamecam = new OrthographicCamera();
        gamecam.setToOrtho(true, AuberGame.V_WIDTH, AuberGame.V_HEIGHT);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(21 / 255f, 25 / 255f, 38 / 255f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //draw buttons
        game.batch.begin();
        game.batch.draw(difftexture, 40, 40, difftexture.getWidth() * 2, difftexture.getHeight() * 2);
        game.batch.end();

        //player enters either playing mode or demo mode with difficulty
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            game.gameState = 0;
            game.createPlayScreen(this.demo, this.loadFromSave, "easy");

        }

        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            game.gameState = 0;
            game.createPlayScreen(this.demo, this.loadFromSave, "normal");

        }

        if (Gdx.input.isKeyPressed(Input.Keys.C)) {
            game.gameState = 0;
            game.createPlayScreen(this.demo, this.loadFromSave, "hard");
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
