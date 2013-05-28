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

package edu.ucla.stat.SOCR.analyses.util.inicial;

import java.awt.Color;
import java.awt.Font;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * It defines all the parameters of the application and it initializes them
 * with the values in the file dendo.ini, or with default values if the file is missing
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Parametres_Inicials {

	private static String sPath_idioma = "";

	private static double marco = 15;

	private static int width_frmPrincipal = 800;

	private static int height_frmPrincipal = 500;

	private static double radi = 5;

	private static double factorEscala = 10.0;

	private static int factorTics = 10;

	private static int width_frmDesk = 400;

	private static int height_frmDesk = 400;

	private static String titolWin = "MultiDendrograms";

	private static String titolDesk = "MultiDendrograms";

	private static Color color_title_font = Color.BLACK;
	private static Color color_title_background = Color.GRAY;
	private static Font fontMenuTitle = new Font("Serif", Font.BOLD, 10);

	private static Color color_jtxt_background = Color.LIGHT_GRAY;
	private static Color color_jtxt_font = Color.BLACK;
	private static Font fontMenuTXT = new Font("Serif", Font.BOLD, 10);

	private static Color color_jarea_background = Color.LIGHT_GRAY;
	private static Color color_jarea_font = Color.BLACK;
	private static Font fontMenuAREA = new Font("Serif", Font.BOLD, 10);

	private static Color color_cb_background = Color.LIGHT_GRAY;
	private static Color color_cb_font = Color.BLACK;
	private static Font fontMenuCB = new Font("Serif", Font.BOLD, 10);

	private static Color color_label_font = Color.BLACK;
	private static Font fontMenuLabel = new Font("Serif", Font.BOLD, 10);

	private static Color color_chk_font = Color.BLACK;

	private static Font fontMenuCHK = new Font("Serif", Font.BOLD, 10);

	private static Color colorMarge = Color.LIGHT_GRAY;

	private static Color color_opt_font = Color.BLACK;
	private static Font fontMenuOPT = new Font("Serif", Font.BOLD, 10);

	private static Font fontNames = new Font("Serif", Font.BOLD, 10);;

	private static Font fontAxis = new Font("Serif", Font.BOLD, 10);

	private static Color colorNames = Color.BLACK;

	private static Color colorAxis = Color.BLACK;;

	private static Color colorLabels = Color.BLACK;

	public Parametres_Inicials() {
		FesLog.LOG.info("Creat nou objecte");
	}

	public static Color getColorMarge() {
		return Parametres_Inicials.colorMarge;
	}

	public static void setColorMarge(final Color color) {
		Parametres_Inicials.colorMarge = color;
	}

	public static int getHeight_frmPrincipal() {
		return Parametres_Inicials.height_frmPrincipal;
	}

	public static double getRadi() {
		return Parametres_Inicials.radi;
	}

	public void setRadi(final double radi) {
		Parametres_Inicials.radi = radi;
	}

	public static double getFactorEscala() {
		return factorEscala;
	}

	public static void setFactorEscala(double factorEscala) {
		Parametres_Inicials.factorEscala = factorEscala;
	}

	public static int getFactorTics() {
		return factorTics;
	}

	public static void setFactorTics(int factorTics) {
		Parametres_Inicials.factorTics = factorTics;
	}

	public static String getTitolWin() {
		return Parametres_Inicials.titolWin;
	}

	public void setTitolWin(final String titol) {
		Parametres_Inicials.titolWin = titol;
	}

	public void setWidth_frmDesk(final int width_frmArrel) {
		Parametres_Inicials.width_frmDesk = width_frmArrel;
	}

	public static int getWidth_frmPrincipal() {
		return Parametres_Inicials.width_frmPrincipal;
	}

	public static String getTitolDesk() {
		return Parametres_Inicials.titolDesk;
	}

	public static void setTitolDesk(final String titolDesk) {
		Parametres_Inicials.titolDesk = titolDesk;
	}

	public static Color getColor_title_background() {
		return Parametres_Inicials.color_title_background;
	}

	public static void setColor_title_background(
			final Color color_title_background) {
		Parametres_Inicials.color_title_background = color_title_background;
	}

	public static Color getColor_title_font() {
		return Parametres_Inicials.color_title_font;
	}

	public static void setColor_title_font(final Color color_title_font) {
		Parametres_Inicials.color_title_font = color_title_font;
	}

	public static Font getFontMenuTitle() {
		return Parametres_Inicials.fontMenuTitle;
	}

	public static void setFontMenuTitle(final Font fontMenuTitle) {
		Parametres_Inicials.fontMenuTitle = fontMenuTitle;
	}

	public static double getMarco() {
		return Parametres_Inicials.marco;
	}

	public static void setMarco(final double marco) {
		Parametres_Inicials.marco = marco;
	}

	public static String getSPath_idioma() {
		return Parametres_Inicials.sPath_idioma;
	}

	public static void setSPath_idioma(final String path_idioma) {
		Parametres_Inicials.sPath_idioma = path_idioma;
	}

	public static Color getColorAxis() {
		return Parametres_Inicials.colorAxis;
	}

	public static void setColorAxis(final Color colorAxis) {
		Parametres_Inicials.colorAxis = colorAxis;
	}

	public static Color getColorLabels() {
		return Parametres_Inicials.colorLabels;
	}

	public static void setColorLabels(final Color colorLabels) {
		Parametres_Inicials.colorLabels = colorLabels;
	}

	public static Color getColorNames() {
		return Parametres_Inicials.colorNames;
	}

	public static void setColorNames(final Color colorNames) {
		Parametres_Inicials.colorNames = colorNames;
	}

	public static Font getFontAxis() {
		return Parametres_Inicials.fontAxis;
	}

	public static void setFontAxis(final Font fontAxis) {
		Parametres_Inicials.fontAxis = fontAxis;
	}

	public static Font getFontNames() {
		return Parametres_Inicials.fontNames;
	}

	public static void setFontNames(final Font fontNames) {
		Parametres_Inicials.fontNames = fontNames;
	}

	public static Color getColor_jarea_background() {
		return Parametres_Inicials.color_jarea_background;
	}

	public static void setColor_jarea_background(
			final Color color_jarea_background) {
		Parametres_Inicials.color_jarea_background = color_jarea_background;
	}

	public static Color getColor_jarea_font() {
		return Parametres_Inicials.color_jarea_font;
	}

	public static void setColor_jarea_font(final Color color_jarea_font) {
		Parametres_Inicials.color_jarea_font = color_jarea_font;
	}

	public static Font getFontMenuAREA() {
		return Parametres_Inicials.fontMenuAREA;
	}

	public static void setFontMenuAREA(final Font fontMenuAREA) {
		Parametres_Inicials.fontMenuAREA = fontMenuAREA;
	}

	public static Color getColor_jtxt_background() {
		return Parametres_Inicials.color_jtxt_background;
	}

	public static void setColor_jtxt_background(
			final Color color_jtxt_background) {
		Parametres_Inicials.color_jtxt_background = color_jtxt_background;
	}

	public static Color getColor_jtxt_font() {
		return Parametres_Inicials.color_jtxt_font;
	}

	public static void setColor_jtxt_font(final Color color_jtxt_font) {
		Parametres_Inicials.color_jtxt_font = color_jtxt_font;
	}

	public static Font getFontMenuTXT() {
		return Parametres_Inicials.fontMenuTXT;
	}

	public static void setFontMenuTXT(final Font fontMenuTXT) {
		Parametres_Inicials.fontMenuTXT = fontMenuTXT;
	}

	public static Color getColor_opt_font() {
		return Parametres_Inicials.color_opt_font;
	}

	public static void setColor_opt_font(final Color color_opt_font) {
		Parametres_Inicials.color_opt_font = color_opt_font;
	}

	public static Font getFontMenuOPT() {
		return Parametres_Inicials.fontMenuOPT;
	}

	public static void setFontMenuOPT(final Font fontMenuOPT) {
		Parametres_Inicials.fontMenuOPT = fontMenuOPT;
	}

	public static Color getColor_cb_background() {
		return Parametres_Inicials.color_cb_background;
	}

	public static void setColor_cb_background(final Color color_cb_background) {
		Parametres_Inicials.color_cb_background = color_cb_background;
	}

	public static Color getColor_cb_font() {
		return Parametres_Inicials.color_cb_font;
	}

	public static void setColor_cb_font(final Color color_cb_font) {
		Parametres_Inicials.color_cb_font = color_cb_font;
	}

	public static Font getFontMenuCB() {
		return Parametres_Inicials.fontMenuCB;
	}

	public static void setFontMenuCB(final Font fontMenuCB) {
		Parametres_Inicials.fontMenuCB = fontMenuCB;
	}

	public static Color getColor_chk_font() {
		return Parametres_Inicials.color_chk_font;
	}

	public static void setColor_chk_font(final Color color_chk_font) {
		Parametres_Inicials.color_chk_font = color_chk_font;
	}

	public static Font getFontMenuCHK() {
		return Parametres_Inicials.fontMenuCHK;
	}

	public static void setFontMenuCHK(final Font fontMenuCHK) {
		Parametres_Inicials.fontMenuCHK = fontMenuCHK;
	}

	public static Color getColor_label_font() {
		return Parametres_Inicials.color_label_font;
	}

	public static void setColor_label_font(final Color color_label_font) {
		Parametres_Inicials.color_label_font = color_label_font;
	}

	public static Font getFontMenuLabel() {
		return Parametres_Inicials.fontMenuLabel;
	}

	public static void setFontMenuLabel(final Font fontMenuLabel) {
		Parametres_Inicials.fontMenuLabel = fontMenuLabel;
	}

	public static int getWidth_frmDesk() {
		return Parametres_Inicials.width_frmDesk;
	}

	public static int getHeight_frmDesk() {
		return Parametres_Inicials.height_frmDesk;
	}

	public void setParametres(final AlmacenPropiedades ap) {
		String s_tmp;
		Font f;
		Color c;

		
			Parametres_Inicials.sPath_idioma = "";

		
			Parametres_Inicials.height_frmPrincipal = 690;
		
		
			Parametres_Inicials.width_frmPrincipal = 800;


		
			Parametres_Inicials.radi = 5.0;


		
			Parametres_Inicials.height_frmDesk = 400;


		
			Parametres_Inicials.width_frmDesk = 400;

		c = this.getColor("colorMarge");
		if (c != null) {
			Parametres_Inicials.colorMarge = c;
		}

		f = this.getFont("fontNames");
		if (f != null) {
			Parametres_Inicials.fontNames = f;
		}
		f = this.getFont("fontAxis");
		if (f != null) {
			Parametres_Inicials.fontAxis = f;
		}

		c = this.getColor("colorNames");
		if (c != null) {
			Parametres_Inicials.colorNames = c;
		}
		c = this.getColor("colorAxis");
		if (c != null) {
			Parametres_Inicials.colorAxis = c;
		}
		c = this.getColor("colorLabels");
		if (c != null) {
			Parametres_Inicials.colorLabels = c;
		}

		c = this.getColor("color_title_font");
		if (c != null) {
			Parametres_Inicials.color_title_font = c;
		}
		c = this.getColor("color_title_background");
		if (c != null) {
			Parametres_Inicials.color_title_background = c;
		}

		f = this.getFont("fontMenuTitle");
		if (f != null) {
			Parametres_Inicials.fontMenuTitle = f;
		}

		c = this.getColor("color_jtxt_font");
		if (c != null) {
			Parametres_Inicials.color_jtxt_font = c;
		}
		c = this.getColor("color_jtxt_background");
		if (c != null) {
			Parametres_Inicials.color_jtxt_background = c;
		}

		f = this.getFont("fontMenuTXT");
		if (f != null) {
			Parametres_Inicials.fontMenuTXT = f;
		}

		c = this.getColor("color_label_font");
		if (c != null) {
			Parametres_Inicials.color_label_font = c;
		}

		f = this.getFont("fontMenuLabel");
		if (f != null) {
			Parametres_Inicials.fontMenuLabel = f;
		}

		c = this.getColor("color_chk_font");
		if (c != null) {
			Parametres_Inicials.color_chk_font = c;
		}

		f = this.getFont("fontMenuCHK");
		if (f != null) {
			Parametres_Inicials.fontMenuCHK = f;
		}

		c = this.getColor("color_opt_font");
		if (c != null) {
			Parametres_Inicials.color_opt_font = c;
		}

		f = this.getFont("fontMenuOPT");
		if (f != null) {
			Parametres_Inicials.fontMenuOPT = f;
		}

		c = this.getColor("color_cb_font");
		if (c != null) {
			Parametres_Inicials.color_cb_font = c;
		}
		c = this.getColor("color_cb_background");
		if (c != null) {
			Parametres_Inicials.color_cb_background = c;
		}

		f = this.getFont("fontMenuCB");
		if (f != null) {
			Parametres_Inicials.fontMenuCB = f;
		}
		c = this.getColor("color_jarea_font");
		if (c != null) {
			Parametres_Inicials.color_jarea_font = c;
		}
		c = this.getColor("color_jarea_background");
		if (c != null) {
			Parametres_Inicials.color_jarea_background = c;
		}

		f = this.getFont("fontMenuAREA");
		if (f != null) {
			Parametres_Inicials.fontMenuAREA = f;
		}
	}

	private Font getFont(final String label) {
		Font f = null;
		
		return f;
	}

	private Color getColor(final String label) {
		
		Color c = null;

		return c;
	}

}