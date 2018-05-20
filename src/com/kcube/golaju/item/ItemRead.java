package com.kcube.golaju.item;

import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kucbe.golaju.common.LoadingProgress;
import com.kucbe.golaju.common.NormalActivity;
import com.kcube.golaju.R;
import com.kcube.golaju.data.Global;
import com.kcube.golaju.mypage.Mypage;
import com.kcube.golaju.user.User;
import com.kcube.golaju.user.UserListAdapter;
import com.kcube.golaju.util.PropertyUtil;
import com.kcube.golaju.util.XmlUtil;
import com.kcube.golaju.util.photoView.PhotoViewAttacher;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemRead extends NormalActivity
{
	URL url = null;
	Long itemId = null;
	String listCategory = null;
	String listType = null;
	ArrayList<Integer> voteCntList = new ArrayList<Integer>(); // 투표 후 각 투표항목의 %를 다시 계산하기 위하여 사용.
	ArrayList<View> votePercentList = new ArrayList<View>(); // 투표 후 각 투표항목의 %를 다시 계산하여 텍스트를 변경하기 위하여 사용.
	ArrayList<ImageView> voteOriginalImgList = new ArrayList<ImageView>(); //
	ArrayList<TextView> voteTitleList = new ArrayList<TextView>(); //
	String timeLimit;
	String peopleLimit;
	String genderLimit;
	String ageLimit;
	boolean isClose;
	LayoutInflater inflater;
	Global userInfo;
	String itemUserId;
	
	ImageView ivUserThumb;
    TextView tvTitle;
    TextView tvLoginId;
    TextView tvContent;
    TextView tvTimeLimit;
    TextView tvPeopleLimit;
    TextView tvGenderLimit;
    TextView tvAgeLimit;
    
    TextView differentItem1;
    TextView differentItem2;
    TextView differentItem3;
    TextView differentItem4;
    TextView differentItem5;
    TextView differentItemTitle;
    
	LinearLayout voteListArea;
	FrameLayout originalImgListArea;
	FrameLayout voteUserListArea;
	ListView voteUserList ;
	FrameLayout voteTitleListArea;
	
	
	
	
	//int INT_TIME;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_read);

		
		
		
		
		/* 메모리로 인한 데이터 clear테스트를 위한 시간 체크용을 위해  사용.
		final Thread THREAD_CHECK_TIME = new Thread(new Runnable(){

			@Override
			public void run()
			{
				
				while(true){
					
					Log.i("Read time입니다 : ", "tlc : " + INT_TIME);
					INT_TIME++;
					
					try{
						Thread.sleep(1000 * 60);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
			
			
		});
		
		THREAD_CHECK_TIME.start();*/
		
		
		
		
		
		
		
		
		
		
		initSetting(this, true, false); // 공통적인 초기 새팅을 한다.
		
		
		
        

        
        
        try 
        {
        	
        	Log.i("itemId입니다.", getIntent().hasExtra("itemId") + "");
        	
        	if(getIntent().hasExtra("itemId")){ // 게시글 목록에서 넘겨받은 itemId값이 있다면.
        		
        		
        		Log.i("itemId입니다2.", getIntent().getExtras().getLong("itemId") + "");
        		
        		
        		itemId = getIntent().getExtras().getLong("itemId");
        		listType = getIntent().getExtras().get("listType").toString();
        		Log.i("has listType", listType);
        		listCategory = getIntent().getExtras().get("list").toString();
        	}else if(saveDataAsClosed.getLogin()){ // 메모리 문제로 인해 강제 종료된 경우를 대비하여 sharedData에 저장한 데이터로 값을 세팅한다.
        		Long savedItemId = saveDataAsClosed.getItemId();
        		String savedList = saveDataAsClosed.getList();
    			if(savedItemId != null && savedList != null){
    				itemId = savedItemId;
    				listCategory = savedList;
    				
    			}
        	}
        	
        	
	        String url = PropertyUtil.getProperty("golaju.dbWork.url") + "/itemRead.jsp?itemId=" + itemId;
	        //String url = "http://125.140.114.8/App/DBLink/itemRead.jsp?itemId=" + itemId;
	        //String url = "http://125.140.114.140/App/DBLink/itemRead.jsp?itemId=" + itemId;

	    	Document itemReadDoc = XmlUtil.readXml(url);
			if (XmlUtil.isXPath(itemReadDoc, "//ItemRead_Info"))
			{
				boolean isSuccess = Boolean.parseBoolean(XmlUtil.getXPath(itemReadDoc, "//ItemRead_Success"));
				if(isSuccess){ //아이템 정보 불러오기에 성공했다면.
					inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					
					ivUserThumb = (ImageView) findViewById(R.id.userThumbArea);
			        tvTitle = (TextView) findViewById(R.id.title);
			        tvLoginId = (TextView) findViewById(R.id.loginId);
			        tvContent = (TextView) findViewById(R.id.contentArea);
			        tvTimeLimit = (TextView) findViewById(R.id.timeLimit);
			        tvPeopleLimit = (TextView) findViewById(R.id.peopleLimit);
			        tvGenderLimit = (TextView) findViewById(R.id.genderLimit);
			        tvAgeLimit = (TextView) findViewById(R.id.ageLimit);
			        voteUserList = (ListView) findViewById(R.id.voteUserList);
					voteListArea  = (LinearLayout) findViewById(R.id.voteListArea);
					originalImgListArea = (FrameLayout) findViewById(R.id.originalImgListArea);
					voteUserListArea = (FrameLayout) findViewById(R.id.voteUserListArea);
					voteTitleListArea = (FrameLayout) findViewById(R.id.voteTitleListArea);
				    
					//XmlUtil.getXPath(doc, "//UserName");
					//XmlUtil.getXPath(doc, "//ItemId");
				    
				    isClose = (XmlUtil.getXPath(itemReadDoc, "//IsClose").equals("0") ? false : true) ;
				    
				    userInfo = (Global) getApplication();
				    itemUserId = XmlUtil.getXPath(itemReadDoc, "//UserId");
				    if((isClose == false) && (userInfo.getUserId() == Long.parseLong(XmlUtil.getXPath(itemReadDoc, "//UserId")))){ //자신이 작성한 글이라면.
				    	Button completeBtn = (Button) findViewById(R.id.btn_complete);  // 완료 버튼
				    	Button deleteBtn = (Button) findViewById(R.id.btn_delete);  //  삭제 버튼
				    	Button modifyBtn = (Button) findViewById(R.id.btn_modify);  // 수정 버튼
				    	completeBtn.setVisibility(View.VISIBLE);
				    	deleteBtn.setVisibility(View.VISIBLE);
				    	modifyBtn.setVisibility(View.VISIBLE);
				    	completeBtn.setOnClickListener(new View.OnClickListener()
						{
							@Override
							public void onClick(View v) // 완료 버튼 클릭.
							{
								AlertDialog.Builder alertConfirm = new AlertDialog.Builder(ItemRead.this);
								
								alertConfirm.setMessage("종료 하시겠습니까?").setCancelable(false)
									.setPositiveButton("확인",
										new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											LoadingProgress.showLoadingDialog(ItemRead.this);
											new Thread(new Runnable(){
												@Override
												public void run()
												{
													try
													{
														String url = PropertyUtil.getProperty("golaju.dbWork.url") + "/itemComplete.jsp?itemId=" + itemId + "&userId=" + itemUserId;
														//String url = "http://125.140.114.107/App/DBLink/itemComplete.jsp?itemId=" + itemId + "&userId=" + itemUserId;
														Document itemComplteDoc;
														
														itemComplteDoc = XmlUtil.readXml(url);
														if (XmlUtil.isXPath(itemComplteDoc, "//ItemComplete_Info"))
														{
															boolean isSuccess = Boolean.parseBoolean(XmlUtil.getXPath(itemComplteDoc, "//ItemComplete_Success"));
															if(isSuccess){ //아이템 정보 불러오기에 성공했다면.
																//새로고침 하여 완료된 결과로 게시글을 리플래쉬 시킨다.
																Intent intent = new Intent(ItemRead.this, ItemRead.class);  
																Bundle bundle = new Bundle() ;
																bundle.putString("list", listCategory);
																bundle.putLong("itemId", itemId) ;
																bundle.putString("listType", listType);
																intent.putExtras(bundle) ;
																startActivity(intent);
																finish();
															}
														}
													}
													catch (Exception e)
													{
														e.printStackTrace();
													}
													LoadingProgress.getHandler().sendEmptyMessage(0);
												}
											}).start();
										}
									}).setNegativeButton("취소",
										new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											return;
										}
									});
								AlertDialog alert = alertConfirm.create();
								alert.show();
							}
						});
				    	deleteBtn.setOnClickListener(new View.OnClickListener()
						{
							@Override
							public void onClick(View v) // 게시글 삭제 버튼 클릭.
							{
								AlertDialog.Builder alertConfirm = new AlertDialog.Builder(ItemRead.this);
								
								alertConfirm.setMessage("삭제 하시겠습니까?").setCancelable(false)
									.setPositiveButton("확인",
										new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											LoadingProgress.showLoadingDialog(ItemRead.this);
											new Thread(new Runnable(){
												@Override
												public void run()
												{
													try
													{
														String url = PropertyUtil.getProperty("golaju.dbWork.url") + "/itemDelete.jsp?itemId=" + itemId;
														Document itemDeleteDoc = XmlUtil.readXml(url);
														if (XmlUtil.isXPath(itemDeleteDoc, "//ItemDelete_Info"))
														{
															boolean isSuccess = Boolean.parseBoolean(XmlUtil.getXPath(itemDeleteDoc, "//ItemDelete_Success"));
															if(isSuccess){
																
																Intent intent = null;
																
																if("myPageList".equals(listCategory)){
																	intent = new Intent(ItemRead.this, Mypage.class);
																}else{
																	intent = new Intent(ItemRead.this, ItemList.class);
																}
																
																intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
																Bundle bundle = new Bundle();
																bundle.putString("list", listCategory);
																bundle.putString("listType", listType);
																intent.putExtras(bundle) ;
																
																Log.i("list", listCategory);
																Log.i("listType", listType);
																
																startActivity(intent);
																//finish();
															}
														}
													}
													catch (Exception e)
													{
														e.printStackTrace();
													}
													LoadingProgress.getHandler().sendEmptyMessage(0);
												}
											}).start();
										}
									}).setNegativeButton("취소",
										new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											return;
										}
									});
								AlertDialog alert = alertConfirm.create();
								alert.show();
							}
						});
				    	modifyBtn.setOnClickListener(new View.OnClickListener()
						{
							@Override
							public void onClick(View v) // 게시글 수정 버튼 클릭.
							{
								LoadingProgress.showLoadingDialog(ItemRead.this);
								new Thread(new Runnable(){
									@Override
									public void run()
									{
										Intent intent = new Intent(ItemRead.this, ModifyForm.class);  
										intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										Bundle bundle = new Bundle();
										bundle.putLong("itemId", itemId);
										bundle.putString("title", tvTitle.getText().toString());
										bundle.putString("content", tvContent.getText().toString());
										bundle.putString("list", listCategory);
										bundle.putString("listType", listType);
										intent.putExtras(bundle) ;
										startActivity(intent);
									
										LoadingProgress.getHandler().sendEmptyMessage(0);
									}
								}).start();
							}
						});
				    }
					
				    String userThumbUrl = PropertyUtil.getProperty("golaju.photo.url") + URLEncoder.encode(XmlUtil.getXPath(itemReadDoc, "//UserThumb"),"utf-8");
					//String userThumbUrl = "https://www.oaasys.com/AttachmentAction.DoDownload.do?inline=true&path=thumb%5C9%5C4%5C4%5Coaasys8236564462443968945.JPG";
				    
				    
				    imageLoader.displayImage(userThumbUrl, ivUserThumb, options);

				    
			        tvTitle.setText(XmlUtil.getXPath(itemReadDoc, "//Title"));
			        tvLoginId.setText(XmlUtil.getXPath(itemReadDoc, "//LoginId"));
			        tvContent.setText(XmlUtil.getXPath(itemReadDoc, "//Content"));
			        timeLimit = XmlUtil.getXPath(itemReadDoc, "//TimeLimit");
			        if(!timeLimit.equals("null")){
			        	tvTimeLimit.setVisibility(View.VISIBLE);
			        	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");
			            String timeLimitFmt = formatter.format(new Date(Long.parseLong(timeLimit)));
			            tvTimeLimit.setText(timeLimitFmt + "마감");
			        }
			        peopleLimit = XmlUtil.getXPath(itemReadDoc, "//PeopleLimit");
			        if(!peopleLimit.equals("null")){
			        	tvPeopleLimit.setVisibility(View.VISIBLE);
			        	tvPeopleLimit.setText(peopleLimit + "명");
			        }
			        genderLimit = XmlUtil.getXPath(itemReadDoc, "//GenderLimit");
			        if(!genderLimit.equals("null")){
			        	tvGenderLimit.setVisibility(View.VISIBLE);
			        	int gender = Integer.parseInt(genderLimit);
				    	if(gender == 1){
				    		tvGenderLimit.setText("남성");
				    	}else if(gender == 2){
				    		tvGenderLimit.setText("여성");
				    	}
			        }
			        ageLimit = XmlUtil.getXPath(itemReadDoc, "//AgeLimit");
			        if(!ageLimit.equals("null")){
			        	tvAgeLimit.setVisibility(View.VISIBLE);
			        	tvAgeLimit.setText(ageLimit.replaceAll(",","대,") + (ageLimit.indexOf("70") > -1 ? "대 이상": "대"));
			        }
			        
			        final int voteTotalCnt = Integer.parseInt(XmlUtil.getXPath(itemReadDoc, "//VoteTotalCnt"));
					Node voteArea = XmlUtil.getXPathes(itemReadDoc, "//VoteList").item(0);
					NodeList nodeList = voteArea.getChildNodes();
					for (int i=0; i<nodeList.getLength(); i++)
					{
						Node node = nodeList.item(i);
						if("VoteItem".equals(node.getNodeName())) {
							voteListArea = (LinearLayout) inflater.inflate(R.layout.vote_item, voteListArea, true); //voteListArea안에 뷰들을 추가하여 voteListArea를 반환.
							LinearLayout voteItem = (LinearLayout) voteListArea.getChildAt(i);
							ImageView voteThumbImg = (ImageView) voteItem.findViewById(R.id.voteImg); // 투표 이미지
							voteThumbImg.setTag(i); // 원본이미지를 불러올 때 매칭되는 원본이미지를 보여주기 위한 tag세팅.
							String voteThumbUrl = PropertyUtil.getProperty("golaju.photo.url") + URLEncoder.encode(XmlUtil.getXPath(node, "ThumbPath"),"utf-8");
							//String voteThumbUrl = "https://www.oaasys.com/AttachmentAction.DoDownload.do?inline=true&path=thumb%5C9%5C4%5C4%5Coaasys8236564462443968945.JPG";
							imageLoader.displayImage(voteThumbUrl, voteThumbImg, options);
							
							originalImgListArea = (FrameLayout) inflater.inflate(R.layout.inflate_original_image, originalImgListArea, true); // 원본이미지 목록 영역에 원본이미지를 넣어 view 추가.
							ImageView voteOriginalImg = (ImageView) originalImgListArea.getChildAt(i);
							voteOriginalImg.setTag(i); // 어떤 투표 썸네일 이미지를 클릭했는지 매칭시키기 위한 tag 세팅.
							voteOriginalImgList.add(voteOriginalImg);
							String originalPath = URLEncoder.encode(XmlUtil.getXPath(node, "OriginalPath"),"utf-8");
							if(originalPath.equals("null")){
								originalPath = "defaultImage%5Cuser_default_95.gif";
							}
							//String voteOriginalImgUrl = PropertyUtil.getProperty("golaju.photo.url") + URLEncoder.encode(XmlUtil.getXPath(node, "OriginalPath"),"utf-8");
							String voteOriginalImgUrl = PropertyUtil.getProperty("golaju.photo.url") + originalPath;
							//String voteOriginalImgUrl = PropertyUtil.getProperty("golaju.photo.url") + "4/3/2/oaasys2901802464429067856.JPG";
						    imageLoader.displayImage(voteOriginalImgUrl, voteOriginalImg, options, new ImageLoadingListener() {
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
						    });
							
							voteThumbImg.setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View v)
								{
									//모든 원본이미지 숨기기
									for(int i=0; i<voteOriginalImgList.size(); i++){
										ImageView originalImg = (ImageView) originalImgListArea.getChildAt(i) ;
										originalImg.setVisibility(View.GONE);
										
										 //썸네일 이미지와 매칭되는 원본이미지만 보여주기.
										if(originalImg.getTag() == v.getTag()){
											originalImg.setVisibility(View.VISIBLE);
										}
									}
									originalImgListArea.setVisibility(View.VISIBLE);	
								}
							});
					        
							Long pollId = Long.parseLong(XmlUtil.getXPath(node, "PollId"));
							
				            TextView votePercent = (TextView) voteItem.findViewById(R.id.votePercent);
				            int percent = 0;
				            int voteCnt = Integer.parseInt(XmlUtil.getXPath(node, "VoteCnt"));
				            voteCntList.add(voteCnt);
						    if(voteTotalCnt != 0){
								float a = (float) voteCnt;
								float b = (float) voteTotalCnt;
						    	percent = (int)((a/b)*100);
						    }
						    votePercent.setText(percent + "% (" + voteCnt + "명)");
						    votePercent.setTag(pollId);
						    votePercentList.add(votePercent);
						    votePercent.setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View v) // 투표자 목룍 가져오기.
								{
									try
									{
										final String url = PropertyUtil.getProperty("golaju.dbWork.url") + "/voteUserList.jsp?pollId=" + v.getTag();
										//final String url = "http://125.140.114.8/App/DBLink/voteUserList.jsp?pollId=" + v.getTag();
										//final String url = "http://125.140.114.17/App/DBLink/voteUserList.jsp?pollId=" + v.getTag();
											
										LoadingProgress.showLoadingDialog(ItemRead.this);
										new Thread(new Runnable(){
											@Override
											public void run()
											{
										        Document voteUserListDoc;
												try
												{
													voteUserListDoc = XmlUtil.readXml(url);
													if (XmlUtil.isXPath(voteUserListDoc, "//VoteUser_Info"))
													{
														boolean isSuccess = Boolean.parseBoolean(XmlUtil.getXPath(voteUserListDoc, "//VoteUserList_Success"));
														if(isSuccess){
							    						 	final UserListAdapter adapter;
							    							ArrayList<User> arrData = new ArrayList<User>();
									    					
							    							Node root = XmlUtil.getXPathes(voteUserListDoc, "//UserList").item(0);
									    					NodeList nodeList = root.getChildNodes();
									    					
									    					for (int i=0; i<nodeList.getLength(); i++)
									    					{
									    						Node node = nodeList.item(i);
									    						if (node.getNodeType() == Node.TEXT_NODE){
									    							continue;
									    						}
									    						if("User".equals(node.getNodeName())) {
									    				    		arrData.add(new User(Long.parseLong(XmlUtil.getXPath(node, "UserId")), Integer.parseInt(XmlUtil.getXPath(node, "Gender")), Integer.parseInt(XmlUtil.getXPath(node, "Age_Group")), XmlUtil.getXPath(node, "ThumbPath")));
									    						} 
									    					}
															
							    				        	adapter = new UserListAdapter(ItemRead.this, arrData, imageLoader, options);
							    				        	
							    				        	runOnUiThread(new Runnable(){
																@Override public void run(){
																	voteUserListArea.setVisibility(View.VISIBLE); // 투표자 목록 영역 보이기.
																	voteUserList.setAdapter(adapter);
																}
															});
							    				        	
														}else{
															runOnUiThread(new Runnable(){
																@Override public void run(){
																	Toast.makeText(ItemRead.this, "투표한 회원이 없습니다.", Toast.LENGTH_SHORT).show();
																}
															});
														}
													}
												}
												catch (Exception e)
												{
													e.printStackTrace();
												}
												LoadingProgress.getHandler().sendEmptyMessage(0);
											}
										}).start();
									}
									catch (Exception e)
									{
										e.printStackTrace();
									}
								}
							});
						    
						    String voteText = XmlUtil.getXPath(node, "VoteText");
						    voteText = (!"null".equals(voteText)? voteText : "내용 없음");
				            TextView voteTitle = (TextView) voteItem.findViewById(R.id.voteTitle);
				            if(voteText.length() > 20){ // 20글자가 넘으면 ...으로 처리.
				            	voteTitle.setText(voteText.substring(0, 20) + "...");
				            }else{
				            	voteTitle.setText(voteText);
				            }
				            voteTitle.setTag(i); // 투표 내용 상세보기를 할 때 매칭시키기 위한 tag값 세팅.
				            voteTitleListArea = (FrameLayout) inflater.inflate(R.layout.inflate_vote_title, voteTitleListArea, true); // 투표 내용 상세보기 영역에 투표 내용 상세보기를 생성하여 삽입.
				            voteTitleListArea.setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View v) // 투표 내용 상세보기 영역 클릭 시 닫히도록 함.
								{
									v.setVisibility(View.GONE);
								}
							});
				            TextView voteTitleDetail = (TextView) voteTitleListArea.getChildAt(i);
				            voteTitleDetail.setText(voteText);
				            voteTitleDetail.setTag(i);
				            voteTitleList.add(voteTitleDetail);
				            voteTitle.setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View v)
								{
									//모든 투표내용상세보기 숨기기
									for(int i=0; i<voteTitleList.size(); i++){
										TextView voteTitle = (TextView) voteTitleListArea.getChildAt(i) ;
										voteTitle.setVisibility(View.GONE);
										
										if(voteTitle.getTag() == v.getTag()){
											voteTitle.setVisibility(View.VISIBLE);
										}
									}
									voteTitleListArea.setVisibility(View.VISIBLE);
								}
							});
				            
				            TextView voteListNum = (TextView) voteItem.findViewById(R.id.voteListNum);
				            voteListNum.setText((i + 1) + ""); // 투표 목록 번호 붙이기.
				            
				            TextView checkBtn = (TextView) voteItem.findViewById(R.id.checkBtn); // 투표하기 버튼.
				            checkBtn.setTag(pollId);
				            checkBtn.setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View v) //투표 버튼을 클릭했으면.
								{
									final View view = v;
									
									LoadingProgress.showLoadingDialog(ItemRead.this);
									new Thread(new Runnable(){
										@Override
										public void run()
										{
											if(isClose == true){ // 종료된 게시글이라면.
												runOnUiThread(new Runnable(){
													@Override public void run(){
														LoadingProgress.getHandler().sendEmptyMessage(0);
														Toast.makeText(ItemRead.this, "투표가 종료되었습니다.", Toast.LENGTH_SHORT).show();
													}
												});
												return;
											}
											
											String pollId = view.getTag().toString(); // 해당 투표목록의 pollId 얻기.
											try
											{
												String url = PropertyUtil.getProperty("golaju.dbWork.url") + "/itemVote.jsp?itemId=" + itemId + "&pollId=" + pollId + "&userId=" + userInfo.getUserId() + "&itemUserId=" + itemUserId;
										        //String url = "http://125.140.114.8/App/DBLink/itemVote.jsp?pollId=" + pollId + ((Global) getApplication()).getUserId();
										        //String url = "http://125.140.114.107/App/DBLink/itemVote.jsp?pollId=" + pollId + ((Global) getApplication()).getUserId();
												
												if(!peopleLimit.equals("null")){ // 인원수 제한이 있다면.
													url += "&peopleLimit=" + peopleLimit; // 게시글에 들어온 후 그 사이에 다른 사람이 투표를 할 수도 있으므로 정확한 투표 인원을 확인하기 위하여 서버단 DB에서 현재 투표한 총인원을 가져와 투표수를 증가시킬지 결정한다.
												}
												if(!genderLimit.equals("null")){ // 성별 제한이 있다면.
													final int gender = Integer.parseInt(genderLimit); //게시글 성별 제한
													if(gender != userInfo.getGender()){ // 내 성별과 다르다면.
														runOnUiThread(new Runnable(){
															@Override public void run(){
																LoadingProgress.getHandler().sendEmptyMessage(0);
																Toast.makeText(ItemRead.this, (gender==1 ? "남성" : "여성")  + "만 참여 할 수 있습니다.", Toast.LENGTH_SHORT).show();
															}
														});
														return;
													}
												}
												if(!ageLimit.equals("null")){ // 연령층 제한이 있다면.
													if(ageLimit.indexOf(String.valueOf(userInfo.getAgeGroup())) == -1){ //내 연령이 포함되어 있지 않다면. 
														runOnUiThread(new Runnable(){
															@Override public void run(){
																LoadingProgress.getHandler().sendEmptyMessage(0);
																Toast.makeText(ItemRead.this, tvAgeLimit.getText() + "만 참여할 수 있습니다.", Toast.LENGTH_SHORT).show();
															}
														});
														return;
													}
												}
												
												Document itemVoewDoc = XmlUtil.readXml(url);
												if(XmlUtil.isXPath(itemVoewDoc, "//Vote_Info")){
													boolean isSuccess = Boolean.parseBoolean(XmlUtil.getXPath(itemVoewDoc, "//Vote_Success"));
													if(isSuccess){ // 투표하기에  성공했다면.
														runOnUiThread(new Runnable(){
															@Override public void run(){
																Toast.makeText(ItemRead.this, "투표되었습니다.", Toast.LENGTH_SHORT).show();
															}
														});
														for(int i=0; i<votePercentList.size(); i++){
															final TextView votePercentArea = (TextView) votePercentList.get(i); // 투표 퍼센티지를 나타내는 View 영역.
															TextView percentAreaVoted = (TextView) ((LinearLayout) view.getParent()).findViewById(R.id.votePercent); // 방금 클릭하여 투표한 투표 퍼센티지를 나타내는 View 영역.
															int voteCnt = voteCntList.get(i);
															float a = (float) (voteCnt + (percentAreaVoted==votePercentArea? 1 : 0));
															float b = (float) (voteTotalCnt+1);
															int percent  = (int) ((a/b)*100);
															final String votePercentText = percent + "% (" + (voteCnt + (percentAreaVoted==votePercentArea? 1 : 0)) + "명)";
															
															runOnUiThread(new Runnable(){
																@Override public void run(){
																	votePercentArea.setText(votePercentText);
																}
															});
														}
													}else{ // 투표하기에 실패했다면.
														if("alreadyVoted".equals(XmlUtil.getXPath(itemVoewDoc, "//ErrorMessage"))){
															runOnUiThread(new Runnable(){
																@Override public void run(){
																	Toast.makeText(ItemRead.this, "해당 게시물에 이미 투표하셨습니다.", Toast.LENGTH_SHORT).show();
																}
															});
														}else if("NotVotePeopleLimit".equals(XmlUtil.getXPath(itemVoewDoc, "//ErrorMessage"))){ // 인원제한초과로 인한 투표 종료.
															runOnUiThread(new Runnable(){
																@Override public void run(){
																	Toast.makeText(ItemRead.this, "투표가 종료되었습니다.", Toast.LENGTH_SHORT).show();
																}
															});
															
															//새로고침을 하여 종료되어 어떤 투표목록이 선택되었는지 보여준다.
															Intent intent = new Intent(ItemRead.this, ItemRead.class);  
															Bundle bundle = new Bundle() ;
															bundle.putLong("itemId", itemId) ;
															bundle.putString("list", listCategory);
															bundle.putString("listType", listType);
															intent.putExtras(bundle) ;
															//intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
															startActivity(intent);
															finish();
															return;
														}
													}
												}
											}
											catch (Exception e)
											{
												e.printStackTrace();
											}
											LoadingProgress.getHandler().sendEmptyMessage(0);
										}
									}).start();
								}
							});
				            int selection = Integer.parseInt(XmlUtil.getXPath(node, "Selection"));
				            if(selection == 1){ //선택된 투표 목록이라면.
				            	((FrameLayout) voteItem.getChildAt(0)).setBackgroundColor(Color.CYAN);
				            }
						} 
					}
				}
			}
			differentItem1 = (TextView) findViewById(R.id.differentItem1);
			differentItem2 = (TextView) findViewById(R.id.differentItem2);
			differentItem3 = (TextView) findViewById(R.id.differentItem3);
			differentItem4 = (TextView) findViewById(R.id.differentItem4);
			differentItem5 = (TextView) findViewById(R.id.differentItem5);
			differentItemTitle = (TextView) findViewById(R.id.differentItemTitle);
			differentItemTitle.setText("다른 사람들의 고민 목록");
			final String [] differentItemId = new String [5];
			final String [] differentItemTitel = new String [5];
			
			String url1 = PropertyUtil.getProperty("golaju.dbWork.url") + "/differentItem.jsp";
			Document differentItemDoc = XmlUtil.readXml(url1);
			if (XmlUtil.isXPath(differentItemDoc, "//DifferentItem_Info"))
			{
				boolean isSuccess = Boolean.parseBoolean(XmlUtil.getXPath(differentItemDoc, "//DifferentItem_Success"));
				if(isSuccess){
					Node differentArea = XmlUtil.getXPathes(differentItemDoc, "//DifferentItem").item(0);
					NodeList nodeList = differentArea.getChildNodes();
					for (int i=0; i<nodeList.getLength(); i++)
					{
						Node node = nodeList.item(i);
						if("Item".equals(node.getNodeName())) {
							differentItemId[i] = node.getFirstChild().getTextContent();
							differentItemTitel[i] = node.getFirstChild().getNextSibling().getTextContent();
							
							differentItem1.setText("* "+differentItemTitel[0]);
							differentItem1.setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(ItemRead.this, ItemRead.class);
									Bundle bundle = new Bundle() ;
									bundle.putLong("itemId", Long.parseLong(differentItemId[0])) ;
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
							});
							differentItem2.setText("* "+differentItemTitel[1]);
							differentItem2.setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(ItemRead.this, ItemRead.class);
									Bundle bundle = new Bundle() ;
									bundle.putLong("itemId", Long.parseLong(differentItemId[1])) ;
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
							});
							differentItem3.setText("* "+differentItemTitel[2]);
							differentItem3.setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(ItemRead.this, ItemRead.class);
									Bundle bundle = new Bundle() ;
									bundle.putLong("itemId", Long.parseLong(differentItemId[2])) ;
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
							});
							differentItem4.setText("* "+differentItemTitel[3]);
							differentItem4.setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(ItemRead.this, ItemRead.class);
									Bundle bundle = new Bundle() ;
									bundle.putLong("itemId", Long.parseLong(differentItemId[3])) ;
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
							});
							differentItem5.setText("* "+differentItemTitel[4]);
							differentItem5.setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(ItemRead.this, ItemRead.class);
									Bundle bundle = new Bundle() ;
									bundle.putLong("itemId", Long.parseLong(differentItemId[4])) ;
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
							});
						}
					}
				}
			}
			
		}
        catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onBackPressed()
	{
		
		
		Log.i("listCategory", listCategory);
		Log.i("listType", listType);
		
		//원본이미지 보기 또는  투표내용상세보기 또는 투표자목록보기 중이라면.
		if(originalImgListArea.getVisibility() == View.VISIBLE || voteTitleListArea.getVisibility() == View.VISIBLE || voteUserListArea.getVisibility() == View.VISIBLE){ 
			if(originalImgListArea.getVisibility() == View.VISIBLE){
				originalImgListArea.setVisibility(View.GONE);
			}else if(voteTitleListArea.getVisibility() == View.VISIBLE){
				voteTitleListArea.setVisibility(View.GONE);
			}else if(voteUserListArea.getVisibility() == View.VISIBLE){
				voteUserListArea.setVisibility(View.GONE);
			}
		}else if(listCategory.equals("myPageList")){
			LoadingProgress.showLoadingDialog(ItemRead.this);
			Intent itemList = new Intent(ItemRead.this, Mypage.class);
			itemList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Bundle bundle = new Bundle() ;
			bundle.putString("listType", listType);
			itemList.putExtras(bundle) ;
			startActivity(itemList);
			LoadingProgress.getHandler().sendEmptyMessage(0);
		}else if(listCategory.equals("itemList")){
			LoadingProgress.showLoadingDialog(ItemRead.this);
			Intent itemList = new Intent(ItemRead.this, ItemList.class);
			itemList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Bundle bundle = new Bundle() ;
			bundle.putString("listType", listType);
			itemList.putExtras(bundle) ;
			startActivity(itemList);
			LoadingProgress.getHandler().sendEmptyMessage(0);
		}
	}
	
}
