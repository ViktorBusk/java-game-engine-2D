package simulation.gravity;
import engine.game2D.GameObject;
import engine.game2D.Vector2D;
import engine.game2D.component.Rigidbody;
import engine.game2D.component.Transform;

import java.awt.*;

public class CircleBody extends GameObject {
    private Color color;

    public CircleBody(Vector2D position, int diameter, Color color) {
        super();
        this.getComponent(Transform.class).getPosition().set(position);
        this.addComponent(new Rigidbody(diameter)); //FIXME: Need to refactor this later!
        this.color = color;
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(this.color);
        Vector2D position = this.getComponent(Transform.class).getPosition();
        Rigidbody body = this.getComponent(Rigidbody.class);
        g2d.fillOval((int)Math.round(position.x - body.getRadius()), (int)Math.round(position.y - body.getRadius()), body.getDiameter(), body.getDiameter());
    }
}
