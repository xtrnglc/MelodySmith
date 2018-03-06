/*
  ==============================================================================

    CorpusPanel.cpp
    Created: 20 Feb 2018 10:10:02pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#include "../JuceLibraryCode/JuceHeader.h"
#include "CorpusPanel.h"

//==============================================================================
CorpusPanel::CorpusPanel()/*(Array<std::tuple<String, String>>& artist_filename_tuples, Array<std::tuple<String, double>>& artists_to_influences_c: tabbedCorpusComponent(TabbedButtonBar::Orientation::TabsAtLeft), managePanel(*curr_artist_filename_tuples),
influencesPanel(*curr_artist_filename_tuples, *artists_to_influences)*/
{
    // In your constructor, you should add any child components, and
    // initialise any special settings that your component needs.

	/*curr_artist_filename_tuples = &artist_filename_tuples;
	artists_to_influences = &artists_to_influences_c;

	tabbedCorpusComponent.setOrientation(TabbedButtonBar::Orientation::TabsAtTop);
	tabbedCorpusComponent.addTab("Manage", Colours::red, &managePanel, false);
	tabbedCorpusComponent.addTab("Influences", Colours::red, &influencesPanel, false);
	//tabbedCorpusComponent.setTabBackgroundColour(0, Colours::aliceblue);
	//tabbedCorpusComponent.setTabBackgroundColour()
	tabbedCorpusComponent.setColour(TabbedComponent::backgroundColourId, Colours::red.brighter(0.4f));
	//tabbedCorpusComponent.setColour()
	addAndMakeVisible(tabbedCorpusComponent);

	*/
}

CorpusPanel::~CorpusPanel()
{
}

void CorpusPanel::paint (Graphics& g)
{
    /* This demo code just fills the component's background and
       draws some placeholder text to get you started.

       You should replace everything in this method with your own
       drawing code..
    */

    g.fillAll (getLookAndFeel().findColour (ResizableWindow::backgroundColourId));   // clear the background
	/*
    g.setColour (Colours::grey);
    g.drawRect (getLocalBounds(), 1);   // draw an outline around the component

    g.setColour (Colours::white);
    g.setFont (14.0f);
    g.drawText ("CorpusPanel", getLocalBounds(),
                Justification::centred, true);   // draw some placeholder text*/
}

void CorpusPanel::resized()
{
    // This method is where you should set the bounds of any child
    // components that your component contains..
	Rectangle<int> area = getLocalBounds();
	//tabbedCorpusComponent.setBounds(area);

}
