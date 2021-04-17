package org.vj.tax;

public class Goods {
	private String name;
	private float price;
	private GoodsEnum type;
	
	public Goods(String name, float price, GoodsEnum goodsEnum){
		this.name = name;
		this.setPrice(price);
		
		this.type = goodsEnum;
		
	}
	
	public String toString(){
		return this.name + this.getPrice();
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isSalesTaxable() {
		return !this.type.isExempted();
	}

	public boolean isImportedTaxable() {
		return this.type.isImported();
	}

}

enum GoodsEnum{
	BOOK(true,false),
	MEDICAL(true,false),
	FOOD(true,false),
	OTHERS ( false , false),
	IMPORTED_BOOK(true,true),
	IMPORTED_MEDICAL(true,true),
	IMPORTED_FOOD(true,true),
	IMPORTED_OTHERS(false,true);
	
	private boolean isExempted;
	private boolean isImported;
	
	private GoodsEnum(boolean exempted , boolean imported){
		isExempted = exempted;
		isImported = imported;
	}

	public boolean isImported(){
		return isImported;
	}
	public boolean isExempted(){
		return isExempted;
	}

}