package JavaPremiereDBSQLSecurityApp;

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

public class LoginFrame extends JFrame{
	private JTextField loginField;
	private JPasswordField passwordField;
	private JPanel loginPanel;
	private JButton loginButton;
	private JButton cancelButton;
	private Connection dbConnection;
	
	public LoginFrame(){
		this.setSize(500,500);
		this.setTitle("Premiere Application Login");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//set up the login panel
		loginPanel = setUpPanel();
		this.setLayout(new BorderLayout());
		//add the login panel to the window
		this.add(loginPanel, BorderLayout.CENTER);
		
		//set up the command buttons
		loginButton = new JButton("LOGIN");
		cancelButton = new JButton ("CANCEL");
		
		loginButton.addActionListener(new LoginListener(this));
		
		
		
		cancelButton.addActionListener (new CancelListener(this));
		
		this.add(loginButton,BorderLayout.WEST);
		this.add(cancelButton,BorderLayout.EAST);
		
		this.pack();
		this.setVisible(true);
		
		
	}
	
	private JPanel setUpPanel(){
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new GridLayout(2,2));
		loginPanel.add(new JLabel("SQL Login:"));
		loginField = new JTextField(20);
		loginPanel.add(loginField);
		loginPanel.add(new JLabel("Password:"));
		passwordField = new JPasswordField();
		loginPanel.add(passwordField);
		return loginPanel;
		
	}
	
	
	 

	  
	private class LoginListener implements ActionListener{
		private JFrame parentWindow;

		public LoginListener(JFrame parentWindow){
			 this.parentWindow = parentWindow;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			//retrieve data that user entered on the screen
			String login = loginField.getText();
			//this statement is a security vulnerability
			char[] code = passwordField.getPassword();
			
			
			connectToDatabase(login, code);
			this.parentWindow.dispose();
			
		}
		
		 private void connectToDatabase(String login, char[] code){
			 
			  String password = new String(code);
		
	   	      //testing this by connecting to sql server using SQL Server Authentication on the local machine
			
			// final String DATABASE_URL = "jdbc:sqlserver://192.168.0.106:1433;databaseName=Premiere;integratedSecurity=true";

			final String DATABASE_URL = "jdbc:sqlserver://192.168.1.102:1433;databaseName=Premiere";
	 		  
	 		
	 	      
	 	       try{
	 	    	   
	 	    	   //establish connection to database
	 	    	   //for computer SQL Server 2008
	 	    	   System.out.println("connecting to database");
	 	    	   //for sql server authentication
	 	    	   dbConnection = DriverManager.getConnection(DATABASE_URL,login,password);
	 	    	   
	 	    	   //for windows authentication
	 	    	   //dbConnection =DriverManager.getConnection(DATABASE_URL);
	 	    	   
	 	    	   //to enable transaction processing do not
	 	    	   //automatically commit
	 	    	   dbConnection.setAutoCommit(false);
	 	    	   
	 	    	   
	 	    	   
	 	    	   
	 	    	   //now that we connected to SQL Server and the database, display the main menu window
	 	    	   new MainMenuFrame(dbConnection);
	 	       }
	 	     catch(SQLException sqlException){
		    	  JOptionPane.showMessageDialog(null,"couldn't connect to premiere database ");
		    	   sqlException.printStackTrace();
		    	   
		       }
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
			JOptionPane.showMessageDialog(null, "login cancelled");
			parentWindow.dispose();
			
		}
		
	}
	
	public static void main(String[] args){
		new LoginFrame();
	}
	}  //end LoginInFrame class
	
	


