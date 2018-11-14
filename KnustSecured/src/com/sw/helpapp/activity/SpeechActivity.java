package com.sw.helpapp.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

public class SpeechActivity  extends Activity{

	private static final int SPEECH_REQUEST_CODE = 0;
	

		// Create an intent that can start the Speech Recognizer activity
		private void displaySpeechRecognizer() {
		    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
		            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		// Start the activity, the intent will be populated with the speech text
		    startActivityForResult(intent, SPEECH_REQUEST_CODE);
		}

		// This callback is invoked when the Speech Recognizer returns.
		// This is where you process the intent and extract the speech text from the intent.
		@Override
		protected void onActivityResult(int requestCode, int resultCode,
		        Intent data) {
		    if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
		        List<String> results = data.getStringArrayListExtra(
		                RecognizerIntent.EXTRA_RESULTS);
		        String spokenText = results.get(0);
		        // Do something with spokenText
		    }
		    super.onActivityResult(requestCode, resultCode, data);
		}
	}


