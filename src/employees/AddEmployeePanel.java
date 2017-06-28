package employees;

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

public class AddEmployeePanel extends JPanel{

	private JLabel employeeIDlbl;
	private JTextField employeeID;
	private JLabel firstNameLbl;
	private JTextField firstName;
	private JLabel lastNameLbl;
	private JTextField lastName;
	private JLabel streetLbl;
	private JTextField street;
	private JLabel cityLbl;
	private JTextField city;
	private JLabel stateLbl;
	private JTextField empState;
	private JLabel zipcodeLbl;
	private JTextField zipcode;
	private JLabel phoneNumberLbl;
	private JTextField phoneNumber;
	private JLabel birthdateLbl;
	private JTextField birthDate;
	private JLabel empTypeLbl;
	private JComboBox<String> empTypeDesc;//https://stackoverflow.com/questions/22519982/how-to-populate-data-in-a-jcombobox
	private JLabel empLoginLbl;
	private JTextField empLogin;
	
	public AddEmployeePanel(Connection dbConnection){
		setLayout(new GridLayout(5,2));
		//set up each text field and associated label
		employeeIDlbl = new JLabel("Employee ID: ");
		employeeID = new JTextField(5);
		
		firstNameLbl = new JLabel("FirstName: ");
		firstName = new JTextField(20);
		lastNameLbl = new JLabel("LastName: ");
		lastName = new JTextField(40);
		streetLbl = new JLabel("Street: ");
		street = new JTextField(35);
		cityLbl = new JLabel("City: ");
		city = new JTextField(25);
		stateLbl = new JLabel("State: ");//should be dropdown
		empState = new JTextField(2);
		zipcodeLbl = new JLabel("zipcode: ");
		zipcode = new JTextField(9);
		phoneNumberLbl = new JLabel("Phone Number: ");
		phoneNumber = new JTextField(10);
		birthdateLbl = new JLabel("Birth Date: ");
		birthDate = new JTextField(10);
		empTypeLbl = new JLabel("Employee Type: ");
		empTypeDesc = new JComboBox<String>();
		empLoginLbl = new JLabel("Employee Login: ");
		empLogin = new JTextField(128);
		
		
		//add the objects to the panel
		//the layout manager will arrange them in the panel
		add(employeeIDlbl);
		add(employeeID);
		add(firstNameLbl);
		add(firstName);
		add(lastNameLbl);
		add(lastName);
		add(streetLbl);
		add(street);
		add(cityLbl);
		add(city);
		add(stateLbl);
		add(empState);
		add(zipcodeLbl);
		add(zipcode);
		add(phoneNumberLbl);
		add(phoneNumber);
		add(birthdateLbl);
		add(birthDate);
		add(empTypeLbl);
		
		try{
			PreparedStatement stmt = dbConnection.prepareStatement("Select emptypedesc from emptype");
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next())
			{
				empTypeDesc.addItem(rs.getString("emptypedesc"));
				System.out.println(rs.getString("emptypedesc"));
			}
			
			
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		
		add(empTypeDesc);
		add(empLoginLbl);
		add(empLogin);
		
		
		
	}
	
	/**
	 * To be called when modifying order to populate with database values
	 */
	public void setValues(){
		
	}

	public JTextField getEmployeeID() {
		return employeeID;
	}

	public JTextField getFirstName() {
		return firstName;
	}

	public JTextField getLastName() {
		return lastName;
	}

	public JTextField getStreet() {
		return street;
	}

	public JTextField getCity() {
		return city;
	}

	public JTextField getEmpState() {
		return empState;
	}

	public JTextField getZipcode() {
		return zipcode;
	}

	public JTextField getPhoneNumber() {
		return phoneNumber;
	}

	public JTextField getBirthDate() {
		return birthDate;
	}

	public LocalDate getHireDate() {
		return LocalDate.now();
	}

	public String getEmpTypeDesc() {
		return empTypeDesc.getSelectedItem().toString();
	}

	public JTextField getEmpLogin() {
		return empLogin;
	}


}
