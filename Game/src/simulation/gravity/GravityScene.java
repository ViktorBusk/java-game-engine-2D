package simulation.gravity;

import engine.game2D.Scene;
import engine.game2D.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;

// NOTE: Don't forget to add listeners!
import java.awt.event.KeyListener;
//import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GravityScene extends Scene implements KeyListener {
    private final ArrayList<CircleBody> circleBodies;
    private final double G = -0.0001;
    private final Player player;
    private final float wallStickyFactor = 0.5f;

    public GravityScene(Dimension size) {
        super(size);
        this.circleBodies = new ArrayList<>();

        int startCircleBodyAmount = 100;
        for (int i = 0; i < startCircleBodyAmount; i++) {
            this.circleBodies.add(new CircleBody(new Vector2D(Math.random() * this.getPreferredSize().width,
                    Math.random() * this.getPreferredSize().height), (int) (Math.random() * 10 + 10), Color.BLACK));
        }

        this.player = new Player(new Vector2D(this.getPreferredSize().width / 2f, this.getPreferredSize().height / 2f));
        this.circleBodies.add(this.player);

        this.sprites.addAll(this.circleBodies);
        this.addKeyListener(this);
    }

    private Vector2D GForce(CircleBody b1, CircleBody b2) {
        Vector2D F = new Vector2D(0, 0);

        double distance = Math.max(b1.getPosition().distance(b2.getPosition()), b1.getDiameter() + b2.getDiameter());
        double FMag = G * b1.getMass() * b2.getMass() / Math.pow(distance, 2);
        double a = Math.atan2(b1.getPosition().y - b2.getPosition().y, b1.getPosition().x - b2.getPosition().x);

        F.x = Math.cos(a) * FMag;
        F.y = Math.sin(a) * FMag;
        return F;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) { }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == KeyEvent.VK_W) player.UP = true;
        else if (keyCode == KeyEvent.VK_A) player.LEFT = true;
        else if (keyCode == KeyEvent.VK_S) player.DOWN = true;
        else if (keyCode == KeyEvent.VK_D) player.RIGHT = true;
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == KeyEvent.VK_W) player.UP = false;
        else if (keyCode == KeyEvent.VK_A) player.LEFT = false;
        else if (keyCode == KeyEvent.VK_S) player.DOWN = false;
        else if (keyCode == KeyEvent.VK_D) player.RIGHT = false;
    }

    @Override
    public void update(long elapsedTime) {
        this.circleBodies.forEach((go) -> {
            // Apply GForce to each circleBody
            go.acceleration.set(0, 0);
            for (CircleBody other : this.circleBodies) {
                if (other != go) go.applyForce(this.GForce(go, other));
            }
            // Friction
            //go.applyForce(new Vector2D(-Math.signum(go.velocity.x) * go.getMass() * 0.0001,
            //        -Math.signum(go.velocity.y) * go.getMass() * 0.0001));

            // Keep circleBodies inside the scene
            if (go.getPosition().x - go.getRadius() <= 0) {
                go.getPosition().x = go.getRadius();
                go.velocity.x *= -this.wallStickyFactor;
            }
            if (go.getPosition().x + go.getRadius() >= getWidth()) {
                go.getPosition().x = getWidth() - go.getRadius();
                go.velocity.x *= -this.wallStickyFactor;
            }
            if (go.getPosition().y - go.getRadius() <= 0) {
                go.getPosition().y = go.getRadius();
                go.velocity.y *= -this.wallStickyFactor;
            }
            if (go.getPosition().y + go.getRadius() >= getHeight()) {
                go.getPosition().y = getHeight() - go.getRadius();
                go.velocity.y *= -this.wallStickyFactor;
            }
        });
       super.update(elapsedTime);
    }
}