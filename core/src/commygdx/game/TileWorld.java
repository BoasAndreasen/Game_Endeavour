package commygdx.game;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import commygdx.game.Screens.PlayScreen;

import java.util.ArrayList;
import java.util.Hashtable;

public class TileWorld {
    private final Hashtable<String, Rectangle> teleporters;
    private final ArrayList<ShipSystem> shipSystems;
    private final ArrayList<Rectangle> collisionBoxes;
    private Rectangle healthpowerUp;
    private Rectangle invispowerUp;
    private Rectangle slowpowerUp;
    private Rectangle speedpowerUp;
    private Rectangle shieldpowerUp;
    private Rectangle infirmary;
    private Rectangle brig;
    private Rectangle crew;
    private Rectangle command;
    private Rectangle laboratory;
    private Rectangle engine;
    private final int scale;

    /**
     * Creates all objects that the sprites can interactive with
     * NEW FOR ASSESMENT 2: UR_POWERUPS. Changed to map to include powerup objects.
     *
     * @param screen the main game screen
     */
    public TileWorld(PlayScreen screen, ArrayList<ShipSystem> newShipSystems) {
        TiledMap map = screen.getMap();
        this.scale = AuberGame.ZOOM;

        createRooms(map);

        // New for assesment 2. Load systems and their state from previous game
        if (!newShipSystems.isEmpty()) {
            // read from save
            this.shipSystems = newShipSystems;
        } else {
            // create systems
            ArrayList<ShipSystem> shipSystems1 = new ArrayList<>();
            for (MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = magnifyRectange(((RectangleMapObject) object).getRectangle());
                shipSystems1.add(new ShipSystem(rect.x, rect.y, getRoom(rect.x, rect.y), screen.graph, 0));
            }
            this.shipSystems = shipSystems1;
        }


        //create objects
        collisionBoxes = new ArrayList<>();

        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = magnifyRectange(((RectangleMapObject) object).getRectangle());
            collisionBoxes.add(rect);
        }
        //create teleporters
        teleporters = new Hashtable<String, Rectangle>();
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            rect.x = rect.x * scale + rect.width * scale / 2;
            rect.y = rect.y * scale + rect.height * scale / 2;
            rect.width = 25;
            rect.height = 25;
            teleporters.put(getRoom(rect.x, rect.y), rect);
        }

        //walls
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = magnifyRectange(((RectangleMapObject) object).getRectangle());
            collisionBoxes.add(rect);
        }

        for (MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            rect.x = rect.x * scale + rect.width * scale / 2;
            rect.y = rect.y * scale + rect.height * scale / 2;
            rect.width = 25;
            rect.height = 25;
            healthpowerUp = rect;
        }

        for (MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = magnifyRectange(((RectangleMapObject) object).getRectangle());
            rect.width = 25;
            rect.height = 25;
            invispowerUp = rect;

        }

        for (MapObject object : map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = magnifyRectange(((RectangleMapObject) object).getRectangle());
            rect.width = 25;
            rect.height = 25;
            speedpowerUp = rect;
        }

        for (MapObject object : map.getLayers().get(14).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = magnifyRectange(((RectangleMapObject) object).getRectangle());
            rect.width = 25;
            rect.height = 25;
            slowpowerUp = rect;
        }

        for (MapObject object : map.getLayers().get(15).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = magnifyRectange(((RectangleMapObject) object).getRectangle());
            rect.width = 25;
            rect.height = 25;
            shieldpowerUp = rect;
        }
    }

    /**
     * Sets rectangle bounds for each room
     *
     * @param map The game's tile map
     */
    private void createRooms(TiledMap map) {
        MapObject roomObj = new MapObject();
        roomObj = map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class).get(0);
        infirmary = magnifyRectange(((RectangleMapObject) roomObj).getRectangle());
        roomObj = map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class).get(0);
        brig = magnifyRectange(((RectangleMapObject) roomObj).getRectangle());
        roomObj = map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class).get(0);
        crew = magnifyRectange(((RectangleMapObject) roomObj).getRectangle());
        roomObj = map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class).get(0);
        laboratory = magnifyRectange(((RectangleMapObject) roomObj).getRectangle());
        roomObj = map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class).get(0);
        command = magnifyRectange(((RectangleMapObject) roomObj).getRectangle());
        roomObj = map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class).get(0);
        engine = magnifyRectange(((RectangleMapObject) roomObj).getRectangle());
    }

    /**
     * Magnifies the bounds of the rectangle to fit with the zoom of screen
     *
     * @param rect The bounds being enlarged
     * @return The magnified bounds
     */
    private Rectangle magnifyRectange(Rectangle rect) {
        rect.x = rect.x * scale;
        rect.y = rect.y * scale;
        rect.width = rect.width * scale;
        rect.height = rect.height * scale;
        return rect;
    }

    public Hashtable<String, Rectangle> getTeleporters() {
        return teleporters;
    }

    public Rectangle getHealthpowerUp() {
        return healthpowerUp;
    }

    public Rectangle getInvispowerUp() {
        return invispowerUp;
    }

    public Rectangle getShieldpowerUp() {
        return shieldpowerUp;
    }

    public Rectangle getSlowpowerUp() {
        return slowpowerUp;
    }

    public Rectangle getSpeedpowerUp() {
        return speedpowerUp;
    }

    public Rectangle getInfirmary() {
        return infirmary;
    }

    public ArrayList<ShipSystem> getSystems() {
        return shipSystems;
    }

    public ArrayList<Rectangle> getCollisionBoxes() {
        return collisionBoxes;
    }

    /**
     * Finds the room that a pair of coordinates are in
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return A string the representing the room the coordinates are in or "none" if they are not in a room
     */
    public String getRoom(float x, float y) {
        if (infirmary.contains(x, y)) {
            return "infirmary";
        }
        else if (command.contains(x, y)) {
            return "command";
        }
        else if (laboratory.contains(x, y)) {
            return "laboratory";
        }
        else if (brig.contains(x, y)) {
            return "brig";
        }
        else if (crew.contains(x, y)) {
            return "crew";
        }
        else if (engine.contains(x, y)) {
            return "engine";
        }
        else {
            return "none";
        }
    }
}
