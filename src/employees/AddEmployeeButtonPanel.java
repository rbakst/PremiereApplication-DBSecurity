package employees;
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddEmployeeButtonPanel extends JPanel

	{
		//reference to database back end  ---- 
		private Connection dbConnection;
		
		private JButton calcButton;
		private JButton exitButton;
		AddEmployeeFrame parentWindow;
		private AddEmployeePanel employeePanel;
		
		public AddEmployeeButtonPanel(AddEmployeeFrame empFrame, AddEmployeePanel empPanel,Connection dbConnection){
			this.dbConnection = dbConnection;
			
			setLayout(new FlowLayout());
			calcButton = new JButton("Add Employee");
			exitButton = new JButton("Exit");
			//get references to text field on the user interface
			this.employeePanel = empPanel;
			this.parentWindow = empFrame;
			
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
		 
			private String employeeID;
			private String firstName;
			private String lastName;
			
			private String street;
			
			private String city;
			
			private String empState;
			
			private String zipcode;
			
			private String phoneNumber;
			
			private LocalDate birthDate;
			
			private LocalDate hireDate;
			
			private String empTypeDesc;//https://stackoverflow.com/questions/22519982/how-to-populate-data-in-a-jcombobox
			
			private String empLogin;
		       
		      
		       private PreparedStatement insertEmployee;
		       private PreparedStatement getEmpTypeID;
		       
		  public ButtonListener(){
			    hireDate =null;
		  }     
		  public void actionPerformed(ActionEvent e){
			   System.out.println("ready to process the button");
			
		      insertEmployee = null;   //query statement
		      getEmpTypeID = null;
		      ResultSet resultSet = null;   //manages results
		      ResultSet empIDresultSet = null;
		       try{
		        	   insertEmployee = dbConnection.prepareStatement("Insert into Employee (EmpID, FirstName, LastName, Street, City, EmpState, ZipCode, "
		        	   		+ "phoneNumber, birthdate, hiredate, emptypeID, empLogin) VALUES (?,?,?,?,?,?,?,?,?,?,?,? )");
		        	   
		        	   getEmpTypeID = dbConnection.prepareStatement("Select empTypeID from empType where empTypeDesc = ?");
		       }
		       catch(SQLException sqlException){
		    	   sqlException.printStackTrace();
		    	   
		       }
			
		     
			  //collect data from the user interface
		      
		      AddEmployeePanel empInfo = employeePanel;
		     
			  System.out.println("access to employee panel");
			
			  //get orderid that user enter in the JTextField on the order entry form
			  employeeID = empInfo.getEmployeeID().getText();
			 
			  System.out.println("Employee " + employeeID);

			  firstName = empInfo.getFirstName().getText();
			  lastName = empInfo.getLastName().getText();
			  street = empInfo.getStreet().getText();
			  city = empInfo.getCity().getText();
			  empState = empInfo.getEmpState().getText();
			  employeeID = empInfo.getEmployeeID().getText();
			  zipcode = empInfo.getZipcode().getText();
			  phoneNumber = empInfo.getPhoneNumber().getText();
			 
			  String bdate = empInfo.getBirthDate().getText();
			  java.sql.Date sqlBirthDate= null;
			  String[] dateParts = bdate.split("/");
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
				
				birthDate = LocalDate.of(year,month,day);
				//convert LocalDate to sql.Date
				sqlBirthDate =  java.sql.Date.valueOf(birthDate);
				
			  }
			  
			 
				 hireDate = empInfo.getHireDate();
				 java.sql.Date sqlHireDate= java.sql.Date.valueOf(hireDate);
				 
				  empTypeDesc = empInfo.getEmpTypeDesc();
				  try{ 
		    		  Integer empTypeID = null;  
		    	getEmpTypeID.setString(1, empTypeDesc);
		    	empIDresultSet = getEmpTypeID.executeQuery();
		    	if (empIDresultSet.next()){
		    		empTypeID = empIDresultSet.getInt(1);
		    		System.out.println("empType:" + empTypeID);
		    	}
		    		empLogin = empInfo.getEmpLogin().toString();  
		    		
				
				//now we have all the data necessary to add a new order record
				//create Statement for querying database
		    	  
				  insertEmployee.setInt(1, Integer.parseInt(employeeID));
				  insertEmployee.setString(2,firstName);
				  insertEmployee.setString(3,lastName);
				  insertEmployee.setString(4, street);
				  insertEmployee.setString(5, city);
				  insertEmployee.setString(6, empState);
				  insertEmployee.setString(7, zipcode);
				  insertEmployee.setString(8, phoneNumber);
				  insertEmployee.setDate(9, sqlBirthDate);
				  insertEmployee.setDate(10, sqlHireDate);
				  insertEmployee.setInt(11, empTypeID);
				  insertEmployee.setString(12, empLogin);
				  
		    	   //query database
		    	   int result = insertEmployee.executeUpdate();
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
