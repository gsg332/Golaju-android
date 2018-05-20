package com.kcube.golaju.mypage;

import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kcube.golaju.R;
import com.kcube.golaju.data.Global;
import com.kcube.golaju.item.GItem;
import com.kcube.golaju.item.ItemListAdapter;
import com.kcube.golaju.item.ItemRead;
import com.kcube.golaju.util.PropertyUtil;
import com.kcube.golaju.util.XmlUtil;
import com.kucbe.golaju.common.LoadingProgress;
import com.kucbe.golaju.common.NormalActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class Mypage extends NormalActivity implements OnScrollListener{
	
	TextView id;
	TextView level;
	TextView percent;
	ImageView imgView;

	Long userid;
	
	
	LinearLayout notClosedList;
	LinearLayout closedList;
	
	TextView btn_notClosedList;
	TextView btn_closedList;
	
	Long notClosedMinId = null;
	Long closedMinId = null;
	
	ListView lvNotClosedItem;
	ListView lvClosedItem;
	
	boolean firstClosedItem = true;
	
	ItemListAdapter notClosedAdapter = null;
	ItemListAdapter closedAdapter = null;
	
	ArrayList<GItem> notClosedItemList = new ArrayList<GItem>();
	ArrayList<GItem> closedItemList = new ArrayList<GItem>();
	
	boolean notClosedLastItemVisible = false;     
	boolean closedLastItemVisible = false;
	
	String listType = null;
	
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView( R.layout.mypage ) ;
		

		initSetting(this, true, false); // 공통적인 초기 새팅을 한다.
		
		
		Log.i("Mypage Excute","onCreate");
		
		
        
        id = (TextView)findViewById(R.id.mypage_id_text);
        level = (TextView)findViewById(R.id.mypage_level);
        percent = (TextView)findViewById(R.id.mypage_percent);
        
        imgView = (ImageView) findViewById(R.id.mypage_img_view);
        
        Global userInfo = (Global) getApplication();
        
        id.setText(userInfo.getLoginId());
        userid = userInfo.getUserId();

        String userThumbUrl;
        
        
        
        try
		{
			userThumbUrl = PropertyUtil.getProperty("golaju.photo.url") + userInfo.getThumbPath();
			
			
			Log.i("thumb path", userThumbUrl);
			
			imageLoader.displayImage(userThumbUrl, imgView, options);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
        
        notClosedList = (LinearLayout) findViewById(R.id.mypage_notClosedList); // 진행중인 목록 영역
		closedList = (LinearLayout) findViewById(R.id.mypage_closedList); // 완료 목록 영역
		btn_notClosedList = (TextView) findViewById(R.id.mypage_tab_menu_ing); // 진행중인 목록 버튼
		btn_closedList = (TextView) findViewById(R.id.mypage_tab_menu_finish); // 완료중인 목록 버튼
		lvNotClosedItem = (ListView) findViewById(R.id.mypage_notClosedListView); // 진행중인 목록 리스트뷰
		lvClosedItem = (ListView) findViewById(R.id.mypage_closedListView); // 완료 목록 리스트뷰
		
		listType = getIntent().getExtras().get("listType").toString();
		if(listType.equals("closed")){
			runOnUiThread(new Runnable(){
				@Override public void run(){
					btn_notClosedList.setEnabled(false);
					btn_closedList.setEnabled(false);
					
					notClosedList.setVisibility(View.INVISIBLE);
					closedList.setVisibility(View.VISIBLE);
					
					btn_notClosedList.setBackgroundResource(R.drawable.no_select_border);
					btn_closedList.setBackgroundResource(R.drawable.select_border);
				}
			});
			
			closedItemList.clear();
			
			DrawItemList("closed", lvClosedItem, closedItemList, null, null);
			
			runOnUiThread(new Runnable(){
				@Override public void run(){
					btn_notClosedList.setEnabled(true);
					btn_closedList.setEnabled(true);
				}
			});
		}else if(listType.equals("notClosed")){
			DrawItemList("notClosed", lvNotClosedItem, notClosedItemList, null, null);
			btn_notClosedList.setBackgroundResource(R.drawable.select_border);
			btn_closedList.setBackgroundResource(R.drawable.no_select_border);
		}
		
		lvNotClosedItem.setOnScrollListener(this);
		lvClosedItem.setOnScrollListener(this);
		
		btn_notClosedList.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
				
				LoadingProgress.showLoadingDialog(Mypage.this);
				new Thread(new Runnable(){
					@Override
					public void run()
					{
						runOnUiThread(new Runnable(){
							@Override public void run(){
								notClosedList.setVisibility(View.VISIBLE);
								closedList.setVisibility(View.INVISIBLE);
								btn_notClosedList.setBackgroundResource(R.drawable.select_border);
								btn_closedList.setBackgroundResource(R.drawable.no_select_border);
							}
						});
						
						notClosedItemList.clear();
						
						DrawItemList("notClosed", lvNotClosedItem, notClosedItemList, null, null);
						
						lvNotClosedItem.setOnScrollListener(Mypage.this);
					
						LoadingProgress.getHandler().sendEmptyMessage(0);
					}
				}).start();
				
				
				
				
				
				
				
			}
		});
		
		btn_closedList.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
				
				
				LoadingProgress.showLoadingDialog(Mypage.this);
				new Thread(new Runnable(){
					@Override
					public void run()
					{
						runOnUiThread(new Runnable(){
							@Override public void run(){
								notClosedList.setVisibility(View.INVISIBLE);
								closedList.setVisibility(View.VISIBLE);
								btn_notClosedList.setBackgroundResource(R.drawable.no_select_border);
								btn_closedList.setBackgroundResource(R.drawable.select_border);
							}
						});
						
						closedItemList.clear();
						
						DrawItemList("closed", lvClosedItem, closedItemList, null, null);
						
						lvClosedItem.setOnScrollListener(Mypage.this);
					
						LoadingProgress.getHandler().sendEmptyMessage(0);
					}
				}).start();
				
				
				
				
				
			}
		});
			
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
			String url = PropertyUtil.getProperty("golaju.dbWork.url") + "/mypageItemList.jsp?listType=" + listType + "&minId=" + minId + "&userid="+userid;
			//String url = "http://125.140.114.8/App/DBLink/mypageItemList.jsp?listType=" + listType + "&minId=" + minId;
			//String url = "http://125.140.114.140/App/DBLink/mypageItemList.jsp?listType=" + listType + "&minId=" + minId + "&userid="+userid;
			
			Document doc = XmlUtil.readXml(url);
			
			final String levelText =  XmlUtil.getXPath(doc, "//Level").toString();
			final String percentText =  XmlUtil.getXPath(doc, "//Percent").toString();
			
			runOnUiThread(new Runnable(){
				@Override public void run(){
					level.setText(levelText);
					percent.setText(percentText);
				}
			});
			
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
								LoadingProgress.showLoadingDialog(Mypage.this);
								new Thread(new Runnable(){
									@Override
									public void run()
									{
										Intent intent = new Intent(Mypage.this, ItemRead.class);
										Bundle bundle = new Bundle() ;
										bundle.putLong("itemId", Long.parseLong(view.getTag().toString())) ;
										bundle.putString("list", "myPageList") ;
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
	 
}
