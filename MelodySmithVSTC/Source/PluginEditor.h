/*
  ==============================================================================

    This file was auto-generated!

    It contains the basic framework code for a JUCE plugin editor.

  ==============================================================================
*/

#pragma once

#include "../JuceLibraryCode/JuceHeader.h"
#include "PluginProcessor.h"
#include "ManageListBoxModel.h"


//==============================================================================
/**
*/
class MelodySmithVSTAudioProcessorEditor  : public AudioProcessorEditor, private Slider::Listener, public Button::Listener
{
public:
    MelodySmithVSTAudioProcessorEditor (MelodySmithVSTAudioProcessor&);
    ~MelodySmithVSTAudioProcessorEditor();

    //==============================================================================
    void paint (Graphics&) override;
    void resized() override;

private:
	//Functions to setup the GUI in the constructor
	void setSizesAndColors();
	void createCorpusPanel();
	void createControlsPanel();

	//Color and size variables
	int pluginHeight;
	int pluginWidth;
	Colour header1Colour;
	Colour buttonBgColour, buttonTextColour;
	Colour pluginBgColour, divBgColour;
	Colour tabSelectedColour, tabDeselectedColour;
	Colour divTextColour, divBorderColour;

	Label corpusHeader;
	ManageListBoxModel corpusListBox;
	//ManageListBoxModel corpusListBoxModel

	TextButton addSongsBtn;

	void sliderValueChanged(Slider* slider) override;

	void buttonClicked(Button* button) override;

    // This reference is provided as a quick way for your editor to
    // access the processor object that created it.
    MelodySmithVSTAudioProcessor& processor;

	Slider midiVolume;

    JUCE_DECLARE_NON_COPYABLE_WITH_LEAK_DETECTOR (MelodySmithVSTAudioProcessorEditor)
};
