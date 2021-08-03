package engine.game2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite {
    protected Vector2D position;
    protected BufferedImage image;
    protected Vector2D size;

    public Sprite(Vector2D position, Vector2D size) {
        this.position = position;
        this.size = size;
    }

    public Sprite(Vector2D position, Vector2D size, BufferedImage image) {
        this(position, size);
        this.image = image;
    }

    public Vector2D getPosition() { return this.position; }
    public void setPosition(Vector2D position) { this.position = position; }

    public void render(Graphics2D g2d) {
        if (image != null)
            g2d.drawImage(this.image, (int)Math.round(this.position.x), (int)Math.round(this.position.y), null);
    }

    public void update(long elapsedTime) {

    }
}
