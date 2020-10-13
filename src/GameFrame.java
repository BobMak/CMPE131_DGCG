import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends Frame {
  Screen screen;
  GameFrame() {
    Frame fm = new Frame();
    fm.setSize(1200, 800);   //setting frame size.
    screen = new Screen();
    fm.add(screen, BorderLayout.CENTER);

    Panel controller = new Panel();
    Button btnGenerate = new Button("Generate");
    btnGenerate.setSize(200, 60);
    btnGenerate.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        screen.test();
        screen.repaint();
      }
    });
    Button btnExport = new Button("Export");
    btnExport.setSize(200, 60);
    controller.add(btnGenerate, FlowLayout.LEFT);
    controller.add(btnExport,   FlowLayout.CENTER);
    fm.add(controller, BorderLayout.SOUTH);

    fm.setVisible(true);     //set frame visibilty true
  }
}
