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

	keyKnobLF.isThreeColumn = false;

	addAndMakeVisible(analysisControls);
	analysisControls.setText("Analysis", NotificationType::dontSendNotification);
	analysisControls.setFont(Font("Avenir", 16.0f, Font::bold));
	analysisControls.setJustificationType(Justification::centred);

	nGramLengthLabel.setText("Rhythmic Importance", NotificationType::dontSendNotification);
	nGramLengthLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(nGramLengthLabel);

	addAndMakeVisible(nGramLengthSlider);
	nGramLengthSlider.setRange(1, 10, 1.0);
	nGramLengthSlider.setLookAndFeel(&keyKnobLF);
	nGramLengthSlider.setSliderStyle(Slider::Rotary);
	nGramLengthSlider.setTextBoxStyle(Slider::NoTextBox, false, 50, 20);

	numberOfComparisonsLabel.setText("Melodic Importance", NotificationType::dontSendNotification);
	numberOfComparisonsLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(numberOfComparisonsLabel);

	addAndMakeVisible(numberOfComparisonsSlider);
	numberOfComparisonsSlider.setRange(1, 10, 1.0);
	numberOfComparisonsSlider.setLookAndFeel(&keyKnobLF);
	numberOfComparisonsSlider.setSliderStyle(Slider::Rotary);
	numberOfComparisonsSlider.setTextBoxStyle(Slider::NoTextBox, false, 50, 20);
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

	g.fillAll(Colours::deepskyblue.darker(0.6f));   // clear the background

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
	analysisControls.setBounds(area.removeFromTop(analysisControls.getHeight() + 20).reduced(0, 2));
	int areaWidth = area.getWidth() / 2;
	{
		int heightPartition = area.getHeight() / 5;

		juce::Rectangle<int> col = area.removeFromLeft(areaWidth);
		nGramLengthLabel.setBounds(col.removeFromTop(heightPartition));
		nGramLengthSlider.setBounds(col.reduced(10, 0));

		col = area.removeFromLeft(areaWidth);
		numberOfComparisonsLabel.setBounds(col.removeFromTop(heightPartition));
		numberOfComparisonsSlider.setBounds(col.reduced(10, 0));
	}
}
