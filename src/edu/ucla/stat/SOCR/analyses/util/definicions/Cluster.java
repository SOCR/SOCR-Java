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

import java.util.LinkedList;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Cluster information
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Cluster {

	// Incremented for each cluster to ensure that there are no repetitions
	private static int darrerId = 0;

	// Variables that define the cluster characteristics
	private Integer id;
	private String nom = "";
	private double alcada = 0.0;
	private double aglomeracio = 0.0;

	// Initial nodes
	private int fills = 0;
	private LinkedList<Cluster> lst;
	private LinkedList<Cluster> lstFills;

	// To know if it is a supercluster
	private boolean nado = true;

	// Highest value of the cluster
	private double cim = 0;
	private double base;

	public Cluster() {
		Inicialitza();
		nom = Integer.toString(id);
	}

	public Cluster(String nom) {
		Inicialitza();
		this.nom = nom;
	}

	private void Inicialitza() {
		lst = new LinkedList<Cluster>();
		lstFills = new LinkedList<Cluster>();
		id = ++Cluster.darrerId;
	}

	public static void resetId() {
		Cluster.darrerId = 0;
	}

	public int getFamily() {
		return (lst.size() > 0 ? lst.size() : 1);
	}

	public int getCardinalitat() {
		if (this.isNado())
			return this.getFamily();
		else
			return 1;
	}

	public int getFills() {
		if (fills == 0)
			return 1;
		else
			return fills;
	}

	public Integer getId() {
		return id;
	}

	public double setAlcada(final double alcada) {
		this.alcada = alcada;
		base = alcada;
		if ((alcada + aglomeracio) > cim)
			cim = (alcada + aglomeracio);
		if ((alcada - aglomeracio) < base)
			base = (alcada - aglomeracio);
		return base;
	}

	public double getAlcada() {
		return alcada;
	}

	public double getAglomeracio() {
		return aglomeracio;
	}

	public double setAglomeracio(final double ag) {
		aglomeracio = ag;
		base = alcada;
		if ((alcada + aglomeracio) > cim)
			cim = (alcada + aglomeracio);
		if ((alcada - aglomeracio) < base)
			base = (alcada - aglomeracio);
		return base;
	}

	public Double getCim() {
		return cim;
	}

	public void setCim(Double cim) {
		this.cim = cim;
	}

	public void addCluster(final Cluster c) throws Exception {
		try {
			if (c.getFamily() == 1)
				lstFills.add(c);
			else
				lstFills.addAll(c.getLstFills());
			lst.addLast(c);
			fills += c.getFills();
		} catch (Exception e) {
			String err_msg;
			err_msg = e.getMessage();
			err_msg += "\n" + Language.getLabel(74);
			throw new Exception(err_msg);
		}

		try {
			if (c.getCim() > cim)
				cim = c.getCim();
			if (c.getBase() < base)
				base = c.getBase();
		} catch (Exception e) {
			String err_msg;
			err_msg = e.getMessage();
			err_msg += "\n" + Language.getLabel(75);
			throw new Exception(err_msg);
		}
	}

	public void isNado(final boolean b) {
		nado = b;
	}

	public boolean isNado() {
		return nado;
	}

	public Cluster getFill(final int pos) throws Exception {
		Cluster c;
		if (lst.isEmpty() && (pos == 0))
			c = this;
		else if (pos < lst.size()) {
			try {
				c = lst.get(pos);
			} catch (Exception e) {
				throw new Exception(Language.getLabel(18));
			}
		} else {
			c = null;
			throw new Exception(Language.getLabel(17));
		}
		return c;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(final String nom) {
		this.nom = nom;
	}

	public LinkedList<Cluster> getLst() {
		return this.lst;
	}

	public LinkedList<Cluster> getLstFills() {
		return this.lstFills;
	}

	public void setBase(double base) {
		this.base = base;
	}

	public double getBase() {
		return base;
	}
}
