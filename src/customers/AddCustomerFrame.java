package customers;

import java.awt.BorderLayout;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AddCustomerFrame extends JFrame{
	

	   private final int WINDOW_WIDTH = 800;
	   private final int WINDOW_HEIGHT = 800;
    private AddCustomerPanel customerPanel;
   // private OrderLine orderLinePanel;
    private AddCustomerButtonPanel buttonPanel;
    private Connection dbConnection;
    
	public AddCustomerFrame(Connection dbConnection){
		
		
			   
				   //store the reference to the database --- back end
				   this.dbConnection = dbConnection;
				   
				   //verify that a databse connection exists
				   if (this.dbConnection == null){
					   JOptionPane.showMessageDialog(null,"missing database connection --- contact IT");
					   
				   }
				   else{ //continue with this process
					   
					   
			 
					   	setTitle("Premiere Customer Entry Form");
					   	//set window size
					   	setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

					   	// Specify an action for the close button.
					   	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					   	//set up layout of the window 
					   	setLayout(new BorderLayout());
		    	
					   	customerPanel = new AddCustomerPanel(dbConnection);
					   	add(customerPanel,BorderLayout.CENTER);
					   //	orderLinePanel = new OrderLine();
					   //	add(orderLinePanel,BorderLayout.CENTER);
					   	buttonPanel = new AddCustomerButtonPanel(this, customerPanel, dbConnection);
					   	add(buttonPanel,BorderLayout.SOUTH);
					   	pack();
					   	setVisible(true);
					   	System.out.println(this.getClass());
				   }
		    	
		    }

			public AddCustomerPanel getCustomerPanel() {
				return customerPanel;
			}

			

			

			
			   
		

	}


