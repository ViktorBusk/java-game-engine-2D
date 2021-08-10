package engine.game2D;

import engine.game2D.component.GameComponent;
import engine.game2D.component.Transform;

import java.awt.*;
import java.util.HashMap;

public class GameObject {
    private final HashMap<Class<? extends GameComponent>, GameComponent> components;

    public GameObject() {
        this.components = new HashMap<>();
        // Every GameObject has a transform component
        this.addComponent(new Transform());
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
        this.components.remove(c);
    }
    public void render(Graphics2D g2d) {
        for (GameComponent component: this.components.values()) {
           component.render(g2d);
        }
    };

    public void update(long elapsedTime) {
        for (GameComponent component: this.components.values()) {
            component.update(elapsedTime);
        }
    }
}
