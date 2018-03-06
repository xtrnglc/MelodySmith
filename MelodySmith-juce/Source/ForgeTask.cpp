/*
  ==============================================================================

    ForgeTask.cpp
    Created: 5 Mar 2018 7:09:43pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#include "../JuceLibraryCode/JuceHeader.h"
#include "ForgeTask.h"

//==============================================================================
ForgeTask::ForgeTask() : ThreadWithProgressWindow("busy...", true, true)
{
    // In your constructor, you should add any child components, and
    // initialise any special settings that your component needs.

}

ForgeTask::~ForgeTask()
{
}

void ForgeTask::run()
{

}
