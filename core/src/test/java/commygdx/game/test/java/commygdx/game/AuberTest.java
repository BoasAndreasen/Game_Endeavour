package test.java.commygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import commygdx.game.AuberGame;
import commygdx.game.Screens.PlayScreen;
import commygdx.game.Screens.TeleportMenu;
import commygdx.game.actors.Auber;
import commygdx.game.actors.Infiltrator;
import commygdx.game.stages.Hud;
import commygdx.game.input.PlayerInput;

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


@ExtendWith(MockitoExtension.class)
class AuberTest {

    @Mock
    PlayerInput playerInputMock;
    PlayScreen playScreenMock;
    AuberGame auberGameMock;
    Auber auberMock;
    TeleportMenu teleportMenuMock;
    Hud hudMock;


    @BeforeEach
    /**
     * For most of the test the main problem is most of the functions in this game don't have variable take in or return
     * All the value is either process in the function or using another function as value which makes testing difficult
     * As Unit test needs to have return values to verify to expect outcome of the test case
     * White Box testing is very limited in this project as mentioned there not much variables being use
     * Black Box testing could be possible but the whole structure will need to be refactor
     */
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Test_1: Test if the teleport only available when Auber is on the teleport pad")
    /**
     * Test_Case_1.1: Test when Auber is in the teleport pad
     * Test_Case_1.2: Test when Auber is not in the teleport pad
     * Test_Case_1.3: Test when Auber is close to the teleport pad
     */
    void testTeleportation(){
        /**
         * PlayScreen.teleportCheck is use in PlayScreen.Render()
         * For teleportCheck the switch to Teleport Menu function is confusing, the if statement player.teleportCheck(tiles) && auberGame.onTeleport.equals("false")
         * Teleport Menu.onClick can test if Auber is able to use the Teleport Menu
         * onClick might be need to change to RETURN a String (Which Room)
         * This test cant be run because onTeleport only changes in onClick and when it call here it have no value
         */
        //playScreen.teleportAuber();
        //teleportMenu.onClick(870, 465);
    }

    @Test
    @DisplayName("Test_2: Test if Auber health turns to full when Auber is in Infirmary")
    /**
     * Test_Case_2.1: Test when Auber is not in infirmary room
     * Test_Case_2.2: Test when Auber is in infirmary room
     * Test_Case_2.3: Test when Auber is close to the infirmary room
     */
    void testHealing(){
        /**
         * Process: PlayScreen.render() -> PlayScreen.healAuber() -> Hud.restoreAuberHealth()
         *
         * The idea of this test is to Change Auber's location and test if Auber's health have restored
         * healAuber() have an IF statement that determine whether Auber is in the infirmary room or not
         * The function don't have any value being used, it gets the boundaries of the infirmary through Tiles
         * when the IF statement in healAuber() is true, restoreAuberHealth() is call and set auberHealth to 100
         * So Auber's location can't be modify, therefore the test can't be run as there aren't input values
         *
         * Possible Solution:
         * Create a new function call isInfirmary() which is basically the IF the in healAuber but will return True or False
         * Mock isInfirmary() so it return True for TC2.1 and False for TC2.2 and TC2.3
         * Then assertEquals(Hud.auberHealth, 100) and assertNotEquals(Hud.auberHealth, 100)
         * This method might not suit for TC2.3
         *
         * Change Code for PlayScreen.java:
         *
         *     public void healAuber() {
         *         if (isInfirmary()) {
         *             hud.restoreAuberHealth();
         *         }
         *     }
         *
         *     public boolean isInfirmary(){
         *         if (player.sprite.getBoundingRectangle().overlaps(tiles.getInfirmary())) {
         *             return true;
         *         }
         *         return false;
         *     }
         */
         //playScreen.healAuber();
         //assertEquals(Hud.auberHealth, 100);
         //assertNotEquals(Hud.auberHealth, 100);

    }

    @Test
    @DisplayName("Test_3: Test if the player is able to move Auber with WASD or Arrow keys")
    /**
     * Test_Case_3.1: Test with pressing one of the WASD keys
     * Test_Case_3.2: Test with pressing one of the Arrow keys
     * Test_Case_3.3: Test with pressing multiple WASD keys
     * Test_Case_3.4: Test with pressing multiple Arrow keys
     */
    void testMovement(){
        /**
         * Progress: Auber.handleMovement() -> PlayerInput.getDirection()
         *              Auber.handleMovement() -> MovementSystem.left()
         *              Auber.handleMovement() -> MovementSystem.right()
         *              Auber.handleMovement() -> MovementSystem.up()
         *              Auber.handleMovement() -> MovementSystem.down()
         *
         * This test should test if auber is moving when Keys are pressed
         * handleMovement will set the Auber's position with setPosition which no value can be use for testing
         * handleMovement is a protect class and it is override from Character.java which makes it very difficult to refactor
         *
         * Possible Solution:
         * Mock getDirection() and test Auber's position X and Y
         */

        /**
         * float x = 900; //Setup X
         * float y = 500; //Setup Y
         * auberMock.setPosition(x, y); //Random position
         * assertEquals(900, auberMock.getX());
         * assertEquals(500, auberMock.getY());
         *
         * when(playerInputMock.getDirection()).thenReturn(1); //Setting Input as going UP
         * auberMock.handleMovement(); //code not working from here handleMovement is protected don't know what to do with it
         * assertTrue(auberMock.getY() > 900);
         *
         * when(playerInputMock.getDirection()).thenReturn(2); //Setting Input as going RIGHT
         * auberMock.handleMovement(); //code not working from here handleMovement is protected don't know what to do with it
         * assertTrue(auberMock.getX() > 500);
         */


    }

    @Test
    @DisplayName("Test_4: Test if the player is able to arrest Auber with SPACE key")
    /**
     * Test_Case_4.1: Test with pressing space key next to infiltrator
     * Test_Case_4.2: Test with pressing space key next to empty space
     */
    void testArrest(){
        /**
         * Progress:Auber.arrest() -> PlayerInput.arrest()
         *              Auber.arrest() -> Infiltrator.arrest
         *              Auber.arrest() -> Hud.infiltratorCaught();
         *
         * This test should test if infiltrators are arrested when SPACE key is pressed
         *
         * Possible Solution:
         * Mock PlayerInput.arrest() and test Hud.infiltratorsRemaining
         */


         hudMock.infiltratorsRemaining = 1;
         ArrayList<Infiltrator> i;
         i = new ArrayList<Infiltrator>(Arrays.asList(new Infiltrator(new Vector2(500, 200), 1,
                 playScreenMock.graph, false, 0, 0, 6f)));
         auberMock.setPosition(550, 220);
         when(playerInputMock.arrest()).thenReturn(true); //Player press SPACE
         auberMock.arrest(i, hudMock);
         assertEquals(0, hudMock.infiltratorsRemaining);
    }


    @Test
    @DisplayName("Test_5: Test if the player can lose the game when no system remaining or Auber's health is 0")
    /**
     * Test_Case_5.1: Test when Auber health >= 1
     * Test_Case_5.2: Test when Auber health = 0
     * Test_Case_5.3: Test when system remaining >= 1
     * Test_Case_5.4: Test when system remaining = 0
     */
    void testGameover(){
        //playScreen.checkGameState(4,0,100);
        //assertEquals(3, auberGame.getGameState());
        //playScreen.checkGameState(4,5,0);
        //assertEquals(3,auberGame.getGameState());
    }
    @Test
    @DisplayName("Test_6: Test if the player can win the game when no Infiltrator remaining")
    /**
     * Test_Case_6.1: Test when Infiltrator >= 1
     * Test_Case_6.2: Test when Infiltrator = 0
     */
    void testGamewin(){
        //playScreen.checkGameState(0,5,100);
        //assertEquals(2,auberGame.getGameState());
    }
    @Test
    @DisplayName("Test_7: Test if system can be destroy by the Infiltrator")
    /**
     * Test_Case_7.1: Test if system being sabotage when infiltrator near
     * Test_Case_7.2: Test if system being sabotage when infiltrator not near
     */
    void testSabotage(){

    }

    @Test
    @DisplayName("Test_8: Test if Auber can be damaged by the Infiltrator")
    /**
     * Test_Case_8.1: Test if Auber is being damaged when near Infiltrator
     * Test_Case_8.2: Test if Auber is being damaged when not near Infiltrator
     */
    void testDamage(){

    }

    @Test
    void testDifficulties()
    {

    };

    @Test
    void testPowerups()
    {

    };

    @Test
    void testSaveGame()
    {

    };


}