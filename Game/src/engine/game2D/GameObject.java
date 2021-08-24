package engine.game2D;

import engine.game2D.component.GameComponent;
import engine.game2D.component.Renderable;
import engine.game2D.component.Transform;
import engine.game2D.component.Updatable;

import java.awt.*;
import java.util.HashMap;

public class GameObject implements Renderable, Updatable {
    private final HashMap<Class<? extends GameComponent>, GameComponent> components;
    private Scene scene;
    private int ID;
    private static int IDCounter; // Total object created

    public GameObject(Scene scene) {
        this.components = new HashMap<>();
        // Every GameObject has a transform component
        this.addComponent(new Transform());
        this.scene = scene;
        this.ID = IDCounter++;
    }

    public <T extends GameComponent> void addComponent(T c) {
        c.setParent(this);
        this.components.put(c.getClass(), c);
    }

    public <T extends GameComponent> T getComponent(Class<T> c){
        return (T) components.get(c);
    }

    public <T extends GameComponent> boolean hasComponent(Class<T> c) {
        return this.components.containsKey(c);
    }

    public <T extends GameComponent> void removeComponent(Class<T> c) {
        this.components.get(c).setParent(null);
        this.components.remove(c);
    }

    public int getID() { return this.ID; }

    @Override
    public void render(Graphics2D g2d) {
        for (GameComponent component: this.components.values()) {
           component.render(g2d);
        }
    }

    @Override
    public void update(long elapsedTime) {
        for (GameComponent component: this.components.values()) {
            component.update(elapsedTime);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        else if (obj instanceof GameObject) {
            GameObject go = (GameObject) obj;
            return (this.ID == go.ID);
        }
        else return false;
    }

    @Override
    public String toString() {
        return "GameObject[ID:" + ID +  " " + this.getClass().getSimpleName() +
                " Pos:" + this.getComponent(Transform.class).getPosition() + " "
                + scene.getClass().getSimpleName() + "]";
    }
}
