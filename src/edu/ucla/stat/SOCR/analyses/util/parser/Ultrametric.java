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

package edu.ucla.stat.SOCR.analyses.util.parser;

import edu.ucla.stat.SOCR.analyses.util.importExport.StructIn;
import edu.ucla.stat.SOCR.analyses.util.inicial.FesLog;
import edu.ucla.stat.SOCR.analyses.util.inicial.Language;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Vector;

import edu.ucla.stat.SOCR.analyses.util.utils.DeviationMeasures;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Calculate and save ultrametric matrix
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Ultrametric {
	private PrintWriter pw;
	private static LinkedList<StructIn<String>> llista_orig;

	public void saveAsTXT(String path, int prec) throws Exception {
		try {
			File nfitx = new File(path);
			FileWriter fitx = new FileWriter(nfitx);
			pw = new PrintWriter(fitx);
			printUltrametrica(prec); // escriure matriu ultrametrica
			pw.close();
		} catch (Exception e) {
                    e.printStackTrace();
			String msg_err = Language.getLabel(81);
			FesLog.LOG.throwing("UltrametricTXT.java", "saveFile()", e);
			e.printStackTrace();
			throw new Exception(msg_err);
		}
	}

	private void printUltrametrica(int prec) {
		Double[][] matriu = Fig_Pizarra.mat_ultrametrica;
		String[] noms = Fig_Pizarra.noms;
		String cad = "";

		NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
		nf.setMinimumFractionDigits(prec);
		nf.setMaximumFractionDigits(prec);
		nf.setGroupingUsed(false);

		for (int i = 0; i < noms.length; i++)
			cad += noms[i] + "\t";
		pw.println(cad);

		int n = matriu.length;
		for (int i = 0; i < n; i++) {
			cad = "";
			for (int j = 0; j < n; j++)
				cad += nf.format(matriu[i][j]) + "\t";
			pw.println(cad);
		}
	}

	public void setLlistaOrig(LinkedList<StructIn<String>> matriu) {
		llista_orig = matriu;
	}

	private static Double[][] getMatriuOrig() {
		Vector<String> noms_orig = new Vector<String>();
		Double[][] matriu_orig;

		Iterator<StructIn<String>> it = llista_orig.iterator();
		while (it.hasNext()) {
			StructIn<String> si = it.next();
			if (!noms_orig.contains(si.getC1()))
				noms_orig.add(si.getC1());
			if (!noms_orig.contains(si.getC2()))
				noms_orig.add(si.getC2());
		}

		matriu_orig = new Double[noms_orig.size()][noms_orig.size()];
		int n = matriu_orig.length;
		int num = 0;
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				if (i == j)
					matriu_orig[i][j] = 0.0; // la diagonal es tot zeros
				else if (j > i) {
					matriu_orig[i][j] = llista_orig.get(num).getVal();
					matriu_orig[j][i] = llista_orig.get(num).getVal();
					num++;
				}
			}

		return matriu_orig;
	}

	public static double[] extractErrors() {
		double[] errors = new double[3];

		// Matriu original
		Double[][] matriu_orig = getMatriuOrig();

		// Matriu ultrametrica
		Double[][] matriu_ultram = Fig_Pizarra.mat_ultrametrica;

		DeviationMeasures dm = new DeviationMeasures();
		errors[0] = dm.getCopheneticCorrelation(matriu_orig, matriu_ultram);
		errors[1] = dm.getSquaredError(matriu_orig, matriu_ultram);
		errors[2] = dm.getAbsoluteError(matriu_orig, matriu_ultram);

		return errors;
	}

	public void showMatrix(Double[][] matriu) {
		String cad = "";
		int n = matriu.length;
		for (int i = 0; i < n; i++) {
			cad = "";
			for (int j = 0; j < n; j++)
				cad += matriu[i][j] + "\t";
			System.out.println(cad);
		}
	}
}
