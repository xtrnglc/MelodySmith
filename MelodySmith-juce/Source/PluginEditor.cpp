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
#include "ForgeTask.h"

// basic file operations
#include <iostream>
#include <fstream>





//==============================================================================
MelodySmithVSTAudioProcessorEditor::MelodySmithVSTAudioProcessorEditor (MelodySmithVSTAudioProcessor& p)
    : AudioProcessorEditor (&p), processor (p), tabbedCorpusComponent(TabbedButtonBar::Orientation::TabsAtLeft), managePanel(curr_artist_filename_tuples, influencesPanel),
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
	setResizable(false, false);
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

	exportBtn.setButtonText("Choose Forge Folder");
	exportBtn.setColour(TextButton::buttonColourId, Colours::red);
	exportBtn.setColour(TextButton::textColourOffId, Colours::white);
	exportBtn.addListener(this);
	addAndMakeVisible(exportBtn);

	//Load/save presets btns
	savePresetsBtn.setButtonText("Save Presets");
	savePresetsBtn.setColour(TextButton::buttonColourId, Colours::deepskyblue.darker(2.0f));
	savePresetsBtn.setColour(TextButton::textColourOffId, Colours::white);
	savePresetsBtn.addListener(this);
	addAndMakeVisible(savePresetsBtn);

	loadPresetsBtn.setButtonText("Load Presets");
	loadPresetsBtn.setColour(TextButton::buttonColourId, Colours::deepskyblue.darker(2.0f));
	loadPresetsBtn.setColour(TextButton::textColourOffId, Colours::white);
	loadPresetsBtn.addListener(this);
	addAndMakeVisible(loadPresetsBtn);

	tabbedCorpusComponent.setOrientation(TabbedButtonBar::Orientation::TabsAtTop);
	tabbedCorpusComponent.addTab("Manage", Colours::deepskyblue, &managePanel, false);
	tabbedCorpusComponent.addTab("Influences", Colours::deepskyblue, &influencesPanel, false);
	//tabbedCorpusComponent.setTabBackgroundColour(0, Colours::aliceblue);
	//tabbedCorpusComponent.setTabBackgroundColour()
	tabbedCorpusComponent.setColour(TabbedComponent::backgroundColourId, Colours::deepskyblue.darker(0.6f));
	//tabbedCorpusComponent.setColour()
	addAndMakeVisible(tabbedCorpusComponent);

	exportFolder = File(melodysmithDirPath);

	//Algorithm Type select
	algorithmTypeLabel.setText("Algorithm Type", NotificationType::dontSendNotification);
	algorithmTypeLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(algorithmTypeLabel);

	algorithmTypeSelect.addItem("Wandering", 1);
	algorithmTypeSelect.addItem("Phrased", 2);
	algorithmTypeSelect.addItem("Structured", 3);
	algorithmTypeSelect.setSelectedId(2);
	addAndMakeVisible(algorithmTypeSelect);

	//Output filename
	outputFilenameLabel.setText("Output Name", NotificationType::dontSendNotification);
	outputFilenameLabel.setJustificationType(Justification::centred);
	addAndMakeVisible(outputFilenameLabel);

	outputFilenameTextbox.setTextToShowWhenEmpty("melodysmith", Colours::lightgrey);
	//outputFilenameTextbox.setText(aName, false);
	addAndMakeVisible(outputFilenameTextbox);
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
	
	//Final Row
	juce::Rectangle<int> fourthRowDivided = fourthRow.removeFromTop(fourthRow.getHeight() / 2);
	int rowWidth = fourthRowDivided.getWidth();

	//Algorithm Type Select
	juce::Rectangle<int> finalPanel1 = fourthRowDivided.removeFromLeft(rowWidth / 3).reduced(4, 0);
	algorithmTypeLabel.setBounds(finalPanel1.removeFromTop(finalPanel1.getHeight() / 2));
	algorithmTypeSelect.setBounds(finalPanel1);

	//Output filename
	juce::Rectangle<int> finalPanel2 = fourthRowDivided.removeFromLeft(rowWidth / 3).reduced(4, 0);
	outputFilenameLabel.setBounds(finalPanel2.removeFromTop(finalPanel2.getHeight() / 2));
	outputFilenameTextbox.setBounds(finalPanel2);

	//Save/load presets
	juce::Rectangle<int> finalPanel3 = fourthRowDivided.removeFromLeft(rowWidth / 3).reduced(4, 0);
	savePresetsBtn.setBounds(finalPanel3.removeFromTop(finalPanel3.getHeight() / 2).reduced(0, 4));
	loadPresetsBtn.setBounds(finalPanel3.reduced(0, 4));



	reforgeImageBtn.setBounds(fourthRow.removeFromLeft(rightCol.getWidth() / 3).reduced(10, fourthRow.getHeight() / 4));
	forgeImageBtn.setBounds(fourthRow.removeFromLeft(rightCol.getWidth() / 3).reduced(10, fourthRow.getHeight() / 4));
	exportBtn.setBounds(fourthRow.removeFromLeft(rightCol.getWidth() / 3).reduced(10, fourthRow.getHeight() / 4));

	//progressBar.setBounds(rightCol);
	//txbtn.setBounds(rightCol);
	//corpusListBox.setBounds(tempArea);

}



void MelodySmithVSTAudioProcessorEditor::sliderValueChanged(Slider* slider)
{

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

		String baseAlgorithmType = algorithmTypeSelect.getText(), algorithmType = "";
		if (baseAlgorithmType == "Phrased") algorithmType = "extract";
		else if (baseAlgorithmType == "Structured") algorithmType = "phrased";
		else algorithmType = "wandering";
		algorithmType = algorithmType.toStdString();

		String clParamsStr = "\"" + keyValue + "\" " + String(rhythmicImportance) + " " + String(melodicImportance) +
			" " + "\"" + restType + "\" " + String(restAmount) + " " + String(syncopation) + " " +  String(phraseLength) + " "
			+ String(creativity) + " " + String(speed) + " " + algorithmType + " ";
		
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
		String currOutputFilename = outputFilenameTextbox.getText();
		if (currOutputFilename.isEmpty())
			currOutputFilename = "melodysmith";
		String outputFilename = melodysmithDirPath + currOutputFilename + String(countOfOutputFiles++) + ".mid";

		String clStr = " -jar \"" + melodySmithJarStr + "\" \"" + artistsStr + "\" \"" + outputFilename + "\" " + clParamsStr;

		//String javaExePath = "\"" + melodysmithDirPath + "java.exe" + "\"";
		String javaExePath = "java";
		//String javaExePath = "C:\\jre1.8.0_121\\bin\\java.exe";

		//system(String(javaExePath + clStr + "& pause").toStdString().c_str());

		/*if (platform.contains("windows"))
		{*/
			ForgeTask forgeTask;
			forgeTask.clStr = clStr;
			//f.javaExePath = javaExePath;
			forgeTask.runThread();

			/*STARTUPINFO si;
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
		/*}
		else if (platform.contains("mac"))
		{
			String fullClStr = javaExePath + clStr;
			system(fullClStr.toStdString().c_str());
		}*/

		//return 0;
		//FreeConsole();
		//i = system(clStr.toStdString().c_str());

	}
	else if (btn == &savePresetsBtn) {
		FileChooser myChooser("Save presets to a file...",
			File::getSpecialLocation(File::userHomeDirectory), "*.mssf");
		//myChooser.
		if (myChooser.browseForFileToSave(true))
		{
			juce::File saveFile = myChooser.getResult();
			writeToFile(saveFile);
		}
	}
	else if (btn == &loadPresetsBtn) {
		FileChooser myChooser("Load presets from a file...",
			File::getSpecialLocation(File::userHomeDirectory), "*.mssf");
		//myChooser.
		if (myChooser.browseForFileToOpen())
		{
			juce::File loadFile = myChooser.getResult();
			loadFromFile(loadFile);
		}
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

void MelodySmithVSTAudioProcessorEditor::writeToFile(juce::File saveFile)
{
	String savePresetsStr = "";
	String outputFilenameValue = outputFilenameTextbox.getText();
	int keyValue = scaleParamsPanel.keySelect.getSelectedId();
	double rhythmicImportance = advancedParamsPanel.nGramLengthSlider.getValue();
	double melodicImportance = advancedParamsPanel.numberOfComparisonsSlider.getValue();
	int restValue = scaleParamsPanel.restTypeSelect.getSelectedId();
	double restAmount = scaleParamsPanel.restAmntSlider.getValue();
	double syncopation = scaleParamsPanel.syncopationSlider.getValue();
	double phraseLength = basicParamsPanel.durationWeightSlider.getValue();
	double creativity = basicParamsPanel.creativityWeightSlider.getValue();
	double speed = basicParamsPanel.invervalWeightSlider.getValue();
	int algorithmValue = algorithmTypeSelect.getSelectedId();

	savePresetsStr = outputFilenameValue + "\n" + String(keyValue) + "\n" + String(rhythmicImportance) + "\n" + String(melodicImportance) + "\n"
		+ String(restValue) + "\n" + String(restAmount) + "\n" + String(syncopation) + "\n" + String(phraseLength) + "\n"
		+ String(creativity) + "\n" + String(speed) + "\n" + String(algorithmValue) + "\n";

	for (int i = 0; i < artists_to_influences.size(); i++)
	{
		//Get artist name and influence
		std::tuple<String, double> currArtistToInfluence(artists_to_influences[i]);
		String artistName = std::get<0>(currArtistToInfluence);
		double inf = std::get<1>(currArtistToInfluence);

		//add to presets string
		savePresetsStr +=  "///" + artistName + ":" + String(inf) + "\n";

		//populate with artist files
		for (int j = 0; j < curr_artist_filename_tuples.size(); j++)
		{
			std::tuple<String, String> currFileArtist(curr_artist_filename_tuples[j]);
			String currArtistName = std::get<1>(currFileArtist);
			String fileName = std::get<0>(currFileArtist);

			if (currArtistName == artistName)
			{
				//Newline for filepath
				savePresetsStr += fileName + "\n";
			}
		}
	}

	saveFile.replaceWithText(savePresetsStr);
	
}

void MelodySmithVSTAudioProcessorEditor::loadFromFile(juce::File loadFile)
{
	String loadFileStr = loadFile.loadFileAsString();

	int indexOf = loadFileStr.indexOf("\r\n");
	outputFilenameTextbox.setText(loadFileStr.substring(0, indexOf));
	loadFileStr = loadFileStr.substring(indexOf + 2);

	indexOf = loadFileStr.indexOf("\r\n");
	int newVal = loadFileStr.substring(0, indexOf).getIntValue();
	scaleParamsPanel.keySelect.setSelectedId(newVal);
	loadFileStr = loadFileStr.substring(indexOf + 2);

	indexOf = loadFileStr.indexOf("\r\n");
	advancedParamsPanel.nGramLengthSlider.setValue(loadFileStr.substring(0, indexOf).getDoubleValue());
	loadFileStr = loadFileStr.substring(indexOf + 2);

	indexOf = loadFileStr.indexOf("\r\n");
	advancedParamsPanel.numberOfComparisonsSlider.setValue(loadFileStr.substring(0, indexOf).getDoubleValue());
	loadFileStr = loadFileStr.substring(indexOf + 2);

	indexOf = loadFileStr.indexOf("\r\n");
	scaleParamsPanel.restTypeSelect.setSelectedId(loadFileStr.substring(0, indexOf).getIntValue());
	loadFileStr = loadFileStr.substring(indexOf + 2);

	indexOf = loadFileStr.indexOf("\r\n");
	scaleParamsPanel.restAmntSlider.setValue(loadFileStr.substring(0, indexOf).getDoubleValue());
	loadFileStr = loadFileStr.substring(indexOf + 2);

	indexOf = loadFileStr.indexOf("\r\n");
	scaleParamsPanel.syncopationSlider.setValue(loadFileStr.substring(0, indexOf).getDoubleValue());
	loadFileStr = loadFileStr.substring(indexOf + 2);


	indexOf = loadFileStr.indexOf("\r\n");
	basicParamsPanel.durationWeightSlider.setValue(loadFileStr.substring(0, indexOf).getDoubleValue());
	loadFileStr = loadFileStr.substring(indexOf + 2);

	indexOf = loadFileStr.indexOf("\r\n");
	basicParamsPanel.creativityWeightSlider.setValue(loadFileStr.substring(0, indexOf).getDoubleValue());
	loadFileStr = loadFileStr.substring(indexOf + 2);

	indexOf = loadFileStr.indexOf("\r\n");
	basicParamsPanel.invervalWeightSlider.setValue(loadFileStr.substring(0, indexOf).getDoubleValue());
	loadFileStr = loadFileStr.substring(indexOf + 2);

	indexOf = loadFileStr.indexOf("\r\n");
	algorithmTypeSelect.setSelectedId(loadFileStr.substring(0, indexOf).getIntValue());
	loadFileStr = loadFileStr.substring(indexOf + 2);

	while (true) {
		indexOf = loadFileStr.indexOf("\r\n");
		if (indexOf == -1) break;
		String artistInfStr = loadFileStr.substring(3, indexOf);

		String artistName = artistInfStr.substring(0, artistInfStr.indexOf(":"));
		double artistInfluence = artistInfStr.substring(artistInfStr.indexOf(":") + 1).getDoubleValue();
		artists_to_influences.add(std::make_tuple(artistName, artistInfluence));

		loadFileStr = loadFileStr.substring(indexOf + 2);

		while (!loadFileStr.substring(0, loadFileStr.indexOf("\r\n")).contains("///")) {
			if (loadFileStr.isEmpty()) break;
			String currfilePath = loadFileStr.substring(0, loadFileStr.indexOf("\r\n"));
			std::tuple<String, String> s(currfilePath, artistName);
			curr_artist_filename_tuples.add(s);
			managePanel.corpusListBox.fileNames.add(File(currfilePath));
			loadFileStr = loadFileStr.substring(loadFileStr.indexOf("\r\n") + 2);
		}
		
	}
	managePanel.corpusListBox.updateContent();
	this->repaint();

}
