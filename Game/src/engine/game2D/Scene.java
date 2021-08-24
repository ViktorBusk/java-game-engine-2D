package engine.game2D;

import engine.game2D.component.Updatable;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public abstract class Scene extends JPanel implements Updatable { //(and Renderable) NOTE: JPanel already has a paintComponent()
    private final HashMap<String, GameObject> gameObjects;

    public Scene(Dimension size) {
        // we are using a game loop to repaint, so probably don't want swing randomly doing it for us
        this.setPreferredSize(size);
        this.setIgnoreRepaint(true);
        this.gameObjects = new HashMap<>();
    }

    public void addGameObject(GameObject gameObject, String name) {
        this.gameObjects.put(name, gameObject);
        Logger.debug("Added " + name + " " + gameObject);
    }

    protected GameObject getGameObject(String name) {
        return this.gameObjects.get(name);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // this method gets called on Scene#repaint in our game loop and we then render each in our game
        for(GameObject gameObject: gameObjects.values()) {
           gameObject.render(g2d);
        }
    }

    @Override
    public void update(long elapsedTime) {
        // Update each sprite
        for(GameObject gameObject: gameObjects.values()) {
            gameObject.update(elapsedTime);
        }
    }
}
