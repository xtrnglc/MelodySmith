/*
  ==============================================================================

    AdvancedParamsPanel.h
    Created: 20 Feb 2018 9:15:31pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#pragma once

#include "../JuceLibraryCode/JuceHeader.h"

//==============================================================================
/*
*/
class AdvancedParamsPanel    : public Component
{
public:
    AdvancedParamsPanel();
    ~AdvancedParamsPanel();

    void paint (Graphics&) override;
    void resized() override;

	Label nGramLengthLabel, numberOfComparisonsLabel;
	Slider nGramLengthSlider, numberOfComparisonsSlider;

private:
    JUCE_DECLARE_NON_COPYABLE_WITH_LEAK_DETECTOR (AdvancedParamsPanel)
};
