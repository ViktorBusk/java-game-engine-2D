package simulation.gravity;

import engine.game2D.Scene;
import engine.game2D.Vector2D;
import engine.game2D.component.Rigidbody;
import engine.game2D.component.Transform;

import java.awt.*;
import java.awt.event.KeyEvent;

// NOTE: Don't forget to add listeners!
import java.awt.event.KeyListener;
//import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GravityScene extends Scene implements KeyListener {
    private final ArrayList<CircleBody> circleBodies; // Need to keep another reference to circleBodies because of G-Force
    private final double G = -0.0001;
    private final float wallStickyFactor = 0.5f;

    public GravityScene(Dimension size) {
        super(size);
        this.circleBodies = new ArrayList<>();
        int startCircleBodyAmount =  50;
        for (int i = 0; i < startCircleBodyAmount; i++) {
            CircleBody circleBody = new CircleBody(this, new Vector2D(Math.random() * this.getPreferredSize().width,
                    Math.random() * this.getPreferredSize().height), (int) (Math.random() * 10 + 10), Color.BLACK);
            this.addGameObject(circleBody, "Circle Body " + i);
            this.circleBodies.add(circleBody);
        }

        Player player = new Player(this, new Vector2D(this.getPreferredSize().width / 2f, this.getPreferredSize().height / 2f));
        this.addGameObject(player, "Player");
        this.circleBodies.add(player);
        this.addKeyListener(this);
    }

    private Vector2D GForce(CircleBody b1, CircleBody b2) {
        Vector2D F = new Vector2D(0, 0);

        Rigidbody b1Body = b1.getComponent(Rigidbody.class);
        Rigidbody b2Body = b2.getComponent(Rigidbody.class);

        double distance = Math.max(b1Body.getCenterPosition().distance(b2Body.getCenterPosition()), b1Body.getShape().getBounds().width + b2Body.getShape().getBounds().width);
        double FMag = G * b1Body.getMass() * b2Body.getMass() / Math.pow(distance, 2);
        double a = Math.atan2(b1Body.getCenterPosition().y - b2Body.getCenterPosition().y, b1Body.getCenterPosition().x - b2Body.getCenterPosition().x);

        F.x = Math.cos(a) * FMag;
        F.y = Math.sin(a) * FMag;
        return F;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) { }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        Player player = (Player) this.getGameObject("Player");
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == KeyEvent.VK_W) player.UP = true;
        else if (keyCode == KeyEvent.VK_A) player.LEFT = true;
        else if (keyCode == KeyEvent.VK_S) player.DOWN = true;
        else if (keyCode == KeyEvent.VK_D) player.RIGHT = true;
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        Player player = (Player) this.getGameObject("Player");
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == KeyEvent.VK_W) player.UP = false;
        else if (keyCode == KeyEvent.VK_A) player.LEFT = false;
        else if (keyCode == KeyEvent.VK_S) player.DOWN = false;
        else if (keyCode == KeyEvent.VK_D) player.RIGHT = false;
    }

    @Override
    public void update(long elapsedTime) {
        super.update(elapsedTime);
        this.circleBodies.forEach((go) -> {
            // Apply GForce to each circleBody
            Vector2D position = go.getComponent(Transform.class).getPosition();
            Rigidbody body = go.getComponent(Rigidbody.class);

            body.getAcceleration().set(0, 0);
            for (CircleBody other : this.circleBodies) {
                if (other != go) body.applyForce(this.GForce(go, other));
            }
            // Keep circleBodies inside the scene
            if (position.x <= 0) {
                position.x = 0;
                body.getVelocity().x *= -this.wallStickyFactor;
            }
            if (position.x + body.getShape().getBounds().width >= getWidth()) {
                position.x = getWidth() - body.getShape().getBounds().width;
                body.getVelocity().x *= -this.wallStickyFactor;
            }
            if (position.y <= 0) {
                position.y = 0;
                body.getVelocity().y *= -this.wallStickyFactor;
            }
            if (position.y + body.getShape().getBounds().height >= getHeight()) {
                position.y = getHeight() - body.getShape().getBounds().height;
                body.getVelocity().y *= -this.wallStickyFactor;
            }
        });
    }
}