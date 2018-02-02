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
    : AudioProcessorEditor (&p), processor (p), tabbedCorpusComponent(TabbedButtonBar::Orientation::TabsAtLeft), managePanel(curr_artist_filename_tuples),
	influencesPanel(curr_artist_filename_tuples, artists_to_influences)
{
    // Make sure that before the constructor has finished, you've set the
    // editor's size to whatever you need it to be.
	setSizesAndColors();
    setSize (pluginHeight, pluginWidth);
	//setResizable(true, false);

	corpusHeader.setColour(Label::textColourId, header1Colour);
	corpusHeader.setText("CORPUS", NotificationType::dontSendNotification);
	corpusHeader.setJustificationType(Justification::centred);
	corpusHeader.setFont(Font("Avenir", 20.0f, 0));
	addAndMakeVisible(corpusHeader);

	tabbedCorpusComponent.setOrientation(TabbedButtonBar::Orientation::TabsAtTop);
	tabbedCorpusComponent.addTab("Manage", Colours::red, &managePanel, false);
	tabbedCorpusComponent.addTab("Influences", Colours::red, &influencesPanel, false);
	//tabbedCorpusComponent.setTabBackgroundColour(0, Colours::aliceblue);
	//tabbedCorpusComponent.setTabBackgroundColour()
	tabbedCorpusComponent.setColour(TabbedComponent::backgroundColourId, Colours::red.brighter(0.4f));
	//tabbedCorpusComponent.setColour()
	addAndMakeVisible(tabbedCorpusComponent);

	File f1 = File::getCurrentWorkingDirectory().getChildFile("anvil_recast2.png");
	File f2 = File::getCurrentWorkingDirectory().getChildFile("forge_fire2.png");
	Image i1 = ImageFileFormat::loadFrom(f1);
	Image i2 = ImageFileFormat::loadFrom(f2); 
	reforgeImageBtn.setImages(false, true, false, i1, 1.0f, Colours::transparentWhite, i1, 1.0f, Colours::darkblue, i1, 1.0f, Colours::darkblue);
	reforgeImageBtn.setMouseCursor(MouseCursor::PointingHandCursor);
	forgeImageBtn.setImages(false, true, false, i2, 1.0f, Colours::transparentWhite, i2, 1.0f, Colours::yellow, i2, 1.0f, Colours::yellow);
	forgeImageBtn.setMouseCursor(MouseCursor::PointingHandCursor);
	addAndMakeVisible(reforgeImageBtn);
	addAndMakeVisible(forgeImageBtn);

	reforgeImageBtn.addListener(this);
	forgeImageBtn.addListener(this);

	/*addAndMakeVisible(keyKnob);
	keyKnob.setLookAndFeel(&keyKnobLF);
	keyKnob.setSliderStyle(Slider::Rotary);
	keyKnob.setTextBoxStyle(Slider::TextBoxBelow, false, 50, 20);
	*///keyKnob.textbox
	//keyKnob.addListener(this);
	//keyKnob.value
	//keyKnob.setTextBoxStyle(Slider::NoTextBox, true, 0, 0);
	scaleLabel.setText("SCALE", NotificationType::dontSendNotification);
	scaleLabel.setJustificationType(Justification::centred);
	scaleLabel.setFont(Font("Avenir", 20.0f, 0));
	addAndMakeVisible(scaleLabel);

	keySelect.addItem("A", 1);
	keySelect.addItem("B Major Blues", 2);
	keySelect.setSelectedId(1);
	addAndMakeVisible(keySelect);

}

MelodySmithVSTAudioProcessorEditor::~MelodySmithVSTAudioProcessorEditor()
{
}

//==============================================================================
void MelodySmithVSTAudioProcessorEditor::paint (Graphics& g)
{
	// fill the whole window white
	//g.fillAll(Colour::fromString("#1a1a1a"));
	g.fillAll(Colours::black.brighter(0.2f));
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
	//tabbedCorpusComponent.setBounds(tempArea.removeFromTop(tempArea.getHeight() / 12));
	tabbedCorpusComponent.setBounds(tempArea);

	Rectangle<int> rightCol(area);
	int rightColHeight = rightCol.getHeight();
	reforgeImageBtn.setBounds(rightCol.removeFromTop(rightColHeight / 3));
	scaleLabel.setBounds(rightCol.removeFromTop(rightColHeight / 6));
	keySelect.setBounds(rightCol.removeFromTop(rightColHeight / 12).reduced(50, 10));
	rightCol.removeFromTop(rightColHeight / 12);
	forgeImageBtn.setBounds(rightCol);
	//txbtn.setBounds(rightCol);
	//corpusListBox.setBounds(tempArea);

}



void MelodySmithVSTAudioProcessorEditor::sliderValueChanged(Slider* slider)
{
	//processor.noteOnVel = midiVolume.getValue();
	if (slider == &keyKnob)
	{

	}
}

void MelodySmithVSTAudioProcessorEditor::buttonClicked(Button* btn)
{
	if (btn == &reforgeImageBtn)
	{
		String s = "";
	}
	else if (btn == &forgeImageBtn)
	{
		String clParamsStr = "c ";
		//Create master directory
		File f(File::getCurrentWorkingDirectory().getFullPathName() + "/artists");
		f.deleteRecursively();
		f.createDirectory();

		//For each artist
		for (int i = 0; i < artists_to_influences.size(); i++)
		{
			//create artist directory
			std::tuple<String, double> currArtistToInfluence(artists_to_influences[i]);
			String artistName = std::get<0>(currArtistToInfluence);
			double inf = std::get<1>(currArtistToInfluence);
			File artistFolder(f.getFullPathName() + "/" + artistName);
			artistFolder.createDirectory();

			//add to cl string
			clParamsStr += artistName + ":" + String(inf) + " ";
			
			//populate with artist files
			for (int j = 0; j < curr_artist_filename_tuples.size(); j++)
			{
				std::tuple<String, String> currFileArtist(curr_artist_filename_tuples[j]);
				String currArtistName = std::get<1>(currFileArtist);
				String fileName = std::get<0>(currFileArtist);

				if (currArtistName == artistName)
				{
					File tempF(fileName);
					File newF(artistFolder.getFullPathName() + "/file" + String(j) + ".mid");
					tempF.copyFileTo(newF);
				}
			}
		}

		//Call commandline melodysmith.jar
		int i;
		printf("Checking if processor is available...");
		if (system(NULL)) puts("Ok");
		else exit(EXIT_FAILURE);

		String clStr = "java -jar MelodySmith.jar artists " + clParamsStr;

		printf("Executing command DIR...\n");
		i = system(clStr.toStdString().c_str());
		printf("The value returned was: %d.\n", i);
	}
}

void MelodySmithVSTAudioProcessorEditor::setSizesAndColors()
{
	pluginWidth = 700;
	pluginHeight = 900;

	header1Colour = Colours::white;
	buttonBgColour = Colours::crimson,  buttonTextColour = Colours::black;
	
	pluginBgColour = Colour::fromString("#1a1a1a");

	divBgColour = Colours::darkgrey;
	tabSelectedColour = Colours::maroon;

	divTextColour = Colours::white.darker(0.1f);
	divBorderColour = Colours::azure;
}

void MelodySmithVSTAudioProcessorEditor::createCorpusPanel()
{

}

void MelodySmithVSTAudioProcessorEditor::createControlsPanel()
{

}
