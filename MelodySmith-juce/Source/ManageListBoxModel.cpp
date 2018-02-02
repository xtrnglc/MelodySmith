#include "ManageListBoxModel.h"

ManageListBoxModel::ManageListBoxModel()
{
	//addSongs();
	setModel(this);
}

ManageListBoxModel::~ManageListBoxModel()
{
}

int ManageListBoxModel::getNumRows()
{
	return fileNames.size();
}

void ManageListBoxModel::paintListBoxItem(int rowNumber, Graphics& g, int width, int height, bool rowIsSelected)
{
	g.fillAll(Colours::darkgrey);
	g.setColour(Colours::aliceblue);

}

Component* ManageListBoxModel::refreshComponentForRow(int rowNumber, bool isRowSelected, Component *existingComponentToUpdate)
{
	if (rowNumber < fileNames.size()) {
		String aName = "";
		if (curr_artist_filename_tuples != nullptr && curr_artist_filename_tuples->size() > rowNumber)
		{
			std::tuple<String, String> s = (*(curr_artist_filename_tuples))[rowNumber];
			aName = std::get<1>(s);
		}

		ManageListBoxRow* newListBoxRow = new ManageListBoxRow(fileNames[rowNumber].getFileName(), aName);
		return newListBoxRow;
	}
	else {
		return nullptr;
	}
}

void ManageListBoxModel::setCurrArtistFilenameTuples(Array<std::tuple<String, String>> *curr_artist_filename_tuples_c)
{
	curr_artist_filename_tuples = curr_artist_filename_tuples_c;
}

void ManageListBoxModel::addSongs(Array<File> midi_files)
{
	for (int i = 0; i < midi_files.size(); i++) 
	{
		fileNames.add(midi_files[i]);
	}
}

void ManageListBoxModel::removeSong()
{

}

void ManageListBoxModel::clearSongs()
{

}