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


// this class has been renamed from 'sampler.java' to 'sampler.java' by annie che 20060702.
package edu.ucla.stat.SOCR.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import edu.ucla.stat.SOCR.modeler.gui.ModelerColor;
import edu.ucla.stat.SOCR.modeler.gui.ModelerDimension;
import edu.ucla.stat.SOCR.modeler.gui.ModelerGui;
import edu.ucla.stat.SOCR.util.OKDialog;
import edu.ucla.stat.SOCR.util.ObservableWrapper;
import edu.ucla.stat.SOCR.util.SOCRJTable;

public class sampler extends SOCRDistributions implements ActionListener {

	private SOCRJTable dataTable;
	private JButton sampleButton = new JButton("Sample");
	//GUI components
	private JPanel samplePanel = new JPanel();
	//private JComboBox modelCombo = new JComboBox();
	public JTextField DataPts = new JTextField("10",3);
	public JScrollPane window = new JScrollPane();
	private ModelerGui link;
	
    private String defaultSelectedDistribution = "Normal Distribution"; //Normal(0, 50^2)

	public sampler(SOCRJTable tabl, URL codebase) {
		codeBase = codebase;
			    
		super.init();
		removeComponent(1);
		addJLabel("Number of samples");
		addJTextField(DataPts);
		addButton(sampleButton);
		sampleButton.addActionListener(this);
		dataTable = tabl;

		samplePanel.setPreferredSize(new Dimension(ModelerDimension.PANEL_SAMPLE_WIDTH, ModelerDimension.PANEL_SAMPLE_HEIGHT));
		// samplePanel is the panel outside.
		samplePanel.setForeground(ModelerColor.PANEL_SAMPLE_FOREGROUND);
		samplePanel.setBackground(ModelerColor.PANEL_SAMPLE_BACKGROUND);
		// fControlPanel is the panel outside.
		fControlPanel.setPreferredSize(new Dimension(ModelerDimension.PANEL_SAMPLE_WIDTH, ModelerDimension.PANEL_SAMPLE_HEIGHT));

		fControlPanel.setForeground(ModelerColor.PANEL_CONTROL_FOREGROUND);
		fControlPanel.setBackground(ModelerColor.PANEL_CONTROL_BACKGROUND);
		
	     // 10/23/08 -- Set the Default Distribution to Normal(0, 50^2) - Why doesn't this work????
	    setSelectedApplication(defaultSelectedDistribution);
	    //System.out.println("(sampler.constructor) Setting the SelectedApp = Normal Distribution!!!");
	}

	public void setDataTable(SOCRJTable tabl){
		dataTable = tabl;
	}
	protected void itemChanged(String className) {
		try {
			dist = Distribution.getInstance(className);
			dist.addObserver(this);
			dist.initialize();
			samplePanel.removeAll();

			samplePanel.setPreferredSize(new Dimension(ModelerDimension.PANEL_SAMPLE_WIDTH, ModelerDimension.PANEL_SAMPLE_HEIGHT));
			samplePanel.setForeground(ModelerColor.PANEL_SAMPLE_FOREGROUND);
			samplePanel.setBackground(ModelerColor.PANEL_SAMPLE_BACKGROUND);

			samplePanel.setLayout(new BoxLayout(samplePanel, BoxLayout.X_AXIS));
			samplePanel.add(Box.createHorizontalStrut(20));
			fControlPanel.setPreferredSize(new Dimension(ModelerDimension.PANEL_SAMPLE_WIDTH, ModelerDimension.PANEL_SAMPLE_HEIGHT));
			fControlPanel.setForeground(ModelerColor.PANEL_CONTROL_FOREGROUND);
			fControlPanel.setBackground(ModelerColor.CHECKBOX_RAWDATA_BACKGROUND);
			samplePanel.add(new JScrollPane(fControlPanel), BorderLayout.EAST);

			samplePanel.add(Box.createHorizontalStrut(20));
			fControlPanel.validate();
			fControlPanel.repaint();

			//window.validate();
			//window.repaint();
			samplePanel.validate();

			samplePanel.repaint();
			//samplePanel.set
			//update graphPanel

		} catch (Throwable e) {
			JOptionPane.showMessageDialog(this, "Sorry, not implemented yet");
			e.printStackTrace();
		}
	}


    public JPanel getPanel() {
		return samplePanel;
    }

	public void addObserver(ObservableWrapper o) {
		o.addJButton(sampleButton);
		//System.out.println("Adding fill table button now!");
	}
	
	public void addGuiLink(ModelerGui l) {
		link =l;
		//System.out.println("Adding fill table button now!");
	}
    public void actionPerformed(ActionEvent evt) {
         if(evt.getSource() ==  sampleButton) {
                if(dataTable.getColumnCount()>1) {
                    OKDialog OKD = new OKDialog(null, true, "Only raw data can be filled. Check Raw Data box and try again");
                    OKD.setVisible(true);
                    }
                    else {
					//clearData();
					int genCt = Integer.parseInt(DataPts.getText());
					if(genCt <1) {
						System.out.println("sample if genCt<1 genCt = " + genCt);
						OKDialog OKD = new OKDialog(null, true, "Number of points must be greater than 0");
						OKD.setVisible(true);
					} else {
						//System.out.println("sample else genCt = " + genCt);

						int vals = dataTable.getTableVal(0).length;
						//System.out.println("Sampler Values="+vals+" genCt="+genCt);
						if(dataTable.getRowCount()- vals <genCt)
						dataTable.appendTableRows(genCt - dataTable.getRowCount()+vals);
						//double[] dat = modelObject.generateSamples(genCt);
						String temp = "";
						for(int i = vals; i< vals+genCt;i++) {
							temp = Double.toString(dist.simulate());
							dataTable.setValueAt(temp, i, 0);
							//System.out.println("dataTable.setValueAt "+i+" " + temp);
						}
						
						if(link!=null)
					    	link.dataTableUpdated();
					//	System.out.println("Sampler after dataTable rowCount="+dataTable.getRowCount());
						//generateSamples(genCt);
                        }
                   }
    		   }
    }
}


