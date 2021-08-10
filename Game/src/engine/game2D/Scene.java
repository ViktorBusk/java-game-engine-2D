package engine.game2D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class Scene extends JPanel {
    protected final ArrayList<GameObject> gameObjects;

    public Scene(Dimension size) {
        // we are using a game loop to repaint, so probably don't want swing randomly doing it for us
        this.setPreferredSize(size);
        this.setIgnoreRepaint(true);
        this.gameObjects = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // this method gets called on Scene#repaint in our game loop and we then render each in our game
        gameObjects.forEach((gameObject) -> {
            gameObject.render(g2d);
        });
    }

    public void update(long elapsedTime) {
        // Update each sprite
        gameObjects.forEach((gameObject) -> {
            gameObject.update(elapsedTime);
        });
    }
}
