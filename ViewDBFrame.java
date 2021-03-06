package dbmskursSSGdesktop;



import java.util.Properties;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;



/**
 * @author SSG
 *
 */
public class ViewDBFrame extends JFrame {
	
	

	 /**
	 * 
	 */
	   private static final long serialVersionUID = 1L;
	   public static final int DEFAULT_WIDTH = 300;
	   public static final int DEFAULT_HEIGHT = 200;
	   private JButton previousButton;
	   private JButton nextButton;
	   private DataPanel dataPanel;
	   private Component scrollPane;
	   private JComboBox tableNames;
	   private Connection conn;
	   private Statement stat;
	   private DatabaseMetaData meta;
	   private ResultSet rs;
	   private boolean scrolling;	
	

	



	   public ViewDBFrame()
	   {
	      setTitle("ViewDB");
	      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	      tableNames = new JComboBox();
	      tableNames.addActionListener(new
	         ActionListener()
	         {
	            public void actionPerformed(ActionEvent event)
	            {
	               showTable((String) tableNames.getSelectedItem());
	            }
	         });
	      add(tableNames, BorderLayout.NORTH);
	       try
	      {
	         conn = getConnection();
	         meta = conn.getMetaData();
	         createStatement();
	         getTableNames();
	      }
	      catch (SQLException e)
	      {
	         JOptionPane.showMessageDialog(this, e);
	      }
	      catch (IOException e)
	      {
	         JOptionPane.showMessageDialog(this, e);
	      }
	      JPanel buttonPanel = new JPanel();
	      add(buttonPanel, BorderLayout.SOUTH);
	       if (scrolling)
	      {
	         previousButton = new JButton("Previous");
	         previousButton.addActionListener(new
	            ActionListener()
	            {
	               public void actionPerformed(ActionEvent event)
	               {
	                  showPreviousRow();
	               }
	            });
	         buttonPanel.add(previousButton);
	      }
	      nextButton = new JButton("Next");
	      nextButton.addActionListener(new
	         ActionListener()
	         {
	            public void actionPerformed(ActionEvent event)
	            {
	               showNextRow();
	            }
	         });
	      buttonPanel.add(nextButton);
	      
         /*	      
	      
	      addWindowListener(new WindowAdapter(){
	            public void windowClosing(WindowEvent event)
	            {
	               try
	               {
	                  if (conn != null) conn.close();
	               }
	               catch (SQLException e)
	               {
	                  while (e != null)
	                  {
	                     e.printStackTrace();
	                     e = e.getNextException();
	                  }
	               }
	            }
	        });
	      
	      */
	      
	   }
	   /**
	      Creates the statement object used for executing queries.
	      If the database supports scrolling cursors, the statement
	      is created to yield them.
	   */
	   public void createStatement() throws SQLException
	   {
	      if (meta.supportsResultSetType(
	         ResultSet.TYPE_SCROLL_INSENSITIVE))
	      {
	         stat = conn.createStatement(
	            ResultSet.TYPE_SCROLL_INSENSITIVE,
	            ResultSet.CONCUR_READ_ONLY);
	         scrolling = true;
	      }
	      else
	      {
	         stat = conn.createStatement();
	         scrolling = false;
	      }
	   }
	    /**
	      Gets all table names of this database and adds them
	      to the combo box.
	   */
	   public void getTableNames() throws SQLException
	   {
	      ResultSet mrs = meta.getTables(null, null, null, new String[] { "TABLE" });
	      while (mrs.next())
	         tableNames.addItem(mrs.getString(3));
	      mrs.close();
	   }
	   /**
	      Prepares the text fields for showing a new table, and
	      shows the first row.
	      @param tableName the name of the table to display
	   */
	   public void showTable(String tableName)
	   {
	      try
	      {
	         if (rs != null) rs.close();
	         rs = stat.executeQuery("SELECT * FROM " + tableName);
	         if (scrollPane != null)
	            remove(scrollPane);
	         dataPanel = new DataPanel(rs);
	         scrollPane = new JScrollPane(dataPanel);
	         add(scrollPane, BorderLayout.CENTER);
	         validate();
	         showNextRow();
	      }
	      catch (SQLException e)
	      {
	         JOptionPane.showMessageDialog(this, e);
	      }
	   }
	   /**
	      Moves to the previous table row.
	   */
	   public void showPreviousRow()
	   {
	      try
	      {
	         if (rs == null || rs.isFirst()) return;
	         rs.previous();
	         dataPanel.showRow(rs);
	      }
	      catch (SQLException e)
	      {
	         JOptionPane.showMessageDialog(this, e);
	      }
	   }
	   /**
	      Moves to the next table row.
	   */
	   public void showNextRow()
	   {
	      try
	      {
	         if (rs == null || scrolling && rs.isLast()) return;
	         if (!rs.next() && !scrolling)
	         {
	            rs.close();
	            rs = null;
	            return;
	         }
	         dataPanel.showRow(rs);
	      }
	      catch (SQLException e)
	      {
	         JOptionPane.showMessageDialog(this, e);
	      }
	   }
	   /**
	      Gets a connection from the properties specified
	      in the file database.properties
	      @return the database connection
	   */
	   public static Connection getConnection()
	      throws SQLException, IOException
	   {
	      Properties _props = new Properties();
	      FileInputStream in
	         = new FileInputStream("database.properties");
	      
	      
	      
	      _props.load(in);
	      in.close();
	      String drivers = _props.getProperty("jdbc.drivers");
	      if (drivers != null) System.setProperty("jdbc.drivers", drivers);
	      String url = _props.getProperty("jdbc.url");
	      String username = _props.getProperty("jdbc.username");
	      String password = _props.getProperty("jdbc.password");
	      return DriverManager.getConnection(url, username, password);
	   }


	
	
	
	
	
	
	

}
