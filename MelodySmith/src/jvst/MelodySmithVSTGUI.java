/*
 * References:
 * http://jvstwrapper.sourceforge.net/
 */

package jvst;

/**
 *
 * @author Team AudioMIDIum, University of Utah Senior Project 2017-2018
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

import jvst.wrapper.*;
import static jvst.wrapper.VSTPluginGUIAdapter.RUNNING_MAC_X;
import jvst.wrapper.gui.VSTPluginGUIRunner;


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
    this.setSize(200, 200);
    this.setResizable(false);
    
    this.pPlugin = plug;
    
    this.init();
    
    //this is needed on the mac only, 
    //java guis are handled there in a pretty different way than on win/linux
    //XXX
    if (RUNNING_MAC_X) this.show();
  }


  public void init() {    
    if (!DEBUG) {
    	((MelodySmithVST)plugin).gui=this; //tell the plug that it has a gui!
    	
    	this.VolumeSlider = new JSlider(JSlider.VERTICAL, 1, 100, (int)(this.pPlugin.getParameter(MelodySmithVST.PARAM_ID_OUT) * 100F));
    	this.FeedbackSlider = new JSlider(JSlider.VERTICAL, 1, 100, (int)(this.pPlugin.getParameter(MelodySmithVST.PARAM_ID_FEEDBACK) * 100F));
    	this.DelaySlider = new JSlider(JSlider.VERTICAL, 1, 100, (int)(this.pPlugin.getParameter(MelodySmithVST.PARAM_ID_DELAY) * 100F));
    }
    else {
       	this.VolumeSlider = new JSlider(JSlider.VERTICAL, 1, 100, 1);
    	this.FeedbackSlider = new JSlider(JSlider.VERTICAL, 1, 100, 1);
    	this.DelaySlider = new JSlider(JSlider.VERTICAL, 1, 100, 1);
    }
    this.VolumeSlider.addChangeListener(this);
    this.FeedbackSlider.addChangeListener(this);
    this.DelaySlider.addChangeListener(this);

    if (!DEBUG) {
	    this.VolumeText = new JTextField(this.pPlugin.getParameterDisplay(MelodySmithVST.PARAM_ID_OUT));
	    this.FeedbackText = new JTextField(this.pPlugin.getParameterDisplay(MelodySmithVST.PARAM_ID_FEEDBACK));
	    this.DelayText = new JTextField(this.pPlugin.getParameterDisplay(MelodySmithVST.PARAM_ID_DELAY));
	}
    else {
	    this.VolumeText = new JTextField("0");
	    this.FeedbackText = new JTextField("0");
	    this.DelayText = new JTextField("0");    	
    }
    
    JLabel DelayLabel = new JLabel("Delay");
    JLabel FeedbackLabel = new JLabel("Feedback");
    JLabel VolumeLabel = new JLabel("Volume");

    GridLayout grids = new GridLayout(1, 3);
    this.getContentPane().setLayout(grids);

    Box VolumeBox = new Box(BoxLayout.Y_AXIS);
    Box FeedbackBox = new Box(BoxLayout.Y_AXIS);
    Box DelayBox = new Box(BoxLayout.Y_AXIS);

    VolumeBox.add(VolumeLabel);
    VolumeBox.add(this.VolumeSlider);
    VolumeBox.add(this.VolumeText);

    FeedbackBox.add(FeedbackLabel);
    FeedbackBox.add(this.FeedbackSlider);
    FeedbackBox.add(this.FeedbackText);

    DelayBox.add(DelayLabel);
    DelayBox.add(this.DelaySlider);
    DelayBox.add(this.DelayText);

    this.getContentPane().add(DelayBox);
    this.getContentPane().add(FeedbackBox);
    this.getContentPane().add(VolumeBox);
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
  
}
