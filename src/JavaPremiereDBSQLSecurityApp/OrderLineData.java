package JavaPremiereDBSQLSecurityApp;

import java.sql.Date;
import java.time.LocalDate;

public class OrderLineData {
  private String partID;
  private int qty;
  private double price;
  
  public OrderLineData( String pID, int q, double p){
	  partID = pID;
	  qty = q;
	  price = p;
	  
  }
    
public String getPartID() {
	return partID;
}

public int getQty() {
	return qty;
}

public double getPrice() {
	return price;
}

public Date getModifiedDate() {
	return Date.valueOf(LocalDate.now());
}


  
}