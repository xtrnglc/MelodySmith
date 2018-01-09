#pragma once
#include "../JuceLibraryCode/JuceHeader.h"

class ManageListBoxModel : public ListBox, private ListBoxModel
{
public:
	ManageListBoxModel();
	~ManageListBoxModel();
	int getNumRows() override;
	void paintListBoxItem(int rowNumber, Graphics& g, int width, int height, bool rowIsSelected) override;
	Component* refreshComponentForRow(int rowNumber, bool isRowSelected, Component *existingComponentToUpdate);
	void addSongs(Array<File> midi_files);
	void removeSong();
	void clearSongs();

	StringArray fileNames;
};

