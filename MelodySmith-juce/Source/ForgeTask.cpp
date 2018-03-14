/*
  ==============================================================================

    ForgeTask.cpp
    Created: 5 Mar 2018 7:09:43pm
    Author:  Daniel Mattheiss

  ==============================================================================
*/

#include "../JuceLibraryCode/JuceHeader.h"
#include "ForgeTask.h"
#include <sstream>
#include <Windows.h>
#include <ShellApi.h>

//==============================================================================
ForgeTask::ForgeTask() : ThreadWithProgressWindow("Creating your music...", false, false)
{
    // In your constructor, you should add any child components, and
    // initialise any special settings that your component needs.

}

ForgeTask::~ForgeTask()
{
}

void ForgeTask::run()
{
	javaExePath = "C:\\jre1.8.0_121\\bin\\java.exe";
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
