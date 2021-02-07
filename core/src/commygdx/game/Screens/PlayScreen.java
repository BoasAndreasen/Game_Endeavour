package commygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import commygdx.game.AI.graph.PathGraph;
import commygdx.game.AI.graph.PathNode;
import commygdx.game.AuberGame;
import commygdx.game.ShipSystem;
import commygdx.game.TileWorld;
import commygdx.game.Utility;
import commygdx.game.actors.Auber;
import commygdx.game.actors.DemoAuber;
import commygdx.game.actors.Infiltrator;
import commygdx.game.actors.NPC;
import commygdx.game.stages.Hud;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class PlayScreen implements Screen {
    private final String difficulty;
    protected AuberGame auberGame;
    private Hud hud;
    private final OrthographicCamera gamecam;
    private final Viewport gamePort;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;
    public ArrayList<Infiltrator> enemies;
    public ArrayList<NPC> people;
    public ArrayList<ShipSystem> systems;
    //Graph used for AI pathfinding
    public PathGraph graph;
    private boolean paused;
    private boolean saved;
    private final boolean demo;
    // Duration of power-up
    private int duration;
    // If specified in IntroScreen then load save
    private final boolean loadFromSave;

    //Scene2D
    protected Auber player;
    private Stage shipStage;

    //Used for the infiltrator's hallucinate power
    private boolean hallucinate;
    private final Texture pauseTexture;
    private final Texture pauseTexture2;
    private final Texture hallucinateTexture;

    private final TileWorld tiles;
    protected int scale;

    public PlayScreen(AuberGame auberGame, boolean demo, boolean loadFromSave, String difficulty) {
        this.auberGame = auberGame;
        this.demo = demo;
        this.scale = AuberGame.ZOOM;
        this.loadFromSave = loadFromSave;
        this.difficulty = difficulty;
        if (loadFromSave) {
            this.loadDurationSave();
        } else {
            this.duration = 0;
        }

        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(AuberGame.V_WIDTH, AuberGame.V_HEIGHT, gamecam);
        /*Possible fullscreen
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());*/

        //load map
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("mapV2.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, scale);

        //create ai pathing graph
        graph = createPathGraph("csv/nodes.csv", "csv/edges.csv");
        //sets up stage and actors
        setupShipStage();

        // Load tiles with or without saved systems
        if (loadFromSave) {
            loadSystemSave();
            tiles = new TileWorld(this, systems);
        } else {
            ArrayList<ShipSystem> emptyList = new ArrayList<ShipSystem>();
            tiles = new TileWorld(this, emptyList);
        }

        //Used for pause screen
        pauseTexture = new Texture("pauseImage.png");
        pauseTexture2 = new Texture("pauseImage2.png");

        //Used for the infiltrator's hallucinate power
        hallucinateTexture = new Texture("hallucinateV2.png");
        hallucinate = false;

        if (!loadFromSave) {
            hud = new Hud(enemies, tiles.getSystems(), 100, false, "None");
        } else {
            loadHudSave();
        }
    }

    /**
     * Creates stage and adds characters (auber and infiltrators) to it
     */
    private void setupShipStage() {
        shipStage = new Stage(new StretchViewport(AuberGame.V_WIDTH, AuberGame.V_HEIGHT, gamecam));
        createAuber();
        createEnemies();
        createNPCs();

        shipStage.addActor(player);
        //Adding infiltrators to stage
        for (Infiltrator enemy : enemies) {
            shipStage.addActor(enemy);
        }
        for (NPC person : people) {
            shipStage.addActor(person);
        }
    }

    private void createAuber() {
        //A different version of Auber is used for the player depending on if it's a demo or not
        if (!demo) {
            if (!loadFromSave) {
                player = new Auber(new Vector2(450 * scale, 778 * scale), false, 6f);
            } else {
                loadAuberSave();
            }
        } else {
            player = new DemoAuber(new Vector2(450 * scale, 778 * scale), graph);
        }
    }

    private void createEnemies() {
        if (!loadFromSave) {
            //Creating and placing infiltrators
            enemies = new ArrayList<Infiltrator>(Arrays.asList(
                    new Infiltrator(new Vector2(4700, 2000), 1, graph, false,
                            0, 0, 6f),
                    new Infiltrator(new Vector2(4800, 2300), 2, graph, false,
                            0, 0, 6f),
                    new Infiltrator(new Vector2(5000, 7356), 3, graph, false,
                            0, 0, 6f),
                    new Infiltrator(new Vector2(4732, 7000), 4, graph, false,
                            0, 0, 6f),
                    new Infiltrator(new Vector2(4732, 7500), 1, graph, false,
                            0, 0, 6f),
                    new Infiltrator(new Vector2(4732, 7800), 2, graph, false,
                            0, 0, 6f),
                    new Infiltrator(new Vector2(4200, 7800), 3, graph, false,
                            0, 0, 6f),
                    new Infiltrator(new Vector2(5400, 7800), 4, graph, false,
                            0, 0, 6f)
            ));
        } else {
            loadEnemySave();
        }
    }

    private void createNPCs() {
        if (!loadFromSave) {
            //Creating and placing infiltrators
            people = new ArrayList<NPC>(Arrays.asList(
                    new NPC(new Vector2(4700, 2000), graph),
                    new NPC(new Vector2(4800, 2300), graph),
                    new NPC(new Vector2(5000, 7356), graph),
                    new NPC(new Vector2(4732, 7000), graph),
                    new NPC(new Vector2(4732, 7500), graph),
                    new NPC(new Vector2(4732, 7800), graph),
                    new NPC(new Vector2(4200, 7800), graph),
                    new NPC(new Vector2(5400, 7800), graph)
            ));
        } else {
            loadNPCSave();
        }
    }

    public void update(float dt) {
        if (!player.powerUpCheck(tiles).equals("null")) {
            duration = 500;
        } else if (duration > 0) {
            duration -= 1;
        }

        shipStage.act(dt);
        player.damageAuber(enemies, hud);
        player.arrest(enemies, hud);
    }

    @Override
    public void show() {

    }

    /**
     * Draws the game to screen and updates game
     *
     * @param delta Time difference from last call
     */
    @Override
    public void render(float delta) {
        if (!paused) {
            //updates game
            checkGameState();
            update(delta);
            updateInfiltrators(delta);
            teleportCheck();
            powerupCheck();
            player.checkCollision(tiles.getCollisionBoxes());
            hud.updateAttacks(tiles.getSystems());
            healAuber();
        }

        //draws game
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        shipStage.draw();
        updateCamera();

        if (hallucinate) {
            drawHallucinate();
        }

        if (paused) {
            drawPause();
        } else {
            hud.stage.draw();
        }

        // pause game
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            paused = true;
        }

        // unpause game
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            paused = false;
            saved = false;
        }

        // quit game
        if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE) && paused) {
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && paused) {
            // Only allow game to be saved once per pause
            if (!saved) {
                saveGame();
            }
            saved = true;
        }
    }

    /**
     * sets camera to players position
     */
    private void updateCamera() {
        Vector3 pos = new Vector3((player.getX()) + player.getWidth() / 2,
                (player.getY()) + player.getHeight() / 2, 0);
        shipStage.getViewport().getCamera().position.set(pos);
        gamecam.position.set(pos);
        gamecam.update();
        renderer.setView(gamecam);
        auberGame.batch.setProjectionMatrix(hud.stage.getCamera().combined);
    }

    /**
     * sets enemy's speed depending on difficulty
     */
    private void diffCheck() {
        if (difficulty.equals("easy")) {
            player.movementSystem.setSpeed(6f);
            for (Infiltrator enemy : enemies) {
                enemy.movementSystem.setSpeed(3f);
            }
        }
        if (difficulty.equals("normal")) {
            player.movementSystem.setSpeed(6f);
            for (Infiltrator enemy : enemies) {
                enemy.movementSystem.setSpeed(6f);
            }
        }
        if (difficulty.equals("hard")) {
            player.movementSystem.setSpeed(4f);
            for (Infiltrator enemy : enemies) {
                enemy.movementSystem.setSpeed(10f);
            }
        }
    }

    private void teleportCheck() {
        //teleport is disabled in demo mode, because the ai can't handle it
        if (demo) {
            return;
        }
        //switch to teleport menu
        if (player.teleportCheck(tiles) && auberGame.onTeleport.equals("false")) {
            auberGame.setScreen(new TeleportMenu(auberGame));
        }
        if ((!auberGame.onTeleport.equals("true")) && (!auberGame.onTeleport.equals("false"))) {
            teleportAuber();
            auberGame.onTeleport = "false";
        }
    }

    /**
     * Sets power-up back or calls default
     */
    private void powerupCheck() {
        if (demo) {
            return;
        }
        if (duration > 0) {
            if (player.powerUpCheck(tiles).equals("Health")) {
                hud.healthPower();
                hud.setPowerUpLabelText("Health");
                hud.powerUpLabel.setText("Health");
            }
            if (player.powerUpCheck(tiles).equals("Invis")) {
                player.setInvisible(true);
                player.checkInvisibleTexture();
                hud.setPowerUpLabelText("Invisible");
                hud.powerUpLabel.setText("Invisible");
            }
            if (player.powerUpCheck(tiles).equals("Slow")) {
                hud.setPowerUpLabelText("Slow Infiltrators");
                hud.powerUpLabel.setText("Slow Infiltrators");
                for (Infiltrator enemy : enemies) {
                    enemy.setSpeed(2f);
                }
            }
            if (player.powerUpCheck(tiles).equals("Speed")) {
                hud.setPowerUpLabelText("Speed");
                hud.powerUpLabel.setText("Speed");
                player.movementSystem.setSpeed(9f);
            }
            if (player.powerUpCheck(tiles).equals("Shield")) {
                hud.setPowerUpLabelText("Speed");
                hud.powerUpLabel.setText("Shield");
                hud.setShieldPower(true);
                player.damageAuber(enemies, hud);
            }
        } else {
            hud.setPowerUpLabelText("None");
            hud.powerUpLabel.setText("None");
            player.setInvisible(false);
            player.checkInvisibleTexture();
            hud.setShieldPower(false);
            diffCheck();
        }
    }

    /**
     * Draws hallucination texture overlay on screen and removes if the auber is in infirmary
     */
    private void drawHallucinate() {
        auberGame.batch.begin();
        if (player.sprite.getBoundingRectangle().overlaps(tiles.getInfirmary())) {
            hud.showHallucinateLabel(false);
            hallucinate = false;
        } else {
            auberGame.batch.draw(hallucinateTexture, 0, 0);
        }
        auberGame.batch.end();
    }

    /**
     * Draws pause message if game is paused
     */
    private void drawPause() {
        auberGame.batch.begin();
        if (!saved) {
            auberGame.batch.draw(pauseTexture, (Gdx.graphics.getWidth()) / 2.0f, (Gdx.graphics.getHeight()) / 2.5f);
        } else {
            auberGame.batch.draw(pauseTexture2, (Gdx.graphics.getWidth()) / 2.0f, (Gdx.graphics.getHeight()) / 2.5f);
        }
        auberGame.batch.end();
    }

    /**
     * Load Hud from save
     */
    private void loadHudSave() {
        //Read JSON
        JSONParser jsonP = new JSONParser();
        try (FileReader reader = new FileReader("saveSprite.json")) {
            //Read JSON File
            Object obj = jsonP.parse(reader);
            JSONArray sprList1 = (JSONArray) obj;
            JSONObject empObj = (JSONObject) sprList1.get(0);

            JSONObject j = (JSONObject) empObj.get("sprite0");
            int health = Integer.parseInt(String.valueOf(j.get("health")));
            boolean shield = (boolean) j.get("shield");
            String powerup = (String) j.get("powerup");
            hud = new Hud(enemies, tiles.getSystems(), health, shield, powerup);

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load duration from save
     */
    private void loadDurationSave() {
        //Read JSON
        JSONParser jsonP = new JSONParser();
        try (FileReader reader = new FileReader("saveSprite.json")) {
            //Read JSON File
            Object obj = jsonP.parse(reader);
            JSONArray sprList1 = (JSONArray) obj;
            JSONObject empObj = (JSONObject) sprList1.get(0);

            JSONObject j = (JSONObject) empObj.get("sprite0");
            duration = Integer.parseInt(String.valueOf(j.get("duration")));
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load auber from save
     */
    private void loadAuberSave() {
        //Read JSON
        JSONParser jsonP = new JSONParser();
        try (FileReader reader = new FileReader("saveSprite.json")) {
            //Read JSON File
            Object obj = jsonP.parse(reader);
            JSONArray sprList1 = (JSONArray) obj;
            JSONObject empObj = (JSONObject) sprList1.get(0);

            JSONObject j = (JSONObject) empObj.get("sprite0");
            double x = (double) j.get("x");
            double y = (double) j.get("y");
            boolean invisible = (boolean) j.get("invisible");
            double speed = (double) j.get("speed");
            player = new Auber(new Vector2((float) x, (float) y), invisible, (float) speed);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load enemy from save
     */
    private void loadEnemySave() {
        ArrayList<Infiltrator> localEnemies = new ArrayList<>();

        //Read JSON
        JSONParser jsonP = new JSONParser();
        try (FileReader reader = new FileReader("saveSprite.json")) {
            //Read JSON File
            Object obj = jsonP.parse(reader);
            JSONArray sprList = (JSONArray) obj;

            for (int i = 1; i < 9; i++) {
                JSONObject e = (JSONObject) sprList.get(i);
                JSONObject empObj = (JSONObject) e.get("sprite" + i);
                double x = (double) empObj.get("x");
                double y = (double) empObj.get("y");
                boolean arrested = (boolean) empObj.get("arrested");
                double powerCooldown = (double) empObj.get("powerCooldown");
                double powerDuration = (double) empObj.get("powerDuration");
                int power = Integer.parseInt(String.valueOf(empObj.get("power")));
                double speed = (double) empObj.get("speed");
                Infiltrator inf = new Infiltrator(new Vector2((float) x, (float) y),
                        power, graph, arrested, (float) powerCooldown, (float) powerDuration, (float) speed);
                localEnemies.add(inf);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        //Creating and placing infiltrators
        enemies = new ArrayList<>(localEnemies);
    }

    /**
     * Load npc from save
     */
    private void loadNPCSave() {
        ArrayList<NPC> localNPCs = new ArrayList<>();

        //Read JSON
        JSONParser jsonP = new JSONParser();
        try (FileReader reader = new FileReader("saveSprite.json")) {
            //Read JSON File
            Object obj = jsonP.parse(reader);
            JSONArray sprList = (JSONArray) obj;

            for (int i = 9; i < sprList.size(); i++) {
                JSONObject e = (JSONObject) sprList.get(i);
                JSONObject empObj = (JSONObject) e.get("sprite" + i);
                double x = (double) empObj.get("x");
                double y = (double) empObj.get("y");
                NPC npc = new NPC(new Vector2((float) x, (float) y), graph);
                localNPCs.add(npc);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        //Creating and placing npcs
        people = new ArrayList<>(localNPCs);
    }

    /**
     * Load system from save
     */
    private void loadSystemSave() {
        ArrayList<ShipSystem> localSystems = new ArrayList<>();

        //Read JSON
        JSONParser jsonP = new JSONParser();
        try (FileReader reader = new FileReader("saveSystem.json")) {
            Object obj = jsonP.parse(reader);
            JSONArray sysList = (JSONArray) obj;

            for (int i = 0; i < sysList.size(); i++) {
                JSONObject e = (JSONObject) sysList.get(i);
                JSONObject empObj = (JSONObject) e.get("system" + i);
                double x = (double) empObj.get("x");
                double y = (double) empObj.get("y");
                long state = (long) empObj.get("state");
                String room = (String) empObj.get("room");
                ShipSystem sys = new ShipSystem((float) x, (float) y, room, graph, (int) state);
                localSystems.add(sys);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        //Creating and placing shipsystems
        systems = new ArrayList<ShipSystem>(localSystems);
    }

    /**
     * Saves game in pause screen
     */
    private void saveGame() {
        // write sprite JSON
        int counter = 0;
        JSONArray sprList = new JSONArray();

        //Auber
        JSONObject spr = new JSONObject();
        spr.put("x", player.getX());
        spr.put("y", player.getY());
        spr.put("health", hud.getAuberHealth());
        spr.put("speed", player.movementSystem.getMovementSpeed());
        spr.put("duration", duration);
        spr.put("invisible", player.getInvisible());
        spr.put("shield", hud.getshieldPower());
        spr.put("powerup", hud.getPowerUpLabelText());

        JSONObject sprObj = new JSONObject();
        sprObj.put("sprite" + counter, spr);
        sprList.add(sprObj);
        counter += 1;

        //infiltrators
        for (int i = 0; i < enemies.size(); i++) {
            spr = new JSONObject();
            spr.put("x", enemies.get(i).getX());
            spr.put("y", enemies.get(i).getY());
            spr.put("arrested", enemies.get(i).getIsArrested());
            spr.put("power", enemies.get(i).getPower());
            spr.put("powerCooldown", enemies.get(i).getPowerCooldown());
            spr.put("powerDuration", enemies.get(i).getPowerDuration());
            spr.put("speed", enemies.get(i).movementSystem.getMovementSpeed());

            sprObj = new JSONObject();
            sprObj.put("sprite" + counter, spr);
            sprList.add(sprObj);
            counter += 1;
        }

        //NPCs
        for (int i = 0; i < people.size(); i++) {
            spr = new JSONObject();
            spr.put("x", people.get(i).getX());
            spr.put("y", people.get(i).getY());

            sprObj = new JSONObject();
            sprObj.put("sprite" + counter, spr);
            sprList.add(sprObj);
            counter += 1;
        }

        // write system JSON
        JSONArray sysList = new JSONArray();
        for (int i = 0; i < tiles.getSystems().size(); i++) {
            JSONObject sys = new JSONObject();
            sys.put("x", tiles.getSystems().get(i).getPosition().x);
            sys.put("y", tiles.getSystems().get(i).getPosition().y);
            sys.put("room", tiles.getSystems().get(i).getRoom());

            // Since infiltrators don't remember which system they are attacking
            // after being saved, systems that were under attack are saved as
            // not being under attack. Thereby resetting the attacks.
            if (tiles.getSystems().get(i).getState() == 2) {
                sys.put("state", 2);
            } else if (tiles.getSystems().get(i).getState() == 1) {
                sys.put("state", 0);
            } else if (tiles.getSystems().get(i).getState() == 0) {
                sys.put("state", 0);
            }

            JSONObject sysObj = new JSONObject();
            sysObj.put("system" + i, sys);
            sysList.add(sysObj);
        }
        // write sprite
        try (FileWriter file = new FileWriter("saveSprite.json")) {
            file.write(sprList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write system
        try (FileWriter file = new FileWriter("saveSystem.json")) {
            file.write(sysList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Heals auber back to full health if auber is in the infirmary
     */
    public void healAuber() {
        if (player.sprite.getBoundingRectangle().overlaps(tiles.getInfirmary())) {
            hud.restoreAuberHealth();
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    /**
     * Sets auber's position to selected teleporter's position
     */
    public void teleportAuber() {
        float x = tiles.getTeleporters().get(auberGame.onTeleport).x + 100;
        float y = tiles.getTeleporters().get(auberGame.onTeleport).y;
        player.setPosition(x, y);
        player.movementSystem.updatePos(new Vector2(x, y));
        if (demo) {
            player.act(0);
        }
    }

    /**
     * Creates an ai pathing graph from .csv files
     *
     * @param nodesFilepath The filepath for the .csv file containing the nodes of the graph
     * @param edgesFilepath The filepath for the .csv file containing the edges of the graph
     * @return The resultant pathing graph
     */
    private PathGraph createPathGraph(String nodesFilepath, String edgesFilepath) {
        PathGraph graph = new PathGraph();
        try {
            //Getting nodes from file
            LinkedList<PathNode> nodes = new LinkedList<PathNode>();

            FileHandle nodesFile = Gdx.files.internal(nodesFilepath);
            BufferedReader reader = nodesFile.reader(1000);
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                String data[] = line.split(",");
                PathNode node = new PathNode(new Vector2(Float.parseFloat(data[2]), Float.parseFloat(data[3])), Boolean.parseBoolean(data[4]));
                nodes.add(node);
                graph.addNode(node);
            }

            //Getting edges from file
            FileHandle edgesFile = Gdx.files.internal(edgesFilepath);
            reader = edgesFile.reader(100);
            line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                graph.addEdge(nodes.get(Integer.parseInt(data[0])), nodes.get(Integer.parseInt(data[1])));
            }

            reader.close();

            for (PathNode node : nodes) {
                if (node.getAdjacentNodes().length == 0) {
                    System.out.println(node);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return graph;
    }

    /**
     * Checks if the game needs to switch to: a winning state, a losing state or to stay in a playing state
     */
    private void checkGameState() {
        if (hud.getInfiltratorsRemaining() <= 0) {
            auberGame.setGameState(2);
        }
        if (hud.getSystemsUp() <= 0 || hud.getAuberHealth() <= 0) {
            auberGame.setGameState(3);
        }
    }

    public void updateInfiltrators(float dt) {
        for (Infiltrator enemy : enemies) {
            enemy.updateTimers();

            //the infiltrator will use their power if the auber is close enough and it's off cooldown
            if (enemy.getPowerCooldown() > 500 && !enemy.getIsArrested()) {
                enemy.usePower(this, tiles.getRoom(player.getX(), player.getY()));
            }
            //the infiltrator will stop their power when it's gone on past it's limit
            if (enemy.getPowerDuration() > 1000) {
                enemy.resetPower();
            }
        }
        checkInfiltratorsSystems();
    }

    public void setHallucinate(boolean hallucinate) {
        this.hallucinate = hallucinate;
        hud.showHallucinateLabel(hallucinate);
    }

    public TiledMap getMap() {
        return map;
    }

    /**
     * Checks if every infiltrator is both available and close enough to a system to attack it.
     * If an infiltrator fits those criteria then it will attack the system it is close enough to.
     */
    private void checkInfiltratorsSystems() {
        //Starts attack if infiltrator in range of a system
        for (Infiltrator infiltrator : enemies) {
            if (infiltrator.isAvailable()) {
                for (ShipSystem system : tiles.getSystems()) {
                    if (system.getState() == 0) {
                        if (Utility.closeEnough(new Vector2(infiltrator.getX(), infiltrator.getY()), system.getPosition())) {
                            infiltrator.startDestruction(system);
                            system.startAttack();
                        }
                    }
                }
            }
        }
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
        map.dispose();
        renderer.dispose();
        shipStage.dispose();
        auberGame.batch.dispose();

    }
}

