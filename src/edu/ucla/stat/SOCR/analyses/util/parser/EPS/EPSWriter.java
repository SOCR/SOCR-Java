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

package edu.ucla.stat.SOCR.analyses.util.parser.EPS;

import edu.ucla.stat.SOCR.analyses.util.inicial.Language;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.JOptionPane;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Utils for the creation of the EPS file
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class EPSWriter {
	private static String EPSFileName;
	private static FileWriter eps_fw;
	private static BufferedWriter eps_bw;

	public static int xmin;
	public static int xmax;
	public static int ymin;
	public static int ymax;

	public EPSWriter(int x0, int y0, int x1, int y1) {
		xmin = x0;
		ymin = y0;
		xmax = x1;
		ymax = y1;
	}

	public static void open(String eps_path) {
		try {
			EPSFileName = eps_path;
			eps_fw = new FileWriter(EPSFileName);
			eps_bw = new BufferedWriter(eps_fw);
		} catch (FileNotFoundException e) {
			String msg = Language.getLabel(106);
			JOptionPane.showMessageDialog(null, msg, "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			String msg = Language.getLabel(107);
			JOptionPane.showMessageDialog(null, msg, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void close() {
		try {
			eps_bw.close();
		} catch (IOException e) {
			String msg = Language.getLabel(108) + EPSFileName;
			JOptionPane.showMessageDialog(null, msg, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void writeComments(String creator, String orientation) {
		// DATA I HORA
		Calendar c = Calendar.getInstance();

		int hora = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		int ss = c.get(Calendar.SECOND);
		int dia = c.get(Calendar.DATE);
		int mes = c.get(Calendar.MONTH);
		int any = c.get(Calendar.YEAR);

		String seg = Integer.toString(ss);
		if (ss < 10)
			seg = "0" + seg;

		// COMENTARIS
		writeLine("%!PS-Adobe-3.0 EPSF-3.0");
		writeLine("%%Title: (" + EPSFileName + ")");
		writeLine("%%Creator: (" + creator + ")");
		writeLine("%%CreationDate: (" + dia + "/" + mes + "/" + any + ")"
				+ " (" + hora + ":" + min + ":" + seg + ")");
		writeLine("%%BoundingBox: " + xmin + " " + ymin + " " + xmax + " "
				+ ymax);
		writeLine("%%Orientation: " + orientation);
		writeLine("%%Pages: 1");
		writeLine("%%EndComments");
		writeLine("");
	}

	public static void writeProlog(String prolog_path) {
		try {
			FileReader prolog_fr = new FileReader(prolog_path);
			BufferedReader prolog_bf = new BufferedReader(prolog_fr);

			String line;
			while ((line = prolog_bf.readLine()) != null)
				writeLine(line);

			prolog_bf.close();
		} catch (IOException e) {
			String msg = Language.getLabel(109) + EPSFileName;
			JOptionPane.showMessageDialog(null, msg, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void writeEnd() {
		// Mostrem la p�gina
		writeLine("showpage");
		writeLine("");
		writeLine("%%EOF");
	}

	public static void writeLine(String line) {
		try {
			eps_bw.write(line + "\n");
		} catch (IOException e) {
			String msg = Language.getLabel(108) + EPSFileName;
			JOptionPane.showMessageDialog(null, msg, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static String setLineWidth(float r) {
		return (r + " slw");
	}

	public static String setDashpattern(float x[], int n) {
		String str = new String();
		for (int i = 0; i < x.length; i++)
			str = str + x[i] + " ";
		return (str + n + " sdp");
	}

	public static String setRGBColor(float rr, float gg, float bb) {
		return (rr + " " + gg + " " + bb + " c");
	}

	public static String setGray(float ww) {
		return (ww + " g");
	}

	public static String stroke() {
		return ("s");
	}

	public static String moveTo(float x, float y) {
		return (x + " " + y + " m");
	}

	public static String lineTo(float x, float y) {
		return (x + " " + y + " l");
	}

	public static String rMoveTo(float dx, float dy) {
		return (dx + " " + dy + " rm");
	}

	public static String rLineTo(float dx, float dy) {
		return (dx + " " + dy + " rl");
	}

	public static String dupDup(float x, float y) {
		return (x + " " + y + " dup2");
	}

	public static String dupDupDup(float x, float y, float z) {
		return (x + " " + y + " " + z + " dup3");
	}

	public static String vectorSubstract(float x1, float y1, float x2, float y2) {
		return (x1 + " " + y1 + " " + x2 + " " + y2 + " vsub");
	}

	public static String dCircle(float x, float y, float r) {
		return (x + " " + y + " " + r + " dci");
	}

	public static String fCircle(float x, float y, float r) {
		return (x + " " + y + " " + r + " fci");
	}

	public static String oCircle(float x, float y, float r) {
		return (x + " " + y + " " + r + " oci");
	}

	public static String dSquare(float x, float y, float r) {
		return (x + " " + y + " " + r + " dsq");
	}

	public static String fSquare(float x, float y, float r) {
		return (x + " " + y + " " + r + " fsq");
	}

	public static String oSquare(float x, float y, float r) {
		return (x + " " + y + " " + r + " osq");
	}

	public static String dTriangleN(float x, float y, float r) {
		return (x + " " + y + " " + r + " dtn");
	}

	public static String fTriangleN(float x, float y, float r) {
		return (x + " " + y + " " + r + " ftn");
	}

	public static String oTriangleN(float x, float y, float r) {
		return (x + " " + y + " " + r + " otn");
	}

	public static String dTriangleW(float x, float y, float r) {
		return (x + " " + y + " " + r + " dtw");
	}

	public static String fTriangleW(float x, float y, float r) {
		return (x + " " + y + " " + r + " ftw");
	}

	public static String oTriangleW(float x, float y, float r) {
		return (x + " " + y + " " + r + " otw");
	}

	public static String dTriangleS(float x, float y, float r) {
		return (x + " " + y + " " + r + " dts");
	}

	public static String fTriangleS(float x, float y, float r) {
		return (x + " " + y + " " + r + " fts");
	}

	public static String oTriangleS(float x, float y, float r) {
		return (x + " " + y + " " + r + " ots");
	}

	public static String dTriangleE(float x, float y, float r) {
		return (x + " " + y + " " + r + " dte");
	}

	public static String fTriangleE(float x, float y, float r) {
		return (x + " " + y + " " + r + " fte");
	}

	public static String oTriangleE(float x, float y, float r) {
		return (x + " " + y + " " + r + " ote");
	}

	public static String dDiamond(float x, float y, float r) {
		return (x + " " + y + " " + r + " ddi");
	}

	public static String fDiamond(float x, float y, float r) {
		return (x + " " + y + " " + r + " fdi");
	}

	public static String oDiamond(float x, float y, float r) {
		return (x + " " + y + " " + r + " odi");
	}

	public static String dPlus(float x, float y, float r) {
		return (x + " " + y + " " + r + " dpl");
	}

	public static String fPlus(float x, float y, float r) {
		return (x + " " + y + " " + r + " fpl");
	}

	public static String oPlus(float x, float y, float r) {
		return (x + " " + y + " " + r + " opl");
	}

	public static String dTimes(float x, float y, float r) {
		return (x + " " + y + " " + r + " dxx");
	}

	public static String fTimes(float x, float y, float r) {
		return (x + " " + y + " " + r + " fxx");
	}

	public static String oTimes(float x, float y, float r) {
		return (x + " " + y + " " + r + " oxx");
	}

	public static String dSplat(float x, float y, float r) {
		return (x + " " + y + " " + r + " dsp");
	}

	public static String fSplat(float x, float y, float r) {
		return (x + " " + y + " " + r + " fsp");
	}

	public static String oSplat(float x, float y, float r) {
		return (x + " " + y + " " + r + " osp");
	}

	public static String dRectangle(float x1, float y1, float x2, float y2) {
		return (x1 + " " + y1 + " " + x2 + " " + y2 + " drc");
	}

	public static String fRectangle(float x1, float y1, float x2, float y2) {
		return (x1 + " " + y1 + " " + x2 + " " + y2 + " frc");
	}

	public static String oRectangle(float x1, float y1, float x2, float y2) {
		return (x1 + " " + y1 + " " + x2 + " " + y2 + " orc");
	}

	public static String dLine(float x1, float y1, float x2, float y2) {
		return (x1 + " " + y1 + " " + x2 + " " + y2 + " dli");
	}

	public static String fLine(float x1, float y1, float x2, float y2) {
		return (x1 + " " + y1 + " " + x2 + " " + y2 + " fli");
	}

	public static String oLine(float x1, float y1, float x2, float y2) {
		return (x1 + " " + y1 + " " + x2 + " " + y2 + " oli");
	}

	public static String scaleSetFont(String fn, float r) {
		return (fn + " " + r + " fss");
	}

	public static String boundingBoxText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") ssbb");
	}

	public static String lowercaseBottomTopText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") ssbt");
	}

	public static String lowerLeftText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") ssll");
	}

	public static String lowerMiddleText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") sslm");
	}

	public static String lowerRightText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") sslr");
	}

	public static String middleLeftText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") ssml");
	}

	public static String middleMiddleText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") ssmm");
	}

	public static String middleRightText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") ssmr");
	}

	public static String upperLeftText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") ssul");
	}

	public static String upperMiddleText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") ssum");
	}

	public static String upperRightText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") ssur");
	}

	public static String bottomLeftText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") ssbl");
	}

	public static String bottomMiddleText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") ssbm");
	}

	public static String bottomRightText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") ssbr");
	}

	public static String centerLeftText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") sscl");
	}

	public static String centerMiddleText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") sscm");
	}

	public static String centerRightText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") sscr");
	}

	public static String topLeftText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") sstl");
	}

	public static String topMiddleText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") sstm");
	}

	public static String topRightText(float x, float y, String str) {
		return (x + " " + y + " (" + str + ") sstr");
	}

	public static String lowerLeftTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssall");
	}

	public static String lowerMiddleTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssalm");
	}

	public static String lowerRightTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssalr");
	}

	public static String middleLeftTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssaml");
	}

	public static String middleMiddleTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssamm");
	}

	public static String middleRightTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssamr");
	}

	public static String upperLeftTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssaul");
	}

	public static String upperMiddleTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssaum");
	}

	public static String upperRightTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssaur");
	}

	public static String bottomLeftTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssabl");
	}

	public static String bottomMiddleTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssabm");
	}

	public static String bottomRightTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssabr");
	}

	public static String centerLeftTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssacl");
	}

	public static String centerMiddleTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssacm");
	}

	public static String centerRightTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssacr");
	}

	public static String topLeftTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssatl");
	}

	public static String topMiddleTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssatm");
	}

	public static String topRightTextRotated(float x, float y, float a,
			String str) {
		return (x + " " + y + " " + a + " (" + str + ") ssatr");
	}
}
