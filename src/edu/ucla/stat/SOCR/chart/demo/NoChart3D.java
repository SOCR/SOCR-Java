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
package edu.ucla.stat.SOCR.chart.demo;


import javax.swing.JPanel;
import javax.swing.JToolBar;

import edu.ucla.stat.SOCR.chart.j3d.Chart3D;

/** 
 * create a empty chart
 * 
 * @author jenny
 *
 */
public class NoChart3D extends Chart3D{
	
	/**
	 * @uml.property  name="toolBar"
	 * @uml.associationEnd  readOnly="true" multiplicity="(1 1)"
	 */
	protected JToolBar toolBar;
	
	public void init(){
		super.init();
  						
		graphPanel= new JPanel();
		}

}

