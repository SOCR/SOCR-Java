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
 * Joint Between-within clustering algorithm
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class JointBetweenWithin extends Method {

	public JointBetweenWithin(final Cluster ci, final Cluster cj,
			final MatriuDistancies md) {
		super(ci, cj, md);
	}

	@Override
	protected double CalculLinkage() {
		return 0;
	}

	@Override
	protected double getAlfa_ij(final Cluster i, final Cluster j) {
		PrecDouble pr;
		pr = new PrecDouble(i.getFills() + j.getFills());
		pr.Division((cI.getFills() + cJ.getFills()));
		return pr.parserToDouble();
	}

	@Override
	protected double getBeta_ii(final Cluster i, final Cluster ii) {
		PrecDouble pr, res;
		pr = new PrecDouble((i.getFills() + ii.getFills()));
		pr.Division((cI.getFills() + cJ.getFills()));

		res = new PrecDouble(cJ.getFills());
		res.Division(cI.getFills());
		res.Producto(pr);
		res.CanviSigne();
		return res.parserToDouble();
	}

	@Override
	protected double getBeta_jj(final Cluster j, final Cluster jj) {
		PrecDouble res, pr;
		pr = new PrecDouble((j.getFills() + jj.getFills()));
		pr.Division((cI.getFills() + cJ.getFills()));

		res = new PrecDouble(cI.getFills());
		res.Division(cJ.getFills());
		res.Producto(pr);
		res.CanviSigne();

		return res.parserToDouble();
	}

	@Override
	protected double getGamma_ij(final Cluster i, final Cluster j) {
		return 0;
	}

}
