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
import edu.ucla.stat.SOCR.analyses.util.inicial.Parametres_Inicials;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import edu.ucla.stat.SOCR.analyses.util.utils.MiMath;
import edu.ucla.stat.SOCR.analyses.util.definicions.Cluster;
import edu.ucla.stat.SOCR.analyses.util.definicions.Config;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Dendrogram navigation window
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class PrnArrelHTML extends JDialog {

	private static final long serialVersionUID = 1L;
	private final Cluster branca;
	private final JTextArea txt;
	private final DefaultMutableTreeNode arrel;
	private final int prec;
	private final Config cfg;

	public PrnArrelHTML(final Cluster c, final int prec, final Config cfg)
			throws Exception {
		super();

		this.branca = c;
		this.prec = prec;
		this.cfg = cfg;

		this.arrel = new DefaultMutableTreeNode(Language.getLabel(62));
		final JTree arbol = new JTree(arrel);
		this.txt = new JTextArea();
		final JScrollPane p = new JScrollPane(arbol,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		try {
			this.Imprime();

			arbol.setExpandsSelectedPaths(true);
			txt.setEditable(false);
			this.add(p);
			arbol.expandPath(new TreePath(arrel));

			int x = 1;
			int rows = arbol.getRowCount();
			while ((rows - 1) >= x) {
				arbol.expandPath(arbol.getPathForRow(x));
				rows = arbol.getRowCount();
				x++;
			}

		} catch (Exception e) {
                    e.printStackTrace();
			String msg_err = e.getMessage() + "\n";
			msg_err += Language.getLabel(78);
			throw new Exception(msg_err);
		}

		final int Width_win = Parametres_Inicials.getWidth_frmDesk();
		final int height_win = Parametres_Inicials.getHeight_frmDesk();

		txt.setSize(Width_win, height_win);
		this.setVisible(true);
		this.setTitle(cfg.getFitxerDades().getNom() + " - "
				+ strMethod(cfg.getMethod().toString()));
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.pack();

		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ventana = getSize();
		setLocation((pantalla.width - ventana.width) / 2,
				(pantalla.height - ventana.height) / 2);
	}

	private String strMethod(String met) {
		if (met.equals("SINGLE_LINKAGE")) {
			return "Single Linkage";
		} else if (met.equals("COMPLETE_LINKAGE")) {
			return "Complete Linkage";
		} else if (met.equals("UNWEIGHTED_AVERAGE")) {
			return "Unweighted Average";
		} else if (met.equals("WEIGHTED_AVERAGE")) {
			return "Weighted Average";
		} else if (met.equals("UNWEIGHTED_CENTROID")) {
			return "Unweighted Centroid";
		} else if (met.equals("WEIGHTED_CENTROID")) {
			return "Weighted Centroid";
		} else if (met.equals("JOINT_BETWEEN_WITHIN")) {
			return "Joint Between-Within";
		} else {
			return "";
		}
	}

	private void Imprime() throws Exception {
		this.MostraBranca(branca, arrel);
	}

	private void MostraBranca(final Cluster c, final DefaultMutableTreeNode bra)
			throws Exception {
		double pmin, pmax, tmp;

		DefaultMutableTreeNode full;
		if (c.getFamily() == 1) {
			full = new DefaultMutableTreeNode("<html><b COLOR='#888888'>"
					+ c.getNom() + "</b></html>");
		} else {
			pmin = c.getAlcada();

			if (cfg.isTipusDistancia()) {
				pmax = pmin + c.getAglomeracio();
			} else {
				pmax = pmin - c.getAglomeracio();
			}
			if (pmin > pmax) {
				tmp = pmin;
				pmin = pmax;
				pmax = tmp;
			}
			pmin = MiMath.Arodoneix(pmin, prec);
			pmax = MiMath.Arodoneix(pmax, prec);

			full = new DefaultMutableTreeNode("<html>" + c.getFills()
					+ " <b COLOR='#000000'>[" + pmin + " , " + pmax
					+ "]</b></html>");
		}

		bra.add(full);

		if (c.getFamily() > 1) {
			for (int n = 0; n < c.getFamily(); n++) {
				this.MostraBranca(c.getFill(n), full);
			}
		}
	}
}
