
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

import edu.ucla.stat.SOCR.util.QSortAlgorithm;

public class  BootStrapSort extends QSortAlgorithm
{
	//upper[0]. lower[1]
	public static double[] getBounds(double a[], double alpha) throws Exception
	{
		double[] bounds= new double[2];
		QSortAlgorithm quick = new QSortAlgorithm();
		quick.sort(a);
		//System.out.println("lower"+(int)(alpha*a.length));
	//	System.out.println("upper"+(int)((1-alpha)*a.length));
		
		bounds[1]=a[(int)(alpha*a.length)]; //lower
		bounds[0] = a[(int)((1-alpha)*a.length)];  //upper
		//System.out.println(bounds[1]+"->"+bounds[0]);
		return bounds;
	}
	
	public static double upperBound(double a[], double alpha) throws Exception
	{
		QSortAlgorithm quick = new QSortAlgorithm();
		quick.sort(a);
	//	System.out.println("upper"+(int)alpha*a.length);
		return a[(int)alpha*a.length];
	}
	
	public static double lowerBound(double a[], double alpha) throws Exception
	{
		QSortAlgorithm quick = new QSortAlgorithm();
		quick.sort(a);
		//System.out.println("lower"+(int)(1-alpha)*a.length);
		return a[(int)(1-alpha)*a.length];
	}
}