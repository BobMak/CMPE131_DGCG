import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class GUI extends JFrame {
  Screen screen;
  JPanel controlPanel;
  Choice algorithms;
  List savedMapsList;
  JTextField mapName;
  Button btnExport;
  int mainComponentsNumber;

  GUI() {
//    JFrame this = new JFrame();
    this.setSize(1200, 800);   // setting frame size.
    GridBagLayout gbl = new GridBagLayout();
    gbl.columnWeights = new double [] {0.1, 0.9};
    gbl.rowWeights = new double [] {1.0};
    this.setLayout(gbl);
    screen = new Screen();
    setPreferredSize(getSize());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    this.add(screen, gbc);

    // Control JPanel includes:
    // buttons: generate, export, and import
    // a saved maps list
    // Algorithm dropdown
    // dynamic configuration parameters list
    JPanel controlPanelOut = new JPanel();
    GridLayout glOut = new GridLayout(2,1);
    glOut.setVgap(10);
    controlPanelOut.setLayout(glOut);
    controlPanel = new JPanel();
    GridLayout gl = new GridLayout(0,2);
    gl.setHgap(10);
    glOut.setVgap(10);
    controlPanel.setLayout(gl);

    savedMapsList = new List();
    savedMapsList.setFont(new Font("Arial", Font.PLAIN, 15));

    JLabel enableGridLable = new JLabel("Enable Grid");
    enableGridLable.setFont(new Font("Arial", Font.PLAIN, 15));
    enableGridLable.setToolTipText("Toggle Grid. Can make , " +
      "will using algorithm's name, configuration, and current time");
    Checkbox enableGrid = new Checkbox();
    enableGrid.addItemListener(new ItemListener(){
      @Override
      public void itemStateChanged(ItemEvent e) {
        screen.toggleGrid();
      }
    });

    JLabel mapNameLabel = new JLabel("Name");
    mapNameLabel.setFont(new Font("Arial", Font.PLAIN, 15));
    mapNameLabel.setToolTipText("Layout name. If not specified, " +
      "will using algorithm's name, configuration, and current time");
    mapName = new JTextField();
    mapName.setFont(new Font("Arial", Font.PLAIN, 15));
    mapName.setToolTipText("Layout name. If not specified, " +
      "will using algorithm's name, configuration, and current time");

    JLabel algorithmsLabel = new JLabel("Algorithm");
    algorithmsLabel.setFont(new Font("Arial", Font.PLAIN, 15));
    algorithms = new Choice();
    algorithms.addItem("BSP");
    algorithms.setFont(new Font("Arial", Font.PLAIN, 15));

    JLabel errMsg = new JLabel();
    errMsg.setText("");
    errMsg.setForeground(new Color(230, 0, 0));
    errMsg.setFont(new Font("Arial", Font.PLAIN, 15));

    Button btnGenerate = new Button("Generate");
    btnGenerate.setSize(200, 60);
    btnGenerate.setFont(new Font("Arial", Font.PLAIN, 15));
    btnGenerate.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        try {
          btnExport.setEnabled(true);
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
//        controlPanel.doLayout();
//        this.doLayout();
        screen.repaint();
//        screen.doLayout();
      }
    });
    btnExport = new Button("Export");
    btnExport.setEnabled(false);
    btnExport.setFont(new Font("Arial", Font.PLAIN, 15));
    btnExport.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        MapGenerator gen = screen.getMapGenerator();
        gen.exportFile( mapName.getText() );
        mapName.setText("");
        updateSavedList();
      }
    });
    Button btnImport = new Button("Import");
    btnImport.setFont(new Font("Arial", Font.PLAIN, 15));
    btnImport.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        String filename = savedMapsList.getSelectedItem();
        MapGenerator mg = screen.getMapGenerator();
        mg.importFile(filename);
//        resetControlPanelTo( "BSP" );
        loadConfigurations();
        screen.repaint();
      }
    });
    controlPanel.add(btnImport);
    controlPanel.add(btnExport);
    controlPanel.add(btnGenerate);
    controlPanel.add(errMsg);
    controlPanel.add(enableGrid);
    controlPanel.add(enableGridLable);
    controlPanel.add(mapName);
    controlPanel.add(mapNameLabel);
    controlPanel.add(algorithms);
    controlPanel.add(algorithmsLabel);
    mainComponentsNumber = controlPanel.getComponentCount();
    resetControlPanelTo("BSP");
    updateSavedList();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    controlPanelOut.add(savedMapsList, gbc);
    controlPanelOut.add(controlPanel, gbc);
    this.add(controlPanelOut, gbc);
    this.doLayout();
    this.setVisible(true);     //set frame visibilty true
    addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e) {
        dispose();
        System.exit(0);
      }
    });
  }

  private void resetControlPanelTo(String algorithm ) {
    String[][] configs = screen.getMapGenerator().getDefaultConfig();
    // remove all algorithm specific configuration fields
    for (int idx=mainComponentsNumber; idx<controlPanel.getComponentCount(); idx++) {
      controlPanel.remove(idx);
    }
    for ( String[] config: configs ) {
      JLabel     lable = new JLabel(config[0]);
      JTextField field = new JTextField(config[1]);
      lable.setFont(new Font("Arial", Font.PLAIN, 15));
      lable.setToolTipText(config[2]);
      field.setFont(new Font("Arial", Font.PLAIN, 15));
      field.setToolTipText(config[2]);
      controlPanel.add(field);
      controlPanel.add(lable);
    }
  }

  private void loadConfigurations() {
    int configCount = (controlPanel.getComponentCount() - mainComponentsNumber)/2;
    MapGenerator mg = screen.getMapGenerator();
    int[] configs = mg.getConfig();
    for (int idx=0; idx<configCount; idx++) {
      JTextField configuration = (JTextField)controlPanel.getComponent(2*idx+mainComponentsNumber);
      configuration.setText( String.valueOf(configs[idx]));  //+configs[idx]
      configuration.setVisible(true);
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
    int configCount = (controlPanel.getComponentCount() - mainComponentsNumber) / 2;
    int[] configs = new int[configCount];
    // Get algorithm-specific configurations
    for (int idx=0; idx<configCount; idx++) {
      JTextField configuration = (JTextField)controlPanel.getComponent(2*idx+mainComponentsNumber);
      configs[idx] = Integer.parseInt(configuration.getText());
    }
    return configs;
  }
}
