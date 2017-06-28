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

public class ModifyOrderButtonPanel extends JPanel{
	//reference to database back end  ---- 
	private Connection dbConnection;
	
	private JButton calcButton;
	private JButton exitButton;
	private OrderPanel orderPanel;
	private OrderLine orderLine;
	private ModifyOrderFrame parentWindow;
	public ModifyOrderButtonPanel(ModifyOrderFrame parentWindow, OrderPanel orderPanel,OrderLine orderLine,Connection dbConnection){
		this.dbConnection = dbConnection;
		
		setLayout(new FlowLayout());
		calcButton = new JButton("Update Order");
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
	       private PreparedStatement updateOrder;
	       private PreparedStatement updateOrderLine;
	       
	  public ButtonListener(){
		    orderDate =null;
	  }     
	  public void actionPerformed(ActionEvent e){
		   System.out.println("ready to process the button");
		
	      updateOrder = null;   //query statement
	      updateOrderLine = null;
	      ResultSet resultSet = null;   //manages results
	       try{
	    	   
	    	
	    	   updateOrder = dbConnection.prepareStatement("Insert into Orders (Order_Num, Order_Date,Cust_Num) "+
	    	        "VALUES (?,?,? )");
	           updateOrderLine = dbConnection.prepareStatement("Insert into Order_Line (Order_Num,Part_Num,QTY_Ordered,Quoted_Price,Modified_date)" +
	        		"VALUES (?,?,?,?)");   
	       }
	       catch(SQLException sqlException){//is this like not having rights to this customer? Why do we check some here but some at database?
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
			  updateOrder.setString(1, orderID);
			  
			  updateOrder.setDate(2,sqlDate);
			  updateOrder.setString(3,custID);
	    	  
	    	   //query database
	    	   int result = updateOrder.executeUpdate();
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
	    			  
	    			   updateOrderLine.setString(1, orderID);
	    		       updateOrderLine.setString(2,rowData.getPartID());
	    		       updateOrderLine.setInt(3, rowData.getQty());
	    		       updateOrderLine.setDouble(4, rowData.getPrice());
	    		       updateOrderLine.setDate(5, Date.valueOf(LocalDate.now()));
	    		       //now execute the statement to insert into database
	    		       int result = updateOrderLine.executeUpdate();
	    	               
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
