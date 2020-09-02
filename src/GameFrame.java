import java.awt.Frame;

public class GameFrame extends Frame {
  Screen screen;
  GameFrame() {
    Frame fm = new Frame();
    screen = new Screen();
    fm.setSize(1600, 1100);   //setting frame size.
    fm.add(screen);
    fm.setVisible(true);     //set frame visibilty true
  }
}
