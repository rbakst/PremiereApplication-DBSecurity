package customers;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddCustomerButtonPanel extends JPanel

	{
		//reference to database back end  ---- 
		private Connection dbConnection;
		
		private JButton calcButton;
		private JButton exitButton;
		AddCustomerFrame parentWindow;
		private AddCustomerPanel customerPanel;
		
		public AddCustomerButtonPanel(AddCustomerFrame custFrame, AddCustomerPanel custPanel,Connection dbConnection){
			this.dbConnection = dbConnection;
			
			setLayout(new FlowLayout());
			calcButton = new JButton("Add Customer");
			exitButton = new JButton("Exit");
			//get references to text field on the user interface
			this.customerPanel = custPanel;
			this.parentWindow = custFrame;
			
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
		 
		  
			private String customerID;
			private String custName;
			
			private String street;
			
			private String city;
			
			private String custState;
			
			private String zipcode;
			
			private String phoneNumber;
			
			
			
			private LocalDate beginDate;
			
			private String repFirstName;//https://stackoverflow.com/questions/22519982/how-to-populate-data-in-a-jcombobox
			private String repLastName;
			private String custLogin;
		    private BigDecimal balance;
		    private BigDecimal creditLimit;
		      
		       private PreparedStatement insertCustomer;
		       private PreparedStatement getRepNum;
		       
		  public ButtonListener(){
			    beginDate =null;
		  }     
		  public void actionPerformed(ActionEvent e){
			   System.out.println("ready to process the button");
			
		      insertCustomer = null;   //query statement
		      getRepNum = null;
		      ResultSet resultSet = null;   //manages results
		      ResultSet repNumResultSet = null;
		       try{
		        	   insertCustomer = dbConnection.prepareStatement("Insert into Customer (Cust_num, cust_name, cust_Street, cust_City, cust_State, cust_Zip, "
		        	   		+ "cust_balance, credit_limit, rep_num, phone,  cust_Login) ValUES (?,?,?,?,?,?,?,?,?,?,? )");
		        	   
		        	   getRepNum = dbConnection.prepareStatement("Select rep_num from salesrepbasicinfoview where firstname = ? and lastname = ?");
		       }
		       catch(SQLException sqlException){
		    	   sqlException.printStackTrace();
		    	   
		       }
			
		     
			  //collect data from the user interface
		      
		      AddCustomerPanel custInfo = customerPanel;
		     
			  System.out.println("access to employee panel");
			
			  //get orderid that user enter in the JTextField on the order entry form
			  customerID = custInfo.getCustID().getText();
			 
			  System.out.println("Customer " + customerID);

			  custName = custInfo.getCustName().getText();
			 street = custInfo.getStreet().getText();
			  city = custInfo.getCity().getText();
			  custState = custInfo.getCustState().getText();
			  zipcode = custInfo.getZipcode().getText();
			  balance = new BigDecimal(custInfo.getCustBalance().toString());
			  creditLimit = new BigDecimal(custInfo.getCreditLimit().toString());
			  
			  phoneNumber = custInfo.getPhoneNumber().getText();
			 
			
			  
				 LocalDate bDate = custInfo.getBeginDate();
				  
					
					//convert LocalDate to sql.Date
					java.sql.Date sqlBeginDate =  java.sql.Date.valueOf(bDate);
					
				  String repName= custInfo.getRepName();
				String[] tokens = repName.split(" ");
			repFirstName = tokens[0];
			repLastName = tokens[1];
				  try{ 
		    		  Integer repNum = null;  
		    	getRepNum.setString(1, repFirstName);
		    	getRepNum.setString(2,  repLastName);
		    	repNumResultSet = getRepNum.executeQuery();
		    	if (repNumResultSet.next()){
		    		repNum = repNumResultSet.getInt(1);
		    		System.out.println("empType:" + repNum);
		    	}
		    		custLogin = custInfo.getCustLogin().toString();  
		    		
				
				//now we have all the data necessary to add a new order record
				//create Statement for querying database
		    	  
				  insertCustomer.setInt(1, Integer.parseInt(customerID));
				  insertCustomer.setString(2,custName);
				  insertCustomer.setString(3, street);
				  insertCustomer.setString(4, city);
				  insertCustomer.setString(5, custState);
				  insertCustomer.setString(6, zipcode);
				  insertCustomer.setBigDecimal(7, balance);
				  insertCustomer.setBigDecimal(8, creditLimit);
					
				  insertCustomer.setInt(9, repNum);
				  insertCustomer.setString(10,  phoneNumber);
				  
				  insertCustomer.setString(11, custLogin);
				  
		    	   //query database
		    	   int result = insertCustomer.executeUpdate();
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
		    	 
				  
			  }
		  }
		  
	 
	

}
