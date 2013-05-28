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

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Font selection dialog
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class FontSelection extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Font font;
	private final Font font_inicial;
	private JComboBox cbMida, cbFonts;
	private JCheckBox chkBold, chkItalic, chkPlain;
	private final JButton btnAceptar, btnCancelar;
	private JLabel lbl;

	private void inicialitzar() {
		final String mides[] = { "6", "8", "10", "11", "12", "14", "16", "18" };
		cbMida = new JComboBox(mides);
		cbMida.setSelectedItem(String.valueOf(font.getSize()));

		final String[] fuentes = FontSelection.getFontsSistema();
		cbFonts = new JComboBox(fuentes);
		cbFonts.setSelectedItem(font.getName());

		chkBold = new JCheckBox(Language.getLabel(54));// Negreta
		chkBold.setSelected(font.isBold());

		chkItalic = new JCheckBox(Language.getLabel(55)); // Italica
		chkItalic.setSelected(font.isItalic());

		chkPlain = new JCheckBox(Language.getLabel(56)); // Normal
		chkPlain.setSelected(font.isPlain());

		lbl = new JLabel(Language.getLabel(57)); // Aquesta es la font escollida
		lbl.setAlignmentX(SwingConstants.CENTER);
		lbl.setFont(font);
	}

	private static String[] getFontsSistema() {
		final GraphicsEnvironment env = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		return env.getAvailableFontFamilyNames();
	}

	public FontSelection(final Font fnt) {
		super();
		this.setTitle(Language.getLabel(58)); // Selected font
		this.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
		font = fnt;
		font_inicial = fnt;

		this.inicialitzar();

		final Container container = this.getContentPane();
		this.setLayout(new GridBagLayout());
		((JPanel) container).setBorder(BorderFactory
				.createTitledBorder(Language.getLabel(59)));
		final GridBagConstraints c = new GridBagConstraints();

		c.weightx = 1.0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		this.add(lbl, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		cbFonts.addActionListener(this);
		container.add(cbFonts, c);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 2;
		c.gridy = 2;
		cbMida.addActionListener(this);
		container.add(cbMida, c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridheight = 1;
		c.gridwidth = 1;
		chkBold.addActionListener(this);
		container.add(chkBold, c);

		c.gridx = 1;
		c.gridy = 3;
		c.gridheight = 1;
		c.gridwidth = 1;
		chkItalic.addActionListener(this);
		container.add(chkItalic, c);

		c.gridx = 2;
		c.gridy = 3;
		c.gridheight = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		chkPlain.addActionListener(this);
		container.add(chkPlain, c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridheight = 1;
		c.gridwidth = GridBagConstraints.RELATIVE;

		btnAceptar = new JButton(Language.getLabel(60)); // ACCEPT
		btnAceptar.addActionListener(this);
		container.add(btnAceptar, c);

		c.gridx = 2;
		c.gridy = 4;
		c.weightx = 1.0;
		c.weighty = 0;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridwidth = GridBagConstraints.REMAINDER;

		btnCancelar = new JButton(Language.getLabel(61)); // CANCEL
		btnCancelar.addActionListener(this);

		c.weightx = 1.0;
		c.weighty = 0;
		container.add(btnCancelar, c);

		this.pack();

		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ventana = getSize();
		setLocation((pantalla.width - ventana.width) / 2,
				(pantalla.height - ventana.height) / 2);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals(Language.getLabel(60))) {
			font = this.giveFont();
			this.dispose();
		} else if (e.getActionCommand().equals(Language.getLabel(61))) {
			font = font_inicial;
			this.dispose();
		} else {
			font = this.giveFont();
			lbl.setFont(font);

		}
	}

	public Font getNewFont() {
		return font;
	}

	private Font giveFont() {
		int style = 0;
		final int size = Integer.valueOf((String) cbMida.getSelectedItem());

		if (chkItalic.isSelected()) {
			style = Font.ITALIC;
		}
		if (chkBold.isSelected()) {
			style += Font.BOLD;
		}
		if (style == 0) {
			style = Font.PLAIN;
		}

		final String nomFont = (String) cbFonts.getSelectedItem();

		return new Font(nomFont, style, size);

	}
}
