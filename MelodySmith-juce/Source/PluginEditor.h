/*
  ==============================================================================

    This file was auto-generated!

    It contains the basic framework code for a JUCE plugin editor.

  ==============================================================================
*/

#pragma once

#include "../JuceLibraryCode/JuceHeader.h"
#include "PluginProcessor.h"
#include "MyKnobLF.h"
#include "ScaleParamsPanel.h"
#include "BasicParamsPanel.h";
#include "AdvancedParamsPanel.h"
#include "CorpusPanel.h"

//==============================================================================
/**
*/
class MelodySmithVSTAudioProcessorEditor  : public AudioProcessorEditor, private Slider::Listener, private ImageButton::Listener
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

	MelodySmithVSTAudioProcessor* audioProcessor;

	//Color and size variables
	int pluginHeight;
	int pluginWidth;
	Colour header1Colour;
	Colour buttonBgColour, buttonTextColour;
	Colour pluginBgColour, divBgColour;
	Colour tabSelectedColour, tabDeselectedColour;
	Colour divTextColour, divBorderColour;

	Label corpusHeader;
	Label parametersHeader;
	//CorpusPanel corpusPanel;
	TabbedComponent tabbedCorpusComponent;
	ManagePanel managePanel;
	InfluencesPanel influencesPanel;
	Array<std::tuple<String, String>> curr_artist_filename_tuples;
	Array<std::tuple<String, double>> artists_to_influences;

	TextButton reforgeImageBtn;
	TextButton forgeImageBtn;

	void sliderValueChanged(Slider* slider) override;

	void buttonClicked(Button* btn) override;

    // This reference is provided as a quick way for your editor to
    // access the processor object that created it.
    MelodySmithVSTAudioProcessor& processor;

	Slider midiVolume;
	Slider keyKnob;
	MyKnobLF keyKnobLF;

	String melodysmithDirPath;

	ScaleParamsPanel scaleParamsPanel;
	BasicParamsPanel basicParamsPanel;
	AdvancedParamsPanel advancedParamsPanel;

	TextButton exportBtn;
	File exportFolder;

	std::shared_ptr<MidiMessageSequence> MidiSequence = std::make_shared<MidiMessageSequence>();

	int countOfOutputFiles;

    JUCE_DECLARE_NON_COPYABLE_WITH_LEAK_DETECTOR (MelodySmithVSTAudioProcessorEditor)
};
