package com.superfunapp.ticketvendor.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superfunapp.ticketvendor.R;

public class HistoryAdapter extends BaseAdapter {

	//private Context context;
	private ArrayList<HashMap<String, String>> historyList;
	private LayoutInflater inflater;

	public HistoryAdapter(Context ctx,
			ArrayList<HashMap<String, String>> dataList) {
		this.historyList = dataList;
		//this.context = ctx;
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return historyList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return historyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;

		if (convertView == null) {

			viewHolder = new ViewHolder();

			convertView = inflater.inflate(R.layout.purchase_item_parent, null);

			viewHolder.layout = (LinearLayout) convertView
					.findViewById(R.id.headerLayout);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.itemNameTv);
			viewHolder.date = (TextView) convertView
					.findViewById(R.id.itemDateTv);
			viewHolder.type = (TextView) convertView
					.findViewById(R.id.itemTypeTv);
			viewHolder.address = (TextView) convertView
					.findViewById(R.id.itemAddress);
			viewHolder.arrowImage = (ImageView) convertView
					.findViewById(R.id.indicatorImg);
			viewHolder.totalBranch = (TextView) convertView
					.findViewById(R.id.itemValueTv);

		

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.name.setText(historyList.get(position).get(
				"op_name"));
		viewHolder.address.setText(Html.fromHtml(historyList.get(position)
				.get("op_loc")));
		viewHolder.date.setText(historyList.get(position).get(
				"op_date"));
		viewHolder.type.setText(historyList.get(position).get(
				"op_type"));
		viewHolder.totalBranch.setText(historyList.get(position).get(
				"op_value"));

		viewHolder.layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (viewHolder.address.getVisibility() == View.VISIBLE) {
					viewHolder.address.setVisibility(View.GONE);
					viewHolder.arrowImage
							.setImageResource(R.drawable.arrowdown);
				} else {
					viewHolder.address.setVisibility(View.VISIBLE);
					viewHolder.arrowImage
							.setImageResource(R.drawable.arrowup);
				}
			}
		});
		return convertView;
	}

	public static class ViewHolder {

		public TextView name;
		public TextView type;
		public TextView date;
		public TextView address;
		public TextView totalBranch;		
		public ImageView arrowImage;
		public LinearLayout layout;
	}

}
