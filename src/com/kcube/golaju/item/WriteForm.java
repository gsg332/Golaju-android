package com.kcube.golaju.item;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.kcube.golaju.R;
import com.kucbe.golaju.common.LoadingProgress;
import com.kucbe.golaju.common.NormalActivity;
import com.kcube.golaju.data.Global;
import com.kcube.golaju.item.ItemCommon;
import com.kcube.golaju.util.PropertyUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class WriteForm extends NormalActivity implements OnClickListener
{
	/**제한 옵션, 이미지 삽입 등의 기능을 사용하기 위해 ITemCommon 인스턴스 생성.*/ 
	ItemCommon itemCommon; 
	/**최종적으로 등록버튼을 눌렀을 때, 해당 view에서 getText()로 값을 가져오기 위해 view를 저장할 변수.*/ 
	LimitOption limitOption = new LimitOption(); 
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_form);
		
		initSetting(this, true, false); // 공통적인 초기 새팅을 한다.
		
		
		
		itemCommon = new ItemCommon(this);
		

		//ImageButton voteWriteBtn = (ImageButton) findViewById(R.id.btn_voteWrite);
        //voteWriteBtn.setVisibility(View.GONE);
        
        final LinearLayout voteListArea = (LinearLayout) findViewById(R.id.vote_listArea); // 투표 항목 전체 영역. 
        
        final ArrayList<View> votes = new ArrayList<View>(); // 투표 항목 각각을 차례대로 저장할 변수. 여기에 투표항목을 저장해 두었다가 항목을 삭제할 때 비교하여 찾아 삭제하거나 최종 등록할때 여기에 있는 값을 확인하여 등록시킴.
        
        votes.add((LinearLayout) findViewById(R.id.voteArea1)); // 첫번째 투표항목.
        votes.add((LinearLayout) findViewById(R.id.voteArea2)); // 두번째 투표항목.
        
        ImageButton votePlus = (ImageButton) findViewById(R.id.btn_votePlus); // 투표항목 추가 버튼.

    	votePlus.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) // 투표항목 추가 버튼을 클릭. 클릭하면 투표 입력 필드 추가.
			{
				final LinearLayout lLayout = new LinearLayout(act);
				final RelativeLayout rLayout = new RelativeLayout(act);
				final EditText plusEdit = new EditText(act);
				final ImageButton clearBtn = new ImageButton(act);
				final ImageView plusImgBtn = new ImageView(act);
				
				/* 추가 투표항목 그리기 Start */
				LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				lLayout.setOrientation(LinearLayout.HORIZONTAL);
				lLayout.setLayoutParams(llParams);
				lLayout.setGravity(Gravity.CENTER);
				
				LinearLayout.LayoutParams rlParams =  new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1);
				rLayout.setLayoutParams(rlParams);
				lLayout.addView(rLayout);
				
				LinearLayout.LayoutParams peParam = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				plusEdit.setLayoutParams(peParam);
				plusEdit.setBackgroundResource(R.drawable.edittext_style);
				plusEdit.setHint("항목을 입력해 주세요 ");
				plusEdit.setSingleLine();
				rLayout.addView(plusEdit);
				
				RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				param.addRule(RelativeLayout.CENTER_VERTICAL);
				param.setMargins(0, 0, 10, 0);
				clearBtn.setLayoutParams(param);
				clearBtn.setBackgroundResource(android.R.drawable.ic_delete);
				clearBtn.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) // 투표 항목 삭제 버튼 클릭. votes에 등록(저장)된 투표항목을 차례대로 돌면서 클릭한 클릭의 x버튼의 부모의 부모(투표 항목 Layout)와 일치한다면 해당 View를 삭제한다는 뜻이므로 그 해당 View를 삭제.  
					{
						for(int i=0; i<votes.size(); i++){
							LinearLayout saveVote = (LinearLayout) votes.get(i); // 저장된 투표 목록 
							LinearLayout clearVote = (LinearLayout) v.getParent().getParent(); // 클릭(삭제)된 투표목록.

							if(saveVote == clearVote ){
								votes.remove(i);
							}
						}
						
						voteListArea.removeView(lLayout); // 투표 항목 제거.
					}
				});
				rLayout.addView(clearBtn);
				
				int pixel = (int) (50 * getResources().getDisplayMetrics().density); // dp -> pixel 변환
				plusImgBtn.setLayoutParams(new LinearLayout.LayoutParams(pixel, pixel, 0));
				plusImgBtn.setImageResource(android.R.drawable.ic_menu_camera);
				plusImgBtn.setOnClickListener((OnClickListener) act);
				lLayout.addView(plusImgBtn);
				
				voteListArea.addView(lLayout); // 투표 항목 전체 영역에 추가된 투표항목 붙이기.
				
				votes.add(lLayout);  // 삭제 및 최정 등록을 위하여 votes변수에 추가된 투표항목을 추가.
				/* 추가 투표항목 그리기 End */
			}
		});
        
        Button limtOptionBtn = (Button) findViewById(R.id.btn_limitOption); // 제한추가 버튼.
        limtOptionBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)  //옵션 버튼을 클릭했으면.
			{
				 itemCommon.openDialogLimitOption(itemCommon.LIMIT_LIST, limitOption, null); // 제한 옵션 목록 띄우기.
			}
		});
    	
    	findViewById(R.id.bnt_votePic1).setOnClickListener(this); // 첫번째 투표항목 사진 버튼에 클릭이벤트 걸기.
    	findViewById(R.id.bnt_votePic2).setOnClickListener(this); // 두번째 투표항목 사진 버튼에 클릭이벤트 걸기.
    	
    	final EditText titleEdit = (EditText) findViewById(R.id.edt_title); // 타이틀 필드
    	final EditText contentEdit = (EditText) findViewById(R.id.edt_content); // 컨텐츠 필드
    	
    	Button regBtn = (Button) findViewById(R.id.btn_register); // 등록 버튼
    	regBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) // 등록 버튼 클릭
			{
				
				
				LoadingProgress.showLoadingDialog(WriteForm.this);
				new Thread(new Runnable(){
					@Override
					public void run()
					{
						Global userInfo = (Global) getApplication(); // 현재 로그인 중인 사용자 정보를 가져오기 위해 Global 인스턴스를 저장할 변수. 
						
						Long userId = userInfo.getUserId();
						String loginId = userInfo.getLoginId();
						String loginPw = userInfo.getLoginPw();
						String userName = userInfo.getUserName();
						String title = titleEdit.getText().toString();
						String content = contentEdit.getText().toString();
						
						Long timeLimit = null;
						TextView timeLimitView = (TextView) limitOption.getTimeLimit();
						if(timeLimitView != null){ // 시간 제한이 있는 경우.
							int hour = Integer.parseInt(timeLimitView.getText().toString().replace("시간", "")); // 시간 제한 View의 텍스트에서 "시간"이란 글자만 빼고 숫자만 추출.  
							timeLimit = System.currentTimeMillis() + (1000 * 60 * 60 * hour); // 현재 시간에서 시간을 밀리세컨드 단위로 계산하여 더하기.
		
							/*
							Log.i("hour", hour + "");
							Log.i("date", new Date(timeLimit).getDate() + "");
							Log.i("hours", new Date(timeLimit).getHours() + "");
							Log.i("minutes", new Date(timeLimit).getMinutes() + "");
							Log.i("day", new Date(timeLimit).getDay() + "");
							Log.i("밀리세컨트 시간", timeLimit + "");
							*/
						}
						
						Integer peopleLimit = null;
						TextView peopleLimitView = (TextView) limitOption.getPeopleLimit();
						if(peopleLimitView != null){ // 인원 제한이 있는 경우.
							peopleLimit = Integer.parseInt(peopleLimitView.getText().toString().replace("명", "")); // 인원 제한 View의 텍스트에서 "명"이란 글자만 빼고 숫자만 추출.  
						}
						
						Integer genderLimit = null;
						TextView genderLimitView = (TextView) limitOption.getGenderLimit();
						if(genderLimitView != null){ // 성별 제한이 있는 경우.
							genderLimit = genderLimitView.getText().toString().equals("남성")? 1 : 2; // 성별 제한 View의 텍스트에서 남성일 경우 1, 여성일 경우 2의 값을 저장.
						}
						
						String ageGroupLimit = null;
						TextView ageGroupLimitView = (TextView) limitOption.getAgeGroupLimit();
						if(ageGroupLimitView != null){ // 연령층 제한이 있는 경우.
							ageGroupLimit = ageGroupLimitView.getText().toString().replaceAll("대 이상", "").replaceAll("대", ""); // 연령 제한 View의 텍스트에서 "대 이상"과 "대"를 모두 제거 하고  "10, 20, 50"과 같은 형태로 저장 시킴.
						}
		
						/* 제목, 컨텐츠 유효성 검사 Start */ 
						if(title == null || "".equals(title)){ 
							runOnUiThread(new Runnable(){
								@Override public void run(){
									LoadingProgress.getHandler().sendEmptyMessage(0);
									Toast.makeText(WriteForm.this, "제목을 입력해 주세요.", Toast.LENGTH_SHORT).show();
								}
							});
							return;
						}
						if(content == null || "".equals(content)){
							runOnUiThread(new Runnable(){
								@Override public void run(){
									LoadingProgress.getHandler().sendEmptyMessage(0);
									Toast.makeText(WriteForm.this, "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
								}
							});
							return;
						}
						/* 제목, 컨텐츠 유효성 검사 End */
						
						try {
							// 웹 DB서버에 저장 시키기 위한 url 주소
							String url = PropertyUtil.getProperty("golaju.dbWork.url") + "/itemWrite.jsp";
							//String url = "http://125.140.114.8/App/DBLink/itemWrite.jsp";
							//String url = "http://125.140.114.107/App/DBLink/itemWrite.jsp";
							
							MultipartEntity reqEntity = new MultipartEntity(); // 파라미터로 보내기 위한 MultipartEntity 객체 생성.
							
							for(int i=0; i<votes.size(); i++){ // 현재의 투표 항목을 차례대로 돌면서  투표항목에 대한 유효성 검사를 하고 파라미터로 전송하기 위하여 투표 항목을 세팅.
								LinearLayout vote = (LinearLayout) votes.get(i);
								RelativeLayout voteRelative = (RelativeLayout) vote.getChildAt(0);
								EditText voteEdit = (EditText) voteRelative.getChildAt(0); // 투표 텍스트 영역.
								ImageView voteImage = (ImageView) vote.getChildAt(1); // 투표 이미지 영역.
								
								// 투표항목 유효성 검사. 투표 텍스트 영역과 투표 사진 첨부 영역 둘다 입력이 되어 있지 않으면 발생. 
								if((voteEdit.getText()==null || voteEdit.getText().toString().equals("")) && (voteImage.getTag()==null || voteImage.getTag().toString().equals(""))){
									runOnUiThread(new Runnable(){
										@Override public void run(){
											LoadingProgress.getHandler().sendEmptyMessage(0);
											Toast.makeText(WriteForm.this, "투표 항목의 내용 또는 사진을 입력해 주세요.", Toast.LENGTH_SHORT).show();
										}
									});
									return;
								}
								
								reqEntity.addPart("voteText" + (i+1), new StringBody((voteEdit.getText()==null || voteEdit.getText().toString().equals("")? "null" : voteEdit.getText().toString())));
								
								if(voteImage.getTag()==null || voteImage.getTag().toString().equals("")){ // Tag에 값이 없으면 사진을 입력하지 않았다는 뜻이므로 null를 세팅.
									reqEntity.addPart("voteImage" + (i+1), new StringBody("null"));
								}else{ // Tag에 값이 있으면 사진을 입력했다는 뜻이므로 해당 파일을 새롭게 생성하여 파라미터에 세팅.
									File file = new File(voteImage.getTag().toString());
									reqEntity.addPart("voteImage" + (i+1), new FileBody(file));
								}
							}
							
							reqEntity.addPart("userId", new StringBody(userId.toString()));
							reqEntity.addPart("loginId", new StringBody(loginId));
							reqEntity.addPart("loginPw", new StringBody(loginPw));
							reqEntity.addPart("userName", new StringBody(userName));
							reqEntity.addPart("title", new StringBody(title));
							reqEntity.addPart("content", new StringBody(content));
							
							if(timeLimit != null){ // 시간 제한이 있으면.
								reqEntity.addPart("timeLimit", new StringBody(timeLimit.toString()));
							}
							if(peopleLimit != null){ // 인원 제한이 있으면.
								reqEntity.addPart("peopleLimit", new StringBody(peopleLimit.toString()));
							}
							if(genderLimit != null){ // 성별 제한이 있으면.
								reqEntity.addPart("genderLimit", new StringBody(genderLimit.toString()));
							}
							if(ageGroupLimit != null){ // 연령 제한이 있으면.
								reqEntity.addPart("ageGroupLimit", new StringBody(ageGroupLimit));
							}
							
							HttpPost httppost = new HttpPost(url);
							httppost.setEntity(reqEntity);
		
							HttpClient httpclient = new DefaultHttpClient();
							HttpResponse response = httpclient.execute(httppost); //execute(httppost)로 url과 파라미터를 전송하여 실행. response로 수행한 결과 값을 받음.
							
							HttpEntity resEntity = response.getEntity();
					   
							String responseData = EntityUtils.toString(resEntity).toString(); // XML문서 읽기
					   
							//Log.d("Golaju Write 응답 데이터", "리스폰스" + responseData);
							
							DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
							DocumentBuilder builder = factory.newDocumentBuilder();
		
							Document document = builder.parse(new InputSource(new StringReader(responseData)));
							
							NodeList write_Success = document.getElementsByTagName("Write_Success"); // Write_Success라는 이름의 요소를 가진값을 찾아 저장.
					   
							String isSuccess = write_Success.item(0).getFirstChild().getNodeValue(); // Write_Success요소의 안에 있는 값을 저장.
							
							if(isSuccess.equals("true"))
							{
								runOnUiThread(new Runnable(){
									@Override public void run(){
										Toast.makeText(WriteForm.this, "등록성공", Toast.LENGTH_SHORT).show();
									}
								});
					    		
					    		Intent intent = new Intent(WriteForm.this, ItemList.class);
					    		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							}
							else
							{
								runOnUiThread(new Runnable(){
									@Override public void run(){
										Toast.makeText(WriteForm.this, "등록실패", Toast.LENGTH_SHORT).show();
									}
								});
							}
					   
							httpclient.getConnectionManager().shutdown();
						} catch (Exception e) {
							e.printStackTrace();
						}
						LoadingProgress.getHandler().sendEmptyMessage(0);
					}
				}).start();
				
			}
		});
    	
    	Button cancelBtn = (Button) findViewById(R.id.btn_writeCancel); // 취소 버튼
    	cancelBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) // 취소 버튼 클릭
			{
				Intent intent = new Intent(WriteForm.this, ItemList.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}

	
	ImageView votePicArea; // 투표 항목 사진 영역
	@Override
	public void onClick(View view)
	{
		votePicArea = (ImageView) view;
		
		if(votePicArea.getDrawable() == null){ //기본 투표 이미지가 없다면.(투표 항목에 사진이 첨부되어 있다면.)
			itemCommon.openDialogForVoteImage(votePicArea, itemCommon.IMAGE_DELECT_MODIFY); // 수정, 변경 alertDialog를 보여줌.
		}else{ // 기본 투표 이미지가 있다면.(투표 항목에 사진이 첨부 안 되어 있다면.)
			itemCommon.openDialogForVoteImage(votePicArea, itemCommon.IMAGE_INSERT); // 사진찍기, 이미지 불러오기 alertDialog를 보여줌.
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		itemCommon.insertImage(requestCode, resultCode, data);
	}
}
