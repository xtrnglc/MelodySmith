/*
  ==============================================================================

    This file was auto-generated!

    It contains the basic framework code for a JUCE plugin editor.

  ==============================================================================
*/

#include "PluginProcessor.h"
#include "PluginEditor.h"



//==============================================================================
MelodySmithVSTAudioProcessorEditor::MelodySmithVSTAudioProcessorEditor (MelodySmithVSTAudioProcessor& p)
    : AudioProcessorEditor (&p), processor (p), tabbedCorpusComponent(TabbedButtonBar::Orientation::TabsAtLeft), managePanel(curr_artist_filename_tuples),
	influencesPanel(curr_artist_filename_tuples, artists_to_influences)
{
	audioProcessor = &p;
	melodysmithDirPath = "C:\\Program Files\\MelodySmith\\";
	//String windowsMelodysmithDirPath = "C:/Users/Daniel Mattheiss/Documents/MelodySmith/MelodySmithVST/MelodySmith/MelodySmith - juce/Builds/VisualStudio2017/";


    // Make sure that before the constructor has finished, you've set the
    // editor's size to whatever you need it to be.
	setSizesAndColors();
    setSize (pluginHeight, pluginWidth);
	//setResizable(true, false);

	corpusHeader.setColour(Label::textColourId, header1Colour);
	corpusHeader.setText("Corpus", NotificationType::dontSendNotification);
	corpusHeader.setJustificationType(Justification::centred);
	//corpusHeader.setFont(Font("Avenir", 20.0f, 0));
	addAndMakeVisible(corpusHeader);

	tabbedCorpusComponent.setOrientation(TabbedButtonBar::Orientation::TabsAtTop);
	tabbedCorpusComponent.addTab("Manage", Colours::red, &managePanel, false);
	tabbedCorpusComponent.addTab("Influences", Colours::red, &influencesPanel, false);
	//tabbedCorpusComponent.setTabBackgroundColour(0, Colours::aliceblue);
	//tabbedCorpusComponent.setTabBackgroundColour()
	tabbedCorpusComponent.setColour(TabbedComponent::backgroundColourId, Colours::red.brighter(0.4f));
	//tabbedCorpusComponent.setColour()
	addAndMakeVisible(tabbedCorpusComponent);

	//File fa = File(windowsMelodysmithDirPath);
	//int z = fa.getSize();
	//File f1 = fa.getChildFile("anvil_recast2.png");
	////File f1 = File(windowsMelodysmithDirPath + "anvil_recast2.png");
	//File f2 = File(melodysmithDirPath + "forge_fire2.png");
	//int x = f2.getSize();
	//int y = f1.getSize();

	//Image i1 = ImageFileFormat::loadFrom(f1);
	//Image i2 = ImageFileFormat::loadFrom(f2); 
	//reforgeImageBtn.setImages(false, true, false, i1, 1.0f, Colours::transparentWhite, i1, 1.0f, Colours::blue, i1, 1.0f, Colours::blue);
	//reforgeImageBtn.setMouseCursor(MouseCursor::PointingHandCursor);
	//forgeImageBtn.setImages(false, true, false, i2, 1.0f, Colours::transparentWhite, i2, 1.0f, Colours::yellow, i2, 1.0f, Colours::yellow);
	//forgeImageBtn.setMouseCursor(MouseCursor::PointingHandCursor);
	//addAndMakeVisible(reforgeImageBtn);
	//addAndMakeVisible(forgeImageBtn);

	////reforgeImageBtn.addListener(this);
	//forgeImageBtn.addListener(this);

	/*addAndMakeVisible(keyKnob);
	keyKnob.setLookAndFeel(&keyKnobLF);
	keyKnob.setSliderStyle(Slider::Rotary);
	keyKnob.setTextBoxStyle(Slider::TextBoxBelow, false, 50, 20);
	*///keyKnob.textbox
	//keyKnob.addListener(this);
	//keyKnob.value
	//keyKnob.setTextBoxStyle(Slider::NoTextBox, true, 0, 0);

	reforgeImageBtn.setButtonText("Recast");
	reforgeImageBtn.setColour(TextButton::buttonColourId, Colours::blue);
	reforgeImageBtn.setColour(TextButton::textColourOffId, Colours::white);
	reforgeImageBtn.addListener(this);
	addAndMakeVisible(reforgeImageBtn);

	forgeImageBtn.setButtonText("Forge");
	forgeImageBtn.setColour(TextButton::buttonColourId, Colours::orange);
	forgeImageBtn.setColour(TextButton::textColourOffId, Colours::white);
	forgeImageBtn.addListener(this);
	addAndMakeVisible(forgeImageBtn);
	

	addAndMakeVisible(invervalWeightSlider);
	invervalWeightSlider.setTextBoxStyle(Slider::TextBoxRight, false, 50, 20);
	invervalWeightSlider.setRange(0, 10, 1.0);
	//Slider::setSliderSnapsToMousePosition


	addAndMakeVisible(durationWeightSlider);
	durationWeightSlider.setRange(0, 10, 1.0);
	addAndMakeVisible(nGramLengthSlider);
	nGramLengthSlider.setRange(2, 10, 1.0);
	addAndMakeVisible(numberOfComparisonsSlider);
	numberOfComparisonsSlider.setRange(0, 9, 1.0);
	durationWeightSlider.setTextBoxStyle(Slider::TextBoxRight, false, 50, 20);
	nGramLengthSlider.setTextBoxStyle(Slider::TextBoxRight, false, 50, 20);
	numberOfComparisonsSlider.setTextBoxStyle(Slider::TextBoxRight, false, 50, 20);

	intervalWeightLabel.setText("Interval", NotificationType::dontSendNotification);
	intervalWeightLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(intervalWeightLabel);

	durationWeightLabel.setText("Duration", NotificationType::dontSendNotification);
	durationWeightLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(durationWeightLabel);

	nGramLengthLabel.setText("n-Gram Size", NotificationType::dontSendNotification);
	nGramLengthLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(nGramLengthLabel);

	numberOfComparisonsLabel.setText("Number of Comparisons", NotificationType::dontSendNotification);
	numberOfComparisonsLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(numberOfComparisonsLabel);

	scaleLabel.setText("Scale", NotificationType::dontSendNotification);
	scaleLabel.setJustificationType(Justification::centred);
	//scaleLabel.setFont(Font("Avenir", 20.0f, 0));
	//Font f = scaleLabel.getFont();	
	addAndMakeVisible(scaleLabel);

	keySelect.addItem("c Gypsy", 1);
	keySelect.addItem("a Minor", 2);
	keySelect.addItem("D Blues", 3);
	keySelect.addItem("f Harmonic", 4);
	keySelect.addItem("G Chromatic", 5);
	keySelect.addItem("aMinor", 6);

	keySelect.setSelectedId(1);
	addAndMakeVisible(keySelect);

	exportBtn.setButtonText("Choose Save Folder");
	exportBtn.setColour(TextButton::buttonColourId, Colours::red);
	exportBtn.setColour(TextButton::textColourOffId, Colours::white);
	exportBtn.addListener(this);
	addAndMakeVisible(exportBtn);

	exportFolder = File(melodysmithDirPath);
}

MelodySmithVSTAudioProcessorEditor::~MelodySmithVSTAudioProcessorEditor()
{
}

//==============================================================================
void MelodySmithVSTAudioProcessorEditor::paint (Graphics& g)
{
	// fill the whole window white
	g.fillAll(Colours::black.brighter(0.2f));

	// set the current drawing colour to black
	//g.setColour(Colours::white);

	g.setFont(Font("Avenir", 20.0f, 0));
}

void MelodySmithVSTAudioProcessorEditor::resized()
{
    // This is generally where you'll want to lay out the positions of any
    // subcomponents in your editor..

	// sets the position and size of the slider with arguments (x, y, width, height)
	Rectangle<int> area(getLocalBounds());

	Rectangle<int> leftCol(area.removeFromLeft(area.getWidth() / 2));
	
	float fontHeight = corpusHeader.getFont().getHeight() + 20;
	Rectangle<int> tempArea(leftCol.removeFromTop(fontHeight));
	corpusHeader.setBounds(tempArea);

	tempArea = Rectangle<int>(leftCol.reduced(15, 10));
	//tabbedCorpusComponent.setBounds(tempArea.removeFromTop(tempArea.getHeight() / 12));
	tabbedCorpusComponent.setBounds(tempArea);

	Rectangle<int> rightCol(area);
	int rightColHeight = rightCol.getHeight();
	int rightColWidth = rightCol.getWidth();

	//reforgeImageBtn.setBounds(rightCol.removeFromTop(rightColHeight / 3));
	Rectangle<int> firstRow = rightCol.removeFromTop(rightColHeight / 4);
	Rectangle<int> secondRow = rightCol.removeFromTop(rightColHeight / 4);
	Rectangle<int> thirdRow = rightCol.removeFromTop(rightColHeight / 4);
	Rectangle<int> fourthRow = rightCol.removeFromTop(rightColHeight / 4);

	{
		Rectangle<int> col = firstRow.removeFromLeft(rightCol.getWidth() / 2);
		int heightPartition = col.getHeight() / 4;
		intervalWeightLabel.setBounds(col.removeFromTop(heightPartition));
		invervalWeightSlider.setBounds(col.removeFromTop(heightPartition).reduced(10, 0));
		col = firstRow;
		durationWeightLabel.setBounds(col.removeFromTop(heightPartition));
		durationWeightSlider.setBounds(col.removeFromTop(heightPartition).reduced(10, 0));
	}

	{
		Rectangle<int> col = secondRow.removeFromLeft(rightCol.getWidth() / 2);
		int heightPartition = col.getHeight() / 4;
		nGramLengthLabel.setBounds(col.removeFromTop(heightPartition));
		nGramLengthSlider.setBounds(col.removeFromTop(heightPartition).reduced(10, 0));
		col = secondRow;
		numberOfComparisonsLabel.setBounds(col.removeFromTop(heightPartition));
		numberOfComparisonsSlider.setBounds(col.removeFromTop(heightPartition).reduced(10, 0));
	}

	int heightPartition = thirdRow.getHeight() / 4;
	scaleLabel.setBounds(thirdRow.removeFromTop(heightPartition));
	keySelect.setBounds(thirdRow.removeFromTop(heightPartition).reduced(50, 0));

	//rightCol.removeFromTop(rightColHeight / 12);
	reforgeImageBtn.setBounds(fourthRow.removeFromLeft(rightCol.getWidth() / 3).reduced(10, fourthRow.getHeight() / 2.5));
	forgeImageBtn.setBounds(fourthRow.removeFromLeft(rightCol.getWidth() / 3).reduced(10, fourthRow.getHeight() / 2.5));
	exportBtn.setBounds(fourthRow.removeFromLeft(rightCol.getWidth() / 3).reduced(10, fourthRow.getHeight() / 2.5));
	//txbtn.setBounds(rightCol);
	//corpusListBox.setBounds(tempArea);

}



void MelodySmithVSTAudioProcessorEditor::sliderValueChanged(Slider* slider)
{
	//processor.noteOnVel = midiVolume.getValue();
	if (slider == &keyKnob)
	{

	}
}

void MelodySmithVSTAudioProcessorEditor::buttonClicked(Button* btn)
{
	if (btn == &reforgeImageBtn)
	{
		String s = "";
	}
	else if (btn == &exportBtn)
	{
		FileChooser myChooser("Select the folder in which to save composed midi files...",
			File::getSpecialLocation(File::userHomeDirectory));
		if (myChooser.browseForDirectory())
		{
			exportFolder = myChooser.getResult();
		}
	}
	else if (btn == &forgeImageBtn)
	{
		String keyValue = keySelect.getText();
		//int keyIndex = keySelect.getItemId();
		//if(keyIndex == 0)*/
		double intervalWeight = invervalWeightSlider.getValue();
		double durationWeight = durationWeightSlider.getValue();
		double nGramLength = nGramLengthSlider.getValue();
		double numberOfComparisons = numberOfComparisonsSlider.getValue();
		String clParamsStr = "\"" + keyValue + "\" " + String(intervalWeight) + " " + String(durationWeight) + " " + String(nGramLength) + " " + String(numberOfComparisons) + " ";
		//Create master directory
		File f(melodysmithDirPath + "artists");
		f.deleteRecursively();
		f.createDirectory();

		//For each artist
		for (int i = 0; i < artists_to_influences.size(); i++)
		{
			//create artist directory
			std::tuple<String, double> currArtistToInfluence(artists_to_influences[i]);
			String artistName = std::get<0>(currArtistToInfluence);
			double inf = std::get<1>(currArtistToInfluence);
			File artistFolder(f.getFullPathName() + "/" + artistName);
			artistFolder.createDirectory();

			//add to cl string
			clParamsStr += artistName + ":" + String(inf) + " ";
			
			//populate with artist files
			for (int j = 0; j < curr_artist_filename_tuples.size(); j++)
			{
				std::tuple<String, String> currFileArtist(curr_artist_filename_tuples[j]);
				String currArtistName = std::get<1>(currFileArtist);
				String fileName = std::get<0>(currFileArtist);

				if (currArtistName == artistName)
				{
					File tempF(fileName);
					File newF(artistFolder.getFullPathName() + "/file" + String(j) + ".mid");
					tempF.copyFileTo(newF);
				}
			}
		}

		//Call commandline melodysmith.jar
		int i;
		printf("Checking if processor is available...");
		if (system(NULL)) puts("Ok");
		else exit(EXIT_FAILURE);

		//String melodySmithJarStr = File::getCurrentWorkingDirectory().getChildFile("MelodySmith.jar").getFullPathName();
		String melodySmithJarStr = melodysmithDirPath + "MelodySmith.jar";
		String artistsStr = melodysmithDirPath + "artists";
		String outputFilename = exportFolder.getFullPathName() + "\\output1.mid";

		String clStr = "java -jar \"" + melodySmithJarStr + "\" \"" + artistsStr + "\" \"" + outputFilename + "\" " + clParamsStr + "& pause";
		//printf("Executing command DIR...\n");
		//system("pause");
		i = system(clStr.toStdString().c_str());
		//system("pause");
		//printf("The value returned was: %d.\n", i);


		//File F = File::getCurrentWorkingDirectory().getChildFile("output.mid");
		//File F("C:\\Users\\Daniel Mattheiss\\Documents\\MelodySmith\\MelodySmithVST\\MelodySmith\\MelodySmith - juce\\Builds\\VisualStudio2017\\output.mid");
		//FileInputStream S(F);
		//MidiFile midifile;
		//midifile.readFrom(S);
		//midifile.convertTimestampTicksToSeconds();

		//MidiMessageSequence seq;
		//seq.clear();
		//for (int t = 0; t < midifile.getNumTracks(); t++)
		//	seq.addSequence(*midifile.getTrack(t), 0.0 /*timeAdjustmentDelta*/);
		//seq.updateMatchedPairs();
		//system("pause");
		//String r = F.getCurrentWorkingDirectory().getFullPathName();

		//MidiSequence->clear();
		////file input
		//ScopedPointer<MidiOutput> OutputController = MidiOutput::openDevice(0);
		//File* ReadFile(&F);
		//FileInputStream* ReadFileStream(new FileInputStream(*ReadFile));
		////String e = ReadFileStream->getStatus().getErrorMessage();
		//MidiFile* ReadMIDIFile(new MidiFile());
		//ReadMIDIFile->readFrom(*ReadFileStream);
		////ScopedPointer<MidiMessageSequence> MidiSequence(new MidiMessageSequence());
		//MidiMessage* msg(new MidiMessage());

		////get all tracks together
		//for (int track = 0; track < ReadMIDIFile->getNumTracks(); track++)
		//{
		//	const MidiMessageSequence* CurrentTrack = ReadMIDIFile->getTrack(track);
		//	MidiSequence->addSequence(*CurrentTrack, 0, 0, CurrentTrack->getEndTime());
		//}

		////should keep note ons and note offs matched?
		//MidiSequence->updateMatchedPairs();
		//int numEvents = MidiSequence->getNumEvents();
		//double TPQN = ReadMIDIFile->getTimeFormat();
		//int currentPosition = 0;
		//double NextEventTime = 0.;
		//double PrevTimestamp = 0.;
		//double msPerTick = 250. / TPQN; //set BPM
		//								//sending messages to output device in loop

		//system("pause");
		//while ((currentPosition < numEvents))
		//{
		//	//getting next message
		//	*msg = MidiSequence->getEventPointer(currentPosition)->message;
		//	//time left to reach next message
		//	NextEventTime = msPerTick * (msg->getTimeStamp() - PrevTimestamp);
		//	//wait for it 
		//	Time::waitForMillisecondCounter(Time::getMillisecondCounter() + NextEventTime);
		//	//play it
		//	OutputController->sendMessageNow(*msg);
		//	//store previous message timestamp
		//	PrevTimestamp = msg->getTimeStamp();
		//	//moving to next message
		//	currentPosition++;
		//}

		//audioProcessor->setMidiSequenceAndReadMIDIFile(MidiSequence, TPQN);
	}
} 

void MelodySmithVSTAudioProcessorEditor::setSizesAndColors()
{
	pluginWidth = 700;
	pluginHeight = 900;

	header1Colour = Colours::white;
	buttonBgColour = Colours::crimson,  buttonTextColour = Colours::black;
	
	pluginBgColour = Colour::fromString("#1a1a1a");

	divBgColour = Colours::darkgrey;
	tabSelectedColour = Colours::maroon;

	divTextColour = Colours::white.darker(0.1f);
	divBorderColour = Colours::azure;
}

void MelodySmithVSTAudioProcessorEditor::createCorpusPanel()
{

}

void MelodySmithVSTAudioProcessorEditor::createControlsPanel()
{

}
