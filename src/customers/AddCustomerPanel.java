package customers;

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddCustomerPanel extends JPanel{

	private JLabel customerIDlbl;
	private JTextField customerID;
	private JLabel custNameLbl;
	private JTextField custName;
	
	private JLabel streetLbl;
	private JTextField street;
	private JLabel cityLbl;
	private JTextField city;
	private JLabel stateLbl;
	private JTextField custState;
	private JLabel zipcodeLbl;
	private JTextField zipcode;
	private JLabel phoneNumberLbl;
	private JTextField phoneNumber;
	private JLabel repNameLbl;
	private JComboBox<String> repName;//https://stackoverflow.com/questions/22519982/how-to-populate-data-in-a-jcombobox
	private JLabel custLoginLbl;
	private JTextField custLogin;
	
	public AddCustomerPanel(Connection dbConnection){
		setLayout(new GridLayout(5,2));
		//set up each text field and associated label
		customerIDlbl = new JLabel("Customer ID: ");
		customerID = new JTextField(5);
		
		custNameLbl = new JLabel("Name: ");
		custName = new JTextField(40);
		
		streetLbl = new JLabel("Street: ");
		street = new JTextField(30);
		cityLbl = new JLabel("City: ");
		city = new JTextField(25);
		stateLbl = new JLabel("State: ");//should be dropdown
		custState = new JTextField(2);
		zipcodeLbl = new JLabel("zipcode: ");
		zipcode = new JTextField(9);
		
		
		repNameLbl = new JLabel("Rep Name: ");
		repName = new JComboBox<String>();
		phoneNumberLbl = new JLabel("Phone Number: ");
		phoneNumber = new JTextField(10);
		
		custLoginLbl = new JLabel("Customer Login: ");
		custLogin = new JTextField(128);
		
		
		//add the objects to the panel
		//the layout manager will arrange them in the panel
		add(customerIDlbl);
		add(customerID);
		add(custNameLbl);
		add(custName);
		
		add(streetLbl);
		add(street);
		add(cityLbl);
		add(city);
		add(stateLbl);
		add(custState);
		add(zipcodeLbl);
		add(zipcode);
		add(phoneNumberLbl);
		add(phoneNumber);
		
		add(repNameLbl);
		
		try{
			PreparedStatement stmt = dbConnection.prepareStatement("Select firstname, lastname from salesrepbasicinfoview");
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next())
			{
				repName.addItem(rs.getString("firstname") + " " + rs.getString("lastname"));
			}
			
			
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		
		add(repName);
		add(custLoginLbl);
		add(custLogin);
		
		
		
	}
	
	/**
	 * To be called when modifying order to populate with database values
	 */
	public void setValues(){
		
	}

	public JTextField getCustID() {
		return customerID;
	}

	public JTextField getCustName() {
		return custName;
	}

	

	public JTextField getStreet() {
		return street;
	}

	public JTextField getCity() {
		return city;
	}

	public JTextField getCustState() {
		return custState;
	}

	public JTextField getZipcode() {
		return zipcode;
	}

	public JTextField getPhoneNumber() {
		return phoneNumber;
	}

	

	public LocalDate getBeginDate() {
		return LocalDate.now();
	}

	public String getRepName() {
		return repName.getSelectedItem().toString();
	}

	public JTextField getCustLogin() {
		return custLogin;
	}
	
	public Double getCustBalance(){
		return 0.0;
	}
	public Double getCreditLimit(){
		return 10000.00;
	}


}
