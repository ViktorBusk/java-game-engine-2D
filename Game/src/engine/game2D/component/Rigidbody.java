package engine.game2D.component;

import engine.game2D.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

public class Rigidbody extends GameComponent {
    private double mass;
    private Vector2D velocity = new Vector2D();
    private Vector2D acceleration = new Vector2D();
    private Shape shape;

    public Rigidbody(Shape shape, double mass) {
        this.shape = shape;
        this.mass = mass;
    }

    public void applyForce(Vector2D force) {
        // F = m * a
        this.acceleration.add(force.getDivided(this.mass));
    }
    public boolean collidesWith(Rigidbody other) {
        return this.shape.getBounds2D().intersects(other.getShape().getBounds2D());
    }

    public Shape getShape() { return this.shape; }
    public double getMass(){ return this.mass; }
    public Vector2D getVelocity() { return this.velocity; }
    public Vector2D getAcceleration() { return this.acceleration; }
    public Vector2D getCenterPosition() {return new Vector2D(this.shape.getBounds().getCenterX(), this.shape.getBounds().getCenterY());}

    private void updateShapePosition(Vector2D position){
        // Update shape position (fixed update based on transform-component-position)
        AffineTransform transform = new AffineTransform();
        transform.translate(Math.round(position.x - this.shape.getBounds().x), Math.round(position.y - this.shape.getBounds().y));
        Path2D path = (shape instanceof Path2D) ? (Path2D)shape : new GeneralPath(shape);
        this.shape = path.createTransformedShape( transform );
    }

    @Override
    public void update(long elapsedTime) {
        super.update(elapsedTime);
        this.velocity.add(this.acceleration.getMultiplied(elapsedTime));
        Vector2D position = this.getParent().getComponent(Transform.class).getPosition();
        position.add(this.velocity.getMultiplied(elapsedTime));
        this.updateShapePosition(position);
    }
}
