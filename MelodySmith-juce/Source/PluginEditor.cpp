/*
  ==============================================================================

    This file was auto-generated!

    It contains the basic framework code for a JUCE plugin editor.

  ==============================================================================
*/

#include "PluginProcessor.h"
#include "PluginEditor.h"
#include <Windows.h>
#include <ShellApi.h>
#include <sstream>
#include <thread>




//==============================================================================
MelodySmithVSTAudioProcessorEditor::MelodySmithVSTAudioProcessorEditor (MelodySmithVSTAudioProcessor& p)
    : AudioProcessorEditor (&p), processor (p), tabbedCorpusComponent(TabbedButtonBar::Orientation::TabsAtLeft), managePanel(curr_artist_filename_tuples),
	influencesPanel(curr_artist_filename_tuples, artists_to_influences, managePanel), progressBar(progressDouble)
{
	audioProcessor = &p;

	//OperatingSystem
	platform = SystemStats::getOperatingSystemName();
	platform = platform.toLowerCase();
	if (platform.contains("mac"))
		melodysmithDirPath = "/Applications/MelodySmith/";
	else
		melodysmithDirPath = "C:\\Program Files\\MelodySmith\\";

	countOfOutputFiles = 0;
	addAndMakeVisible(progressBar);
	progressBar.setVisible(false);
	//String windowsMelodysmithDirPath = "C:/Users/Daniel Mattheiss/Documents/MelodySmith/MelodySmithVST/MelodySmith/MelodySmith - juce/Builds/VisualStudio2017/";


    // Make sure that before the constructor has finished, you've set the
    // editor's size to whatever you need it to be.
	setSizesAndColors();
	setSize(700, 550);
    //setSize (pluginHeight, pluginWidth);
	setResizable(true, true);
	setResizeLimits(100, 100, 1800, 3200);

	corpusHeader.setColour(Label::textColourId, header1Colour);
	corpusHeader.setText("Corpus", NotificationType::dontSendNotification);
	corpusHeader.setJustificationType(Justification::centred);
	//corpusHeader.setFont(Font("Avenir", 20.0f, 0));
	addAndMakeVisible(corpusHeader);
	//addAndMakeVisible(corpusPanel);

	parametersHeader.setColour(Label::textColourId, header1Colour);
	parametersHeader.setText("Parameters", NotificationType::dontSendNotification);
	parametersHeader.setJustificationType(Justification::centred);
	addAndMakeVisible(parametersHeader);

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

	addAndMakeVisible(advancedParamsPanel);
	addAndMakeVisible(basicParamsPanel);
	addAndMakeVisible(scaleParamsPanel);

	exportBtn.setButtonText("Choose Save Folder");
	exportBtn.setColour(TextButton::buttonColourId, Colours::red);
	exportBtn.setColour(TextButton::textColourOffId, Colours::white);
	exportBtn.addListener(this);
	addAndMakeVisible(exportBtn);

	tabbedCorpusComponent.setOrientation(TabbedButtonBar::Orientation::TabsAtTop);
	tabbedCorpusComponent.addTab("Manage", Colours::deepskyblue, &managePanel, false);
	tabbedCorpusComponent.addTab("Influences", Colours::deepskyblue, &influencesPanel, false);
	//tabbedCorpusComponent.setTabBackgroundColour(0, Colours::aliceblue);
	//tabbedCorpusComponent.setTabBackgroundColour()
	tabbedCorpusComponent.setColour(TabbedComponent::backgroundColourId, Colours::deepskyblue.darker(0.6f));
	//tabbedCorpusComponent.setColour()
	addAndMakeVisible(tabbedCorpusComponent);

	exportFolder = File(melodysmithDirPath);
}

MelodySmithVSTAudioProcessorEditor::~MelodySmithVSTAudioProcessorEditor()
{
}

//==============================================================================
void MelodySmithVSTAudioProcessorEditor::paint (Graphics& g)
{
	// fill the whole window white
	//g.fillAll(Colours::black.brighter(0.2f));
	g.fillAll(Colours::deepskyblue.darker(2.0f));   // clear the background

	// set the current drawing colour to black
	//g.setColour(Colours::white);

	g.setFont(Font("Avenir", 20.0f, 0));
}

void MelodySmithVSTAudioProcessorEditor::resized()
{
    // This is generally where you'll want to lay out the positions of any
    // subcomponents in your editor..

	// sets the position and size of the slider with arguments (x, y, width, height)
	juce::Rectangle<int> area(getLocalBounds());

	juce::Rectangle<int> leftCol(area.removeFromLeft(area.getWidth() / 2));
	
	float fontHeight = corpusHeader.getFont().getHeight() + 20;
	juce::Rectangle<int> tempArea(leftCol.removeFromTop(fontHeight));
	corpusHeader.setBounds(tempArea);

	tempArea = juce::Rectangle<int>(leftCol.reduced(15, 10));
	//tabbedCorpusComponent.setBounds(tempArea.removeFromTop(tempArea.getHeight() / 12));
	//corpusPanel.setBounds(tempArea);
	tabbedCorpusComponent.setBounds(tempArea);

	juce::Rectangle<int> rightCol(area);

	parametersHeader.setBounds(rightCol.removeFromTop(fontHeight));

	int rightColHeight = rightCol.getHeight();
	int rightColWidth = rightCol.getWidth();

	//reforgeImageBtn.setBounds(rightCol.removeFromTop(rightColHeight / 3));
	juce::Rectangle<int> firstRow = rightCol.removeFromTop(rightColHeight / 4);
	juce::Rectangle<int> secondRow = rightCol.removeFromTop(rightColHeight / 4);
	juce::Rectangle<int> thirdRow = rightCol.removeFromTop(rightColHeight / 4);
	juce::Rectangle<int> fourthRow = rightCol.removeFromTop(rightColHeight / 4);

	basicParamsPanel.setBounds(firstRow.removeFromLeft(firstRow.getWidth() - 10).reduced(0, 10));
	scaleParamsPanel.setBounds(secondRow.removeFromLeft(secondRow.getWidth() - 10).reduced(0, 10));
	advancedParamsPanel.setBounds(thirdRow.removeFromLeft(thirdRow.getWidth() - 10).reduced(0, 10));

	//rightCol.removeFromTop(rightColHeight / 12);
	reforgeImageBtn.setBounds(fourthRow.removeFromLeft(rightCol.getWidth() / 3).reduced(10, fourthRow.getHeight() / 2.5));
	forgeImageBtn.setBounds(fourthRow.removeFromLeft(rightCol.getWidth() / 3).reduced(10, fourthRow.getHeight() / 2.5));
	exportBtn.setBounds(fourthRow.removeFromLeft(rightCol.getWidth() / 3).reduced(10, fourthRow.getHeight() / 2.5));

	//progressBar.setBounds(rightCol);
	//txbtn.setBounds(rightCol);
	//corpusListBox.setBounds(tempArea);

}



void MelodySmithVSTAudioProcessorEditor::sliderValueChanged(Slider* slider)
{
	//processor.noteOnVel = midiVolume.getValue();
	//if (slider == &keyKnob)
	//{

	//}
}

void MelodySmithVSTAudioProcessorEditor::updateProgressBar()
{
	progressBar.setVisible(true);
}

void MelodySmithVSTAudioProcessorEditor::buttonClicked(Button* btn)
{
	if (btn == &reforgeImageBtn)
	{
		curr_artist_filename_tuples.clear();
		artists_to_influences.clear();
		/*corpusPanel.managePanel.corpusListBox.clearSongs();
		corpusPanel.managePanel.corpusListBox.updateContent();
		corpusPanel.influencesPanel.influencesListBox.updateContent();*/
		managePanel.corpusListBox.clearSongs();
		managePanel.corpusListBox.updateContent();
		influencesPanel.influencesListBox.updateContent();
		/*NativeDesktopWindow* const w = getNativeWindow();

		if (w->isMinimised())
			w->setMinimised(false);
		else
			w->setMinimised(true);*/
	}
	else if (btn == &exportBtn)
	{
		//File f("C:\\Users\\Daniel Mattheiss\\Downloads\\corpus");
		FileChooser myChooser("Select the folder in which to save composed midi files...",
			File::getSpecialLocation(File::userHomeDirectory));
		//myChooser.
		if (myChooser.browseForDirectory())
		{
			exportFolder = myChooser.getResult();
		}
	}
	else if (btn == &forgeImageBtn)
	{
		//std::thread t1(&MelodySmithVSTAudioProcessorEditor::updateProgressBar, this);
		//t1.join();
		//progressBar.launc
		//MessageManager::
		/*MessageManager::callAsync(
			[=]() {
			progressBar.setVisible(true);
		}
		);*/

		//Old way
		/*std::string keyValue = scaleParamsPanel.keySelect.getText().toStdString();
		keyValue.erase(std::remove(keyValue.begin(), keyValue.end(), ' '), keyValue.end());
		double intervalWeight = basicParamsPanel.invervalWeightSlider.getValue();
		double durationWeight = basicParamsPanel.durationWeightSlider.getValue();
		double nGramLength = advancedParamsPanel.nGramLengthSlider.getValue();
		double numberOfComparisons = advancedParamsPanel.numberOfComparisonsSlider.getValue();
		String clParamsStr = "\"" + keyValue + "\" " + String(intervalWeight) + " " + String(durationWeight) + " " + String(nGramLength) + " " + String(numberOfComparisons) + " ";
		*/

		//New way
		std::string keyValue = scaleParamsPanel.keySelect.getText().toStdString();
		keyValue.erase(std::remove(keyValue.begin(), keyValue.end(), ' '), keyValue.end());
		double rhythmicImportance = advancedParamsPanel.nGramLengthSlider.getValue() * 10;
		double melodicImportance = advancedParamsPanel.numberOfComparisonsSlider.getValue() * 10; 
		String restType = scaleParamsPanel.restTypeSelect.getText().toStdString();
		double restAmount = scaleParamsPanel.restAmntSlider.getValue() * 2;
		double syncopation = scaleParamsPanel.syncopationSlider.getValue() * 10;
		double phraseLength = basicParamsPanel.durationWeightSlider.getValue() * 2;
		double creativity = basicParamsPanel.creativityWeightSlider.getValue();
		double speed = basicParamsPanel.invervalWeightSlider.getValue() * 10;
		String clParamsStr = "\"" + keyValue + "\" " + String(rhythmicImportance) + " " + String(melodicImportance) +
			" " + "\"" + restType + "\" " + String(restAmount) + " " + String(syncopation) + " x" + String(phraseLength) + " "
			+ String(creativity) + " " + String(speed) + " ";
		
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

		String melodySmithJarStr = melodysmithDirPath + "MelodySmith.jar";
		String artistsStr = melodysmithDirPath + "artists";
		String outputFilename = melodysmithDirPath + "melodysmith" + String(countOfOutputFiles++) + ".mid";

		String clStr = " -jar \"" + melodySmithJarStr + "\" \"" + artistsStr + "\" \"" + outputFilename + "\" " + clParamsStr;

		//String javaExePath = "\"" + melodysmithDirPath + "java.exe" + "\"";
		String javaExePath = "java";

		if (platform.contains("windows"))
		{
			STARTUPINFO si;
			PROCESS_INFORMATION pi;

			ZeroMemory(&si, sizeof(si));
			si.cb = sizeof(si);
			ZeroMemory(&pi, sizeof(pi));

			si.dwFlags = STARTF_USESHOWWINDOW;
			si.wShowWindow = SW_HIDE;

			// Start the child process. 
			if (!CreateProcessA(javaExePath.toStdString().c_str(),   // No module name (use command line)
				&clStr.toStdString()[0],        // Command line
				NULL,           // Process handle not inheritable
				NULL,           // Thread handle not inheritable
				FALSE,          // Set handle inheritance to FALSE
				CREATE_NO_WINDOW,              // No creation flags
				NULL,           // Use parent's environment block
				NULL,           // Use parent's starting directory 
				&si,            // Pointer to STARTUPINFO structure
				&pi)            // Pointer to PROCESS_INFORMATION structure
				)
			{
				std::ostringstream os;
				os << GetLastError();
				String str = os.str();
				//printf("CreateProcess failed (%d).\n", GetLastError());
				//return 1;
			}

			// Wait until child process exits.
			WaitForSingleObject(pi.hProcess, INFINITE);

			// Close process and thread handles. 
			CloseHandle(pi.hProcess);
			CloseHandle(pi.hThread);
			//progressBar.setVisible(false);
			//progressBar.repaint();
			//;

			/*MessageManager::callAsync(
			[=]() {
			progressBar.setVisible(false);
			}
			);*/
		}
		else if (platform.contains("mac"))
		{
			String fullClStr = javaExePath + clStr;
			system(fullClStr.toStdString().c_str());
		}

		//return 0;
		//FreeConsole();
		//i = system(clStr.toStdString().c_str());

	}
} 

void MelodySmithVSTAudioProcessorEditor::setSizesAndColors()
{
	pluginWidth = 700;
	pluginHeight = 600;

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
