package JavaPremiereDBSQLSecurityApp;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class ResultsTableModelWithPrepStmt extends AbstractTableModel {
	Connection dbConnection;
	private PreparedStatement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;
	private JFrame parent;
	private Component type;

	public ResultsTableModelWithPrepStmt(Connection dbConnection, PreparedStatement ps, Component c, JFrame frame)
			throws SQLException {
		this.dbConnection = dbConnection;
		parent = frame;
		type = c;
		// table.getModel.addTableModelListener(this);
		// this query is read only, user can view data but can't modify any of
		// the data
		this.statement = ps;
		// set query and execute it
		setQuery();
		// resultSet = ps.executeQuery();
		// fireTableStructureChanged();
		/*
		 * while (resultSet.next()) { this. }
		 */
	}

	public void setQuery() throws SQLException, IllegalStateException {
		if (dbConnection == null) {
			throw new IllegalStateException("Not connected to database");
		}
		resultSet = statement.executeQuery();
		metaData = resultSet.getMetaData();

		// determine number of rows in the resultset
		resultSet.last(); // move to last row
		numberOfRows = resultSet.getRow();

		// notify JTable that model has changed
		fireTableStructureChanged();

	}

	@Override
	public int getColumnCount() throws IllegalStateException {

		if (dbConnection == null) {
			throw new IllegalStateException("not connected to the database");
		}

		try {
			return metaData.getColumnCount();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}

		return 0; // problem occurred, return 0
	}

	@Override
	public int getRowCount() throws IllegalStateException {
		if (dbConnection == null) {
			throw new IllegalStateException("not connected to database");
		}
		return numberOfRows;

	}

	// this method will be invoked when the JTable is set up so that the
	// column headings appear in the table
	public String getColumnName(int column) throws IllegalStateException {
		if (dbConnection == null) {
			throw new IllegalStateException("not connected to database");
		}
		try {
			return metaData.getColumnName(column + 1);
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		return null; // problem occurred , nothing to return
	}

	// returns the data type of a particular column, including this method makes
	// retrieval of
	// numeric columns more efficient.
	public Class getColumnClass(int column) throws IllegalStateException {
		if (dbConnection == null) {
			throw new IllegalStateException();
		}
		try {
			String className = metaData.getColumnClassName(column + 1);
			return Class.forName(className);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return Object.class; // if problem occurred
	}

	@Override
	public Object getValueAt(int row, int column) throws IllegalStateException {
		if (dbConnection == null) {
			throw new IllegalStateException("not connected to database");
		}
		try {
			resultSet.absolute(row + 1); // resultset rows and columns start
											// from 1
			return resultSet.getObject(column + 1);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null; // if problem
	}

	public void disconnectFromDatabase() {

		try {
			resultSet.close();
			statement.close();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}

	public boolean isCellEditable(int row, int col) {

		if (col != 3)
			return false;
		return true;

	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		System.out.println("value: " + value + "\nrow: " + row + "\ncol: " + col);

		/*
		 * try { resultSet.getRowId(row); resultSet.updateObject(col, value); }
		 * catch (SQLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		PreparedStatement ps = null;
		try {

			switch (type) {

			case ORDER:
				ps = dbConnection.prepareStatement(
						"UPDATE salesreporderlineview SET qty_ordered = ?, modified_date = ? WHERE order_num = ? and part_num = ?; ");
//change
				//ps.setInt(1, Integer.parseInt(getValueAt(row,3).toString()));
				
				ps.setInt(1, Integer.parseInt(value.toString()));
				ps.setDate(2, Date.valueOf(LocalDate.now()));
				ps.setInt(3, Integer.parseInt(getValueAt(row, 0).toString()));
				ps.setString(4, (getValueAt(row, 1).toString()));

				

				//ResultSet r = dbConnection.createStatement()
					//	.executeQuery("select * from salesreporderlineview where order_num = 21610");
				//while (r.next()) {
					//System.out.println("Q:" + r.getString(4));
				//}
				break;
			case CUSTOMER:
//can't update all fields, only ones with permissions
				ps=dbConnection.prepareStatement("Update customer set cust_street = ?, cust_city = ?, cust_state = ?, cust_zip = ?,  "
						+ "credit_limit = ?, rep_num = ? where cust_num = ?");
				ps.setString(1	, getValueAt(row, 2).toString());
				ps.setString(2, getValueAt(row,3).toString());
				ps.setString(3, getValueAt(row,4).toString());
				ps.setString(4,  getValueAt(row, 5).toString());
				ps.setString(5, getValueAt(row, 7).toString());
				ps.setInt(6, Integer.parseInt(getValueAt(row,8).toString()));
				ps.setInt(7, Integer.parseInt(getValueAt(row, 0).toString()));
			
				break;
			case EMPLOYEE:
				break;
			}
			int result = ps.executeUpdate();
			System.out.println("PS result: " + result);
			ps.close();
			
			//disconnectFromDatabase();

			//parent.dispose();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				dbConnection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		try {
			dbConnection.commit();
			JOptionPane.showMessageDialog(null, "modified");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * fireTableDataChanged();
		 * 
		 * fireTableCellUpdated(row, col);
		 */
	}

}