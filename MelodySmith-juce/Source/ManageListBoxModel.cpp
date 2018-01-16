#include "ManageListBoxModel.h"
#include "ManageListBoxRow.h"


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

	Rectangle<int> totalArea(0, 0, width, height);
	Rectangle<int> leftCol(totalArea.removeFromLeft(totalArea.getWidth() / 2));
	g.drawFittedText(fileNames[rowNumber], leftCol, Justification::centred, 1);

	//Rectangle<int> rightCol(totalArea);
	//TextEditor artistName;
	//artistName.setBounds(rightCol);
	//addAndMakeVisible(artistName);
	//g.
	//Label l;
	//l.setBounds(rightCol);
	//l.setColour(Label::backgroundColourId, Colours::red);
	//l.setSize(rightCol.wi)
	//addAndMakeVisible(l);

}

Component* ManageListBoxModel::refreshComponentForRow(int rowNumber, bool isRowSelected, Component *existingComponentToUpdate)
{
	if (rowNumber < fileNames.size()) {
		//ManageListBoxRow newListBoxRow; 
		//newListBoxRow.setFileName(fileNames[rowNumber]);
		//return newListBoxRow;
		return existingComponentToUpdate;
	}
	else {
		return nullptr;
	}
}

void ManageListBoxModel::addSongs(Array<File> midi_files)
{
	for (int i = 0; i < midi_files.size(); i++) 
	{
		fileNames.add(midi_files[i].getFileName());
	}
}

void ManageListBoxModel::removeSong()
{

}

void ManageListBoxModel::clearSongs()
{

}