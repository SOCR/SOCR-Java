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

package edu.ucla.stat.SOCR.analyses.util.utils;

import java.util.Vector;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Calculation of deviation measures
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class DeviationMeasures {
	private final Vector<Double> dists_eucl = new Vector<Double>();
	private final Vector<Double> dists_dendro = new Vector<Double>();
	private final Double[] avgs = new Double[2];
	private int count = 0;

	private static Double distance(Double x, Double y) {
		return Math.abs(x - y);
	}

	private Double average(Vector<Double> list) {
		double sum = 0.0;
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			sum += list.get(i);
			count++;
		}

		return sum / count;
	}

	private void calculateDistances(Double[][] mat_orig, Double[][] mat_um) {
		// Calculem la mitjana de les distancies euclidianes i dendrogramatiques
		int n = 0;
		for (int i = 0; i < mat_um.length; i++)
			for (int j = 0; j < mat_um.length; j++)
				if (i < j) {
					dists_eucl.add(n, mat_orig[i][j]);
					dists_dendro.add(n, mat_um[i][j]);
					n++;
				}

		count = n;
		avgs[0] = average(dists_eucl); // mitjana dists. eucl.
		avgs[1] = average(dists_dendro); // mitjana dists. dendro. (altures)
	}

	public Double getCopheneticCorrelation(Double[][] matriu_orig,
			Double[][] matriu_ultram) {
		calculateDistances(matriu_orig, matriu_ultram);
		return cc_num(matriu_orig, matriu_ultram)
				/ cc_den(matriu_orig, matriu_ultram);
	}

	private Double cc_num(Double[][] mat_orig, Double[][] mat) {
		Double sum = 0.0;
		for (int i = 0; i < mat.length; i++)
			for (int j = 0; j < mat.length; j++)
				if (i < j) {
					Double sum1 = mat_orig[i][j] - avgs[0]; // x(i,j)-x
					Double sum2 = mat[i][j] - avgs[1]; // t(i,j)-t
					sum += sum1 * sum2;
				}
		Double num = sum / count;

		return num;
	}

	private Double cc_den(Double[][] mat_orig, Double[][] mat) {
		Double sum_x = 0.0; // sum_i<j (x(i,j)-x)**2
		Double sum_y = 0.0; // sum_i<j (t(i,j)-t)**2
		for (int i = 0; i < mat.length; i++)
			for (int j = 0; j < mat.length; j++)
				if (i < j) {
					sum_x += Math.pow(mat_orig[i][j] - avgs[0], 2); // [x(i,j)-x]**2
					sum_y += Math.pow(mat[i][j] - avgs[1], 2); // [t(i,j)-t]**2
				}
		Double sigma_x = Math.sqrt(sum_x / count);
		Double sigma_y = Math.sqrt(sum_y / count);
		Double den = sigma_x * sigma_y;

		return den;
	}

	public Double getSquaredError(Double[][] matriu_orig,
			Double[][] matriu_ultram) {
		calculateDistances(matriu_orig, matriu_ultram);

		Double num = 0.0, den = 0.0;
		for (int i = 0; i < matriu_orig.length; i++)
			for (int j = 0; j < matriu_orig.length; j++)
				if (i < j) {
					num += Math
							.pow(distance(matriu_orig[i][j],
									matriu_ultram[i][j]), 2); // |x-t|**2
					den += Math.pow(matriu_orig[i][j], 2); // x**2
				}
		return num / den;
	}

	public Double getAbsoluteError(Double[][] matriu_orig,
			Double[][] matriu_ultram) {
		calculateDistances(matriu_orig, matriu_ultram);

		Double num = 0.0, den = 0.0;
		for (int i = 0; i < matriu_orig.length; i++)
			for (int j = 0; j < matriu_orig.length; j++)
				if (i < j) {
					num += distance(matriu_orig[i][j], matriu_ultram[i][j]); // |x-t|
					den += matriu_orig[i][j]; // x
				}
		return num / den;
	}

}
