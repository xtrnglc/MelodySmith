/*
  ==============================================================================

    ForgeTask.h
    Created: 5 Mar 2018 7:09:43pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#pragma once

#include "../JuceLibraryCode/JuceHeader.h"


//==============================================================================
/*
*/
class ForgeTask    : public ThreadWithProgressWindow
{
public:
    ForgeTask();
    ~ForgeTask();

    void run() override;

	String javaExePath, clStr;

private:
    JUCE_DECLARE_NON_COPYABLE_WITH_LEAK_DETECTOR (ForgeTask)
};
