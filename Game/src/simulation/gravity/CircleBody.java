package simulation.gravity;
import engine.game2D.GameObject;
import engine.game2D.Vector2D;
import engine.game2D.component.Rigidbody;
import engine.game2D.component.Transform;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class CircleBody extends GameObject {
    private final Color color;

    public CircleBody(Vector2D position, int diameter, Color color) {
        super();
        this.getComponent(Transform.class).getPosition().set(position);

        double radius = diameter/2f;
        double mass = (4f/3f)*Math.PI*Math.pow(radius, 3f);
        Shape circle = new Ellipse2D.Double(position.x, position.y, diameter, diameter);

        this.addComponent(new Rigidbody(circle, mass));
        this.color = color;
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(this.color);
        Rigidbody body = this.getComponent(Rigidbody.class);
        //g2d.draw(body.getShape());
        g2d.fillOval((int)Math.round(body.getShape().getBounds().x),
                (int)Math.round(body.getShape().getBounds().y),
                (int)Math.round(body.getShape().getBounds().width),
                (int)Math.round(body.getShape().getBounds().height));
    }
}
