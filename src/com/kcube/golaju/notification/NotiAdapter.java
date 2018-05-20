package com.kcube.golaju.notification;

import java.util.ArrayList;

import com.kcube.golaju.R;
import com.kcube.golaju.data.SaveDataAsLogin;

import android.R.integer;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class NotiAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<NotiItem> arrData;
	private LayoutInflater inflater;
	private ViewHolder viewHolder = null;
	private boolean[] isCheckedConfrim; 
	
	public NotiAdapter(Context c, ArrayList<NotiItem> arr){
		this.context = c;
		this.inflater = LayoutInflater.from(c);
		this.arrData = arr;
		this.isCheckedConfrim = new boolean[arrData.size()];
	}
	
	public void setChecked(int position) {
		String a= null;
        isCheckedConfrim[position] = !isCheckedConfrim[position];
    }

    public String getChecked(int position){
    	String check= null;
    	if(isCheckedConfrim[position]){
    		check="true";
    	}else{
    		check="false";
    	}
    	
        return check;
    }
    
    class ViewHolder {
        // 새로운 Row에 들어갈 CheckBox
        private CheckBox cBox = null;
        private TextView title = null;
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
        View v = convertView;
        
        viewHolder = new ViewHolder();
        // View를 inflater 시켜준다.
        v = inflater.inflate(R.layout.noti_item, parent, false);
        viewHolder.cBox = (CheckBox) v.findViewById(R.id.noti_box);
        viewHolder.cBox.setChecked(isCheckedConfrim[position]);
        viewHolder.title = (TextView) v.findViewById(R.id.text_notiItemTitle);
        viewHolder.title.setText(arrData.get(position).notiTitle);
        v.setTag(viewHolder);

        viewHolder.cBox.setClickable(false);
        viewHolder.cBox.setFocusable(false);
        
		return v;
	}
}
