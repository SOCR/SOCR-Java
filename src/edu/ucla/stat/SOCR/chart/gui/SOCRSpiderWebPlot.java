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

import java.awt.Shape;
import java.util.Iterator;
import java.util.List;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;


import edu.ucla.stat.SOCR.chart.data.*;

import org.jfree.chart.labels.*;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.data.category.CategoryDataset;

public class SOCRSpiderWebPlot extends SpiderWebPlot{

	Summary s =null;

	public SOCRSpiderWebPlot(CategoryDataset dataset) {
		super(dataset);
}

/**	      * Returns a collection of legend items for the radar chart.
 * 	      * @return The legend items.
 */
public LegendItemCollection getLegendItems() {
	LegendItemCollection result = new LegendItemCollection();

	CategoryDataset dataset = super.getDataset();

	s = new Summary(dataset);
	List keys = null;
	
	keys = dataset.getRowKeys();
	
	if (keys != null) {
		int series = 0;
		Iterator iterator = keys.iterator();
		Shape shape = getLegendItemShape();
		
		while (iterator.hasNext()) {
			String label = iterator.next().toString()+":"+s.getSeriesSummary(series)+"\n";;
			String description = label;
			
			Paint paint = getSeriesPaint(series);
			Paint outlinePaint = getSeriesOutlinePaint(series);	         
			Stroke stroke = getSeriesOutlineStroke(series);	             	
			LegendItem item = new LegendItem(label, description, 
											 null, null, shape, paint, stroke, outlinePaint);
			result.add(item);	                
			series++;
		}
	}
	
	return result;
}

} 
