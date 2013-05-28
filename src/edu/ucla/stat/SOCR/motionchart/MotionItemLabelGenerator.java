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
 It's Online, Therefore, It Exists!
 ***************************************************/

package edu.ucla.stat.SOCR.motionchart;

import org.jfree.chart.labels.BubbleXYItemLabelGenerator;
import org.jfree.data.xy.XYDataset;

import java.text.MessageFormat;

/**
 * Package: edu.ucla.stat.SOCR.motionchart
 * User: Khashim
 * Date: Dec 21, 2008
 * Time: 6:42:35 PM
 *
 * @author Jameel
 */
public class MotionItemLabelGenerator extends BubbleXYItemLabelGenerator
{
    /**
     * Generates an item label for a particular item within a series.
     *
     * @param dataset the dataset (<code>null</code> not permitted).
     * @param series  the series index (zero-based).
     * @param item    the item index (zero-based).
     * @return The item label (possibly <code>null</code>).
     */
    @Override
    public String generateLabel(XYDataset dataset, int series, int item)
    {
        if(dataset instanceof MotionDataSet)
        {
            return MessageFormat.format("{0}", ((MotionDataSet)dataset).getCategory(series,item));
        }

        return super.generateLabel(dataset, series, item);
    }
}
