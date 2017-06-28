package sales;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import JavaPremiereDBSQLSecurityApp.ResultsTableModelWithPrepStmt;

public class Listener implements TableModelListener {

	private Connection dbConnection;
	private ResultsTableModelWithPrepStmt table;
	public Listener(Connection dbConn, ResultsTableModelWithPrepStmt rt){
		dbConnection=dbConn;
		table=rt;
	}


	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		//resultTable.getModel().removeTableModelListener(this);
		//tableModel.fireTableChanged(e);
		
		int row = e.getFirstRow();
		int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        //tableModel.setValueAt(row,column);
        
        //String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);
        System.out.println(data);
        
		
        try {
			PreparedStatement ps = dbConnection.prepareStatement
					("UPDATE salesreporderlineview SET qty_ordered = ?, modified_date = ? WHERE order_num = ? and part_num = ?; ");
			System.out.println("UPDATE salesreporderlineview SET qty_ordered = "+ Integer.parseInt(data.toString())+
					", modified_date = "+Date.valueOf(LocalDate.now()) +
					" WHERE order_num = " + Integer.parseInt(model.getValueAt(row, 0).toString()) +
					" and part_num = " + model.getValueAt(row, 1).toString());
			ps.setInt(1, Integer.parseInt(data.toString()));
			ps.setDate(2, Date.valueOf(LocalDate.now()));
			ps.setInt(3, Integer.parseInt(model.getValueAt(row, 0).toString()));
			ps.setString(4, (model.getValueAt(row, 1).toString()));
			
			ps.executeUpdate();
			//resultTable.getModel().addTableModelListener(this);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        JOptionPane.showMessageDialog(null, "Order modified");
		
	}

}
