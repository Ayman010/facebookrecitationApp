package com.donnatest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;



public class MainActivity extends Activity {
	
	// Your Facebook APP ID
	private static String APP_ID = "612013318815800"; // Replace with your App ID

	// Instance of Facebook Class
	private Facebook facebook = new Facebook(APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	
	Button loginbtn;
	Button logoutbtn;
	Button pickerbtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loginbtn= (Button) findViewById(R.id.blogin);
		logoutbtn= (Button) findViewById(R.id.blogout);
		pickerbtn= (Button) findViewById(R.id.bpicker);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		
		/**
		 * Login button Click event
		 * */
		loginbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("Image Button", "button Clicked");
				loginToFacebook();
			}
		});
		
		logoutbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("Image Button", "button Clicked");
				logoutFacebook();
			}
		});
		
		pickerbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			Bundle params = new Bundle();
			params.putString("fields","name,picture,location");
			
			}
		});
	}

	protected void logoutFacebook() {
		// TODO Auto-generated method stub
		mAsyncRunner.logout(this, new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {
				Log.d("Logout from Facebook", response);
				if (Boolean.parseBoolean(response) == true) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// make Login button visible
							loginbtn.setVisibility(View.VISIBLE);

							// making all remaining buttons invisible
							logoutbtn.setVisibility(View.INVISIBLE);
							pickerbtn.setVisibility(View.INVISIBLE);
						}

					});

				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
			}
		});
	}
		

	protected void loginToFacebook() {
		// TODO Auto-generated method stub
		
		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);
			
			loginbtn.setVisibility(View.INVISIBLE);
			
			// Making get profile button visible
			logoutbtn.setVisibility(View.VISIBLE);

			// Making post to wall visible
			pickerbtn.setVisibility(View.VISIBLE);

			Log.d("FB Sessions", "" + facebook.isSessionValid());
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			facebook.authorize(this,
					new String[] { "email", "publish_stream" },
					new DialogListener() {

						@Override
						public void onCancel() {
							// Function to handle cancel event
						}

						@Override
						public void onComplete(Bundle values) {
							// Function to handle complete event
							// Edit Preferences and update facebook acess_token
							SharedPreferences.Editor editor = mPrefs.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();

							loginbtn.setVisibility(View.INVISIBLE);
							
							// Making get profile button visible
							logoutbtn.setVisibility(View.VISIBLE);

							// Making post to wall visible
							pickerbtn.setVisibility(View.VISIBLE);
						}

						@Override
						public void onError(DialogError error) {
							// Function to handle error

						}

						@Override
						public void onFacebookError(FacebookError fberror) {
							// Function to handle Facebook errors

						}

					});
		}
		
	}

}
