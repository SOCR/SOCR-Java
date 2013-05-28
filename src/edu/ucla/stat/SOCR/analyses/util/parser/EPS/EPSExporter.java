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

import edu.ucla.stat.SOCR.analyses.util.moduls.frm.children.FrmPiz;
import edu.ucla.stat.SOCR.analyses.util.definicions.Config;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Save dendrogram to EPS
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class EPSExporter {
	private final EPSPiz piz;

	// Coordenades de la Bounding Box
	private final int xmin;
	private final int xmax;
	private final int ymin;
	private final int ymax;

	public EPSExporter(Config cfg, FrmPiz frmp, String path) {

		this.xmin = 72;
		this.ymin = 72;
		this.xmax = frmp.getWidth() + 72;
		this.ymax = frmp.getHeight() + 72;

		new EPSWriter(xmin, ymin, xmax, ymax);
		piz = new EPSPiz(frmp, cfg, xmax, ymax);
		writeEPS(path);
	}

	public void writeEPS(String eps_path) {
		EPSWriter.open(eps_path);
		EPSWriter.writeComments("David Torres Mart�n, Justo Montiel Borrull",
				"Portrait");
		EPSWriter.writeProlog("./ini/PSprolog.txt");
		this.writeBody();
		EPSWriter.writeEnd();
		EPSWriter.close();
	}

	public void writeBody() {
		EPSWriter.writeLine("");
		EPSWriter.writeLine("%%Page: 1 1");
		EPSWriter.writeLine(EPSWriter.setLineWidth(1.0f));
		EPSWriter.writeLine("[] 0 setdash");
		piz.dibuixa(); // Dibuixem la imatge
	}

}