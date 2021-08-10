package engine.game2D.component;

import engine.game2D.GameObject;

import java.awt.*;

//FIXME: Maybe declare this class as an interface later
public abstract class GameComponent {
    private GameObject parent = null;

    public void setParent(GameObject parent) { this.parent = parent; }
    public GameObject getParent() { return this.parent; }

    public void render(Graphics2D g2d) {

    };

    public void update(long elapsedTime) {

    };
}
