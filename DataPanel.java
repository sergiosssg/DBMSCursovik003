package dbmskursSSGdesktop;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author SSG
 *
 */
class DataPanel extends JPanel {
	
	
private ArrayList<JTextField> fields;
	
	
	

	   /**
    Constructs the data panel.
    @param rs the result set whose contents this panel displays
 */
 public DataPanel(ResultSet rs) throws SQLException
 {
    fields = new ArrayList<JTextField>();
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    ResultSetMetaData rsmd = rs.getMetaData();
    for (int i = 1; i <= rsmd.getColumnCount(); i++)
    {
       gbc.gridy = i - 1;
       String columnName = rsmd.getColumnLabel(i);
       gbc.gridx = 0;
       gbc.anchor = GridBagConstraints.EAST;
       add(new JLabel(columnName), gbc);
       int columnWidth = rsmd.getColumnDisplaySize(i);
       JTextField tb = new JTextField(columnWidth);
       fields.add(tb);
       gbc.gridx = 1;
       gbc.anchor = GridBagConstraints.WEST;
       add(tb, gbc);
    }
 }
 /**
    Shows a database row by populating all text fields
    with the column values.
 */
 public void showRow(ResultSet rs) throws SQLException
 {
    for (int i = 1; i <= fields.size(); i++)
    {
       String field = rs.getString(i);
       JTextField tb = (JTextField) fields.get(i - 1);
       tb.setText(field);
    }
 }
 



	
	

}

