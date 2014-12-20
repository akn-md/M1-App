package m1.nayak.m1;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class M1Application extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.enableLocalDatastore(getApplicationContext());
		Parse.initialize(this, "nIQTcBixsb6TgzFfxGFWyqcEvcNS7oHg0FsTUOzk", "BHV4tflQu9yRydvc1BbDIarww9vUSlET9yfdJjfT");

		ParseUser.enableAutomaticUser();
//		ParseACL defaultACL = new ParseACL();
		// If you would like all objects to be private by default, remove this line.
//		defaultACL.setPublicReadAccess(true);
//		ParseACL.setDefaultACL(defaultACL, true);
	}
}
