/*
 * References:
 * http://jvstwrapper.sourceforge.net/
 */

package jvst;

/**
 *
 * @author Team AudioMIDIum, University of Utah Senior Project 2017-2018
 */


import composition.Composer;

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
import org.apache.commons.lang3.ArrayUtils;


public class MelodySmithVSTGUI extends VSTPluginGUIAdapter implements ChangeListener {

  private static final long serialVersionUID = -8641024370578430211L;

  private VSTPluginAdapter plugin;
  protected static boolean DEBUG = false;
  
  protected static String currDirectoryPathName;
  final int width = 750;
  final int height = 700;
  
  protected FileAndArtistName[] currentFileAndArtistNames = null;
  public JTextField[] artistNameFields = null;
  public ArtistGrouping[] artistGroupings = null;
  public JTextField[] influenceTextFields = null; 
  
  String newDirectoryName;
  File newDirectory;
  String currentAbsolutePath;
  
  ImageIcon fireForgeIcon = null;
  ImageIcon fireForgeIconHover = null;
  ImageIcon anvilReforgeIcon = null; 
  ImageIcon anvilReforgeIconHover = null;
  
  JPanel secondCol;
  boolean isCMajor = true;
  boolean isManageTab = true;
  
  int fileCounter = 0;

  public MelodySmithVSTGUI(VSTPluginGUIRunner r, VSTPluginAdapter plug) throws Exception {
	super(r,plug);
    log("MelodySmithVSTGUI <init>");
    
    this.setTitle("MelodySmith v1.0");
    this.setSize(width, height);
    this.setResizable(true);
    
    this.plugin = plug;
    
    this.currDirectoryPathName = null;
    this.currentFileAndArtistNames = new FileAndArtistName[0];
    this.artistGroupings = new ArtistGrouping[0];
    
    //Get paths and set up directory to write midi files to
    Path currentRelativePath = Paths.get("");
    currentAbsolutePath = currentRelativePath.toAbsolutePath().toString();
    log("MelodySmithVSTGUI curr rel path: " + currentAbsolutePath);
    System.out.println("MelodySmithVSTGUI curr rel path: " + currentAbsolutePath);
    this.newDirectoryName = currentAbsolutePath + "//uploaded_training_sets//curr";
    this.newDirectory = new File(newDirectoryName);     
    
    //Get anvil, fire icons
    try {
        BufferedImage img = ImageIO.read(new File(currentAbsolutePath + "/assets/anvil_recast2.png"));
        ImageIcon icon = new ImageIcon(img);
        anvilReforgeIcon = icon;
    } catch(Exception e) {
        System.out.println(e.toString());
    }
    try {
        BufferedImage img = ImageIO.read(new File(currentAbsolutePath + "/assets/anvil_recast_hover.png"));
        ImageIcon icon = new ImageIcon(img);
        anvilReforgeIconHover = icon;
    } catch(Exception e) {
        System.out.println(e.toString());
    }
    
    try {
        Image img = ImageIO.read(new File(currentAbsolutePath + "/assets/forge_fire2.png"));
        ImageIcon icon = new ImageIcon(img);
        fireForgeIcon = icon;
    } catch(Exception e) {
        System.out.println(e.toString());
    }  
    
    try {
        Image img = ImageIO.read(new File(currentAbsolutePath + "/assets/forge_fire_hover.png"));
        ImageIcon icon = new ImageIcon(img);
        fireForgeIconHover = icon;
    } catch(Exception e) {
        System.out.println(e.toString());
    } 
    
    
    this.init();
    
    //this is needed on the mac only, 
    //java guis are handled there in a pretty different way than on win/linux
    //XXX
    if (RUNNING_MAC_X) this.show();
  }

  public void init() {   
      if (!DEBUG) {
        ((MelodySmithVST)plugin).gui=this;          
      }
    
    JLabel contentPane = new JLabel();
    contentPane.setOpaque(true);
    contentPane.setLayout( new BorderLayout() );
    this.setContentPane( contentPane );
    this.getContentPane().setBackground(Color.BLACK);
    
    GridLayout grids = new GridLayout(1, 2);
    this.getContentPane().setLayout(grids);
    
    //First column////////////////////////////////////////////////////////////////////////////////////////////////////
    JPanel firstCol = new JPanel();
    JPanel trainingsetPanel = new JPanel();
    firstCol.setBackground(Color.BLACK);
    
    //First column Corpus Label and vertical margin
    JPanel trainingsetPanelVerticalMargin = new JPanel();
    trainingsetPanelVerticalMargin.setBackground(Color.BLACK) ;
    trainingsetPanelVerticalMargin.setPreferredSize(new Dimension(this.width / 2, this.height / 32));
    trainingsetPanelVerticalMargin.setLayout(new GridLayout(1,1,0,0));
    
    JLabel trainingsetPanelMarginLabel = new JLabel("CORPUS");
    trainingsetPanelMarginLabel.setForeground(Color.WHITE);
    trainingsetPanelMarginLabel.setHorizontalAlignment(SwingConstants.CENTER);
    trainingsetPanelMarginLabel.setFont(trainingsetPanelMarginLabel.getFont().deriveFont(20.0f));
    
    trainingsetPanelVerticalMargin.add(trainingsetPanelMarginLabel);
    firstCol.add(trainingsetPanelVerticalMargin);
    
    //Add tabs for Manage,Influences
    JPanel manageAndInfluencesTabsPanel = new JPanel();
    manageAndInfluencesTabsPanel.setLayout(new GridLayout(1,2, 0, 0));
    manageAndInfluencesTabsPanel.setBackground(Color.BLACK);
    manageAndInfluencesTabsPanel.setPreferredSize(new Dimension(this.width / 2, this.height / 32));
    firstCol.add(trainingsetPanelVerticalMargin);
    
    JButton manageButton = new JButton("Manage");
    manageButton.setBorder(new RoundedBorder(Color.ORANGE,2,0,0, false));
    manageButton.setFont(manageButton.getFont().deriveFont(20.0f));
    manageButton.setBackground(Color.ORANGE);
    manageButton.setForeground(Color.WHITE);
    
    JButton influencesButton = new JButton("Influences");
    influencesButton.setBorder(new RoundedBorder(Color.ORANGE,2,0,0, false));
    influencesButton.setFont(influencesButton.getFont().deriveFont(20.0f));
    influencesButton.setBackground(Color.BLACK);
    influencesButton.setForeground(Color.WHITE);
    
    manageAndInfluencesTabsPanel.add(manageButton);
    manageAndInfluencesTabsPanel.add(influencesButton);
        
    //Add animations, on clicks for manage and influences buttons
    manageButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!isManageTab) {
                isManageTab = true;
                
                manageButton.setBackground(Color.ORANGE);
                manageButton.setForeground(Color.WHITE);
                influencesButton.setBackground(Color.BLACK);
                influencesButton.setForeground(Color.WHITE);
                manageAndInfluencesTabsPanel.repaint();
                
                trainingsetPanel.removeAll();
                showManageView(trainingsetPanel);
                trainingsetPanel.revalidate();
                trainingsetPanel.repaint();
            }
        }
    });
    influencesButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(isManageTab) {
                isManageTab = false;
                
                manageButton.setBackground(Color.BLACK);
                manageButton.setForeground(Color.WHITE);
                influencesButton.setBackground(Color.ORANGE);
                influencesButton.setForeground(Color.WHITE);
                manageAndInfluencesTabsPanel.repaint();
                
                writeMidiFilesToDirectory();
                createArtistGroupings();       
                
                trainingsetPanel.removeAll();
                showInfluencesView(trainingsetPanel);
                trainingsetPanel.revalidate();
                trainingsetPanel.repaint();               
            }
        }
    });    
    
    firstCol.add(manageAndInfluencesTabsPanel);
    
    //Upload/Modify training set panel
    int trainingSetPanelWidth = (int) ((this.width/ 2) / 1.25);
    int trainingSetPanelHeight = (int) (this.height / 1.25);
    trainingsetPanel.setBackground(Color.BLACK);
    trainingsetPanel.setPreferredSize(new Dimension(trainingSetPanelWidth,trainingSetPanelHeight));
    GridLayout trainingsetGridLayout = new GridLayout(0,1);
    trainingsetGridLayout.setVgap(0);
    trainingsetPanel.setLayout(new GridLayout(0, 1, 0, 0));
    
    this.showManageView(trainingsetPanel);
    firstCol.add(trainingsetPanel);
    
    //Second column///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    this.secondCol = new JPanel();
    secondCol.setBackground(Color.BLACK);
    secondCol.setLayout(new GridLayout(0,1));
    
    //Key Chooser
    JPanel keyandKeyTextPanel = new JPanel();
    keyandKeyTextPanel.setBackground(Color.BLACK);
    keyandKeyTextPanel.setLayout(new GridLayout(4,1));
    
    JLabel keyLabel = new JLabel("KEY");
    keyLabel.setForeground(Color.WHITE);
    keyLabel.setFont(keyLabel.getFont().deriveFont(32.0F));
    keyLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
    JPanel keyPanel = new JPanel();
    keyPanel.setBackground(Color.BLACK);
    keyPanel.setLayout(new GridLayout(1,4)); 
    
    JButton cMajorButton = new JButton("C");
    cMajorButton.setBackground(Color.GREEN);
    cMajorButton.setForeground(Color.BLACK);
    cMajorButton.setFont(cMajorButton.getFont().deriveFont(32.0F));
    JButton aMinorButton = new JButton("a");
    aMinorButton.setFont(aMinorButton.getFont().deriveFont(32.0F));
    aMinorButton.setBackground(Color.CYAN);
    aMinorButton.setForeground(Color.BLACK);
    
    cMajorButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!isCMajor) {
                isCMajor = true;
                final Timer timer1 = getTimerForFadeEffect(Color.GREEN, cMajorButton, keyPanel);
                final Timer timer2 = getTimerForFadeEffect(Color.CYAN, aMinorButton, keyPanel);
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
                final Timer timer1 = getTimerForFadeEffect(Color.GREEN, aMinorButton, keyPanel);
                final Timer timer2 = getTimerForFadeEffect(Color.CYAN, cMajorButton, keyPanel);
                timer1.start();
                timer2.start();
            }
        }
    });
    
    addEmptyLabels(keyPanel,1);
    keyPanel.add(cMajorButton);
    keyPanel.add(aMinorButton);
    addEmptyLabels(keyPanel, 1);
    
    addEmptyLabels(keyandKeyTextPanel,1);
    keyandKeyTextPanel.add(keyLabel);
    keyandKeyTextPanel.add(keyPanel);
    addEmptyLabels(keyandKeyTextPanel,1);
    
    //Anvil and forge labels and icons, hover funcs
    JLabel anvilReforgeLabel = new JLabel(anvilReforgeIcon);
    anvilReforgeLabel.addMouseListener(new MouseAdapter()  
    {  
        @Override
         public void mouseEntered(MouseEvent e) {
    		 anvilReforgeLabel.setIcon(anvilReforgeIconHover);
                 anvilReforgeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                 
         }
         @Override
         public void mouseExited(MouseEvent e) {
    		anvilReforgeLabel.setIcon(anvilReforgeIcon);
                anvilReforgeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

         }
        public void mouseClicked(MouseEvent e)  
        {  
           // you can open a new frame here as
           // i have assumed you have declared "frame" as instance variable
            isManageTab = true;

            manageButton.setBackground(Color.ORANGE);
            manageButton.setForeground(Color.WHITE);
            influencesButton.setBackground(Color.BLACK);
            influencesButton.setForeground(Color.WHITE);
            manageAndInfluencesTabsPanel.repaint();
            
            artistNameFields = null;
            influenceTextFields = null; 
            currentFileAndArtistNames = new FileAndArtistName[0];
            artistGroupings = new ArtistGrouping[0];

            trainingsetPanel.removeAll();
            showManageView(trainingsetPanel);
            trainingsetPanel.revalidate();
            trainingsetPanel.repaint();

        }  
    });
    
    JLabel fireForgedLabel = new JLabel(fireForgeIcon);
    fireForgedLabel.addMouseListener(new MouseAdapter() {
    	 @Override
         public void mouseEntered(MouseEvent e) {
    		 fireForgedLabel.setIcon(fireForgeIconHover);
             fireForgedLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

         }
         @Override
         public void mouseExited(MouseEvent e) {
    		 fireForgedLabel.setIcon(fireForgeIcon);
                fireForgedLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

         }
        @Override
        public void mouseClicked(MouseEvent e) {
        	System.out.println("Calling alex");
            for(int i = 0; i < influenceTextFields.length; i++) {
                artistGroupings[i].artistInfluence = Double.parseDouble(influenceTextFields[i].getText()); 
            }
            ArtistGrouping[] currArtistGroupings = artistGroupings;
            for(ArtistGrouping ag: currArtistGroupings) {
                System.out.println(ag.artistName + ", " + ag.artistInfluence);
            }
            boolean currKey = isCMajor;
            Composer composer = new Composer(currArtistGroupings);
            composer.composeMelody(currentAbsolutePath + "//generated_files//file" + (fileCounter++) + ".mid", 200, currKey);
        }

    });
    
    secondCol.add(anvilReforgeLabel);
    secondCol.add(keyandKeyTextPanel);
    secondCol.add(fireForgedLabel);
    
    this.getContentPane().add(firstCol);
    this.getContentPane().add(secondCol);
    
 
  }
  
  public void stateChanged(ChangeEvent e) {
      //pass
  }
  
  public static void main(String[] args) throws Throwable {
      DEBUG=true;
	
    MelodySmithVSTGUI gui = new MelodySmithVSTGUI(null,null);
    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gui.show();
  }
  
  
  //View Functions//////////////////////////////////////////////////////////////////////
    
  private void showInfluencesView(JPanel trainingsetPanel) {
      trainingsetPanel.setLayout(new GridLayout(0,3));
          
      //Influence title
      addEmptyLabels(trainingsetPanel, 1);
      JLabel influencesLabel = new JLabel("Influences");
      influencesLabel.setFont(influencesLabel.getFont().deriveFont(20.0f));
      influencesLabel.setForeground(Color.WHITE);
      trainingsetPanel.add(influencesLabel);
      addEmptyLabels(trainingsetPanel, 1);
      
      this.influenceTextFields = new JTextField[this.artistGroupings.length];
      for(int i = 0; i < this.artistGroupings.length; i++) {
          
          JLabel artistLabel = new JLabel(artistGroupings[i].artistName);
          artistLabel.setFont(artistLabel.getFont().deriveFont(20.0f));
          artistLabel.setForeground(Color.lightGray);

          JSlider influenceSlider = new JSlider(0,100,0);
          influenceSlider.setBackground(Color.BLACK);
          influenceSlider.setPaintTicks(true);
          influenceSlider.setPaintLabels(true);
          influenceSlider.setMinorTickSpacing(10);

          JPanel influenceTextFieldPanel = new JPanel();
          
          influenceTextFieldPanel.setLayout(new GridLayout(5,4));
          influenceTextFieldPanel.setBackground(Color.BLACK);
          
          JTextField influenceTextField = new JTextField("0");
          influenceTextField.setForeground(Color.lightGray);
          influenceTextField.setBackground(Color.BLACK);
          influenceTextField.setFont(influenceTextField.getFont().deriveFont(15.0F));
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
  
  private void showManageView(JPanel trainingsetPanel) {
    //Training set edit corpus data
    trainingsetPanel.setLayout(new GridLayout(0,1));
    
    //Add artist, song headers
    JPanel artistAndSongHeaderPanel = new JPanel();
    artistAndSongHeaderPanel.setLayout(new GridLayout(1,2));
    artistAndSongHeaderPanel.setBackground(Color.BLACK);
    
    JLabel artistHeader = new JLabel("ARTIST");
    artistHeader.setForeground(Color.WHITE);
    artistHeader.setFont(artistHeader.getFont().deriveFont(20.0f));
    
    JLabel songHeader = new JLabel("SONG");
    songHeader.setForeground(Color.WHITE);
    songHeader.setFont(songHeader.getFont().deriveFont(20.0f));
    songHeader.setHorizontalAlignment(SwingConstants.RIGHT);
    
    artistAndSongHeaderPanel.add(artistHeader);
    artistAndSongHeaderPanel.add(songHeader);
    
    trainingsetPanel.add(artistAndSongHeaderPanel);
    
    artistNameFields = new JTextField[currentFileAndArtistNames.length];
    for(int i = 0; i < this.currentFileAndArtistNames.length; i++) {
        JPanel currentArtistEditPanel = new JPanel();
        currentArtistEditPanel.setLayout(new GridLayout(1,2));
        currentArtistEditPanel.setBackground(Color.BLACK);
        
        JLabel midiFname = new JLabel(this.currentFileAndArtistNames[i].f.getName());
        midiFname.setForeground(Color.lightGray);
        midiFname.setFont(midiFname.getFont().deriveFont(20.0f));
        midiFname.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JTextField midiArtistname = new JTextField(this.currentFileAndArtistNames[i].artistName);
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
        currentArtistEditPanel.add(midiArtistname);        
        currentArtistEditPanel.add(midiFname);
        trainingsetPanel.add(currentArtistEditPanel);
        addEmptyLabels(trainingsetPanel, 1);
    }   
    
    //If len(currentlyUploadedFiles) < 10, add rows so that our add songs button works
    for(int i = currentFileAndArtistNames.length; i < 10; i++) {
        addEmptyLabels(trainingsetPanel, 2);
    }
    //Add songs for training corpus data editing
    addEmptyLabels(trainingsetPanel, 1);
    JButton nextEditTrainingSetButton = new JButton("Add Songs");
    nextEditTrainingSetButton.setFont(nextEditTrainingSetButton.getFont().deriveFont(18.0f));
    nextEditTrainingSetButton.setBackground(Color.CYAN);    
    
    nextEditTrainingSetButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new File(currentAbsolutePath));
            fc.setMultiSelectionEnabled(true);
            int returnVal = fc.showOpenDialog(trainingsetPanel);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File[] newCurrentlyUploadedFiles = fc.getSelectedFiles();
                FileAndArtistName[] newCurrFileAndArtistNames = new FileAndArtistName[newCurrentlyUploadedFiles.length];
                for(int i = 0; i < newCurrentlyUploadedFiles.length; i++) {
                    newCurrFileAndArtistNames[i] = new FileAndArtistName(newCurrentlyUploadedFiles[i], "Artist Name");
                }
                currentFileAndArtistNames = (FileAndArtistName[])ArrayUtils.addAll(currentFileAndArtistNames, newCurrFileAndArtistNames);
                
                trainingsetPanel.removeAll();
                showManageView(trainingsetPanel);
                trainingsetPanel.revalidate();
                trainingsetPanel.repaint();

            } else {
                System.out.println("Nothing selected");
            }
        }
    }); 
    
    trainingsetPanel.add(nextEditTrainingSetButton);
  }
  
  
  //Helper Functions//////////////////////////////////////////////////////////////////////////
  
  private Timer getTimerForFadeEffect(Color targetColor, JComponent toSetBG, JComponent toRepaint) {
    return new Timer(2, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt) {
            final int changingSpeed = 5;
            
            final Color currentColor = toSetBG.getBackground();

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


            toSetBG.setBackground(new Color(r,g,b));

            toRepaint.repaint();
        }
    });      
  }  
  
  private void createArtistGroupings() {
    HashMap<String, ArrayList<File>> artistNameToFiles = new HashMap();
    for(int i = 0; i < currentFileAndArtistNames.length; i++) {
        currentFileAndArtistNames[i].artistName = artistNameFields[i].getText().trim();
        String currArtistName = currentFileAndArtistNames[i].artistName;
        ArrayList<File> artistFiles = artistNameToFiles.get(currArtistName);

        if(artistFiles == null) artistFiles = new ArrayList();
        artistFiles.add(currentFileAndArtistNames[i].f);
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
  }
  
  private void writeMidiFilesToDirectory() {
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
    for(int i = 0; i < currentFileAndArtistNames.length; i++) {
        File f = currentFileAndArtistNames[i].f;
        System.out.println("Opening: " + f.getName());
        String newFileName = newDirectoryName + "//" + currentFileAndArtistNames[i].f.getName();
        try {
            FileUtils.copyFile(f, new File(newFileName));
        } catch(IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("Wrote MIDI file to " + currDirectoryPathName);
    }      
  }
  
  private void addEmptyLabels(JPanel curr, int num) {
      for(int i= 0; i < num; i++) {
          curr.add(new JLabel());
      }
  } 
}

//private tuple
class FileAndArtistName{
    File f;
    String artistName;
    public FileAndArtistName(File f, String artistName) {
        this.f = f;
        this.artistName = artistName;
    }
}