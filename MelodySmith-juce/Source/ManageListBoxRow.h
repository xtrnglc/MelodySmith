#pragma once
#include "../JuceLibraryCode/JuceHeader.h"

class ManageListBoxRow : public Component
{
public:
	ManageListBoxRow(String fName, String aName);
	~ManageListBoxRow();

	void paint(Graphics&) override;
	void resized() override;

	void setFileName(String fName);

	String fileName;
	Label fileNameLabel;
	TextEditor artistName;

private:
	JUCE_DECLARE_NON_COPYABLE_WITH_LEAK_DETECTOR(ManageListBoxRow)
};