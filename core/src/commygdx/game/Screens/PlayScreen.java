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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.*;
import commygdx.game.AI.graph.PathGraph;
import commygdx.game.AI.graph.PathNode;
import commygdx.game.AuberGame;
import commygdx.game.ShipSystem;
import commygdx.game.TileWorld;
import commygdx.game.Utility;
import commygdx.game.actors.DemoAuber;
import commygdx.game.actors.Infiltrator;
import commygdx.game.actors.NPC;
import java.io.BufferedReader;
import java.io.IOException;
import commygdx.game.stages.Hud;
import commygdx.game.actors.Auber;

import java.io.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PlayScreen implements Screen {
    protected AuberGame auberGame;
    private final Hud hud;
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

    private Texture health;
    private Texture invis;
    private Texture slow;
    private Texture speed;
    private Texture rdmg;
    private Image h;
    private Image i;
    private Image sl;
    private Image sp;
    private Image r;

    private final TileWorld tiles;
    protected int scale;


    public PlayScreen(AuberGame auberGame, boolean demo, boolean loadFromSave) {
        this.auberGame = auberGame;
        this.demo = demo;
        this.scale = AuberGame.ZOOM;
        this.loadFromSave = loadFromSave;
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
        }
        else {
            ArrayList<ShipSystem> emptyList = new ArrayList<ShipSystem>();
            tiles = new TileWorld(this, emptyList);
        }

        //Used for pause screen
        pauseTexture = new Texture("pauseImage.png");
        pauseTexture2 = new Texture("pauseImage2.png");

        //Used for the infiltrator's hallucinate power
        hallucinateTexture = new Texture("hallucinateV2.png");
        hallucinate = false;

        //TODO save health
        hud = new Hud(enemies, tiles.getSystems(), 95);
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
            if (!loadFromSave){
                player = new Auber(new Vector2(450 * scale, 778 * scale));
            }
            else{
                loadAuberSave();
            }
        } else {
            player = new DemoAuber(new Vector2(450 * scale, 778 * scale), graph);
        }
    }

    private void createEnemies(){
        if (!loadFromSave){
            //Creating and placing infiltrators
            enemies = new ArrayList<Infiltrator>(Arrays.asList(
                    new Infiltrator(new Vector2(4700, 2000), 1, graph, false),
                    new Infiltrator(new Vector2(4800, 2300), 2, graph, false),
                    new Infiltrator(new Vector2(5000, 7356), 3, graph, false),
                    new Infiltrator(new Vector2(4732, 7000), 4, graph, false),
                    new Infiltrator(new Vector2(4732, 7500), 1, graph, false),
                    new Infiltrator(new Vector2(4732, 7800), 1, graph, false),
                    new Infiltrator(new Vector2(4200, 7800), 2, graph, false),
                    new Infiltrator(new Vector2(5400, 7800), 2, graph, false)
            ));
        }
        else{
            loadEnemySave();
        }
    }

    private void createNPCs() {
        if (!loadFromSave){
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
        }
        else{
            loadNPCSave();
        }
    }

    public void update(float dt) {
        shipStage.act(dt);
        player.damageAuber(enemies, hud,false);
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
            try {
                powerupCheck();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            player.checkCollision(tiles.getCollisionBoxes());
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
        }
        else {
            hud.updateAttacks(tiles.getSystems());
            hud.stage.draw();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            paused = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            paused = false;
            saved = false;
        }
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

    private void updateCamera() {
        //sets camera to players position
        Vector3 pos = new Vector3((player.getX()) + player.getWidth() / 2, (player.getY()) + player.getHeight() / 2, 0);
        shipStage.getViewport().getCamera().position.set(pos);
        gamecam.position.set(pos);
        gamecam.update();
        renderer.setView(gamecam);
        auberGame.batch.setProjectionMatrix(hud.stage.getCamera().combined);
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
        //teleport auber
        if (!auberGame.onTeleport.equals("true") && !auberGame.onTeleport.equals("false")) {
            teleportAuber();
            auberGame.onTeleport = "false";
        }
    }

    private void powerupCheck() throws InterruptedException {
        if (demo) {
            return;
        }
        if (player.powerUpCheck(tiles)!= "null"){
            if (player.powerUpCheck(tiles)=="Health"){
                hud.healthPower();
                hud.powerUpLabel.setText("Health");
                System.out.println("Health");
            }
            if (player.powerUpCheck(tiles)=="Invis"){
                player.goInvisible();
                hud.powerUpLabel.setText("Invisible");
                System.out.println("Invis");
            }
            if (player.powerUpCheck(tiles)=="Slow Infiltrators"){
                System.out.println("Slow");
                hud.powerUpLabel.setText("Slow");
                for (int x = 0; x< enemies.size(); x++){
                        enemies.get(x).setSpeed(4f);
                    }
            }
            if (player.powerUpCheck(tiles)=="Speed"){
                System.out.println("Speed");
                hud.powerUpLabel.setText("Speed");
                player.movementSystem.setSpeed(9f);
            }
            if (player.powerUpCheck(tiles)=="Rdmg"){
                System.out.println("R");
                hud.powerUpLabel.setText("Reduce Damage");
                player.damageAuber(enemies,hud,true);

            }
        }
        else{
            hud.powerUpLabel.setText("None");
            player.sprite.setTexture(new Texture(Gdx.files.internal("Characters/auberSprite.png")));
            for (int x = 0; x< enemies.size(); x++){
                enemies.get(x).setSpeed(6f);
            }
            player.movementSystem.setSpeed(6f);

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
        }
        else {
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
        }
        else {
            auberGame.batch.draw(pauseTexture2, (Gdx.graphics.getWidth()) / 2.0f, (Gdx.graphics.getHeight()) / 2.5f);
        }
        auberGame.batch.end();
    }

    /**
     * Load auber from save
     */
    private void loadAuberSave() {
        //Read JSON
        JSONParser jsonP = new JSONParser();
        try(FileReader reader = new FileReader("saveSprite.json")) {
            //Read JSON File
            Object obj = jsonP.parse(reader);
            JSONArray sprList1 = (JSONArray) obj;
            JSONObject empObj = (JSONObject) sprList1.get(0);
            System.out.println(empObj);
            JSONObject j = (JSONObject) empObj.get("sprite0");

            double x = (double) j.get("x");
            double y = (double) j.get("y");
            player = new Auber(new Vector2((float) x, (float) y));
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load enemy from save
     */
    private void loadEnemySave() {
        int [] enemyPowerUpList = {1,2,3,4,1,1,2,2};
        ArrayList<Infiltrator> localEnemies = new ArrayList<>();

        //Read JSON
        JSONParser jsonP = new JSONParser();
        try(FileReader reader = new FileReader("saveSprite.json")) {
            //Read JSON File
            Object obj = jsonP.parse(reader);
            JSONArray sprList = (JSONArray) obj;

            for (int i = 1; i < 9; i++) {
                System.out.println(i);
                JSONObject e = (JSONObject) sprList.get(i);
                JSONObject empObj = (JSONObject) e.get("sprite" + i);
                double x = (double) empObj.get("x");
                double y = (double) empObj.get("y");
                boolean arrested = (boolean) empObj.get("arrested");
                System.out.println("x: " + x);
                System.out.println("y: " + y);
                Infiltrator inf = new Infiltrator(new Vector2((float) x, (float) y),
                        enemyPowerUpList[i - 1], graph, arrested);
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
        try(FileReader reader = new FileReader("saveSprite.json")) {
            //Read JSON File
            Object obj = jsonP.parse(reader);
            JSONArray sprList = (JSONArray) obj;

            for (int i = 9; i < sprList.size(); i++) {
                JSONObject e = (JSONObject) sprList.get(i);
                JSONObject empObj = (JSONObject) e.get("sprite" + i);
                double x = (double) empObj.get("x");
                double y = (double) empObj.get("y");
                System.out.println("x: " + x);
                System.out.println("y: " + y);
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
        try(FileReader reader = new FileReader("saveSystem.json")) {
            //Read JSON File
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

        //Creating and placing infiltrators
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
            sys.put("state", tiles.getSystems().get(i).getState());

            JSONObject sysObj = new JSONObject();
            sysObj.put("system" + i, sys);
            sysList.add(sysObj);
        }
        // write sprite
        try(FileWriter file = new FileWriter("saveSprite.json")) {
            file.write(sprList.toJSONString());
            file.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // write system
        try(FileWriter file = new FileWriter("saveSystem.json")) {
            file.write(sysList.toJSONString());
            file.flush();
        }
        catch (IOException e) {
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
            enemy.updateTimers(dt * 100);

            //the infiltrator will use their power if the auber is close enough and it's off cooldown
            if (enemy.getPowerCooldown() > 500 && inRange(enemy) && !enemy.getIsArrested()) {
                enemy.usePower(this, tiles.getRoom(player.getX(), player.getY()));
            }
            //the infiltrator will stop their power when it's gone on past it's limit
            if (enemy.getPowerDuration() > 1000) {
                enemy.stopPower(this);
            }
        }
        checkInfiltratorsSystems();
    }

    /**
     * Determines if the given enemy is within a certain range of the auber
     *
     * @param enemy The enemy whose range is being checked
     * @return True if the enemy is in range, false otherwise
     */
    public boolean inRange(Infiltrator enemy) {
        return enemy.getX() < player.getX() + 1000 && enemy.getX() > player.getX() - 1000 &&
                enemy.getY() < player.getY() + 1000 && enemy.getY() > player.getY() - 1000;
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

