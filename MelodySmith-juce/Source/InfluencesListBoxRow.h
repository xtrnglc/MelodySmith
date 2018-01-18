/*
  ==============================================================================

    InfluencesListBoxRow.h
    Created: 16 Jan 2018 3:44:27pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#pragma once
#include "../JuceLibraryCode/JuceHeader.h"

class InfluencesListBoxRow : public Component, public Slider::Listener
{
public:
	InfluencesListBoxRow(String aName, Array<std::tuple<String, double>>* artists_to_influences_c, int i);
	~InfluencesListBoxRow();

	void paint(Graphics&) override;
	void resized() override;
	void sliderValueChanged(Slider* slider) override;

	String artistName;
	Label artistNameLabel;
	Slider influenceSlider;
	Array<std::tuple<String, double>>* artists_to_influences = nullptr;
	int index;

private:
	JUCE_DECLARE_NON_COPYABLE_WITH_LEAK_DETECTOR(InfluencesListBoxRow)
};
