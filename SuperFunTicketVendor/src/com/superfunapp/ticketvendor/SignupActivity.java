package com.superfunapp.ticketvendor;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.superfunapp.ticketvendor.servercommunication.NetworkAvailablity;
import com.superfunapp.ticketvendor.servercommunication.ServiceHandler;
import com.superfunapp.ticketvendor.servercommunication.WebServiceDetails;
import com.superfunapp.ticketvendor.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.ticketvendor.utils.AlertDialogManager;
import com.superfunapp.ticketvendor.utils.Constants;

public class SignupActivity extends Activity implements OnClickListener {

	// EditText
	private EditText etUUID;

	private EditText etBranchCode;

	private EditText etPassword;

	private EditText etSimNumber;

	// Button
	private Button submitButton;

	private ProgressDialog progressDialog;

	public static Activity signUp;

	private AlertDialogManager alertDialogManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		signUp = this;
		alertDialogManager = new AlertDialogManager();
		if (SharedPrefrnceSuperFun.getSharedPrefData(signUp, "vendor_verified") != null) {
			if (SharedPrefrnceSuperFun.getSharedPrefData(signUp, "vendor_verified").equals("true")) {
				Intent intent = new Intent(signUp, HomeScreen.class);
				startActivity(intent);

				finish();
			}

		} else {
			setContentView(R.layout.signup_screen);
			signUp = this;
			etUUID = (EditText) findViewById(R.id.uuidEt);
			etBranchCode = (EditText) findViewById(R.id.branchCodeEt);
			etPassword = (EditText) findViewById(R.id.passwordEt);
			etSimNumber = (EditText) findViewById(R.id.simNumEt);

			submitButton = (Button) findViewById(R.id.submitBtn);

			submitButton.setOnClickListener(this);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.submitBtn:
			String uuidStr = etUUID.getText().toString().trim();
			String passwordStr = etPassword.getText().toString().trim();
			String simNumberStr = etSimNumber.getText().toString().trim();
			String branchCode = etBranchCode.getText().toString().trim();
			
			SharedPrefrnceSuperFun.setDataInSharedPrefrence(SignupActivity.this, Constants.BRANCH_ID, branchCode);
			
			if(uuidStr.length() == 0 || passwordStr.length() == 0 || simNumberStr.length() == 0 || branchCode.length() == 0){
				if(uuidStr.length() == 0){
					alertDialogManager.showAlertDialog(this, "Alert", "Please enter UUID");
				}else if(simNumberStr.length() == 0){
					alertDialogManager.showAlertDialog(this, "Alert", "Please enter SIM Number");
				}else if(passwordStr.length() == 0){
					alertDialogManager.showAlertDialog(this, "Alert", "Please enter Password");
				}else{
					alertDialogManager.showAlertDialog(this, "Alert", "Please enter Branch Code");
				}
			}else{
				if (NetworkAvailablity.chkStatus(this))
					new RegisterVendor().execute(uuidStr, simNumberStr, branchCode, passwordStr);
				else
					alertDialogManager.showAlertDialog(this, "Network Error", getResources().getString(R.string.network_connection));

			}

			/*if (uuidStr.length() != 0 && passwordStr.length() != 0 && simNumberStr.length() != 0 && branchCode.length() != 0) {

				SharedPrefrnceSuperFun.setDataInSharedPrefrence(signUp, Constants.BRANCH_ID, branchCode);

				if (NetworkAvailablity.chkStatus(this))
					new RegisterVendor().execute(uuidStr, simNumberStr, branchCode, passwordStr);
				else
					alertDialogManager.showAlertDialog(this, "Network Error", getResources().getString(R.string.network_connection));

			} else {
				alertDialogManager.showAlertDialog(this, "Error", "Please enter all fields");
			}*/

			break;

		default:
			break;
		}
	}

	// Register vendor API implementation

	public class RegisterVendor extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(SignupActivity.this);
			progressDialog.setMessage(getResources().getString(R.string.please_wait));
			progressDialog.setIndeterminate(true);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ServiceHandler serviceHandler = new ServiceHandler();

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("UU_id", params[0]));
			nameValuePairs.add(new BasicNameValuePair("sim_no", params[1]));
			nameValuePairs.add(new BasicNameValuePair("branch_id", params[2]));
			nameValuePairs.add(new BasicNameValuePair("password", params[3]));
			nameValuePairs.add(new BasicNameValuePair("station_type", "tickets"));

			String json = serviceHandler.makeServiceCall(WebServiceDetails.WS_URL + WebServiceDetails.REGISTER, ServiceHandler.POST, nameValuePairs);

			Log.e("REGISTER response", json + "--");

			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (result.length() != 0) {

				JSONObject jsonObject = null;
				try {

					jsonObject = new JSONObject(result);

					if (jsonObject != null) {
						JSONObject data = jsonObject.optJSONObject("detail");

						SharedPrefrnceSuperFun.setDataInSharedPrefrence(signUp, "vendor_verified", "true");
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(SignupActivity.this, Constants.BRANCH_ADDRESS, data.optString("address"));
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(SignupActivity.this, Constants.BRANCH_NAME, data.optString("branchname"));
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(SignupActivity.this, Constants.COMPANY_NAME, data.optString("companyname"));
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(SignupActivity.this, Constants.COMPANY_ID, data.optString("companyid"));
						
						System.out.println("-----idddddd-----"+data.optString("companyid"));
						
						Intent intent = new Intent(signUp, HomeScreen.class);
						startActivity(intent);

						finish();

					}

				} catch (Exception e) {
					e.printStackTrace();
					alertDialogManager.showAlertDialog(SignupActivity.this, "Error", "Invalid information. Please try again.");
				}

			}

		}
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		showAlertgbtgrb();
	//	overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}
	public void showAlertgbtgrb() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_NEGATIVE:
					//overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
					dialog.dismiss();
					
					finish();
					break;
				case DialogInterface.BUTTON_POSITIVE:
					overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
					dialog.dismiss();
					break;
				}
			}
		};
		builder.setMessage("Do you want to exit?").setPositiveButton("No", dialogClickListener).setNegativeButton("Yes", dialogClickListener).show();

		/*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);
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
						overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
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
		alertDialog.show();*/
	}
}
