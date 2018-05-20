package com.kcube.golaju.join;

import com.kcube.golaju.R;
import com.kucbe.golaju.common.NormalActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class AddJoinInfoGuide extends NormalActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_info_guide);
		
		initSetting(this, false, false); // 공통적인 초기 새팅을 한다.
		
		
		Button addInfoInput = (Button) findViewById(R.id.btn_addInfoInput);

		addInfoInput.setOnClickListener(new View.OnClickListener() { //추가 정보 입력 버튼을 클릭했으면.
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(AddJoinInfoGuide.this, FirstAddInfo.class);
				Bundle bundle = new Bundle( ) ;
				
				
				
				
				if(getIntent().hasExtra("loginId")){ // 게시글 목록에서 넘겨받은 loginId값이 있다면.
	        		bundle.putString("loginId", getIntent().getExtras().get("loginId").toString());
	        	}else if(saveDataAsClosed.getLogin()){ // 메모리 문제로 인해 강제 종료된 경우를 대비하여 sharedData에 저장한 데이터로 값을 세팅한다.
	        		String savedLoginId = saveDataAsClosed.getLoginId();
	    			if(savedLoginId != null){
	    				bundle.putString("loginId", savedLoginId);
	    			}
	        	}
	        	
	        	
				
				
				
				
				
				intent.putExtras(bundle) ;
				startActivity(intent);
				finish();
			} 
        });
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    switch (keyCode) {
	    case KeyEvent.KEYCODE_BACK:
	    	Log.i("add guide","add guide");
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

}


	