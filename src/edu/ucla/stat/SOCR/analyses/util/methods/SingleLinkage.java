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

import edu.ucla.stat.SOCR.analyses.util.inicial.Language;
import edu.ucla.stat.SOCR.analyses.util.utils.PrecDouble;
import edu.ucla.stat.SOCR.analyses.util.definicions.Cluster;
import edu.ucla.stat.SOCR.analyses.util.definicions.MatriuDistancies;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Single Linkage clustering algorithm
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class SingleLinkage extends Method {

	public SingleLinkage(final Cluster ci, final Cluster cj,
			final MatriuDistancies md) {
		super(ci, cj, md);
	}

	@Override
	protected double getAlfa_ij(final Cluster i, final Cluster j) {
		double res;
		final PrecDouble pr = new PrecDouble("1.0");
		int a, b;

		a = cI.isNado() ? cI.getCardinalitat() : 1;
		b = cJ.isNado() ? cJ.getCardinalitat() : 1;
		res = (a * b);

		pr.Division(res);
		return pr.parserToDouble();
	}

	@Override
	protected double getBeta_ii(final Cluster i, final Cluster ii) {
		return 0;
	}

	@Override
	protected double getBeta_jj(final Cluster j, final Cluster jj) {
		return 0;
	}

	@Override
	protected double getGamma_ij(final Cluster i, final Cluster j) {
		return this.getAlfa_ij(i, j);
	}

	@Override
	protected double CalculLinkage() throws Exception {
		PrecDouble res, gamma, dif, tmp;
		res = new PrecDouble("0.0");

		if (mdAct.isTipusDistancies()) {
			dif = new PrecDouble(this.getDistanciaMin(cJ, cI));
		} else {
			dif = new PrecDouble(this.getDistanciaMax(cJ, cI));
		}

		if (cI.isNado() && cJ.isNado()) {
			for (int i = 0; i < cI.getFamily(); i++) {
				for (int j = 0; j < cJ.getFamily(); j++) {
					gamma = new PrecDouble(this.getGamma_ij(cI.getFill(i),
							cJ.getFill(j)));
					tmp = new PrecDouble(mdAct.getDistancia(cI.getFill(i),
							cJ.getFill(j)));
					tmp.Resta(dif);
					tmp.Producto(gamma);
					res.Suma(tmp);
				}
			}
		} else if (cI.isNado() || cJ.isNado()) {
			if (cI.isNado()) {
				for (int i = 0; i < cI.getFamily(); i++) {
					gamma = new PrecDouble(this.getGamma_ij(cI.getFill(i), cJ));
					tmp = new PrecDouble(mdAct.getDistancia(cI.getFill(i), cJ));
					tmp.Resta(dif);
					tmp.Producto(gamma);
					res.Suma(tmp);
				}
			} else {
				for (int j = 0; j < cJ.getFamily(); j++) {
					gamma = new PrecDouble(this.getGamma_ij(cJ.getFill(j), cI));
					tmp = new PrecDouble(mdAct.getDistancia(cJ.getFill(j), cI));
					tmp.Resta(dif);
					tmp.Producto(gamma);
					res.Suma(tmp);
				}
			}
		} else {
			throw new Exception(Language.getLabel(69));
		}
		res.CanviSigne();
		return res.parserToDouble();
	}
}
