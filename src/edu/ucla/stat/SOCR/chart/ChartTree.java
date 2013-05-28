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

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.jfree.chart.plot.CategoryPlot;


/**
 * this is the class generate a static JTree, replaced by ChartTree_dynamic class
 */
public class ChartTree{

	public JTree getTree(){
		/*try {
			// this is default
			UIManager.setLookAndFeel(UIManager.getSystemLookLookAndFeelClassName());		
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
			catch(Exception e){
			}*/
		JTree tree = new JTree(createTreeModel());

        return tree;
    }
 
    private TreeModel createTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("SOCRCharts");
        root.add(createPieChartsNode());
        root.add(createBarChartsNode());
        root.add(createLineChartsNode());
		root.add(createAreaChartsNode());
		//  root.add(createTimeSeriesChartsNode());
		//  root.add(createFinancialChartsNode());
		//  root.add(createXYChartsNode());
		// root.add(createMeterChartsNode());
		// root.add(createMultipleAxisChartsNode());
		//  root.add(createCombinedAxisChartsNode());
		//  root.add(createGanttChartsNode());
        root.add(createMiscellaneousChartsNode());
        return new DefaultTreeModel(root);
    }
    
    /**
     * Creates the tree node for the pie chart demos.
     * 
     * @return A populated tree node.
     */
    private MutableTreeNode createPieChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Pie Charts");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.PieChartDemo1", "PieChartDemo1")
        );  
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.PieChartDemo2", "PieChartDemo2")
        );            
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.PieChartDemo4", "PieChartDemo4")
        );
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.PieChart3DDemo1", "PieChart3DDemo1")
        );
        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.PieChart3DDemo2", "PieChart3DDemo2")
            );
        DefaultMutableTreeNode n6 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.PieChart3DDemo3", "PieChart3DDemo3")
        );  
        DefaultMutableTreeNode n7 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.MultiplePieChartDemo1", "MultiplePieChartDemo1"
            )
        );
        DefaultMutableTreeNode n8 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.RingChartDemo1", "RingChartDemo1"
            )
        );
                
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
        root.add(n6);
		// root.add(n7);
        root.add(n8);
        return root;
    }
    
    /**
     * Creates a tree node containing sample bar charts.
     * 
     * @return The tree node.
     */
    private MutableTreeNode createBarChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Bar Charts");            
        root.add(createCategoryBarChartsNode());
        root.add(createXYBarChartsNode());
        return root;        
    }
    
    /**
     * Creates a tree node containing bar charts based on the 
     * {@link CategoryPlot} class.
     * 
     * @return The tree node.
     */
    private MutableTreeNode createCategoryBarChartsNode() {
        DefaultMutableTreeNode root 
            = new DefaultMutableTreeNode("CategoryPlot");
        
        MutableTreeNode n1 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.BarChartDemo1", "BarChartDemo1"));                
        MutableTreeNode n2 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.BarChartDemo2", "BarChartDemo2"));                
        MutableTreeNode n3 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.BarChartDemo3", "BarChartDemo3"));                
        MutableTreeNode n4 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.BarChartDemo4", "BarChartDemo4"));                
        MutableTreeNode n5 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.BarChartDemo5", "BarChartDemo5"));                
        MutableTreeNode n6 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.BarChartDemo6", "BarChartDemo6"));                
        MutableTreeNode n7 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.BarChartDemo7", "BarChartDemo7"));                
        MutableTreeNode n8 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.BarChartDemo8", "BarChartDemo8"));                
        MutableTreeNode n9 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.BarChartDemo9", "BarChartDemo9"));                
        MutableTreeNode n10 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.BarChartDemo10", "BarChartDemo10"));                
        MutableTreeNode n11 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.BarChart3DDemo1", "BarChart3DDemo1"));                
        MutableTreeNode n12 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.BarChart3DDemo2", "BarChart3DDemo2"));                
        MutableTreeNode n13 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.BarChart3DDemo3", "BarChart3DDemo3"));
        MutableTreeNode n14 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.IntervalBarChartDemo1", "IntervalBarChartDemo1"));
        MutableTreeNode n15 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.LayeredBarChartDemo1", "LayeredBarChartDemo1"));
        MutableTreeNode n16 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.LayeredBarChartDemo2", "LayeredBarChartDemo2"));
        MutableTreeNode n17 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.StackedBarChartDemo1", "StackedBarChartDemo1"));
        MutableTreeNode n18 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.StackedBarChartDemo2", "StackedBarChartDemo2"));
        MutableTreeNode n19 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.StackedBarChartDemo3", "StackedBarChartDemo3"));
        MutableTreeNode n20 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.StackedBarChartDemo4", "StackedBarChartDemo4"));
        MutableTreeNode n21 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.StatisticalBarChartDemo1", 
                "StatisticalBarChartDemo1"));
        MutableTreeNode n22 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.StatisticalBarChartDemo2", 
                "StatisticalBarChartDemo2"));

        MutableTreeNode n23 = new DefaultMutableTreeNode(new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.WaterfallChartDemo1", "WaterfallChartDemo1"));
            
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
		// root.add(n6);
        root.add(n7);
        root.add(n8);
        root.add(n9);
		// root.add(n10);
        root.add(n11);
        root.add(n12);
        root.add(n13);
		// root.add(n14);
        root.add(n15);
        root.add(n16);
        root.add(n17);
        root.add(n18);
        root.add(n19);
        root.add(n20);
        root.add(n21);
        root.add(n22);
        root.add(n23);
        
        return root;        
    }
    
    private MutableTreeNode createXYBarChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("XYPlot");
        
        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.XYBarChartDemo1", "XYBarChartDemo1")
        );                
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.XYBarChartDemo2", "XYBarChartDemo2")
        );                
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.XYBarChartDemo3", "XYBarChartDemo3")
        );                
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.XYBarChartDemo4", "XYBarChartDemo4")
        );                
        
        root.add(n1);
        root.add(n2);
		// root.add(n3);
		// root.add(n4);
        
        return root;
    }
    
    /**
     * Creates a tree node containing line chart items.
     * 
     * @return A tree node.
     */
    private MutableTreeNode createLineChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Line Charts");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.AnnotationDemo1", "AnnotationDemo1"));
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.LineChartDemo1", "LineChartDemo1"));
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.LineChartDemo2", "LineChartDemo2"));
        DefaultMutableTreeNode n3a = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.ScatterChartDemo1", "ScatterChartDemo1"));
        DefaultMutableTreeNode n3b = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.QQNormalPlotDemo", "QQNormalPlotDemo"));
        DefaultMutableTreeNode n3c = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.QQData2DataDemo", "QQData2DataDemo"));
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.LineChartDemo3", "LineChartDemo3"));

        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.LineChartDemo4", "LineChartDemo4"));
        DefaultMutableTreeNode n6 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.LineChartDemo5", "LineChartDemo5"));
        DefaultMutableTreeNode n7 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.LineChartDemo6", "LineChartDemo6"));
        DefaultMutableTreeNode n8 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.NormalDistributionDemo", 
                "NormalDistributionDemo"));
        DefaultMutableTreeNode n9 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.StatisticalLineChartDemo1", 
                "StatisticalLineChartDemo1"));
        DefaultMutableTreeNode n10 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.StatisticalLineChartDemo2", 
                "StatisticalLineChartDemo2"));

        DefaultMutableTreeNode n11 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.XYStepRendererDemo1", 
                "XYStepRendererDemo1"));
        
		//  root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);

		// root.add(n5);
        root.add(n6);
		//  root.add(n7);//same as n3
        root.add(n3a);
		root.add(n3b);// qqnormaldemo
		root.add(n3c); // qqdddemo
        root.add(n8);
        root.add(n9);
        root.add(n10);
        root.add(n11);
        
        return root;
    }
    
    /**
     * A node for various area charts.
     * 
     * @return The node.
     */
    private MutableTreeNode createAreaChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Area Charts");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.AreaChartDemo1", "AreaChartDemo1"));
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.StackedXYAreaChartDemo1", 
                "StackedXYAreaChartDemo1"));
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.StackedXYAreaChartDemo2", 
                "StackedXYAreaChartDemo2"));
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.XYAreaChartDemo1", 
                "XYAreaChartDemo1"));
        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.XYAreaChartDemo2", 
                "XYAreaChartDemo2"));
        
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
        
        return root;
    }
    
    /**
     * Creates a sub-tree for the time series charts.
     * 
     * @return The root node for the subtree.
     */
    private MutableTreeNode createTimeSeriesChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
            "Time Series Charts");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.TimeSeriesDemo1", "TimeSeriesDemo1"));
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.TimeSeriesDemo2", "TimeSeriesDemo2"));
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.TimeSeriesDemo3", "TimeSeriesDemo3"));
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.TimeSeriesDemo4", "TimeSeriesDemo4"));
        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.TimeSeriesDemo5", "TimeSeriesDemo5"));
        DefaultMutableTreeNode n6 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.TimeSeriesDemo6", "TimeSeriesDemo6"));
        DefaultMutableTreeNode n7 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.TimeSeriesDemo7", "TimeSeriesDemo7"));
        DefaultMutableTreeNode n8 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.TimeSeriesDemo8", "TimeSeriesDemo8"));
        DefaultMutableTreeNode n9 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.TimeSeriesDemo9", "TimeSeriesDemo9"));
        DefaultMutableTreeNode n10 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.TimeSeriesDemo10", 
                    "TimeSeriesDemo10"));
        DefaultMutableTreeNode n11 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.TimeSeriesDemo11", 
                    "TimeSeriesDemo11"));
        DefaultMutableTreeNode n12 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.TimeSeriesDemo12", 
                    "TimeSeriesDemo12"));
        DefaultMutableTreeNode n13 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.TimeSeriesDemo13", 
                    "TimeSeriesDemo13"));
        DefaultMutableTreeNode n14 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.PeriodAxisDemo1", "PeriodAxisDemo1"));
        DefaultMutableTreeNode n15 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.PeriodAxisDemo2", "PeriodAxisDemo2"));
        DefaultMutableTreeNode n16 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.DynamicDataDemo1", 
                "DynamicDataDemo1"));
        DefaultMutableTreeNode n17 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.DynamicDataDemo2", 
                "DynamicDataDemo2"));
        DefaultMutableTreeNode n18 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.DynamicDataDemo3", 
                "DynamicDataDemo3"));
        
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
        root.add(n6);
        root.add(n7);
        root.add(n8);
        root.add(n9);
        root.add(n10);
        root.add(n11);
        root.add(n12);
        root.add(n13);
        root.add(n14);
        root.add(n15);
        root.add(n16);
        root.add(n17);
        root.add(n18);
        
        return root;
    }
    
    /**
     * Creates a node for the tree model that contains financial charts.
     * 
     * @return The tree node.
     */
    private MutableTreeNode createFinancialChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
                "Financial Charts");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.CandlestickChartDemo1", 
                "CandlestickChartDemo1"));
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.HighLowChartDemo1", 
                "HighLowChartDemo1"));
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.HighLowChartDemo2", 
                "HighLowChartDemo2"));
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.PriceVolumeDemo1", 
                "PriceVolumeDemo1"));
        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.YieldCurveDemo", 
                "YieldCurveDemo"));
        
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
        return root;
    }

    private MutableTreeNode createXYChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("XY Charts");
        
        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.XYLineAndShapeRendererDemo1", 
                "XYLineAndShapeRendererDemo1"
            )
        );                
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.XYSeriesDemo1", "XYSeriesDemo1")
        );                
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.XYSeriesDemo2", "XYSeriesDemo2")
        );                
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.XYSeriesDemo3", "XYSeriesDemo3")
        );                
        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.WindChartDemo1", "WindChartDemo1")
        );                
        
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
        
        return root;
    }

    /**
     * Creates a node for the tree model that contains "meter" charts.
     * 
     * @return The tree node.
     */
    private MutableTreeNode createMeterChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
                "Meter Charts");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.MeterChartDemo1", 
                "MeterChartDemo1"));
        
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.MeterChartDemo2", 
                "MeterChartDemo2"));
        
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.MeterChartDemo4", 
                "MeterChartDemo4"));
        
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.ThermometerDemo1", 
                "ThermometerDemo1"));
       
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        return root;
    }

    private MutableTreeNode createMultipleAxisChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
            "Multiple Axis Charts"
        );

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.DualAxisDemo1", "DualAxisDemo1")
        );
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.DualAxisDemo2", "DualAxisDemo2")
        );
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.DualAxisDemo3", "DualAxisDemo3")
        );
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.DualAxisDemo4", "DualAxisDemo4")
        );
        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.DualAxisDemo5", "DualAxisDemo5")
        );
        DefaultMutableTreeNode n6 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.MultipleAxisDemo1", "MultipleAxisDemo1"
            )
        );
        
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
        root.add(n6);
        
        return root;
    }
    
    private MutableTreeNode createCombinedAxisChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
            "Combined Axis Charts"
        );

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.CombinedCategoryPlotDemo1", 
                "CombinedCategoryPlotDemo1"
            )
        );
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.CombinedCategoryPlotDemo2", 
                "CombinedCategoryPlotDemo2"
            )
        );
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.CombinedTimeSeriesDemo1", 
                "CombinedTimeSeriesDemo1"
            )
        );
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.CombinedXYPlotDemo1", 
                "CombinedXYPlotDemo1"
            )
        );
        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.CombinedXYPlotDemo2", 
                "CombinedXYPlotDemo2"
            )
        );
        DefaultMutableTreeNode n6 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.CombinedXYPlotDemo3", 
                "CombinedXYPlotDemo3"
            )
        );
        DefaultMutableTreeNode n7 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.CombinedXYPlotDemo4", 
                "CombinedXYPlotDemo4"
            )
        );

        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
        root.add(n6);
        root.add(n7);
        
        return root;
    }

    private MutableTreeNode createGanttChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
            "Gantt Charts"
        );

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.GanttDemo1", "GanttDemo1")
        );
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.GanttDemo2", "GanttDemo2")
        );
        
        root.add(n1);
        root.add(n2);
        
        return root;
    }
    
    /**
     * Creates the subtree containing the miscellaneous chart types.
     * 
     * @return A subtree.
     */
    private MutableTreeNode createMiscellaneousChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
            "Miscellaneous");

        DefaultMutableTreeNode n0 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.BoxAndWhiskerChartDemo1", "BoxAndWhiskerChartDemo1"
            )
        );

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.BubbleChartDemo1", "BubbleChartDemo1"
            )
        );
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.CategoryStepChartDemo1", "CategoryStepChartDemo1"
            )
        );
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.CompassDemo1", "CompassDemo1")
        );
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.CompassFormatDemo1", "CompassFormatDemo1"
            )
        );
        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.CompassFormatDemo2", "CompassFormatDemo2"
            )
        );
        DefaultMutableTreeNode n6 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.DifferenceChartDemo1", "DifferenceChartDemo1"
            )
        );
        DefaultMutableTreeNode n7 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.DifferenceChartDemo2", "DifferenceChartDemo2"
            )
        );
        DefaultMutableTreeNode n8 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.EventFrequencyDemo1", "EventFrequencyDemo1"
            )
        );

        DefaultMutableTreeNode n9 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.HideSeriesDemo1", "HideSeriesDemo1")
        );
        
        DefaultMutableTreeNode n10 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.OverlaidBarChartDemo1", "OverlaidBarChartDemo1"
            )
        );
        
        DefaultMutableTreeNode n11 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.OverlaidBarChartDemo2", "OverlaidBarChartDemo2"
            )
        );
        
        DefaultMutableTreeNode n12 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.SpiderWebChartDemo1", "SpiderWebChartDemo1"
            )
        );
        DefaultMutableTreeNode n13 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.SymbolAxisDemo1", "SymbolAxisDemo1"
            )
        );
        DefaultMutableTreeNode n14 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.PolarChartDemo1", "PolarChartDemo1")
        );
        DefaultMutableTreeNode n15 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.YIntervalChartDemo1", 
                "YIntervalChartDemo1"));
        
        root.add(createCrosshairChartsNode());
		//  root.add(createItemLabelsNode());
		//  root.add(createLegendNode());
		//  root.add(createMarkersNode());
		// root.add(createOrientationNode());
        root.add(n0);
        root.add(n1);
        root.add(n2);
        root.add(n3);
		//  root.add(n4);
		//  root.add(n5);
        root.add(n6);
		//  root.add(n7);
        root.add(n8);
		//  root.add(n9);
		//    root.add(n10);
		//   root.add(n11);
        root.add(n12);
        root.add(n13);
        root.add(n14);
        root.add(n15);
        
        return root;
    }
    
    private MutableTreeNode createCrosshairChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Crosshairs");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.CrosshairDemo1", 
                "CrosshairDemo1"));
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.CrosshairDemo2", 
                "CrosshairDemo2"));
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.CrosshairDemo3", 
                "CrosshairDemo3"));
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.CrosshairDemo4", 
                "CrosshairDemo4"));
        
        root.add(n1);
		//        root.add(n2);
        root.add(n3);
        root.add(n4);
        
        return root;
    }
    
    private MutableTreeNode createItemLabelsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Item Labels");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.ItemLabelDemo1", 
                "ItemLabelDemo1"));
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.ItemLabelDemo2", 
                "ItemLabelDemo2"));
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.ItemLabelDemo3", 
                "ItemLabelDemo3"));
        
        root.add(n1);
        root.add(n2);
        root.add(n3);
        
        return root;
    }
    
    private MutableTreeNode createLegendNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Legends");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
                new DemoDescription("edu.ucla.stat.SOCR.chart.demo.LegendWrapperDemo1", 
                        "LegendWrapperDemo1"));
        
        root.add(n1);
        
        return root;
    }
    
    private MutableTreeNode createMarkersNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Markers");
        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.CategoryMarkerDemo1", 
            "CategoryMarkerDemo1"));
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.CategoryMarkerDemo2", 
            "CategoryMarkerDemo2"));
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.MarkerDemo1", "MarkerDemo1"));
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription("edu.ucla.stat.SOCR.chart.demo.MarkerDemo2", "MarkerDemo2"));
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        return root;
    }
    
    private MutableTreeNode createOrientationNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
            "Plot Orientation"
        );

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.PlotOrientationDemo1", "PlotOrientationDemo1"
            )
        );
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription(
                "edu.ucla.stat.SOCR.chart.demo.PlotOrientationDemo2", "PlotOrientationDemo2"
            )
        );
       
        root.add(n1);
        root.add(n2);
        
        return root;
    }
    
	    
    /**
     * Receives notification of tree selection events and updates the demo 
     * display accordingly.
     * 
     * @param event  the event.
     */
    public void valueChanged(TreeSelectionEvent event) {
        TreePath path = event.getPath();
        Object obj = path.getLastPathComponent();
        if (obj != null) {
            DefaultMutableTreeNode n = (DefaultMutableTreeNode) obj;
            Object userObj = n.getUserObject();
            if (userObj instanceof DemoDescription) {
				//                DemoDescription dd = (DemoDescription) userObj;
                //SwingUtilities.invokeLater(new DisplayDemo(this, dd));
            }
            else {
				/* this.chartContainer.removeAll();
                this.chartContainer.add(createNoDemoSelectedPanel());
                this.displayPanel.validate();
                displayDescription("select.html");*/
            }
        }
        System.out.println(obj);
    }
    

}
