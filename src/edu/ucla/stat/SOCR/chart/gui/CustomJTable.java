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
package edu.ucla.stat.SOCR.chart.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Paint;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
;

public class CustomJTable extends JTable{
	
		Paint[] rowColor;
		

		public CustomJTable(DefaultTableModel model_results) {
			// TODO Auto-generated constructor stub
			super(model_results);
		}

		public void setColor(Paint[] c, int row_count){
			 rowColor = new Paint[row_count]; 
			 rowColor=c;
		 }
		 
		 public Component prepareRenderer(TableCellRenderer renderer,
	             int rowIndex, int vColIndex) {
			 Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
			 if (rowColor==null)
				 return c;
			 
			 c.setForeground((Color)rowColor[rowIndex]);
			 c.setBackground(Color.lightGray);
			 return c;
		 }
	
}
