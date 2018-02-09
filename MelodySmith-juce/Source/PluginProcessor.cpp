/*
  ==============================================================================

    This file was auto-generated!

    It contains the basic framework code for a JUCE plugin processor.

  ==============================================================================
*/

#include "PluginProcessor.h"
#include "PluginEditor.h"


//==============================================================================
MelodySmithVSTAudioProcessor::MelodySmithVSTAudioProcessor()
#ifndef JucePlugin_PreferredChannelConfigurations
     : AudioProcessor (BusesProperties()
                     #if ! JucePlugin_IsMidiEffect
                      #if ! JucePlugin_IsSynth
                       .withInput  ("Input",  AudioChannelSet::stereo(), true)
                      #endif
                       .withOutput ("Output", AudioChannelSet::stereo(), true)
                     #endif
                       )
#endif
{
	MidiSequence = nullptr;
}

MelodySmithVSTAudioProcessor::~MelodySmithVSTAudioProcessor()
{
}

//==============================================================================
const String MelodySmithVSTAudioProcessor::getName() const
{
    return JucePlugin_Name;
}

bool MelodySmithVSTAudioProcessor::acceptsMidi() const
{
   #if JucePlugin_WantsMidiInput
    return true;
   #else
    return false;
   #endif
}

bool MelodySmithVSTAudioProcessor::producesMidi() const
{
   #if JucePlugin_ProducesMidiOutput
    return true;
   #else
    return false;
   #endif
}

bool MelodySmithVSTAudioProcessor::isMidiEffect() const
{
   #if JucePlugin_IsMidiEffect
    return true;
   #else
    return false;
   #endif
}

double MelodySmithVSTAudioProcessor::getTailLengthSeconds() const
{
    return 0.0;
}

int MelodySmithVSTAudioProcessor::getNumPrograms()
{
    return 1;   // NB: some hosts don't cope very well if you tell them there are 0 programs,
                // so this should be at least 1, even if you're not really implementing programs.
}

int MelodySmithVSTAudioProcessor::getCurrentProgram()
{
    return 0;
}

void MelodySmithVSTAudioProcessor::setCurrentProgram (int index)
{
}

const String MelodySmithVSTAudioProcessor::getProgramName (int index)
{
    return {};
}

void MelodySmithVSTAudioProcessor::changeProgramName (int index, const String& newName)
{
}

//==============================================================================
void MelodySmithVSTAudioProcessor::prepareToPlay (double sampleRate, int samplesPerBlock)
{
    // Use this method as the place to do any pre-playback
    // initialisation that you need..
}

void MelodySmithVSTAudioProcessor::releaseResources()
{
    // When playback stops, you can use this as an opportunity to free up any
    // spare memory, etc.
}

#ifndef JucePlugin_PreferredChannelConfigurations
bool MelodySmithVSTAudioProcessor::isBusesLayoutSupported (const BusesLayout& layouts) const
{
  #if JucePlugin_IsMidiEffect
    ignoreUnused (layouts);
    return true;
  #else
    // This is the place where you check if the layout is supported.
    // In this template code we only support mono or stereo.
    if (layouts.getMainOutputChannelSet() != AudioChannelSet::mono()
     && layouts.getMainOutputChannelSet() != AudioChannelSet::stereo())
        return false;

    // This checks if the input layout matches the output layout
   #if ! JucePlugin_IsSynth
    if (layouts.getMainOutputChannelSet() != layouts.getMainInputChannelSet())
        return false;
   #endif

    return true;
  #endif
}
#endif

void MelodySmithVSTAudioProcessor::setMidiSequenceAndReadMIDIFile(std::shared_ptr<MidiMessageSequence> MS, double x)
{
	MidiSequence = MS;
	TPQN = x;
	//AudioProcessorPlayer app;
	//app.setProcessor(this);
	//MidiMessageCollector mmc = app.getMidiMessageCollector();
}

void MelodySmithVSTAudioProcessor::processBlock (AudioSampleBuffer& buffer, MidiBuffer& midiMessages)
{
    ScopedNoDenormals noDenormals;
    const int totalNumInputChannels  = getTotalNumInputChannels();
    const int totalNumOutputChannels = getTotalNumOutputChannels();

    // In case we have more outputs than inputs, this code clears any output
    // channels that didn't contain input data, (because these aren't
    // guaranteed to be empty - they may contain garbage).
    // This is here to avoid people getting screaming feedback
    // when they first compile a plugin, but obviously you don't need to keep
    // this code if your algorithm always overwrites all the output channels.
    for (int i = totalNumInputChannels; i < totalNumOutputChannels; ++i)
        buffer.clear (i, 0, buffer.getNumSamples());

    // This is the place where you'd normally do the guts of your plugin's
    // audio processing...
    for (int channel = 0; channel < totalNumInputChannels; ++channel)
    {
        float* channelData = buffer.getWritePointer (channel);

        // ..do something to the data...
    }

	//buffer.clear();
	//midiMessages.clear();
	//MidiBuffer generatedMidi;
	//int time;
	//MidiMessage m;
	////int8 ccTempVal;
	////int8 myVal = (int8)CC00Val;

	////if (myVal != ccTempVal)
	////{
	//	//MidiMessage::controllerEvent()
	//m = MidiMessage::controllerEvent(2, 1, 100);
	//generatedMidi.addEvent(m, midiMessages.getLastEventTime());
	//}
	//else generatedMidi.clear();

	//ccTempVal = myVal;
	//midiMessages.swapWith(generatedMidi);

	// create a copy
	//MidiBuffer localCopy(midiMessages);

	//// clear the output before adding stuff
	//midiMessages.clear();

	//if (MidiSequence != nullptr)
	//{
	//	//MidiOutput newDevice = MidiOutput::createNewDevice(“New Midi Output Device”);
	//	//setMidi
	//	//processorPlayer->setMidiOutput(newDevice)
	//	//for (int t = 0; t < MidiSequence->; t++) {
	//	//	const MidiMessageSequence* track = MidiSequence.getTrack(t);
	//	//	for (int i = 0; i < track->getNumEvents(); i++) {
	//	//		MidiMessage& m = track->getEventPointer(i)->message;
	//	//		m.multiplyVelocity(0.01f);
	//	//		int sampleOffset = (int)(sampleRate * m.getTimeStamp());
	//	//		if (sampleOffset > totalSamples)
	//	//			totalSamples = sampleOffset;
	//	//		midiMessages.addEvent(m, sampleOffset);
	//	//	}
	//	//}
	//	int numSamples = buffer.getNumSamples();
	//	system("pause");
	//	ScopedPointer<MidiMessage> msg(new MidiMessage());

	//	double sampleRate(44100.0);

	//	// now add stuff
	//	int numEvents = MidiSequence->getNumEvents();
	//	int currentPosition = 0;
	//	double NextEventTime = 0.;
	//	double PrevTimestamp = 0.;
	//	double msPerTick = 250. / TPQN; //set BPM
	//									//sending messages to output device in loop
	//	while ((currentPosition < numEvents) && (currentPosition < numSamples))
	//	{
	//		//getting next message
	//		*msg = MidiSequence->getEventPointer(currentPosition)->message;
	//		//time left to reach next message
	//		//NextEventTime = msPerTick * (msg->getTimeStamp() - PrevTimestamp);
	//		//wait for it 
	//		//Time::waitForMillisecondCounter(Time::getMillisecondCounter() + NextEventTime);
	//		//play it
	//		//const double timestamp = msg->getTimeStamp();
	//		//const int sampleNumber = (int)(timestamp * sampleRate);
	//		midiMessages.addEvent(*msg, currentPosition);
	//		//midiMessages.add(*msg);
	//		//store previous message timestamp
	//		//PrevTimestamp = msg->getTimeStamp();
	//		//moving to next message
	//		currentPosition++;
	//	}

	//	//MidiOutput::openDevice(0)->startBackgroundThread();
	//	//MidiOutput::openDevice(0)->sendBlockOfMessages(midiMessages, Time::getMillisecondCounter(), getSampleRate());
	//	//MidiOutput::openDevice(0)->stop
	//	//DeviceMan

 //		MidiSequence = nullptr;
	//}


}


//==============================================================================
bool MelodySmithVSTAudioProcessor::hasEditor() const
{
    return true; // (change this to false if you choose to not supply an editor)
}

AudioProcessorEditor* MelodySmithVSTAudioProcessor::createEditor()
{
    return new MelodySmithVSTAudioProcessorEditor (*this);
}

//==============================================================================
void MelodySmithVSTAudioProcessor::getStateInformation (MemoryBlock& destData)
{
    // You should use this method to store your parameters in the memory block.
    // You could do that either as raw data, or use the XML or ValueTree classes
    // as intermediaries to make it easy to save and load complex data.
}

void MelodySmithVSTAudioProcessor::setStateInformation (const void* data, int sizeInBytes)
{
    // You should use this method to restore your parameters from this memory block,
    // whose contents will have been created by the getStateInformation() call.
}

//==============================================================================
// This creates new instances of the plugin..
AudioProcessor* JUCE_CALLTYPE createPluginFilter()
{
    return new MelodySmithVSTAudioProcessor();
}
