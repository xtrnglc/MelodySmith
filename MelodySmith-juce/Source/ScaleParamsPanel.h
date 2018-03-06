/*
  ==============================================================================

    ScaleParamsPanel.h
    Created: 20 Feb 2018 9:15:49pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#pragma once

#include "../JuceLibraryCode/JuceHeader.h"
#include "MyKnobLF.h"

//==============================================================================
/*
*/
class ScaleParamsPanel    : public Component
{
public:
    ScaleParamsPanel();
    ~ScaleParamsPanel();

    void paint (Graphics&) override;
    void resized() override;

	ComboBox keySelect;
	Label scaleLabel;

	Label syncopationLabel, restAmntLabel;
	Slider syncopationSlider, restAmntSlider;

	//TextButton restTypeBtnDes, restTypeBtnCon;
	Label restTypeLabel;
	ComboBox restTypeSelect;

	Label restControls;
	MyKnobLF keyKnobLF;

private:
    JUCE_DECLARE_NON_COPYABLE_WITH_LEAK_DETECTOR (ScaleParamsPanel)
};
