package engine.game2D.component;

import engine.game2D.GameObject;

import java.awt.*;

public abstract class GameComponent implements Renderable, Updatable {
    private GameObject parent = null;

    public void setParent(GameObject parent) { this.parent = parent; }
    public GameObject getParent() { return this.parent; }

    @Override
    public void render(Graphics2D g2d){}

    @Override
    public void update(long elapsedTime){}
}
