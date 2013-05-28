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

package edu.ucla.stat.SOCR.analyses.util.methods;

import edu.ucla.stat.SOCR.analyses.util.utils.PrecDouble;
import edu.ucla.stat.SOCR.analyses.util.definicions.Cluster;
import edu.ucla.stat.SOCR.analyses.util.definicions.MatriuDistancies;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Abstract class for the different clustering methods available
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public abstract class Method {

	Cluster cI, cJ;
	MatriuDistancies mdAct;

	public Method(final Cluster ci, final Cluster cj, final MatriuDistancies md) {
		cI = ci;
		cJ = cj;
		mdAct = md;
	}

	public double Distancia() throws Exception {
		return this.calculaDistanciaIJ();
	}

	private double calculaDistanciaIJ() throws Exception {
		PrecDouble dist, dl, alfa, beta, tmp;
		tmp = new PrecDouble("0.0");

		if (mdAct.existDistance(cI, cJ)) {
			dist = new PrecDouble(mdAct.getDistancia(cI, cJ));
		} else {
			if (cI.isNado() && cJ.isNado()) {
				for (int i = 0; i < cI.getFamily(); i++) {
					for (int j = 0; j < cJ.getFamily(); j++) {
						dl = new PrecDouble(mdAct.getDistancia(cI.getFill(i),
								cJ.getFill(j)));
						alfa = new PrecDouble(this.getAlfa_ij(cI.getFill(i),
								cJ.getFill(j)));
						alfa.Producto(dl);
						tmp.Suma(alfa);
					}
				}
			} else {
				if (cI.isNado()) {
					for (int i = 0; i < cI.getFamily(); i++) {
						dl = new PrecDouble(mdAct.getDistancia(cI.getFill(i),
								cJ));
						alfa = new PrecDouble(
								this.getAlfa_ij(cI.getFill(i), cJ));
						alfa.Producto(dl);
						tmp.Suma(alfa);
					}
				} else {
					for (int j = 0; j < cJ.getFamily(); j++) {
						dl = new PrecDouble(mdAct.getDistancia(cJ.getFill(j),
								cI));
						alfa = new PrecDouble(
								this.getAlfa_ij(cJ.getFill(j), cI));
						alfa.Producto(dl);
						tmp.Suma(alfa);
					}
				}
			}
			dist = tmp.clone();

			if (cI.isNado()) {
				tmp = new PrecDouble("0.0");
				for (int i = 0; i < cI.getFamily() - 1; i++) {
					for (int ii = i + 1; ii < cI.getFamily(); ii++) {
						beta = new PrecDouble(this.getBeta_ii(cI.getFill(i),
								cI.getFill(ii)));
						beta.Producto(mdAct.getDistancia(cI.getFill(i),
								cI.getFill(ii)));
						tmp.Suma(beta);
					}
				}
				dist.Suma(tmp);
			}

			if (cJ.isNado()) {
				tmp = new PrecDouble("0.0");
				for (int j = 0; j < cJ.getFamily() - 1; j++) {
					for (int jj = j + 1; jj < cJ.getFamily(); jj++) {
						beta = new PrecDouble(this.getBeta_jj(cJ.getFill(j),
								cJ.getFill(jj)));
						beta.Producto(mdAct.getDistancia(cJ.getFill(j),
								cJ.getFill(jj)));
						tmp.Suma(beta);
					}
				}
				dist.Suma(tmp);
			}
			dist.Suma(this.CalculLinkage());
		}

		return dist.parserToDouble();
	}

	protected abstract double CalculLinkage() throws Exception;

	protected abstract double getAlfa_ij(Cluster i, Cluster j);

	protected abstract double getBeta_ii(Cluster i, Cluster ii);

	protected abstract double getBeta_jj(Cluster j, Cluster jj);

	protected abstract double getGamma_ij(Cluster i, Cluster j);

	public Cluster getcI() {
		return cI;
	}

	public void setcI(final Cluster i) {
		cI = i;
	}

	public Cluster getcJ() {
		return cJ;
	}

	public void setcJ(final Cluster j) {
		cJ = j;
	}

	protected double getDistanciaMax(final Cluster ci, final Cluster cj)
			throws Exception {
		double max = Double.MIN_VALUE;
		double tmp;

		if (mdAct.existDistance(ci, cj)) {
			max = mdAct.getDistancia(ci, cj);
		} else if (ci.isNado() && cj.isNado()) {
			for (int i = 0; i < ci.getFamily(); i++) {
				for (int j = 0; j < cj.getFamily(); j++) {
					tmp = mdAct.getDistancia(ci.getFill(i), cj.getFill(j));
					max = max < tmp ? tmp : max;
				}
			}
		} else if (ci.isNado()) {
			for (int i = 0; i < ci.getFamily(); i++) {
				tmp = mdAct.getDistancia(ci.getFill(i), cj);
				max = max < tmp ? tmp : max;
			}
		} else {
			for (int j = 0; j < cj.getFamily(); j++) {
				tmp = mdAct.getDistancia(cj.getFill(j), ci);
				max = max < tmp ? tmp : max;
			}
		}
		return max;
	}

	protected double getDistanciaMin(final Cluster ci, final Cluster cj)
			throws Exception {
		double min = Double.MAX_VALUE;
		double tmp;

		if (mdAct.existDistance(ci, cj)) {
			min = mdAct.getDistancia(ci, cj);
		} else if (ci.isNado() && cj.isNado()) {
			for (int i = 0; i < ci.getFamily(); i++) {
				for (int j = 0; j < cj.getFamily(); j++) {
					tmp = mdAct.getDistancia(ci.getFill(i), cj.getFill(j));
					min = min > tmp ? tmp : min;
				}
			}
		} else if (ci.isNado()) {
			for (int i = 0; i < ci.getFamily(); i++) {
				tmp = mdAct.getDistancia(ci.getFill(i), cj);
				min = min > tmp ? tmp : min;
			}
		} else {
			for (int j = 0; j < cj.getFamily(); j++) {
				tmp = mdAct.getDistancia(cj.getFill(j), ci);
				min = min > tmp ? tmp : min;
			}
		}
		return min;
	}

}
