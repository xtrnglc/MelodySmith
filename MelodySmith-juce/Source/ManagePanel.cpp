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
ManagePanel::ManagePanel(Array<std::tuple<String, String>>& artist_filename_tuples)
{
    // In your constructor, you should add any child components, and
    // initialise any special settings that your component needs.
	corpusListBox.setColour(ListBox::backgroundColourId, Colours::darkgrey);
	addAndMakeVisible(corpusListBox);

	addSongsBtn.setColour(TextButton::buttonColourId, Colours::red);
	addSongsBtn.setButtonText("Add Songs");
	addAndMakeVisible(addSongsBtn);
	addSongsBtn.addListener(this);

	saveSongsBtn.setColour(TextButton::buttonColourId, Colours::red);
	saveSongsBtn.setButtonText("Save");
	addAndMakeVisible(saveSongsBtn);
	saveSongsBtn.addListener(this);

	curr_artist_filename_tuples = &artist_filename_tuples;

	corpusListBox.setCurrArtistFilenameTuples(curr_artist_filename_tuples);
}

ManagePanel::~ManagePanel()
{
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
}

void ManagePanel::resized()
{
    // This method is where you should set the bounds of any child
    // components that your component contains..
	Rectangle<int> area(getLocalBounds());
	int areaHeight = area.getHeight();
	saveSongsBtn.setBounds(area.removeFromBottom(areaHeight / 12));
	addSongsBtn.setBounds(area.removeFromBottom(areaHeight / 12));
	corpusListBox.setBounds(area);

}

void ManagePanel::buttonClicked(Button* button)
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

