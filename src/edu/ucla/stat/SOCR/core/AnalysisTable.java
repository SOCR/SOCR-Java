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
package edu.ucla.stat.SOCR.core;

import java.io.*;
import java.util.*;
//import javax.
import javax.swing.table.*;
import javax.swing.*;

class AnalysisTable extends JTable {
    String[] columnNames;
    Object[][] data;
    String delimier = "\t"; //when IO with text file, used to separate fields
    
    
    
    public AnalysisTable() {
        int COL = 10;
        int ROW = 30;
        setDefaultColNames(COL);
        data = new String[ROW][COL];
    }
    
   
    
    public AnalysisTable(InputStream input, boolean fline_is_colname, String del) 
            throws IOException {
        delimier = del;
        BufferedReader rder = new BufferedReader(new InputStreamReader(input));
        String line = rder.readLine();
        StringTokenizer tks = new StringTokenizer(line, delimier);
        int colnum = tks.countTokens();
        setDefaultColNames(colnum);
        if ( fline_is_colname) {
            for ( int i = 0; i < colnum; i++) {
                columnNames[i] = tks.nextToken();
            }
        }
        
        ArrayList<String[]> list = new ArrayList<String[]>();
        int r = 0;
        
        for (; (line = rder.readLine()) != null; r++) {
            tks = new StringTokenizer(line, del);
            String[] row = new String[colnum];
            for ( int i = 0; i < colnum; i++) {
                row[i] = tks.nextToken();
            }
            list.add(row);
        }
        data = new String[r][colnum];
        for ( int i = 0; i < r; i++) {
            data[i] = (String[])list.get(i);
        }    
    }
    
    public void save2File(File file) throws IOException {
        PrintWriter output = new PrintWriter(new FileWriter(file));
        writeOneLine(columnNames, output);
        for ( int i = 0; i < data.length; i++) {
            writeOneLine(data[i], output);
        }
        
        output.close();
    }
    
    public void clearData() {
        data = new String[data.length][data[0].length];
    }
    
    public int getColumnCount() {
        //return columnNames.length + 1;
        return columnNames.length;
    }
    
    public String getColumnName(int index) {
        //return (index == 0) ? "Row" : columnNames[index - 1];
        return columnNames[index];
    }
    
    public int getRowCount() { return data.length; }
    
    public Object getValueAt(int row, int col) {
        //if (col == 0) return "row " + (row + 1);
        //return data[row][col -1];
        return data[row][col];
    }
    
    public boolean isCellEditable(int row, int col) {
        return true; //col != 0;
    }
    
    private void setDefaultColNames(int colnum) {
        columnNames = new String[colnum];
        for ( int i = 0; i < colnum; i++) {
            //columnNames[i] = "var " + (i + 1);
            columnNames[i] = "var " + i;
        }
    }
    
    private void writeOneLine(Object[] array, PrintWriter output) {
        for ( int i = 0; i < array.length; i++) {
            output.print(array[i] + delimier);
        }
        output.println();
    }
}