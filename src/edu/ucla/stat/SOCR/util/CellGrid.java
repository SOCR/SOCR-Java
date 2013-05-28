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
import java.awt.*;
import javax.swing.*;

/**This class models the cell grid used in the Coupon Collector Experiment*/
public class CellGrid extends JComponent{
	private int cells;
	private static Font font = new Font("SansSerif", Font.PLAIN, 10);
	private int[] cellCount;

	/**This general constructor: creates a new cell grid with a specified number of
	cells*/
	public CellGrid(int m){
		if (m < 1) m = 1;
		cells = m;
		cellCount = new int[cells];
	}

	/**This method paints the cell grid*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		int n = (int) Math.ceil(Math.sqrt(cells)),
			x, y, rows, bigColumns, width, height, cellWidth, cellHeight,
			labelWidth, labelHeight;
		String label;
		rows = cells / n + 1;
		bigColumns = cells % n;
		//draw grid
		width = getSize().width;
		height = getSize().height;
		cellWidth = width / n;
		cellHeight = height / n;
		width = n * cellWidth;
		g.setColor(Color.blue);
		y = 0;
		for(int i = 0; i < rows; i++){
			g.drawLine(0, y, width, y);
			y = y + cellHeight;
		}
		if (bigColumns > 0){
			y = rows * cellHeight;
			x = bigColumns * cellWidth;
			g.drawLine(0, y, x, y);
		}
		x = 0;
		for(int i = 0; i <= n; i++){
			if(i <= bigColumns) y = rows * cellHeight;
			else y = (rows - 1) * cellHeight;
			g.drawLine(x, 0, x, y);
			x = x + cellWidth;
		}
		//draw numbers
		labelHeight = g.getFontMetrics(font).getAscent();
		g.setColor(Color.red);
		for (int i = 0; i < cells; i++){
			if(cellCount[i] > 0){
				y = (i / n) * cellHeight + cellHeight / 2 + labelHeight / 2;
				x = (i % n) * cellWidth + cellWidth / 2;
				label = String.valueOf(cellCount[i]);
				labelWidth = g.getFontMetrics(font).stringWidth(label);
 				g.drawString(label, x - labelWidth / 2, y);
			}
		}
	}

	/**This method sets the counts for the cells*/
	public void setCellCount(int[] cellCount){
		this.cellCount = cellCount;
	}

	/**This method resets the cell grid*/
	public void reset(){
		cellCount = new int[cells];
		repaint();
	}

	/**This mtheod sets the number of cells*/
	public void setCells(int m){
		cells = m;
		reset();
	}
}



