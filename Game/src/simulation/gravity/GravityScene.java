package simulation.gravity;

import engine.game2D.Scene;
import engine.game2D.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GravityScene extends Scene {
    private final ArrayList<CircleBody> circleBodies;
    private double G = -0.0001;
    private Player player;
    private float wallStickyFactor = 0.5f;
    private int startCircleBodyAmount = 50;

    public GravityScene(Dimension size) {
        super(size);
        this.circleBodies = new ArrayList<>();

        for (int i = 0; i < this.startCircleBodyAmount; i++) {
            this.circleBodies.add(new CircleBody(new Vector2D(Math.random() * this.getPreferredSize().width,
                                         Math.random() * this.getPreferredSize().height), (int)(Math.random() * 30 + 10), Color.BLACK));
        }

        this.player = new Player(new Vector2D(this.getPreferredSize().width/2, this.getPreferredSize().height/2));
        this.circleBodies.add(this.player);

        this.sprites.addAll(this.circleBodies);
        this.addKeyBindings();
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

    private void addKeyBindings() {
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "A pressed");
        this.getActionMap().put("A pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.LEFT = true;
            }
        });
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "A released");
        this.getActionMap().put("A released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.LEFT = false;
            }
        });
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "D pressed");
        this.getActionMap().put("D pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.RIGHT = true;
            }
        });
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "D released");
        this.getActionMap().put("D released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.RIGHT = false;
            }
        });
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "W pressed");
        this.getActionMap().put("W pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.UP = true;
            }
        });
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "W released");
        this.getActionMap().put("W released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.UP = false;
            }
        });
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "S pressed");
        this.getActionMap().put("S pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.DOWN = true;
            }
        });
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "S released");
        this.getActionMap().put("S released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.DOWN = false;
            }
        });
    }

    @Override
    public void update(long elapsedTime) {
        super.update(elapsedTime);
        this.circleBodies.forEach((go) -> {

            // Apply GForce to each circleBody
            go.acceleration.set(0, 0);
            for (CircleBody other: this.circleBodies) {
                if(other != go) go.applyForce(this.GForce(go, other));
            }

            // Keep circleBodies inside the scene
            if(go.getPosition().x - go.getRadius() <= 0) {
                go.getPosition().x = go.getRadius();
                go.velocity.x *=- this.wallStickyFactor;
            }
            if(go.getPosition().x + go.getRadius() >= getWidth()) {
                go.getPosition().x = getWidth() - go.getRadius();
                go.velocity.x *=- this.wallStickyFactor;
            }
            if(go.getPosition().y - go.getRadius() <= 0) {
                go.getPosition().y = go.getRadius();
                go.velocity.y *=- this.wallStickyFactor;
            }
            if(go.getPosition().y + go.getRadius() >= getHeight()) {
                go.getPosition().y = getHeight() - go.getRadius();
                go.velocity.y *=- this.wallStickyFactor;
            }
        });
    }
}
