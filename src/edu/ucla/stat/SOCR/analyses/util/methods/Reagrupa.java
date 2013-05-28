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

import edu.ucla.stat.SOCR.analyses.util.inicial.FesLog;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.logging.Level;

import edu.ucla.stat.SOCR.analyses.util.tipus.metodo;
import edu.ucla.stat.SOCR.analyses.util.tipus.tipusDades;
import edu.ucla.stat.SOCR.analyses.util.utils.MiMath;
import edu.ucla.stat.SOCR.analyses.util.utils.PrecDouble;
import edu.ucla.stat.SOCR.analyses.util.definicions.Cluster;
import edu.ucla.stat.SOCR.analyses.util.definicions.MatriuDistancies;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Calculates the clustering from the distances matrix
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Reagrupa {

	private Vector<Integer> grups;
	private int nextGrup = 0;
	private int numElems = 0;
	private double valmin, valmax;

	private final Integer grupNul = new Integer(0);

	private final tipusDades typeData;
	private final metodo method;
	private final int precision;

	private final MatriuDistancies mdAct;

	public Reagrupa(final MatriuDistancies md, final tipusDades typeData,
			final metodo method, final int precision) {
		FesLog.LOG.info("MatriuDistancies" + md);
		this.typeData = typeData;
		this.method = method;
		this.precision = precision;
		this.mdAct = md;
		this.IniciaDades();
	}

	private void IniciaDades() {
		valmin = mdAct.minValue();
		valmax = mdAct.maxValue();
		numElems = mdAct.getCardinalitat();
		grups = new Vector<Integer>(numElems);

		for (int n = 0; n < numElems; n++) {
			grups.add(n, grupNul);
		}
	}

	private boolean inRang(final double d) {
		double max, min, val;
		boolean enRango;

		if (typeData.equals(tipusDades.DISTANCIA)) {
			min = MiMath.Arodoneix(valmin, precision);
			val = MiMath.Arodoneix(d, precision);
			enRango = (min >= val);
		} else {
			max = MiMath.Arodoneix(valmax, precision);
			val = MiMath.Arodoneix(d, precision);
			enRango = (max <= val);
		}
		return enRango;
	}

	public MatriuDistancies Recalcula() throws Exception {
		Integer grupActual;
		int numElements;
		double dist;

		LinkedHashMap<Integer, Cluster> lhm;
		MatriuDistancies Newmd;
		final Vector<Cluster> v = mdAct.getClusters();
		numElements = mdAct.getCardinalitat();

		// introduim cada cluster en un grup, si dos clusters s'han de
		// fusionar, aquest s'introduiran en un mateix grup, igual amb
		// la resta de clusters que hi puguin exitir en el grup.
		for (int i = 0; i < numElements; i++) {
			// grup del cluster, si en te, si no null.
			grupActual = new Integer(grups.get(i));

			// l'element "i" el comparem amb tots els que te per sota.
			for (int inext = i + 1; inext < numElements; inext++) {
				// inext = cluster proxim
				dist = mdAct.getDistancia(v.get(i), v.get(inext));

				if (this.inRang(dist)) {
					// cal agrupar-los, estan a distsncia minima
					if (grupActual.equals(grupNul)) {
						// si cluster actual no te grup
						if (grups.get(inext).equals(grupNul)) {
							// si cluster proxim no te grup
							// nou identificador d'agrupacio
							grupActual = ++nextGrup;
							grups.set(i, new Integer(grupActual));
							grups.set(inext, new Integer(grupActual));
						} else {
							// el cluster proxim en te id d'agrupacio
							grupActual = new Integer(grups.get(inext));
							grups.set(i, new Integer(grupActual));
						}
					} else {
						// el cluster actual pertany a una agrupaci
						if (grups.get(inext).equals(grupNul)) {
							// el cluster proxim no te grup
							grups.set(inext, new Integer(grupActual));
						} else {
							// el cluster prxim pertany a una agrupaci
							if (!grupActual.equals(grups.get(inext))) {
								// tots dos clusters son de diferents agrupacions transfuga
								final Integer transfuga = grups.get(inext);
								// afegim a l'agrupacio del cluster tots els clusters que
								// pertanyen a l'agrupacio del cluster proxim
								for (int n = 0; n < numElems; n++) {
									if (grups.get(n).equals(transfuga)) {
										// assignem a un nou grup
										grups.set(n, new Integer(grupActual));
									}
								}
							}

						}
					}
				}
			}
		}

		if (FesLog.LOG.isLoggable(Level.INFO)) {
			String cad;
			cad = "AGRUPACIONS\n";
			for (int n = 0; n < numElems; n++)
				cad += "Id: " + v.get(n).getId() + "   --->   " + grups.get(n)
						+ "\n";
			FesLog.LOG.info(cad);
		}

		lhm = this.NousClusters(grups);
		Newmd = this.NovaMatriu(lhm);

		return Newmd;
	}

	private MatriuDistancies NovaMatriu(
			final LinkedHashMap<Integer, Cluster> lhm) throws Exception {
		final int numelems = lhm.size();
		double tmp;

		PrecDouble max;

		Method mt;
		Cluster c;
		MatriuDistancies nMd;

		double minbase1 = Double.MAX_VALUE;

		final Vector<Cluster> v = new Vector<Cluster>(numelems);

		final Collection<Cluster> col = lhm.values();
		final Iterator<Cluster> itr = col.iterator();

		// distancia maxima interna de les aglomeracions
		while (itr.hasNext()) {
			tmp = 0;
			if (typeData.equals(tipusDades.DISTANCIA)) {
				max = new PrecDouble(Double.MIN_VALUE);
			} else {
				max = new PrecDouble(Double.MAX_VALUE);
			}

			c = itr.next();

			// nomes s'ha de fer en els nous
			if (c.isNado()) {
				for (int i = 0; i < c.getFamily() - 1; i++) {
					for (int ii = i + 1; ii < c.getFamily(); ii++) {
						tmp = mdAct.getDistancia(c.getFill(i), c.getFill(ii));
						// maxima distancia dins del supercluster
						if (typeData.equals(tipusDades.DISTANCIA)) {
							// max = (max > tmp ? max : tmp);
							max.SetMajor(tmp);
						} else {
							// max = (max < tmp ? max : tmp);
							max.SetMenor(tmp);
						}
					}
				}
				double b;
				if (c.getFamily() > 2) {
					final PrecDouble tp = new PrecDouble(c.getAlcada());
					tp.Resta(max);
					tp.SetPositiu();

					b = c.setAglomeracio(tp.parserToDouble());
				} else
					b = c.setAglomeracio(0.0);

				if (b < minbase1)
					minbase1 = b;
			}
			v.add(c);
		}

		// matriu d'un element, afegim la copa
		if (numelems == 1) {
			nMd = new MatriuDistancies(1, typeData);
			nMd.setDistancia(v.get(0));
			return nMd;
		}

		// nova matriu
		nMd = new MatriuDistancies(numelems, typeData);

		// distancies entre clusters
		for (int i = 0; i < numelems - 1; i++) {
			for (int ii = i + 1; ii < numelems; ii++) {
				// els calculs s'han de fer nomes si hi ha un cluster nou (agrupacio)
				if (v.get(i).isNado() || v.get(ii).isNado()) {
					// metode ha utilitzar en el calcul
					mt = this.getMethod(v.get(i), v.get(ii), mdAct);
					tmp = mt.Distancia();

					// afegim clusters i distancies
					nMd.setDistancia(v.get(i), v.get(ii), tmp);
				} else {
					// ja tenim calculada la distancia entre els dos clusters
					tmp = mdAct.getDistancia(v.get(i), v.get(ii));
					// afegim clusters i distancies
					nMd.setDistancia(v.get(i), v.get(ii), tmp);
				}
			}
		}

		// debug
		if (FesLog.LOG.getLevel().equals(Level.FINER)) {
			System.out.println("\nMariu Creada.\n");
			for (int i = 0; i < numelems - 1; i++) {
				for (int ii = i + 1; ii < numelems; ii++) {
					FesLog.LOG.finer("Distancia entre "
							+ nMd.getCluster(i).getId()
							+ " i "
							+ nMd.getCluster(ii).getId()
							+ " = "
							+ nMd.getDistancia(nMd.getCluster(i),
									nMd.getCluster(ii)));
				}
			}
			System.out.println("\n");
		}

		nMd.getArrel().setBase(minbase1);

		return nMd;
	}

	private Method getMethod(final Cluster ci, final Cluster cj,
			final MatriuDistancies md) {
		Method m;
		if (method.equals(metodo.SINGLE_LINKAGE)) {
			m = new SingleLinkage(ci, cj, md);
		} else if (method.equals(metodo.COMPLETE_LINKAGE)) {
			m = new CompleteLinkage(ci, cj, md);
		} else if (method.equals(metodo.WEIGHTED_AVERAGE)) {
			m = new WeightedAverage(ci, cj, md);
		} else if (method.equals(metodo.UNWEIGHTED_CENTROID)) {
			m = new UnweightedCentroid(ci, cj, md);
		} else if (method.equals(metodo.JOINT_BETWEEN_WITHIN)) {
			m = new JointBetweenWithin(ci, cj, md);
		} else if (method.equals(metodo.WEIGHTED_CENTROID)) {
			m = new WeightedCentroid(ci, cj, md);
		} else if (method.equals(metodo.UNWEIGHTED_AVERAGE)) {
			m = new UnweightedAverage(ci, cj, md);
		} else {
			m = null;
		}

		return m;
	}

	private LinkedHashMap<Integer, Cluster> NousClusters(
			final Vector<Integer> grup) throws Exception {
		Cluster sc;
		final LinkedHashMap<Integer, Cluster> lhm = new LinkedHashMap<Integer, Cluster>();
		Integer id;

		for (int i = 0; i < grup.size(); i++) {
			id = grup.get(i);

			if (id == grupNul) {
				mdAct.getCluster(i).isNado(false);
				lhm.put(mdAct.getCluster(i).hashCode(), mdAct.getCluster(i));
			} else {
				// s'afegeix el cluster al supercluster corresponent
				if (lhm.containsKey(id)) {
					lhm.get(id).addCluster(mdAct.getCluster(i));
				} else {
					// en ell el primer cluster de l'agrupacio
					sc = new Cluster();

					// alcada de l'agrupacio
					if (typeData.equals(tipusDades.DISTANCIA)) {
						sc.setAlcada(valmin);
					} else {
						sc.setAlcada(valmax);
					}

					// afegeixo el cluster a al supercluster
					sc.addCluster(mdAct.getCluster(i));

					lhm.put(grup.get(i), sc);
				}
			}
		}

		return lhm;
	}

}
