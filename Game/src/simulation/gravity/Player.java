package simulation.gravity;

import engine.game2D.Vector2D;

import java.awt.*;

public class Player extends CircleBody {
    private final Vector2D speedForce = new Vector2D(10, 10);
    public boolean LEFT, RIGHT, UP, DOWN;

    public Player(Vector2D position) {
        super(position, 30, Color.GREEN);
    }

    @Override
    public void update(long elapsedTime) {
        if (LEFT) this.applyForce(this.speedForce.getRotatedTo(Math.PI));
        if (RIGHT) this.applyForce(this.speedForce.getRotatedTo(0));
        if (UP) this.applyForce(this.speedForce.getRotatedTo(3*Math.PI/2));
        if (DOWN) this.applyForce(this.speedForce.getRotatedTo(Math.PI/2));
        super.update(elapsedTime);
    }
}
