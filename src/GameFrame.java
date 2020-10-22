import PCG.Util;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameFrame extends Frame {
  Screen screen;
  Panel controlPanel;
  Choice algorithms;

  GameFrame() {
    JFrame fm = new JFrame();
    fm.setSize(1200, 800);   // setting frame size.
    GridBagLayout gbl = new GridBagLayout();
    gbl.columnWeights = new double [] {0.1, 0.9};
    gbl.rowWeights = new double [] {1.0};
    fm.setLayout(gbl);
    screen = new Screen();
    setPreferredSize(getSize());
    GridBagConstraints gbc = new GridBagConstraints();
//    gbc.gridwidth = 5;
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    fm.add(screen, gbc);

    controlPanel = new Panel();
    GridLayout gl = new GridLayout(0,2);
    gl.setHgap(10);
    gl.setHgap(5);
    controlPanel.setLayout(gl);
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
            TextField configuration = (TextField)controlPanel.getComponent(2*idx+4);
            config[idx] = Integer.parseInt(configuration.getText());
            System.out.println(Integer.parseInt(configuration.getText()));
          }
          m.setConfig(config);
          screen.generate();
          errMsg.setText(m.getError());
        }
        catch (Exception ex) {
          errMsg.setText("Error:"+ex);
          System.out.println(ex);
        }
        controlPanel.doLayout();
        fm.doLayout();
        screen.repaint();
//        screen.doLayout();
      }
    });
    Button btnExport = new Button("Export");
    btnExport.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        screen.getMapGenerator().export();
      }
    });
    btnExport.setSize(200, 60);

    controlPanel.add(btnGenerate);
    controlPanel.add(btnExport);
    controlPanel.add(errMsg);
    controlPanel.add(algorithms);
    controlPanel.doLayout();
    updateControlPanel("BSP");
//    gbc.gridwidth = 1;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.NONE;
    fm.add(controlPanel, gbc);
    fm.doLayout();
    fm.setVisible(true);     //set frame visibilty true
    addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e) {
        screen.quit = true;
        dispose();
      }
    });
  }

  private void updateControlPanel( String algorithm ) {
    String[][] configs = screen.getMapGenerator().getConfigs( algorithm );
    // remove all algorithm specific configuration fields
    for (int idx=4; idx<controlPanel.getComponentCount(); idx++) {
      controlPanel.remove(idx);
    }
    for ( String[] config: configs ) {
      Label     lable = new Label(config[0]);
      TextField field = new TextField(config[1]);
      controlPanel.add(field);
      controlPanel.add(lable);
    }
  }
}
