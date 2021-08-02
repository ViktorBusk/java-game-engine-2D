package simulation.gravity;

import engine.game2D.Vector2D;

import java.awt.*;

public class Player extends CircleBody {
    private float speed = 0.0001f;
    public boolean LEFT, RIGHT, UP, DOWN;

    public Player(Vector2D position) {
        super(position, 30, Color.GREEN);
    }

    @Override
    public void update(long elapsedTime) {

        if (LEFT) this.acceleration.x -= this.speed * elapsedTime;
        if (RIGHT) this.acceleration.x += this.speed * elapsedTime;
        if (UP) this.acceleration.y -= this.speed * elapsedTime;
        if (DOWN) this.acceleration.y += this.speed * elapsedTime;

        super.update(elapsedTime);
    }
}
