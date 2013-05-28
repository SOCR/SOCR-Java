/*
 * Copyright (C) Justo Montiel, David Torres, Sergio Gomez, Alberto Fernandez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see
 * <http://www.gnu.org/licenses/>
 */

package edu.ucla.stat.SOCR.analyses.util.moduls.frm.Panels;

import edu.ucla.stat.SOCR.analyses.util.inicial.FesLog;
import edu.ucla.stat.SOCR.analyses.util.inicial.Language;
import edu.ucla.stat.SOCR.analyses.util.inicial.Parametres_Inicials;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import edu.ucla.stat.SOCR.analyses.util.moduls.frm.FrmPrincipalDesk;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.InternalFrameData;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.children.FontSelection;
import edu.ucla.stat.SOCR.analyses.util.tipus.Orientation;
import edu.ucla.stat.SOCR.analyses.util.tipus.metodo;
import edu.ucla.stat.SOCR.analyses.util.tipus.rotacioNoms;
import edu.ucla.stat.SOCR.analyses.util.tipus.tipusDades;
import edu.ucla.stat.SOCR.analyses.util.utils.MiMath;
import edu.ucla.stat.SOCR.analyses.util.utils.SmartAxis;
import edu.ucla.stat.SOCR.analyses.util.definicions.CfgPanelMenu;
import edu.ucla.stat.SOCR.analyses.util.definicions.Config;
import edu.ucla.stat.SOCR.analyses.util.definicions.Formats;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Settings panel
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Jpan_Menu extends JPanel implements ActionListener, FocusListener,
		InternalFrameListener {

	private static final long serialVersionUID = 1L;

	// Labels to identify the components of the JPanel
	private static JLabel lblTypeMeasure, lblMethod, lblPrecision;
	private static JLabel lblTreeTitle, lblTreeOrientation;
	private static JLabel lblNodesTitle, lblNodesOrientation, lblNodesSize;
	private static JLabel lblAxisTitle, lblAxisMin, lblAxisMax,
			lblAxisSeparation, lblAxisEvery, lblAxisTicks, lblAxisDecimals;

	// Type of measure
	private static JRadioButton rbDistances, rbWeights;

	// Clustering algorithm
	private static JComboBox cbMethod;

	// Text box for the precision
	private static JTextField txtPrecision;

	// Orientation of the tree
	private static JComboBox cbTreeOrientation;

	// Radius for the bullets of nodes
	private static JComboBox cbNodesSize;

	// Orientation of the labels of nodes
	private static JComboBox cbNodesOrientation;

	// To decide the visibility of components
	private static JCheckBox chkBands, chkNodesLabels, chkAxis, chkAxisLabels;

	// Text boxes for user inputs
	private static JTextField txtAxisMin, txtAxisMax, txtAxisSeparation,
			txtAxisEvery, txtAxisDecimals;

	// Fonts and colors
	private static JButton btnColorBands, btnFontNodes, btnColorNodes,
			btnFontAxisLabels, btnColorAxis, btnColorAxisLabels;
	private static Font fontNodesLabels, fontAxisLabels;
	private static Color colorBands, colorNodesLabels, colorAxis,
			colorAxisLabels;

	// Value for the automatic generation of the axis when a file is loaded
	private static double factorEscala = Parametres_Inicials.getFactorEscala();

	private Jpan_Imatge pimg;

	public Jpan_Menu(final FrmPrincipalDesk fr) {
		super();
		loadFormats();
		getPanel();
	}

	private void loadFormats() {
            try{
                new Language("");
            }
            catch (Exception e){
            }
		lblTypeMeasure = Formats.AtributLabelFont(Language.getLabel(114));// Type of measure
		rbDistances = Formats.AtributOPTFont(Language.getLabel(27), true);// Distances
		rbWeights = Formats.AtributOPTFont(Language.getLabel(28), false);// Weights

		lblMethod = Formats.AtributLabelFont(Language.getLabel(24));// Clustering algorithm
		final String strMethod[] = { "Single Linkage", "Complete Linkage",
				"Unweighted Average", "Weighted Average",
				"Unweighted Centroid", "Weighted Centroid",
				"Joint Between-Within" };
		cbMethod = Formats.AttributCBFont(strMethod);
		cbMethod.setSelectedItem("Unweighted Average");

		lblPrecision = Formats.AtributLabelFont(Language.getLabel(51));// Precision
		txtPrecision = Formats.AtributTXTFont("", 4, getLocale());

		lblTreeTitle = Formats.AtributTitleFont(" " + Language.getLabel(29));// TREE

		lblTreeOrientation = Formats.AtributLabelFont(Language.getLabel(26));// Tree orientation
		final String strTreeOrientation[] = { Language.getLabel(88),
				Language.getLabel(89), Language.getLabel(90),
				Language.getLabel(91) };
		cbTreeOrientation = Formats.AttributCBFont(strTreeOrientation);
		cbTreeOrientation.setSelectedItem(Language.getLabel(88));// North (by default)

		chkBands = Formats.AtributCHKFont(Language.getLabel(48));// Show bands
		chkBands.setSelected(true);
		colorBands = Parametres_Inicials.getColorMarge();

		lblNodesTitle = Formats.AtributTitleFont(" " + Language.getLabel(30));// NODES

		lblNodesSize = Formats.AtributLabelFont(Language.getLabel(113));// Nodes size
		final String strNodesSize[] = { "0", "2", "3", "4", "5", "6" };
		cbNodesSize = Formats.AttributCBFont(strNodesSize);
		cbNodesSize.setSelectedItem("0");

		chkNodesLabels = Formats.AtributCHKFont(Language.getLabel(31));// Show labels (nodes)
		chkNodesLabels.setSelected(true);
		fontNodesLabels = Parametres_Inicials.getFontNames();
		colorNodesLabels = Parametres_Inicials.getColorNames();

		lblNodesOrientation = Formats.AtributLabelFont(Language.getLabel(33));// Labels orientation
		final String strLabelsOrientation[] = { Language.getLabel(94),
				Language.getLabel(92), Language.getLabel(93) };
		cbNodesOrientation = Formats.AttributCBFont(strLabelsOrientation);
		cbNodesOrientation.setSelectedItem(Language.getLabel(94));// Vertical (by default)

		lblAxisTitle = Formats.AtributTitleFont(" " + Language.getLabel(36));// AXIS

		chkAxis = Formats.AtributCHKFont(Language.getLabel(37));// Show axis
		chkAxis.setSelected(true);
		colorAxis = Parametres_Inicials.getColorAxis();

		lblAxisMin = Formats.AtributLabelFont(Language.getLabel(41));// Minimum value
		txtAxisMin = Formats.AtributTXTFont("", 4, getLocale());

		lblAxisMax = Formats.AtributLabelFont(Language.getLabel(42));// Maximum value
		txtAxisMax = Formats.AtributTXTFont("", 4, getLocale());

		lblAxisSeparation = Formats.AtributLabelFont(Language.getLabel(43));// Tick separation
		txtAxisSeparation = Formats.AtributTXTFont("", 4, getLocale());

		chkAxisLabels = Formats.AtributCHKFont(Language.getLabel(39));// Show labels (axis)
		chkAxisLabels.setSelected(true);
		fontAxisLabels = Parametres_Inicials.getFontAxis();
		colorAxisLabels = Parametres_Inicials.getColorLabels();

		lblAxisEvery = Formats.AtributLabelFont(Language.getLabel(44));// Labels every
		txtAxisEvery = Formats.AtributTXTFont("", 4, getLocale());
		lblAxisTicks = Formats.AtributLabelFont(Language.getLabel(115));// ticks

		lblAxisDecimals = Formats.AtributLabelFont(Language.getLabel(49));// Labels decimals
		txtAxisDecimals = Formats.AtributTXTFont("", 4, getLocale());
	}

	private void getPanel() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder(Language.getLabel(23))); // Settings
		final GridBagConstraints c = new GridBagConstraints();
		int gridy = 0;

		// group options
		ButtonGroup optTipus;
		optTipus = new ButtonGroup();
		optTipus.add(rbDistances);
		optTipus.add(rbWeights);

		// lbl type measure
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(lblTypeMeasure, c);
		// opt distances
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(rbDistances, c);
		// opt weights
		c.gridx = 2;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(rbWeights, c);
		gridy++;

		// lbl method
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(lblMethod, c);
		// cb method
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(cbMethod, c);
		gridy++;

		// lbl precision
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(lblPrecision, c);
		// txt precision
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		txtPrecision.setName("precision");
		txtPrecision.addFocusListener(this);
		add(txtPrecision, c);
		gridy++;

		// empty space
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(new JLabel(" "), c);
		gridy++;

		// TREE

		// lbl tree title
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(lblTreeTitle, c);
		gridy++;

		// lbl tree orientation
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(lblTreeOrientation, c);
		// cb tree orientation
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		cbTreeOrientation.setActionCommand("orientacio_dendo");
		cbTreeOrientation.addActionListener(this);
		add(cbTreeOrientation, c);
		// tree drawing
		c.gridx = 2;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 2;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 1, 1, 1);
		pimg = new Jpan_Imatge();
		add(pimg, c);
		gridy++;

		// checkbox bands
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(chkBands, c);
		// color bands
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		btnColorBands = new JButton(Language.getLabel(35));// Color (bands)
		btnColorBands.setActionCommand("color_marge");
		btnColorBands.addActionListener(this);
		add(btnColorBands, c);
		gridy++;

		// empty space
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(new JLabel(" "), c);
		gridy++;

		// NODES

		// lbl nodes title
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(lblNodesTitle, c);
		gridy++;

		// lbl nodes size
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(lblNodesSize, c);
		// cb nodes size
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(cbNodesSize, c);
		gridy++;

		// chk nodes labels
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(chkNodesLabels, c);
		// btn font nodes
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		btnFontNodes = new JButton(Language.getLabel(34));// Font (nodes)
		btnFontNodes.setActionCommand("font_noms");
		btnFontNodes.addActionListener(this);
		add(btnFontNodes, c);
		// btn color nodes
		c.gridx = 2;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		btnColorNodes = new JButton(Language.getLabel(35));// Color (nodes)
		btnColorNodes.setActionCommand("color_noms");
		btnColorNodes.addActionListener(this);
		add(btnColorNodes, c);
		gridy++;

		// lbl nodes orientation
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(lblNodesOrientation, c);
		// cb nodes orientation
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(cbNodesOrientation, c);
		gridy++;

		// empty space
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(new JLabel(" "), c);
		gridy++;

		// AXIS

		// lbl axis title
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(lblAxisTitle, c);
		gridy++;

		// chk axis
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(chkAxis, c);
		// btn color axis
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		btnColorAxis = new JButton(Language.getLabel(38));// Color (axis)
		btnColorAxis.setActionCommand("color_axis");
		btnColorAxis.addActionListener(this);
		add(btnColorAxis, c);
		gridy++;

		// lbl axis min
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(lblAxisMin, c);
		// txt axis min
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		txtAxisMin.setName("axis_min");
		txtAxisMin.addFocusListener(this);
		add(txtAxisMin, c);
		gridy++;

		// lbl axis max
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(lblAxisMax, c);
		// txt axis max
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		txtAxisMax.setName("axis_max");
		txtAxisMax.addFocusListener(this);
		add(txtAxisMax, c);
		gridy++;

		// lbl axis ticks separation
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(lblAxisSeparation, c);
		// txt axis ticks separation
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		txtAxisSeparation.setName("axis_separation");
		txtAxisSeparation.addFocusListener(this);
		add(txtAxisSeparation, c);
		gridy++;

		// chk axis labels
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(chkAxisLabels, c);
		// btn font axis labels
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		btnFontAxisLabels = new JButton(Language.getLabel(45));// Font (axis labels)
		btnFontAxisLabels.setActionCommand("font_axis");
		btnFontAxisLabels.addActionListener(this);
		add(btnFontAxisLabels, c);
		// btn color axis labels
		c.gridx = 2;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		btnColorAxisLabels = new JButton(Language.getLabel(40));// Color (axis labels)
		btnColorAxisLabels.setActionCommand("color_label");
		btnColorAxisLabels.addActionListener(this);
		add(btnColorAxisLabels, c);
		gridy++;

		// lbl axis labels every
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(lblAxisEvery, c);
		// txt axis labels every
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		txtAxisEvery.setName("axis_every");
		txtAxisEvery.addFocusListener(this);
		add(txtAxisEvery, c);
		// txt axis ticks
		c.gridx = 2;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(lblAxisTicks, c);
		gridy++;

		// lbl axis decimals
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		add(lblAxisDecimals, c);
		// txt axis decimals
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		txtAxisDecimals.setName("axis_decimals");
		txtAxisDecimals.addFocusListener(this);
		add(txtAxisDecimals, c);
		gridy++;

		// padding
		c.gridx = 0;
		c.gridy = gridy;
		c.weighty = 1.0;
		JLabel jt = new JLabel();
		add(jt, c);
		gridy++;
	}

	private Color changeColorFont(final Color c) {
		Color color = JColorChooser.showDialog(null, Language.getLabel(1), c);// Color selection
		if (color == null) {
			color = c;
		}
		return color;
	}

	public static tipusDades getTypeData() {
		if (rbDistances.isSelected()) {
			return tipusDades.DISTANCIA;
		} else {
			return tipusDades.PESO;
		}
	}

	public static metodo getMethod() {
		return metodo.values()[cbMethod.getSelectedIndex()];
	}

	public static int getPrecision() {
		int precision = -1;
		String str;

		str = txtPrecision.getText().trim();
		if (str.equals("")) {
			precision = -1;
		} else {
			try {
				precision = Integer.parseInt(str);
				if (precision < 0) {
					precision = -1;
				}
			} catch (NumberFormatException e) {
				precision = -1;
			}
		}
		return precision;
	}

	public static void setPrecision(final int precision) {
		txtPrecision.setText(String.valueOf(precision));
		Jpan_btn.precisionCorrect = true;
	}

	private static double getMinValue() {
		double minValue = -1;
		String str;

		str = txtAxisMin.getText().trim();
		if (str.equals("")) {
			minValue = -1;
		} else {
			try {
				minValue = Double.parseDouble(str);
			} catch (NumberFormatException e) {
				minValue = -1;
			}
		}
		return minValue;
	}

	private static void setMinValue(final double minValue) {
		txtAxisMin.setText(String.valueOf(minValue));
		Jpan_btn.axisMinCorrect = true;
	}

	private static double getMaxValue() {
		double maxValue = -1;
		String str;

		str = txtAxisMax.getText().trim();
		if (str.equals("")) {
			maxValue = -1;
		} else {
			try {
				maxValue = Double.parseDouble(str);
			} catch (NumberFormatException e) {
				maxValue = -1;
			}
		}
		return maxValue;
	}

	private static void setMaxValue(final double maxValue) {
		txtAxisMax.setText(String.valueOf(maxValue));
		Jpan_btn.axisMaxCorrect = true;
	}

	private static double getTicksSeparation() {
		double ticksSeparation = -1;
		String str;

		str = txtAxisSeparation.getText().trim();
		if (str.equals("")) {
			ticksSeparation = -1;
		} else {
			try {
				ticksSeparation = Double.parseDouble(str);
				if (ticksSeparation <= 0) {
					ticksSeparation = -1;
				}
			} catch (NumberFormatException e) {
				ticksSeparation = -1;
			}
		}
		return ticksSeparation;
	}

	private static void setTicksSeparation(final double ticksSeparation) {
		txtAxisSeparation.setText(String.valueOf(ticksSeparation));
		Jpan_btn.axisSeparationCorrect = true;
	}

	private static int getLabelsEvery() {
		int labelsEvery = -1;
		String str;

		str = txtAxisEvery.getText().trim();
		if (str.equals("")) {
			labelsEvery = -1;
		} else {
			try {
				labelsEvery = Integer.parseInt(str);
				if (labelsEvery < 0) {
					labelsEvery = -1;
				}
			} catch (NumberFormatException e) {
				labelsEvery = -1;
			}
		}
		return labelsEvery;
	}

	private static void setLabelsEvery(final int labelsEvery) {
		txtAxisEvery.setText(String.valueOf(labelsEvery));
		Jpan_btn.axisEveryCorrect = true;
	}

	private static int getLabelsDecimals() {
		int labelsDecimals = -1;
		String str;

		str = txtAxisDecimals.getText().trim();
		if (str.equals("")) {
			labelsDecimals = -1;
		} else {
			try {
				labelsDecimals = Integer.parseInt(str);
				if (labelsDecimals < 0) {
					labelsDecimals = -1;
				}
			} catch (NumberFormatException e) {
				labelsDecimals = -1;
			}
		}
		return labelsDecimals;
	}

	private static void setLabelsDecimals(final int labelsDecimals) {
		txtAxisDecimals.setText(String.valueOf(labelsDecimals));
		Jpan_btn.axisDecimalsCorrect = true;
	}

	public static CfgPanelMenu getCfgPanel() {
		final CfgPanelMenu cfg = new CfgPanelMenu();

		// DATA
		if (rbDistances.isSelected()) {
			cfg.setTipusDades(tipusDades.DISTANCIA);
		} else {
			cfg.setTipusDades(tipusDades.PESO);
		}

		// METHOD
		cfg.setMetodo(metodo.values()[cbMethod.getSelectedIndex()]);

		// PRECISION
		cfg.setDecimalsSignificatius(getPrecision());

		// TREE
		cfg.setOrientacioDendograma(Orientation.values()[cbTreeOrientation
				.getSelectedIndex()]);

		// BANDS
		cfg.setFranjaVisible(chkBands.isSelected());
		cfg.setColorMarge(colorBands);

		// NODES
		cfg.setNomsVisibles(chkNodesLabels.isSelected());
		cfg.setRadiBullets(Integer.parseInt((String) cbNodesSize
				.getSelectedItem()));
		cfg.setRotNoms(rotacioNoms.values()[cbNodesOrientation
				.getSelectedIndex()]);
		cfg.setFontNoms(fontNodesLabels);
		cfg.setColorNoms(colorNodesLabels);

		// AXIS
		cfg.setEscalaVisible(chkAxis.isSelected());
		cfg.setEtiquetaEscalaVisible(chkAxisLabels.isSelected());
		cfg.setColorEix(colorAxis);
		cfg.setColorLabels(colorAxisLabels);

		// FONT AXIS LABELS
		cfg.setFontLabels(fontAxisLabels);

		// MIN and MAX
		cfg.setValMin(getMinValue());
		cfg.setValMax(getMaxValue());

		// TICKS SEPARATION
		cfg.setIncrement(getTicksSeparation());

		// LABELS EVERY ...
		cfg.setTics(getLabelsEvery());

		// DECIMALS
		cfg.setAxisDecimals(getLabelsDecimals());

		return cfg;
	}

	public static void getConfigPanel(InternalFrameData ifd) {
		// TYPE OF DATA
		if (rbDistances.isSelected()) {
			ifd.setTypeData(tipusDades.DISTANCIA);
		} else {
			ifd.setTypeData(tipusDades.PESO);
		}

		// METHOD
		ifd.setMethod(metodo.values()[cbMethod.getSelectedIndex()]);

		// PRECISION
		ifd.setPrecision(getPrecision());

		// TREE
		ifd.setOrientacioDendograma(Orientation.values()[cbTreeOrientation
				.getSelectedIndex()]);

		// BANDS
		ifd.setFranjaVisible(chkBands.isSelected());
		ifd.setColorMarge(colorBands);

		// NODES
		ifd.setRadiBullets(Integer.parseInt((String) cbNodesSize
				.getSelectedItem()));
		ifd.setNomsVisibles(chkNodesLabels.isSelected());
		ifd.setFontNoms(fontNodesLabels);
		ifd.setColorNoms(colorNodesLabels);
		ifd.setRotNoms(rotacioNoms.values()[cbNodesOrientation
				.getSelectedIndex()]);

		// AXIS
		ifd.setEscalaVisible(chkAxis.isSelected());
		ifd.setColorEix(colorAxis);

		// MIN and MAX
		ifd.setValMin(getMinValue());
		ifd.setValMax(getMaxValue());

		// TICKS SEPARATION
		ifd.setIncrement(getTicksSeparation());

		// AXIS LABELS
		ifd.setEtiquetaEscalaVisible(chkAxisLabels.isSelected());
		ifd.setFontLabels(fontAxisLabels);
		ifd.setColorLabels(colorAxisLabels);

		// LABELS EVERY ...
		ifd.setTics(getLabelsEvery());

		// DECIMALS
		ifd.setAxisDecimals(getLabelsDecimals());
	}

	public static void setConfigPanel(final InternalFrameData ifd) {
		// Type of measure
		rbDistances.setSelected(ifd.getTypeData().equals(tipusDades.DISTANCIA));
		rbWeights.setSelected(ifd.getTypeData().equals(tipusDades.PESO));

		// Clustering algorithm
		cbMethod.setSelectedIndex(ifd.getMethod().ordinal());

		// Precision
		setPrecision(ifd.getPrecision());

		// Orientation of the tree
		cbTreeOrientation.setSelectedIndex(ifd.getOrientacioDendograma()
				.ordinal());

		// Bands
		chkBands.setSelected(ifd.isFranjaVisible());
		colorBands = ifd.getColorMarge();

		// Nodes
		cbNodesSize.setSelectedItem(Integer.toString(ifd.getRadiBullets()));
		chkNodesLabels.setSelected(ifd.isNomsVisibles());
		cbNodesOrientation.setSelectedIndex(ifd.getRotNoms().ordinal());
		fontNodesLabels = ifd.getFontNoms();
		colorNodesLabels = ifd.getColorNoms();

		// Axis
		chkAxis.setSelected(ifd.isEscalaVisible());
		chkAxisLabels.setSelected(ifd.isEtiquetaEscalaVisible());
		colorAxis = ifd.getColorEix();
		colorAxisLabels = ifd.getColorLabels();

		// Font axis labels
		fontAxisLabels = ifd.getFontLabels();

		// MIN
		setMinValue(ifd.getValMin());

		// MAX
		setMaxValue(ifd.getValMax());

		// Ticks separation
		setTicksSeparation(ifd.getIncrement());

		// Labels every ... ticks
		setLabelsEvery(ifd.getTics());

		// Labels decimals
		setLabelsDecimals(ifd.getAxisDecimals());
	}

	public static void clearConfigPanel() {
		txtPrecision.setText("");
		Jpan_btn.precisionCorrect = false;
		txtAxisMin.setText("");
		Jpan_btn.axisMinCorrect = false;
		txtAxisMax.setText("");
		Jpan_btn.axisMaxCorrect = false;
		txtAxisSeparation.setText("");
		Jpan_btn.axisSeparationCorrect = false;
		txtAxisEvery.setText("");
		Jpan_btn.axisEveryCorrect = false;
		txtAxisDecimals.setText("");
		Jpan_btn.axisDecimalsCorrect = false;
	}

	public static void ajustaValors(final Config cfg) {
		int precision;
		double min, max, separation;
		SmartAxis sa;

		precision = cfg.getPrecision();
		min = cfg.getBaseDendograma();
		max = cfg.getCimDendograma();
		sa = new SmartAxis(min, max);
		min = sa.smartMin();
		max = sa.smartMax();
		separation = sa.smartTicksSize();
		setPrecision(precision);
		cfg.getConfigMenu().setValMin(min);
		cfg.getConfigMenu().setValMax(max);
		setMinAxisValue(min);
		setMaxAxisValue(max);
		setTicksSeparation(separation);
		setLabelsEvery(1);
		setLabelsDecimals(cfg.getPrecision());
	}

	private static void setMinAxisValue(double min) {
		Locale loc = new Locale("en");
		NumberFormat nf = NumberFormat.getInstance(loc);
		nf.setGroupingUsed(false);
		String num = nf.format(min);
		txtAxisMin.setText(num);
		Jpan_btn.axisMinCorrect = true;
	}

	private static void setMaxAxisValue(double max) {
		Locale loc = new Locale("en");
		NumberFormat nf = NumberFormat.getInstance(loc);
		nf.setGroupingUsed(false);
		String num = nf.format(max);
		txtAxisMax.setText(num);
		Jpan_btn.axisMaxCorrect = true;
	}

	// ACTION EVENTS

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("orientacio_dendo")) {
			pimg.setImatge(Orientation.values()[cbTreeOrientation
					.getSelectedIndex()]);
		} else if (evt.getActionCommand().equals("color_marge")) {
			colorBands = this.changeColorFont(colorBands);
		} else if (evt.getActionCommand().equals("font_noms")) {
			final FontSelection f = new FontSelection(fontNodesLabels);
			f.setVisible(true);
			fontNodesLabels = f.getNewFont();
		} else if (evt.getActionCommand().equals("color_noms")) {
			colorNodesLabels = this.changeColorFont(colorNodesLabels);
		} else if (evt.getActionCommand().equals("color_axis")) {
			colorAxis = this.changeColorFont(colorAxis);
		} else if (evt.getActionCommand().equals("font_axis")) {
			final FontSelection f = new FontSelection(fontAxisLabels);
			f.setVisible(true);
			fontAxisLabels = f.getNewFont();
		} else if (evt.getActionCommand().equals("color_label")) {
			colorAxisLabels = this.changeColorFont(colorAxisLabels);
		} else {
			FesLog.LOG.warning(Language.getLabel(47) + ": " + evt.toString());
		}
	}

	// FOCUS EVENTS

	@Override
	public void focusGained(FocusEvent evt) {
	}

	@Override
	public void focusLost(FocusEvent evt) {
		if (!evt.isTemporary()) {
			if (evt.getComponent().getName().equals("precision")) {
				Jpan_btn.precisionCorrect = checkPrecision();
			} else if (evt.getComponent().getName().equals("axis_min")) {
				Jpan_btn.axisMinCorrect = checkMinValue();
			} else if (evt.getComponent().getName().equals("axis_max")) {
				Jpan_btn.axisMaxCorrect = checkMaxValue();
			} else if (evt.getComponent().getName().equals("axis_separation")) {
				Jpan_btn.axisSeparationCorrect = checkTicksSeparation();
			} else if (evt.getComponent().getName().equals("axis_every")) {
				Jpan_btn.axisEveryCorrect = checkLabelsEvery();
			} else if (evt.getComponent().getName().equals("axis_decimals")) {
				Jpan_btn.axisDecimalsCorrect = checkLabelsDecimals();
			}
			Jpan_btn.enableUpdate();
		}
	}

	private boolean checkPrecision() {
		boolean correct = true;
		String str = txtPrecision.getText().trim();
		if (str.equals("")) {
			correct = false;
			showError(Language.getLabel(2));
		} else {
			try {
				int precision = Integer.parseInt(str);
				if (precision < 0) {
					correct = false;
					showError(Language.getLabel(2));
				}
			} catch (NumberFormatException e) {
				correct = false;
				showError(Language.getLabel(2));
			}
		}
		return correct;
	}

	private boolean checkMinValue() {
		boolean correct = true;
		String str = txtAxisMin.getText().trim();
		if (str.equals("")) {
			correct = false;
			showError(Language.getLabel(3));
		} else {
			try {
				double minValue = Double.parseDouble(str);
				if (minValue > getMaxValue()) {
					correct = false;
					showError(Language.getLabel(19));
				}
			} catch (NumberFormatException e) {
				correct = false;
				showError(Language.getLabel(3));
			}
		}
		return correct;
	}

	private boolean checkMaxValue() {
		boolean correct = true;
		String str = txtAxisMax.getText().trim();
		if (str.equals("")) {
			correct = false;
			showError(Language.getLabel(4));
		} else {
			try {
				double maxValue = Double.parseDouble(str);
				if (maxValue < getMinValue()) {
					correct = false;
					showError(Language.getLabel(19));
				}
			} catch (NumberFormatException e) {
				correct = false;
				showError(Language.getLabel(4));
			}
		}
		return correct;
	}

	private boolean checkTicksSeparation() {
		boolean correct = true;
		String str = txtAxisSeparation.getText().trim();
		if (str.equals("")) {
			correct = false;
			showError(Language.getLabel(5));
		} else {
			try {
				double ticksSeparation = Double.parseDouble(str);
				if (ticksSeparation <= 0) {
					correct = false;
					showError(Language.getLabel(5));
				}
			} catch (NumberFormatException e) {
				correct = false;
				showError(Language.getLabel(5));
			}
		}
		return correct;
	}

	private boolean checkLabelsEvery() {
		boolean correct = true;
		String str = txtAxisEvery.getText().trim();
		if (str.equals("")) {
			correct = false;
			showError(Language.getLabel(6));
		} else {
			try {
				int labelsEvery = Integer.parseInt(str);
				if (labelsEvery < 0) {
					correct = false;
					showError(Language.getLabel(6));
				}
			} catch (NumberFormatException e) {
				correct = false;
				showError(Language.getLabel(6));
			}
		}
		return correct;
	}

	private boolean checkLabelsDecimals() {
		boolean correct = true;
		String str = txtAxisDecimals.getText().trim();
		if (str.equals("")) {
			correct = false;
			showError(Language.getLabel(50));
		} else {
			try {
				int labelsDecimals = Integer.parseInt(str);
				if (labelsDecimals < 0) {
					correct = false;
					showError(Language.getLabel(50));
				}
			} catch (NumberFormatException e) {
				correct = false;
				showError(Language.getLabel(50));
			}
		}
		return correct;
	}

	private static void showError(String message) {
		JOptionPane.showMessageDialog(null, message, Language.getLabel(7),
				JOptionPane.ERROR_MESSAGE);
	}

	// INTERNAL FRAME EVENTS

	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameIconified(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
	}

}
