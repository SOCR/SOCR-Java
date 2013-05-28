/****************************************************
Statistics Online Computational Resource (SOCR)
http://www.StatisticsResource.org
 
All SOCR programs, materials, tools and resources are developed by and freely disseminated to the entire community.
Users may revise, extend, redistribute, modify under the terms of the Lesser GNU General Public License
as published by the Open Source Initiative http://opensource.org/licenses/. All efforts should be made to develop and distribute
factually correct, useful, portable and extensible resource all available in all digital formats for free over the Internet.
 
SOCR resources are distributed in the hope that they will be useful, but without
any warranty; without any explicit, implicit or implied warranty for merchantability or
fitness for a particular purpose. See the GNU Lesser General Public License for
more details see http://opensource.org/licenses/lgpl-license.php.
 
http://www.SOCR.ucla.edu
http://wiki.stat.ucla.edu/socr
 It s Online, Therefore, It Exists! 
****************************************************/

package edu.ucla.stat.SOCR.experiments.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LookAndFeel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import edu.ucla.stat.SOCR.util.EditableHeader;
import edu.ucla.stat.SOCR.util.JScrollPaneAdjuster;
import edu.ucla.stat.SOCR.util.JTableRowHeaderResizer;
import edu.ucla.stat.SOCR.util.RowHeaderRenderer;

public class SOCRJTablePanel  extends JPanel implements KeyListener{
	//protected JTable dataTable;
	protected JTable headerTable;
	protected TableColumnModel columnModel;
	protected DefaultTableModel tModel;
	protected DefaultTableModel hModel;
	protected Object [][] dataObject;
	protected Object [][] headerDataObject;
	protected int columnNumber 	= 10;
	protected int rowNumber 		= 10;
	protected String [] columnNames;
	protected final String DEFAULT_HEADER 		= "C";
	protected boolean animating = false;
	
	
	private DefaultTableCellRenderer allOtherValuesRenderer = new DefaultTableCellRenderer();
	   
	private JPanel jPanel1 = new JPanel();
	
	protected EditableHeader dTableHeader;
	
	private JTable dataTable = new JTable() {
    	public TableCellRenderer getCellRenderer(int row, int column) {
              TableCellRenderer renderer = getCellRendererProper(row, column);

              if (renderer instanceof JComponent) {
                  updateBackground((JComponent) renderer);
              }

              return renderer;
          }

          private TableCellRenderer getCellRendererProper(int row, int column) {
         /*     if ((row == 0) && (column == attributeColumn())) {
                  return allOtherValuesRenderer;
              }*/

              TableCellRenderer renderer = super.getCellRenderer(row, column);

              if (renderer instanceof JLabel) {
                  ((JLabel) renderer).setHorizontalAlignment(JLabel.LEFT);
              }

              return renderer;
          }
      };
      
	protected int[] listIndex;  // for mapping: 1,2,3
	
	  public SOCRJTablePanel() {
		  dataObject = new Object[rowNumber][columnNumber];
		  columnNames = new String[columnNumber];
		  for (int i = 0; i < columnNumber; i++)
				columnNames[i] = new String(DEFAULT_HEADER+(i+1));
		  initTable();
	  }
	  
	  public SOCRJTablePanel(DefaultTableModel tModel) {
		   
		    initTable(tModel);
	   
	  }
	  public SOCRJTablePanel(Object[][] data, String[] columns) {
		    
		 dataObject = data;
		 columnNames = columns;
		 initTable();
		   // tModel = new javax.swing.table.DefaultTableModel(dataObject, columnNames);
		    
		    //dataTable = new JTable(tModel);
		
		    }
	  
	  public int getColumnCount(){
		  return dataTable.getColumnCount();
	  }
	  public void setValueAt(Object aValue, int row, int column){
		  dataTable.setValueAt(aValue, row, column);
	  }
	  
	  public Object getValueAt(int row, int column){
		 return dataTable.getValueAt(row, column);
	  }
	  
	  public String[] getRow(int rowIndex){
		  
		  String[] row = new String[getColumnCount()];
		  
		  for (int j= 0; j<getColumnCount(); j++)
			  row[j] =(String) dataTable.getValueAt(rowIndex, j);
		  
		  return row;
		  
	  }
	  
	  public double[] getRowValue(int rowIndex){
		  
		  double[] row = new double[getColumnCount()];
		  
		  for (int j= 0; j<getColumnCount(); j++){
			 // System.out.println("*"+dataTable.getValueAt(rowIndex, j)+"*");
			 // row[j] =(double)(Double.valueOf(dataTable.getValueAt(rowIndex, j).toString()));
			  row[j] =Double.parseDouble(dataTable.getValueAt(rowIndex, j).toString());
	  }
		  return row;
		  
	  }
	  public TableColumnModel getColumnModel(){
		  return dataTable.getColumnModel();
	  }
	  
	  public int getSelectedRow(){
		  return dataTable.getSelectedRow();
	  }
	  
	  public int getSelectedColumn(){
		  return dataTable.getSelectedColumn();
	  }
	  
	  public int getSelectedHeaderColumn(){
		  return dTableHeader.getEditingColumn();
	  }
	  
	  public void updateEditableHeader(TableColumnModel  aColumnModel){
		  dTableHeader = new EditableHeader(aColumnModel);
			/*for (int i=0; i<aColumnModel.getColumnCount(); i++){
				System.out.println("H "+aColumnModel.getColumn(i).getHeaderValue());
			}*/
			dTableHeader.setEditingColumn(-1);
			dataTable.setTableHeader(dTableHeader);
			dataTable.validate();		
			
			this.removeAll();
			
			JTableHeader corner = headerTable.getTableHeader();
			corner.setReorderingAllowed(false);
			corner.setResizingAllowed(false);
			JScrollPane tablePanel = new JScrollPane(dataTable);
	    	tablePanel.setRowHeaderView(headerTable);
	    	tablePanel.setCorner(JScrollPane.UPPER_LEFT_CORNER, corner);
	    	new JScrollPaneAdjuster(tablePanel);
	    	new JTableRowHeaderResizer(tablePanel).setEnabled(true); 
			this.add(tablePanel);
		
			this.validate();
			//System.out.println("table Header updated");
		}
	  
	  
	  private void updateBackground(JComponent component) {
	        component.setBackground(animatingTable()
	            ? Color.white : jPanel1.getBackground());
	    }
	  protected void initTable(){
		
			tModel = new DefaultTableModel(dataObject, columnNames);
			
			initTable(tModel);
	  }
	  
	  protected boolean animatingTable(){
		  return animating;
	  }
	  protected void initTable(DefaultTableModel t){
		  	tModel = t;
		
			dataTable = new JTable(tModel);
		// this is a fix for the BAD SGI Java VM - not up to date as of dec. 22, 2003
			try { 
				dataTable.setDragEnabled(true); 
			} catch (Exception e) { 
			}
			columnModel = dataTable.getColumnModel();
			
			dataTable.addKeyListener(this);
			dataTable.setGridColor(Color.gray);
			dataTable.setShowGrid(true);
			dataTable.setTableHeader(new EditableHeader(columnModel));
			dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			dataTable.setCellSelectionEnabled(true);
			dataTable.setColumnSelectionAllowed(true);
			dataTable.setRowSelectionAllowed(true);
			
			columnModel = dataTable.getColumnModel();
			dTableHeader =new EditableHeader(columnModel);
			dataTable.setTableHeader(dTableHeader);

			hookTableAction();
			
			addHeaderTable();
		}
	  
	  private void addHeaderTable(){
		  removeAll();
		hModel = new DefaultTableModel(0, 1);
		
		for (int i = 0; i < dataTable.getModel().getRowCount(); i++)
			hModel.addRow(new Object[] { (i+1)+":" } );
			
		
		headerTable = new JTable(hModel);
		headerTable.setCellSelectionEnabled(false);
		headerTable.setEnabled(false);
		headerTable.setGridColor(Color.gray);
		headerTable.setShowGrid(true);
		LookAndFeel.installColorsAndFont
	      (headerTable, "TableHeader.background", 
	      "TableHeader.foreground", "TableHeader.font");
			headerTable.setIntercellSpacing(new Dimension(0, 0));
	      Dimension d = headerTable.getPreferredScrollableViewportSize();
	      d.width = headerTable.getPreferredSize().width;
	      headerTable.setPreferredScrollableViewportSize(d);
	      headerTable.setRowHeight(dataTable.getRowHeight());
	      headerTable.setDefaultRenderer(Object.class, new RowHeaderRenderer());
			
	      JTableHeader corner = headerTable.getTableHeader();
	      corner.setReorderingAllowed(false);
	      corner.setResizingAllowed(false);
	      
	      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	      JScrollPane tablePanel = new JScrollPane(dataTable);
	      tablePanel.setRowHeaderView(headerTable);
	      tablePanel.setCorner(JScrollPane.UPPER_LEFT_CORNER, corner);
	    	
	      new JScrollPaneAdjuster(tablePanel);

	      new JTableRowHeaderResizer(tablePanel).setEnabled(true); 
	      add(tablePanel);
	      validate();
	  }
	  
	  protected void hookTableAction() {
			
			//Tab--add column
			String actionName = "selectNextColumnCell";
			final Action tabAction = dataTable.getActionMap().get(actionName);
			Action myAction = new AbstractAction() {
					public void actionPerformed(ActionEvent e) {
						//if (dataTable.isEditing() && isLastCell()) {
						if (isLastCell()) {
							//resetTableColumns(dataTable.getColumnCount()+1);
							appendTableColumns(1);
						}
						else tabAction.actionPerformed(e);
					}
				};
			dataTable.getActionMap().put(actionName, myAction);

			//Enter--append row
			String actionName2 = "selectNextRowCell";
			final Action enterAction = dataTable.getActionMap().get(actionName2);

			Action myAction2 = new AbstractAction() {
					public void actionPerformed(ActionEvent e) {
						//if (dataTable.isEditing() && isLastCell()) {
						if (isLastCell()) {
							appendTableRows(1);
						}
						else enterAction.actionPerformed(e);
					}
				};
			
			dataTable.getActionMap().put(actionName2, myAction2);
			
			
		/*	
			String actionName3 = "deleteSelectedData";
			final Action delAction = dataTable.getActionMap().get(actionName3);

			Action myAction3 = new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					if ((dataTable.getSelectedRow() >= 0) && (dataTable.getSelectedColumn() >= 0)){
						int[] rows=dataTable.getSelectedRows();
						int[] cols=dataTable.getSelectedColumns(); 
						for (int i=rows[0]; i<cols[rows.length-1]; i++)
						 for (int j = cols[0]; j<cols[cols.length-1]; j++){
							 dataTable.setValueAt("", i, j);
						}
					}else delAction.actionPerformed(e);
						
				}
			};
			dataTable.getActionMap().put("delete", myAction3);*/

	}
	private	 boolean isLastCell()
	{
	    int rows = dataTable.getRowCount();
	    int cols = dataTable.getColumnCount();
	    int selectedRow = dataTable.getSelectedRow();
	    int selectedCol = dataTable.getSelectedColumn();

	    if((rows == (selectedRow+1)) && (cols == (selectedCol+1)))
	        return true;
	    else
	        return false;
	}
	  public void appendTableColumns(int n) {
			 // System.out.println("appending column="+n);

				   columnModel = dataTable.getColumnModel();
			       String[] savedHeaders= new String[columnModel.getColumnCount()];
			       for(int i=0; i<columnModel.getColumnCount(); i++){ 
			    	//   System.out.println("header"+i+"is "+columnModel.getColumn(i).getHeaderValue());
			    	   savedHeaders[i] = (String)columnModel.getColumn(i).getHeaderValue();
			       }
			       
			       int ct = dataTable.getColumnCount();
			       tModel =  (DefaultTableModel) dataTable.getModel();
			       for(int j=0;j<n;j++) {
			    	   tModel.addColumn(DEFAULT_HEADER+(ct+j+1),new java.util.Vector(ct)); 
			    	 //  System.out.println("adding header:"+(DEFAULT_HEADER+(ct+j+1)));
			       }
			         
			      
			       dataTable.setModel(tModel);
			       columnModel = dataTable.getColumnModel();	
			       
			       for (int i=0; i< savedHeaders.length; i++)
			    	   columnModel.getColumn(i).setHeaderValue(savedHeaders[i]);
			       
			   /* 	   
			       for(int i=0; i<columnModel.getColumnCount(); i++){ 
			    	   System.out.println("2header"+i+"is "+columnModel.getColumn(i).getHeaderValue());
			       }*/
			       dataTable.setTableHeader(new EditableHeader(columnModel));
			    
			       int[] listIndex2 = new int[dataTable.getColumnCount()];
			        for(int j=0;j<listIndex.length;j++)
			            listIndex2[j]= listIndex[j];
			        for(int j=listIndex.length;j<listIndex2.length;j++)
			        	listIndex2[j]=1;
			        
			        listIndex = new int[dataTable.getColumnCount()];
			        for(int j=0;j<listIndex2.length;j++)
			        	listIndex[j]= listIndex2[j];
			   }
			
	  public int[] getListIndex(){
	        return listIndex;
	  }
	  
	  public void setTable(JTable table){
		 
		  rowNumber = table.getRowCount();
		  columnNumber = table.getColumnCount();
		
		 // System.out.println("setting table dataRowCount="+ rowNumber+", "+columnNumber);
		  
		  dataObject = new Object[rowNumber][columnNumber];
		  for (int i = 0; i < rowNumber; i++)
			  for (int j = 0; j < columnNumber; j++)
				  dataObject[i][j] = table.getValueAt(i, j);
		  
		  columnNames = new String[columnNumber];
		  for (int i = 0; i < columnNumber; i++)
				columnNames[i] = table.getColumnName(i);
		  
		  initTable();
	  }
	  
	  public String getColumnNameAt(int column){
		return columnNames[column];  
	  }
	  
	  public void resetTable(){

			//	System.out.println("resetTable get called");
				for (int i = 0; i < columnNumber; i++)
					columnNames[i] = new String(DEFAULT_HEADER+(i+1));

				tModel = new javax.swing.table.DefaultTableModel(dataObject, columnNames);
				hModel = new DefaultTableModel(0, 1);
				
				for (int i = 0; i < tModel.getRowCount(); i++)
		            hModel.addRow(new Object[] { (i+1)+":" } );
				
				dataTable = new JTable(tModel);
				dataTable.setGridColor(Color.gray);
				dataTable.setShowGrid(true);
				dataTable.doLayout();
				dataTable.setCellSelectionEnabled(true);
				dataTable.setColumnSelectionAllowed(true);
				dataTable.setRowSelectionAllowed(true);
		         
				columnModel = dataTable.getColumnModel();
				dataTable.setTableHeader(new EditableHeader(columnModel));
				
				listIndex = new int[dataTable.getColumnCount()];
		        for(int j=0;j<listIndex.length;j++)
		            listIndex[j]=1;
				
				hookTableAction();

				headerTable = new JTable(hModel);
				headerTable.setCellSelectionEnabled(false);
				LookAndFeel.installColorsAndFont
		        (headerTable, "TableHeader.background", 
		        "TableHeader.foreground", "TableHeader.font");
				headerTable.setIntercellSpacing(new Dimension(0, 0));
		        Dimension d = headerTable.getPreferredScrollableViewportSize();
		        d.width = headerTable.getPreferredSize().width;
		        headerTable.setPreferredScrollableViewportSize(d);
		        headerTable.setRowHeight(dataTable.getRowHeight());
		        headerTable.setDefaultRenderer(Object.class, new RowHeaderRenderer());		
		        JTableHeader corner = headerTable.getTableHeader();
		        corner.setReorderingAllowed(false);
		        corner.setResizingAllowed(false);
		        
		        
				//		summaryPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/2));
				

				this.removeAll();
				JScrollPane tablePanel = new JScrollPane(dataTable);
		    	tablePanel.setRowHeaderView(headerTable);
		    	tablePanel.setCorner(JScrollPane.UPPER_LEFT_CORNER, corner);
		    	new JScrollPaneAdjuster(tablePanel);
		    	new JTableRowHeaderResizer(tablePanel).setEnabled(true); 
				
		    	add(tablePanel);
				validate();
				 
			}
	  public void resetTableRows(int n) {
			//	System.out.println("Chart resetTableRows:" +n);
		        tModel = (javax.swing.table.DefaultTableModel) dataTable.getModel();
				tModel.setRowCount(n);
		        dataTable.setModel(tModel);
		        
		       
		        hModel = (javax.swing.table.DefaultTableModel) headerTable.getModel();
		        int b= hModel.getRowCount();
				hModel.setRowCount(n);
				headerTable.setModel(hModel);
				for (int i = b; i < hModel.getRowCount(); i++)
		            hModel.setValueAt((i+1)+":",i,0);    
		    }

		// reset mapping also
			public void resetTableColumns(int n) {
			//	System.out.println("resetTableColumns get Called " +n);
			
				tModel = (javax.swing.table.DefaultTableModel) dataTable.getModel();	      
				tModel.setColumnCount(n);
		        dataTable.setModel(tModel);  
		       
				listIndex = new int[dataTable.getColumnCount()];
		        for(int j=0;j<listIndex.length;j++)
		            listIndex[j]=1;
		      
		    }
			
	  
	 public void appendTableRows(int n) {
	        int ct = dataTable.getColumnCount();
	        tModel = (javax.swing.table.DefaultTableModel) dataTable.getModel();
	        
	        for(int j=0;j<n;j++) 
	        	tModel.addRow(new java.util.Vector(ct));  
	        dataTable.setModel(tModel);
	        columnModel = dataTable.getColumnModel();
	        dataTable.setTableHeader(new EditableHeader(columnModel));
	      /*  for(int i=0; i<columnModel.getColumnCount(); i++)
		    	   System.out.println("appendrow_header"+i+"is "+columnModel.getColumn(i).getHeaderValue());
	      */
	        hModel = (javax.swing.table.DefaultTableModel) headerTable.getModel();
	        int rowCount= hModel.getRowCount();
	        for(int j=0;j<n;j++) {
             hModel.addRow(new Object[] {(rowCount+j+1)+":"});  
	        }
	        headerTable.setModel(hModel);
	    }
	 
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public int getRowCount(){
		return dataTable.getRowCount();
	}
}