/*
  ==============================================================================

    MyKnobLF.cpp
    Created: 25 Jan 2018 1:22:37pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/
#include "MyKnobLF.h"

MyKnobLF::MyKnobLF()
{

}

void MyKnobLF::drawRotarySlider(Graphics& g, int x, int y, int width, int height, float sliderPos,
	const float rotaryStartAngle, const float rotaryEndAngle, Slider& slider)
{
	const float radius = jmin(width / 2, height / 2) - 4.0f;
	const float centreX = x + width * 0.5f;
	const float centreY = y + height * 0.5f;
	const float rx = centreX - radius;
	const float ry = centreY - radius;
	const float rw = radius * 2.0f;
	const float angle = rotaryStartAngle + sliderPos * (rotaryEndAngle - rotaryStartAngle);

	// fill
	g.setColour(Colours::black.brighter(0.2f));
	g.fillEllipse(rx, ry, rw, rw);

	// outline
	g.setColour(Colours::lightblue);
	//g.drawEllipse(rx, ry, rw, rw, 2.0f);

	Path knobArc;
	knobArc.addArc(rx, ry, rw, rw, 3.14, angle, true);
	g.strokePath(knobArc, PathStrokeType(2.0f));

	Path p;
	const float pointerLength = radius;
	const float pointerThickness = 2.0f;
	p.addRectangle(-pointerThickness * 0.5f, -radius, pointerThickness, pointerLength);
	p.applyTransform(AffineTransform::rotation(angle).translated(centreX, centreY));

	// pointer
	g.setColour(Colours::white);
	g.fillPath(p);

	//Text labels for knob
	Range<double> sliderRange = slider.getRange();
	g.setColour(Colours::white);
	g.setFont(Font("Cooper", 16.0f, 0));
	//StringArray sa = Font::findAllTypefaceNames();

	double offset = 0.35;
	if (isThreeColumn) offset = 0.25;

	Rectangle<int> textRect(0, height - (height * 0.1), width - (width * (1 - offset)), height - (height * 0.9));
	g.drawText(String(sliderRange.getStart()), textRect, Justification::bottomRight, true);

	Rectangle<int> textRect2(width - (width * offset), height - (height * 0.1), width - (width * (1 - offset)), height - (height * 0.9));
	g.drawText(String(sliderRange.getEnd()), textRect2, Justification::bottomLeft, true);
}
