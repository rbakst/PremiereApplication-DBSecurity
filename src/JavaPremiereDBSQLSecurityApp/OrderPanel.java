package JavaPremiereDBSQLSecurityApp;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OrderPanel extends JPanel{
	private JLabel orderIDlbl;
	private JTextField orderID;
	private JLabel datelbl;
	private JTextField date;
	private JLabel custIDlbl;
	private JTextField custID;
	
	public OrderPanel(){
		setLayout(new GridLayout(3,2));
		//set up each text field and associated label
		orderIDlbl = new JLabel("Order ID: ");
		orderID = new JTextField(5);
		datelbl = new JLabel("Date: ");
		date = new JTextField(10);
		custIDlbl = new JLabel("Customer ID:");
		custID = new JTextField(3);
		//add the objects to the panel
		//the layout manager will arrange them in the panel
		add(orderIDlbl);
		add(orderID);
		add(datelbl);
		add(date);
		add(custIDlbl);
		add(custID);
		
	}
	
	/**
	 * To be called when modifying order to populate with database values
	 */
	public void setValues(){
		
	}

	public JTextField getOrderID() {
		return orderID;
	}

	
	public JTextField getDate() {
		return date;
	}

	

	public JTextField getCustID() {
		return custID;
	}

	
}
