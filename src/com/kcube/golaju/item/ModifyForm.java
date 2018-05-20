package com.kcube.golaju.item;

import java.net.URLEncoder;

import org.w3c.dom.Document;

import com.kcube.golaju.R;
import com.kcube.golaju.util.PropertyUtil;
import com.kcube.golaju.util.XmlUtil;
import com.kucbe.golaju.common.LoadingProgress;
import com.kucbe.golaju.common.NormalActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ModifyForm extends NormalActivity
{
	Long itemId;
	String title;
	String content;
	
	EditText titleEdit;
	EditText contentEdit;
	
	/**제한 옵션, 이미지 삽입 등의 기능을 사용하기 위해 ITemCommon 인스턴스 생성.*/ 
	ItemCommon itemCommon; 
	
	LimitOption limitOption = new LimitOption(); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_form);
		
		initSetting(this, true, false); // 공통적인 초기 새팅을 한다.
		
		
		
		itemCommon = new ItemCommon(this);
		
		
		titleEdit = (EditText) findViewById(R.id.edt_title); // 타이틀 필드
    	contentEdit = (EditText) findViewById(R.id.edt_content); // 컨텐츠 필드
		
		Intent intent = getIntent();
		
		if(getIntent().hasExtra("itemId") && getIntent().hasExtra("title") && getIntent().hasExtra("content")){ // 게시글 목록에서 넘겨받은 itemId, title, content 값이 있다면.
    		itemId = intent.getExtras().getLong("itemId");
    		title = intent.getExtras().get("title").toString();
    		content = intent.getExtras().get("content").toString();
    	}else if(saveDataAsClosed.getLogin()){ // 메모리 문제로 인해 강제 종료된 경우를 대비하여 sharedData에 저장한 데이터로 값을 세팅한다.
    		Long savedItemId = saveDataAsClosed.getItemId();
    		String savedTitle = saveDataAsClosed.getTitle();
    		String savedContent = saveDataAsClosed.getContent();
			if(savedItemId != null && savedTitle != null && savedContent != null){
				itemId = savedItemId;
				title = savedTitle;
				content = savedContent;
			}
    	}
    	
		
		
		
		
		titleEdit.setText(title);
		contentEdit.setText(content);
		
		Button modifyBtn = (Button) findViewById(R.id.btn_modify);
		modifyBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LoadingProgress.showLoadingDialog(ModifyForm.this);
				new Thread(new Runnable(){
					@Override
					public void run()
					{
						String edtTitle = titleEdit.getText().toString();
						String edtContent = contentEdit.getText().toString();
						
						/* 제목, 컨텐츠 유효성 검사 Start */ 
						if(edtTitle == null || "".equals(edtTitle)){
							runOnUiThread(new Runnable(){
								@Override public void run(){
									LoadingProgress.getHandler().sendEmptyMessage(0);
									Toast.makeText(ModifyForm.this, "제목을 입력해 주세요.", Toast.LENGTH_SHORT).show();
								}
							});
							return;
						}
						if(edtContent == null || "".equals(edtContent)){
							runOnUiThread(new Runnable(){
								@Override public void run(){
									LoadingProgress.getHandler().sendEmptyMessage(0);
									Toast.makeText(ModifyForm.this, "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
								}
							});
							return;
						}
						/* 제목, 컨텐츠 유효성 검사 End */
						
						String url;
						try
						{
							url = PropertyUtil.getProperty("golaju.dbWork.url") + "/itemModify.jsp?itemId=" + itemId + "&title=" + URLEncoder.encode(edtTitle,"utf-8") + "&content=" + URLEncoder.encode(edtContent,"utf-8");
							//String url = "http://125.140.114.8/App/DBLink/itemModify.jsp?itemId=" + itemId + "&title=" + titleEdit.getText() + "&content=" + contentEdit.getText();
							//String url = "http://125.140.114.107/App/DBLink/itemModify.jsp?itemId=" + itemId + "&title=" + titleEdit.getText() + "&content=" + contentEdit.getText();
					       
							Document itemModifyDoc = XmlUtil.readXml(url);
							
							itemModifyDoc = XmlUtil.readXml(url);
							if (XmlUtil.isXPath(itemModifyDoc, "//ItemModify_Info"))
							{
								boolean isSuccess = Boolean.parseBoolean(XmlUtil.getXPath(itemModifyDoc, "//ItemModify_Success"));
								if(isSuccess){
									
									Intent intent = new Intent(ModifyForm.this, ItemRead.class);
									Bundle bundle = new Bundle();
									bundle.putLong("itemId", itemId);
									bundle.putString("list", getIntent().getExtras().get("list").toString());
									bundle.putString("listType", getIntent().getExtras().get("listType").toString());
									intent.putExtras(bundle) ;
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(intent);
									//finish();
									
									runOnUiThread(new Runnable(){
										@Override public void run(){
											Toast.makeText(ModifyForm.this, "게시글 수정을 성공하였습니다.", Toast.LENGTH_SHORT).show();
										}
									});
									
								}else{
									runOnUiThread(new Runnable(){
										@Override public void run(){
											Toast.makeText(ModifyForm.this, "게시글 수정을 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
		});
		
		Button modifyCancelBtn = (Button) findViewById(R.id.btn_modifyCancel);
		modifyCancelBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBackPressed(); //히스토리 back.
			}
		});
	}
	
}
