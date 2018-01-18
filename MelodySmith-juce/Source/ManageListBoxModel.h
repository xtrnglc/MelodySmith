#pragma once
#include "../JuceLibraryCode/JuceHeader.h"
#include "ManageListBoxRow.h"

class ManageListBoxModel : public ListBox, private ListBoxModel
{
public:
	ManageListBoxModel();
	~ManageListBoxModel();
	int getNumRows() override;
	void paintListBoxItem(int rowNumber, Graphics& g, int width, int height, bool rowIsSelected);
	Component* refreshComponentForRow(int rowNumber, bool isRowSelected, Component *existingComponentToUpdate);
	void addSongs(Array<File> midi_files);
	void removeSong();
	void clearSongs();

	void setCurrArtistFilenameTuples(Array<std::tuple<String, String>> *curr_artist_filename_tuples_c);

	Array<std::tuple<String, String>> *curr_artist_filename_tuples = nullptr;

	StringArray fileNames;
};

