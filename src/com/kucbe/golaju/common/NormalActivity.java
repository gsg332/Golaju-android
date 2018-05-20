package com.kucbe.golaju.common;

import java.io.File;
import java.util.ArrayList;

import com.kcube.golaju.R;
import com.kcube.golaju.data.Global;
import com.kcube.golaju.data.SaveDataAsClosed;
import com.kcube.golaju.data.SaveDataAsLogin;
import com.kcube.golaju.item.WriteForm;
import com.kcube.golaju.util.PropertyUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class NormalActivity extends Activity
{
	public Activity act;
	
	
	public ImageLoader imageLoader;
	public DisplayImageOptions options;
	
	
	public ImageView menuBtn;
	public ImageView btn_prePage;
	FrameLayout menuLayer;
	
	public ListView menulistView;
	public ArrayList<Item> list = new ArrayList<Item>();
		
	//public Bundle savedIS;
	
	
	public SaveDataAsLogin saveDataAsLogin = null; // 로그인 시 저장할 SharedPreferences 값.
	public SaveDataAsClosed saveDataAsClosed = null; // 강제 종료 될 때, 메모리 보존을 위한 SharedPreferences 값. 
	
	public Global userInfo;
	
	
	/**
	 * 초기화 세팅.
	 * @param act Activity
	 * @param setHeaderMenu 헤더바를 세팅 여부.
	 * @param isLoginActivity 현재 액티비티(화면)이 로그인 액티비티(화면)인지의 여부.
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void initSetting(Activity act, boolean setHeaderMenu, boolean isLoginActivity){
		
		
		if(!isSetting()){ // 초기 세팅이 되어 있지 않으면.
			
		
		
		
			this.act = act;
			//this.savedIS = savedInstanceState;
			
			//이 두줄을 써넣어야 인터넷 연결시 NetworkThead....오류가 발생하지 않음.
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			
		
	
			Log.i("PropertyUtil.isEmpty입니다.", PropertyUtil.pros + "");
			if(PropertyUtil.pros == null){
				new PropertyUtil(act); // activity를 넘겨 생성자로 초기화 시키기 위하여 어플 실행 후 한번만 실행.
			}
			
			
			Log.i("액티비티", act + "");
			

			saveDataAsLogin = new SaveDataAsLogin(act);
			saveDataAsClosed = new SaveDataAsClosed(act);
			
			userInfo = (Global) getApplication(); 
			
			if (!userInfo.isLogin() && !isLoginActivity){
				userInfo.setLogin(saveDataAsClosed.getLogin()); // 로그인 중임을 세팅.
				userInfo.setUserId(saveDataAsClosed.getUserId());
				userInfo.setLoginId(saveDataAsClosed.getLoginId());
				userInfo.setLoginPw(saveDataAsClosed.getLoginPw());
				userInfo.setUserName(saveDataAsClosed.getUserName());
				userInfo.setJobCode(saveDataAsClosed.getJobCode());
				userInfo.setThumbPath(saveDataAsClosed.getThumbPath());
				userInfo.setSavePath(saveDataAsClosed.getSavePath());
				userInfo.setAdmin(saveDataAsClosed.getAdmin());
				userInfo.setEmail(saveDataAsClosed.getEmail());
				userInfo.setGender(saveDataAsClosed.getGender());
				userInfo.setAgeGroup(saveDataAsClosed.getAgeGroup());
				
				
				Log.i("login", saveDataAsClosed.getLogin() + "");
				Log.i("userId", saveDataAsClosed.getUserId() + "");
				Log.i("loginId", saveDataAsClosed.getLoginId() + "");
				Log.i("loginPw", saveDataAsClosed.getLoginPw() + "");
				Log.i("userName", saveDataAsClosed.getUserName() + "");
				Log.i("jobCode", saveDataAsClosed.getJobCode() + "");
				Log.i("thumbPath", saveDataAsClosed.getThumbPath() + "");
				Log.i("savePath", saveDataAsClosed.getSavePath() + "");
				Log.i("admin.", saveDataAsClosed.getAdmin() + "");
				Log.i("email", saveDataAsClosed.getEmail() + "");
				Log.i("gender", saveDataAsClosed.getGender() + "");
				Log.i("ageGroup", saveDataAsClosed.getAgeGroup() + "");
				
				Toast.makeText(act, saveDataAsClosed.getUserId() + "", Toast.LENGTH_LONG).show();
			 }
			
			
			
			
			
			if(setHeaderMenu){
				settingHeaderMenu();
			}
			
			
			
			imageLoader = ImageLoader.getInstance();
	        File cacheDir = StorageUtils.getCacheDirectory(act);
	        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(act)
		        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
		        .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
		        .threadPoolSize(3) // default
		        .threadPriority(Thread.NORM_PRIORITY - 1) // default
		        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
		        .denyCacheImageMultipleSizesInMemory()
		        .memoryCache(new LruMemoryCache(50 * 1024 * 1024))
		        .memoryCacheSize(50 * 1024 * 1024)
		        .memoryCacheSizePercentage(13) // default
		        .discCache(new UnlimitedDiscCache(cacheDir)) // default
		        .discCacheSize(50 * 1024 * 1024)
		        .discCacheFileCount(100)
		        .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
		        .imageDownloader(new BaseImageDownloader(act)) // default
		        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
		        .writeDebugLogs()
		        .build();
	        imageLoader.init(config);
	        options = new DisplayImageOptions.Builder()
	        	.showStubImage(R.drawable.stub) //최초에 보이는 이미지.
				.showImageForEmptyUri(R.drawable.icon) //
				.showImageOnFail(R.drawable.user_default_58) //이미지를 불러오는 것에 실패했을 경우 보여줄 이미지.
				.resetViewBeforeLoading(true)
				.delayBeforeLoading(0)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Bitmap.Config.ARGB_8888)
				.displayer(new SimpleBitmapDisplayer())
				.handler(new Handler())
				.build();
	        
	        
	        
	        //Thread.setDefaultUncaughtExceptionHandler(new GlobalException(act));
		
	        
	        
	        
	        
		}
	}
	
	
	public boolean isSetting(){
		
		boolean isSetting = false;
		
		if(act != null){
			isSetting = true;
		}
		
		return isSetting;
	}
	
	

	
	public void settingHeaderMenu(){
		
		Log.i("헤더바 init","Start");
		
		//headerLoginId = (TextView) act.findViewById(R.id.headerLoginId);
			
		//Global userInfo = (Global) act.getApplication(); 
		//headerLoginId.setText(userInfo.getLoginId());
			
		menulistView = (ListView) act.findViewById(R.id.menu_listView);
		
		Log.i("menulistView", menulistView + "");
		
		list.add(new Item(R.drawable.ic_launcher,"홈으로"));
	    list.add(new Item(R.drawable.ic_launcher,"My페이지"));
	    list.add(new Item(R.drawable.ic_launcher,"계정설정"));
	    list.add(new Item(R.drawable.ic_launcher,"알림설정"));
	    list.add(new Item(R.drawable.ic_launcher,"서비스지원"));
	    list.add(new Item(R.drawable.ic_launcher,"로그아웃"));
	        
	    
	    
	    Log.i("액티비티2", act + "");
	    
	    
	    MenuListAdapter adapter = new MenuListAdapter(act, R.layout.menu_item, list); //어댑터 만들기
	        
	    
	    
	    Log.i("adapter", adapter + "");
	    
	    Log.i("menulistView", menulistView + "");
	    
	    menulistView.setAdapter(adapter); //리스트뷰와 어댑터 연결하기
	        
	    menulistView.setOnItemClickListener(new MenuListener(act));
		
	    menuBtn = (ImageView) act.findViewById(R.id.btn_menu);
	    menuBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) //hearderbar의 메뉴 버튼을 눌렀을 때 동작.
			{
				menuLayer = (FrameLayout) act.findViewById(R.id.menuArea);
				
				if(menuLayer.getVisibility() == View.VISIBLE){ //메뉴가 보이는 상태라면
					menuBtn.setImageResource(R.drawable.btn_menu_off);
					menuLayer.setAnimation(AnimationUtils.loadAnimation(act, R.anim.menu_hide));
					menuLayer.setVisibility(View.INVISIBLE);
				}else{
					menuBtn.setImageResource(R.drawable.btn_menu_on);
					menuLayer.setAnimation(AnimationUtils.loadAnimation(act, R.anim.menu_show));
					menuLayer.setVisibility(View.VISIBLE);
				}
			}
		});
	    
	    
	    
	    
	   /* ImageButton voteWriteBtn = (ImageButton) act.findViewById(R.id.btn_voteWrite);
	    voteWriteBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) //hearderbar의 메뉴 버튼을 눌렀을 때 동작.
			{
				LoadingProgress.showLoadingDialog(act);
				new Thread(new Runnable(){
					@Override
					public void run()
					{
						Intent intent = new Intent(act, WriteForm.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						act.startActivity(intent);
					
						LoadingProgress.getHandler().sendEmptyMessage(0);
					}
				}).start();
			}
		});*/
	    
	    
	    btn_prePage = (ImageView) act.findViewById(R.id.btn_pre); 
		btn_prePage.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				((ImageView) v).setImageResource(R.drawable.btn_back_on);
				act.onBackPressed();
			}
		});
	    
	    
	    
	    Log.i("헤더바 init","End");
    }
	
	
	
	
	
	@Override
	public void onPause()
	{
		super.onPause();
		
		/*
		// 이전, 메뉴 버튼 클릭 표시를 해제 한다.
		if(menuBtn != null){
			menuBtn.setImageResource(R.drawable.btn_menu_off);
			
			if(menuLayer != null){ 
				menuLayer.setVisibility(View.INVISIBLE); // 메뉴 닫기.
			}
		}
		if(btn_prePage != null){
			btn_prePage.setImageResource(R.drawable.btn_back_off);
		}*/
		
		
		saveDataAsClosed.setLogin(userInfo.isLogin());
		saveDataAsClosed.setUserId(userInfo.getUserId());
		saveDataAsClosed.setLoginPw(userInfo.getLoginPw());
		saveDataAsClosed.setUserName(userInfo.getUserName());
		saveDataAsClosed.setJobCode(userInfo.getJobCode());
		saveDataAsClosed.setThumbPath(userInfo.getThumbPath());
		saveDataAsClosed.setSavePath(userInfo.getSavePath());
		saveDataAsClosed.setAdmin(userInfo.isAdmin());
		saveDataAsClosed.setEmail(userInfo.getEmail());
		saveDataAsClosed.setAgeGroup(userInfo.getAgeGroup());
		

		Log.i("NormalActivity onPause", userInfo.getLoginId() + "");
		
		if(userInfo.getLoginId() == null){
			saveDataAsClosed.setLoginId(userInfo.getLoginId());
		}else if(act.getIntent().hasExtra("loginId")){
			saveDataAsClosed.setLoginId(act.getIntent().getExtras().get("loginId").toString());
		}
		if(userInfo.getGender() != 0){
			saveDataAsClosed.setGender(userInfo.getGender());
		}else if(act.getIntent().hasExtra("gender")){
			saveDataAsClosed.setGender(act.getIntent().getExtras().getInt("gender"));
		}
		
		
		
		
		// 메모리 clear로 인하여 데이터가 날아갈 경우 intent(페이지이동)시 넘겨 받은 값을 savedInstanceState에 저장한 후 사용. 
		if(act.getIntent().hasExtra("itemId")){
			saveDataAsClosed.setItemId(act.getIntent().getExtras().getLong("itemId"));
		}
		if(act.getIntent().hasExtra("title")){
			saveDataAsClosed.setTitle(act.getIntent().getExtras().get("title").toString());
		}
		if(act.getIntent().hasExtra("content")){
			saveDataAsClosed.setContent(act.getIntent().getExtras().get("content").toString());
		}
		if(act.getIntent().hasExtra("list")){
			saveDataAsClosed.setList(act.getIntent().getExtras().get("list").toString());
		}
				
		saveDataAsClosed.editCommit();
		
	}

}
