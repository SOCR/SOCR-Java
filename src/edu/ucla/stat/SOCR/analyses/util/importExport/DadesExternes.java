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

package edu.ucla.stat.SOCR.analyses.util.importExport;

import edu.ucla.stat.SOCR.analyses.util.inicial.FesLog;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import edu.ucla.stat.SOCR.analyses.util.parser.Ultrametric;
import edu.ucla.stat.SOCR.analyses.util.definicions.Cluster;
import edu.ucla.stat.SOCR.analyses.util.definicions.MatriuDistancies;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Given a text file representing a distances matrix between elements,
 * gets all the distances between elements and stores them into a list
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class DadesExternes {

	private final FitxerDades fitx;
	private Hashtable<Integer, String> htNoms;
	private MatriuDistancies matriuDades;
	private int numClusters = 0;
	private int prec = 0;

	public DadesExternes(final FitxerDades fitx) throws Exception {
		this.fitx = new FitxerDades(fitx);
		this.OmpleMatriuDistancies();
	}
        
        public DadesExternes(final FitxerDades fitx, String dummyString) throws Exception {
		this.fitx = new FitxerDades(fitx);
		this.OmpleMatriuDistancies1();
	}

	private void OmpleMatriuDistancies() throws Exception {
		StructIn<String> s_in;
		Iterator<StructIn<String>> it;
		final ComptaDecimals cp = new ComptaDecimals();

		LinkedList<StructIn<String>> lst = new LinkedList<StructIn<String>>();
		MatriuDistancies md;

		lst = this.LlegeixFitxer();
		Ultrametric um = new Ultrametric();
		um.setLlistaOrig(lst);

		FesLog.LOG.config("Creo una matriu per a " + numClusters + " elements");

		Cluster.resetId();
		md = new MatriuDistancies(numClusters);
		it = lst.iterator();

		final Hashtable<String, Cluster> ht = new Hashtable<String, Cluster>();
		Cluster c1, c2;

		while (it.hasNext()) {
			s_in = it.next();

			if (ht.containsKey(s_in.getC1()))
				c1 = ht.get(s_in.getC1());
			else {
				c1 = new Cluster();
				c1.setNom(s_in.getC1());
				ht.put(s_in.getC1(), c1);
			}

			if (ht.containsKey(s_in.getC2()))
				c2 = ht.get(s_in.getC2());
			else {
				c2 = new Cluster();
				c2.setNom(s_in.getC2());
				ht.put(s_in.getC2(), c2);
			}
			cp.inValue(s_in.getVal());
			md.setDistancia(c1, c2, s_in.getVal());
		}
		prec = cp.getPrecisio();

		matriuDades = md;
		htNoms = new Hashtable<Integer, String>();
		String s;
		Enumeration<String> e = ht.keys();
		while (e.hasMoreElements())
			s = e.nextElement();

		e = ht.keys();
		while (e.hasMoreElements()) {
			s = e.nextElement();
			htNoms.put(ht.get(s).getId(), s);
		}
	}
        
        private void OmpleMatriuDistancies1() throws Exception {
		StructIn<String> s_in;
		Iterator<StructIn<String>> it;
		final ComptaDecimals cp = new ComptaDecimals();

		LinkedList<StructIn<String>> lst = new LinkedList<StructIn<String>>();
		MatriuDistancies md;

		lst = this.LlegeixFitxer1();
		Ultrametric um = new Ultrametric();
		um.setLlistaOrig(lst);

		FesLog.LOG.config("Creo una matriu per a " + numClusters + " elements");

		Cluster.resetId();
		md = new MatriuDistancies(numClusters);
		it = lst.iterator();

		final Hashtable<String, Cluster> ht = new Hashtable<String, Cluster>();
		Cluster c1, c2;

		while (it.hasNext()) {
			s_in = it.next();

			if (ht.containsKey(s_in.getC1()))
				c1 = ht.get(s_in.getC1());
			else {
				c1 = new Cluster();
				c1.setNom(s_in.getC1());
				ht.put(s_in.getC1(), c1);
			}

			if (ht.containsKey(s_in.getC2()))
				c2 = ht.get(s_in.getC2());
			else {
				c2 = new Cluster();
				c2.setNom(s_in.getC2());
				ht.put(s_in.getC2(), c2);
			}
			cp.inValue(s_in.getVal());
			md.setDistancia(c1, c2, s_in.getVal());
		}
		prec = cp.getPrecisio();

		matriuDades = md;
		htNoms = new Hashtable<Integer, String>();
		String s;
		Enumeration<String> e = ht.keys();
		while (e.hasMoreElements())
			s = e.nextElement();

		e = ht.keys();
		while (e.hasMoreElements()) {
			s = e.nextElement();
			htNoms.put(ht.get(s).getId(), s);
		}
	}

	private LinkedList<StructIn<String>> LlegeixFitxer() throws Exception {
		LinkedList<StructIn<String>> lst = new LinkedList<StructIn<String>>();
		// Llegim el fitxer.
		ReadTXT txt = new ReadTXT(fitx.getRuta());
		lst = txt.read();
		numClusters = txt.getNumElements();
		return lst;
	}
        
        private LinkedList<StructIn<String>> LlegeixFitxer1() throws Exception {
		LinkedList<StructIn<String>> lst = new LinkedList<StructIn<String>>();
		// Llegim el fitxer.
		ReadTXT txt = new ReadTXT(fitx.getRuta(), "");
		lst = txt.read();
		numClusters = txt.getNumElements();
		return lst;
	}

	public FitxerDades getFitxerDades() {
		return fitx;
	}

	public Hashtable<Integer, String> getTaulaNoms() {
		return htNoms;
	}

	public MatriuDistancies getMatriuDistancies() {
		return matriuDades;
	}

	public int getPrecisio() {
		return prec;
	}

}
