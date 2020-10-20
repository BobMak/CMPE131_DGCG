import PCG.Util;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

public class GameFrame extends Frame {
  Screen screen;
  Panel controlPanel;
  Choice algorithms;

  GameFrame() {
    Frame fm = new Frame();
    fm.setSize(1200, 800);   //setting frame size.
    screen = new Screen();
    fm.add(screen, BorderLayout.CENTER);

    controlPanel = new Panel();
    algorithms = new Choice();
    algorithms.addItem("BSP");

    Label errMsg = new Label();
    errMsg.setText("");
    errMsg.setForeground(new Color(230, 0, 0));

    Button btnGenerate = new Button("Generate");
    btnGenerate.setSize(200, 60);
    btnGenerate.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        try {
          MapGenerator m = screen.getMapGenerator();
          m.setConfigAlgorithm(algorithms.getItem(algorithms.getSelectedIndex()));
          int configCount = (controlPanel.getComponentCount() - 4) / 2;
          int[] config = new int[configCount];
          // Get algorithm-specific configurations
          for (int idx=0; idx<configCount; idx++) {
            TextField configuration = (TextField)controlPanel.getComponent(1 + 2*idx);
            config[idx] = Integer.parseInt(configuration.getText());
          }
          m.setConfig(config);
          screen.generate();
          errMsg.setText(m.getError());
        }
        catch (Exception ex) {
          errMsg.setText("Error:"+ex);
        }
        controlPanel.doLayout();
        screen.repaint();
      }
    });
    Button btnExport = new Button("Export");
    btnExport.setSize(200, 60);

    controlPanel.add(btnGenerate,   FlowLayout.LEFT);
    controlPanel.add(btnExport,     FlowLayout.LEFT);
    controlPanel.add(errMsg,        FlowLayout.LEFT);
    controlPanel.add(algorithms,    FlowLayout.LEFT);
    updateControlPanel("BSP");
    fm.add(controlPanel, BorderLayout.SOUTH);

    fm.setVisible(true);     //set frame visibilty true
  }

  private void updateControlPanel( String algorithm ) {
    String[][] configs = screen.getMapGenerator().getConfigs( algorithm );
    // remove all algorithm specific configuration fields
    for (int idx=0; idx<controlPanel.getComponentCount()-4; idx++) {
      controlPanel.remove(idx);
    }
    for ( String[] config: configs ) {
      Label     lable = new Label(config[0]);
      TextField field = new TextField(config[1]);
      controlPanel.add(field, FlowLayout.LEFT);
      controlPanel.add(lable, FlowLayout.LEFT);
    }
  }
}
