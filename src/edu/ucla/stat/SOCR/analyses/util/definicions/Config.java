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

import edu.ucla.stat.SOCR.analyses.util.importExport.DadesExternes;
import edu.ucla.stat.SOCR.analyses.util.importExport.FitxerDades;
import edu.ucla.stat.SOCR.analyses.util.inicial.FesLog;

import java.util.Hashtable;

import javax.swing.JInternalFrame;

import edu.ucla.stat.SOCR.analyses.util.tipus.Orientation;
import edu.ucla.stat.SOCR.analyses.util.tipus.metodo;
import edu.ucla.stat.SOCR.analyses.util.tipus.rotacioNoms;
import edu.ucla.stat.SOCR.analyses.util.tipus.tipusDades;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Class that stores all the settings defined at the user GUI
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Config {

	private MatriuDistancies md = null;
	private Hashtable<Integer, String> htNoms;
	private final CfgPanelMenu cfgMenu;
	private JInternalFrame pizarra;
	private FitxerDades fitx;
	private double radi = 5.0;
	private double val_max_origen = 0;

	public Config(final CfgPanelMenu cfgMenu) {
		this.cfgMenu = cfgMenu;
	}

	public void setPizarra(final JInternalFrame pizarra) {
		this.pizarra = pizarra;
	}

	public JInternalFrame getPizarra() {
		return pizarra;
	}

	public CfgPanelMenu getConfigMenu() {
		return this.cfgMenu;
	}

	public void setFitxerDades(final FitxerDades fd) {
		this.fitx = new FitxerDades(fd);
	}

	public FitxerDades getFitxerDades() {
		return fitx;
	}

	public tipusDades getTipusMatriu() {
		return cfgMenu.getTipusDades();
	}

	public boolean isTipusDistancia() {
		return cfgMenu.getTipusDades().equals(tipusDades.DISTANCIA);
	}

	public metodo getMethod() {
		return cfgMenu.getMetodo();
	}

	public int getPrecision() {
		return cfgMenu.getDecimalsSignificatius();
	}

	public void setMatriu(final MatriuDistancies md) {
		this.md = md;
		if ((md != null) && (!isTipusDistancia()) && (val_max_origen == 0)) {
			val_max_origen = md.maxValue();
		}
	}

	public MatriuDistancies getMatriu() {
		return md;
	}

	public MatriuDistancies getMatriuDistancies() {
		MatriuDistancies md = null;
		DadesExternes de;

		try {
			de = new DadesExternes(fitx);
			md = de.getMatriuDistancies();
		} catch (Exception e) {
			FesLog.LOG.throwing("Config", "getMatriuDistancies", e);
		}
		return md;
	}
        
        public MatriuDistancies getMatriuDistancies1() {
		MatriuDistancies md = null;
		DadesExternes de;

		try {
			de = new DadesExternes(fitx, "");
			md = de.getMatriuDistancies();
		} catch (Exception e) {
			FesLog.LOG.throwing("Config", "getMatriuDistancies", e);
		}
		return md;
	}

	public Orientation getOrientacioDendo() {
		return cfgMenu.getOrientacioDendograma();
	}

	public void setOrientacioDendo(Orientation or) {
		cfgMenu.setOrientacioDendograma(or);
	}

	public rotacioNoms getOrientacioNoms() {
		return cfgMenu.getRotNoms();
	}

	public double getRadi() {
		return radi;
	}

	public void setRadi(final double radi) {
		this.radi = radi;
	}

	public double getValorMaxim() {
		return cfgMenu.getValMax();
	}

	public double getValorMinim() {
		return cfgMenu.getValMin();
	}

	public double getCimDendograma() {
		if ((md != null) && (this.isTipusDistancia())) {
			return md.getArrel().getCim();
		} else {
			return val_max_origen;
		}
	}

	public double getBaseDendograma() {
		if (!this.isTipusDistancia()) {
			return md.getArrel().getBase();
		} else {
			return val_max_origen;
		}
	}

	public double getIncrement() {
		return cfgMenu.getIncrement();
	}

	public int getTics() {
		return cfgMenu.getTics();
	}

	public int getAxisDecimals() {
		return cfgMenu.getAxisDecimals();
	}

	public Hashtable<Integer, String> getHtNoms() {
		return htNoms;
	}

	public void setHtNoms(final Hashtable<Integer, String> htNoms) {
		this.htNoms = htNoms;
	}

}
