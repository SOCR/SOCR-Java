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
package edu.ucla.stat.SOCR.chart.util;

import java.awt.*;
import java.io.*;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;

public class Uniform extends Module {

    public  Uniform(double xSize, double ySize, double w) {
	super(xSize, ySize, w);
    }

    void randomKernel(double w)  {
      weight = w;      
    }

    void paint(Graphics g, Database db)  {
    }
    

    double density(int x, int y)  {
      return weight / xsiz / ysiz;
    }
    
    double density(double x, double y)  {
        return weight / xsiz / ysiz;
      }

    void EMpar(Database db, double prior)  {
    }

	XYSeriesCollection getDataset() {
		// TODO Auto-generated method stub
		return null;
	}
}	//END::Uniform Module Class part of MixtureEMExperiment
