package engine.game2D;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public abstract class Engine2D {
    private Thread gameLoop;
    private boolean isRunning;
    private final HashMap<String, Scene> scenes;
    private Scene currentScene;
    private final JFrame frame;
    private int TARGET_FPS;
    private int FPS;
    public static final int FALLBACK_FPS = 60;

    public Engine2D() {
        this.scenes = new HashMap<>();
        this.frame = new JFrame();

        this.setupFromGraphicsEnvironment();

        this.frame.setTitle("Untitled");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.frame.setVisible(true);
        this.setupLoopThread();
    }

    private void setupFromGraphicsEnvironment() {
        Logger.debug("Setting up graphics environment...");
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        DisplayMode defaultDisplayMode = ge.getDefaultScreenDevice().getDisplayMode();

        Vector2D resolution = new Vector2D(defaultDisplayMode.getWidth(), defaultDisplayMode.getHeight());
        TARGET_FPS = Integer.max(defaultDisplayMode.getRefreshRate() , FALLBACK_FPS);
        Logger.debug("Detected refreshrate on main monitor: " + TARGET_FPS);

        Vector2D frameSize = resolution.getDivided(1.2);
        this.frame.setSize((int)Math.round(frameSize.x), (int)Math.round(frameSize.y));
        Logger.debug("Frame size: " + frameSize);
    }

    public void setFrameSize(Dimension size) {
        this.frame.setSize(size);
    }

    public Dimension getFrameSize() {
        return this.frame.getSize();
    }

    public void setTitle(String title) {
        this.frame.setTitle(title);
    }

    public void addScene(String name, Scene scene) {
        Logger.debug("Added scene " + scene.getClass().getSimpleName());
        this.scenes.put(name, scene);
    }

    public void setScene(String sceneName) {
        if(!this.scenes.containsKey(sceneName)) {
            Logger.error("Did not find scene: " + sceneName);
            return;
        }

        // Remove the current scene if it is in the frame
        for(Component component: this.frame.getComponents()) {
            if (component.equals(this.currentScene)) {
                this.currentScene.setFocusable(false);
                this.frame.remove(this.currentScene);
                break;
            }
        }

        this.currentScene = this.scenes.get(sceneName);
        this.currentScene.setFocusable(true);
        this.frame.add(this.currentScene);
    }

    public void startLoop() {
        Logger.debug("Starting loop...");
        // after setting the frame visible we start the game loop, this could be done in a button or wherever you want
        this.isRunning = true;
        this.gameLoop.start();
    }

    private void setupLoopThread() {
        gameLoop = new Thread(() -> {
            // calculate how many nano seconds each frame should take for our target frames per second.
            final long TIME_BETWEEN_UPDATES = 1000000000 / TARGET_FPS;
            // if you're worried about visual hitches more than perfect timing, set this to 1. else 5 should be okay
            final int MAX_UPDATES_BETWEEN_RENDER = 1;
            // we will need the last update time.
            long lastUpdateTime = System.nanoTime();
            // we will need th last render time.
            long lastRenderTime = 0;
            // store the time we started this will be used for updating map and character animations
            long currTime = System.currentTimeMillis();
            // loop
            while (isRunning) {
                long now = System.nanoTime();
                long elapsedTime = System.currentTimeMillis() - currTime;
                currTime += elapsedTime;
                int updateCount = 0;
                // do as many game updates as we need to, potentially playing catchup.
                while (now - lastUpdateTime >= TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BETWEEN_RENDER) {
                    this.currentScene.update(elapsedTime);//Update the entity movements and collision checks etc (all has to do with updating the games status i.e  call move() on Enitites)
                    lastUpdateTime += TIME_BETWEEN_UPDATES;
                    updateCount++;
                }
                // if for some reason an update takes forever, we don't want to do an insane number of catchup's.
                // if you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
                if (now - lastUpdateTime >= TIME_BETWEEN_UPDATES) {
                    lastUpdateTime = now - TIME_BETWEEN_UPDATES;
                }
                // draw call for rendering sprites etc
                this.currentScene.repaint();
                // Calculate FPS
                FPS = (int)Math.round(1000000000.0 / (now - lastRenderTime));
                lastRenderTime = now;
                //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
                while (now - lastRenderTime < TIME_BETWEEN_UPDATES && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                    Thread.yield();
                    now = System.nanoTime();
                }
            }
        });
    }
}
