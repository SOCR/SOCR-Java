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
package edu.ucla.stat.SOCR.core;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.text.*;

import javax.swing.*;
import javax.swing.border.*;

import edu.ucla.stat.SOCR.gui.SOCROptionPane;

/**
 * This is abstract JApplect which used for edu.ucla.stat.SOCR. subclass must
 * implements getCurrentItem() to return the current object which the applect is
 * working on. e.g. for Distributions it should be an Distribution instance.
 * <p>
 * The contentPane is a split Pane, the left pane is called fControlPane, it has
 * a JComboBox, an Panel for buttons, if the getCurrentItem() return a
 * IValueSettable it will add valueSetters automatically. You dont have to
 * construct it from scratch. Instead, you just provide in the method initGUI()
 * a <code> implementedFile </code> a value to let the JComboBox to initilize
 * from the file. Using addButton() to add Button to it. Subclasses also should
 * implement or override the following mehtods initGUI(),itemChanged() for
 * detailed information, see javadoc of these methods
 * 
 * @author <A HREF="mailto:qma@loni.ucla.edu">Jeff Ma </A>
 */

public abstract class SOCRApplet3 extends JApplet {
	public static final Color textColor = new Color(194, 215, 134);
	public static final Font textFont = new Font("SansSerif", Font.PLAIN, 12);
	protected	URL codeBase;

	/**
	 * Controlpane has two component, north and valueSetterPane
	 */
	protected JSplitPane fSOCRPane;
	public JPanel fControlPanel = new JPanel(new BorderLayout());

	protected JScrollPane fPresentPanel = new JScrollPane();

	public String controlPanelTitle = "controlPanelTitle";
	public String implementedFunctor = null; 
	public String implementedFile = null;

	//north cotains JComboBox and buttons
	private Box north = Box.createVerticalBox();

	private SOCRJComboBox implementedCombo;
	private SOCRJComboBox implementedFunctorCombo;

	public JPanel buttonP, buttonP2,radioButtonP,jTextAreaPane;
	public JPanel valueSetterPane = new JPanel();//for plugins to add

	JRadioButton[] rButtons;
	// valuesetter

	public void init() {
		if(codeBase == null)
			codeBase = ((JApplet) this).getCodeBase();
		//this.getC
		initGUI();
		packFunctorPane(); // added by rahul gidwani
		// packControlPane();

		/*JSplitPane*/ fSOCRPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				new JScrollPane(fControlPanel), fPresentPanel);
		fSOCRPane.setOneTouchExpandable(true);
		setContentPane(fSOCRPane);
		fireFunctorChanged();
		fireItemChanged();
	}

	public void setSOCRAppletCodeBase(URL _codeBase)
	{	codeBase = _codeBase;
	}

	public URL getSOCRAppletCodeBase()
	{	return codeBase;
	}

	public void start() {
		//fPresentPanel.setDividerLocation(0.62);
	}

	/**
	 * initialize gui, however it don't have to create Controlpane from scratch,
	 * it should itialize the implementedFile add invoke addButton() to add
	 * buttons, if it is necessary
	 */
	protected void initGUI() {}

	/**
	 * subclass should implement this method to do whatever needed coressponding
	 * the itemchanged event of JComboBox. It should update the object which the
	 * applet is working on
	 * 
	 * @param className className what need be instantiated
	 */
	protected void itemChanged(String className) {}

	/**
	 * subclass should implement this method to do whatever needed coressponding
	 * the itemchanged event of JComboBox. It should update the object which the
	 * applet is working on
	 * 
	 * @param className className what need be instantiated
	 */
	protected void functorChanged(String className) {}

	/**
	 * @return the object which the applet is working on. for SocrDistributin it
	 *         should return current instance of Distribution.
	 * @uml.property name="currentItem"
	 */
	public abstract Object getCurrentItem();

	public void packFunctorPaneExternalCall()
	{
		packFunctorPane();
	}

	public void packFunctorPane()
	{
		if (implementedFunctor != null)
		{
			try
			{ 
				implementedFunctorCombo = new SOCRJComboBox(new URL(codeBase,
						implementedFunctor).openStream());
				north.add(implementedFunctorCombo);
				implementedFunctorCombo.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						fireFunctorChanged();
					}

				}); 
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
				e1.printStackTrace();
			}

		}
		getParameterFunctorFromHtml();
	}

	public void packControlPaneExternalCall()
	{	packControlPane();
	}

	private void packControlPane() {
		if (implementedFile != null) {
			try {
				if (implementedCombo != null)
					north.remove(implementedCombo); 
				
				implementedCombo = new SOCRJComboBox(new URL(codeBase,
						implementedFile).openStream());

				north.add(implementedCombo);
				implementedCombo.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						fireItemChanged();
					}
				});
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
				e1.printStackTrace();
			}
		}
		 getParameterItemFromHtml();
		 
		if (buttonP2 != null)	north.add(buttonP2);
		if (buttonP != null) north.add(buttonP);
		if (radioButtonP != null ) north.add(radioButtonP);


		fControlPanel.add(north, BorderLayout.NORTH);
		fControlPanel.add(valueSetterPane, BorderLayout.CENTER);
		valueSetterPane.setLayout(new BoxLayout(valueSetterPane, BoxLayout.Y_AXIS));
		fControlPanel.setBorder(new TitledBorder(new EtchedBorder(),
				controlPanelTitle));
	}

	public void getParameterItemFromHtml() {
    	String choosen="";
    
    	choosen = getParameter("selectedItemName");

    	
    	//System.out.println("The parameterItem from html ="+choosen);
    	if (choosen!=null && choosen.length()!=0){
    		choosen = choosen.toLowerCase(); 
    	
    		int listLength= implementedCombo.getItemCount();
    		
    		for (int i=0; i<listLength; i++){
    			//System.out.println(i+" item"+" ("+implementedCombo.getItemName(i).toLowerCase()+")");
    			if ( implementedCombo.getItemName(i).toLowerCase().equals(choosen)){
    				implementedCombo.setSelectedIndex(i);
    				fireItemChanged();
    			}
    		}
    	}
    }
	
	public void getParameterFunctorFromHtml() {
    	String choosen="";
    	
    	choosen = getParameter("selectedFunctorName");
   	
    	//System.out.println("The functor from html ="+choosen);
    	if (choosen!=null && choosen.length()!=0){
    		choosen = choosen.toLowerCase(); 

    		int listLength= implementedFunctorCombo.getItemCount();
    		
    		for (int i=0; i<listLength; i++){
    		//	System.out.println(i+" functor"+" ("+implementedFunctorCombo.getItemName(i).toLowerCase()+")");
    			if ( implementedFunctorCombo.getItemName(i).toLowerCase().equals(choosen)){
    				implementedFunctorCombo.setSelectedIndex(i);
    				fireFunctorChanged();
    			}
    		}
    	}
    }
	private void fireItemChanged() {
		valueSetterPane.removeAll();

		itemChanged(implementedCombo.getSelectedClassName());
		//update controlpane
		Object o = getCurrentItem();
		if (o instanceof IValueSettable) {       	
			ValueSetter[] vsetter = ((IValueSettable) o).getValueSetters();
			for (int i = 0; i < vsetter.length; i++) {
				valueSetterPane.add(Box.createVerticalStrut(8));
				valueSetterPane.add(vsetter[i]);
			}
			valueSetterPane.add(Box.createVerticalGlue());
		}
		valueSetterPane.validate();
		valueSetterPane.repaint();
	}

	private void fireFunctorChanged()
	{
		functorChanged(implementedFunctorCombo.getSelectedClassName());
		packControlPane();
		fireItemChanged();
	}

	public SOCRJComboBox getImplementedCombo()
	{	return implementedCombo;
	}

	public SOCRJComboBox getImplementedFunctorCombo()
	{
		return implementedFunctorCombo;
	}

	public void addButton2(String text, String toolTipText, ActionListener l) {
		JButton b = new JButton(text);
		b.setName(text);
		b.setToolTipText(toolTipText);
		b.addActionListener(l);
		if (buttonP2== null) {
			buttonP2 = new JPanel();
			buttonP2.add(Box.createVerticalGlue());
		}
		buttonP2.add(b);       
	}
	public void addButton(String text, ActionListener l) {
		JButton b = new JButton(text);
		b.setName(text);
		b.addActionListener(l);
		if (buttonP == null) {
			buttonP = new JPanel();
			buttonP.add(Box.createVerticalGlue());
		}
		buttonP.add(b);       
	}

	public void addRadioButton(String text, String toolTipText, String[] bValues,int defaultIndex, ActionListener l) {
		if (radioButtonP == null) {
			radioButtonP = new JPanel();
			//radioButtonP.setLayout(new BoxLayout(radioButtonP, BoxLayout.Y_AXIS));
			radioButtonP.add(Box.createVerticalGlue());
			radioButtonP.add(new JLabel(text));
		}
		ButtonGroup group = new ButtonGroup();
		rButtons = new JRadioButton[bValues.length];
		for (int i=0; i<bValues.length; i++){
			rButtons[i] = new JRadioButton(bValues[i]);
			rButtons[i].setName(bValues[i]);
			rButtons[i].addActionListener(l);    
			rButtons[i].setActionCommand(bValues[i]);
			radioButtonP.add(rButtons[i]); 
			group.add(rButtons[i]);
			if (defaultIndex==i)
				rButtons[i].setSelected(true);
		}
	}

	public void resetRadioButton(int defaultIndex){
		rButtons[defaultIndex].setSelected(true);
	}

	public void addButton(String text, String toolTipText, ActionListener l) {
		JButton b = new JButton(text);
		b.setName(text);
		b.setToolTipText(toolTipText);
		b.addActionListener(l);
		if (buttonP == null) {
			buttonP = new JPanel();
			buttonP.add(Box.createVerticalGlue());
		}
		buttonP.add(b);       
	}

	public void removeComponent(int pos) {
		buttonP.remove(buttonP.getComponent(pos));
	}
	
	public void addJScrollPane(JScrollPane jp){
		if (buttonP == null) {
			buttonP = new JPanel();
			buttonP.add(Box.createHorizontalGlue());
		}
		buttonP.add(jp);
	}

	public void addButton(JButton b) {
		if (buttonP == null) {
			buttonP = new JPanel();
			buttonP.add(Box.createVerticalGlue());
		}
		buttonP.add(b);
	}

	public void addJCheckBox(JCheckBox b) {
		if (buttonP == null) {
			buttonP = new JPanel();
			buttonP.add(Box.createHorizontalGlue());
		}
		buttonP.add(b);
	}

	public void addJTextField(JTextField b) {
		if (buttonP == null) {
			buttonP = new JPanel();
			buttonP.add(Box.createVerticalBox());
		}
		buttonP.add(b);
	}

	public void addJTextField(JTextField b, JLabel l) {
		if (buttonP == null) {
			buttonP = new JPanel();
			buttonP.add(Box.createVerticalBox());
		}
		if (jTextAreaPane==null) jTextAreaPane = new JPanel(new GridLayout(2,2));
		jTextAreaPane.add(l);
		jTextAreaPane.add(b);
		fControlPanel.add(jTextAreaPane, BorderLayout.SOUTH);
	}

	public void addJLabel(String b) {
		if (buttonP == null) {
			buttonP = new JPanel();
			buttonP.add(Box.createHorizontalGlue());
		}
		buttonP.add(new JLabel(b));
	}

	public void addJPanel(JPanel jp){
		if (buttonP == null) {
			buttonP = new JPanel();
			buttonP.add(Box.createHorizontalGlue());
		}
		valueSetterPane.add(Box.createVerticalStrut(8));
		valueSetterPane.add(jp);
		valueSetterPane.validate();
		valueSetterPane.repaint();
	}
	
	public void clearvalueSetterPane(){
		valueSetterPane.removeAll();
	}

	public String getAppletInfo() {
		return "Name: Statistics Online Compute Resource (SOCR)\r\n"
		+ "Author: Ivo Dinov, Ph.D.\r\n"
		+ "Institution: UCLA Statistics/Neurology\r\n"
		+ "Version: 1.0\r\n"
		+ "Date: 2000-2006 (06/29/2003)\r\n"
		+ "URL: http://www.socr.ucla.edu";
	}

	private static DecimalFormat decimalFormat = new DecimalFormat("#.000000");

	public static String format(double x) {
		return decimalFormat.format(x);
	}

	//	if the user has internet connection, goto wiki page
	// if not, use optionPane to display the info
	public void popInfo(String noConnectionInfo, URL isConnectedUrl, String target){
		try{	             		         
			boolean isIt = java.net.InetAddress.getLocalHost().isLoopbackAddress();
			if (isIt) {
				//System.out.println("not conected to internet");
				SOCROptionPane.showMessageDialog(this, noConnectionInfo);

			}else{
				//System.out.println("conected to internet");
				getAppletContext().showDocument(isConnectedUrl,target);          					
			}          	 
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			e.printStackTrace();
		}
	}

	public static class SOCRTextArea extends JTextArea {
		public SOCRTextArea() {
			//configuration for statusTextArea
			setBackground(textColor);
			setFont(textFont);
			setEditable(false);
		}
	}
}
