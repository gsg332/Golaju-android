package com.kcube.golaju.item;

import java.io.IOException;
import java.util.ArrayList;

import com.kcube.golaju.R;
import com.kcube.golaju.util.PropertyUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemListAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<GItem> arrData;
	private LayoutInflater inflater;

	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	
	public ItemListAdapter(Context c, ArrayList<GItem> arr, ImageLoader imageLoader, DisplayImageOptions options) {
		this.context = c;
		this.arrData = arr;
		this.imageLoader = imageLoader;
		this.options = options;  
		inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
		
		
	@Override
	public int getCount()
	{
		return arrData.size();
	}

	@Override
	public Object getItem(int position)
	{
		return arrData.get(position).getItemId();
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if(convertView == null){
			convertView = inflater.inflate(R.layout.gitem, parent, false);
		}
			  
		ImageView image = (ImageView)convertView.findViewById(R.id.image);
		
		try
		{
			String voteThumbUrl = PropertyUtil.getProperty("golaju.photo.url") + arrData.get(position).getVoteThumb();
			imageLoader.displayImage(voteThumbUrl, image, options);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		//image.setImageResource(arrData.get(position).getImage());
			  
		TextView title = (TextView) convertView.findViewById(R.id.text_itemTitle);
		title.setText(arrData.get(position).getTitle());
		convertView.setTag(arrData.get(position).getItemId());
			  
		return convertView;
	}

}
