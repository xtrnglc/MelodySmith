/*
  ==============================================================================

    AdvancedParamsPanel.cpp
    Created: 20 Feb 2018 9:15:31pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#include "../JuceLibraryCode/JuceHeader.h"
#include "AdvancedParamsPanel.h"

//==============================================================================
AdvancedParamsPanel::AdvancedParamsPanel()
{
    // In your constructor, you should add any child components, and
    // initialise any special settings that your component needs.

	nGramLengthSlider.setTextBoxStyle(Slider::TextBoxRight, false, 50, 20);
	numberOfComparisonsSlider.setTextBoxStyle(Slider::TextBoxRight, false, 50, 20);

	addAndMakeVisible(nGramLengthSlider);
	nGramLengthSlider.setRange(2, 10, 1.0);
	addAndMakeVisible(numberOfComparisonsSlider);
	numberOfComparisonsSlider.setRange(1, 9, 1.0);

	nGramLengthLabel.setText("n-Gram Size", NotificationType::dontSendNotification);
	nGramLengthLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(nGramLengthLabel);

	numberOfComparisonsLabel.setText("Number of Comparisons", NotificationType::dontSendNotification);
	numberOfComparisonsLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(numberOfComparisonsLabel);
}

AdvancedParamsPanel::~AdvancedParamsPanel()
{
}

void AdvancedParamsPanel::paint (Graphics& g)
{
    /* This demo code just fills the component's background and
       draws some placeholder text to get you started.

       You should replace everything in this method with your own
       drawing code..
    */

    g.fillAll (Colours::darkcyan/*getLookAndFeel().findColour (ResizableWindow::backgroundColourId)*/);   // clear the background

   /* g.setColour (Colours::grey);
    g.drawRect (getLocalBounds(), 1);   // draw an outline around the component

    g.setColour (Colours::white);
    g.setFont (14.0f);
    g.drawText ("AdvancedParamsPanel", getLocalBounds(),
                Justification::centred, true);   // draw some placeholder text*/
}

void AdvancedParamsPanel::resized()
{
    // This method is where you should set the bounds of any child
    // components that your component contains..

	juce::Rectangle<int> area(getLocalBounds());

	{
		juce::Rectangle<int> col = area.removeFromLeft(area.getWidth() / 2);
		int heightPartition = col.getHeight() / 5;
		col = col.reduced(0, heightPartition);
		nGramLengthLabel.setBounds(col.removeFromTop(heightPartition));
		nGramLengthSlider.setBounds(col.removeFromTop(heightPartition).reduced(10, 0));
		col = area;
		col = col.reduced(0, heightPartition);
		numberOfComparisonsLabel.setBounds(col.removeFromTop(heightPartition));
		numberOfComparisonsSlider.setBounds(col.removeFromTop(heightPartition).reduced(10, 0));
	}
}
