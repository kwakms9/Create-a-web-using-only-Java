package task;

import java.sql.Date;

public class BasketVO {
	private String user_id;
	private String goods;
	private int ea;
	private int price;
	
	public String getUser_id() {
		return user_id;
	}
	
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	public String getGoods() {
		return goods;
	}
	
	public void setGoods(String goods) {
		this.goods = goods;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getEa() {
		return ea;
	}
	
	public void setEa(int ea) {
		this.ea = ea;
	}
	
	

	

	
}
