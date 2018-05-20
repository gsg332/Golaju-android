package com.kcube.golaju.option;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.kcube.golaju.R;
import com.kcube.golaju.data.Global;
import com.kcube.golaju.item.ItemCommon;
import com.kcube.golaju.util.PropertyUtil;
import com.kucbe.golaju.common.LoadingProgress;
import com.kucbe.golaju.common.NormalActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Option extends NormalActivity implements OnClickListener{

	EditText nameEdit;
	EditText emailEdit;
	EditText pwEdit;
	EditText newPwEdit;
	EditText reNewPwEdit;
	TextView id;
	Button submit;
	ImageView imgView;
	
	String name;
	String email;
	String pw;
	String newPw;
	String reNewPw;
	String loginId;
	Long userId;
	int imgcheck = 0;
	
	/**제한 옵션, 이미지 삽입 등의 기능을 사용하기 위해 ITemCommon 인스턴스 생성.*/ 
	ItemCommon itemCommon; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView( R.layout.option ) ;
		
		initSetting(this, true, false); // 공통적인 초기 새팅을 한다.
		
		
		
		
	
		itemCommon = new ItemCommon(this);
        
		nameEdit = (EditText)findViewById(R.id.option_name_edit);
		emailEdit = (EditText)findViewById(R.id.email_edit_text);
		pwEdit = (EditText)findViewById(R.id.nowPw_edit);
		newPwEdit = (EditText)findViewById(R.id.newPw_edit);
		reNewPwEdit = (EditText)findViewById(R.id.reNewPw_edit);
		
		id = (TextView)findViewById(R.id.option_id_text);
		
		submit = (Button)findViewById(R.id.option_submit);
		submit.setOnClickListener( this ) ;
		
		imgView = (ImageView) findViewById(R.id.profile_img_view);
		
		Global userInfo = (Global) getApplication();
		userId = userInfo.getUserId();
		loginId = userInfo.getLoginId();
		nameEdit.setText(userInfo.getUserName());
		emailEdit.setText(userInfo.getEmail());

		id.setText(userInfo.getLoginId());

		//String url = "http://www.oaasys.com/AttachmentAction.DoDownload.do?inline=true&path=thumb/2/7/7/oaasys8558040359244178843.jpg";
		//String url = "http://i1.daumcdn.net/imgsrc.search/search_all/2012/img/daumlogo.gif";
		/*
		URL bitmapUrl;
		try {
			bitmapUrl = new URL(url);
			HttpGet httpRequest = new HttpGet(bitmapUrl.toURI()); 
			HttpClient httpclient = new DefaultHttpClient(); 
			HttpResponse response = (HttpResponse) httpclient.execute(httpRequest); 
			HttpEntity entity = response.getEntity(); 
			BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity); 
			InputStream is = bufHttpEntity.getContent(); 
			Bitmap bm = BitmapFactory.decodeStream(is);
			imgView.setImageBitmap(bm);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		*/
		//imgView.setImageBitmap(getRemoteImage(url));
		
		
		if(userInfo.getThumbPath().equals("null")){
			imgView.setImageResource(R.drawable.user_default_58);
		}else{
			String userThumbUrl = null;
			try 
			{
				userThumbUrl = PropertyUtil.getProperty("golaju.photo.url") + userInfo.getThumbPath();
					
				imageLoader.displayImage(userThumbUrl, imgView, options);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		/*imgView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)  
			{
				if(imgView.getDrawable() == null){ //기본 투표 이미지가 없다면.(투표 항목에 사진이 첨부되어 있다면.)
					itemCommon.openDialogForVoteImage(imgView, itemCommon.IMAGE_DELECT_MODIFY); // 수정, 변경 alertDialog를 보여줌.
					imgcheck = 1;
					//Log.d("option imgcheck", Integer.toString(imgcheck) );
				}else{ // 기본 투표 이미지가 있다면.(투표 항목에 사진이 첨부 안 되어 있다면.)
					itemCommon.openDialogForVoteImage(imgView, itemCommon.IMAGE_INSERT); // 사진찍기, 이미지 불러오기 alertDialog를 보여줌.
					imgcheck = 1;
					//Log.d("option imgcheck", Integer.toString(imgcheck) );
				}
			}
		});*/
		
		findViewById(R.id.profile_img_view).setOnClickListener(this);
		
		submit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) // 등록 버튼 클릭
			{
				name = nameEdit.getText().toString();
				email = emailEdit.getText().toString();
				pw = pwEdit.getText().toString();
				newPw = newPwEdit.getText().toString();
				reNewPw = reNewPwEdit.getText().toString();
				
				boolean mailCk = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
				
				if( mailCk ){//이메일 유효성 검사
					Log.d("이메일 유효성", "성공");
					try
					{
						//String url = "http://www.oaasys.com/App/DBLink/accountUpdate.jsp?name=" + URLEncoder.encode(name, "UTF-8") + "&loginId=" + URLEncoder.encode(loginId, "UTF-8") + "&pw=" + URLEncoder.encode(pw, "UTF-8") + "&newPw=" + URLEncoder.encode(newPw, "UTF-8") + "&reNewPw=" + URLEncoder.encode(reNewPw, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8") + "&newPw=" + URLEncoder.encode(newPw, "UTF-8")+"&userId="+userId;
						//String url1 = "http://www.oaasys.com/App/DBLink/imgUpdate.jsp?name=" + URLEncoder.encode(name, "UTF-8") + "&loginId=" + URLEncoder.encode(loginId, "UTF-8") + "&pw=" + URLEncoder.encode(pw, "UTF-8") + "&newPw=" + URLEncoder.encode(newPw, "UTF-8") + "&reNewPw=" + URLEncoder.encode(reNewPw, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8") + "&newPw=" + URLEncoder.encode(newPw, "UTF-8")+"&userId="+userId;
						String url = PropertyUtil.getProperty("golaju.dbWork.url") + "/imgUpdate.jsp";
						//String url = "http://125.140.114.140/App/DBLink/imgUpdate.jsp?name=" + URLEncoder.encode(name, "UTF-8") + "&loginId=" + URLEncoder.encode(loginId, "UTF-8") + "&pw=" + URLEncoder.encode(pw, "UTF-8") + "&newPw=" + URLEncoder.encode(newPw, "UTF-8") + "&reNewPw=" + URLEncoder.encode(reNewPw, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8") + "&newPw=" + URLEncoder.encode(newPw, "UTF-8")+"&userId="+userId;
						//String url = "http://125.140.114.140/App/DBLink/accountUpdate.jsp?name=" + URLEncoder.encode(name, "UTF-8") + "&loginId=" + URLEncoder.encode(loginId, "UTF-8") + "&pw=" + URLEncoder.encode(pw, "UTF-8") + "&newPw=" + URLEncoder.encode(newPw, "UTF-8") + "&reNewPw=" + URLEncoder.encode(reNewPw, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8") + "&newPw=" + URLEncoder.encode(newPw, "UTF-8")+"&userId="+userId;
						//String url = "http://125.140.114.140/App/DBLink/accountUpdate.jsp?name=" + URLEncoder.encode(name, "UTF-8") + "&loginId=" + URLEncoder.encode(loginId, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8");
			    		
						MultipartEntity reqEntity = new MultipartEntity(); // 파라미터로 보내기 위한 MultipartEntity 객체 생성.
						if(imgcheck == 1){
							File file = new File(imgView.getTag().toString());
							Log.d("이미지 getTag", imgView.getTag().toString());
							reqEntity.addPart("imgView", new FileBody(file));
							Log.d("option imgcheck send", file.getPath());
						}
						Log.d("---imgcheck---", Integer.toString(imgcheck));
						reqEntity.addPart("userId", new StringBody(userId.toString()));
						reqEntity.addPart("loginId", new StringBody(loginId));
						reqEntity.addPart("pw", new StringBody(pw));
						reqEntity.addPart("newPw", new StringBody(newPw));
						reqEntity.addPart("reNewPw", new StringBody(reNewPw));
						reqEntity.addPart("email", new StringBody(email));
						reqEntity.addPart("name", new StringBody(name));
						reqEntity.addPart("imgcheck", new StringBody(Integer.toString(imgcheck)));
						
						
						HttpPost httppost = new HttpPost(url);
						httppost.setHeader("ENCTYPE", "multipart/form-data");
						httppost.setHeader("Connection","Keep-Alive");
						httppost.setEntity(reqEntity);
	
						HttpClient httpclient = new DefaultHttpClient();
						HttpResponse response = httpclient.execute(httppost); //execute(httppost)로 url과 파라미터를 전송하여 실행. response로 수행한 결과 값을 받음.
						Log.d("Golaju Write 응답 데이터", " 완료 ");
						
						HttpEntity resEntity = response.getEntity();
						Log.d("Golaju Write 응답 데이터456", "123");
						String responseData =EntityUtils.toString(resEntity);
						Log.d("---responseData", responseData);
						//String responseData = EntityUtils.toString(resEntity).toString(); // XML문서 읽기
						
						JSONObject jObj = new JSONObject( responseData ) ;
						
						final String ErrorMessage = jObj.getString("ErrorMessage");
						String newName = jObj.getString("name");
						String originalPath = jObj.getString("originalPath");
						String thumbPath = jObj.getString("thumbPath");
						String newEmail = jObj.getString("email");
						Log.d("ErrorMessage", ErrorMessage);
						Log.d("newName", newName);
						Log.d("originalPath", originalPath);
						Log.d("thumbPath", thumbPath);
						Log.d("newEmail", newEmail);
						
			    		if (ErrorMessage.equals("null"))
			    		{
			    			Global userInfo = (Global) getApplication();
			    			userInfo.setUserName(newName);
			    			if(!originalPath.equals("null") || !thumbPath.equals("null")){
			    				userInfo.setSavePath(originalPath);
				    			userInfo.setThumbPath(thumbPath);
			    			}
			    			userInfo.setEmail(newEmail);
			    			
		    				Log.d("수정 성공", "성공");
		    				Intent intent = new Intent(Option.this, Option.class);
		    				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		    				startActivity(intent);
		    				runOnUiThread(new Runnable(){
								@Override public void run(){
									Toast.makeText(Option.this, "수정을 성공하셨습니다.", Toast.LENGTH_SHORT).show();
								}
		    				});
		    				//finish();
			    		}else{
			    			runOnUiThread(new Runnable(){
								@Override public void run(){
									Toast.makeText(Option.this, ErrorMessage, Toast.LENGTH_SHORT).show();
								}
			    			});
			    		}
			    			
					} catch (Exception e){
						e.printStackTrace();
					}
				}else if(!mailCk){
					runOnUiThread(new Runnable(){
						@Override public void run(){
							Toast.makeText(getBaseContext(), "이메일 형식이 잘못되었습니다", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		});		
	}
	
	ImageView imgView2;
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Log.d("=======Image onClick======", "start");
		//View v = view;
		
		imgView2 = (ImageView) view;
		if(imgView2.getDrawable() == null){ //기본 투표 이미지가 없다면.(투표 항목에 사진이 첨부되어 있다면.)
			itemCommon.openDialogForVoteImage(imgView2, itemCommon.IMAGE_DELECT_MODIFY); // 수정, 변경 alertDialog를 보여줌.
			imgcheck = 1;
			//Log.d("option imgcheck", Integer.toString(imgcheck) );
		}else{ // 기본 투표 이미지가 있다면.(투표 항목에 사진이 첨부 안 되어 있다면.)
			itemCommon.openDialogForVoteImage(imgView2, itemCommon.IMAGE_INSERT); // 사진찍기, 이미지 불러오기 alertDialog를 보여줌.
			imgcheck = 1;
			//Log.d("option imgcheck", Integer.toString(imgcheck) );
		}
		
		
		/*LoadingProgress.showLoadingDialog(Option.this);
		new Thread(new Runnable(){
			@Override
			public void run()
			{
				if(v == submit){
					name = nameEdit.getText().toString();
					email = emailEdit.getText().toString();
					pw = pwEdit.getText().toString();
					newPw = newPwEdit.getText().toString();
					reNewPw = reNewPwEdit.getText().toString();
					
					boolean mailCk = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
					
					if( mailCk ){//이메일 유효성 검사
						Log.d("이메일 유효성", "성공");
						try
						{
							//String url = "http://www.oaasys.com/App/DBLink/accountUpdate.jsp?name=" + URLEncoder.encode(name, "UTF-8") + "&loginId=" + URLEncoder.encode(loginId, "UTF-8") + "&pw=" + URLEncoder.encode(pw, "UTF-8") + "&newPw=" + URLEncoder.encode(newPw, "UTF-8") + "&reNewPw=" + URLEncoder.encode(reNewPw, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8") + "&newPw=" + URLEncoder.encode(newPw, "UTF-8")+"&userId="+userId;
							//String url1 = "http://www.oaasys.com/App/DBLink/imgUpdate.jsp?name=" + URLEncoder.encode(name, "UTF-8") + "&loginId=" + URLEncoder.encode(loginId, "UTF-8") + "&pw=" + URLEncoder.encode(pw, "UTF-8") + "&newPw=" + URLEncoder.encode(newPw, "UTF-8") + "&reNewPw=" + URLEncoder.encode(reNewPw, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8") + "&newPw=" + URLEncoder.encode(newPw, "UTF-8")+"&userId="+userId;
							String url = PropertyUtil.getProperty("golaju.dbWork.url") + "/imgUpdate.jsp";
							//String url = "http://125.140.114.140/App/DBLink/imgUpdate.jsp?name=" + URLEncoder.encode(name, "UTF-8") + "&loginId=" + URLEncoder.encode(loginId, "UTF-8") + "&pw=" + URLEncoder.encode(pw, "UTF-8") + "&newPw=" + URLEncoder.encode(newPw, "UTF-8") + "&reNewPw=" + URLEncoder.encode(reNewPw, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8") + "&newPw=" + URLEncoder.encode(newPw, "UTF-8")+"&userId="+userId;
							//String url = "http://125.140.114.140/App/DBLink/accountUpdate.jsp?name=" + URLEncoder.encode(name, "UTF-8") + "&loginId=" + URLEncoder.encode(loginId, "UTF-8") + "&pw=" + URLEncoder.encode(pw, "UTF-8") + "&newPw=" + URLEncoder.encode(newPw, "UTF-8") + "&reNewPw=" + URLEncoder.encode(reNewPw, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8") + "&newPw=" + URLEncoder.encode(newPw, "UTF-8")+"&userId="+userId;
							//String url = "http://125.140.114.140/App/DBLink/accountUpdate.jsp?name=" + URLEncoder.encode(name, "UTF-8") + "&loginId=" + URLEncoder.encode(loginId, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8");
				    		
							MultipartEntity reqEntity = new MultipartEntity(); // 파라미터로 보내기 위한 MultipartEntity 객체 생성.
							if(imgcheck == 1){
								File file = new File(imgView.getTag().toString());
								Log.d("이미지 getTag", imgView.getTag().toString());
								reqEntity.addPart("imgView", new FileBody(file));
								Log.d("option imgcheck send", file.getPath());
							}
							Log.d("---imgcheck---", Integer.toString(imgcheck));
							reqEntity.addPart("userId", new StringBody(userId.toString()));
							reqEntity.addPart("loginId", new StringBody(loginId));
							reqEntity.addPart("pw", new StringBody(pw));
							reqEntity.addPart("newPw", new StringBody(newPw));
							reqEntity.addPart("reNewPw", new StringBody(reNewPw));
							reqEntity.addPart("email", new StringBody(email));
							reqEntity.addPart("name", new StringBody(name));
							reqEntity.addPart("imgcheck", new StringBody(Integer.toString(imgcheck)));
							
							
							HttpPost httppost = new HttpPost(url);
							httppost.setHeader("ENCTYPE", "multipart/form-data");
							httppost.setHeader("Connection","Keep-Alive");
							httppost.setEntity(reqEntity);
		
							HttpClient httpclient = new DefaultHttpClient();
							HttpResponse response = httpclient.execute(httppost); //execute(httppost)로 url과 파라미터를 전송하여 실행. response로 수행한 결과 값을 받음.
							Log.d("Golaju Write 응답 데이터", " 완료 ");
							
							HttpEntity resEntity = response.getEntity();
							Log.d("Golaju Write 응답 데이터456", "123");
							String responseData =EntityUtils.toString(resEntity);
							Log.d("---responseData", responseData);
							//String responseData = EntityUtils.toString(resEntity).toString(); // XML문서 읽기
							
							JSONObject jObj = new JSONObject( responseData ) ;
							
							final String ErrorMessage = jObj.getString("ErrorMessage");
							String newName = jObj.getString("name");
							String originalPath = jObj.getString("originalPath");
							String thumbPath = jObj.getString("thumbPath");
							String newEmail = jObj.getString("email");
							Log.d("ErrorMessage", ErrorMessage);
							Log.d("newName", newName);
							Log.d("originalPath", originalPath);
							Log.d("thumbPath", thumbPath);
							Log.d("newEmail", newEmail);
							
				    		if (ErrorMessage.equals("null"))
				    		{
				    			Global userInfo = (Global) getApplication();
				    			userInfo.setUserName(newName);
				    			if(!originalPath.equals("null") || !thumbPath.equals("null")){
				    				userInfo.setSavePath(originalPath);
					    			userInfo.setThumbPath(thumbPath);
				    			}
				    			userInfo.setEmail(newEmail);
				    			
			    				Log.d("수정 성공", "성공");
			    				Intent intent = new Intent(Option.this, Option.class);
			    				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			    				startActivity(intent);
			    				runOnUiThread(new Runnable(){
									@Override public void run(){
										Toast.makeText(Option.this, "수정을 성공하셨습니다.", Toast.LENGTH_SHORT).show();
									}
			    				});
			    				//finish();
				    		}else{
				    			runOnUiThread(new Runnable(){
									@Override public void run(){
										Toast.makeText(Option.this, ErrorMessage, Toast.LENGTH_SHORT).show();
									}
				    			});
				    		}
				    			
						} catch (Exception e){
							e.printStackTrace();
						}
					}else if(!mailCk){
						runOnUiThread(new Runnable(){
							@Override public void run(){
								Toast.makeText(getBaseContext(), "이메일 형식이 잘못되었습니다", Toast.LENGTH_SHORT).show();
							}
						});
					}
				}
				
				LoadingProgress.getHandler().sendEmptyMessage(0);
			}
		}).start();*/
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		itemCommon.insertImage(requestCode, resultCode, data);
	}
}
