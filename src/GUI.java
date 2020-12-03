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
  GridBagConstraints generalConstrains;
  GridBagConstraints componentConstrains;
  int mainComponentsNumber;

  MapGenerator mapGen;

  GUI() {
//    JFrame this = new JFrame();
    this.setSize(1200, 800);   // setting frame size.
    GridBagLayout gbl = new GridBagLayout();
    gbl.columnWeights = new double [] {0.1, 0.9};
    gbl.rowWeights = new double [] {1.0};
    this.setLayout(gbl);
    screen = new Screen();
    setPreferredSize(getSize());
    generalConstrains   = new GridBagConstraints();
    componentConstrains = new GridBagConstraints();
    generalConstrains.gridx = 1;
    generalConstrains.gridy = 0;
    generalConstrains.fill = GridBagConstraints.BOTH;
//    gbc.gridwidth = 6;
    generalConstrains.weightx = 0.7;
//    generalConstrains.gridheight = 2;
    this.add(screen, generalConstrains);
//    generalConstrains.fill = GridBagConstraints.HORIZONTAL;

    mapGen = new MapGenerator();
    // Control JPanel includes:
    // buttons: generate, export, and import
    // a saved maps list
    // Algorithm dropdown
    // dynamic configuration parameters list
    JPanel controlPanelOut = new JPanel();
    GridLayout glOut = new GridLayout(2,1);  // 2,1
//    glOut.setVgap(10);
    controlPanelOut.setLayout(glOut);
    controlPanel = new JPanel();
    GridBagLayout gl = new GridBagLayout();  //0,2
//    gl.setHgap(10);
//    glOut.setVgap(10);
    controlPanel.setLayout(gl);

    savedMapsList = new List();
    savedMapsList.setFont(new Font("Arial", Font.PLAIN, 15));

    JLabel enableASCIIlable = new JLabel("ASCII mode");
    enableASCIIlable.setFont(new Font("Arial", Font.PLAIN, 15));
    enableASCIIlable.setToolTipText("Toggle ASCII mode");
    Checkbox enableASCII = new Checkbox();
    enableASCII.addItemListener(new ItemListener(){
      @Override
      public void itemStateChanged(ItemEvent e) {
        screen.toggleASCII();
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
    for ( String algorithmID : mapGen.getAlgorithms() ) {
      algorithms.addItem(algorithmID);
    }
    algorithms.setFont(new Font("Arial", Font.PLAIN, 15));
    algorithms.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        resetControlPanelTo(e.getItem().toString());
      }
    });
    algorithms.select("BSP");

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
          String[] configs = getConfigs();
          mapGen.setConfig(configs);
          screen.setMap( mapGen.generate() );
          errMsg.setText(mapGen.getError());
        }
        catch (Exception ex) {
          errMsg.setText("Error:"+ex);
          System.out.println(ex);
        }
        screen.repaint();
      }
    });
    btnExport = new Button("Export");
    btnExport.setEnabled(false);
    btnExport.setFont(new Font("Arial", Font.PLAIN, 15));
    btnExport.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        mapGen.exportFile( mapName.getText() );
        mapName.setText("");
        updateSavedList();
      }
    });
    Button btnImport = new Button("Import");
    btnImport.setFont(new Font("Arial", Font.PLAIN, 15));
    btnImport.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        String filename = savedMapsList.getSelectedItem();
        mapGen.importFile(filename);
        resetControlPanelTo( mapGen.getConfigAlgorithm() );
        algorithms.select( mapGen.getConfigAlgorithm() );

        loadConfigurations();
        screen.setMap( mapGen.getMap() );
        screen.repaint();
      }
    });
    componentConstrains.gridx = 0;
    componentConstrains.gridy = 0;
    componentConstrains.fill = GridBagConstraints.BOTH;
    componentConstrains.gridheight = 1;
    componentConstrains.gridwidth = 1;
    controlPanel.add(btnImport, componentConstrains );
    componentConstrains .gridx = 1;
    componentConstrains .gridy = 0;
    controlPanel.add(btnExport, componentConstrains );
    componentConstrains.gridx = 0;
    componentConstrains.gridy = 1;
    controlPanel.add(btnGenerate, componentConstrains );
    componentConstrains.gridx = 1;
    componentConstrains.gridy = 1;
    controlPanel.add(errMsg, componentConstrains );
    componentConstrains.gridx = 0;
    componentConstrains.gridy = 2;
    controlPanel.add(enableASCII, componentConstrains );
    componentConstrains.gridx = 1;
    componentConstrains.gridy = 2;
    controlPanel.add(enableASCIIlable, componentConstrains );
    componentConstrains.gridx = 0;
    componentConstrains.gridy = 3;
    controlPanel.add(mapName, componentConstrains );
    componentConstrains.gridx = 1;
    componentConstrains.gridy = 3;
    controlPanel.add(mapNameLabel, componentConstrains );
    componentConstrains.gridx = 0;
    componentConstrains.gridy = 4;
    controlPanel.add(algorithms, componentConstrains );
    componentConstrains.gridx = 1;
    componentConstrains.gridy = 4;
    controlPanel.add(algorithmsLabel, componentConstrains );
    componentConstrains .gridx = 1;
    componentConstrains .gridy = 5;
    mainComponentsNumber = controlPanel.getComponentCount();
    resetControlPanelTo("BSP");
    updateSavedList();
    componentConstrains .gridx = 0;
    componentConstrains .gridy = 0;
    componentConstrains .gridwidth = 2;
    componentConstrains.weighty = 0.3;
    componentConstrains.fill = GridBagConstraints.HORIZONTAL;
    controlPanelOut.add(savedMapsList, componentConstrains );
    componentConstrains.fill = GridBagConstraints.BOTH;
    componentConstrains .gridx = 0;
    componentConstrains .gridy = 1;
    componentConstrains .gridwidth = 2;
    componentConstrains.weighty = 0.7;
    controlPanelOut.add(controlPanel, componentConstrains );
    componentConstrains.weighty = 0;
    generalConstrains.weightx = 0.2;
    generalConstrains.gridx = 0;
    generalConstrains.gridy = 0;
    this.add(controlPanelOut, generalConstrains);
    this.doLayout();
    this.setVisible(true); // set frame visibilty true
    addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e) {
        dispose();
        System.exit(0);
      }
    });
  }

  private void resetControlPanelTo(String algorithm ) {
    String[][] configs = mapGen.switchTo( algorithm );
    // remove all algorithm specific configuration fields
    if ( controlPanel.getComponentCount() >= mainComponentsNumber ) {
      int toRemove = controlPanel.getComponentCount() - mainComponentsNumber;
      for (int idx=0; idx<toRemove; idx++) {
        controlPanel.remove(mainComponentsNumber);
      }
      controlPanel.doLayout();
      this.doLayout();
    }
    int rowIdx = mainComponentsNumber / 2;
    for ( String[] config: configs ) {
      JLabel     lable = new JLabel(config[0]);
      JTextField field = new JTextField(config[1]);
      lable.setFont(new Font("Arial", Font.PLAIN, 15));
      lable.setToolTipText(config[2]);
      field.setFont(new Font("Arial", Font.PLAIN, 15));
      field.setToolTipText(config[2]);
      generalConstrains.gridx = 0;
      generalConstrains.gridy = rowIdx;
      generalConstrains.gridheight = 1;
      generalConstrains.gridwidth = 1;
      controlPanel.add(field, generalConstrains);
      generalConstrains.gridx = 1;
      generalConstrains.gridy = rowIdx;
//      gbc.gridwidth = 2;
      controlPanel.add(lable, generalConstrains);
      rowIdx++;
    }
    this.doLayout();
    this.setVisible(true); // set frame visibilty true
  }

  private void loadConfigurations() {
    int configCount = (controlPanel.getComponentCount() - mainComponentsNumber)/2;
    String[] configs = mapGen.getConfig();
    for (int idx=0; idx<configCount; idx++) {
      JTextField configuration = (JTextField)controlPanel.getComponent(2*idx+mainComponentsNumber);
      configuration.setText( configs[idx]);  //+configs[idx]
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

  private String[] getConfigs() {
    int configCount = (controlPanel.getComponentCount() - mainComponentsNumber) / 2;
    String[] configs = new String[configCount];
    // Get algorithm-specific configurations
    for (int idx=0; idx<configCount; idx++) {
      JTextField configuration = (JTextField)controlPanel.getComponent(2*idx+mainComponentsNumber);
      configs[idx] = configuration.getText();
    }
    return configs;
  }
}
