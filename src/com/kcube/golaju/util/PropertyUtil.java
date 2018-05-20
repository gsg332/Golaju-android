package com.kcube.golaju.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.AssetManager.AssetInputStream;
import android.content.res.Resources;

public class PropertyUtil {
	
	public static Properties pros;

	
	public PropertyUtil(Activity act) {
		
		Resources resources = act.getResources();
		AssetManager assetManager = resources.getAssets();
    	
    	try
		{	/*
			// 한글이 없을 경우 아래 한줄의 코드를 사용. 한글이 있으면 한글이 깨짐.
    		InputStream inputStream = assetManager.open("golaju.properties");
    		*/
    		
    		// 한글이 깨짐이 경우 아래 두줄의 코드를 사용. EUC-KR문서일 경우 EUC-KR로 세팅?
			AssetInputStream ais = (AssetInputStream) assetManager.open("golaju.properties"); // 개발 테스트용은 golaju.dev.properties로 변경. 실제 운영은 golaju.properties파일로 변경. 
	        BufferedReader br = new BufferedReader(new InputStreamReader(ais, "UTF-8"));
			
	    	pros = new Properties();
	    	pros.load(br);
	    	System.out.println("properties: " + pros);

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
    }  

	
    /**
     * 키(key)로 값(value)을 가져온다. 
     * @param key
     * @return Property의 value를 가져온다.
     * @throws IOException
     */
    static public String getProperty(String key) throws IOException {  
        return pros.getProperty(key);  
    }  
    
}  
