package com.superfunapp.ticketvendor.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogManager {

	

	public void showAlertDialog(Context context, String title, String message) {


		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

		//this.fragmentName = fragmentNm;
		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting OK Button
		alertDialog.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//changeFragment(fragmentName);
						dialog.dismiss();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	
}
