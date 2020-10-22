import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class GameFrame extends Frame {
  Screen screen;
  Panel controlPanel;
  Choice algorithms;
  List savedMapsList;

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

    // Control Panel includes:
    // buttons: generate, export, and import
    // a saved maps list
    // Algorithm dropdown
    // dynamic configuration parameters list
    Panel controlPanelOut = new Panel();
    GridLayout glOut = new GridLayout(2,1);
    glOut.setVgap(10);
    controlPanelOut.setLayout(glOut);
    controlPanel = new Panel();
    GridLayout gl = new GridLayout(0,2);
    gl.setHgap(10);
    glOut.setVgap(10);
    controlPanel.setLayout(gl);

    savedMapsList = new List();

    Label algorithmsLabel = new Label("Algorithm");
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
          int[] configs = getConfigs();
          m.setConfig(configs);
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
        screen.getMapGenerator().exportFile();
        updateSavedList();
      }
    });
    Button btnImport = new Button("Import");
    btnImport.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        String filename = savedMapsList.getSelectedItem();
        screen.getMapGenerator().importFile(filename);
        screen.repaint();
      }
    });
    controlPanel.add(btnImport);
    controlPanel.add(btnExport);
    controlPanel.add(btnGenerate);
    controlPanel.add(errMsg);
    controlPanel.add(algorithms);
    controlPanel.add(algorithmsLabel);
    updateControlPanel("BSP");
    updateSavedList();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    controlPanelOut.add(savedMapsList, gbc);
    controlPanelOut.add(controlPanel, gbc);
    fm.add(controlPanelOut, gbc);
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
    for (int idx=6; idx<controlPanel.getComponentCount(); idx++) {
      controlPanel.remove(idx);
    }
    for ( String[] config: configs ) {
      Label     lable = new Label(config[0]);
      TextField field = new TextField(config[1]);
      controlPanel.add(field);
      controlPanel.add(lable);
    }
  }

  private void updateSavedList() {
    savedMapsList.removeAll();
    File f = new File("data/");
    String[] allFiles = f.list();
    if ( allFiles==null ) return;
    for ( String filename : allFiles ) {
      savedMapsList.add(filename);
    }
  }

  private int[] getConfigs() {
    int configCount = (controlPanel.getComponentCount() - 6) / 2;
    int[] configs = new int[configCount];
    // Get algorithm-specific configurations
    for (int idx=0; idx<configCount; idx++) {
      TextField configuration = (TextField)controlPanel.getComponent(2*idx+6);
      configs[idx] = Integer.parseInt(configuration.getText());
    }
    return configs;
  }
}
