package sales;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import JavaPremiereDBSQLSecurityApp.*;

public class ViewOrModifyGenericFrame extends JFrame implements WindowListener// ,
																				// TableModelListener
{
	private Connection dbConnection;
	private ResultsTableModelWithPrepStmt tableModel;
	private JTable resultTable;
	private Component type;
	private Roles role;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ViewOrModifyGenericFrame(Connection dbConnection, Integer componentID, Component c, Roles role) {
		// store the reference to the database --- back end
		this.dbConnection = dbConnection;
		type = c;
		this.role = role;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// verify that a database connection exists
		if (this.dbConnection == null) {
			JOptionPane.showMessageDialog(null, "missing database connection --- contact IT");

		} else { // continue with this process

			setTitle("Order");
			// set window size
			setSize(600, 600);
			// Specify an action for the close button.
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setLayout(new BorderLayout());

			String query;
			PreparedStatement ps;
			try {
				switch (type) {
				case ORDER:
					query = "select SalesReporderlineView.order_num, Part_num, part_description, qty_ordered from SalesRepOrdersView"
							+ " inner join SalesReporderlineView "
							+ "on SalesRepOrdersView.order_num = SalesReporderlineView.order_num "
							+ "where SalesRepOrdersView.order_num = ?";
					System.out.println("Order num: " + componentID);
					ps = dbConnection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
					ps.setInt(1, componentID);

					// instead of results table model, want a modifiable table
					// row (listener makes a table modifiable but I don't need a
					// whole table).

					System.out.println("After prepstmt is set");

					// instantiate the model which will automatically fire off
					// the
					// execution of the query in String, query.
					tableModel = new ResultsTableModelWithPrepStmt(this.dbConnection, ps, Component.ORDER, this);
					break;
				case CUSTOMER:
					if (role.equals(Roles.PR_OfficeRole) || role.equals(Roles.PR_AccountantRole))
					{
						/*
					query = "select cust_name, cust_street + ' ' + cust_city + ' '   + cust_state + ' ' + isnull(cust_zip,' ') As [Cust Address],"
							+ "phone,  convert(decimal(8,2),cust_balance ) As [CurrentBalance],"
							+ "convert(decimal(8,2),credit_limit) As [CreditLimit],"
							+ "cust_login, firstname + ' ' + lastname As [Rep Name], e.phoneNumber As [RepPhone]"
							+ " from customer   inner join employee e " + " on customer.rep_num = e.empid";*/
						query = "select * from customer";
					}
					else{//self
						query="select * from customerData";
						
					}
					ps = dbConnection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);

					tableModel = new ResultsTableModelWithPrepStmt(this.dbConnection, ps, Component.CUSTOMER, this);

					break;
				case EMPLOYEE:
					break;
				}
				// tableModel.addTableModelListener(new Listener(dbConnection,
				// tableModel));

				System.out.println("TableModel with listener is set");
				// create JTable based on the tableModel
				// the table holds the data that will be displayed on the screen
				resultTable = new JTable(tableModel);
				// resultTable.getModel().addTableModelListener(this);
				System.out.println("resultstable is set");
				// place components in the window
				// place table in a scrollpane so that user can scroll through
				// the
				// contents of the table
				this.add(new JScrollPane(resultTable), BorderLayout.CENTER);

				// adding a row sorter will allow the user to click on any
				// column heading
				// in order to resort the rows by the data in that column
				final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
				resultTable.setRowSorter(sorter);

				// set window size and make it and its components visible on the
				// screen
				setSize(500, 250);
				setVisible(true);

			} catch (SQLException sqlException) {
				JOptionPane.showMessageDialog(null, sqlException.getMessage());
				tableModel.disconnectFromDatabase();

			}
		}

		// set up layout of the window
		pack();
		setVisible(true);

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		System.out.println("windowClosed");
		tableModel.disconnectFromDatabase();

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("windowClosing");

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("windowDeactivated");

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
	/*
	 * public void changeIt(TableModelEvent e) { // TODO Auto-generated method
	 * stub //resultTable.getModel().removeTableModelListener(this);
	 * tableModel.fireTableChanged(e);
	 * 
	 * int row = e.getFirstRow(); int column = e.getColumn(); TableModel model =
	 * (TableModel)e.getSource(); //tableModel.setValueAt(row,column);
	 * 
	 * //String columnName = model.getColumnName(column); Object data =
	 * model.getValueAt(row, column); System.out.println(data);
	 * 
	 * 
	 * try { PreparedStatement ps = dbConnection.prepareStatement
	 * ("UPDATE salesreporderlineview SET qty_ordered = ?, modified_date = ? WHERE order_num = ? and part_num = ?; "
	 * ); System.out.println("UPDATE salesreporderlineview SET qty_ordered = "+
	 * Integer.parseInt(data.toString())+
	 * ", modified_date = "+Date.valueOf(LocalDate.now()) +
	 * " WHERE order_num = " + Integer.parseInt(model.getValueAt(row,
	 * 0).toString()) + " and part_num = " + model.getValueAt(row,
	 * 1).toString()); ps.setInt(1, Integer.parseInt(data.toString()));
	 * ps.setDate(2, Date.valueOf(LocalDate.now())); ps.setInt(3,
	 * Integer.parseInt(model.getValueAt(row, 0).toString())); ps.setString(4,
	 * (model.getValueAt(row, 1).toString()));
	 * 
	 * ps.executeUpdate(); //resultTable.getModel().addTableModelListener(this);
	 * } catch (SQLException e1) { // TODO Auto-generated catch block
	 * e1.printStackTrace(); }
	 * 
	 * JOptionPane.showMessageDialog(null, "Order modified");
	 * 
	 * }
	 */
}
