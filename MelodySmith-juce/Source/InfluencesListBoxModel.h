/*
  ==============================================================================

    InfluencesListBoxModel.h
    Created: 16 Jan 2018 3:44:14pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#pragma once

#include "../JuceLibraryCode/JuceHeader.h"
#include <map>

//==============================================================================
/*
*/
class InfluencesListBoxModel : public ListBox, private ListBoxModel
{
public:
	InfluencesListBoxModel();
	~InfluencesListBoxModel();
	int getNumRows() override;
	void paintListBoxItem(int rowNumber, Graphics& g, int width, int height, bool rowIsSelected) override;
	Component* refreshComponentForRow(int rowNumber, bool isRowSelected, Component *existingComponentToUpdate);
	void generateArtistFileMap();
	void setArtistsAndFiles(Array<std::tuple<String, String>> *curr);
	void setArtistsToInfluences(Array<std::tuple<String, double>>* artists_to_influences_c);

	std::map<String, Array<String>> artistFileMap;
	Array<std::tuple<String, String>> *curr_artist_filename_tuples = nullptr;
	Array<std::tuple<String, double>>* artists_to_influences = nullptr;

};
