/*
  ==============================================================================

    BasicParamsPanel.cpp
    Created: 20 Feb 2018 9:15:14pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#include "../JuceLibraryCode/JuceHeader.h"
#include "BasicParamsPanel.h"

//==============================================================================
BasicParamsPanel::BasicParamsPanel()
{
    // In your constructor, you should add any child components, and
    // initialise any special settings that your component needs.

	addAndMakeVisible(invervalWeightSlider);
	invervalWeightSlider.setTextBoxStyle(Slider::TextBoxRight, false, 50, 20);
	invervalWeightSlider.setRange(0, 10, 1.0);
	//Slider::setSliderSnapsToMousePosition

	addAndMakeVisible(durationWeightSlider);
	durationWeightSlider.setRange(0, 10, 1.0);
	durationWeightSlider.setTextBoxStyle(Slider::TextBoxRight, false, 50, 20);

	intervalWeightLabel.setText("Interval", NotificationType::dontSendNotification);
	intervalWeightLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(intervalWeightLabel);

	durationWeightLabel.setText("Duration", NotificationType::dontSendNotification);
	durationWeightLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(durationWeightLabel);

}

BasicParamsPanel::~BasicParamsPanel()
{
}

void BasicParamsPanel::paint (Graphics& g)
{
    /* This demo code just fills the component's background and
       draws some placeholder text to get you started.

       You should replace everything in this method with your own
       drawing code..
    */

    g.fillAll (Colours::tan);   // clear the background

   /* g.setColour (Colours::grey);
    g.drawRect (getLocalBounds(), 1);   // draw an outline around the component

    g.setColour (Colours::white);
    g.setFont (14.0f);
    g.drawText ("BasicParamsPanel", getLocalBounds(),
                Justification::centred, true);   // draw some placeholder text*/
}

void BasicParamsPanel::resized()
{
    // This method is where you should set the bounds of any child
    // components that your component contains..
	juce::Rectangle<int> area(getLocalBounds());
	{
		juce::Rectangle<int> col = area.removeFromLeft(area.getWidth() / 2);
		int heightPartition = col.getHeight() / 5;
		col = col.reduced(0, heightPartition);
		intervalWeightLabel.setBounds(col.removeFromTop(heightPartition));
		invervalWeightSlider.setBounds(col.removeFromTop(heightPartition).reduced(10, 0));
		col = area;
		col = col.reduced(0, heightPartition);
		durationWeightLabel.setBounds(col.removeFromTop(heightPartition));
		durationWeightSlider.setBounds(col.removeFromTop(heightPartition).reduced(10, 0));
	}
}
