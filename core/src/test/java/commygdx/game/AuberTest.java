package test.java.commygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import commygdx.game.AuberGame;
import commygdx.game.Screens.PlayScreen;
import commygdx.game.actors.Auber;
import commygdx.game.actors.Infiltrator;
import commygdx.game.stages.Hud;
import commygdx.game.input.PlayerInput;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;


class AuberTest {

    PlayerInput playerInput;
    PlayScreen playScreen;
    AuberGame auberGame;
    Auber auber;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Test_A1: UR_TELEPORTATION, FR_TELEPORT_PADS")
    /**
     * Class: TeleportMenu.java [render()]
     * A1_Method 1: get current x1 y1 compare with after x2 y2
     * A1_Condition 1 - use assertEqual: x1 y1 match with x2 y2
     */
    void testTeleportation(){

    }

    @Test
    @DisplayName("Test_A2: UR_HEAL, FR_INFIRMARY")
    /**
     * Class: Hud.java
     * A2_Method 1: test if auber's health = 100 when auber x y = infirmary room
     */
    void testHealing(){

    }

    @Test
    @DisplayName("Test_A3: UR_KEYBOARD, FR_MOVEMENT")
    /**
     * Class: Auber.java, Charater.java, MovementSystem.java
     * A3_Method 1: test if auber x y change correctly when key pressed
     */
    void testMovement(){
        assertTrue(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY));
    }
    @Test
    @DisplayName("Test_A4: UR_ARREST, FR_ARREST")
    /**
     * Class: Auber.java, Infiltrator.java
     * A4_Method 1: check infiltrator arrest state when SPACE pressed
     */
    void testArrest(){

    }

    @Test
    @DisplayName("Test_A5: UR_FAIL, FR_GAME_OVER")
    /**
     * Class: PlayScreen.java
     * A5_Method 1: modify checkGameState so it take 3 parameters and use it to verify if the game can lose (gameState = 3)
     */
    void testGameover(){
        playScreen.checkGameState(4,0,100);
        assertEquals(3, auberGame.getGameState());
        playScreen.checkGameState(4,5,0);
        assertEquals(3,auberGame.getGameState());
    }
    @Test
    @DisplayName("Test_A6: UR_SUCCEED")
    /**
     * Class: PlayScreen.java
     * A6_Method 1: modify checkGameState so it take 3 parameters and use it to verify if the game can win (gameState = 2)
     */
    void testGamewin(){
        playScreen.checkGameState(0,5,100);
        assertEquals(2,auberGame.getGameState());
    }
    @Test
    @DisplayName("Test_A7: UR_SYSTEMS, FR_SABOTAGE")
    /**
     * A7_Method 1: check if system being sabotage when infiltrator near, compare x and y
     */
    void testSabotage(){

    }

    @Test
    @DisplayName("Test_A8: UR_DAMAGE, FR_DAMAGE")
    /**
     * Class: Hub.java
     * A8_Method 1: check if auber's health decrease when infiltrator nearby
     */
    void testDamage(){

    }

    @Test
    @DisplayName("Test_A9: FR_SPECIAL_ABILITIES")
    /**
     * No idea, maybe manual test will be easier
     */
    void testAbilities() {

    }

    @Test
    @DisplayName("Test_A10: FR_SYSTEMS_DESTROYED")
    /**
     * Class: Hud.java
     * No idea
     */
    void testSystemRemaining() {

    }

    @Test
    @DisplayName("Test_A11: FR_PAUSE")
    /**
     * No idea
     */
    void testPauseGame() {

    }

    /**
     *  Additional tests - tests after new implementation
     *  Test_A12: Collisions
     *  Test_A13:UR_DIFFICULTIES
     *  Test_A14:UR_POWERUPS
     *  Test_A15:UR_SAVEGAME, FR_LOADFILE, FR_QUITGAME
     */

}