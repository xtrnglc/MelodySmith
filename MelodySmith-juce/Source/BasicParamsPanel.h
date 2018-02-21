/*
  ==============================================================================

    BasicParamsPanel.h
    Created: 20 Feb 2018 9:15:14pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#pragma once

#include "../JuceLibraryCode/JuceHeader.h"

//==============================================================================
/*
*/
class BasicParamsPanel    : public Component
{
public:
    BasicParamsPanel();
    ~BasicParamsPanel();

    void paint (Graphics&) override;
    void resized() override;

	Label intervalWeightLabel, durationWeightLabel;
	Slider invervalWeightSlider, durationWeightSlider;

private:
    JUCE_DECLARE_NON_COPYABLE_WITH_LEAK_DETECTOR (BasicParamsPanel)
};
