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
 * Save dendrogram as Newick tree text file
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class ToNewick {

	private final Cluster root;
	private final int precision;
	private final tipusDades typeData;
	private final double heightBottom;
	private PrintWriter printWriter;

	public ToNewick(Cluster root, int precision, tipusDades typeData,
			double heightBottom) {
		this.root = root;
		this.precision = precision;
		this.typeData = typeData;
		this.heightBottom = heightBottom;
	}

	public void saveAsNewick(String sPath) throws Exception {
		File file;
		FileWriter fileWriter;
		String errMsg;

		file = new File(sPath);
		try {
			fileWriter = new FileWriter(file);
			printWriter = new PrintWriter(fileWriter);
			showCluster(root, root.getAlcada());
			printWriter.print(";");
			printWriter.close();
		} catch (Exception e) {
			errMsg = Language.getLabel(83);
			FesLog.LOG.throwing("ToNewick.java", "saveAsNewick()", e);
			throw new Exception(errMsg);
		}
	}

	private void showCluster(final Cluster cluster, final double heightParent)
			throws Exception {
		String name;
		double length;
		int n;

		if (cluster.getAlcada() == 0) {
			name = cluster.getNom();
			name = name.replace(' ', '?');
			name = name.replace(':', '?');
			name = name.replace(';', '?');
			name = name.replace(',', '?');
			name = name.replace('(', '?');
			name = name.replace(')', '?');
			name = name.replace('[', '?');
			name = name.replace(']', '?');
			printWriter.print(name);
			if (typeData.equals(tipusDades.DISTANCIA)) {
				length = MiMath.Arodoneix(heightParent - heightBottom,
						precision);
			} else {
				length = MiMath.Arodoneix(heightBottom - heightParent,
						precision);
			}
			if (length > 0) {
				printWriter.print(":" + length);
			}
		} else if (cluster.getFamily() > 1) {
			printWriter.print("(");
			for (n = 0; n < cluster.getFamily(); n++) {
				this.showCluster(cluster.getFill(n), cluster.getAlcada());
				if (n < cluster.getFamily() - 1) {
					printWriter.print(",");
				}
			}
			printWriter.print(")");
			if (typeData.equals(tipusDades.DISTANCIA)) {
				length = MiMath.Arodoneix(heightParent - cluster.getAlcada(),
						precision);
			} else {
				length = MiMath.Arodoneix(cluster.getAlcada() - heightParent,
						precision);
			}
			if (length > 0) {
				printWriter.print(":" + length);
			}
		}
	}

}
