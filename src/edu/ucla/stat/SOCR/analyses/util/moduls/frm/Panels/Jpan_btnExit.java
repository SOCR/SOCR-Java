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

package edu.ucla.stat.SOCR.analyses.util.moduls.frm.Panels;

import edu.ucla.stat.SOCR.analyses.util.inicial.Language;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import edu.ucla.stat.SOCR.analyses.util.moduls.frm.FrmPrincipalDesk;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.children.AboutBox;

// added to enalbe "Frame"

import edu.ucla.stat.SOCR.analyses.gui.Analysis;
import edu.ucla.stat.SOCR.analyses.gui.AnalysisPanel;
import edu.ucla.stat.SOCR.gui.OKDialog;
import edu.ucla.stat.SOCR.util.EditableHeader;

import java.awt.*;
//import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.*;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * About and Exit buttons
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Jpan_btnExit extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JButton btnExit, btnInfo;
	private final FrmPrincipalDesk fr;
	private String sBtn1, sBtn2;

	private void CarregaIdioma() {
		sBtn1 = Language.getLabel(46); // exit
		sBtn2 = "Info";
	}

	public Jpan_btnExit(final FrmPrincipalDesk fr) {
		super();
		this.fr = fr;
		this.CarregaIdioma();
		this.getPanel();
		this.setVisible(true);
	}

	private void getPanel() {

		this.setLayout(new FlowLayout());
		this.setBorder(BorderFactory.createTitledBorder(""));

		// btn Info
		btnInfo = new JButton(sBtn2);
		btnInfo.addActionListener(this);
		this.add(btnInfo);

		// btn Exit
		btnExit = new JButton(sBtn1);
		btnExit.addActionListener(this);
		this.add(btnExit);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals(sBtn1)) { // sortir
			fr.toGoOut();
		}
		else if (e.getActionCommand().equals(sBtn2)) { // info
                     Frame fDialog = new Frame();  // added as dummy frame
			AboutBox a = new AboutBox(fDialog);
			a.setVisible(true);
                        fDialog.setVisible(true);
		}
	}
}
