package engine.game2D.component;

import engine.game2D.Vector2D;

public class Rigidbody extends GameComponent {
    private int diameter;
    private float radius;
    private double mass;
    private Vector2D velocity = new Vector2D(0, 0);
    private Vector2D acceleration = new Vector2D(0, 0);
    private double maxSpeed = 1.5; //Double.MAX_VALUE;

    // FIXME: need to refactor this later into a proper rigidbody class!
    public Rigidbody(int diameter) {
        this.diameter = diameter;
        this.radius = this.diameter/2f;
        this.mass = (4f/3)*Math.PI*Math.pow(radius, 3);
    }

    public void applyForce(Vector2D force) {
        // F = m * a
        this.acceleration.add(force.getDivided(this.mass));
    }

    public Vector2D getVelocity() { return this.velocity; }
    public Vector2D getAcceleration() { return this.acceleration; }

    public int getDiameter(){ return this.diameter; }
    public float getRadius(){ return this.radius; }
    public double getMass(){ return  this.mass; }

    @Override
    public void update(long elapsedTime) {
        super.update(elapsedTime);// Update position
        this.velocity.add(this.acceleration.getMultiplied(elapsedTime));
        //this.velocity.clampMagnitude(this.maxSpeed);
        this.getParent().getComponent(Transform.class).getPosition().add(this.velocity.getMultiplied(elapsedTime));
    }
}
