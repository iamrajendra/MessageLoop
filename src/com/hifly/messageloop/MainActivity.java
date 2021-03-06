package com.hifly.messageloop;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * This MainActivity displays the screen's UI and starts a single TaskFragment that
 * will retain itself when configuration changes occur.
 */
public class MainActivity extends FragmentActivity implements TaskFragment.TaskCallbacks {
	private static final String	TAG = MainActivity.class.getSimpleName();
	private static final String	KEY_TEXT = "current_text";
	private TaskFragment	mTaskFragment;
	private Button	    	mButton;
	private EditText    	vw1, vw2;


	/* ****************************
	 * initialize MainActivity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "===== onCreate:"+this);
		setContentView(R.layout.activity_main);

		// Initialize views
		vw1    	= (EditText) findViewById(R.id.text1);
		vw2    	= (EditText) findViewById(R.id.text2);
		mButton	= (Button)   findViewById(R.id.task_button);

		// Restore saved state
		if (savedInstanceState != null) {
			vw1.setText(savedInstanceState.getString(KEY_TEXT));
		}

		// create the TaskFragment
		FragmentManager fm = getSupportFragmentManager();
		mTaskFragment	= (TaskFragment) fm.findFragmentByTag("task");

		// If the Fragment is not null, then it already exists and is currently being
		// retained across a configuration change so no need to recreate it.
		// Create the fragment if it does not exist yet which will be the first
		// time this onCreate is called.
		if (mTaskFragment == null) {
			// Log.i(TAG, "TaskFragment create");
			mTaskFragment = new TaskFragment();
			fm.beginTransaction().add(mTaskFragment, "task").commit();
		}
		// Log.i(TAG, "TaskFragment set: "+mTaskFragment);

		// preset text in textviews and buttons
		mButton.setText(getString(DispatchWork.isConnected()?R.string.cancel:R.string.start));
		vw2.setText( DispatchWork.SERVERIP );
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_TEXT, vw1.getText().toString());
	}

	/*****************************
	 * Called when task button is clicked. Sends connect command to DispatchWork with
	 * ip address of server in server_IP.
	 * @param view calling UI object
	 */
	public void onBtnTask(View view) {
		String txt	= getString(DispatchWork.isConnected()?R.string.start:R.string.cancel);
		mButton.setText(txt);
		DispatchWork.server_IP	= vw2.getText().toString().trim();
		Log.i(TAG, "onBtnTask("+txt+")="+DispatchWork.server_IP);
		mTaskFragment.doCmd(DispatchWork.isConnected()?DispatchWork.CM_CANCEL:DispatchWork.CM_START);
	}

	/**
	 * Called when send button is clicked. Sends the write command to DispatchWork
	 * for handling.
	 * @param view calling UI object
	 */
	public void onBtnSend(View view) {
		DispatchWork.val	= vw1.getText().toString().trim();
		mTaskFragment.doCmd(DispatchWork.CM_WRITESRV);
	}

	/**
	 * Callback to update the views with the return values from the TaskThread.
	 * Called as result of an action of DispatchWork.
	 * @param t text to display
	 */
	public void updActivity(String t) {
		vw1.setText(DispatchWork.val);
		vw2.setText(vw2.getText() + t + " " );
	}


	/************************/
	/***** LOGS & STUFF *****/
	/************************/

/* ********************** * /
	@Override
	protected void onStart() {
		Log.i(TAG, "onStart "+this);
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume "+this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "onPause "+this);
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.i(TAG, "onStop "+this);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy "+this);
		super.onDestroy();
	}
/* ********************** */
}