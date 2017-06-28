package JavaPremiereDBSQLSecurityApp;
import javax.security.auth.*;
import javax.security.auth.login.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class OldMainMenuFrame extends JFrame implements WindowListener{
	
	private JMenuBar premiereMenuBar;
	
	private JMenu salesMenu, employeeMenu,customerMenu,purchaseMenu;
	private JMenuItem addSalesItem,modifySalesItem, cancelSalesItem,viewSalesItem,viewSalesOrdersItem;
	private JMenuItem employeeMenuItem;
	private JMenuItem customerMenuItem;
	private JMenuItem purchasesMenuItem;
	private Connection dbConnection;
	
	public OldMainMenuFrame(Connection dbConnection){
		
		this.setSize(700,700);
		this.setTitle("Premiere Application");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//first display LoginFrame
		
		this.dbConnection = dbConnection;
		setUpMenuBar();
		this.setJMenuBar(premiereMenuBar);   //add menu bar to the window
		pack();
		this.setVisible(true);
	}

    
     
     private void setUpMenuBar(){
    	 //set up the menu bar
    	 this.premiereMenuBar = new JMenuBar();
    	 //set up and add the main menus to the menu bar
    	 this.salesMenu = new JMenu("Sales");
    	 premiereMenuBar.add(salesMenu);
    	 
    	 //set up each menu item and add the item to the menu on the menu bar
    	 this.addSalesItem = new JMenuItem("Add Sales Order");
    	 this.modifySalesItem = new JMenuItem("Modify Sales Order");
    	 this.cancelSalesItem = new JMenuItem("Cancel Sales Order");
    	 
    	 this.salesMenu.add(addSalesItem);
    	 this.salesMenu.add(modifySalesItem);
    	 this.salesMenu.add(cancelSalesItem);
    	 this.salesMenu.addSeparator();   //add separate between modify operations and list operations
    	 this.viewSalesItem = new JMenuItem ("View Sales Order");
    	 this.viewSalesOrdersItem = new JMenuItem("View All Sales Orders");
    	 
    	 this.salesMenu.add(viewSalesItem);
    	 this.salesMenu.add(viewSalesOrdersItem);
    	 
    	 //connect each menu item to the appropriate action listener
    	 this.addSalesItem.addActionListener(new AddSalesOrderListener());
    	 
    	 //code must still be implemented
    	 
    	 this.modifySalesItem.addActionListener(new ModifySalesListener());
    	 //this.cancelSalesItem.addActionListener(new CancelSalesListener());
     }
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowClosed(WindowEvent e) {
		System.exit(0);   //window close , exit the application
	}


	@Override
	public void windowClosing(WindowEvent e) {
		try{
			dbConnection.close();  //close connection to database
			dispose(); //close and dispose of this window
		}
		catch(SQLException sqlexception){
			JOptionPane.showMessageDialog(null, "couldn't close connection to database");
		}
		
	}


	@Override
	public void windowDeactivated(WindowEvent e) {
		
		
	}


	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
    private class AddSalesOrderListener implements ActionListener{
    	

		@Override
		public void actionPerformed(ActionEvent arg0) {
			/*
			 * USER_NAME() - database username - a.k.a USER and CURRENT_USER

             * SUSER_SNAME() - login identification name - supersedes SUSER_NAME() 
			 */
			
			
			
			//verify that the user that is logged in has permission to view this part of the system
			boolean hasPermissions = false;
			
			
			 String currentLogin;
			//set up a Statement that can query the sql server
			Statement sqlStatement =null;
			
			//set up the query to get the SQL Server login if using SQL Server authentication
			String query =
					"select suser_sname()";   
			//now create an instance of Statement
			//and use it to execute the query
			try{
				sqlStatement = dbConnection.createStatement();
				//execute the query
				ResultSet rs = sqlStatement.executeQuery(query);
				//move to first record in the resultset
				rs.next();
				//retrieve data from first column in the resultset
				//there should be only one row and one column
				 currentLogin = rs.getString(1);
				 
				 
				 System.out.println("current login " + currentLogin);
				
				  //now get the user and the role(s) associated with the
				 //user that is associated with this sql server login
				 query =
					
				
				"select u.name, r.name " +
				"from [PREMIERE].sys.database_role_members as m " +
				"inner join [PREMIERE].sys.database_principals r "+
				"on m.role_principal_id = r.principal_id "+
				"inner join [PREMIERE].sys.database_principals as u "+
				"on u.principal_id = m.member_principal_id "+
				"where u.name = "+
				//access sql logins instead of windows logins
				"(select u.name from [PREMIERE].sys.sql_logins  l "+
				"inner join [PREMIERE].sys.sysusers  u "+
				"on l.sid = u.sid "+
				"where l.name = "+ "'" + currentLogin + "')";
				   
						
				//now execute this query
				rs = sqlStatement.executeQuery(query);
				//now check the results that came back
				if (!rs.next()){
					hasPermissions = false;
					System.out.println("no permission data retrieved");
				}
				else{
				    //iterate through the resultset
					do{	
					//get the data from the first row
					String currentUser	=	
							rs.getString(1);
				    String currentDBRole =
				    		rs.getString(2);
				    if (currentDBRole == null){
				    	hasPermissions = false;
				    }
				    else {
				    	System.out.println("current user" + currentUser + " current role" + currentDBRole);
				    	if (currentDBRole.equalsIgnoreCase("PR_SalesRole")){
				    		hasPermissions =true;
				    		break;   //no need to iterate further , the user is associated with the required role
				    	}
				    }
				}  while (rs.next());  //go to next row
				}
			}catch(SQLException ex){
				JOptionPane.showMessageDialog(null, "couldnt retrieve data");
				ex.printStackTrace();
			}
			
			//if (hasPermissions)
			{
				new OrderEntry(dbConnection);
			}
			/*else
			{
				JOptionPane.showMessageDialog(null, "access denied ... contact IT");
			}*/
			
		}
    	
    }
    
    private class ModifySalesListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			boolean hasPermissions = false;
			
			String currentLogin;
			Statement sqlStatement;
			
			String query = "select suser_sname()";
			
			try{
				sqlStatement = dbConnection.createStatement();
				ResultSet rs = sqlStatement.executeQuery(query);
				rs.next();
				currentLogin = rs.getString(1);
				
				System.out.println("Current Login: " + currentLogin);
				
				query = "select u.name, r.name " +
				"from Premiere.sys.database_role_members as m " +
						"inner join Premiere.sys.database_principals r " +
				"on m.role_principal_id = r.principal_id "+
						"inner join Premiere.sys.database_principals as u "+
				"on u.principal_id = m.member_principal_id "+
						"where u.name = " +
				"(select u.name from Premiere.sys.sql_logins l "+
						"inner join Premiere.sys.sysusers u "+
				"on l.sid = u.sid "+
						"where l.name = "+"'"+currentLogin + "')";
				
				rs = sqlStatement.executeQuery(query);
				if(!rs.next()){
					hasPermissions = false;
					System.out.println("No permission data retrieved");
				}
					else
					{
						do{
							String currUser = rs.getString(1);
							String currDBRole = rs.getString(2);
							
							if(currDBRole == null)
							{
								hasPermissions = false;
							}
							else{
								
								System.out.println("Current User: "+currUser+" Current Role: " + currDBRole);
								if(currDBRole.equalsIgnoreCase("PR_SalesRole"))
										{
									hasPermissions=true;
									break;
										}
							}
						}while(rs.next());
					}
				}catch(SQLException ex){
					JOptionPane.showMessageDialog(null, "Couldn't retrieve data");
					ex.printStackTrace();
					}
			if (hasPermissions){
				//open modify entry form
				//new OrderEntry(dbConnection);
			}
			else{
				JOptionPane.showMessageDialog(null, "access denied ... contact IT");
			}
			}
		}
}