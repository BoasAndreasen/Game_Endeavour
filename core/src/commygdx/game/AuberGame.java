package commygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import commygdx.game.Screens.DifficultyScreen;
import commygdx.game.Screens.GameOverScreen;
import commygdx.game.Screens.IntroScreen;
import commygdx.game.Screens.PlayScreen;

public class AuberGame extends com.badlogic.gdx.Game {
    public SpriteBatch batch;
    public static final int V_WIDTH = 2560;
    public static final int V_HEIGHT = 1440;
    public static final int ZOOM = 12;
    public String onTeleport;
    public Screen playscreen;
    public Screen diffscreen;
    //game state -1 = intro screen 0 = exit introscreen 1 = playing 2 = win 3 = lost
    public int gameState;
    private GameOverScreen gameOverScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        Screen introScreen = new IntroScreen(this);
        gameOverScreen = new GameOverScreen(this);
        setScreen(introScreen);
        onTeleport = "false";
        gameState = -1;
    }

    /**
     * Initialises the game's play screen
     *
     * @param demo     If the screen should be in demo mode or not
     * @param loadGame if data should be loaded from previous save
     */
    public void createPlayScreen(boolean demo, boolean loadGame, String difficulty) {
        playscreen = new PlayScreen(this, demo, loadGame, difficulty);
    }

    public void createDifficultyScreen(boolean demo, boolean loadGame) {
        diffscreen = new DifficultyScreen(this, demo, loadGame);
    }

    @Override
    public void render() {
        super.render();
        //exit intro screen and start game
        if (gameState == 0 || gameState == 4) {
            setScreen(playscreen);
            gameState = 1;
        }

        //when game over go to gameoverscreen
        if (gameState == 2 || gameState == 3) {
            setScreen(gameOverScreen);
        }

        if (gameState == 5) {
            setScreen(diffscreen);
        }

        if ((!onTeleport.equals("true")) && (!onTeleport.equals("false"))) {
            //exit teleport screen
            setScreen(playscreen);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }
}
