/*
  ==============================================================================

    ManagePanel.cpp
    Created: 16 Jan 2018 3:30:43pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#include "../JuceLibraryCode/JuceHeader.h"
#include "ManagePanel.h"

//==============================================================================
ManagePanel::ManagePanel(Array<std::tuple<String, String>>& artist_filename_tuples, InfluencesPanel& influencesPanelc)
{
    // In your constructor, you should add any child components, and
    // initialise any special settings that your component needs.
	corpusListBox.setColour(ListBox::backgroundColourId, Colours::deepskyblue.darker(2.0f));
	addAndMakeVisible(corpusListBox);

	addSongsBtn.setColour(TextButton::buttonColourId, Colours::deepskyblue);
	addSongsBtn.setColour(TextButton::textColourOffId, Colours::black.brighter(0.2f));
	addSongsBtn.setButtonText("Add Songs");
	addAndMakeVisible(addSongsBtn);
	addSongsBtn.addListener(this);

	/*saveSongsBtn.setColour(TextButton::buttonColourId, Colours::deepskyblue);
	saveSongsBtn.setButtonText("Save");
	addAndMakeVisible(saveSongsBtn);*/
	saveSongsBtn.addListener(this);

	curr_artist_filename_tuples = &artist_filename_tuples;

	corpusListBox.setCurrArtistFilenameTuples(curr_artist_filename_tuples);

	//File
	influencesPanel = &influencesPanelc;
	
}

ManagePanel::~ManagePanel()
{
}

bool ManagePanel::isInterestedInFileDrag(const StringArray & files)
{
	return true;
}

void ManagePanel::filesDropped(const StringArray & files, int x, int y)
{
	Array<File> midi_files;
	for (int i = 0; i < files.size(); i++)
	{
		File f(files[i].toStdString());
		midi_files.add(f);
	}
	corpusListBox.addSongs(midi_files);
	corpusListBox.updateContent();
	
}


void ManagePanel::paint (Graphics& g)
{
    /* This demo code just fills the component's background and
       draws some placeholder text to get you started.

       You should replace everything in this method with your own
       drawing code..
    */

    //g.fillAll (getLookAndFeel().findColour (ResizableWindow::backgroundColourId));   // clear the background

    //g.setColour (Colours::grey);
    //g.drawRect (getLocalBounds(), 1);   // draw an outline around the component

    //g.setColour (Colours::white);
    //g.setFont (14.0f);
    //g.drawText ("ManagePanel", getLocalBounds(),
    //            Justification::centred, true);   // draw some placeholder text

	/*g.setColour(Colours::white);
	g.drawRect(getLocalBounds(), 3);   // draw an outline around the component*/
}

void ManagePanel::resized()
{
    // This method is where you should set the bounds of any child
    // components that your component contains..
	Rectangle<int> area(getLocalBounds());
	//area = area.reduced(10);
	int areaHeight = area.getHeight();
	//saveSongsBtn.setBounds(area.removeFromBottom(areaHeight / 12));
	addSongsBtn.setBounds(area.removeFromBottom(areaHeight / 12));
	corpusListBox.setBounds(area);

}

void ManagePanel::buttonClicked(Button* button)
{
	if (button == &addSongsBtn)
	{
		File f(/*"C:\\Users\\Daniel Mattheiss\\Downloads\\corpus"*/ File::getSpecialLocation(File::userHomeDirectory));
		FileChooser myChooser("Please select the midi files you want to load...",
			f);
		if (myChooser.browseForMultipleFilesOrDirectories())
		{
			Array<File> midi_files = myChooser.getResults();
			corpusListBox.addSongs(midi_files);
			corpusListBox.updateContent();
			influencesPanel->visibilityChanged();
		}
	}
	else if (button == &saveSongsBtn)
	{
		curr_artist_filename_tuples->clear();
		for (int i = 0; i < corpusListBox.getNumRows(); i++)
		{
			ManageListBoxRow *currListBoxRow = dynamic_cast<ManageListBoxRow*>(corpusListBox.getComponentForRowNumber(i));
			std::tuple<String, String> s(corpusListBox.fileNames[i].getFullPathName(), currListBoxRow->artistName.getText());

			curr_artist_filename_tuples->add(s);
		}
	}
}

