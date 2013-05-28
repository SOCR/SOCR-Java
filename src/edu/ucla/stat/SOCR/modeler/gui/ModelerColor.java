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

import java.awt.Color;

public class ModelerColor extends Color {
	public ModelerColor(float r, float g, float b) {// constructor does nothing
		super(r, g, b);
	}
	public static final Color HISTOGRAM_BAR_FILL = Color.PINK;
	public static final Color HISTOGRAM_BAR_OUTLINE = Color.BLUE;
	public static final Color HISTOGRAM_AXIS = Color.BLACK;
	public static final Color HISTOGRAM_TICKMARK = Color.BLACK;
	public static final Color HISTOGRAM_LABEL = Color.BLUE;

	public static final Color HISTOGRAM_FOURIER_DATA = Color.BLUE;
	public static final Color HISTOGRAM_FOURIER_MODEL = Color.RED;
	public static final Color HISTOGRAM_RANGE = Color.PINK;

	public static final Color PANEL_DATA_FOREGROUND = Color.BLACK;
	public static final Color PANEL_DATA_BACKGROUND = Color.LIGHT_GRAY;
	public static final Color PANEL_GRAPH_FOREGROUND = Color.BLACK;
	public static final Color PANEL_GRAPH_BACKGROUND = Color.LIGHT_GRAY;
	public static final Color PANEL_RESULT_FOREGROUND = Color.BLACK;
	public static final Color PANEL_RESULT_BACKGROUND = Color.LIGHT_GRAY;
	public static final Color PANEL_ABOUT_FOREGROUND = Color.BLACK;
	public static final Color PANEL_ABOUT_BACKGROUND = Color.LIGHT_GRAY;
	public static final Color PANEL_SAMPLE_FOREGROUND = Color.BLACK; // data generator.
	public static final Color PANEL_SAMPLE_BACKGROUND = Color.LIGHT_GRAY;

	// control panel, PANEL_CONTROL_... is the panel at the left side.
	public static final Color PANEL_CONTROL_FOREGROUND = Color.BLACK;
	public static final Color PANEL_CONTROL_BACKGROUND = Color.LIGHT_GRAY;
	public static final Color PANEL_CHOICE_FOREGROUND = Color.BLACK;
	public static final Color PANEL_CHOICE_BACKGROUND = Color.LIGHT_GRAY;
	public static final Color PANEL_PRESENT_BACKGROUND = Color.LIGHT_GRAY;
	public static final Color BAR_BACKGROUND = Color.LIGHT_GRAY;
	public static final Color BUTTON_BACKGROUND = Color.LIGHT_GRAY;
	public static final Color BAR_TEXT_BACKGROUND = Color.LIGHT_GRAY;
	//public static final Color BUTTON_BACKGROUND = Color.LIGHT_GRAY;

	public static final Color CHECKBOX_RESCALE_BACKGROUND = Color.LIGHT_GRAY;
	public static final Color CHECKBOX_RAWDATA_BACKGROUND = Color.LIGHT_GRAY;

	public static final Color GRAPH_BACKGROUND = LIGHT_GRAY;
	public static final Color MODEL_OUTLINE = RED;

}
