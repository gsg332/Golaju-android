package com.kcube.golaju.user;

import java.net.URLEncoder;
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


public class UserListAdapter extends BaseAdapter {
	
	private Context context;
 	private ArrayList<User> arrData;
 	private LayoutInflater inflater;
 	private ImageLoader imageLoader;
 	private DisplayImageOptions options;
 
 	public UserListAdapter(Context c, ArrayList<User> arr, ImageLoader imageLoader, DisplayImageOptions options) {
  		this.context = c;
  		this.arrData = arr;
  		inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // inflate(View를 생성하여 추가하는 것)할 수 있도록 현재 액티비티에 대한 inflater 가져오기. 

  		this.imageLoader = imageLoader;
  		this.options = options;
 	}

 	@Override
	public int getCount()
	{
		return arrData.size();
	}

	@Override
	public Object getItem(int position)
	{
		return arrData.get(position).getUserId();
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
  		
		if(convertView == null){
   			convertView = inflater.inflate(R.layout.inflate_vote_user, parent, false); // 목록하나를 그리기 위하여 위에서 추가한 목록 레이아웃을 가지고 목록 View를 만든다.
  		}
		
  		ImageView ivThumb = (ImageView) convertView.findViewById(R.id.thumbPath);
  		
  		String userThumbUrl;
		try
		{
			userThumbUrl = PropertyUtil.getProperty("golaju.photo.url") + URLEncoder.encode(arrData.get(position).getThumbPath(),"utf-8");
			//String userThumbUrl = "https://www.oaasys.com/AttachmentAction.DoDownload.do?inline=true&path=thumb%5C9%5C4%5C4%5Coaasys8236564462443968945.JPG";
			imageLoader.displayImage(userThumbUrl, ivThumb, options);
			
		   /* imageLoader.displayImage(userThumbUrl, ivThumb, options, new ImageLoadingListener() {
		        @Override
		        public void onLoadingStarted(String imageUri, View view) {
		        }
		        @Override
		        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		        	PhotoViewAttacher mAttacher = new PhotoViewAttacher((ImageView)view);
					mAttacher.update(); //이미지 확대 축소 기능 구현.
		        }
		        @Override
		        public void onLoadingCancelled(String imageUri, View view) {
		        }
				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason)
				{
				}
		    });*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
  		
  		TextView tvGender = (TextView) convertView.findViewById(R.id.gender);
  		tvGender.setText(arrData.get(position).getGender()==1? "남성" : "여성");
  		
  		TextView tvAgeGroup = (TextView) convertView.findViewById(R.id.ageGroup);
  		int ageGroup = arrData.get(position).getAgeGroup();
  		tvAgeGroup.setText(ageGroup + (ageGroup==70? "대 이상" : "대"));

  		return convertView;
 	}
}
