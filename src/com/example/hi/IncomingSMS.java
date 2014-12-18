package com.example.hi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class IncomingSMS extends BroadcastReceiver{

	// Get the object of SMSManager
	final SmsManager smsMgr = SmsManager.getDefault();
	
	String crunchifyNumber(String number)
	{
		char[] stringToCharArray = new char[160];
		stringToCharArray = number.toCharArray();
		char[] crunchifiedStringToCharArray = new char[320];
		for(int i = 0; i < stringToCharArray.length; i++)
		{
			crunchifiedStringToCharArray[i] = stringToCharArray[i];
			i++;
			crunchifiedStringToCharArray[i] = ' ';
		}
		
		String crunchifiedNumber = String.valueOf(crunchifiedStringToCharArray);
		return crunchifiedNumber; 
	}
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		// Retrieve a map of extended data from the intent.
		final Bundle bundle = intent.getExtras();
		
		try
		{
			if(bundle != null)
			{
				final Object[] pdus = (Object[]) bundle.get("pdus");
				SmsMessage currentMessage = SmsMessage
						.createFromPdu((byte[]) pdus[0]);
				String senderNumber = currentMessage.getDisplayOriginatingAddress();
				String messageBody = currentMessage.getDisplayMessageBody();
				
				Log.i("SmsReceiver", "senderNumber: " + senderNumber + ";messageBody: " + messageBody);
				
				// Show alert
				int duration = Toast.LENGTH_LONG;
				Toast toast = Toast.makeText(context, "senderNumber: " + senderNumber + "\nmessageBody: " + messageBody, duration);
				toast.show();		
				
				Intent speechServiceIntent = new Intent(context, TTSService.class);
				speechServiceIntent.setAction("speak");
				speechServiceIntent.putExtra("text", "Hey, You have received an SMS from " + crunchifyNumber(senderNumber) + ". " + messageBody);
				context.startService(speechServiceIntent);
			}
		}
		catch(Exception e)
		{
			Log.e("SmsReceiver", "Exception in SmsReceiver");
		}
	}
}
