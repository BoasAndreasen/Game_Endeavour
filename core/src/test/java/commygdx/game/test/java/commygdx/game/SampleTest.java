package test.java.commygdx.game;

import commygdx.game.AuberGame;
import commygdx.game.Screens.PlayScreen;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.Assert.*;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SampleTest {

    /**
     * This class is use for try testings
     */



    @Mock
    private AuberGame auberGame = new AuberGame();

    @InjectMocks
    private PlayScreen playScreen = new PlayScreen(auberGame, false ,false ,"Easy");

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testGetValues() {
        int x = playScreen.duration;
        assertEquals(1, x);
    }
}
