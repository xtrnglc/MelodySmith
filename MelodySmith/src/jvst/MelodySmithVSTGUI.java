/*
 * References:
 * http://jvstwrapper.sourceforge.net/
 */

package jvst;

/**
 *
 * @author Team AudioMIDIum, University of Utah Senior Project 2017-2018
 */


import jvst.wrapper.*;
import static jvst.wrapper.VSTPluginGUIAdapter.RUNNING_MAC_X;
import jvst.wrapper.gui.VSTPluginGUIRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.Document;

import org.apache.commons.io.FileUtils;


public class MelodySmithVSTGUI extends VSTPluginGUIAdapter implements ChangeListener {

  private static final long serialVersionUID = -8641024370578430211L;

  JSlider DelaySlider;
  JSlider FeedbackSlider;
  JSlider VolumeSlider;
  JTextField DelayText;
  JTextField FeedbackText;
  JTextField VolumeText;

  private VSTPluginAdapter pPlugin;
  protected static boolean DEBUG = false;
  
  protected static String currDirectoryPathName;
  final int width = 1920;
  final int height = 1080;
  
  protected File[] currentlyUploadedFiles = null;
  public JTextField[] artistNameFields = null;
  public ArtistGrouping[] artistGroupings = null;
  
  String newDirectoryName;
  File newDirectory;
  
  JPanel secondCol;
  boolean isCMajor = true;

  public MelodySmithVSTGUI(VSTPluginGUIRunner r, VSTPluginAdapter plug) throws Exception {
	super(r,plug);
    log("MelodySmithVSTGUI <init>");
    
    //make sure we use the defaul ui!
    //if there is another plugin loaded using a different Look and feel, we would 
    //use that one because the LaF is a static property and we are running in the 
    //same VM. 
    
//    //So, I highly recommend setting a LaF in each of your plugins GUI constructors!!!
//    
//    UIManager.put("ClassLoader", null); //use the default classloader to load the system LaF
//    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//    SwingUtilities.updateComponentTreeUI(this);
    
    this.setTitle("MelodySmith v1.0");
    this.setSize(width, height);
    this.setResizable(false);
    
    this.pPlugin = plug;
    
    this.currDirectoryPathName = null;
    
    //Get paths and set up directory to write midi files to
    Path currentRelativePath = Paths.get("");
    String currentAbsolutePath = currentRelativePath.toAbsolutePath().toString();
    log("MelodySmithVSTGUI curr rel path: " + currentAbsolutePath);
    System.out.println("MelodySmithVSTGUI curr rel path: " + currentAbsolutePath);
    this.newDirectoryName = currentAbsolutePath + "//uploaded_training_sets//curr";
    this.newDirectory = new File(newDirectoryName);     
    
    
    this.init();
    
    //this is needed on the mac only, 
    //java guis are handled there in a pretty different way than on win/linux
    //XXX
    if (RUNNING_MAC_X) this.show();
  }
  
  private void addEmptyLabels(JPanel curr, int num) {
      for(int i= 0; i < num; i++) {
          curr.add(new JLabel());
      }
  } 


  public void init() {    
      //((MelodySmithVST)plugin).gui=this;
//    if (!DEBUG) {
//    	((MelodySmithVST)plugin).gui=this; //tell the plug that it has a gui!
//    	
//    	this.VolumeSlider = new JSlider(JSlider.VERTICAL, 1, 100, (int)(this.pPlugin.getParameter(MelodySmithVST.PARAM_ID_OUT) * 100F));
//    	this.FeedbackSlider = new JSlider(JSlider.VERTICAL, 1, 100, (int)(this.pPlugin.getParameter(MelodySmithVST.PARAM_ID_FEEDBACK) * 100F));
//    	this.DelaySlider = new JSlider(JSlider.VERTICAL, 1, 100, (int)(this.pPlugin.getParameter(MelodySmithVST.PARAM_ID_DELAY) * 100F));
//    }
//    else {
//       	this.VolumeSlider = new JSlider(JSlider.VERTICAL, 1, 100, 1);
//    	this.FeedbackSlider = new JSlider(JSlider.VERTICAL, 1, 100, 1);
//    	this.DelaySlider = new JSlider(JSlider.VERTICAL, 1, 100, 1);
//    }
//    this.VolumeSlider.addChangeListener(this);
//    this.FeedbackSlider.addChangeListener(this);
//    this.DelaySlider.addChangeListener(this);
//
//    if (!DEBUG) {
//	    this.VolumeText = new JTextField(this.pPlugin.getParameterDisplay(MelodySmithVST.PARAM_ID_OUT));
//	    this.FeedbackText = new JTextField(this.pPlugin.getParameterDisplay(MelodySmithVST.PARAM_ID_FEEDBACK));
//	    this.DelayText = new JTextField(this.pPlugin.getParameterDisplay(MelodySmithVST.PARAM_ID_DELAY));
//            
//            Dimension d = this.VolumeText.getPreferredSize();
//            d.height = 100;
//            this.VolumeText.setPreferredSize(d);
//	}
//    else {
//	    this.VolumeText = new JTextField("0");
//	    this.FeedbackText = new JTextField("0");
//	    this.DelayText = new JTextField("0");    	
//    }
//    
//    JLabel DelayLabel = new JLabel("Delay");
//    JLabel FeedbackLabel = new JLabel("Feedback");
//    JLabel VolumeLabel = new JLabel("Volume");

//    ImageIcon backgroundImage = null; 
//    try {
//        BufferedImage img = ImageIO.read(new File(currentAbsolutePath + "\\bg.jpg"));
//        ImageIcon icon = new ImageIcon(img);
//        backgroundImage = icon;
//    } catch(Exception e) {
//        System.out.println(e.toString());
//    }
    
        

    JLabel contentPane = new JLabel();
    contentPane.setBackground(Color.BLACK);
    contentPane.setOpaque(true);
    // contentPane.setIcon( backgroundImage );
    contentPane.setLayout( new BorderLayout() );
    this.setContentPane( contentPane );
    this.getContentPane().setBackground(Color.darkGray);
    
    
    GridLayout grids = new GridLayout(1, 2);
    this.getContentPane().setLayout(grids);

    
/////////////////////// Design For Prototype ////////////////////////////////////
//    JPanel firstCol = new JPanel();
//    firstCol.setBackground(new Color(0,0,0,64));
//    firstCol.setLayout(new GridLayout(0,3));
//    
//    addEmptyLabels(firstCol, 10);
//    JButton uploadMIDIFiles = new JButton("Upload Files");
//    uploadMIDIFiles.setBackground(Color.BLACK);
//    uploadMIDIFiles.setForeground(Color.WHITE);
//    uploadMIDIFiles.setFont(uploadMIDIFiles.getFont().deriveFont(24.0f));
//    
//    uploadMIDIFiles.addActionListener(new ActionListener() {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            final JFileChooser fc = new JFileChooser();
//            fc.setMultiSelectionEnabled(true);
//            int returnVal = fc.showOpenDialog(firstCol);
//
//            if (returnVal == JFileChooser.APPROVE_OPTION) {
//                File[] files = fc.getSelectedFiles();
//                //This is where a real application would open the file.
//                
//                
//                String newDirectoryName = currentAbsolutePath + "//uploaded_training_sets//1";
//                boolean success = (new File(newDirectoryName)).mkdirs();
//                if (!success) {
//                    System.out.println("Unabled to create directory to store training sets");
//                }
//                
//                currDirectoryPathName = newDirectoryName;
//                
//                for(int i = 0; i < files.length; i++) {
//                    File f = files[i];
//                    System.out.println("Opening: " + f.getName());
//                    String newFileName = newDirectoryName + "//" + i + ".MID";
//                    try {
//                        FileUtils.copyFile(f, new File(newFileName));
//                    } catch(IOException ex) {
//                        ex.printStackTrace();
//                    }
//                    
//                    System.out.println("Wrote MIDI files to " + currDirectoryPathName);
//                }
//            } else {
//                System.out.println("Nothing selected");
//            }
//        }
//    });    
//
//    firstCol.add(uploadMIDIFiles);
//    addEmptyLabels(firstCol, 10);
//    
//    this.getContentPane().add(firstCol);
//    
//    JPanel secondCol = new JPanel();
//    secondCol.setBackground(new Color(0,0,0,64));
//    secondCol.setLayout(new GridLayout(0,3));  
//
//    addEmptyLabels(secondCol, 4);
//    JLabel currTrainingSetLabel = new JLabel("Training Set");
//    currTrainingSetLabel.setFont(currTrainingSetLabel.getFont().deriveFont(24.0f));
//    currTrainingSetLabel.setForeground(Color.WHITE);
//    secondCol.add(currTrainingSetLabel);
//    addEmptyLabels(secondCol,4);
//
//    
//    this.getContentPane().add(secondCol);
//    this.getContentPane().add(new JLabel());
//    }
    
/////////////////////// Design for Final Project ////////////////////////////////
    
    //First column
    JPanel firstCol = new JPanel();
    firstCol.setBackground(Color.darkGray);
    
    JPanel trainingsetPanelVerticalMargin = new JPanel();
    trainingsetPanelVerticalMargin.setBackground(Color.darkGray) ;
    trainingsetPanelVerticalMargin.setPreferredSize(new Dimension(this.width / 2, this.height / 16));
    firstCol.add(trainingsetPanelVerticalMargin);
    
    //Upload/Modify training set panel
    JPanel trainingsetPanel = new JPanel();
    int trainingSetPanelWidth = (int) ((this.width/ 2) / 1.25);
    int trainingSetPanelHeight = (int) (this.height / 1.25);
    AbstractBorder roundedOrangeBorder = new TextBubbleBorder(Color.ORANGE,2,16,0, false);
    trainingsetPanel.setBorder(roundedOrangeBorder);
    trainingsetPanel.setBackground(Color.BLACK);
    trainingsetPanel.setPreferredSize(new Dimension(trainingSetPanelWidth,trainingSetPanelHeight));
    
    this.showUploadTrainingSet(trainingsetPanel);
 
    firstCol.add(trainingsetPanel);
    
    //Second column
    this.secondCol = new JPanel();
    secondCol.setBackground(Color.darkGray);
    
    secondCol.setLayout(new GridLayout(2,1));
    
    //Add New training set button
    JPanel secondColNewTrainingSetPanel = new JPanel();
    secondColNewTrainingSetPanel.setBackground(Color.darkGray);
    secondColNewTrainingSetPanel.setLayout(new GridLayout(5,3));
    secondColNewTrainingSetPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.ORANGE));
    
    JButton newTrainingSetButton = new JButton("New Training Set");
    newTrainingSetButton.setFont(newTrainingSetButton.getFont().deriveFont(32.0f));
    newTrainingSetButton.setBackground(Color.BLACK);
    newTrainingSetButton.setForeground(Color.WHITE);    
    newTrainingSetButton.setBorder(new TextBubbleBorder(Color.CYAN,4,16,0, false));
    
    newTrainingSetButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final JFileChooser fc = new JFileChooser();
            fc.setMultiSelectionEnabled(true);
            int returnVal = fc.showOpenDialog(trainingsetPanel);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                currentlyUploadedFiles = fc.getSelectedFiles();
                
                trainingsetPanel.removeAll();
                showTrainingSetDataEditing(trainingsetPanel);
                trainingsetPanel.revalidate();
                trainingsetPanel.repaint();

            } else {
                System.out.println("Nothing selected");
            }
        }
    });  
       
    addEmptyLabels(secondColNewTrainingSetPanel,7);
    secondColNewTrainingSetPanel.add(newTrainingSetButton);
    addEmptyLabels(secondColNewTrainingSetPanel,7);
   
   
    //Panel for key chooser and melody creation
    JPanel keyAndForgePanel = new JPanel();
    keyAndForgePanel.setBackground(Color.darkGray);
    keyAndForgePanel.setLayout(new GridLayout(1,2));
    
    //Key Chooser
    JPanel keyPanel = new JPanel();
    keyPanel.setBackground(Color.darkGray);
    keyPanel.setLayout(new GridLayout(5,4));
    
    JButton cMajorButton = new JButton("C Major");
    cMajorButton.setBackground(Color.lightGray);
    cMajorButton.setForeground(Color.WHITE);
    JButton aMinorButton = new JButton("A Minor");
    aMinorButton.setBackground(Color.BLACK);
    aMinorButton.setForeground(Color.WHITE);
    

    cMajorButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!isCMajor) {
                isCMajor = true;
                final Timer timer1 = new Timer(2, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        final Color targetColor = Color.lightGray;
                        final int changingSpeed = 5;

                        final Color currentColor = cMajorButton.getBackground();

                        // step 1
                        int r = currentColor.getRed();
                        int g = currentColor.getGreen();
                        int b = currentColor.getBlue();

                        // step 2
                        double dr = targetColor.getRed() - r;
                        double dg = targetColor.getGreen() - g;
                        double db = targetColor.getBlue() - b;

                        // step 3
                        double norm = Math.sqrt(dr*dr+dg*dg+db*db);
                        if (norm < .001) {
                            ((Timer)(evt.getSource())).stop();
                            return;
                        }
                        dr /= norm;
                        dg /= norm;
                        db /= norm;

                        // step 4
                        dr *= Math.min(changingSpeed, norm);
                        dg *= Math.min(changingSpeed, norm);
                        db *= Math.min(changingSpeed, norm);

                        // step 5
                        r += dr;
                        g += dg;
                        b += db;


                        cMajorButton.setBackground(new Color(r,g,b));

                        keyPanel.repaint();
                    }
                });
                final Timer timer2 = new Timer(2, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        final Color targetColor = Color.BLACK;
                        final int changingSpeed = 5;

                        final Color currentColor = aMinorButton.getBackground();

                        // step 1
                        int r = currentColor.getRed();
                        int g = currentColor.getGreen();
                        int b = currentColor.getBlue();

                        // step 2
                        double dr = targetColor.getRed() - r;
                        double dg = targetColor.getGreen() - g;
                        double db = targetColor.getBlue() - b;

                        // step 3
                        double norm = Math.sqrt(dr*dr+dg*dg+db*db);
                        if (norm < .001) {
                            ((Timer)(evt.getSource())).stop();
                            return;
                        }
                        dr /= norm;
                        dg /= norm;
                        db /= norm;

                        // step 4
                        dr *= Math.min(changingSpeed, norm);
                        dg *= Math.min(changingSpeed, norm);
                        db *= Math.min(changingSpeed, norm);

                        // step 5
                        r += dr;
                        g += dg;
                        b += db;


                        aMinorButton.setBackground(new Color(r,g,b));

                        keyPanel.repaint();
                    }
                });
                timer1.start();
                timer2.start();
            }
        }
    });
    
    aMinorButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(isCMajor) {
                isCMajor = false;
                final Timer timer1 = new Timer(2, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        final Color targetColor = Color.lightGray;
                        final int changingSpeed = 5;

                        final Color currentColor = aMinorButton.getBackground();

                        // step 1
                        int r = currentColor.getRed();
                        int g = currentColor.getGreen();
                        int b = currentColor.getBlue();

                        // step 2
                        double dr = targetColor.getRed() - r;
                        double dg = targetColor.getGreen() - g;
                        double db = targetColor.getBlue() - b;

                        // step 3
                        double norm = Math.sqrt(dr*dr+dg*dg+db*db);
                        if (norm < .001) {
                            ((Timer)(evt.getSource())).stop();
                            return;
                        }
                        dr /= norm;
                        dg /= norm;
                        db /= norm;

                        // step 4
                        dr *= Math.min(changingSpeed, norm);
                        dg *= Math.min(changingSpeed, norm);
                        db *= Math.min(changingSpeed, norm);

                        // step 5
                        r += dr;
                        g += dg;
                        b += db;


                        aMinorButton.setBackground(new Color(r,g,b));

                        keyPanel.repaint();
                    }
                });
                final Timer timer2 = new Timer(2, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        final Color targetColor = Color.BLACK;
                        final int changingSpeed = 5;

                        final Color currentColor = cMajorButton.getBackground();

                        // step 1
                        int r = currentColor.getRed();
                        int g = currentColor.getGreen();
                        int b = currentColor.getBlue();

                        // step 2
                        double dr = targetColor.getRed() - r;
                        double dg = targetColor.getGreen() - g;
                        double db = targetColor.getBlue() - b;

                        // step 3
                        double norm = Math.sqrt(dr*dr+dg*dg+db*db);
                        if (norm < .001) {
                            ((Timer)(evt.getSource())).stop();
                            return;
                        }
                        dr /= norm;
                        dg /= norm;
                        db /= norm;

                        // step 4
                        dr *= Math.min(changingSpeed, norm);
                        dg *= Math.min(changingSpeed, norm);
                        db *= Math.min(changingSpeed, norm);

                        // step 5
                        r += dr;
                        g += dg;
                        b += db;


                        cMajorButton.setBackground(new Color(r,g,b));

                        keyPanel.repaint();
                    }
                });
                timer1.start();
                timer2.start();
            }
        }
    });
    
    addEmptyLabels(keyPanel, 9);
    keyPanel.add(cMajorButton);
    keyPanel.add(aMinorButton);
    addEmptyLabels(keyPanel, 9);
    
    //Forge Panel - TODO
    JPanel forgePanel = new JPanel();
    forgePanel.setBackground(Color.darkGray);
    forgePanel.setLayout(new GridLayout(5,3));
    
    JButton forgeButton = new JButton("Forge");
    forgeButton.setBackground(Color.ORANGE);
    forgeButton.setForeground(Color.WHITE);
    forgeButton.setBorder(new TextBubbleBorder(Color.CYAN,2,16,0, false));
    
    forgeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //Call Alex's composition method
            ArtistGrouping[] currArtistGroupings = artistGroupings;
            boolean currKey = isCMajor;
        }
    });
    
    addEmptyLabels(forgePanel, 7);
    forgePanel.add(forgeButton);
    addEmptyLabels(forgePanel, 7);    
    
    
    keyAndForgePanel.add(keyPanel);
    keyAndForgePanel.add(forgePanel);
    
    
    secondCol.add(secondColNewTrainingSetPanel);
    secondCol.add(keyAndForgePanel);
    
    
    
    this.getContentPane().add(firstCol);
    this.getContentPane().add(secondCol);
    
    //this.getContentPane().add(firstCol);
    // firstCol.setLayout(new GridLayout(0,3));
    
//    
//    JPanel tabsPanel = new JPanel();
//    tabsPanel.setLayout(new GridLayout(1,3));
//    
//    JButton anvilBtn = new JButton("Anvil");
//    anvilBtn.setBackground(Color.BLACK);
//    anvilBtn.setForeground(Color.WHITE);
//    JButton forgeBtn = new JButton("Forge");
//    forgeBtn.setBackground(Color.BLACK);
//    forgeBtn.setForeground(Color.WHITE);
//    JButton guildBtn = new JButton("Guild");
//    guildBtn.setBackground(Color.BLACK);
//    guildBtn.setForeground(Color.WHITE);    
//    
//    firstCol.add(anvilBtn);
//    firstCol.add(forgeBtn);
//    firstCol.add(guildBtn);
//
//    for(int i = 0; i < 3 * 1; i++) {
//        firstCol.add(new JLabel());
//    }
//
//    //JPanel corpusInfo = new JPanel();
//    //corpusInfo.setLayout(new GridLayout(2,1));
//    //corpusInfo.setBackground(new Color(0,0,0,64));
//    JLabel currentCorpusLabel = new JLabel("   Classical");
//    currentCorpusLabel.setFont(currentCorpusLabel.getFont().deriveFont(40.0f));
//    currentCorpusLabel.setForeground(Color.WHITE);
//    //corpusInfo.add(currentCorpusLabel);
//    firstCol.add(currentCorpusLabel);
//    
//    firstCol.add(new JLabel());
//    firstCol.add(new JLabel());
//    JButton viewCorpusBtn = new JButton("View Corpus");
//    viewCorpusBtn.setBackground(Color.RED);
//    viewCorpusBtn.setForeground(Color.WHITE);
//    //corpusInfo.add(viewCorpusBtn);
//    JPanel viewCorpusPanel = new JPanel();
//    viewCorpusPanel.setLayout(new GridLayout(2,2));
//    viewCorpusPanel.setBackground(new Color(0,0,0,64));    
//    viewCorpusPanel.add(new JLabel());
//    viewCorpusPanel.add(viewCorpusBtn);
//    viewCorpusPanel.add(new JLabel());
//    viewCorpusPanel.add(new JLabel());
//    firstCol.add(viewCorpusPanel);
//    firstCol.add(new JLabel());
//    firstCol.add(new JLabel());
//    
//    
//    firstCol.add(new JLabel());
//    JLabel influenceLabel = new JLabel("Influence");
//    influenceLabel.setFont(influenceLabel.getFont().deriveFont(32.0f));
//    influenceLabel.setForeground(Color.WHITE);
//    firstCol.add(influenceLabel);
//    firstCol.add(new JLabel());
//    
//    String[] artistNames = { "Bach", "Mozart", "Brahms", "Beethoven", "Liszt" };
//    for(int i = 0; i < artistNames.length; i++) {
//        JLabel artistLabel = new JLabel("   " + artistNames[i]);
//        artistLabel.setFont(currentCorpusLabel.getFont().deriveFont(24.0f));
//        currentCorpusLabel.setFont(currentCorpusLabel.getFont().deriveFont(40.0f));
//        artistLabel.setForeground(Color.WHITE);
//        firstCol.add(artistLabel);
//        JSlider influenceSlider = new JSlider();
//        influenceSlider.setBackground(new Color(0,0,0,64));
//        firstCol.add(influenceSlider);
//        
//        JPanel influenceTextFieldPanel = new JPanel();
//        influenceTextFieldPanel.setLayout(new GridLayout(3,3));
//        influenceTextFieldPanel.setBackground(new Color(0,0,0,64));
//        addEmptyLabels(influenceTextFieldPanel,4);
//        JTextField influenceTextField = new JTextField("0");
//        influenceTextFieldPanel.add(influenceTextField);
//        addEmptyLabels(influenceTextFieldPanel,4);
//        firstCol.add(influenceTextFieldPanel);
//    }
//    for(int i = 0; i < 3; i++) {
//        firstCol.add(new JLabel());
//    }
//    
//    String[] list_of_songs = {"song1", "song2", "song3", "song4", "song5"};
//    
//    
//    
//    this.getContentPane().add(firstCol);
//    
////    ////////////////////////////////////////////////////////////////////////
//    JPanel secondCol = new JPanel();
//    secondCol.setBackground(new Color(0,0,0,64));
//    secondCol.setLayout(new GridLayout(0,3));  
//    addEmptyLabels(secondCol, 10);
//    
//    JButton reforgeBtn = new JButton("REFORGE");
//    reforgeBtn.setFont(reforgeBtn.getFont().deriveFont(32.0f));
//    reforgeBtn.setBackground(Color.BLACK);
//    reforgeBtn.setForeground(Color.WHITE);     
//    secondCol.add(reforgeBtn);
//    
//    addEmptyLabels(secondCol, 2);
//    JLabel measuresLabel = new JLabel("Measures");
//    measuresLabel.setFont(measuresLabel.getFont().deriveFont(32.0f));
//    measuresLabel.setForeground(Color.WHITE);
//    secondCol.add(measuresLabel);
//    
//    addEmptyLabels(secondCol, 2);
//    JPanel measuresTextFieldPanel = new JPanel();
//    measuresTextFieldPanel.setLayout(new GridLayout(2,1));
//    measuresTextFieldPanel.setBackground(new Color(0,0,0,64));
//    JTextField measuresTextField = new JTextField("1");
//    measuresTextFieldPanel.add(measuresTextField);
//    addEmptyLabels(measuresTextFieldPanel,1);
//    secondCol.add(measuresTextFieldPanel);  
//    
//    addEmptyLabels(secondCol, 2);
//    JLabel keyLabel = new JLabel("Key");
//    keyLabel.setFont(keyLabel.getFont().deriveFont(32.0f));
//    keyLabel.setForeground(Color.WHITE);
//    secondCol.add(keyLabel);
//    
//    addEmptyLabels(secondCol, 2);
//    JPanel keyTextFieldPanel = new JPanel();
//    keyTextFieldPanel.setLayout(new GridLayout(2,1));
//    keyTextFieldPanel.setBackground(new Color(0,0,0,64));
//    JTextField keyTextField = new JTextField("A");
//    keyTextFieldPanel.add(keyTextField);
//    addEmptyLabels(keyTextFieldPanel,1);
//    secondCol.add(keyTextFieldPanel);
//    
//    addEmptyLabels(secondCol, 2);
//    JLabel chaosLabel = new JLabel("Chaos");
//    chaosLabel.setFont(chaosLabel.getFont().deriveFont(32.0f));
//    chaosLabel.setForeground(Color.WHITE);
//    secondCol.add(chaosLabel);
    
    /////////Chaos////////////////
//    addEmptyLabels(secondCol, 2);
//    JPanel chaosPanel = new JPanel();
//    chaosPanel.setLayout(new GridLayout(1,2));
//    chaosPanel.setBackground(new Color(0,0,0,64));
//
//    JPanel chaosTextFieldPanel = new JPanel();
//    chaosTextFieldPanel.setLayout(new GridLayout(5,1));
//    chaosTextFieldPanel.setBackground(new Color(0,0,0,64));
//    JTextField chaosTextField = new JTextField("50%");
//    addEmptyLabels(chaosTextFieldPanel,2);
//    chaosTextFieldPanel.add(chaosTextField);
//    addEmptyLabels(chaosTextFieldPanel,2);
//    chaosPanel.add(chaosTextFieldPanel);
//    
//    JSlider chaosSlider= new JSlider(JSlider.VERTICAL);
//    chaosSlider.setBackground(new Color(0,0,0,64));
//    chaosPanel.add(chaosSlider);
//    
//    secondCol.add(chaosPanel);
//    
//    addEmptyLabels(secondCol,4);
    
//
//    
//    this.getContentPane().add(secondCol);
//    this.getContentPane().add(new JLabel());
//    
//    JPanel subpubhelpPanel = new JPanel();
//    subpubhelpPanel.setLayout(new GridLayout(1, 2));
//    JPanel subpubPanel = new JPanel();
//    subpubPanel.setLayout(new GridLayout(2, 1));
//    subpubPanel.add(new JButton("Load Composer"));
//    subpubPanel.add(new JButton("Publish Composer"));
//    subpubhelpPanel.add(subpubPanel);
//    JPanel helpPanel = new JPanel();
//    helpPanel.setLayout(new GridLayout(1,1));
//    helpPanel.add(new JButton("?"));
//    subpubhelpPanel.add(helpPanel);
//    this.getContentPane().add(subpubhelpPanel);
//    
//    for(int i = 0; i < 3 * 1; i++) {
//        this.getContentPane().add(new JLabel());
//    }
//    
//
//    this.getContentPane().add(new JButton("Reforge"));
//    this.getContentPane().add(new JButton("Smith"));
 
  }
  /**
   *  Slider value has changed...
   */
  public void stateChanged(ChangeEvent e) {
    JSlider sl = (JSlider)e.getSource();
    
    if (!DEBUG) {
	    if (sl == this.VolumeSlider) {
	      //this.pPlugin.setParameter(DelayProgram.PARAM_ID_OUT, (float)((float)sl.getValue() / 100F));
	      this.pPlugin.setParameterAutomated(MelodySmithVST.PARAM_ID_OUT, (float)((float)sl.getValue() / 100F));
	      this.VolumeText.setText(this.pPlugin.getParameterDisplay(MelodySmithVST.PARAM_ID_OUT));
	    }
	    else if (sl == this.FeedbackSlider) {
	      //this.pPlugin.setParameter(DelayProgram.PARAM_ID_FEEDBACK, (float)((float)sl.getValue() / 100F));
	      this.pPlugin.setParameterAutomated(MelodySmithVST.PARAM_ID_FEEDBACK, (float)((float)sl.getValue() / 100F));
	      this.FeedbackText.setText(this.pPlugin.getParameterDisplay(MelodySmithVST.PARAM_ID_FEEDBACK));
	    }
	    else if (sl == this.DelaySlider) {
	      //this.pPlugin.setParameter(DelayProgram.PARAM_ID_DELAY, (float)((float)sl.getValue() / 100F));
	      this.pPlugin.setParameterAutomated(MelodySmithVST.PARAM_ID_DELAY, (float)((float)sl.getValue() / 100F));
	      this.DelayText.setText(this.pPlugin.getParameterDisplay(MelodySmithVST.PARAM_ID_DELAY));
	    }
    }
  }

  
  public static void main(String[] args) throws Throwable {
	DEBUG=true;
	
    MelodySmithVSTGUI gui = new MelodySmithVSTGUI(null,null);
    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gui.show();
  }
  
  private void showUploadTrainingSet(JPanel trainingsetPanel) {
    //Upload Training Data button
    trainingsetPanel.setLayout(new FlowLayout());
    int trainingSetPanelWidth = trainingsetPanel.getPreferredSize().width;
    int trainingSetPanelHeight = trainingsetPanel.getPreferredSize().height;
    
    JButton uploadTrainingDataButton = new JButton("Upload MIDI Files");
    int uploadTrainingDataButtonWidth = (int) (trainingSetPanelWidth / 1.5);
    int uploadTrainingDataButtonHeight = (int) (trainingSetPanelHeight / 10);
    uploadTrainingDataButton.setPreferredSize(new Dimension(uploadTrainingDataButtonWidth, uploadTrainingDataButtonHeight));
    uploadTrainingDataButton.setFont(uploadTrainingDataButton.getFont().deriveFont(32.0f));
    uploadTrainingDataButton.setBackground(Color.CYAN);
    
    uploadTrainingDataButton.setBorder(new TextBubbleBorder(Color.BLUE,4,16,0, false));

    //Process uploading files
   
    uploadTrainingDataButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final JFileChooser fc = new JFileChooser();
            fc.setMultiSelectionEnabled(true);
            int returnVal = fc.showOpenDialog(trainingsetPanel);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                currentlyUploadedFiles = fc.getSelectedFiles();
                
                trainingsetPanel.removeAll();
                showTrainingSetDataEditing(trainingsetPanel);
                trainingsetPanel.revalidate();
                trainingsetPanel.repaint();

            } else {
                System.out.println("Nothing selected");
            }
        }
    });  

    trainingsetPanel.add(uploadTrainingDataButton);
  }
  
  private void showTrainingSetInfluences(JPanel trainingsetPanel) {
      trainingsetPanel.setLayout(new GridLayout(0,3));
      
//      //Add new training set button
//      JButton newTrainingSetButton = new JButton("New Training Set");
//      newTrainingSetButton.setFont(newTrainingSetButton.getFont().deriveFont(12.0f));
//      newTrainingSetButton.setBackground(Color.CYAN);
//      newTrainingSetButton.setBorder(new TextBubbleBorder(Color.BLUE,4,64,0, false));
//      trainingsetPanel.add(newTrainingSetButton);
      
      //Influence title
      addEmptyLabels(trainingsetPanel, 1);
      JLabel influencesLabel = new JLabel("Influences");
      influencesLabel.setFont(influencesLabel.getFont().deriveFont(32.0f));
      influencesLabel.setForeground(Color.WHITE);
      trainingsetPanel.add(influencesLabel);
      addEmptyLabels(trainingsetPanel, 1);
      
      
      
      JTextField[] influenceTextFields = new JTextField[this.artistGroupings.length];
      for(int i = 0; i < this.artistGroupings.length; i++) {
          
          JLabel artistLabel = new JLabel(artistGroupings[i].artistName);
          artistLabel.setFont(artistLabel.getFont().deriveFont(20.0f));
          artistLabel.setForeground(Color.lightGray);

          JSlider influenceSlider = new JSlider(0,100,0);
          influenceSlider.setBackground(Color.BLACK);
          //influenceSlider.setBackground(new Color(0,0,0,64));
          //firstCol.add(influenceSlider);

          
          JPanel influenceTextFieldPanel = new JPanel();
          
          //Find out how tall our influenceTextField should be so the bottom border shows up right (rows needs to be odd)
          /*int rows = influenceTextFieldPanel.height / 20;
          System.out.println(rows);
          if(rows % 2 == 0) rows -= 1;
          if(rows < 1) rows = 1;*/
          
          influenceTextFieldPanel.setLayout(new GridLayout(5,4));
          influenceTextFieldPanel.setBackground(Color.BLACK);
          
          JTextField influenceTextField = new JTextField("0");
          influenceTextField.setForeground(Color.lightGray);
          influenceTextField.setBackground(Color.BLACK);
          influenceTextField.setFont(influenceTextField.getFont().deriveFont(20.0F));
          influenceTextField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.ORANGE));
          
          JLabel influencePercentageSign = new JLabel("%");
          influencePercentageSign.setBackground(Color.BLACK);
          influencePercentageSign.setForeground(Color.lightGray);
          influencePercentageSign.setFont(influencePercentageSign.getFont().deriveFont(20.0F));
          
          addEmptyLabels(influenceTextFieldPanel, 9);
          influenceTextFieldPanel.add(influenceTextField);
          influenceTextFieldPanel.add(influencePercentageSign);
          addEmptyLabels(influenceTextFieldPanel, 9);
          
          //Link sliders and influence text fields
          influenceSlider.addChangeListener(new ChangeListener(){
              @Override
              public void stateChanged(ChangeEvent e) {
                  influenceTextField.setText(String.valueOf(influenceSlider.getValue()));
              }
          });
          influenceTextField.addKeyListener(new KeyAdapter(){
              @Override
              public void keyReleased(KeyEvent ke) {
                  String typed = influenceTextField.getText();
                  influenceSlider.setValue(0);
                  if(!typed.matches("\\d+") || typed.length() > 3) {
                      return;
                  }
                  int value = Integer.parseInt(typed);
                  influenceSlider.setValue(value);
              }
          });
          
          influenceTextFields[i] = influenceTextField;
          
          trainingsetPanel.add(artistLabel);
          trainingsetPanel.add(influenceSlider);
          trainingsetPanel.add(influenceTextFieldPanel);
      }      
  }
  
  private void showTrainingSetDataEditing(JPanel trainingsetPanel) {
    //Training set edit corpus data
    trainingsetPanel.setLayout(new GridLayout(0,2));
    this.artistNameFields = new JTextField[this.currentlyUploadedFiles.length];
    for(int i = 0; i < this.currentlyUploadedFiles.length; i++) {
        JLabel midiFname = new JLabel(this.currentlyUploadedFiles[i].getName());
        midiFname.setForeground(Color.lightGray);
        midiFname.setFont(midiFname.getFont().deriveFont(20.0f));
        JTextField midiArtistname = new JTextField("Artist Name");
        midiArtistname.setFont(midiArtistname.getFont().deriveFont(20.0f));
        midiArtistname.setForeground(Color.GRAY);
        
        midiArtistname.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (midiArtistname.getText().equals("Artist Name")) {
                    midiArtistname.setText("");
                    midiArtistname.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (midiArtistname.getText().isEmpty()) {
                    midiArtistname.setForeground(Color.GRAY);
                    midiArtistname.setText("Artist Name");
                }
            }
            });
        artistNameFields[i] = midiArtistname;
        
        trainingsetPanel.add(midiFname);
        trainingsetPanel.add(midiArtistname);
        addEmptyLabels(trainingsetPanel, 2);
    }   
    
    //Next button for training corpus data editing
    addEmptyLabels(trainingsetPanel, 1);
    JButton nextEditTrainingSetButton = new JButton("Next");
    nextEditTrainingSetButton.setFont(nextEditTrainingSetButton.getFont().deriveFont(32.0f));
    nextEditTrainingSetButton.setBackground(Color.CYAN);
    nextEditTrainingSetButton.setBorder(new TextBubbleBorder(Color.BLUE,4,64,0, false));
    
    nextEditTrainingSetButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            //Write Midi files to temporary directory
            try{
                FileUtils.cleanDirectory(newDirectory);
            } catch(Exception ex) {
                System.out.println("Unable to clean training set directory");
            }
            boolean success = (newDirectory).mkdirs();
            if (!success) {
                System.out.println("Unabled to create directory to store training sets");
            }

            currDirectoryPathName = newDirectoryName;
            for(int i = 0; i < currentlyUploadedFiles.length; i++) {
                File f = currentlyUploadedFiles[i];
                System.out.println("Opening: " + f.getName());
                String newFileName = newDirectoryName + "//" + currentlyUploadedFiles[i].getName();
                try {
                    FileUtils.copyFile(f, new File(newFileName));
                } catch(IOException ex) {
                    ex.printStackTrace();
                }

                System.out.println("Wrote MIDI file to " + currDirectoryPathName);
            }
            
            //Create artist groupings
            HashMap<String, ArrayList<File>> artistNameToFiles = new HashMap();
            for(int i = 0; i < currentlyUploadedFiles.length; i++) {
                String currArtistName = artistNameFields[i].getText().trim();
                ArrayList<File> artistFiles = artistNameToFiles.get(currArtistName);
                
                if(artistFiles == null) artistFiles = new ArrayList();
                artistFiles.add(currentlyUploadedFiles[i]);
                artistNameToFiles.put(currArtistName, artistFiles);
            }
            
            artistGroupings = new ArtistGrouping[artistNameToFiles.size()];
            Iterator it = artistNameToFiles.entrySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                artistGroupings[i++] = new ArtistGrouping((String) pair.getKey(), 0, (ArrayList<File>) pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
            
            trainingsetPanel.removeAll();
            showTrainingSetInfluences(trainingsetPanel);
            trainingsetPanel.revalidate();
            trainingsetPanel.repaint();
        }
    });
    
    trainingsetPanel.add(nextEditTrainingSetButton);
  }
  
}

class TextBubbleBorder extends AbstractBorder {

    private Color color;
    private int thickness = 4;
    private int radii = 8;
    private int pointerSize = 7;
    private Insets insets = null;
    private BasicStroke stroke = null;
    private int strokePad;
    private int pointerPad = 4;
    private boolean left = true;
    RenderingHints hints;

    TextBubbleBorder(
            Color color) {
        this(color, 4, 8, 7);
    }

    TextBubbleBorder(
            Color color, int thickness, int radii, int pointerSize) {
        this.thickness = thickness;
        this.radii = radii;
        this.pointerSize = pointerSize;
        this.color = color;

        stroke = new BasicStroke(thickness);
        strokePad = thickness / 2;

        hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = radii + strokePad;
        int bottomPad = pad + pointerSize + strokePad;
        insets = new Insets(pad, pad, bottomPad, pad);
    }

    TextBubbleBorder(
            Color color, int thickness, int radii, int pointerSize, boolean left) {
        this(color, thickness, radii, pointerSize);
        this.left = left;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        return getBorderInsets(c);
    }

    @Override
    public void paintBorder(
            Component c,
            Graphics g,
            int x, int y,
            int width, int height) {

        Graphics2D g2 = (Graphics2D) g;

        int bottomLineY = height - thickness - pointerSize;

        RoundRectangle2D.Double bubble = new RoundRectangle2D.Double(
                0 + strokePad,
                0 + strokePad,
                width - thickness,
                bottomLineY,
                radii,
                radii);

        Polygon pointer = new Polygon();

        if (left) {
            // left point
            pointer.addPoint(
                    strokePad + radii + pointerPad,
                    bottomLineY);
            // right point
            pointer.addPoint(
                    strokePad + radii + pointerPad + pointerSize,
                    bottomLineY);
            // bottom point
            pointer.addPoint(
                    strokePad + radii + pointerPad + (pointerSize / 2),
                    height - strokePad);
        } else {
            // left point
            pointer.addPoint(
                    width - (strokePad + radii + pointerPad),
                    bottomLineY);
            // right point
            pointer.addPoint(
                    width - (strokePad + radii + pointerPad + pointerSize),
                    bottomLineY);
            // bottom point
            pointer.addPoint(
                    width - (strokePad + radii + pointerPad + (pointerSize / 2)),
                    height - strokePad);
        }

        Area area = new Area(bubble);
        area.add(new Area(pointer));

        g2.setRenderingHints(hints);

        // Paint the BG color of the parent, everywhere outside the clip
        // of the text bubble.
        Component parent  = c.getParent();
        if (parent!=null) {
            Color bg = parent.getBackground();
            Rectangle rect = new Rectangle(0,0,width, height);
            Area borderRegion = new Area(rect);
            borderRegion.subtract(area);
            g2.setClip(borderRegion);
            g2.setColor(bg);
            g2.fillRect(0, 0, width, height);
            g2.setClip(null);
        }

        g2.setColor(color);
        g2.setStroke(stroke);
        g2.draw(area);
    }
}

class Fader
{
    //  background color when component has focus
    private Color fadeColor;

    //  steps to fade from original background to fade background
    private int steps;

    //  apply transition colors at this time interval
    private int interval;

    //  store transition colors from orginal background to fade background
    private Hashtable backgroundColors = new Hashtable();

    /*
     *  Fade from a background color to the specified color using
     *  the default of 10 steps at a 50 millisecond interval.
     *
     *  @param fadeColor the temporary background color
     */
    public Fader(Color fadeColor)
    {
        this(fadeColor, 10, 50);
    }

    /*
     *  Fade from a background color to the specified color in the
     *  specified number of steps at the default 5 millisecond interval.
     *
     *  @param fadeColor the temporary background color
     *  @param steps     the number of steps to fade in the color
     */
    public Fader(Color fadeColor, int steps)
    {
        this(fadeColor, steps, 50);
    }

    /*
     *  Fade from a background color to the specified color in the
     *  specified number of steps at the specified time interval.
     *
     *  @param fadeColor the temporary background color
     *  @param steps     the number of steps to fade in the color
     *  @param intevral  the interval to apply color fading
     */
    public Fader(Color fadeColor, int steps, int interval)
    {
        this.fadeColor = fadeColor;
        this.steps = steps;
        this.interval = interval;
    }

    /*
     *  Add a component to this fader.
     *
     *  The fade color will be applied when the component gains focus.
     *  The background color will be restored when the component loses focus.
     *
     *  @param component apply fading to this component
    */
    public Fader add(JComponent component)
    {
        //  Get colors to be used for fading

        ArrayList colors = getColors( component.getBackground() );

        //  FaderTimer will apply colors to the component

        new FaderTimer( colors, component, interval );

        return this;
    }

    /*
    **  Get the colors used to fade this background
    */
    private ArrayList getColors(Color background)
    {
        //  Check if the color ArrayList already exists

        Object o = backgroundColors.get( background );

        if (o != null)
        {
            return (ArrayList)o;
        }

        //  Doesn't exist, create fader colors for this background

        ArrayList colors = new ArrayList( steps + 1 );
        colors.add( background );

        int rDelta = ( background.getRed() - fadeColor.getRed() ) / steps;
        int gDelta = ( background.getGreen() - fadeColor.getGreen() ) / steps;
        int bDelta = ( background.getBlue() - fadeColor.getBlue() ) / steps;

        for (int i = 1; i < steps; i++)
        {
            int rValue = background.getRed() - (i * rDelta);
            int gValue = background.getGreen() - (i * gDelta);
            int bValue = background.getBlue() - (i * bDelta);

            colors.add( new Color(rValue, gValue, bValue) );
        }

        colors.add( fadeColor );
        backgroundColors.put(background, colors);

        return colors;
    }

    class FaderTimer implements FocusListener, ActionListener
    {
        private ArrayList colors;
        private JComponent component;
        private Timer timer;
        private int alpha;
        private int increment;

        FaderTimer(ArrayList colors, JComponent component, int interval)
        {
            this.colors = colors;
            this.component = component;
            component.addFocusListener( this );
            timer = new Timer(interval, this);
        }

        public void focusGained(FocusEvent e)
        {
            alpha = 0;
            increment = 1;
            timer.start();
        }

        public void focusLost(FocusEvent e)
        {
            alpha = steps;
            increment = -1;
            timer.start();
        }

        public void actionPerformed(ActionEvent e)
        {
            alpha += increment;

            component.setBackground( (Color)colors.get(alpha) );

            if (alpha == steps || alpha == 0)
                timer.stop();
        }
    }
}