#include "ManageListBoxRow.h"

ManageListBoxRow::ManageListBoxRow(String fName, String aName)
{
	fileName = fName;

	fileNameLabel.setColour(Label::textColourId, Colours::white);
	fileNameLabel.setText(fileName, NotificationType::dontSendNotification);
	fileNameLabel.setJustificationType(Justification::centred);
	fileNameLabel.setFont(Font("Avenir", 20.0f, 0));

	addAndMakeVisible(fileNameLabel);

	artistName.setTextToShowWhenEmpty("Artist Name", Colours::lightgrey);
	artistName.setText(aName, false);
	addAndMakeVisible(artistName);
}

ManageListBoxRow::~ManageListBoxRow()
{

}

void ManageListBoxRow::setFileName(String fName)
{
	//fileName = fName;
}

void ManageListBoxRow::paint(Graphics &g)
{
	//Rectangle<int> totalArea(0, 0, width, height);
	//Rectangle<int> leftCol(totalArea.removeFromLeft(totalArea.getWidth() / 2));
	//g.drawFittedText(fileName, getLocalBounds(), Justification::centred, 1);
	g.fillAll(Colours::black.brighter(0.2f));

}

void ManageListBoxRow::resized()
{
	Rectangle<int> area(getLocalBounds());

	Rectangle<int> totalArea = area;
	Rectangle<int> leftCol(totalArea.removeFromLeft(totalArea.getWidth() / 2));
	fileNameLabel.setBounds(leftCol);

	Rectangle<int> rightCol(totalArea);
	artistName.setBounds(rightCol);
}