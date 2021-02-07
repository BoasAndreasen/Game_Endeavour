package commygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import commygdx.game.AuberGame;

public class IntroScreen implements Screen {
    private final Texture introTexture;
    private final AuberGame game;

    public IntroScreen(AuberGame game) {
        this.game = game;
        introTexture = new Texture("IntroV3.png");
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
        game.batch.draw(introTexture, 40, 40, introTexture.getWidth() * 2, introTexture.getHeight() * 2);
        game.batch.end();

        // normal mode without save
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            game.createDifficultyScreen(false, false);
            game.gameState = 5;
        }

        // enter save
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            game.createDifficultyScreen(false, true);
            game.gameState = 5;
        }

        //demo mode
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            game.createPlayScreen(true, false, "easy");
            game.gameState = 0;
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
