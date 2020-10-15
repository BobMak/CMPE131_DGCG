import PCG.Util;

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
    Choice algorithms = new Choice();
    algorithms.addItem("BSP");

    TextField mapWidth = new TextField();
    mapWidth.setText("75");
    TextField mapHeight = new TextField();
    mapHeight.setText("50");
    TextField roomSize = new TextField();
    roomSize.setText("10");

    Label lableWidth =    new Label("Width");
    Label lableHeight =   new Label("Height");
    Label lableRoomSize = new Label("Room");

    Label errMsg = new Label();
    errMsg.setText("");
    errMsg.setForeground(new Color(230, 0, 0));

    Button btnGenerate = new Button("Generate");
    btnGenerate.setSize(200, 60);
    btnGenerate.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        try {
          MapGenerator m = screen.getMapGenerator();
          m.setAlgorithm(algorithms.getItem(algorithms.getSelectedIndex()));
          m.setMapSize(Integer.parseInt(mapWidth.getText()),
            Integer.parseInt(mapHeight.getText()));
          m.setMinRoomSize(Integer.parseInt(roomSize.getText()));
          screen.generate();
          errMsg.setText(m.getError());
        }
        catch (Exception ex) {
          errMsg.setText("Error: bad parameters");
        }
        controller.doLayout();
        screen.repaint();
      }
    });
    Button btnExport = new Button("Export");
    btnExport.setSize(200, 60);

    controller.add(algorithms,    FlowLayout.LEFT);
    controller.add(mapWidth,      FlowLayout.LEFT);
    controller.add(lableWidth,    FlowLayout.LEFT);
    controller.add(mapHeight,     FlowLayout.LEFT);
    controller.add(lableHeight,   FlowLayout.LEFT);
    controller.add(roomSize,      FlowLayout.LEFT);
    controller.add(lableRoomSize, FlowLayout.LEFT);
    controller.add(btnGenerate,   FlowLayout.LEFT);
    controller.add(btnExport,     FlowLayout.CENTER);
    controller.add(errMsg,        FlowLayout.LEFT);
    fm.add(controller, BorderLayout.SOUTH);

    fm.setVisible(true);     //set frame visibilty true
  }
}
