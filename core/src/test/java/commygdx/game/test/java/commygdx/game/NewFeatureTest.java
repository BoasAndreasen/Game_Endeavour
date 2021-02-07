package test.java.commygdx.game;

import commygdx.game.AuberGame;
import commygdx.game.Screens.PlayScreen;
import commygdx.game.actors.Auber;
import commygdx.game.syst.MovementSystem;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.Assert.*;


import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class NewFeatureTest {

    Auber auber;
    AuberGame auberGame;
    PlayScreen playScreen;
    MovementSystem movementSystem;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testEasyDifficulties()
    {
        /**
         * IntroScreen -> DifficultyScreen -> PlayScreen.diffCheck() -> MovementSystem.setSpeed()
         */

        //Set the game to easy
        auberGame.createPlayScreen(false, false, "easy");
        //Get Auber's speed
        float speed = auber.movementSystem.getMovementSpeed();
        //Test if Auber's speed is in Easy mode
        assertEquals(6f, speed);
    }

    @Test
    void testNormalDifficulties()
    {
        /**
         * IntroScreen -> DifficultyScreen -> PlayScreen.diffCheck() -> MovementSystem.setSpeed()
         */

        //Set the game to normal
        auberGame.createPlayScreen(false, false, "normal");
        //Get Auber's speed
        float speed = auber.movementSystem.getMovementSpeed();
        //Test if Auber's speed is in normal mode
        assertEquals(6f, speed);
    }

    @Test
    void testHardDifficulties()
    {
        /**
         * IntroScreen -> DifficultyScreen -> PlayScreen.diffCheck() -> MovementSystem.setSpeed()
         */

        //Set the game to hard
        auberGame.createPlayScreen(false, false, "hard");
        //Get Auber's speed
        float speed = auber.movementSystem.getMovementSpeed();
        //Test if Auber's speed is in hard mode
        assertEquals(4f, speed);
    }

    @Test
    void testPowerups()
    {

    }

    @Test
    void testSaveGame()
    {

    }
}