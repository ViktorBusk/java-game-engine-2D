package simulation.gravity;

import engine.game2D.Vector2D;
import engine.game2D.component.Rigidbody;

import java.awt.*;

public class Player extends CircleBody {
    private final Vector2D speedForce = new Vector2D(10, 10);
    public boolean LEFT, RIGHT, UP, DOWN;

    public Player(Vector2D position) {
        super(position, 30, Color.GREEN);
    }

    @Override
    public void update(long elapsedTime) {
        Rigidbody body = this.getComponent(Rigidbody.class);

        if (LEFT) body.applyForce(this.speedForce.getRotatedTo(Math.PI));
        if (RIGHT) body.applyForce(this.speedForce.getRotatedTo(0));
        if (UP) body.applyForce(this.speedForce.getRotatedTo(3*Math.PI/2));
        if (DOWN) body.applyForce(this.speedForce.getRotatedTo(Math.PI/2));
        super.update(elapsedTime);
    }
}
