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

package edu.ucla.stat.SOCR.analyses.util.moduls.frm.children;

import edu.ucla.stat.SOCR.analyses.util.inicial.Language;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.GroupLayout;

import edu.ucla.stat.SOCR.analyses.util.parser.Ultrametric;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Deviation measures dialog
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class DeviationMeasuresBox extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private javax.swing.JLabel cccLabel;
	private javax.swing.JLabel cccValue;
	private javax.swing.JLabel nmaeLabel;
	private javax.swing.JLabel nmaeValue;
	private javax.swing.JLabel nmseLabel;
	private javax.swing.JLabel nmseValue;
	private javax.swing.JButton okButton;

	private final Font font1 = new Font("Arial", Font.BOLD, 12);
	private final Font font2 = new Font("Arial", Font.PLAIN, 12);

	public DeviationMeasuresBox(Frame parent) {
		super(parent);
		initComponents();

		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ventana = getSize();
		setLocation((pantalla.width - ventana.width) / 2,
				(pantalla.height - ventana.height) / 2);
	}

	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(Language.getLabel(123));
		setModal(true);
		setName("DeviationMeasuresBox");
		setResizable(false);

		NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
		nf.setMinimumFractionDigits(6);
		nf.setMaximumFractionDigits(6);
		nf.setGroupingUsed(false);

		double[] errors = Ultrametric.extractErrors();
		String e1 = nf.format(errors[0]);
		String e2 = nf.format(errors[1]);
		String e3 = nf.format(errors[2]);

		cccLabel = new JLabel();
		nmseLabel = new JLabel();
		nmaeLabel = new JLabel();
		cccValue = new JLabel();
		nmseValue = new JLabel();
		nmaeValue = new JLabel();
		okButton = new JButton();

		cccLabel.setFont(font1);
		cccLabel.setText("Cophenetic Correlation Coefficient:");
		cccLabel.setName("cccLabel");

		nmseLabel.setFont(font1);
		nmseLabel.setText("Normalized Mean Squared Error:");
		nmseLabel.setName("nmseLabel");

		nmaeLabel.setFont(font1);
		nmaeLabel.setText("Normalized Mean Absolute Error:");
		nmaeLabel.setName("nmaeLabel");

		cccValue.setFont(font2);
		cccValue.setText(e1);
		cccValue.setName("cccValue");

		nmseValue.setFont(font2);
		nmseValue.setText(e2);
		nmseValue.setName("nmseValue");

		nmaeValue.setFont(font2);
		nmaeValue.setText(e3);
		nmaeValue.setName("nmaeValue");

		okButton.setText(Language.getLabel(60));
		okButton.setName("okButton");
		okButton.addActionListener(this);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING,
												false)
												.addComponent(cccLabel)
												.addComponent(nmseLabel)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		nmaeLabel)
																.addGap(11, 11,
																		11)))
								.addGap(18, 18, 18)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(cccValue)
												.addComponent(nmseValue)
												.addComponent(nmaeValue))
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap(119, Short.MAX_VALUE)
								.addComponent(okButton).addGap(113, 113, 113)));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		cccValue)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		nmseValue)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		nmaeValue))
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		cccLabel)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		nmseLabel)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		nmaeLabel)))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										40, Short.MAX_VALUE)
								.addComponent(okButton).addContainerGap()));

		pack();
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		dispose();
	}
}
