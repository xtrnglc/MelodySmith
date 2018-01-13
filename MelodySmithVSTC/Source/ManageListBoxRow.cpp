#include "ManageListBoxRow.h"

ManageListBoxRow::ManageListBoxRow()
{

}

ManageListBoxRow::~ManageListBoxRow()
{

}

void ManageListBoxRow::setFileName(String fName)
{
	fileName = fName;
}

void ManageListBoxRow::paint(Graphics &g)
{
	//Rectangle<int> totalArea(0, 0, width, height);
	//Rectangle<int> leftCol(totalArea.removeFromLeft(totalArea.getWidth() / 2));
	g.drawFittedText(fileName, getLocalBounds(), Justification::centred, 1);
}

void ManageListBoxRow::resized()
{

}