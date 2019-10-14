package dto;

import java.sql.Date;

public class Buy {

	private int buycode;
	private String itemname ;
	private String ctmid;  
	private int count;
	private Date buydate;
	
	public int getBuycode() {
		return buycode;
	}
	public void setBuycode(int buycode) {
		this.buycode = buycode;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public String getCtmid() {
		return ctmid;
	}
	public void setCtmid(String ctmid) {
		this.ctmid = ctmid;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Date getBuydate() {
		return buydate;
	}
	public void setBuydate(Date buydate) {
		this.buydate = buydate;
	}
	
	@Override
	public String toString() {
		return "Buy [buycode=" + buycode + ", itemname=" + itemname + ", ctmid=" + ctmid + ", count=" + count
				+ ", buydate=" + buydate + "]";
	}
	
	
	
	
}
