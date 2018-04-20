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

	addAndMakeVisible(melodyControls);
	melodyControls.setText("Melody", NotificationType::dontSendNotification);
	melodyControls.setFont(Font("Avenir", 16.0f, Font::bold));
	melodyControls.setJustificationType(Justification::centred);

	intervalWeightLabel.setText("Average Note Length", NotificationType::dontSendNotification);
	intervalWeightLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(intervalWeightLabel);

	addAndMakeVisible(invervalWeightSlider);
	invervalWeightSlider.setLookAndFeel(&keyKnobLF);
	invervalWeightSlider.setSliderStyle(Slider::Rotary);
	invervalWeightSlider.setRange(1, 10, 1.0);
	invervalWeightSlider.setTextBoxStyle(Slider::NoTextBox, false, 50, 20);

	durationWeightLabel.setText("N-Gram Length", NotificationType::dontSendNotification);
	durationWeightLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(durationWeightLabel);

	addAndMakeVisible(durationWeightSlider);
	durationWeightSlider.setLookAndFeel(&keyKnobLF);
	durationWeightSlider.setSliderStyle(Slider::Rotary);
	durationWeightSlider.setRange(1, 10, 1.0);
	durationWeightSlider.setTextBoxStyle(Slider::NoTextBox, false, 50, 20);

	creativityLabel.setText("Creativity", NotificationType::dontSendNotification);
	creativityLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(creativityLabel);

	addAndMakeVisible(creativityWeightSlider);
	creativityWeightSlider.setLookAndFeel(&keyKnobLF);
	creativityWeightSlider.setSliderStyle(Slider::Rotary);
	creativityWeightSlider.setRange(1, 10, 1.0);
	creativityWeightSlider.setTextBoxStyle(Slider::NoTextBox, false, 50, 20);

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

    g.fillAll (Colours::deepskyblue.darker(0.6f));   // clear the background

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
	melodyControls.setBounds(area.removeFromTop(melodyControls.getHeight() + 20).reduced(0, 2));
	int areaWidth = area.getWidth() / 3;
	{
		int heightPartition = area.getHeight() / 5;

		juce::Rectangle<int> col = area.removeFromLeft(areaWidth);
		intervalWeightLabel.setBounds(col.removeFromTop(heightPartition));
		invervalWeightSlider.setBounds(col.reduced(10, 0));

		col = area.removeFromLeft(areaWidth);
		durationWeightLabel.setBounds(col.removeFromTop(heightPartition));
		durationWeightSlider.setBounds(col.reduced(10, 0));
		//durationWeightSlider.setBounds(col);

		col = area.removeFromLeft(areaWidth);
		creativityLabel.setBounds(col.removeFromTop(heightPartition));
		creativityWeightSlider.setBounds(col.reduced(10, 0));
	}
}
