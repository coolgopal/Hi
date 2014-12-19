package com.example.hi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener
{

//	private TextToSpeech tts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		//tts = new TextToSpeech(this, this);
		findViewById(R.id.button1).setOnClickListener(this);
		
		// Initialize ttsservice
		Intent speechServiceIntent = new Intent(this, TTSService.class);
		startService(speechServiceIntent);
	}

	@Override
	public void onClick(View v) {
			String text =

			((EditText) findViewById(R.id.editText1)).getText().toString();
			Intent speechServiceIntent = new Intent(this, TTSService.class);
			speechServiceIntent.setAction("speak");
			speechServiceIntent.putExtra("text", text);
			startService(speechServiceIntent);
	}

/*	@Override
	protected void onDestroy() {
		if (tts != null) {

			tts.stop();

			tts.shutdown();

		}

		super.onDestroy();
	}
*/
}