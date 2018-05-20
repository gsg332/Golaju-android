package com.kucbe.golaju.common;

public class Item
{
	private int iconID;//아이콘 리소스 아이디 저장(이미지)
    private String itemName;//텍스트뷰에 보여질 목록명
    
    public Item() {
    }
    
    public Item(int iconID, String itemName){
        this.iconID=iconID;
        this.itemName=itemName;
    }
    
    public int getIconID() {
        return iconID;
    }
    public void setIconID(int iconID) {
        this.iconID = iconID;
    }
    
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }    
}
