package sales;





import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import JavaPremiereDBSQLSecurityApp.Component;
import JavaPremiereDBSQLSecurityApp.Roles;
import customers.ModifyCustomerFrame;

public class ChooseIDToModifyForm extends JFrame{

	private JTextField IDField;
	private JPanel choosePanel;
	private JButton submitButton;
	private JButton cancelButton;
	private Connection dbConnection;
	private Component type;
	private Roles role;
	public ChooseIDToModifyForm(Connection dbConnection, Component c, Roles r){
		this.setSize(500,500);
		this.setTitle("Premiere Application - ID To Modify");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.dbConnection = dbConnection;
		type = c;
		role=r;
		//set up the login panel
		choosePanel = setUpPanel();
		this.setLayout(new BorderLayout());
		//add the login panel to the window
		this.add(choosePanel, BorderLayout.CENTER);
		
		//set up the command buttons
		submitButton = new JButton("SUBMIT");
		cancelButton = new JButton ("CANCEL");
		
		submitButton.addActionListener(new SubmitListener(this));
		
		
		
		cancelButton.addActionListener (new CancelListener(this));
		
		this.add(submitButton,BorderLayout.WEST);
		this.add(cancelButton,BorderLayout.EAST);
		
		this.pack();
		this.setVisible(true);
		
		
	}
	
	private JPanel setUpPanel(){
		
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2,2));
		p.add(new JLabel("ID:"));
		IDField = new JTextField(20);
		p.add(IDField);
		return p;
		
	}
	
	
	

	  
	private class SubmitListener implements ActionListener{
		private JFrame parentWindow;

		public SubmitListener(JFrame parentWindow){
			 this.parentWindow = parentWindow;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			//retrieve data that user entered on the screen
			
			switch(type){
			case CUSTOMER:
				int custID = Integer.parseInt(IDField.getText());
				new ModifyCustomerFrame(dbConnection);
				break;
			case ORDER:
				int orderID = Integer.parseInt(IDField.getText());
				
				new ViewOrModifyGenericFrame(dbConnection, orderID, Component.ORDER,  role);
				
				break;
			case EMPLOYEE:
				break;
			
			
			}
			this.parentWindow.dispose();
		}
		
		
	}   //end LoginListener class
	
	private class CancelListener implements ActionListener{
		private JFrame parentWindow;
		public CancelListener(JFrame parentWindow){
			this.parentWindow = parentWindow;
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			//close the login window
			JOptionPane.showMessageDialog(null, "action cancelled");
			parentWindow.dispose();
			
		}
		
	}
	
	}  //end LoginInFrame class
	
	



