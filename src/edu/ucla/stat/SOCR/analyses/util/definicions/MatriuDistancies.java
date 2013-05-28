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

import edu.ucla.stat.SOCR.analyses.util.inicial.Language;

import java.util.Vector;

import edu.ucla.stat.SOCR.analyses.util.tipus.tipusDades;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Stores the distances matrix and implements methods for its management
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class MatriuDistancies {

	// Type of data stored in the matrix
	private tipusDades tipDades = tipusDades.DISTANCIA;

	// When it's 'true', it indicates that the matrix has a single element
	private boolean isCopa = false;

	private int numElems = 0;

	// Distances minimum and maximum among all existing distances
	private double minValue = Double.MAX_VALUE;
	private double maxValue = Double.MIN_VALUE;

	// Storage of distance data
	private Vector<Cluster> arrClusters;
	private Vector<Vector<Double>> arrDistancies;
	private Id_To_Key<Integer> claus;

	public MatriuDistancies(int size, final tipusDades tip) throws Exception {
		tipDades = tip;
		if (size < 2) {
			isCopa = true;
			size = 2;
		} else {
			isCopa = false;
		}
		this.inicialitza(size);
	}

	public MatriuDistancies(int size) throws Exception {
		tipDades = tipusDades.DISTANCIA;
		if (size < 2) {
			isCopa = true;
			size = 2;
		} else {
			isCopa = false;
		}
		this.inicialitza(size);
	}

	private void inicialitza(final int size) throws Exception {
		arrClusters = new Vector<Cluster>(size);
		arrDistancies = new Vector<Vector<Double>>(size);
		for (int n = 0; n < size; n++) {
			try {
				arrDistancies.add(new Vector<Double>((size - n - 1)));
				for (int nn = 0; nn < (size - n - 1); nn++) {
					arrDistancies.get(n).add(null);
				}
			} catch (final Exception e) {
				String err_msg;
				err_msg = e.getMessage();
				err_msg += "\n" + Language.getLabel(73);
				throw new Exception(err_msg);
			}
		}
		claus = new Id_To_Key<Integer>();
	}

	public boolean isUnari() {
		return isCopa;
	}

	public boolean existDistance(final Cluster ci, final Cluster cii) {
		return (claus.containsKey(ci.getId()) && claus.containsKey(cii.getId()));
	}

	public void setDistancia(final Cluster ci, final Cluster cii,
			final double dis) throws Exception {
		int k1, k2, tmp;

		if (claus.containsKey(ci.getId())) {
			k1 = claus.getInd(ci.getId());
		} else {
			k1 = claus.setInd(ci.getId());
			numElems++;
			arrClusters.add(k1, ci);
		}
		if (claus.containsKey(cii.getId())) {
			k2 = claus.getInd(cii.getId());
		} else {
			k2 = claus.setInd(cii.getId());
			numElems++;
			arrClusters.add(k2, cii);
		}
		try {
			if (k1 > k2) {
				tmp = k2;
				k2 = k1;
				k1 = tmp;
			} else if ((k1 == k2) && (k1 == 0)) {
				// single cluster matrix
				arrDistancies.get(k1).setElementAt(new Double(dis), k1);
			} else {
				arrDistancies.get(k1)
						.setElementAt(new Double(dis), k2 - k1 - 1);
			}
		} catch (final Exception e) {
			String err_msg;
			err_msg = e.getMessage();
			err_msg += "\n" + Language.getLabel(71);
			throw new Exception(err_msg);
		}
		if (dis < minValue || minValue == Double.MAX_VALUE) {
			minValue = new Double(dis);
		}
		if (dis > maxValue || maxValue == Double.MIN_VALUE) {
			maxValue = new Double(dis);
		}
	}

	public void setDistancia(final Cluster ci) throws Exception {
		if (numElems == 0) {
			this.setDistancia(ci, ci, 0);
			isCopa = true;
			numElems = 1;
		}
	}

	public Double getDistancia(final Cluster i, final Cluster ii)
			throws Exception {
		int tmp;
		int k1 = 0, k2 = 0;
		double dis = 0.0;

		try {
			k1 = claus.getInd(i.getId());
			k2 = claus.getInd(ii.getId());
			if (k1 > k2) {
				tmp = k2;
				k2 = k1;
				k1 = tmp;
			}
			dis = arrDistancies.get(k1).get(k2 - k1 - 1);
		} catch (final Exception e) {
			String err_msg;
			err_msg = e.getMessage();
			err_msg += "\n" + Language.getLabel(70);
			throw new Exception(err_msg);
		}
		return dis;
	}

	public int getCardinalitat() {
		return numElems;
	}

	public Double minValue() {
		return minValue;
	}

	public Double maxValue() {
		return maxValue;
	}

	public Vector<Cluster> getClusters() {
		return arrClusters;
	}

	public Cluster getCluster(final int pos) {
		return arrClusters.elementAt(pos);
	}

	public double[][] getMatriu() throws Exception {
		final double[][] matriu = new double[numElems][numElems];
		try {
			for (int n = 0; n < numElems - 1; n++) {
				for (int m = n + 1; m < numElems; m++) {
					matriu[n][m] = this.getDistancia(arrClusters.get(n),
							arrClusters.get(m));
					matriu[m][n] = matriu[n][m];
				}
			}
		} catch (Exception e) {
			String err_msg;
			err_msg = e.getMessage();
			err_msg += "\n" + Language.getLabel(72);
			throw new Exception(err_msg);
		}
		return matriu;
	}

	public Cluster getArrel() {
		return this.getCluster(0);
	}

	public tipusDades getTipDades() {
		return tipDades;
	}

	public void setTipDades(final tipusDades tipDades) {
		this.tipDades = tipDades;
	}

	public boolean isTipusDistancies() {
		return tipDades.equals(tipusDades.DISTANCIA);
	}

}
