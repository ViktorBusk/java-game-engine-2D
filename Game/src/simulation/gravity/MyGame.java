package simulation.gravity;

import engine.game2D.Engine2D;

import javax.swing.SwingUtilities;

public class MyGame extends Engine2D {

    public MyGame() {
        this.setTitle("Gravity 2D");
        //this.setFrameSize(new Dimension(800, 800));
        this.addScene("Gravity", new GravityScene(this.getFrameSize())); // Default size
        this.setScene("Gravity");
        this.startLoop();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyGame::new);
    }
}

