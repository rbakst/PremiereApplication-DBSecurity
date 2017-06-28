package JavaPremiereDBSQLSecurityApp;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class OrderEntryButtonPanel extends JPanel{
	//reference to database back end  ---- 
	private Connection dbConnection;
	
	private JButton calcButton;
	private JButton exitButton;
	private OrderPanel orderPanel;
	private OrderLine orderLine;
	private OrderEntry  parentWindow;
	public OrderEntryButtonPanel(OrderEntry parentWindow, OrderPanel orderPanel,OrderLine orderLine,Connection dbConnection){
		this.dbConnection = dbConnection;
		
		setLayout(new FlowLayout());
		calcButton = new JButton("Insert Order");
		exitButton = new JButton("Exit");
		//get references to text field on the user interface
		this.orderPanel = orderPanel;
		this.orderLine = orderLine;
		this.parentWindow = parentWindow;
		
		add(calcButton);
		add(exitButton);
		System.out.println(this.getClass());
		
		calcButton.addActionListener(new ButtonListener());
		exitButton.addActionListener(new ExitListener());
		
	}
  private class ExitListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		parentWindow.dispose();
		
	}
	  
  }	
  private class ButtonListener implements ActionListener{
	       private String orderID;
	       private String custID;
	       private LocalDate orderDate;
	       private String partID;
	       private int qty;
	       private double price;
	       private double total;
	       
	       //private Date orderDate;
	       private PreparedStatement insertOrder;
	       private PreparedStatement insertOrderLine;
	       
	  public ButtonListener(){
		    orderDate =null;
	  }     
	  public void actionPerformed(ActionEvent e){
		   System.out.println("ready to process the button");
		
	      insertOrder = null;   //query statement
	      insertOrderLine = null;
	      ResultSet resultSet = null;   //manages results
	       try{
	    	   
	    	
	    	   insertOrder = dbConnection.prepareStatement("Insert into Orders (Order_Num, Order_Date,Cust_Num) "+
	    	        "VALUES (?,?,? )");
	           insertOrderLine = dbConnection.prepareStatement("Insert into Order_Line (Order_Num,Part_Num,QTY_Ordered,Quoted_Price,modified_date)" +
	        		"VALUES (?,?,?,?,?)");   
	       }
	       catch(SQLException sqlException){
	    	   sqlException.printStackTrace();
	    	   
	       }
		
	     
		  //collect data from the user interface
	      
	      OrderPanel orderInfo = orderPanel;
	      //data about parts that were ordered
	      OrderLine orderLineInfo = orderLine;
	      
		  System.out.println("access to order panel");
		  String orderNum;
		  //get orderid that user enter in the JTextField on the order entry form
		  orderID = orderInfo.getOrderID().getText();
		 
		  System.out.println("Order " + orderID);
		  //get date that user entered on order entry form
		  String date = orderInfo.getDate().getText();
		  java.sql.Date sqlDate= null;
		  String[] dateParts = date.split("/");
		  if (dateParts.length !=3){
			  //user made an error entering the date
			  JOptionPane.showMessageDialog(null,"incorrect date format");
			  return;
		  }
		  else
		  { int month;
		    int day;
		    int year;
			month = Integer.parseInt(dateParts[0]);
			day = Integer.parseInt(dateParts[1]);
			year = Integer.parseInt(dateParts[2]);
			System.out.println(month + " " + day + " " + year);
			
			orderDate = LocalDate.of(year,month,day);
			//convert LocalDate to sql.Date
			sqlDate =  java.sql.Date.valueOf(orderDate);
			
		
			
			
			//get custid from order entry form
			custID = orderInfo.getCustID().getText();
			
			//now we have all the data necessary to add a new order record
			//create Statement for querying database
	    	  try{ 
			  insertOrder.setString(1, orderID);
			  
			  insertOrder.setDate(2,sqlDate);
			  insertOrder.setString(3,custID);
	    	  
	    	   //query database
	    	   int result = insertOrder.executeUpdate();
	    	   System.out.println("executed insert statement");
	    	   
	    	   
	    	  }
	    	  catch(SQLException sqlExcept){
	    		  sqlExcept.printStackTrace();
	    		  try{
	   	    	   dbConnection.rollback();}
	   	    	   catch(SQLException sqlEx){
	   	    		   sqlEx.printStackTrace();
	   	    		   
	   	    	   }
	   	    	   
	    		  
	    	  }
	    	  //now proceed to insert parts of the order
	    	  try{
	    		  System.out.println("enter part order to order line table ");
	    		 //loop through orderline information
	    		  for (int i=0;i<orderLineInfo.partData.length;i++) //number of rows in the grid
	    		   //retrieve data from one row at a time
	    		  { 
	    			  OrderLineData rowData = orderLineInfo.getOrderLineData(i);
	    			   //now prepare record to insert 
	    			  if (rowData == null ){
	    				  if (i==0){
	    				     System.out.println("details not provided, rollback the order");
	    				     try{
	    					     dbConnection.rollback();
	    					     return; //end this method
	    				     }
	    				     catch(SQLException ex){
	    					    ex.printStackTrace();
	    				     }
	    			     }
	    			     else { //no more details, commit the order
	    				  break;  //leave the loop
	    			      }
	    			  }
	    			  else{  //insert the details
	    			  System.out.println("part order " +
	    					  orderID + " " +
		    				  rowData.getPartID() + " " +
		    				  rowData.getQty() + " " +
		    				  rowData.getPrice());
	    			  
	    			   insertOrderLine.setString(1, orderID);
	    		       insertOrderLine.setString(2,rowData.getPartID());
	    		       insertOrderLine.setInt(3, rowData.getQty());
	    		       insertOrderLine.setDouble(4, rowData.getPrice());
	    		       insertOrderLine.setDate(5, rowData.getModifiedDate());
	    		       //now execute the statement to insert into database
	    		       int result = insertOrderLine.executeUpdate();
	    	               
	    		   }
	    		  }
	    	  }
	    	 
	    	  catch(SQLException ex){
	    		  ex.printStackTrace();
	    		  try{
	    		  dbConnection.rollback();}
	    		  catch(SQLException sqlE){
	    			  sqlE.printStackTrace();
	    			  
	    		  }
	    	  }
	    	  try{
	    	  dbConnection.commit();}
	    	  catch(SQLException except){
	    		  except.printStackTrace();
	    		  try{
	    			  dbConnection.rollback();
	    			  
	    		  }
	    		  catch(SQLException sqle){
	    			  sqle.printStackTrace();
	    			  
	    		  }
	    	  }
			  
		  }
	  }
	  
  }
}
