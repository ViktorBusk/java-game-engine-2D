package engine.game2D.component;

import engine.game2D.Vector2D;

public class Transform extends GameComponent {
    private Vector2D position;
    private Vector2D rotation;
    private Vector2D scale;

    public Transform() {
        this.position = new Vector2D(0, 0);
        this.rotation = new Vector2D(0, 0); // NOTE: Rotation in degrees!
        this.scale = new Vector2D(1, 1);
    }

    public Transform(Vector2D position) {
        this();
        this.position = position;
    }

    public Transform(Vector2D position, Vector2D rotation) {
        this();
        this.position = position;
        this.rotation = rotation;
    }

    public Transform(Vector2D position, Vector2D rotation, Vector2D scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vector2D getPosition() { return this.position; }
    public Vector2D getRotation() { return this.rotation; }
    public Vector2D getScale() { return this.scale; }
}
