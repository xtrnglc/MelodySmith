/*
  ==============================================================================

    InfluencesPanel.cpp
    Created: 16 Jan 2018 3:43:53pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#include "../JuceLibraryCode/JuceHeader.h"
#include "InfluencesPanel.h"

//==============================================================================
InfluencesPanel::InfluencesPanel(Array<std::tuple<String, String>>& artist_filename_tuples, Array<std::tuple<String, double>>& artists_to_influences_c)
{
    // In your constructor, you should add any child components, and
    // initialise any special settings that your component needs.

	influencesListBox.setColour(ListBox::backgroundColourId, Colours::red.brighter(0.6f));
	addAndMakeVisible(influencesListBox);

	curr_artist_filename_tuples = &artist_filename_tuples;
	artists_to_influences = &artists_to_influences_c;

	influencesListBox.setArtistsAndFiles(curr_artist_filename_tuples);
	influencesListBox.setArtistsToInfluences(artists_to_influences);
}

InfluencesPanel::~InfluencesPanel()
{
}

void InfluencesPanel::paint (Graphics& g)
{

}

void InfluencesPanel::resized()
{
	Rectangle<int> area(getLocalBounds());
	influencesListBox.setBounds(area);
}

void InfluencesPanel::visibilityChanged()
{
	influencesListBox.generateArtistFileMap();
	influencesListBox.updateContent();
}
