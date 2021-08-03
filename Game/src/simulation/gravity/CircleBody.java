package simulation.gravity;
import engine.game2D.Sprite;
import engine.game2D.Vector2D;

import java.awt.*;

public class CircleBody extends Sprite {
    protected double maxSpeed = 1.5;
    protected Vector2D velocity = new Vector2D(0, 0);
    protected Vector2D acceleration = new Vector2D(0, 0);
    private Color color;
    private int diameter;
    private float radius;
    private double mass;

    public CircleBody(Vector2D position, int diameter, Color color) {
        super(position, new Vector2D(diameter, diameter));
        this.color = color;
        this.add(diameter);
    }

    public void add(double diameter) {
        this.diameter += diameter;
        this.radius = this.diameter/2f;
        this.mass = (4f/3)*Math.PI*Math.pow(radius, 3);
    }

    public void applyForce(Vector2D force) {
        // F = m * a
        this.acceleration.add(force.getDivided(this.mass));
    }

    public float getRadius() { return this.radius; }
    public int getDiameter() { return this.diameter; }
    public double getMass() { return this.mass; }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(this.color);
        g2d.fillOval((int)Math.round(this.position.x - this.radius), (int)Math.round(this.position.y - this.radius), diameter, diameter);
    }

    @Override
    public void update(long elapsedTime) {
        // Update position
        this.velocity.add(this.acceleration.getMultiplied(elapsedTime));
        this.velocity.clampMagnitude(this.maxSpeed);
        this.position.add(this.velocity.getMultiplied(elapsedTime));
    }
}
