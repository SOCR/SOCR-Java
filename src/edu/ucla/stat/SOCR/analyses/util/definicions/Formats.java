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

package edu.ucla.stat.SOCR.analyses.util.definicions;

import edu.ucla.stat.SOCR.analyses.util.inicial.Parametres_Inicials;

import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Defines the formats for each component, which are loaded from the
 * configuration file or from the default parameters
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Formats {

	public static JCheckBox AtributCHKFont(final String caption) {
		final JCheckBox chk = new JCheckBox(caption);
		chk.setFont(Parametres_Inicials.getFontMenuCHK());
		chk.setForeground(Parametres_Inicials.getColor_chk_font());
		return chk;
	}

	public static JComboBox AttributCBFont(final String[] s) {
		final JComboBox cb = new JComboBox(s);
		cb.setFont(Parametres_Inicials.getFontMenuCB());
		cb.setBackground(Parametres_Inicials.getColor_cb_background());
		cb.setForeground(Parametres_Inicials.getColor_cb_font());
		return cb;
	}

	public static JLabel AtributTitleFont(final String caption) {
		final JLabel lbl = new JLabel(caption);
		lbl.setFont(Parametres_Inicials.getFontMenuTitle());
		lbl.setBackground(Parametres_Inicials.getColor_title_background());
		lbl.setForeground(Parametres_Inicials.getColor_title_font());
		lbl.setOpaque(true);
		return lbl;
	}

	public static JLabel AtributLabelFont(final String caption) {
		final JLabel lbl = new JLabel(caption);
		lbl.setFont(Parametres_Inicials.getFontMenuLabel());
		lbl.setForeground(Parametres_Inicials.getColor_label_font());
		lbl.setOpaque(false);
		return lbl;
	}

	public static JRadioButton AtributOPTFont(final String caption,
			final boolean selec) {
		final JRadioButton opt = new JRadioButton(caption, selec);
		opt.setFont(Parametres_Inicials.getFontMenuOPT());
		opt.setForeground(Parametres_Inicials.getColor_opt_font());
		return opt;
	}

	public static JTextField AtributTXTFont(final String caption,
			final int size, Locale loc) {
		final JTextField txt = new JTextField(caption, size);
		txt.setFont(Parametres_Inicials.getFontMenuTXT());
		txt.setBackground(Parametres_Inicials.getColor_jtxt_background());
		txt.setForeground(Parametres_Inicials.getColor_jtxt_font());
		txt.setLocale(loc);
		return txt;
	}

}
