/*
  ==============================================================================

    ManagePanel.h
    Created: 16 Jan 2018 3:30:43pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#pragma once

#include "../JuceLibraryCode/JuceHeader.h"
#include "ManageListBoxModel.h"
#include "ManageListBoxRow.h"
#include "InfluencesPanel.h"

//==============================================================================
/*
*/
class InfluencesPanel;
class ManagePanel    : public Component, public Button::Listener, public FileDragAndDropTarget
{
public:
	//class InfluencesPanel;
    ManagePanel(Array<std::tuple<String, String>>& artist_filename_tuples, InfluencesPanel& influencesPanelc);
    ~ManagePanel();

    void paint (Graphics&) override;
    void resized() override;
	ManageListBoxModel corpusListBox;
	TextButton addSongsBtn;
	TextButton saveSongsBtn;
	Array<std::tuple<String, String>> *curr_artist_filename_tuples;
	InfluencesPanel* influencesPanel;


	void buttonClicked(Button* button) override;
	bool isInterestedInFileDrag(const StringArray & files) override;
	void filesDropped(const StringArray & files, int x, int y) override;
	


private:
    JUCE_DECLARE_NON_COPYABLE_WITH_LEAK_DETECTOR (ManagePanel)
};
