/* Top level game class */

import java.awt.event.WindowEvent;

public class Game {
  public static void main(String[] args) throws InterruptedException {
    GameFrame g = new GameFrame();
    while (!g.screen.quit) {
//      g.screen.test();
//      g.screen.repaint();
      Thread.sleep(3000);
    }
  }
}
