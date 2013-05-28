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

package edu.ucla.stat.SOCR.analyses.util.moduls.frm;

import edu.ucla.stat.SOCR.analyses.util.tipus.Orientation;
import edu.ucla.stat.SOCR.analyses.util.tipus.tipusDades;
import edu.ucla.stat.SOCR.analyses.util.definicions.BoxContainer;
import edu.ucla.stat.SOCR.analyses.util.definicions.Config;
import edu.ucla.stat.SOCR.analyses.util.definicions.Dimensions;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Calculates de lower left coordinates of all window components
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class XYBox {

	private final Dimensions<Double> mida_bullets;
	private final Dimensions<Double> mida_noms_bullets;
	private final Dimensions<Double> mida_escala, mida_lbl_escala;
	private final Orientation or;
	private final tipusDades tip;
	private final double radi, k;
	private final double mon_w, mon_h;
	private double val_max_show, val_min_show;
	private final int numClusters;
	private double w_ocupat, h_ocupat;
	private double d_width, d_height;
	private final Config cfg;
	private BoxContainer bd, bb, bn, be, bl;

	public XYBox(final Config cfg, final double k, final double mon_w,
			final double mon_h, final Dimensions<Double> m_d,
			final Dimensions<Double> m_b, final Dimensions<Double> m_n,
			final Dimensions<Double> m_e, final Dimensions<Double> m_l) {
		this.cfg = cfg;
		or = cfg.getOrientacioDendo();
		tip = cfg.getTipusMatriu();
		radi = cfg.getRadi();
		this.k = k;
		mida_bullets = m_b;
		mida_noms_bullets = m_n;
		mida_escala = m_e;
		mida_lbl_escala = m_l;
		this.mon_w = mon_w;
		this.mon_h = mon_h;
		numClusters = cfg.getMatriu().getArrel().getFills();

		// per calcular la part lliure de la finestra
		if (Orientation.NORTH.equals(or) || Orientation.SOUTH.equals(or)) {
			w_ocupat = m_e.getWidth() + m_l.getWidth() + 2 * radi;
			h_ocupat = m_n.getHeight() + m_b.getHeight() + 2 * radi;
		} else {
			w_ocupat = m_n.getWidth() + m_b.getWidth() + 2 * radi;
			h_ocupat = m_e.getHeight() + m_l.getHeight() + 2 * radi;
		}
		this.calculaDendo();
	}

	private void calculaDendo() {
		double x, y;
		Orientation or_tmp;
		bd = new BoxContainer();

		// control del tipus de dades canvia la ubicacio dels butllets
		or_tmp = this.AdaptaOrientacio();
		this.CarregaValorsDendo();

		if (Orientation.NORTH.equals(or_tmp)
				|| Orientation.SOUTH.equals(or_tmp)) {
			d_width = mon_w - (w_ocupat + mida_noms_bullets.getWidth() + 2 * k);
			d_height = mon_h - (h_ocupat + 2 * k);
		} else if (Orientation.EAST.equals(or_tmp)
				|| Orientation.WEST.equals(or_tmp)) {
			d_width = mon_w - (w_ocupat + 2 * k);
			d_height = mon_h
					- (h_ocupat + 2 * k + mida_noms_bullets.getHeight());
		}

		x = Orientation.WEST.equals(or_tmp) ? k : (w_ocupat + k);
		y = Orientation.NORTH.equals(or_tmp) ? (h_ocupat + k) : k;

		bd.setCorner_x(x);
		bd.setCorner_y(y);
		if (Orientation.EAST.equals(or_tmp) || Orientation.WEST.equals(or_tmp))
			bd.setCorner_y(y + mida_noms_bullets.getHeight());
		bd.setWidth(d_width);
		bd.setHeight(d_height);
		if (Orientation.NORTH.equals(or_tmp)
				|| Orientation.SOUTH.equals(or_tmp)) {
			bd.setVal_max_X(this.AmpladaBoxClusters());
			bd.setVal_min_X(0d);
			bd.setVal_max_Y(val_max_show);
			bd.setVal_min_Y(val_min_show);
		} else {
			bd.setVal_max_Y(this.AmpladaBoxClusters());
			bd.setVal_min_Y(0d);
			bd.setVal_max_X(val_max_show);
			bd.setVal_min_X(val_min_show);
		}
	}

	public BoxContainer getBoxDendo() {
		return bd;
	}

	public BoxContainer getBoxLabelsEscala() {
		double x, y;
		double w, h;
		Orientation or_tmp;
		bl = new BoxContainer();

		or_tmp = this.AdaptaOrientacio();

		if (Orientation.NORTH.equals(or_tmp)
				|| Orientation.SOUTH.equals(or_tmp)) {
			x = k;
			y = bd.getCorner_y();
			w = mida_lbl_escala.getWidth();
			h = d_height;
		} else {
			x = bd.getCorner_x();
			y = bd.getCorner_y() + d_height + mida_escala.getHeight() + 2
					* radi;
			w = d_width;
			h = mida_lbl_escala.getHeight();
		}

		bl.setCorner_x(x);
		bl.setCorner_y(y);
		bl.setWidth(w);
		bl.setHeight(h);

		if (Orientation.NORTH.equals(or_tmp)
				|| Orientation.SOUTH.equals(or_tmp)) {
			bl.setVal_max_X(mida_lbl_escala.getWidth());
			bl.setVal_min_X(0d);
			bl.setVal_max_Y(val_max_show);
			bl.setVal_min_Y(val_min_show);
		} else {
			bl.setVal_max_Y(mida_lbl_escala.getHeight());
			bl.setVal_min_Y(0d);
			bl.setVal_max_X(val_max_show);
			bl.setVal_min_X(val_min_show);
		}

		return bl;
	}

	public BoxContainer getBoxEscala() {
		double x, y;
		double w, h;
		Orientation or_tmp;
		be = new BoxContainer();

		// en pesos invertim l'orientacio
		or_tmp = this.AdaptaOrientacio();

		if (Orientation.NORTH.equals(or_tmp)
				|| Orientation.SOUTH.equals(or_tmp)) {
			x = mida_lbl_escala.getWidth() + radi + k;
			y = bd.getCorner_y();
			w = mida_escala.getWidth();
			h = d_height;

		} else {
			x = bd.getCorner_x();
			y = bd.getCorner_y() + d_height + radi;
			w = d_width;
			h = mida_escala.getHeight();
		}

		be.setCorner_x(x);
		be.setCorner_y(y);
		be.setWidth(w);
		be.setHeight(h);

		if (Orientation.NORTH.equals(or_tmp)
				|| Orientation.SOUTH.equals(or_tmp)) {
			be.setVal_max_X(2d);
			be.setVal_min_X(0d);
			be.setVal_max_Y(val_max_show);
			be.setVal_min_Y(val_min_show);
		} else {
			be.setVal_max_Y(2);
			be.setVal_min_Y(0d);
			be.setVal_max_X(val_max_show);
			be.setVal_min_X(val_min_show);
		}

		return be;
	}

	public BoxContainer getBoxNames() {
		double x = 0, y = 0;
		double w = 0, h = 0;
		Orientation or_tmp;
		bn = new BoxContainer();

		// en pesos invertim l'orientacio
		or_tmp = this.AdaptaOrientacio();

		if (Orientation.NORTH.equals(or_tmp)) {
			x = bd.getCorner_x();
			y = k;
		} else if (Orientation.SOUTH.equals(or_tmp)) {
			x = bd.getCorner_x();
			y = bd.getCorner_y() + d_height + mida_bullets.getHeight() + 2
					* radi;
		} else if (Orientation.WEST.equals(or_tmp)) {
			x = bd.getCorner_x() + d_width + mida_bullets.getWidth() + 2 * radi;
			y = bd.getCorner_y();
		} else if (Orientation.EAST.equals(or_tmp)) {
			x = k;
			y = bd.getCorner_y();
		}

		if (Orientation.NORTH.equals(or_tmp) || Orientation.SOUTH.equals(or_tmp)) {
			w = d_width;
			h = mida_noms_bullets.getHeight();
		} else {
			w = mida_noms_bullets.getWidth();
			h = d_height;
		}

		bn.setCorner_x(x);
		bn.setCorner_y(y);
		bn.setWidth(w);
		bn.setHeight(h);

		if (Orientation.NORTH.equals(or_tmp) || Orientation.SOUTH.equals(or_tmp)) {
			bn.setVal_max_X(this.AmpladaBoxClusters());
			bn.setVal_min_X(0d);
			bn.setVal_max_Y(mida_noms_bullets.getHeight());
			bn.setVal_min_Y(0d);
		} else {
			bn.setVal_max_Y(this.AmpladaBoxClusters());
			bn.setVal_min_Y(0d);
			bn.setVal_max_X(mida_noms_bullets.getWidth());
			bn.setVal_min_X(0d);
		}
		return bn;
	}

	public BoxContainer getBoxBulles() {
		double x = 0, y = 0;
		double w = 0, h = 0;
		Orientation or_tmp;
		bb = new BoxContainer();
		or_tmp = this.AdaptaOrientacio();

		if (Orientation.NORTH.equals(or_tmp)) {
			x = bd.getCorner_x();
			y = bd.getCorner_y() - radi + 1;
		} else if (Orientation.SOUTH.equals(or_tmp)) {
			x = bd.getCorner_x();
			y = bd.getCorner_y() + d_height - radi + 1;
		} else if (Orientation.WEST.equals(or_tmp)) {
			x = bd.getCorner_x() + d_width - radi + 1;
			y = bd.getCorner_y();
		} else if (Orientation.EAST.equals(or_tmp)) {
			x = bd.getCorner_x() - radi + 1;
			y = bd.getCorner_y();
		}

		if (Orientation.NORTH.equals(or_tmp) || Orientation.SOUTH.equals(or_tmp)) {
			w = d_width;
			h = mida_bullets.getHeight();
		} else {
			w = mida_bullets.getWidth();
			h = d_height;
		}

		bb.setCorner_x(x);
		bb.setCorner_y(y);
		bb.setWidth(w);
		bb.setHeight(h);

		if (Orientation.NORTH.equals(or_tmp)
				|| Orientation.SOUTH.equals(or_tmp)) {
			bb.setVal_max_X(this.AmpladaBoxClusters());
			bb.setVal_min_X(0d);
			bb.setVal_max_Y(radi);
			bb.setVal_min_Y(0d);
		} else {
			bb.setVal_max_Y(this.AmpladaBoxClusters());
			bb.setVal_min_Y(0d);
			bb.setVal_max_X(radi);
			bb.setVal_min_X(0d);
		}
		return bb;
	}

	private Orientation AdaptaOrientacio() {
		Orientation or_rel;
		if (tipusDades.PESO.equals(tip)) {
			if (Orientation.NORTH.equals(or))
				or_rel = Orientation.SOUTH;
			else if (Orientation.SOUTH.equals(or))
				or_rel = Orientation.NORTH;
			else if (Orientation.WEST.equals(or))
				or_rel = Orientation.EAST;
			else
				or_rel = Orientation.WEST;
		} else
			or_rel = or;

		return or_rel;
	}

	private double AmpladaBoxClusters() {
		return ((2 * radi * numClusters) + ((numClusters - 1) * radi));
	}

	private void CarregaValorsDendo() {
		val_max_show = cfg.getValorMaxim();
		val_min_show = cfg.getValorMinim();
	}
}
