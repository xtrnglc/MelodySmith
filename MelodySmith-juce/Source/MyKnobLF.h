#pragma once

#include "../JuceLibraryCode/JuceHeader.h"


class MyKnobLF : public LookAndFeel_V3
{
public:
	MyKnobLF();
	void drawRotarySlider(Graphics& g, int x, int y, int width, int height, float sliderPos,
		const float rotaryStartAngle, const float rotaryEndAngle, Slider& slider) override;

	bool isThreeColumn = true;
};