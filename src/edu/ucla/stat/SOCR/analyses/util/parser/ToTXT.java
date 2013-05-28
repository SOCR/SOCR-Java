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

import edu.ucla.stat.SOCR.analyses.util.inicial.FesLog;
import edu.ucla.stat.SOCR.analyses.util.inicial.Language;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import edu.ucla.stat.SOCR.analyses.util.tipus.tipusDades;
import edu.ucla.stat.SOCR.analyses.util.utils.MiMath;
import edu.ucla.stat.SOCR.analyses.util.definicions.Cluster;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Save dendrogram as text file
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class ToTXT {
	private final Cluster arrel;
	private String sPath;
	private PrintWriter pw;
	private final int prec;
	private final tipusDades tipDades;

	public ToTXT(Cluster arrel, int prec, tipusDades tip) {
		this.arrel = arrel;
		this.prec = prec;
		this.tipDades = tip;
	}

	public void saveAsTXT(String sPath) throws Exception {
		this.sPath = sPath;
		saveFile();
	}

	private void saveFile() throws Exception {
		File nfitx = new File(sPath);
		try {
			FileWriter fitx = new FileWriter(nfitx);
			pw = new PrintWriter(fitx);
			mostraCluster(arrel, 0);
			pw.close();
		} catch (Exception e) {
			String msg_err = Language.getLabel(83);
			FesLog.LOG.throwing("ToTXT.java", "saveFile()", e);
			throw new Exception(msg_err);
		}
	}

	private void mostraCluster(final Cluster cl, final int nivell)
			throws Exception {
		String cad;
		double pmin, pmax, tmp;
		cad = this.posaNivell(nivell);

		if (cl.getAlcada() == 0) {
			cad += "* " + cl.getNom();
		} else {

			pmin = cl.getAlcada();
			if (tipDades.equals(tipusDades.DISTANCIA)) {
				pmax = pmin + cl.getAglomeracio();
			} else {
				pmax = pmin - cl.getAglomeracio();
			}
			if (pmin > pmax) {
				tmp = pmin;
				pmin = pmax;
				pmax = tmp;
			}
			pmin = MiMath.Arodoneix(pmin, prec);
			pmax = MiMath.Arodoneix(pmax, prec);

			cad += "+ " + cl.getFills() + " [" + pmin + ", " + pmax + "]";
		}
		// write
		pw.println(cad);
		if (cl.getFamily() > 1) {
			for (int n = 0; n < cl.getFamily(); n++)
				this.mostraCluster(cl.getFill(n), nivell + 1);
		}
	}

	private String posaNivell(final int niv) {
		String cad = "";
		for (int n = 0; n < niv; n++) {
			cad += "\t";
		}
		return cad;
	}

}
