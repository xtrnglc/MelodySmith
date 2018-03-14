		/*
  ==============================================================================

    InfluencesListBoxModel.cpp
    Created: 16 Jan 2018 3:44:14pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#include "../JuceLibraryCode/JuceHeader.h"
#include "InfluencesListBoxModel.h"
#include "InfluencesListBoxRow.h"

//==============================================================================
InfluencesListBoxModel::InfluencesListBoxModel()
{
    // In your constructor, you should add any child components, and
    // initialise any special settings that your component needs.

	setModel(this);

}

InfluencesListBoxModel::~InfluencesListBoxModel()
{
}

int InfluencesListBoxModel::getNumRows()
{
	try {
		if (artists_to_influences == nullptr)
			return 0;
		return artists_to_influences->size();
	}
	catch (...)
	{
		return 0;
	}
}

void InfluencesListBoxModel::setArtistsAndFiles(Array<std::tuple<String, String>> *curr)
{
	curr_artist_filename_tuples = curr;
}

void InfluencesListBoxModel::generateArtistFileMap()
{
	if (curr_artist_filename_tuples == nullptr)
		return;

	artists_to_influences->clear();

	for (int i = 0; i < curr_artist_filename_tuples->size(); i++)
	{
		//If artist does not exist create a mapping
		std::tuple<String, String> currFileArtist ((*curr_artist_filename_tuples)[i]);
		String artistName = std::get<1>(currFileArtist);
		String fileName = std::get<0>(currFileArtist);

		if (artists_to_influences != nullptr)
		{
			bool artistExists = false;
			for (int j = 0; j < artists_to_influences->size(); j++)
			{
				std::tuple<String, double> currArtistToInfluence((*artists_to_influences)[j]);
				if (std::get<0>(currArtistToInfluence) == artistName)
				{
					artistExists = true;
					break;
				}
			}

			if (!artistExists)
			{
				artists_to_influences->add(std::make_tuple(artistName, 0));
			}
		}
	}
}


void InfluencesListBoxModel::paintListBoxItem(int rowNumber, Graphics& g, int width, int height, bool rowIsSelected)
{
		g.fillAll(Colours::darkgrey);
		g.setColour(Colours::aliceblue);
}

Component* InfluencesListBoxModel::refreshComponentForRow(int rowNumber, bool isRowSelected, Component *existingComponentToUpdate)
{
	if (rowNumber < getNumRows()) {
		std::tuple<String, double> currArtistToInfluence((*artists_to_influences)[rowNumber]);
		String artistName = std::get<0>(currArtistToInfluence);
		InfluencesListBoxRow* newListBoxRow = new InfluencesListBoxRow(artistName, artists_to_influences, rowNumber);
		return newListBoxRow;
	}
	else {
		return nullptr;
	}
}

void InfluencesListBoxModel::setArtistsToInfluences(Array<std::tuple<String, double>>* artists_to_influences_c)
{
	artists_to_influences = artists_to_influences_c;
}
