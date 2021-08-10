package engine.game2D.component;

import engine.game2D.Vector2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sprite extends GameComponent {
    private BufferedImage image;
    private Vector2D size;

    public Sprite(String filepath) {
        try {
            this.image = ImageIO.read(new File(filepath));
            this.size = new Vector2D(this.image.getWidth(), this.image.getHeight());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void rotateAndScale(Transform transform) {
        int w = (int)Math.round(this.size.x * transform.getScale().x);
        int h = (int)Math.round(this.size.y * transform.getScale().y);
        BufferedImage rotated = new BufferedImage(w, h, image.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(Math.toRadians(transform.getRotation().x), w/2f, h/2f);
        graphic.drawImage(this.image, null, 0, 0);
        graphic.dispose();
        this.image = rotated;
    }

    @Override
    public void render(Graphics2D g2d) {
        super.render(g2d);
        Vector2D position = this.getParent().getComponent(Transform.class).getPosition();
        g2d.drawImage(this.image, (int)Math.round(position.x), (int)Math.round(position.y), null);
    }

    @Override
    public void update(long elapsedTime) {
        super.update(elapsedTime);
        Transform transform = this.getParent().getComponent(Transform.class);
        this.rotateAndScale(transform);
    }
}
