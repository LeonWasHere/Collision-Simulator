package org.lwjglb.game;

/**
 * Author: Leon Wasiliew
 * Subject: INFT4000 - Special Topics 1
 * Creation Date: 2026-04-15
 * Modification Date: 2026-04-15
 * Description: DummyGame provides a sample implementation of IGameLogic (interface),
 * demonstrating scene setup, rendering, lighting, camera control, object spawning,
 * and basic collision testing.
 */

import org.joml.*;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;

import org.lwjglb.engine.*;
import org.lwjglb.engine.graph.Camera;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.Renderer;
import org.lwjglb.engine.graph.lights.DirectionalLight;
import org.lwjglb.engine.graph.lights.PointLight;
import org.lwjglb.engine.graph.weather.Fog;
import org.lwjglb.engine.items.*;
import org.lwjglb.engine.loaders.assimp.StaticMeshesLoader;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;


/**
 * Russ <RS> changed this game code to put a bunch
 * of moving GameItems on the screen.
 * <p>
 * To understand what's going on, do these steps:
 * - look at IGameLogic ... the game has several key entry points...understand those.
 * <p>
 * - Follow the use of "camera"  the camera is how you see the scene.  Easiest way to
 * to understand it is put the camera away a bit and pointing back towards the scene, and leave it alone.
 * <p>
 * - Follow the creation of a mesh, being added to the scene.  You can make your own mesh with Blender.
 * <p>
 * - Blender: save your mesh as an OBJ file with cube projection including Normals,
 * UVs, Materials, and Triangulate Faces.
 * <p>
 * - public void input(Window window, MouseInput mouseInput) => process keystrokes sent to the Graphics window.
 * <p>
 * - public void update(float interval, MouseInput mouseInput, Window window) called many times per second.  Use
 * this method for synched game logic. Processing within events is sloppy, and pron to threading
 * errors (and done a lot in this sample).
 */
public class DummyGame implements IGameLogic {

    // Constant for mouse rotation sensitivity when adjusting the camera
    private static final float MOUSE_SENSITIVITY = 0.2f;

    // Constant for the camera movement step size per update
    private static final float CAMERA_POS_STEP = 0.40f;

    // Constants defining the world boundaries for object placement and movement
    private static final float WORLD_MIN_X = -50.0f;
    private static final float WORLD_MAX_X = 50.0f;
    private static final float WORLD_MIN_Y = -0.0f;
    private static final float WORLD_MAX_Y = 30.0f;
    private static final float WORLD_MIN_Z = -50.0f;
    private static final float WORLD_MAX_Z = 50.0f;

    private final Vector3f cameraInc;
    private final Renderer renderer;
    private final Camera camera;
    private Scene scene;

    private float angleInc;
    private float lightAngle;
    private boolean firstTime = true;
    private boolean sceneChanged;

    private Vector3f pointLightPos;

    private boolean removeAll = false;


    private boolean testModeActive = false;   // Tracks whether the Collision Test is active

    private boolean isTKeyPressedLastFrame = false;  // Tracks whether the "T" key is pressed last
    private boolean isCKeyPressedLastFrame = false;  // Tracks whether the "C" key is pressed last
    private boolean isPKeyPressedLastFrame = false;  // Tracks whether the "P" key is pressed last
    private boolean isRKeyPressedLastFrame = false;  // Tracks whether the "R" key is pressed last

    private java.util.Random random = new java.util.Random();  // Generates random numbers

    private Vehicle lastVehicleAdded;  // Stores the most recently added vehicle

    /**
     * Default constructor for DummyGame.
     * Initializes the renderer, camera, movement vector,
     * and default lighting rotation values.
     */
    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        angleInc = 0;
        lightAngle = 90;
    }

    /**
     * Initializes the engine state and prepares all scene components.
     * Loads terrain, fog, skybox, lighting, and sets the initial camera
     * position and orientation for the starting window.
     * @param window
     * @throws Exception
     */
    @Override
    public void init(Window window) throws Exception {

        renderer.init(window);  // Initializes renderer resources and shaders

        scene = new Scene();  // Creates a new scene container

        loadDefaultScene();  // Loads the default scene
    }

    /**
     * Initializes all lighting used in the scene.
     * Configures ambient, skybox, directional, and point lights.
     */
    private void setupLights() {
        SceneLight sceneLight = new SceneLight();  // Creates a new lighting container
        scene.setSceneLight(sceneLight);           // Registers the container with the scene

        // Ambient and skybox lighting
        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
        sceneLight.setSkyBoxLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional light setup
        float lightIntensity = 1.0f;
        Vector3f lightDirection = new Vector3f(0, 1, 1);
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1),
                lightDirection, lightIntensity);
        sceneLight.setDirectionalLight(directionalLight);

        // Initial point light position
        pointLightPos = new Vector3f(0.0f, 25.0f, 0.0f);

        // Point light setup
        Vector3f pointLightColour = new Vector3f(0.0f, 1.0f, 0.0f);
        PointLight.Attenuation attenuation = new PointLight.Attenuation(1, 0.0f, 0);
        PointLight pointLight = new PointLight(pointLightColour, pointLightPos, lightIntensity, attenuation);
        sceneLight.setPointLightList(new PointLight[]{pointLight});
    }

    /**
     * Processes user input for the current frame.
     * Handles scene toggling, camera movement, lighting adjustments,
     * and prepares movement increments for the update cycle.
     * @param window
     * @param mouseInput
     */
    @Override
    public void input(Window window, MouseInput mouseInput) {

        // Resets scene-change flag and clears camera movement increments
        sceneChanged = false;
        cameraInc.set(0, 0, 0);

        // Toggles the collision test once the T key is pressed
        boolean tPressed = window.isKeyPressed(GLFW_KEY_T);
        if (tPressed && !isTKeyPressedLastFrame) {
            testModeActive = !testModeActive;

            if (testModeActive) {
                executeCollisionTest();  // Executes the collision test if it is newly active
            } else {
                loadDefaultScene();  // Resets to the default scene if it is not active
            }
        }
        isTKeyPressedLastFrame = tPressed;

        // Defines camera movement controls (WASD + Z/X for vertical movement)
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }

        // Directional light rotation (-/=)
        if (window.isKeyPressed(GLFW_KEY_LEFT_BRACKET)) {
            angleInc -= 0.05f;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT_BRACKET)) {
            angleInc += 0.05f;
        } else {
            angleInc = 0;
        }

        // Point Light vertical adjustments
        if (window.isKeyPressed(GLFW_KEY_1)) {
            pointLightPos.y += 0.5f;
        } else if (window.isKeyPressed(GLFW_KEY_2)) {
            pointLightPos.y -= 0.5f;
        }

        // Spawns Car
        boolean cPressed = window.isKeyPressed(GLFW_KEY_C);
        if (cPressed && !isCKeyPressedLastFrame) {
            spawnCar();
        }
        isCKeyPressedLastFrame = cPressed;

        // Spawns Plane
        boolean pPressed = window.isKeyPressed(GLFW_KEY_P);
        if (pPressed && !isPKeyPressedLastFrame) {
            spawnPlane();
        }
        isPKeyPressedLastFrame = pPressed;

        // Resets Scene
        boolean rPressed = window.isKeyPressed(GLFW_KEY_R);
        if (rPressed && !isRKeyPressedLastFrame) {
            loadDefaultScene();
        }
        isRKeyPressedLastFrame = rPressed;

        if (lastVehicleAdded != null) {

            Vector3f velocity = lastVehicleAdded.getVelocity();

            // Forward / Backward (Z axis)
            if (window.isKeyPressed(GLFW_KEY_UP)) {
                velocity.z -= 0.01f;
            }
            if (window.isKeyPressed(GLFW_KEY_DOWN)) {
                velocity.z += 0.01f;
            }

            // Left / Right (X axis)
            if (window.isKeyPressed(GLFW_KEY_LEFT)) {
                velocity.x -= 0.01f;
            }
            if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
                velocity.x += 0.01f;
            }

            // Speed control
            if (window.isKeyPressed(GLFW_KEY_EQUAL)) {
                velocity.mul(1.05f);
            }
            if (window.isKeyPressed(GLFW_KEY_MINUS)) {
                velocity.mul(0.95f);
            }

        }

        // TODO: Implement GUI actions (Add Car, Add Plane, Clear Screen)
    }

    /**
     * Spawns a car at a random position within the scene.
     */
    private void spawnCar() {
        try {
            // Loads the car mesh
            Mesh[] mesh = StaticMeshesLoader.load("src/main/resources/models/russ/Chevrolet_Camaro_SS_Low.obj",
                    "src/main/resources/models/russ");

            Car car = new Car(mesh);  // Creates a new car instance

            // Assigns a random position within a 20 x 20 area on the ground
            car.setPosition(-10 + random.nextFloat() * 20, 0.0f, -10 + random.nextFloat() * 20);

            // Applies a slow default velocity
            car.setVelocity(0.01f, 0.0f, 0.01f);

            scene.addGameItem(car);  // Adds the car to the scene

            lastVehicleAdded = car;  // Tracks this car as the last added vehicle

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Spawns a plane at a random position within the scene.
     */
    private void spawnPlane() {
        try {
            // Loads the plane mesh
            Mesh[] mesh = StaticMeshesLoader.load("src/main/resources/models/russ/toyPlane.obj",
                    "src/main/resources/models/russ");

            Plane plane = new Plane(mesh);  // Creates a new plane instance

            // Assigns a random position within a 20 x 20 area with altitude between 5 and 15
            plane.setPosition(-10 + random.nextFloat() * 20, 5 + random.nextFloat() * 10, -10 + random.nextFloat() * 20);

            // Applies a slow default velocity
            plane.setVelocity(0.01f, 0.005f, 0.01f);

            scene.addGameItem(plane);  // Adds the plane to the scene

            lastVehicleAdded = plane;  // Tracks this plane as the last added vehicle

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Enforces world movement constraints for all Vehicle objects (including Car & Plane).
     * Applies boundary checks on each axis, reverses velocity when limits are exceeded.
     */
    private void enforceVehicleConstraints() {

        // Iterates through all GameItems in the scene
        for (GameItem item : scene.getgameItems()) {

            // Processes only Vehicle objects
            if (item instanceof Vehicle) {

                Vehicle vehicle = (Vehicle) item;
                Vector3f pos = vehicle.getPosition();
                Vector3f vel = vehicle.getVelocity();

                // X-axis boundaries (horizontal bounce)
                if (pos.x < WORLD_MIN_X) {
                    pos.x = WORLD_MIN_X;
                    vel.x *= -1;
                } else if (pos.x > WORLD_MAX_X) {
                    pos.x = WORLD_MAX_X;
                    vel.x *= -1;
                }

                // Z-axis boundaries (depth bounce)
                if (pos.z < WORLD_MIN_Z) {
                    pos.z = WORLD_MIN_Z;
                    vel.z *= -1;
                } else if (pos.z > WORLD_MAX_Z) {
                    pos.z = WORLD_MAX_Z;
                    vel.z *= -1;
                }

                // Y-axis behavior depends on vehicle type
                if (vehicle instanceof Car) {

                    // Cars do not move vertically
                    vel.y = 0;

                } else if (vehicle instanceof Plane) {

                    // Planes bounce vertically
                    if (pos.y < WORLD_MIN_Y) {
                        pos.y = WORLD_MIN_Y;
                        vel.y *= -1;
                    } else if (pos.y > WORLD_MAX_Y) {
                        pos.y = WORLD_MAX_Y;
                        vel.y *= -1;
                    }
                }
            }
        }
    }

    /**
     * Executes a predefined collision test scenario.
     * Clears the scene, loads meshes, spawns test vehicles and planes,
     * and prints a message to the console.
     */
    private void executeCollisionTest() {
        try {
            scene.removeAll();

            // Loads meshes (reuses the existing ones)
            Mesh[] terrainMesh = StaticMeshesLoader.load(
                    "src/main/resources/models/terrain/terrain.obj",
                    "src/main/resources/models/terrain");
            Mesh[] carMesh = StaticMeshesLoader.load(
                    "src/main/resources/models/russ/Chevrolet_Camaro_SS_Low.obj",
                    "src/main/resources/models/russ");
            Mesh[] planeMesh = StaticMeshesLoader.load(
                    "src/main/resources/models/russ/toyPlane.obj",
                    "src/main/resources/models/russ");

            GameItem terrain = new GameItem(terrainMesh);
            terrain.setPosition(0.0f, 0.0f, 0.0f);
            terrain.setScale(100.0f);

            // Creates two car objects
            Car car1 = new Car(carMesh);
            Car car2 = new Car(carMesh);

            // Sets the position of the two cars
            car1.setPosition(-5.0f, 0.0f, 0.0f);
            car2.setPosition(5.0f, 0.0f, 0.0f);

            // Sets the speed of the two cars
            car1.setVelocity(0.02f, 0.0f, 0.0f);
            car2.setVelocity(-0.02f, 0.0f, 0.0f);

            // Creates two plane objects
            Plane plane1 = new Plane(planeMesh);
            Plane plane2 = new Plane(planeMesh);

            // Sets the position of the two planes
            plane1.setPosition(-5.0f, 5.0f, 0.0f);
            plane2.setPosition(5.0f, 5.0f, 0.0f);

            // Sets the speed of the two planes
            plane1.setVelocity(0.02f, 0.0f, 0.0f);
            plane2.setVelocity(-0.0f, 0.0f, 0.0f);

            // Adds to scene
            scene.setGameItems(new GameItem[]{
                    car1, car2, plane1, plane2
            });

            // Prints info to the console
            System.out.println("Collision test started (T key)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDefaultScene() {
        try {
            scene.removeAll();

            lastVehicleAdded = null;  // No active vehicle after reset

            // Setup Terrain
            Mesh[] terrainMesh = StaticMeshesLoader.load("src/main/resources/models/terrain/terrain.obj",
                    "src/main/resources/models/terrain");

            GameItem terrain = new GameItem(terrainMesh);
            terrain.setPosition(0.00f, -15.000f, 0.000f);
            terrain.setScale(100.0f);

            scene.setGameItems(new GameItem[]{terrain});

            // Setup Fog
            Vector3f fogColour = new Vector3f(0.5f, 0.5f, 0.5f);
            scene.setFog(new Fog(true, fogColour, 0.02f));

            // Setup SkyBox
            float skyBoxScale = 100.0f;
            SkyBox skyBox = new SkyBox("src/main/resources/models/skybox.obj", new Vector4f(0.65f, 0.65f, 0.65f, 1.0f));
            skyBox.setScale(skyBoxScale);
            scene.setSkyBox(skyBox);

            // Setup Lights
            setupLights();

            // Setup camera position and rotation to look back at the scene
            camera.setPosition(-17.0f, 17.0f, -30.0f);
            camera.setRotation(20.0f, 140.0f, 0.0f);

            System.out.println("Scene reset");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the game state for the current frame.
     * @param interval
     * @param mouseInput
     * @param window
     */
    @Override
    public void update(float interval, MouseInput mouseInput, Window window) {

        // Since we move gameItems in the background (with their own
        // thread, all the time, so cause the lighting/shadows to be recomputed
        sceneChanged = true;

        // Retrieves all GameItems in the scene
        List<GameItem> gameItems = scene.getgameItems();

        // Filters only Vehicle objects for collision checks
        List<Vehicle> vehicles = new ArrayList<Vehicle>();

        // Determines if each GameItem is a Vehicle
        for (GameItem item : gameItems) {
            if (item instanceof Vehicle) {
                vehicles.add((Vehicle) item);
            }
        }

        // Runs collision detection on all vehicles
        CollisionManager.manageVehicleCollision(vehicles);

        // Applies camera movement based on the input
        camera.movePosition(
                cameraInc.x * CAMERA_POS_STEP,
                cameraInc.y * CAMERA_POS_STEP,
                cameraInc.z * CAMERA_POS_STEP);

        // Handles camera rotation when the right mouse button is pressed
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(
                    rotVec.x * MOUSE_SENSITIVITY,
                    rotVec.y * MOUSE_SENSITIVITY,
                    0);
        }

        // Updates directional light angle
        lightAngle += angleInc;
        if (lightAngle < 0) {
            lightAngle = 0;
        } else if (lightAngle > 180) {
            lightAngle = 180;
        }

        // Computes new light direction from the angle
        float zValue = (float) Math.cos(Math.toRadians(lightAngle));
        float yValue = (float) Math.sin(Math.toRadians(lightAngle));

        // Updates directional light vector
        Vector3f lightDirection = this.scene.getSceneLight().getDirectionalLight().getDirection();
        lightDirection.x = 0;
        lightDirection.y = yValue;
        lightDirection.z = zValue;
        lightDirection.normalize();

        // Updates view matrix
        camera.updateViewMatrix();
    }

    /**
     * Renders the current frame to the window.
     * @param window
     */
    @Override
    public void render(Window window) {

        // Checks if all items should be removed and resets the scene
        if (removeAll) {
            loadDefaultScene();  // Resets to default scene state
            removeAll = false;   // Clears the flag
        }

        // Marks the scene as changed on the first render pass
        if (firstTime) {
            sceneChanged = true;
            firstTime = false;
        }

        // Handles window resizing
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.updateProjectionMatrix();
            renderer.resize(window);
            window.setResized(false);
        }

        // Performs the actual rendering of the screen
        renderer.render(window, camera, scene, sceneChanged);
    }

    /**
     * Releases resources by cleaning up the renderer and scene.
     */
    @Override
    public void cleanup() {
        renderer.cleanup();
        scene.cleanup();
    }
}
