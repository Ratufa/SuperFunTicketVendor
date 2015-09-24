package com.superfunapp.ticketvendor.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.superfunapp.ticketvendor.HomeScreen;
import com.superfunapp.ticketvendor.R;
import com.superfunapp.ticketvendor.servercommunication.NetworkAvailablity;
import com.superfunapp.ticketvendor.servercommunication.ServiceHandler;
import com.superfunapp.ticketvendor.servercommunication.WebServiceDetails;
import com.superfunapp.ticketvendor.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.ticketvendor.utils.AlertDialogManager;
import com.superfunapp.ticketvendor.utils.Constants;
import com.superfunapp.ticketvendor.utils.Utility;

public class CollectFragment extends Fragment implements OnClickListener {

	// TextView
	private TextView textActionTitle;

	private TextView branchName;

	private TextView branchAddress;

	// EditText
	private EditText etMobile;

	private EditText etConfirmCode;

	// ImageView
	private ImageView imageViewBack;

	private Button submitBtn;

	private Fragment fragment;

	private FragmentManager fm;

	private FragmentTransaction ft;

	private ProgressDialog progressDialog;

	private AlertDialogManager alertDialogManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		alertDialogManager = new AlertDialogManager();
		submitBtn = (Button) view.findViewById(R.id.submitBtn);
		etMobile = (EditText) view.findViewById(R.id.mobNumberEt);
		etConfirmCode = (EditText) view.findViewById(R.id.confirmCodeEt);
		branchAddress = (TextView) view.findViewById(R.id.branchAddress);
		branchName = (TextView) view.findViewById(R.id.branchName);

		branchAddress.setText(Html.fromHtml(SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.BRANCH_ADDRESS)));
		branchName.setText(SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.BRANCH_NAME));

		submitBtn.setOnClickListener(this);
		View tabs = getActivity().findViewById(R.id.tabLayout);
		tabs.setVisibility(View.VISIBLE);
		setupActionBar();
		setHasOptionsMenu(true);
		return view;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.action_settings).setVisible(false);
	}

	@SuppressLint("InflateParams")
	private void setupActionBar() {

		ActionBar actionBar = ((HomeScreen) getActivity()).getSupportActionBar();
		actionBar.setIcon(R.drawable.arrow_divide);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

		LayoutInflater mInflater = LayoutInflater.from(getActivity());

		View mCustomView = mInflater.inflate(R.layout.custom_action_layout, null);
		textActionTitle = (TextView) mCustomView.findViewById(R.id.screenTitle);
		textActionTitle.setText(getResources().getString(R.string.header_name));
		imageViewBack = (ImageView) mCustomView.findViewById(R.id.backBtn);

		imageViewBack.setVisibility(View.GONE);

		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);

		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar));

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submitBtn:

			String mobileStr = etMobile.getText().toString().trim();
			String confirmCode = etConfirmCode.getText().toString().trim();

			if (mobileStr.length() != 0 && confirmCode.length() != 0) {

				if (NetworkAvailablity.chkStatus(getActivity()))
					new CollectTicket().execute(mobileStr, confirmCode);
				else
					alertDialogManager.showAlertDialog(getActivity(), "Network Error", getResources().getString(R.string.network_connection));

			} else {

				if (mobileStr.length() == 0) {

					alertDialogManager.showAlertDialog(getActivity(), "Alert", getResources().getString(R.string.mob_not_specified));

				}
				else if (confirmCode.length() == 0) {
					alertDialogManager.showAlertDialog(getActivity(), "Alert", "Confirmation code not specified");

				}

			}

			break;

		default:
			break;
		}
	}

	public class CollectTicket extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
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
			nameValuePairs.add(new BasicNameValuePair("mob_no", params[0]));
			nameValuePairs.add(new BasicNameValuePair("branch_id", SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.BRANCH_ID)));
			nameValuePairs.add(new BasicNameValuePair("loc_id", SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.COMPANY_ID)));
			nameValuePairs.add(new BasicNameValuePair("confirmation_code", params[1]));
			nameValuePairs.add(new BasicNameValuePair("ip_address", Utility.getLocalIpAddress()));
			nameValuePairs.add(new BasicNameValuePair("operation", "collect_ticket"));
			nameValuePairs.add(new BasicNameValuePair("device_id", Utility.getDeviceId(getActivity())));
			String json = serviceHandler.makeServiceCall(WebServiceDetails.WS_URL + WebServiceDetails.COLLECT_TICKET, ServiceHandler.POST, nameValuePairs);

			Log.e("COLLECT_TICKET response", json + "--" + nameValuePairs);

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

						if (jsonObject.optString("status").equals("success")) {
							
							SharedPrefrnceSuperFun.setDataInSharedPrefrence(getActivity(), Constants.CONFIRMATION_CODE, jsonObject.optString("confirmation_code"));
							SharedPrefrnceSuperFun.setDataInSharedPrefrence(getActivity(), Constants.USER_MOB, jsonObject.optString("mobile"));
							SharedPrefrnceSuperFun.setDataInSharedPrefrence(getActivity(), Constants.TOTAL_TICKETS, jsonObject.optString("total_ticket"));
							
							alertDialogManager.showAlertDialog(getActivity(), "Message", "Tickets collected successfully");
							fragment = new TicketsCollector();
							ft = fm.beginTransaction();
							ft.replace(R.id.content_frame, fragment, "check");
							ft.addToBackStack(fm.getClass().getName());
							ft.commit();
						} /*else if (jsonObject.has("data")) {
							alertDialogManager.showAlertDialog(getActivity(), "Message", "Please Login again");

						}*/else {
							alertDialogManager.showAlertDialog(getActivity(), "Message", "Wrong mobile number or Code");

						}

					}
				} catch (Exception e) {
					e.printStackTrace();
					//alertDialogManager.showAlertDialog(getActivity(), "Network Error", "Server communication failed");
					// Toast.makeText(getActivity(), "Server communication failed", Toast.LENGTH_LONG).show();
				}

			}

		}
	}

}
