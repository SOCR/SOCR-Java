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

import edu.ucla.stat.SOCR.chart.data.*;
import org.jfree.chart.labels.*;
import org.jfree.data.category.*;

 public class SOCRCategoryCellLabelGenerator extends StandardCategorySeriesLabelGenerator { 

	 /**
	 * @uml.property  name="s"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	Summary s =null;
	 /**
	 * @uml.property  name="values_storage" multiplicity="(0 -1)" dimension="2"
	 */
	String values_storage[][];
	 /**
	 * @uml.property  name="seriesCount"
	 */
	int seriesCount;
	 /**
	 * @uml.property  name="categoryCount"
	 */
	int categoryCount;
	
	public SOCRCategoryCellLabelGenerator(CategoryDataset dataset, String[][] vs, int sCount, int cCount){
		super(); 
		s = new Summary(dataset, vs, sCount, cCount);
	} 
	
	public String generateLabel(CategoryDataset dataset, int series){ 
		//		return super.generateLabel(dataset, series);

		return s.getCellSummary(dataset, series)+"\n";

	} 
} 
