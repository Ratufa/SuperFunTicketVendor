package com.superfunapp.ticketvendor;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.superfunapp.ticketvendor.fragments.CollectFragment;
import com.superfunapp.ticketvendor.fragments.DailyReport;
import com.superfunapp.ticketvendor.utils.Utility;

public class HomeScreen extends ActionBarActivity implements OnClickListener {

	//Button
	private Button buttonCollect;

	private Button buttonHistory;

	private DisplayMetrics dimension;

	//Fragment
	private Fragment fragment;

	private FragmentManager fm;

	private FragmentTransaction ft;
	
	private boolean pressed=false;

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.main_screen);
		Utility.setThreadPolicy();
		fm = getSupportFragmentManager();
		buttonCollect = (Button) findViewById(R.id.collectBtn);
		buttonHistory = (Button) findViewById(R.id.historyBtn);
		
		buttonHistory.setSelected(false);
		buttonCollect.setSelected(true);

		dimension = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dimension);
		setUpActionBar();
		
		if (savedInstanceState == null) {
				// On home screen load fragment view
		pressed = true;	
		fragment = new CollectFragment();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.content_frame, fragment, "check");
		//ft.addToBackStack(fm.getClass().getName());
		ft.commit();
		}
		
		buttonCollect.setOnClickListener(this);
		buttonHistory.setOnClickListener(this);	

	}

	public void setUpActionBar() {

		getSupportActionBar().setIcon(R.drawable.arrow_divide);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.action_bar));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		menu.findItem(R.id.action_settings).setVisible(true);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_settings:
			//popupWindowDialog();
			return true;
		case android.R.id.home:
			onBackPressed();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.historyBtn:
			pressed = false;
			buttonHistory.setSelected(true);
			buttonCollect.setSelected(false);
			fragment = new DailyReport();
			ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, fragment, "check");
		//	ft.addToBackStack(fm.getClass().getName());
			ft.commit();
			break;
		case R.id.collectBtn:
			buttonHistory.setSelected(false);
			buttonCollect.setSelected(true);

			if(!pressed){
			fragment = new CollectFragment();
			ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, fragment, "check");
		//	ft.addToBackStack(fm.getClass().getName());
			ft.commit();
			pressed = true;
			}
			break;
	
		case R.id.backBtn:
			onBackPressed();
			break;

		default:
			break;
		}
	}

	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		int count = fm.getBackStackEntryCount();
		//showAlert();
		if (count > 1) {
			super.onBackPressed();
		} else {
			showAlert();
		}

	}
	
	public void showAlert() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set title
		alertDialogBuilder.setTitle("Alert");
		// set dialog message
		alertDialogBuilder
		.setMessage(
				("Do you want to exit?"))
				.setCancelable(false)
				.setPositiveButton(("Yes"),
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						/*overridePendingTransition(
								R.anim.trans_right_in,
								R.anim.trans_right_out);*/
						overridePendingTransition(R.anim.trans_right_in,
								R.anim.trans_right_out);
						finish();

					}
				})
				.setNegativeButton(("No"),
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}

}
