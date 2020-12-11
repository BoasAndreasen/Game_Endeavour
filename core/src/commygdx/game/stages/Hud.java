package commygdx.game.stages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import commygdx.game.AuberGame;
import commygdx.game.ShipSystem;
import commygdx.game.actors.Infiltrator;
import java.util.ArrayList;
import java.util.List;

public class Hud {
    public Stage stage;
    private int systemsUp;
    private final Label systemLabel;
    private int infiltratorsRemaining;
    private final Label infiltratorLabel;
    private final Label attackLabel;
    private final Label hallucinateLabel;

    //used for buttons,text, etc
    public Hud(SpriteBatch sb, ArrayList<Infiltrator> enemies, ArrayList<ShipSystem> systems) {
        Viewport viewport = new FitViewport(AuberGame.V_WIDTH, AuberGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        systemsUp = systems.size();
        infiltratorsRemaining = enemies.size();

        System.out.format("%d / 15 systems", systemsUp);
        BitmapFont font = new BitmapFont();
        font.getData().setScale(3f);

        //operational systems
        systemLabel = new Label(String.format("%d / 15", systemsUp), new Label.LabelStyle(font, Color.WHITE));
        Label systemTextLabel = new Label("systems operational", new Label.LabelStyle(font, Color.WHITE));

        //remaining infiltrators
        infiltratorLabel = new Label(String.format("%d / 8 ", infiltratorsRemaining), new Label.LabelStyle(font, Color.WHITE));
        Label infiltratorTextLabel = new Label("infiltrators remaining", new Label.LabelStyle(font, Color.WHITE));

        //systems under attack
        attackLabel = new Label("None", new Label.LabelStyle(font, Color.WHITE));
        Label attackTextLabel = new Label("Current attacks", new Label.LabelStyle(font, Color.WHITE));

        //hallucination warning
        hallucinateLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));

        table.setPosition(viewport.getScreenWidth() / 2 + 150, 0);

        table.add(systemLabel).expandX().padTop(50);
        table.row();
        table.add(systemTextLabel).expandX().padTop(10);
        table.row();
        table.add(infiltratorLabel).expandX().padTop(50);
        table.row();
        table.add(infiltratorTextLabel).expandX().padTop(10);
        table.row();
        table.add(attackTextLabel).expandX().padTop(50);
        table.row();
        table.add(attackLabel).expandX().padTop(10);
        table.row();
        table.add(hallucinateLabel).expandX().padTop(50);

        stage.addActor(table);
    }

    /**
     * Updates the HUD to decrease the amount of infiltrators
     */
    public void infiltratorCaught() {
        infiltratorsRemaining -= 1;
        infiltratorLabel.setText(String.format("%d / 8", infiltratorsRemaining));
    }

    /**
     * Sets the HUD's hallucination warning off or on
     *
     * @param show If the hallucination warning should be shown or not
     */
    public void showHallucinateLabel(boolean show) {
        if (show) {
            hallucinateLabel.setText("You are hallucinating \n Go to infirmary to heal ");
        } else {
            hallucinateLabel.setText("");
        }
    }

    /**
     * Updates the HUD's display on what rooms have systems under attack
     *
     * @param systems List of all the systems on the map
     */
    public void updateAttacks(List<ShipSystem> systems) {
        /*Update hud to reflect attacks*/
        String room = "";
        systemsUp = 0;
        for (ShipSystem system : systems) {
            if (system.getState() == 1) {
                if (!room.contains(system.getRoom())) {
                    room += system.getRoom();
                    room += "\n";
                }
            }
            if (system.getState() != 2) {
                systemsUp += 1;
            }
        }
        if (room.length() < 1) {
            room = "None";
        }
        attackLabel.setText(room);
        systemLabel.setText(String.format("%d / 15", systemsUp));
    }

    public int getInfiltratorsRemaining() {
        return infiltratorsRemaining;
    }

    public int getSystemsUp() {
        return systemsUp;
    }
}
