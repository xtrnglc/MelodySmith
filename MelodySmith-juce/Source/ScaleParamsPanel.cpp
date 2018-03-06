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

	addAndMakeVisible(restControls);
	restControls.setText("Rest", NotificationType::dontSendNotification);
	restControls.setFont(Font("Avenir", 16.0f, Font::bold));
	restControls.setJustificationType(Justification::centred);

	syncopationLabel.setText("Syncopation", NotificationType::dontSendNotification);
	syncopationLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(syncopationLabel);

	addAndMakeVisible(syncopationSlider);
	syncopationSlider.setRange(1, 10, 1.0);
	syncopationSlider.setLookAndFeel(&keyKnobLF);
	syncopationSlider.setSliderStyle(Slider::Rotary);
	syncopationSlider.setTextBoxStyle(Slider::NoTextBox, false, 50, 20);

	restAmntLabel.setText("Rest Amount", NotificationType::dontSendNotification);
	restAmntLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(restAmntLabel);

	addAndMakeVisible(restAmntSlider);
	restAmntSlider.setRange(1, 10, 1.0);
	restAmntSlider.setLookAndFeel(&keyKnobLF);
	restAmntSlider.setSliderStyle(Slider::Rotary);
	restAmntSlider.setTextBoxStyle(Slider::NoTextBox, false, 50, 20);

	scaleLabel.setText("Scale", NotificationType::dontSendNotification);
	scaleLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(scaleLabel);

	keySelect.addItem("c Gypsy", 1);
	keySelect.addItem("a Minor", 2);
	keySelect.addItem("D Blues", 3);
	keySelect.addItem("f Harmonic", 4);
	keySelect.addItem("G Chromatic", 5);
	keySelect.addItem("C Major", 6);

	keySelect.setSelectedId(1);
	addAndMakeVisible(keySelect);

	restTypeLabel.setText("Rest Type", NotificationType::dontSendNotification);
	restTypeLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(restTypeLabel);

	restTypeSelect.addItem("Constructive", 1);
	restTypeSelect.addItem("Destructive", 2);
	restTypeSelect.setSelectedId(2);
	addAndMakeVisible(restTypeSelect);

	/*restTypeBtnDes.setButtonText("DES");
	addAndMakeVisible(restTypeBtnDes);
	//restTypeBtnDes.setColour(ToggleButton::colour)
	restTypeBtnCon.setButtonText("CON");
	addAndMakeVisible(restTypeBtnCon);*/

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

	g.fillAll(Colours::deepskyblue.darker(0.6f));   // clear the background

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

	/*juce::Rectangle<int> area(getLocalBounds());
	int heightPartition = area.getHeight() / 5;
	area = area.reduced(0, heightPartition);
	scaleLabel.setBounds(area.removeFromTop(heightPartition));
	keySelect.setBounds(area.removeFromTop(heightPartition).reduced(50, 0));*/

	juce::Rectangle<int> area(getLocalBounds());
	restControls.setBounds(area.removeFromTop(restControls.getHeight() + 20).reduced(0, 2));
	int areaWidth = area.getWidth() / 3;
	{
		int heightPartition = area.getHeight() / 5;

		juce::Rectangle<int> col = area.removeFromLeft(areaWidth);
		syncopationLabel.setBounds(col.removeFromTop(heightPartition));
		syncopationSlider.setBounds(col.reduced(10, 0));

		col = area.removeFromLeft(areaWidth);
		restAmntLabel.setBounds(col.removeFromTop(heightPartition));
		restAmntSlider.setBounds(col.reduced(10, 0));

		col = area.removeFromLeft(areaWidth);
		scaleLabel.setBounds(col.removeFromTop(heightPartition));
		keySelect.setBounds(col.removeFromTop(heightPartition));

		Rectangle<int> restSelect = col.removeFromTop(3 * heightPartition).reduced(0, 4);
		int restSelectHeight = restSelect.getHeight();

		restTypeLabel.setBounds(restSelect.removeFromTop(restSelectHeight / 3 + 5).reduced(0, 2));
		restTypeSelect.setBounds(restSelect);
		/*restTypeBtnDes.setBounds(restSelect.removeFromLeft(restSelect.getWidth() / 2));
		restTypeBtnCon.setBounds(restSelect);*/
	}

}
