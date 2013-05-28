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

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import org.jfree.chart.LegendItem;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.data.xy.XYDataset;

import edu.ucla.stat.SOCR.chart.data.Summary;

public class SOCRPolarItemRenderer extends DefaultPolarItemRenderer{
	Summary s =null;

    public LegendItem getLegendItem(int series) {
        LegendItem result = null;

        PolarPlot polarPlot = getPlot();
        if (polarPlot != null) {
            XYDataset dataset;

            dataset = polarPlot.getDataset();
			s = new Summary(dataset);
            if (dataset != null) {
                //String label = dataset.getSeriesKey(series).toString();
				String label = s.getYSummary(series)+"\n";
                String description = label;
                Shape shape = lookupSeriesShape(series);
                Paint paint = lookupSeriesPaint(series);
                Paint outlinePaint = lookupSeriesOutlinePaint(series);
                Stroke outlineStroke = lookupSeriesOutlineStroke(series);
                result = new LegendItem(label, description, null, null, 
                        shape, paint, outlineStroke, outlinePaint);
                result.setDataset(dataset);
            }
        }
        return result;
    }

} 
