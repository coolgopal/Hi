package com.example.hi;

import java.util.Locale;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract.PhoneLookup;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

public class TTSService extends Service implements OnInitListener
{
	TextToSpeech tts;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.v("TTSService", "onCreate");
		tts = new TextToSpeech(this, this);
	}

	// Think whether to put in different class?
	String getContactName(Context context, String phoneNumber)
	{
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		Cursor cursor = cr.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
		if(cursor == null)
		{
			return null;
		}
		
		String contactName = null;
		if(cursor.moveToFirst())
		{
			contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
		}
		
		if(cursor != null && !cursor.isClosed())
		{
			cursor.close();
		}
		
		return contactName;
	}
	
	String crunchifyNumber(String number)
	{
		char[] stringToCharArray = new char[160];
		stringToCharArray = number.toCharArray();
		char[] crunchifiedStringToCharArray = new char[320];
		for(int i = 0, j = 0; i < stringToCharArray.length; i++, j++)
		{
			crunchifiedStringToCharArray[j] = stringToCharArray[i];
			j++;
			crunchifiedStringToCharArray[j] = ' ';
		}
		
		String crunchifiedNumber = String.valueOf(crunchifiedStringToCharArray);
		return crunchifiedNumber; 
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if(intent == null)
			return START_STICKY;
			
		String action = intent.getAction(); 
		if(action != null)
		{
			String text = null;
			
			if(action.equals("readSMS"))
			{
				String senderNumber = intent.getStringExtra("senderNumber");
				String messageBody = intent.getStringExtra("messageBody");
				
				String contactName = getContactName(getApplicationContext(), senderNumber);
				if(contactName != null)
				{
					text = "Hey, You have received an SMS from " + contactName + ". " + messageBody;
				}
				else
				{
					text = "Hey, You have received an SMS from " + crunchifyNumber(senderNumber) + ". " + messageBody;
				}
			}
			
			if(action.equals("read"))
			{
				text = intent.getStringExtra("text");
			}
			
			if(text != null)
			{
				if (!tts.isSpeaking()) 
				{
					Log.v("TTSService", "Speaking...");
					tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
					//tts.speak(text, queueMode, params, utteranceId)
				}
			}
			else
			{
				Log.i("TTSService", "Nothing to speak!!!");
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
