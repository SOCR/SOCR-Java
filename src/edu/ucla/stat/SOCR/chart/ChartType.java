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
package edu.ucla.stat.SOCR.chart;

public class ChartType {
	public static final short BAR_CAT_CHART =1;
	public static final short BAR_XY_CHART =2;
	public static final short PIE_CHART =3;
	public static final short LINE_CHART =4;
	public static final short XY_CHART =5;
	public static final short SCATTERPLOT_CHART =100;

	

	public static String lookup(short type) {
		String chartClass = "edu.ucla.stat.SOCR.chart.";
		//hange this to read from XML file
		switch (type) {
		case PIE_CHART:
				chartClass = chartClass + "PieChart";
				break;
		case LINE_CHART:
				chartClass = chartClass + "LineChart";
				break;
		case XY_CHART:
				chartClass = chartClass + "XYPlot";
				break;

		default: chartClass = null;

		}
		System.out.println("In ChartType " + chartClass);
		return chartClass;
	}

}
