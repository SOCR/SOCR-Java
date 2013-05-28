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

package edu.ucla.stat.SOCR.analyses.util.inicial;

import edu.ucla.stat.SOCR.analyses.util.inicial.FesLog.TipLog;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import edu.ucla.stat.SOCR.analyses.util.moduls.frm.FrmPrincipalDesk;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Main of MultiDendrograms application
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Dendrograma {

	public Dendrograma() {
		final String title = Parametres_Inicials.getTitolDesk();
		FrmPrincipalDesk f = new FrmPrincipalDesk(title);
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ventana = f.getSize();
		f.setLocation((pantalla.width - ventana.width) / 2,
				(pantalla.height - ventana.height) / 2);
		f.setVisible(true);
	}

	public static void main(final String[] args) throws Exception {
		boolean argsCorrectes = true;
		AlmacenPropiedades ap;
		TipLog tip_log = FesLog.TipLog.XML;
		Level l = Level.WARNING;
		String arg;

		for (int i = 0; i < args.length; i += 2) {
			arg = args[i].toUpperCase();
			if (arg.equals("-H") || arg.equals("-HELP")) {
				MostraSintaxis();
				argsCorrectes = false;
			} else if (arg.equals("-XML")) {
				tip_log = FesLog.TipLog.XML;
			} else if (arg.equals("-TXT")) {
				tip_log = FesLog.TipLog.TXT;
			} else if (arg.equals("-LEVEL")) {
				try {
					arg = args[i + 1].toUpperCase();
					if (arg.equals("OFF"))
						l = Level.OFF;
					else if (arg.equals("SEVERE"))
						l = Level.SEVERE;
					else if (arg.equals("WARNING"))
						l = Level.WARNING;
					else if (arg.equals("INFO"))
						l = Level.INFO;
					else if (arg.equals("CONFIG"))
						l = Level.CONFIG;
					else if (arg.equals("FINE"))
						l = Level.FINE;
					else if (arg.equals("FINER"))
						l = Level.FINER;
					else if (arg.equals("FINEST"))
						l = Level.FINEST;
					else if (arg.equals("ALL"))
						l = Level.ALL;
					else {
						MostraSintaxis();
						argsCorrectes = false;
					}
				} catch (Exception e) {
					MostraSintaxis();
					argsCorrectes = false;
                                        e.printStackTrace();
				}
			} else {
				MostraSintaxis();
				argsCorrectes = false;
			}
		}

		if (argsCorrectes) {
			new FesLog("logs/dendograma_log.xml", tip_log);
			FesLog.LOG.setLevel(l);
			FesLog.LOG.fine("Inici Programa");
			try {
				ap = new AlmacenPropiedades();
				final Parametres_Inicials p = new Parametres_Inicials();
				p.setParametres(ap);
			} catch (Exception e) {
				FesLog.LOG.severe(e.getMessage() + " " + e);
                                e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage(),
						"Multidendrograms", JOptionPane.OK_OPTION);
			}
			try {
				new Language(Parametres_Inicials.getSPath_idioma());
				FesLog.LOG.config("Carregat Idioma: "
						+ Parametres_Inicials.getSPath_idioma());
			} catch (Exception e) {
				FesLog.LOG.warning("Es carrega l'idioma per defecte");
				JOptionPane.showMessageDialog(null, e.getMessage(),
						"Multidendrograms", JOptionPane.OK_OPTION);
                                e.printStackTrace();
			}
			new Dendrograma();
		}
	}

	private static void MostraSintaxis() {
		System.out.println("Use: java jar multidendograms.jar [options]");
		System.out
				.println("\t-level NIVELL   log level, LEVEL = {OFF, SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, ALL}");
		System.out.println("\t-p      log file direction");
		System.out.println("\t-h      Syntax help");
		System.out.println("\n\t(example) jar multidendograms.jar -level OFF");
	}

}
