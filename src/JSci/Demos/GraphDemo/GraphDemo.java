package JSci.Demos.GraphDemo;

import java.awt.*;
import java.awt.event.*;
import JSci.awt.*;
import JSci.swing.*;

/**
* Sample program demonstrating use of the Swing/AWT graph components.
* @author Mark Hale
* @version 1.1
*/
public class GraphDemo extends Frame {
        private DefaultCategoryGraph2DModel catModel;
        private DefaultGraph2DModel valModel;

        public static void main(String arg[]) {
                new GraphDemo();
        }
        public GraphDemo() {
                super("JSci Graph Demo");
                addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent evt) {
                                dispose();
                                System.exit(0);
                        }
                });
                setLayout(new BorderLayout());
                setSize(700,600);
                Label title;
                Font titleFont=new Font("Default",Font.BOLD,14);
        // category graphs
                catModel=createCategoryData();
                // bar graph
                Panel graph1=new Panel();
                graph1.setLayout(new JGraphLayout());
                title=new Label("Bar graph",Label.CENTER);
                title.setFont(titleFont);
                graph1.add(title,"Title");
                graph1.add(new JBarGraph(catModel),"Graph");
                graph1.add(new Label("y-axis",Label.RIGHT),"Y-axis");
                graph1.add(new Label("category axis",Label.CENTER),"X-axis");
                // pie chart
                Panel graph2=new Panel();
                graph2.setLayout(new GraphLayout());
                title=new Label("Pie chart",Label.CENTER);
                title.setFont(titleFont);
                graph2.add(title,"Title");
                graph2.add(new JPieChart(catModel),"Graph");
        // value graphs
                valModel=createValueData();
                // line graph
                Panel graph3=new Panel();
                graph3.setLayout(new JGraphLayout());
                title=new Label("Line graph",Label.CENTER);
                title.setFont(titleFont);
                graph3.add(title,"Title");
                graph3.add(new JLineGraph(valModel),"Graph");
                Choice choice=new Choice();
                choice.add("Temp.");
                choice.add("Rain fall");
                choice.addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent evt) {
                                if(evt.getStateChange()==ItemEvent.SELECTED) {
                                        if(evt.getItem().toString().equals("Temp.")) {
                                                valModel.setSeriesVisible(0,true);
                                                valModel.setSeriesVisible(1,false);
                                        } else if(evt.getItem().toString().equals("Rain fall")) {
                                                valModel.setSeriesVisible(0,false);
                                                valModel.setSeriesVisible(1,true);
                                        }
                                }
                        }
                });
                graph3.add(choice,"Y-axis");
                graph3.add(new Label("x-axis",Label.CENTER),"X-axis");
        // layout
                Panel graphs=new Panel();
                GridBagLayout gb=new GridBagLayout();
                GridBagConstraints gbc=new GridBagConstraints();
                graphs.setLayout(gb);
                gbc.weightx=0.5;gbc.weighty=0.5;
                gbc.fill=GridBagConstraints.BOTH;
                gbc.gridx=0;gbc.gridy=0;
                gb.setConstraints(graph1,gbc);
                graphs.add(graph1);
                gbc.gridx=1;gbc.gridy=0;
                gb.setConstraints(graph2,gbc);
                graphs.add(graph2);
                gbc.gridx=0;gbc.gridy=1;
                gbc.gridwidth=GridBagConstraints.REMAINDER;
                gb.setConstraints(graph3,gbc);
                graphs.add(graph3);
                add(graphs,"Center");
                Button push=new Button("update");
                push.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                                float newData[]={3.4f,6.5f,6.2f,2.9f,1.8f};
                                catModel.changeSeries(0,newData);
                        }
                });
                add(push,"South");
                setVisible(true);
        }
        private static DefaultCategoryGraph2DModel createCategoryData() {
                String labels[]={"Alpha1","Beta2","Gamma3","Delta4","Epsilon5"};
                float values1[]={2.4f,7.3f,3.2f,0.5f,2.2f};
                float values2[]={0.9f,3.4f,2.1f,6.5f,8.2f};
                DefaultCategoryGraph2DModel catmodel=new DefaultCategoryGraph2DModel();
                catmodel.setCategories(labels);
                catmodel.addSeries(values1);
                catmodel.addSeries(values2);
                return catmodel;
        }
        private static DefaultGraph2DModel createValueData() {
                float values1[]={3.0f,2.8f,3.5f,3.6f,3.1f,2.6f};
                float values2[]={7.8f,4.1f,0.9f,0.2f,1.3f,2.5f};
                DefaultGraph2DModel valmodel=new DefaultGraph2DModel();
                valmodel.setXAxis(0.0f,5.0f,values1.length);
                valmodel.addSeries(values1);
                valmodel.addSeries(values2);
                valmodel.setSeriesVisible(1,false);
                return valmodel;
        }
}

