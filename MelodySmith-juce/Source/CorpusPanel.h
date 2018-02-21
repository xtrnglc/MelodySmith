/*
  ==============================================================================

    CorpusPanel.h
    Created: 20 Feb 2018 10:10:02pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#pragma once

#include "../JuceLibraryCode/JuceHeader.h"
#include "ManagePanel.h"
#include "InfluencesPanel.h"

//==============================================================================
/*
*/
class CorpusPanel    : public Component
{
public:
    CorpusPanel(Array<std::tuple<String, String>>& artist_filename_tuples, Array<std::tuple<String, double>>& artists_to_influences_c);
    ~CorpusPanel();

    void paint (Graphics&) override;
    void resized() override;

	TabbedComponent tabbedCorpusComponent;
	ManagePanel managePanel;
	InfluencesPanel influencesPanel;

	Array<std::tuple<String, String>> *curr_artist_filename_tuples;
	Array<std::tuple<String, double>>* artists_to_influences;

private:
    JUCE_DECLARE_NON_COPYABLE_WITH_LEAK_DETECTOR (CorpusPanel)
};
