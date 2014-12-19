package com.example.hi;

import java.util.Locale;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

public class TTSService extends Service implements OnInitListener{

	TextToSpeech tts;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.v("TTSService", "onCreate");
		tts = new TextToSpeech(this, this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if(intent == null)
			return START_STICKY;
			
		String action = intent.getAction(); 
		if(action != null && action.equals("speak"))
		{
			String text = intent.getStringExtra("text");
			if (!tts.isSpeaking()) 
			{
				Log.v("TTSService", "Speaking...");
				tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
				//tts.speak(text, queueMode, params, utteranceId)
			}

		}
		
		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS) 
		{
			//Toast.makeText(MainActivity.context, "TTS engine initialized successfully.", Toast.LENGTH_SHORT).show();
			Log.v("TTSService", "TTS engine initialized successfully.");
			tts.setLanguage(Locale.getDefault());
		}
		else 
		{
			tts = null;
			Log.e("TTSService", "Failed to initialize TTS engine.");
			//Toast.makeText(MainActivity.context, "Failed to initialize TTS engine.", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
