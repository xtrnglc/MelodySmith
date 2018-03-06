/*
  ==============================================================================

    InfluencesPanel.h
    Created: 16 Jan 2018 3:43:53pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#pragma once

#include "../JuceLibraryCode/JuceHeader.h"
#include "InfluencesListBoxModel.h"
#include "ManagePanel.h"


//==============================================================================
/*
*/
class InfluencesPanel    : public Component
{
public:
    InfluencesPanel(Array<std::tuple<String, String>>& artist_filename_tuples, Array<std::tuple<String, double>>& artists_to_influences_c, ManagePanel& managePanelc);
    ~InfluencesPanel();

    void paint (Graphics&) override;
    void resized() override;
	//void repaint(Graphics&) override;
	void visibilityChanged() override;

	Array<std::tuple<String, String>> *curr_artist_filename_tuples;
	Array<std::tuple<String, double>>* artists_to_influences;

	ManagePanel* managePanel;


	InfluencesListBoxModel influencesListBox;

private:
    JUCE_DECLARE_NON_COPYABLE_WITH_LEAK_DETECTOR (InfluencesPanel)
};
