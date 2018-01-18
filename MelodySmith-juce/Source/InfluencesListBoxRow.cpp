/*
  ==============================================================================

    InfluencesListBoxRow.cpp
    Created: 16 Jan 2018 3:44:27pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#include "InfluencesListBoxRow.h"

InfluencesListBoxRow::InfluencesListBoxRow(String aName, Array<std::tuple<String, double>>* artists_to_influences_c, int i)
{
	artistName = aName;
	artists_to_influences = artists_to_influences_c;
	index = i;
	std::tuple<String, double> currArtistToInfluence((*artists_to_influences)[index]);

	artistNameLabel.setColour(Label::textColourId, Colours::aqua);
	artistNameLabel.setText(artistName, NotificationType::dontSendNotification);
	artistNameLabel.setJustificationType(Justification::centred);
	artistNameLabel.setFont(Font("Avenir", 20.0f, 0));

	addAndMakeVisible(artistNameLabel);

	influenceSlider.setRange(0, 100);
	addAndMakeVisible(influenceSlider);
	influenceSlider.addListener(this);
	influenceSlider.setValue(std::get<1>(currArtistToInfluence));

}

InfluencesListBoxRow::~InfluencesListBoxRow()
{

}

void InfluencesListBoxRow::paint(Graphics &g)
{
	//Rectangle<int> totalArea(0, 0, width, height);
	//Rectangle<int> leftCol(totalArea.removeFromLeft(totalArea.getWidth() / 2));
	//g.drawFittedText(fileName, getLocalBounds(), Justification::centred, 1);
	g.fillAll(Colours::black);
}

void InfluencesListBoxRow::resized()
{
	Rectangle<int> area(getLocalBounds());

	Rectangle<int> totalArea = area;
	Rectangle<int> leftCol(totalArea.removeFromLeft(totalArea.getWidth() / 2));
	artistNameLabel.setBounds(leftCol);

	influenceSlider.setBounds(totalArea);
}

void InfluencesListBoxRow::sliderValueChanged(Slider* slider)
{
	//influenceSlider.setValue(influenceSlider.getValue(), dontSendNotification);
	double x = influenceSlider.getValue();
	artists_to_influences->remove(index);
	artists_to_influences->insert(index, std::make_tuple(artistName, x));
	//curr_inf = &x;
}