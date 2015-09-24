package com.superfunapp.ticketvendor.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.superfunapp.ticketvendor.HomeScreen;
import com.superfunapp.ticketvendor.R;
import com.superfunapp.ticketvendor.adapters.HistoryAdapter;
import com.superfunapp.ticketvendor.servercommunication.NetworkAvailablity;
import com.superfunapp.ticketvendor.servercommunication.ServiceHandler;
import com.superfunapp.ticketvendor.servercommunication.WebServiceDetails;
import com.superfunapp.ticketvendor.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.ticketvendor.utils.AlertDialogManager;
import com.superfunapp.ticketvendor.utils.Constants;

public class DailyReport extends Fragment {

	// ExpandableListView
	ListView purchaseHistoryView;

	// TextView
	private TextView textActionTitle;

	// ImageView
	private ImageView imageViewBack;

	private ProgressDialog progressDialog;

	private ArrayList<HashMap<String, String>> headerList;

	private AlertDialogManager alertDialogManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.purchase_history, container, false);
		purchaseHistoryView = (ListView) view.findViewById(R.id.purchaseHistoryList);
		View tabs = getActivity().findViewById(R.id.tabLayout);
		tabs.setVisibility(View.VISIBLE);

		alertDialogManager = new AlertDialogManager();

		Calendar c = Calendar.getInstance();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

		String formattedDate = df.format(c.getTime());

		System.out.println("Current time => " + c.getTime() + ">>>>>>>" + formattedDate);

		if (NetworkAvailablity.chkStatus(getActivity()))
			new VendorDailyReport().execute(formattedDate);
		else

			alertDialogManager.showAlertDialog(getActivity(), "Network Error", getResources().getString(R.string.network_connection));

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
		Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
		actionBar.setIcon(transparentDrawable);
		actionBar.setTitle("");
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);

		LayoutInflater mInflater = LayoutInflater.from(getActivity());

		View mCustomView = mInflater.inflate(R.layout.custom_action_layout, null);
		textActionTitle = (TextView) mCustomView.findViewById(R.id.screenTitle);
		textActionTitle.setText(getResources().getString(R.string.header_name));
		imageViewBack = (ImageView) mCustomView.findViewById(R.id.backBtn);
		imageViewBack.setVisibility(View.INVISIBLE);

		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);

		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar));

	}

	// purchase history API implementation
	public class VendorDailyReport extends AsyncTask<String, String, String> {

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
			headerList = new ArrayList<HashMap<String, String>>();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ServiceHandler serviceHandler = new ServiceHandler();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("dt", params[0]));
			nameValuePairs.add(new BasicNameValuePair("branch_id", SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.BRANCH_ID)));
			nameValuePairs.add(new BasicNameValuePair("operation", "Collect Tickets"));
			String json = serviceHandler.makeServiceCall(WebServiceDetails.WS_URL + WebServiceDetails.VENDOR_REPORT, ServiceHandler.POST, nameValuePairs);
			Log.e("VENDOR_REPORT response", json + "--");
			
			System.out.println("-----dadadddddd---"+params[0]);
			
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (result.length() != 0) {

				JSONArray jsonArray = null;
				try {
					jsonArray = new JSONArray(result);

					if (jsonArray != null) {

						if (jsonArray.optJSONObject(0).optString("data").equals("not found")) {
							alertDialogManager.showAlertDialog(getActivity(), "Message", "No reports available for today");
							//Toast.makeText(getActivity(), "No reports available for today", Toast.LENGTH_LONG).show();
						} else {
							for (int i = 0; i < jsonArray.length(); i++) {

								HashMap<String, String> map = new HashMap<String, String>();

								map.put("op_name", jsonArray.optJSONObject(i).optString("operation_name"));
								map.put("op_type", jsonArray.optJSONObject(i).optString("operation_type"));
								map.put("op_value", jsonArray.optJSONObject(i).optString("operation_value"));
								map.put("op_loc", jsonArray.optJSONObject(i).optString("operation_location"));
								map.put("op_date", jsonArray.optJSONObject(i).optString("date"));
								headerList.add(map);

							}
							if (!headerList.isEmpty()) {
								purchaseHistoryView.setAdapter(new HistoryAdapter(getActivity(), headerList));

							} else {
								alertDialogManager.showAlertDialog(getActivity(), "Message", "No reports available for today");

							}

						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//alertDialogManager.showAlertDialog(getActivity(), "Network Error", "Server communication failed");

				}

			}

		}
	}

}
