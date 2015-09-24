package com.superfunapp.ticketvendor.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.superfunapp.ticketvendor.HomeScreen;
import com.superfunapp.ticketvendor.R;
import com.superfunapp.ticketvendor.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.ticketvendor.utils.Constants;

public class TicketsCollector extends Fragment implements OnClickListener {

	// TextView
	private TextView textActionTitle;

	private TextView branchAddress;

	private TextView branchName;

	private TextView mobNumberTv;
	
	private TextView confirmCodeTv;
	
	private TextView totalTicketTv;

	private Button doneBtn;
	
	// ImageView
	private ImageView imageViewBack;
	
	private Fragment fragment;

	private FragmentManager fm;

	private FragmentTransaction ft;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.tickets_collector, container,
				false);
		fm = ((HomeScreen) getActivity()).getSupportFragmentManager();
		branchAddress  = (TextView) view.findViewById(R.id.branchAddressTv);
		branchName  = (TextView) view.findViewById(R.id.branchNameTv);
		mobNumberTv  = (TextView) view.findViewById(R.id.mobNumberEt);
		confirmCodeTv  = (TextView) view.findViewById(R.id.confirmCodeTv);
		totalTicketTv  = (TextView) view.findViewById(R.id.totalTicketTv);
		
		doneBtn = (Button) view.findViewById(R.id.doneBtn);
		
		branchAddress.setText(Html.fromHtml(SharedPrefrnceSuperFun.getSharedPrefData(
				getActivity(), Constants.BRANCH_ADDRESS)));
		branchName.setText(SharedPrefrnceSuperFun.getSharedPrefData(
				getActivity(), Constants.BRANCH_NAME));
		
		mobNumberTv.setText(SharedPrefrnceSuperFun.getSharedPrefData(
				getActivity(), Constants.USER_MOB));
		confirmCodeTv.setText(SharedPrefrnceSuperFun.getSharedPrefData(
				getActivity(), Constants.CONFIRMATION_CODE));
		totalTicketTv.setText(SharedPrefrnceSuperFun.getSharedPrefData(
				getActivity(), Constants.TOTAL_TICKETS));
		
		doneBtn.setOnClickListener(this);
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

		ActionBar actionBar = ((HomeScreen) getActivity())
				.getSupportActionBar();
		actionBar.setIcon(R.drawable.arrow_divide);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		LayoutInflater mInflater = LayoutInflater.from(getActivity());

		View mCustomView = mInflater.inflate(R.layout.custom_action_layout,
				null);
		textActionTitle = (TextView) mCustomView.findViewById(R.id.screenTitle);
		textActionTitle.setText(getResources().getString(
				R.string.header_name));
		imageViewBack = (ImageView) mCustomView.findViewById(R.id.backBtn);

		imageViewBack.setVisibility(View.GONE);

		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);

		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.action_bar));

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.doneBtn:
			fragment = new CollectFragment();
			ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, fragment, "check");
			ft.addToBackStack(fm.getClass().getName());
			ft.commit();
			break;

		default:
			break;
		}
	}

	
}
