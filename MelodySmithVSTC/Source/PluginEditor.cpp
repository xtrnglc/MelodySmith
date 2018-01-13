/*
  ==============================================================================

    This file was auto-generated!

    It contains the basic framework code for a JUCE plugin editor.

  ==============================================================================
*/

#include "PluginProcessor.h"
#include "PluginEditor.h"



//==============================================================================
MelodySmithVSTAudioProcessorEditor::MelodySmithVSTAudioProcessorEditor (MelodySmithVSTAudioProcessor& p)
    : AudioProcessorEditor (&p), processor (p)
{
    // Make sure that before the constructor has finished, you've set the
    // editor's size to whatever you need it to be.
	setSizesAndColors();
    setSize (pluginHeight, pluginWidth);
	//setResizable(true, false);

	// these define the parameters of our slider object
	//midiVolume.setSliderStyle(Slider::LinearBarVertical);
	//midiVolume.setRange(0.0, 127.0, 1.0);
	//midiVolume.setTextBoxStyle(Slider::NoTextBox, false, 90, 0);
	//midiVolume.setPopupDisplayEnabled(true, false, this);
	//midiVolume.setTextValueSuffix(" Volume");
	//midiVolume.setValue(1.0);

	//// this function adds the slider to the editor
	//addAndMakeVisible(&midiVolume);

	//midiVolume.addListener(this);

	//sidebar.setColour(TextButton::buttonColourId, Colours::grey);
	//// [7]
	//addAndMakeVisible(sidebar);
	//sideItemA.setColour(TextButton::buttonColourId, Colours::maroon);
	//sideItemB.setColour(TextButton::buttonColourId, Colours::maroon);
	//sideItemC.setColour(TextButton::buttonColourId, Colours::maroon);
	//sideItemA.setButtonText("Item A");
	//sideItemB.setButtonText("Item B");
	//sideItemC.setButtonText("Item C");
	//addAndMakeVisible(sideItemA);
	//addAndMakeVisible(sideItemB);
	//addAndMakeVisible(sideItemC);

	corpusHeader.setColour(Label::textColourId, Colours::aqua);
	corpusHeader.setText("CORPUS", NotificationType::dontSendNotification);
	corpusHeader.setJustificationType(Justification::centred);
	corpusHeader.setFont(Font("Avenir", 20.0f, 0));
	addAndMakeVisible(corpusHeader);

	//corpusListBoxModel = ManageListBoxModel();
	//corpusListBox.addSongs();
	int x = corpusListBox.getNumRows();
	corpusListBox.setColour(ListBox::backgroundColourId, Colours::darkgrey);
	addAndMakeVisible(corpusListBox);
	
	addSongsBtn.setColour(TextButton::buttonColourId, buttonBgColour);
	addSongsBtn.setButtonText("Add Songs");
	addAndMakeVisible(addSongsBtn);
	addSongsBtn.addListener(this);

}

MelodySmithVSTAudioProcessorEditor::~MelodySmithVSTAudioProcessorEditor()
{
}

//==============================================================================
void MelodySmithVSTAudioProcessorEditor::paint (Graphics& g)
{
	// fill the whole window white
	g.fillAll(pluginBgColour);

	// set the current drawing colour to black
	//g.setColour(Colours::white);

    //g.setFont (15.0f);
	g.setFont(Font("Avenir", 20.0f, 0));
    //g.drawFittedText ("Hello World!", getLocalBounds(), Justification::centred, 1);
	//g.drawFittedText("Midi Volume", 0, 0, getWidth(), 30, Justification::centred, 1);
}

void MelodySmithVSTAudioProcessorEditor::resized()
{
    // This is generally where you'll want to lay out the positions of any
    // subcomponents in your editor..

	// sets the position and size of the slider with arguments (x, y, width, height)
	//midiVolume.setBounds(40, 30, 20, getHeight() - 60);
	Rectangle<int> area(getLocalBounds());
	//{
	//	Rectangle<int> sideBarArea(area.removeFromRight(jmax(80, area.getWidth() / 4)));
	//	sidebar.setBounds(sideBarArea);

	//	const int sideItemHeight = 40;
	//	const int sideItemMargin = 5;
	//	sideItemA.setBounds(sideBarArea.removeFromTop(sideItemHeight).reduced(sideItemMargin));
	//	sideItemB.setBounds(sideBarArea.removeFromTop(sideItemHeight).reduced(sideItemMargin));
	//	sideItemC.setBounds(sideBarArea.removeFromTop(sideItemHeight).reduced(sideItemMargin));
	//}
	Rectangle<int> leftCol(area.removeFromLeft(area.getWidth() / 2));
	
	float fontHeight = corpusHeader.getFont().getHeight() + 20;
	Rectangle<int> tempArea(leftCol.removeFromTop(fontHeight));
	corpusHeader.setBounds(tempArea);
	tempArea = Rectangle<int>(leftCol.reduced(15, 10));
	addSongsBtn.setBounds(tempArea.removeFromBottom(tempArea.getHeight() / 12));

	corpusListBox.setBounds(tempArea);

}

void MelodySmithVSTAudioProcessorEditor::sliderValueChanged(Slider* slider)
{
	processor.noteOnVel = midiVolume.getValue();
}

void MelodySmithVSTAudioProcessorEditor::buttonClicked(Button* button)
{
	if (button == &addSongsBtn)
	{
		FileChooser myChooser("Please select the midi files you want to load...",
			File::getSpecialLocation(File::userHomeDirectory),
			"*.mid");
		if (myChooser.browseForMultipleFilesToOpen())
		{
			Array<File> midi_files = myChooser.getResults();
			corpusListBox.addSongs(midi_files);
			corpusListBox.updateContent();
		}
	}
}

void MelodySmithVSTAudioProcessorEditor::setSizesAndColors()
{
	pluginWidth = 700;
	pluginHeight = 900;

	header1Colour = Colours::antiquewhite;
	buttonBgColour = Colours::crimson,  buttonTextColour = Colours::black;

	pluginBgColour = Colours::black;

	divBgColour = Colours::darkgrey;
	tabSelectedColour = Colours::maroon;

	divTextColour = Colours::antiquewhite;
	divBorderColour = Colours::azure;
}

void MelodySmithVSTAudioProcessorEditor::createCorpusPanel()
{

}

void MelodySmithVSTAudioProcessorEditor::createControlsPanel()
{

}
