package com.kcube.golaju.item;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.android.gcm.GCMRegistrar;
import com.kcube.golaju.R;
import com.kcube.golaju.data.Global;
import com.kcube.golaju.data.SaveDataAsLogin;
import com.kcube.golaju.login.Login;
import com.kcube.golaju.util.PropertyUtil;
import com.kcube.golaju.util.XmlUtil;
import com.kucbe.golaju.common.LoadingProgress;
import com.kucbe.golaju.common.NormalActivity;
 
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

public class ItemList extends NormalActivity implements OnScrollListener{
	
	LinearLayout notClosedList;
	LinearLayout closedList;
	
	ImageView btn_notClosedList;
	ImageView btn_closedList;
	ImageView btn_write;
	
	Long notClosedMinId = null;
	Long closedMinId = null;
	
	ListView lvNotClosedItem;
	ListView lvClosedItem;
	
	ItemListAdapter notClosedAdapter = null;
	ItemListAdapter closedAdapter = null;
	
	ArrayList<GItem> notClosedItemList = new ArrayList<GItem>();
	ArrayList<GItem> closedItemList = new ArrayList<GItem>();
	
	boolean notClosedLastItemVisible = false;     
	boolean closedLastItemVisible = false;
	
	String listType = null;
	
	
	//int INT_TIME;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		Log.i("ItemList Excute","onCreate");
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_list);
		
		
		
		
		
		
		
		
		
		/* 메모리로 인한 데이터 clear테스트를 위한 시간 체크용을 위해  사용.
		final Thread THREAD_CHECK_TIME = new Thread(new Runnable(){

			@Override
			public void run()
			{
				
				while(true){
					
					Log.i("List time입니다 : ", "tlc : " + INT_TIME);
					INT_TIME++;
					
					try{
						Thread.sleep(1000 * 60);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
			
			
		});
		
		THREAD_CHECK_TIME.start();
		*/
		
		
		
		
		
		
		
		initSetting(this, true, false); // 공통적인 초기 새팅을 한다.
		
		notClosedList = (LinearLayout) findViewById(R.id.notClosedList); // 진행중인 목록 영역
		closedList = (LinearLayout) findViewById(R.id.closedList); // 완료 목록 영역
		btn_notClosedList = (ImageView) findViewById(R.id.btn_notClosedList); // 진행중인 목록 버튼
		btn_closedList = (ImageView) findViewById(R.id.btn_closedList); // 완료중인 목록 버튼
		btn_write = (ImageView) findViewById(R.id.btn_write); // 글쓰기 버튼.
		lvNotClosedItem = (ListView) findViewById(R.id.notClosedListView); // 진행중인 목록 리스트뷰
		lvClosedItem = (ListView) findViewById(R.id.closedListView); // 완료 목록 리스트뷰
		
		
		
		
		
		
		if(getIntent().hasExtra("listType")){
			listType = getIntent().getExtras().get("listType").toString();
			if(listType.equals("closed")){
				
				
				
				
				runOnUiThread(new Runnable(){
					@Override public void run(){
						
						btn_notClosedList.setEnabled(false);
						btn_closedList.setEnabled(false);
						
						notClosedList.setVisibility(View.INVISIBLE);
						closedList.setVisibility(View.VISIBLE);
						
						btn_notClosedList.setImageResource(R.drawable.not_closed_list);
						btn_notClosedList.setBackgroundColor(Color.parseColor("#525457"));
						btn_closedList.setImageResource(R.drawable.closed_list);
						btn_closedList.setBackgroundColor(Color.parseColor("#33b5e5"));
						btn_write.setImageResource(R.drawable.btn_write);
						btn_write.setBackgroundColor(Color.parseColor("#525457"));
					}
				});
				
				closedItemList.clear();
				
				DrawItemList("closed", lvClosedItem, closedItemList, null, null);
				
				lvClosedItem.setOnScrollListener(ItemList.this);
				
				runOnUiThread(new Runnable(){
					@Override public void run(){
						btn_notClosedList.setEnabled(true);
						btn_closedList.setEnabled(true);
					}
				});
			}else if(listType.equals("notClosed")){
				DrawItemList("notClosed", lvNotClosedItem, notClosedItemList, null, null);
				btn_notClosedList.setImageResource(R.drawable.not_closed_list);
				btn_notClosedList.setBackgroundColor(Color.parseColor("#33b5e5"));
				btn_closedList.setImageResource(R.drawable.closed_list);
				btn_closedList.setBackgroundColor(Color.parseColor("#525457"));
				btn_write.setImageResource(R.drawable.btn_write);
				btn_write.setBackgroundColor(Color.parseColor("#525457"));
			}
		}else{
			DrawItemList("notClosed", lvNotClosedItem, notClosedItemList, null, null);
			btn_notClosedList.setImageResource(R.drawable.not_closed_list);
			btn_notClosedList.setBackgroundColor(Color.parseColor("#33b5e5"));
			btn_closedList.setImageResource(R.drawable.closed_list);
			btn_closedList.setBackgroundColor(Color.parseColor("#525457"));
			btn_write.setImageResource(R.drawable.btn_write);
			btn_write.setBackgroundColor(Color.parseColor("#525457"));
		}
		
		
		
		
		
		
		
		
		lvNotClosedItem.setOnScrollListener(this);
		lvClosedItem.setOnScrollListener(this);
		
		saveDataAsLogin = new SaveDataAsLogin(ItemList.this);
		if(!saveDataAsLogin.getFirstRun()){//처음 어플 실행인지 체크
			saveDataAsLogin.setMail(true);//첫 어플 실행시 메일수신 여부 true로 셋팅
			saveDataAsLogin.setPush(true);//첫 어플 실행시 PUSH수신 여부 true로 셋팅
			saveDataAsLogin.setVibrate(true);
			saveDataAsLogin.setFirstRun(true);
			saveDataAsLogin.editCommit();
		}
		
		btn_notClosedList.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
				
				
				LoadingProgress.showLoadingDialog(ItemList.this);
				new Thread(new Runnable(){
					@Override
					public void run()
					{
						
						
						runOnUiThread(new Runnable(){
							@Override public void run(){
								btn_notClosedList.setEnabled(false);
								btn_closedList.setEnabled(false);
								
								notClosedList.setVisibility(View.VISIBLE);
								closedList.setVisibility(View.INVISIBLE);
								
								btn_notClosedList.setImageResource(R.drawable.not_closed_list);
								btn_notClosedList.setBackgroundColor(Color.parseColor("#33b5e5"));
								btn_closedList.setImageResource(R.drawable.closed_list);
								btn_closedList.setBackgroundColor(Color.parseColor("#525457"));
							}
						});
						
						
						
						
						
						
						
						
						
						
						
						notClosedItemList.clear();
						
						DrawItemList("notClosed", lvNotClosedItem, notClosedItemList, null, null);
						
						lvNotClosedItem.setOnScrollListener(ItemList.this);
						
						
						
						
						LoadingProgress.getHandler().sendEmptyMessage(0);
						
						
						
						runOnUiThread(new Runnable(){
							@Override public void run(){
								btn_notClosedList.setEnabled(true);
								btn_closedList.setEnabled(true);
							}
						});
						
						
						
						
					}
				}).start();
					
						
				
			}
		});
		
		btn_closedList.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
				
				LoadingProgress.showLoadingDialog(ItemList.this);
				new Thread(new Runnable(){
					@Override
					public void run()
					{
						
						
						
						
						
						runOnUiThread(new Runnable(){
							@Override public void run(){
								btn_notClosedList.setEnabled(false);
								btn_closedList.setEnabled(false);
								
								
								notClosedList.setVisibility(View.INVISIBLE);
								closedList.setVisibility(View.VISIBLE);
								
								btn_notClosedList.setImageResource(R.drawable.not_closed_list);
								btn_notClosedList.setBackgroundColor(Color.parseColor("#525457"));
								btn_closedList.setImageResource(R.drawable.closed_list);
								btn_closedList.setBackgroundColor(Color.parseColor("#33b5e5"));
							}
						});
						
						
						
						
						
						
						
						
						
						closedItemList.clear();
						
						DrawItemList("closed", lvClosedItem, closedItemList, null, null);
						
						lvClosedItem.setOnScrollListener(ItemList.this);
					
						
						
						
						LoadingProgress.getHandler().sendEmptyMessage(0);
						
						
						
						
						runOnUiThread(new Runnable(){
							@Override public void run(){
								btn_notClosedList.setEnabled(true);
								btn_closedList.setEnabled(true);
							}
						});
						
					}
				}).start();
				
				
			}
		});
		
		
		
		
		
		
		
		
	    
		
		btn_write.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				
				LoadingProgress.showLoadingDialog(act);
				new Thread(new Runnable(){
					@Override
					public void run()
					{
						
						
						runOnUiThread(new Runnable(){
							@Override 
							public void run(){
								btn_write.setBackgroundColor(Color.parseColor("#33b5e5"));
							}
						});
						
						
						
						Intent intent = new Intent(act, WriteForm.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						act.startActivity(intent);
					
						LoadingProgress.getHandler().sendEmptyMessage(0);
						
						
						runOnUiThread(new Runnable(){
							@Override 
							public void run(){
								btn_write.setBackgroundColor(Color.parseColor("#525457"));
							}
						});
						
					}
				}).start();
				
				
				
				
			}
		});
		
		
		
		
		
		
		//GCM
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(this);
        if("".equals(regId)){   //구글 가이드에는 regId.equals("")로 되어 있는데 Exception을 피하기 위해 수정
        	Log.d("===GCM init===", regId); 
            GCMRegistrar.register(this, "804630958109");
        }else{
        	Log.d("===GCM init check===", regId); 
        	checkKey(regId);
        }
        Log.d("===GCM regId===", regId); 
	}
	
	protected void checkKey(String regId){
    	String posturl = null;
    	Global userInfo = (Global) getApplication();
    	try {//이메일 체크 http연결
			HttpClient client = new DefaultHttpClient();
			posturl = PropertyUtil.getProperty("golaju.gcm.url") + "/GCMcheckId.jsp";
			HttpPost post = new HttpPost(posturl);
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			
			Log.d("---GCM onRegistered---", "userId : "+Long.toString(userInfo.getUserId()));
			
		    params.add(new BasicNameValuePair("userId", Long.toString(userInfo.getUserId())));
		    params.add(new BasicNameValuePair("regId", regId));
							 
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			post.setEntity(ent);
			 
			HttpResponse responsePost = client.execute(post);
			HttpEntity resEntity = responsePost.getEntity();
			String err = EntityUtils.toString(resEntity);
			Log.d("---GCM checkKey---", "err : "+err);
		}catch (Exception e) {
			e.printStackTrace();
		}
    	
    	Log.d("---GCM checkKey---", "regId : "+regId);
    }
	
	/**
	 * 게시글 목록을 그린다.
	 * @param listType 진행중 목록인지 완료 목록인지 구분. 
	 * @param listView 진행중 목록 또는 완료 목록의 리스트뷰.
	 * @param itemList GItem타입이 저장된 list.
	 * @param itemListAdapter itemList를 적용시킬 adapter.
	 * @param minId 현재 그려진 목록이 있다면 그 목록 중 가장 마지막에 있는 itemId값.
	 */
	public void DrawItemList(String type, ListView listView, ArrayList<GItem> itemList, ItemListAdapter itemListAdapter, Long minId){
		
		final ListView lv = listView; 
		
		if(type.equals("notClosed")){
			listType = "notClosed";
		}else{
			listType = "closed";
		}
		
		try
		{
			String url = PropertyUtil.getProperty("golaju.dbWork.url") + "/itemList.jsp?listType=" +  listType + "&minId=" + minId;
			//String url = "http://125.140.114.8/App/DBLink/itemList.jsp?listType=" +  listType + "&minId=" + minId;
			//String url = "http://125.140.114.107/App/DBLink/itemList.jsp?listType=" +  listType + "&minId=" + minId;
			
			Document doc = XmlUtil.readXml(url);
			
			if (XmlUtil.isXPath(doc, "//ItemList_Info"))
    		{
    			boolean isSuccess = Boolean.parseBoolean(XmlUtil.getXPath(doc, "//ItemList_Get_Success"));
    			
    			if(isSuccess){ //목록 불러오기에 성공했다면.
					Node root = XmlUtil.getXPathes(doc, "//ItemList").item(0);
					NodeList nodeList = root.getChildNodes();
					
					if(minId == null || minId != Long.parseLong(XmlUtil.getXPath(nodeList.item(nodeList.getLength() - 1), "ItemId"))){ //minId가 없거나 가져온 목록이 현재 목록에 존재하지 않는다면 통과. 현재 minId가 가져온 목록의 마지막 글의 itemId와 같다면 현재 존재한다는 뜻.
					
    					for (int i=0; i<nodeList.getLength(); i++)
    					{
    						Node node = nodeList.item(i);
    						XmlUtil.getXPath(node, "Title");
    						
    						if (node.getNodeType() == Node.TEXT_NODE){
    							continue;
    						}
    						
    						if("Item".equals(node.getNodeName())) {

    							itemList.add(new GItem(Long.parseLong(XmlUtil.getXPath(node, "ItemId")), XmlUtil.getXPath(node, "Title"), XmlUtil.getXPath(node, "VoteThumb")));
    							
    							if((i + 1) == nodeList.getLength()){
    								if(listType.equals("notClosed")){
    									notClosedMinId = Long.parseLong(XmlUtil.getXPath(node, "ItemId"));
    								}else{
    									closedMinId = Long.parseLong(XmlUtil.getXPath(node, "ItemId"));
    								}
    							}
    						} 
    					}
    					
    					if(listType.equals("notClosed")){
    						if(itemListAdapter == null){
    							notClosedAdapter = new ItemListAdapter(this, itemList, imageLoader, options);
		    					
    							runOnUiThread(new Runnable(){
									@Override public void run(){
										lv.setAdapter(notClosedAdapter);
									}
    							});
    						}else{
    							runOnUiThread(new Runnable(){
									@Override public void run(){
										notClosedAdapter.notifyDataSetChanged();
									}
    							});
    						}
    					}else{
    						if(itemListAdapter == null){
    							closedAdapter = new ItemListAdapter(this, itemList, imageLoader, options);
		    					
    							runOnUiThread(new Runnable(){
									@Override public void run(){
										lv.setAdapter(closedAdapter);
									}
    							});
    						}else{
    							runOnUiThread(new Runnable(){
									@Override public void run(){
										closedAdapter.notifyDataSetChanged();
										
									}
    							});
    						}
    					}
    					
    					listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

							@Override
							public void onItemClick(AdapterView av, View v, int position, long id)
							{
								final View view = v;
								LoadingProgress.showLoadingDialog(ItemList.this);
								new Thread(new Runnable(){
									@Override
									public void run()
									{
										Intent intent = new Intent(ItemList.this, ItemRead.class);
										Bundle bundle = new Bundle() ;
										bundle.putLong("itemId", Long.parseLong(view.getTag().toString())) ;
										bundle.putString("list", "itemList");
										if(listType.equals("closed")){
											bundle.putString("listType", "closed");
										}else if(listType.equals("notClosed")){
											bundle.putString("listType", "notClosed");
										}
										intent.putExtras(bundle) ;
										startActivity(intent);
										
										LoadingProgress.getHandler().sendEmptyMessage(0);
									}
								}).start();
									
							}
    					});
					}
    				//Toast.makeText(ItemList.this, "목록 불러오기에 성공하엿습니다.", Toast.LENGTH_SHORT).show();
    			}else{ //목록 불러오기에 실패했다면.
    				// 더이상 불러올 리스트가 없으면 스크롤시 목록을 불러오는 것을 막기 위해 스크롤이벤트 재설정.
    				listView.setOnScrollListener(new AbsListView.OnScrollListener() {
						@Override
						public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
						@Override
						public void onScrollStateChanged(AbsListView view, int scrollState) {
							if(scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
								btn_notClosedList.setEnabled(false);
								btn_closedList.setEnabled(false);
							}else if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
								btn_notClosedList.setEnabled(true);
								btn_closedList.setEnabled(true);
							}
						}
					});
    				//Toast.makeText(ItemList.this, "목록이 더 이상 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
    			}
    		}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		if(view == lvNotClosedItem){
			notClosedLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount == totalItemCount);
        	if(notClosedLastItemVisible){ // 마지막 리스트에 도달 했다면.
        		Log.i("하단 : ", "하단에 도달");
        		DrawItemList("notClosed", lvNotClosedItem, notClosedItemList, notClosedAdapter, notClosedMinId); // 추가 목록 보여주기.
        	}
		}else if(view == lvClosedItem){
			closedLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount == totalItemCount); 
        	if(closedLastItemVisible){ // 마지막 리스트에 도달 했다면.
        		Log.i("하단 : ", "하단에 도달");
        		DrawItemList("closed", lvClosedItem, closedItemList, closedAdapter, closedMinId); // 추가 목록 보여주기.
        	}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		// 게시글 목록을 스크롤할 때에는 목록 보기 버튼을 비활성화 하고 스크롤이 끝난 후에 활성화 시켜 오류를 막는다.
		if(scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
			btn_notClosedList.setEnabled(false);
			btn_closedList.setEnabled(false);
		}else if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
			btn_notClosedList.setEnabled(true);
			btn_closedList.setEnabled(true);
		}
	}
	
	@Override
	public void onBackPressed()
	{
		Intent intent = new Intent(this, Login.class);
		Bundle bundle = new Bundle();
		bundle.putString("flag", "finish");
		intent.putExtras(bundle) ;
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		//super.onBackPressed();
	}
	
}