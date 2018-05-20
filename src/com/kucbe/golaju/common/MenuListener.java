package com.kucbe.golaju.common;

import com.kcube.golaju.data.SaveDataAsClosed;
import com.kcube.golaju.data.SaveDataAsLogin;
import com.kcube.golaju.item.ItemList;
import com.kcube.golaju.login.Login;
import com.kcube.golaju.mypage.Mypage;
import com.kcube.golaju.option.Option;
import com.kcube.golaju.notification.Notification;
import com.kcube.golaju.service.Service;
import com.kcube.golaju.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MenuListener implements AdapterView.OnItemClickListener
{
	final private int home = 0;
	final private int myPage = 1;
	final private int account_conf = 2;
	final private int alimi_conf = 3;
	final private int service = 4;
	final private int logout = 5;

	Activity act;
	
	public MenuListener(Activity act){
		this.act = act;
	}
	
	@Override
	public void onItemClick(AdapterView av, final View v, final int position, long id)
	{
		/*if(position == home){
			Intent intent = null;
			intent = new Intent(v.getContext(), ItemList.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			v.getContext().startActivity(intent);
		}else{*/
		
			LoadingProgress.showLoadingDialog(v.getContext());
			new Thread(new Runnable(){
				@Override
				public void run()
				{
					Intent intent = null;
					
					switch (position)
					{
						case home :
							intent = new Intent(v.getContext(), ItemList.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							v.getContext().startActivity(intent);
							break;
						case myPage : 
							Log.d("My페이지", position + "My페이지"); 
							intent = new Intent(v.getContext(), Mypage.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							Bundle bundle = new Bundle() ;
							bundle.putString("listType", "notClosed");
							intent.putExtras(bundle) ;
							v.getContext().startActivity(intent);
							break;
						case account_conf : 
							Log.d("계정설정", position + "계정설정"); 
							intent = new Intent(v.getContext(), Option.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							v.getContext().startActivity(intent);
							break;
						case alimi_conf : 
							Log.d("알림설정", position + "알림설정"); 
							intent = new Intent(v.getContext(), Notification.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							v.getContext().startActivity(intent);
							break;
						case service : 
							Log.d("서비스지원", position + "서비스지원"); 
							
							intent = new Intent(v.getContext(), Service.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							v.getContext().startActivity(intent);
							break;
						case logout : 
							
							SaveDataAsClosed saveDataAsClosed = new SaveDataAsClosed(v.getContext());
							saveDataAsClosed.setLogin(false);
							
							// 자동 로그인 해제.				
							SaveDataAsLogin saveDataAsLogin = new SaveDataAsLogin(v.getContext());
							saveDataAsLogin.setLoginId("");
							saveDataAsLogin.setLoginPw("");
							saveDataAsLogin.setAutoLogin(false);
							saveDataAsLogin.editCommit();
				
							intent = new Intent(v.getContext(), Login.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							v.getContext().startActivity(intent);
			
							break;
						default: 
							Log.d("aaadsfd", position + "디폴특값"); 
							
							break;
					}
					
					LoadingProgress.getHandler().sendEmptyMessage(0);
					
				}
			}).start();
		//}
		
		// 메뉴버튼을 클릭한 후 메뉴목록 닫기.
		FrameLayout menuLayer = (FrameLayout) act.findViewById(R.id.menuArea);
		menuLayer.setVisibility(View.INVISIBLE);
		
		// 메뉴버튼 클릭 표시 해제.
		ImageView btn_menu = (ImageView) act.findViewById(R.id.btn_menu);
		btn_menu.setImageResource(R.drawable.btn_menu_off);
		
		
	}
}
