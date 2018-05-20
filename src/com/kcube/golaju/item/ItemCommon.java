package com.kcube.golaju.item;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.kcube.golaju.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class ItemCommon
{
	Activity activity; //현재 보여지고 있는 activity
	ImageView imageView; //투표사진 영역
	
	static final int PICK_FROM_ALBUM  = 1; 
	static final int PICK_FROM_CAMERA = 2;
	
	Uri imageUri = null;
	File tempImgFile = null;
		
	public ItemCommon(Activity act)
	{
		activity = act;
	}
	
	
	final public int IMAGE_DELECT_MODIFY = 1;
	final public int IMAGE_INSERT = 2;
	/**
	 * alertDialog로 이미지가 첨부된 경우 수정, 삭제 다이어로그를 보여주고 첨부되지 않은 경우 카메라찍기와 앨범에서 불러오기 다이어로그를 보여준다.
	 */
	public void openDialogForVoteImage(ImageView imgView, int listType){
		
		imageView = imgView;

		String title = null;
		String positiveStr= null;
		String negativeStr= null;
		DialogInterface.OnClickListener positive = null;
		DialogInterface.OnClickListener negative = null;

		switch (listType)
		{
			case IMAGE_DELECT_MODIFY : 
				
				title = "삭제 수정";
				positiveStr = "삭제";
				negativeStr = "변경";
						
				positive = new DialogInterface.OnClickListener() 
				{
					@SuppressLint("NewApi")
					@Override
					public void onClick(DialogInterface dialog, int which) //삭제
					{
						imageView.setBackground(null);
						imageView.setImageResource(android.R.drawable.ic_menu_camera);
						
						imageView.setTag(null);
					}
				};
				
				negative = new DialogInterface.OnClickListener( )
				{
					@Override
					public void onClick(DialogInterface dialog, int which) //수정
					{
						openDialogForVoteImage(imageView, IMAGE_INSERT);
					}
				};
				
				break;

			case IMAGE_INSERT : 
				
				title = "이미지 삽입";
				positiveStr = "사진찍기";
				negativeStr = "불러오기";
				
				positive = new DialogInterface.OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) //카메라찍기
					{
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					    String fileName = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
					    tempImgFile = new File(Environment.getExternalStorageDirectory(), fileName);
					    imageUri = Uri.fromFile(tempImgFile);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
						activity.startActivityForResult(intent, PICK_FROM_CAMERA);
					}
				};
				
				negative = new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) //사진 불러오기
					{
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_GET_CONTENT);
						intent.setType("image/*");
						activity.startActivityForResult(intent, PICK_FROM_ALBUM) ;
					}
				};
				
				break;
				
			default: break;
		}
		
		AlertDialog.Builder adImage = new AlertDialog.Builder(activity) ;
		adImage.setMessage(title)
		.setCancelable(true)
		.setPositiveButton(positiveStr, positive)
		.setNegativeButton(negativeStr, negative)
		.create()
		.show();
	}
	

	/**
	 * 사진찍기 및 사진 불러오기에서 얻은 이미지를 이미지영역에 삽입한다.
	 */
	public void insertImage(int requestCode, int resultCode, Intent data){
		
		String path ;
		
		String filePath = null;
		Bitmap bmp = null;
		
		if(resultCode == Activity.RESULT_OK) { //성공했을 경우.
		
			filePath = activity.getCacheDir().getPath() + System.currentTimeMillis() + ".jpg"; //캐쉬 폴더 임시로 저장할 사진 경로?
            File imgFile = new File(filePath);
            
			if(requestCode == ItemCommon.PICK_FROM_ALBUM) //사진 불러오기를 실행했으면.
			{
				Uri currImageURI = data.getData(); //현재 카메라로 찍은 이미지 경로 얻어오기.
				path = getRealPathFromURI(currImageURI); //이미지의 URI를 통해 진짜 이미지 경로를 가져온다??
		        
		        BitmapFactory.Options options = new BitmapFactory.Options(); 
		        options.inJustDecodeBounds = true; 
		        BitmapFactory.decodeFile(path, options);
		        int fscale = options.outHeight;
		        if(options.outWidth > options.outHeight)
		        {
		        	fscale = options.outWidth;
		        }

		        if(800 < fscale) {
		        	fscale = fscale / 800;
		        		        
			        BitmapFactory.Options options2 = new BitmapFactory.Options();
			        options2.inSampleSize = fscale;
			        bmp = BitmapFactory.decodeFile( path, options2 );
		        } else {
		        	bmp = BitmapFactory.decodeFile( path );
		        }
		        
			} else if(requestCode == ItemCommon.PICK_FROM_CAMERA) { // 카메라 찍기를 실행했으면.
				String imagePath = imageUri.getPath();
				bmp = BitmapFactory.decodeFile(imagePath);
			}
			
			try {
				Drawable drawable = new BitmapDrawable(activity.getResources() ,Bitmap.createScaledBitmap(bmp, 500, 500, false)); //2048 * 2048가 넘는 사이즈의 bitmap인 경우 기종에 따라 이미지가 imageView에 보이지 않는 현상이 있을 수 있기 때문에 500 * 500 사이즈로 축소 해서 넣음. 
				imageView.setBackground(drawable);
				
				imageView.setTag(filePath); // 선택한 이미지의 파일 경로를 이미지 영역의 Tag로 저장. 최종적으로 등록버튼을 눌렀을 때 이 값으로 해당 이미지 파일을 세팅하여 전송시킴.
				
		    	imgFile.createNewFile();
				OutputStream out = new FileOutputStream(imgFile);
				bmp.compress(CompressFormat.JPEG, 80, out);
				out.close();

				imageView.setImageDrawable(null);
			} catch (FileNotFoundException e) {
				e.printStackTrace(); 
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			
			if(tempImgFile != null){
				if(tempImgFile.exists()){
					tempImgFile.delete(); // 사진찍기에서 임시로 저장한 이미지 파일 삭제.
				}
			}
		} else if(resultCode == Activity.RESULT_CANCELED) { //실패했을 경우.
			
		}
	}
	public String getRealPathFromURI(Uri contentUri)
	{
		String [] proj={ MediaStore.Images.Media.DATA } ;
		Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null) ; 
		int column_index = cursor.getColumnIndexOrThrow( MediaStore.Images.Media.DATA ) ;
		cursor.moveToFirst( ) ;
		return cursor.getString( column_index ) ;
	}
	
	
	final public int LIMIT_LIST = 100;
	final public int TIME_LIMIT_LIST = 201;
	final public int PEOPLE_LIMIT_LIST = 202;
	final public int GENDER_LIMIT_LIST = 203;
	final public int AGE_GROUP_LIMIT_LIST = 204;
	/**
	 * alertDialog로 제한 설정 목록을 띄워주고 지정한 설정을 화면에 표시.
	 * listType : 어떤 제한 설정 목록을 보여줄지를 알리기 위함.
	 * limitOption : 설정된 제한 설정을 저장하기 위한 bean객체.
	 * textView : 제한옵션을 변경할 경우, 변경을 윈하는 TextView지정. 새로운 제한으로 추가할 경우 null값. 
	 */
	public void openDialogLimitOption(final int listType, final LimitOption limitOption, final TextView modiView){

		String title = null;
		final String[] items;;
		
		ArrayList<String> limitList = new ArrayList<String>();
		
		switch (listType)
		{
			case LIMIT_LIST : 
				title = "제한 추가";
				limitList.add("시간제한");
				limitList.add("인원제한");
				limitList.add("성별제한");
				limitList.add("연령제한");
				break;

			case TIME_LIMIT_LIST :
				title = "시간제한";
				limitList.add("1시간");
				limitList.add("3시간");
				limitList.add("6시간");
				limitList.add("12시간");
				limitList.add("24시간");
				break;
				
			case PEOPLE_LIMIT_LIST :
				title = "인원제한";
				limitList.add("100명");
				limitList.add("1000명");
				limitList.add("3000명");
				limitList.add("6000명");
				limitList.add("10000명");
				break;
			
			case GENDER_LIMIT_LIST :
				title = "성별제한";
				limitList.add("남성");
				limitList.add("여성");
				break;
			
			case AGE_GROUP_LIMIT_LIST :
				title = "연령제한";
				limitList.add("10대");
				limitList.add("20대");
				limitList.add("30대");
				limitList.add("40대");
				limitList.add("50대");
				limitList.add("60대");
				limitList.add("70대 이상");
				break;
			
			default: break;
		}
		
		items = new String[limitList.size()];
		limitList.toArray(items);
		
		final LinearLayout limitOptionList = (LinearLayout) activity.findViewById(R.id.limitOptionList); // 추가한 제한 목록이 들어갈 전체 영역.
		
		// 설정된 제한을 보여주기 위한 TextView 설정.
		final TextView tvlimit = new TextView(activity);
		LinearLayout.LayoutParams tvParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int pixel = (int) (5 * activity.getResources().getDisplayMetrics().density); // dp -> pixel 변환
		tvParam.setMargins(pixel, 0, pixel, pixel);
		tvlimit.setLayoutParams(tvParam);
		tvlimit.setPadding(pixel, pixel, pixel, pixel);
		
		AlertDialog.Builder limit = new AlertDialog.Builder(activity);
		limit.setTitle(title);
		
		if(listType == AGE_GROUP_LIMIT_LIST){ // 연령층 제한을 선택하였을 경우.
			final boolean itemsChecked[] = {false, false, false, false, false, false, false}; // 연령층 체크박스 기본 세팅값
			
			if(modiView != null){ // 연령 제한을 수정하는 경우. 현재 연령제한이 선택되어 있는  값으로 세팅.
				String[] currentAgeGroup = ((TextView) limitOption.getAgeGroupLimit()).getText().toString().split(",");
				
				for(int i=0; i < currentAgeGroup.length; i++){
					itemsChecked[Integer.parseInt(currentAgeGroup[i].replaceAll("대 이상", "").replaceAll("대", "").replace("0", "")) - 1] = true;
				}
			}
			
			limit.setMultiChoiceItems(items, itemsChecked, new DialogInterface.OnMultiChoiceClickListener() {  
			    @Override
			    public void onClick(DialogInterface dialog, int which, boolean isChecked)
			    {
			    }
			 })
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {  
				@Override  
			    public void onClick(DialogInterface dialog, int item) { // 제한을 걸 연령층을 선택한 다음 최종적인 확인 버튼 클릭.
			    	
			    	String listText = "";
					tvlimit.setBackgroundColor(Color.YELLOW);
					
					for(int i = 0; i < itemsChecked.length; i++){
						if(itemsChecked[i] == true){
							listText += items[i] + ",";
						}
					}
					
					if(!"".equals(listText)){
						listText = listText.substring(0, listText.length() - 1); // 마지막 문자에 ","가 있으면 삭제.
						if(modiView == null){ // 새로운 제한 옵션을 추가하는 경우.
							tvlimit.setText(listText);

							limitOptionList.addView(tvlimit, 0); // 0인자값을 세팅함으로써 가장 좌측에 추가시킴. 1일 경우 가장 끝(우측)에 추가시킴.
							
							limitOption.setAgeGroupLimit(tvlimit); // 최종 등록을 할 경우, limitOption의 값을 확인하여 제한 설정을 등록시킴.
						}else{ //제한옵션을 수정하는 경우, 현재 제한옵을 수정.
							modiView.setText(listText);
						}
						
						tvlimit.setOnClickListener(new View.OnClickListener() 
						{
							@Override
							public void onClick(View view) // 추가된 제한을 클릭 시.
							{
								openDialogForLimitDelModi(limitOptionList, view, limitOption);
							}
						});
					}
			    }  
			 })
			 .setNegativeButton("취소", new DialogInterface.OnClickListener() {  
				@Override
				public void onClick(DialogInterface arg0, int item) {  
				}  
			});
		} else { //연령층제한 이외의 모든 항목 중 하나를 선택하였을 경우.
			
			limit.setItems(items, new DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int item){
					dialog.dismiss();
					
					switch (listType)
					{
						case LIMIT_LIST :
							switch (item)
							{
								case 0 : //시간제한
									if(limitOption.getTimeLimit() == null){ //시간 설정을 하지 않은 상태이면 다음 선택창 보여주기.
										openDialogLimitOption(TIME_LIMIT_LIST, limitOption, null);
					    			}else{
					    				Toast.makeText(activity, "시간제한이 이미 설정되어 있습니다.", Toast.LENGTH_SHORT).show();
					    				dialog.dismiss();
					    			}
									break;
									
								case 1 : //인원제한
									if(limitOption.getPeopleLimit() == null){ //연령 설정을 하지 않은 상태이면 다음 선택창 보여주기.
										openDialogLimitOption(PEOPLE_LIMIT_LIST, limitOption, null);
					    			}else{
					    				Toast.makeText(activity, "인원제한이 이미 설정되어 있습니다.", Toast.LENGTH_SHORT).show();
					    				dialog.dismiss();
					    			}	
									break;
							
								case 2 : //성별제한
									if(limitOption.getGenderLimit() == null){ //성별 설정을 하지 않은 상태이면 다음 선택창 보여주기.
										openDialogLimitOption(GENDER_LIMIT_LIST, limitOption, null);
					    			}else{
					    				Toast.makeText(activity, "성별제한이 이미 설정되어 있습니다.", Toast.LENGTH_SHORT).show();
					    				dialog.dismiss();
					    			}
									break;
									
								case 3 : //연령제한
									if(limitOption.getAgeGroupLimit() == null){ //연령 설정을 하지 않은 상태이면 다음 선택창 보여주기.
										openDialogLimitOption(AGE_GROUP_LIMIT_LIST, limitOption, null);
					    			}else{
					    				Toast.makeText(activity, "연령제한이 이미 설정되어 있습니다.", Toast.LENGTH_SHORT).show();
					    				dialog.dismiss();
					    			}
									break;
									
								default: break;
							}
							
							break;

						default: //하위 제한 목록(2depth)
							
							if(modiView == null){ //새로운 제한 옵션을 추가하는 경우.
								//선택한 제한항목을 표시해 주기 위해 배열에 있는 해당 제한텍스트를 세팅. timeList, peopleList, genderList의 배열수는 최대 5개 이므로 5까지만 비교하여  값을 세팅.
								switch (item)
								{
									case 0 : tvlimit.setText(items[0]); break;
									case 1 : tvlimit.setText(items[1]); break;
									case 2 : tvlimit.setText(items[2]); break;
									case 3 : tvlimit.setText(items[3]); break;
									case 4 : tvlimit.setText(items[4]); break;
									case 5 : tvlimit.setText(items[5]); break;
									default: break;
								}
								limitOptionList.addView(tvlimit, 0); //0인자값을 세팅함으로써 가장 좌측에 추가시킴. 1일 경우 가장 끝(우측)에 추가시킴.
							}else{ //제한 옵션을 수정하는 경우.
								
								switch (item)
								{   //선택한 제한항목을 표시해 주기 위해 배열에 있는 해당 제한텍스트를 세팅. timeList, peopleList, genderList의 배열수는 최대 5개 이므로 5까지만 비교하여  값을 세팅.
									case 0 : modiView.setText(items[0]); break;
									case 1 : modiView.setText(items[1]); break;
									case 2 : modiView.setText(items[2]); break;
									case 3 : modiView.setText(items[3]); break;
									case 4 : modiView.setText(items[4]); break;
									case 5 : modiView.setText(items[5]); break;
									default: break;
								}
							}
							
							if(items[0].equals("1시간")){ //시간제한 일 경우.
								if(modiView == null){ //새로운 제한 옵션을 추가하는 경우.
									tvlimit.setBackgroundColor(Color.GREEN);
									limitOption.setTimeLimit(tvlimit); //시간 제한 View 세팅
								}
							}else if(items[0].equals("100명")){ //인원제한 일 경우.
								if(modiView == null){ //새로운 제한 옵션을 추가하는 경우.
									tvlimit.setBackgroundColor(Color.CYAN);
									limitOption.setPeopleLimit(tvlimit); //인원 제한 View 세팅
								}
							}else if(items[0].equals("남성")){ //성별 제한 일 경우.
								if(modiView == null){ //새로운 제한 옵션을 추가하는 경우.
									tvlimit.setBackgroundColor(Color.MAGENTA);
									limitOption.setGenderLimit(tvlimit); //성별제한 View 세팅
								}
							}
							
							tvlimit.setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View view)
								{
									openDialogForLimitDelModi(limitOptionList, view, limitOption);
								}
							});
							
							break;
					}
		    	}
			});
		}
		limit.show();
	}
	
	
	/**
	 * 현재 설정된 제한옵션에 대한 삭제,수정할 수 있는 alertDialog를 보여준다.
	 */
	public void openDialogForLimitDelModi(final LinearLayout optionList, final View view, final LimitOption limitOption){
		String title = "삭제 수정";
		String positiveStr= "삭제";
		String negativeStr= "변경";
		DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() 
		{
			@SuppressLint("NewApi")
			@Override
			public void onClick(DialogInterface dialog, int which) //삭제 쿨릭 시.
			{
				String limitText = ((TextView) view).getText().toString();
				
				if(limitText.equals("1시간") || limitText.equals("3시간") || limitText.equals("6시간") || limitText.equals("12시간") || limitText.equals("24시간")){ // 시간 제한을 삭제한 경우.
					limitOption.setTimeLimit(null);
				}else if(limitText.equals("100명") || limitText.equals("1000명") || limitText.equals("3000명") || limitText.equals("6000명") || limitText.equals("10000명")){ // 인원 제한을 삭제한 경우.
					limitOption.setPeopleLimit(null);
				}else if(limitText.equals("남성") || limitText.equals("여성")){ // 성별 제한을 삭제한 경우.
					limitOption.setGenderLimit(null);
				}else{ // 연령 제한을 삭제한 경우.
					limitOption.setAgeGroupLimit(null);
				}
				
				optionList.removeView(view);
			}
		};
		DialogInterface.OnClickListener negative = new DialogInterface.OnClickListener( )
		{
			@Override
			public void onClick(DialogInterface dialog, int which) //수정 클릭 시.
			{
				TextView textView = (TextView) view;
				String limitText = textView.getText().toString();
				
				int listType = 0;
				
				if(limitText.equals("1시간") || limitText.equals("3시간") || limitText.equals("6시간") || limitText.equals("12시간") || limitText.equals("24시간")){ // 시간 제한일 경우.
					listType = TIME_LIMIT_LIST;
				}else if(limitText.equals("100명") || limitText.equals("1000명") || limitText.equals("3000명") || limitText.equals("6000명") || limitText.equals("10000명")){ // 인원 제한일 경우.
					listType = PEOPLE_LIMIT_LIST;
				}else if(limitText.equals("남성") || limitText.equals("여성")){ // 성별 제한일 경우.
					listType = GENDER_LIMIT_LIST;
				}else{ // 연령 제한일 경우.
					listType = AGE_GROUP_LIMIT_LIST;
				}
				
				 openDialogLimitOption(listType, limitOption, textView);
			}
		};
		
		AlertDialog.Builder adImage = new AlertDialog.Builder(activity) ;
		adImage.setMessage(title)
		.setCancelable(true)
		.setPositiveButton(positiveStr, positive)
		.setNegativeButton(negativeStr, negative)
		.create()
		.show();
	}
}
