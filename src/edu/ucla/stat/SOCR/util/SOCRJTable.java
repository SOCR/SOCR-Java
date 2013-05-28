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
package edu.ucla.stat.SOCR.util;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import edu.ucla.stat.SOCR.util.*;


public class SOCRJTable extends JTable implements KeyListener{
//private JTable dataTable;
private DefaultTableModel tModel;

 public SOCRJTable(DefaultTableModel tModel) {
    
    super(tModel);
   // tModel = new javax.swing.table.DefaultTableModel(dataObject, columnNames);
    
    //dataTable = new JTable(tModel);
    setDragEnabled(true);
    setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    setCellSelectionEnabled(true);
    setColumnSelectionAllowed(true);
    setRowSelectionAllowed(true);
    
    this.addKeyListener(this); 
    
             
          
  // this.addFocu
    
    }
    
    public SOCRJTable(Object[][] dataObject, String[] columnNames) {
    
    super(new javax.swing.table.DefaultTableModel(dataObject, columnNames));
   // tModel = new javax.swing.table.DefaultTableModel(dataObject, columnNames);
    
    //dataTable = new JTable(tModel);
    setDragEnabled(true);
    setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    setCellSelectionEnabled(true);
    setColumnSelectionAllowed(true);
    setRowSelectionAllowed(true);
    
    this.addKeyListener(this); 
    
             
          
  // this.addFocu
    
    }
    
    

    public void insertTableRows(int n,int rw) {
        int cl= getSelectedColumn();
           if(getColumnCount()==2) {  
               
                   tModel = (javax.swing.table.DefaultTableModel) getModel();
                    for(int j=0;j<n;j++)
                   tModel.insertRow(rw,new java.util.Vector(2));
                setModel(tModel);
                
            } else {
           
                   tModel = (javax.swing.table.DefaultTableModel) getModel();
                    for(int j=0;j<n;j++) 
                   tModel.insertRow(rw,new java.util.Vector(1));
                   setModel(tModel);
                
            }
        
        }
        
        public void appendTableRows(int n) {
        int cl= getSelectedColumn();
            if(getColumnCount()==2) {  
                   tModel = (javax.swing.table.DefaultTableModel) getModel();
                   for(int j=0;j<n;j++)
                   tModel.addRow(new java.util.Vector(2));
                   setModel(tModel);
            } else {
               
                   tModel = (javax.swing.table.DefaultTableModel) getModel();
                   for(int j=0;j<n;j++) 
                   tModel.addRow(new java.util.Vector(1));  
                   setModel(tModel);

            }
        }
        
       /* public void addColumn(int n) {
        int crt = this.getColumnCount();
        
            
            
        }
        */
        
        
        public void clearData(int c1, int c2, int r1, int r2) {
          //  clears data between columns c1 to c2 and rows r1 to r2
            if(isEditing())
               getCellEditor().stopCellEditing();
           
            for(int j=c1;j<c2;j++) {
            
                for(int i =r1; i<r2; i++) {
                setValueAt("", i, j);
           
                }

            }
        }
        
         public void clearTable() {
          //  clears data between columns c1 to c2 and rows r1 to r2
            if(isEditing())
               getCellEditor().stopCellEditing();
           
            for(int j=0;j< getColumnCount()   ;j++) {
            
                for(int i =0; i< getRowCount() ; i++) {
                setValueAt("", i, j);
           
                }

            }
        }
         
         
         public float[] getTableVal(int clmNo) {
       
            float[] datatemp = new float[getRowCount()];
                       if(isEditing())
                       getCellEditor().stopCellEditing();
                       int count = 0;
                       for(int i=0;i<getRowCount();i++) {
                            if(getValueAt(i,clmNo) != null) {
                                  // && dataTable.getValueAt(i,1).toString() != ""
                               if(getValueAt(i,clmNo).toString().trim().equals("")  ) {
                               break;
                               
                               }else{
                               try{
                               datatemp[count] = Float.valueOf(getValueAt(i,clmNo).toString()).floatValue();
                               count++;
                               } catch(Exception e)
                               {OKDialog OKD = new OKDialog(null, true, "Warning.Data is not numeric. Fit Curve might not work.");
                                OKD.setVisible(true);
                                return null;}
                               
                               
                               
                             }
                        } else {
                        break;
                        }
                            

                        } //END:for-loop
                  
            
           if(count == 0)
               return new float[0];
           float[] dataVec = new float[count];
                  // ydata = new float[count];
           System.arraycopy(datatemp,0, dataVec, 0, count); 
           return dataVec;

        }

         public void keyPressed(KeyEvent evt) {
        int rw = getSelectedRow();
        if(evt.getKeyCode() == evt.VK_ENTER)
        insertTableRows(1,rw);
         }
         
         public void keyReleased(KeyEvent evt) {
         }
         
         public void keyTyped(KeyEvent evt) {
         }
         
         
         
         
         
}
