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
package edu.ucla.stat.SOCR.modeler.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

public class ModelerDimension {
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static int DEFAULT_SCREEN_WIDTH = screenSize.width;
	public static int DEFAULT_SCREEN_HEIGHT = screenSize.height;

	public static double PANEL_DIVISION_PROPORTION = .15; // how much reserved for the left panel.

	public static int DATA_TABLE_HEIGHT = 400;
	public static int DATA_TABLE_WIDTH = 500;

	public static int GRAPH_MARGIN_LEFT = 30;
	public static int GRAPH_MARGIN_RIGHT = 30;
	public static int GRAPH_MARGIN_BOTTOM = 30;
	public static int GRAPH_MARGIN_TOP = 30;


	public static int INFO_ROW = 25;
	public static int INFO_COL = 50;
	public static int RESULT_ROW = 25;
	public static int RESULT_COL = 50;

	public static int PANEL_CHOICE_HEIGHT = 150;
	public static int PANEL_CHOICE_WIDTH = 200;

	public static int PANEL_CONTROL_HEIGHT = 200; // left panel
	public static int PANEL_CONTROL_WIDTH = (int) (DEFAULT_SCREEN_WIDTH * .27);//200;

	public static int PANEL_GRAPH_HEIGHT = 400;
	public static int PANEL_GRAPH_WIDTH = 500;

	public static int PANEL_INFO_HEIGHT = 400;
	public static int PANEL_INFO_WIDTH = 500;

	public static int PANEL_PRESENT_HEIGHT = 400;
	public static int PANEL_PRESENT_WIDTH = (int) (DEFAULT_SCREEN_WIDTH * .48);//600; // right panel

	public static int PANEL_RESULT_HEIGHT = 400;
	public static int PANEL_RESULT_WIDTH = 500;

	public static int PANEL_SAMPLE_HEIGHT = 500; // this determines the ht of the sampling panel.
	public static int PANEL_SAMPLE_WIDTH = 600;

	public static int TOOL_BAR_HEIGHT = 54;
	public static int TOOL_BAR_WIDTH = 400;

	public static int TOOL_PANEL_HEIGHT = 100;
	public static int TOOL_PANEL_WIDTH = 400;

	public static int TOP_RIGHT_PANE_HEIGHT = 240;
	public static int TOP_RIGHT_PANE_WIDTH = 400;

	public static int TABBED_PANE_HEIGHT = (int) (DEFAULT_SCREEN_HEIGHT - TOP_RIGHT_PANE_HEIGHT); // bottom-right panel.
	public static int TABBED_PANE_WIDTH = 200;


	public static void initScreenSize() {
//		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//DEFAULT_SCREEN_WIDTH = screenSize.width;
		//DEFAULT_SCREEN_HEIGHT = screenSize.height;
		//System.out.println("ModelerDimension DEFAULT_SCREEN_WIDTH = " + DEFAULT_SCREEN_WIDTH);
		//System.out.println("ModelerDimension DEFAULT_SCREEN_HEIGHT = " + DEFAULT_SCREEN_HEIGHT);
		//System.out.println("ModelerDimension PANEL_PRESENT_WIDTH = " + PANEL_PRESENT_WIDTH);
		//System.out.println("ModelerDimension PANEL_CONTROL_WIDTH = " + PANEL_CONTROL_WIDTH);

		//PANEL_PRESENT_WIDTH = (int) (DEFAULT_SCREEN_WIDTH * .5);
		//PANEL_CONTROL_WIDTH = (int) (DEFAULT_SCREEN_WIDTH * .2);

	}
}
