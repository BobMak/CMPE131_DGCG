/* Top level game class */

import java.awt.event.WindowEvent;

public class Game {
  public static void main(String[] args) {
    GameFrame g = new GameFrame();
    while (!g.screen.quit) {
      System.out.println('1');
    }
    g.dispatchEvent(new WindowEvent(g, WindowEvent.WINDOW_CLOSING));
    g.setVisible(false);
  }
}
