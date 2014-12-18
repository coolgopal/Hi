package com.example.hi;

import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
		OnInitListener

{

	private TextToSpeech tts;
	public static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		tts = new TextToSpeech(this, this);
		findViewById(R.id.button1).setOnClickListener(this);

		context = this;
	}

	@Override
	public void onInit(int code) {
		if (code == TextToSpeech.SUCCESS) {

			tts.setLanguage(Locale.getDefault());

		} else {
			tts = null;
			Toast.makeText(this, "Failed to initialize TTS engine.",

			Toast.LENGTH_SHORT).show();

		}
	}

	@Override
	public void onClick(View v) {
		if (tts != null) {
			String text =

			((EditText) findViewById(R.id.editText1)).getText().toString();
			if (text != null) {
				if (!tts.isSpeaking()) {
					tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

				}

			}

		}

	}

	@Override
	protected void onDestroy() {
		if (tts != null) {

			tts.stop();

			tts.shutdown();

		}

		super.onDestroy();
	}

}