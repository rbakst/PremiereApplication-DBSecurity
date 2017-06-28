package JavaPremiereDBSQLSecurityApp;

import java.awt.BorderLayout;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ModifyOrderFrame extends JFrame{
	   private final int WINDOW_WIDTH = 800;
	   private final int WINDOW_HEIGHT = 800;
       private OrderPanel orderPanel;
       private OrderLine orderLinePanel;
       private ModifyOrderButtonPanel buttonPanel;
       private Connection dbConnection;
       
	   public ModifyOrderFrame(Connection dbConnection){
		   //store the reference to the database --- back end
		   this.dbConnection = dbConnection;
		   
		   //verify that a databse connection exists
		   if (this.dbConnection == null){
			   JOptionPane.showMessageDialog(null,"missing database connection --- contact IT");
			   
		   }
		   else{ //continue with this process
			   
			   
	 
			   	setTitle("Premiere Modify Order Form");
			   	//set window size
			   	setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

			   	// Specify an action for the close button.
			   	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			   	//set up layout of the window 
			   	setLayout(new BorderLayout());
    	
			   	orderPanel = new OrderPanel();
			   	add(orderPanel,BorderLayout.WEST);
			   	orderLinePanel = new OrderLine();
			   	add(orderLinePanel,BorderLayout.CENTER);
			   	buttonPanel = new ModifyOrderButtonPanel(this, orderPanel,orderLinePanel,dbConnection);
			   	add(buttonPanel,BorderLayout.SOUTH);
			   	pack();
			   	setVisible(true);
			   	System.out.println(this.getClass());
		   }
    	
    }

	public OrderPanel getOrderPanel() {
		return orderPanel;
	}

	

	public OrderLine getOrderLinePanel() {
		return orderLinePanel;
	}

	
	   
}
