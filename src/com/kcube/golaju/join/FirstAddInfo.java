package com.kcube.golaju.join;

import com.kcube.golaju.R;
import com.kucbe.golaju.common.NormalActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class FirstAddInfo extends NormalActivity implements OnClickListener 
{
	String gender = null;
	ImageView man;
	ImageView woman;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_add_info);
		
		initSetting(this, false, false); // 공통적인 초기 새팅을 한다.
		
		
		
		man = (ImageView) findViewById(R.id.img_man);
		woman = (ImageView) findViewById(R.id.img_woman);
		man.setOnClickListener(this);
		woman.setOnClickListener(this);
		
		Button genderInput = (Button) findViewById(R.id.btn_genderInput);
		genderInput.setOnClickListener(new View.OnClickListener() { //성별 입력 버튼을 클릭했으면.
			@Override
			public void onClick(View v)
			{
				if(gender != null){ //성별을 선택했으면.
					Intent intent = new Intent(FirstAddInfo.this, SecondAddInfo.class);
					Bundle bundle = new Bundle( ) ;
					
					
					if(getIntent().hasExtra("loginId")){ // 게시글 목록에서 넘겨받은 loginId값이 있다면.
						bundle.putString("loginId", getIntent().getExtras().get("loginId").toString());
		        	}else if(saveDataAsClosed.getLogin()){ // 메모리 문제로 인해 강제 종료된 경우를 대비하여 sharedData에 저장한 데이터로 값을 세팅한다.
		        		String savedLoginId = saveDataAsClosed.getLoginId();
		    			if(savedLoginId != null){
		    				bundle.putString("loginId", savedLoginId);
		    			}
		        	}
		        	
					
					
					
					
					
					
					bundle.putString("gender", gender) ;
					intent.putExtras(bundle) ;
					startActivity(intent);
				}else{ //성별을 선택하지 않았으면.
					Toast.makeText(FirstAddInfo.this, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
				}
			} 
        }); 
	}

	@Override
	public void onClick(View v)
	{
		man.setImageResource(R.drawable.ic_launcher);
		woman.setImageResource(R.drawable.ic_launcher);
		
		ImageView genderImg = (ImageView) findViewById(v.getId());
		genderImg.setImageResource(R.drawable.selected);
		
		gender = v.getTag().toString();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    switch (keyCode) {
	    case KeyEvent.KEYCODE_BACK:
	    	Log.i("add guide1","add guide1");
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
