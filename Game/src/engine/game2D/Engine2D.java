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

    public Engine2D() {
        this.scenes = new HashMap<>();
        this.frame = new JFrame();

        this.frame.setTitle("Untitled");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Default frame size
        this.frame.setSize(1400, 800);
        this.frame.setVisible(true);
        this.setupLoopThread();
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
        this.scenes.put(name, scene);
    }

    public void setScene(String sceneName) {
        if(!this.scenes.containsKey(sceneName)) {
            System.out.println("Did not find scene: " + sceneName);
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
        // after setting the frame visible we start the game loop, this could be done in a button or wherever you want
        this.isRunning = true;
        this.gameLoop.start();
    }

    private void setupLoopThread() {
        gameLoop = new Thread(() -> {
            // how many frames should be drawn in a second
            final int FRAMES_PER_SECOND = 60;
            // calculate how many nano seconds each frame should take for our target frames per second.
            final long TIME_BETWEEN_UPDATES = 1000000000 / FRAMES_PER_SECOND;
            // track number of frames
            int frameCount;
            // if you're worried about visual hitches more than perfect timing, set this to 1. else 5 should be okay
            final int MAX_UPDATES_BETWEEN_RENDER = 1;

            // we will need the last update time.
            long lastUpdateTime = System.nanoTime();
            // store the time we started this will be used for updating map and character animations
            long currTime = System.currentTimeMillis();
            // alert if no current scene is set
            boolean alertNoCurrentScene = true;

            while (isRunning) {
                if (this.currentScene == null) {
                    if (alertNoCurrentScene) {
                        System.out.println("No scene set! Add a scene with addScene() and then set a scene with setScene().");
                        alertNoCurrentScene = false;
                    }
                    continue;
                } else alertNoCurrentScene = true;

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

                this.currentScene.repaint(); // draw call for rendering sprites etc

                long lastRenderTime = now;

                //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
                while (now - lastRenderTime < TIME_BETWEEN_UPDATES && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                    Thread.yield();
                    now = System.nanoTime();
                }
            }
        });
    }
}
