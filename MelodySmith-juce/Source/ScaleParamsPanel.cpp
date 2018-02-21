/*
  ==============================================================================

    ScaleParamsPanel.cpp
    Created: 20 Feb 2018 9:15:49pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#include "../JuceLibraryCode/JuceHeader.h"
#include "ScaleParamsPanel.h"

//==============================================================================
ScaleParamsPanel::ScaleParamsPanel()
{
    // In your constructor, you should add any child components, and
    // initialise any special settings that your component needs.

	scaleLabel.setText("Scale", NotificationType::dontSendNotification);
	scaleLabel.setJustificationType(Justification::centred);
	//scaleLabel.setFont(Font("Avenir", 20.0f, 0));
	//Font f = scaleLabel.getFont();	
	addAndMakeVisible(scaleLabel);

	keySelect.addItem("c Gypsy", 1);
	keySelect.addItem("a Minor", 2);
	keySelect.addItem("D Blues", 3);
	keySelect.addItem("f Harmonic", 4);
	keySelect.addItem("G Chromatic", 5);
	keySelect.addItem("C Major", 6);

	keySelect.setSelectedId(1);
	addAndMakeVisible(keySelect);

}

ScaleParamsPanel::~ScaleParamsPanel()
{
}

void ScaleParamsPanel::paint (Graphics& g)
{
    /* This demo code just fills the component's background and
       draws some placeholder text to get you started.

       You should replace everything in this method with your own
       drawing code..
    */

    g.fillAll (Colours::green/*getLookAndFeel().findColour (ResizableWindow::backgroundColourId)*/);   // clear the background

    /*g.setColour (Colours::grey);
    g.drawRect (getLocalBounds(), 1);   // draw an outline around the component

    /*g.setColour (Colours::white);
    g.setFont (14.0f);
    g.drawText ("ScaleParamsPanel", getLocalBounds(),
                Justification::centred, true);   // draw some placeholder text*/
}

void ScaleParamsPanel::resized()
{
    // This method is where you should set the bounds of any child
    // components that your component contains..

	juce::Rectangle<int> area(getLocalBounds());
	int heightPartition = area.getHeight() / 5;
	area = area.reduced(0, heightPartition);
	scaleLabel.setBounds(area.removeFromTop(heightPartition));
	keySelect.setBounds(area.removeFromTop(heightPartition).reduced(50, 0));

}
