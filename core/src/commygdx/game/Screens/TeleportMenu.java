package commygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import commygdx.game.AuberGame;

public class TeleportMenu implements Screen {
    private final AuberGame game;
    private final Texture mapTexture;
    private final OrthographicCamera gamecam;
    private final Rectangle brigRect;
    private final Rectangle commandRect;
    private final Rectangle crewRect;
    private final Rectangle engineRect;
    private final Rectangle infirmaryRect;
    private final Rectangle laboratoryRext;

    public TeleportMenu(AuberGame game) {
        this.game = game;
        gamecam = new OrthographicCamera();
        gamecam.setToOrtho(false, AuberGame.V_WIDTH, AuberGame.V_HEIGHT);
        //get button textures
        mapTexture = new Texture("mapv3_signs.png");

        //teleport buttons
        brigRect = new Rectangle(870, 465, 183, 308);
        commandRect = new Rectangle(1143, 1090, 313, 181);
        laboratoryRext = new Rectangle(1121, 796, 357, 222);
        engineRect = new Rectangle(1143, 213, 315, 232);
        infirmaryRect = new Rectangle(1121, 508, 359, 223);
        crewRect = new Rectangle(1543, 466, 182, 307);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //draw menu
        Gdx.gl.glClearColor(21 / 255f, 25 / 255f, 38 / 255f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int zoom = 7;
        int textureWidth = mapTexture.getWidth() * zoom;
        int textureHeight = mapTexture.getHeight() * zoom;

        //draw buttons
        game.batch.begin();
        game.batch.draw(mapTexture, 850, 200, textureWidth, textureHeight);
        game.batch.end();

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            onClick(Gdx.input.getX(), Gdx.input.getY());
        }
    }

    private void onClick(int x, int y) {
        //convert click screen cords to world
        Vector3 clickPos = new Vector3(x, y, 0);
        gamecam.unproject(clickPos);

        //check which button pressed
        if (brigRect.contains(clickPos.x, clickPos.y)) {
            game.onTeleport = "brig";
        }

        else if (commandRect.contains(clickPos.x, clickPos.y)) {
            game.onTeleport = "command";
        }

        else if (crewRect.contains(clickPos.x, clickPos.y)) {
            game.onTeleport = "crew";
        }
        else if (engineRect.contains(clickPos.x, clickPos.y)) {
            game.onTeleport = "engine";
        }
        else if (infirmaryRect.contains(clickPos.x, clickPos.y)) {
            game.onTeleport = "infirmary";
        }
        else if (laboratoryRext.contains(clickPos.x, clickPos.y)) {
            game.onTeleport = "laboratory";
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
