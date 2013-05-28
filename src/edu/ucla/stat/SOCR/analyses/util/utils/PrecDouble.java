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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Simplified interface to BigDecimal
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class PrecDouble {

	private BigDecimal bd = new BigDecimal("0.0");

	public PrecDouble(final String num) {
		this.inicialitzaValor(num);
	}

	public PrecDouble(final Double num) {
		this.inicialitzaValor(num.toString());
	}

	public PrecDouble(final Float num) {
		this.inicialitzaValor(num.toString());
	}

	public PrecDouble(final Integer num) {
		this.inicialitzaValor(num.toString());
	}

	public PrecDouble(final BigDecimal num) {
		bd = num;
	}

	private void inicialitzaValor(final String num) {
		try {
			bd = new BigDecimal(num);
		} catch (final Exception e) {
			System.out.println(e.getMessage());
			e.getStackTrace();
			bd = new BigDecimal("0.0");
		}
	}

	public void Suma(final BigDecimal num) {
		bd = bd.add(num);
	}

	public void Suma(final PrecDouble num) {
		this.Suma(num.parserToBigDecimal());
	}

	public void Suma(final int num) {
		this.Suma(new BigDecimal(Integer.toString(num)));
	}

	public void Suma(final double num) {
		this.Suma(new BigDecimal(Double.toString(num)));
	}

	public void Suma(final String num) {
		this.Suma(new BigDecimal(num));
	}

	public void Resta(final BigDecimal num) {
		bd = bd.subtract(num);
	}

	public void Resta(final PrecDouble num) {
		this.Resta(num.parserToBigDecimal());
	}

	public void Resta(final int num) {
		this.Resta(new BigDecimal(Integer.toString(num)));
	}

	public void Resta(final double num) {
		this.Resta(new BigDecimal(Double.toString(num)));
	}

	public void Resta(final String num) {
		this.Resta(new BigDecimal(num));
	}

	public void Producto(final BigDecimal num) {
		bd = bd.multiply(num);
	}

	public void Producto(final PrecDouble num) {
		this.Producto(num.parserToBigDecimal());
	}

	public void Producto(final int num) {
		this.Producto(new BigDecimal(Integer.toString(num)));
	}

	public void Producto(final double num) {
		this.Producto(new BigDecimal(Double.toString(num)));
	}

	public void Producto(final String num) {
		this.Producto(new BigDecimal(num));
	}

	public void Division(final BigDecimal num) {
		bd = bd.divide(num, new MathContext(100));
	}

	public void Division(final PrecDouble num) {
		this.Division(num.parserToBigDecimal());
	}

	public void Division(final int num) {
		this.Division(new BigDecimal(Integer.toString(num)));
	}

	public void Division(final double num) {
		this.Division(new BigDecimal(Double.toString(num)));
	}

	public void Division(final String num) {
		this.Division(new BigDecimal(num));
	}

	public void Pow(final int pot) {
		bd = bd.pow(pot);
	}

	public void Plus() {
		bd = bd.plus();
	}

	public void CanviSigne() {
		bd = bd.negate();
	}

	public void SetPositiu() {
		bd = bd.abs();
	}

	public void SetMajor(final BigDecimal num) {
		if (num.compareTo(bd) > 0) {
			bd = num;
		}
	}

	public void SetMajor(final PrecDouble num) {
		this.SetMajor(num.parserToBigDecimal());
	}

	public void SetMajor(final double num) {
		this.SetMajor(new BigDecimal(Double.toString(num)));
	}

	public void SetMajor(final int num) {
		this.SetMajor(new BigDecimal(Integer.toString(num)));
	}

	public void SetMajor(final String num) {
		this.SetMajor(new BigDecimal(num));
	}

	public void SetMenor(final BigDecimal num) {
		if (num.compareTo(bd) < 0) {
			bd = num;
		}
	}

	public void SetMenor(final PrecDouble num) {
		this.SetMenor(num.parserToBigDecimal());
	}

	public void SetMenor(final double num) {
		this.SetMenor(new BigDecimal(Double.toString(num)));
	}

	public void SetMenor(final int num) {
		this.SetMenor(new BigDecimal(Integer.toString(num)));
	}

	public void SetMenor(final String num) {
		this.SetMenor(new BigDecimal(num));
	}

	public boolean equals(final BigDecimal num) {
		return bd.compareTo(num) == 0;
	}

	public boolean equals(final PrecDouble num) {
		return this.equals(num.parserToBigDecimal());
	}

	public boolean equals(final int num) {
		return this.equals(new BigDecimal(Integer.toString(num)));
	}

	public boolean equals(final double num) {
		return this.equals(new BigDecimal(Double.toString(num)));
	}

	public boolean equals(final String num) {
		return this.equals(new BigDecimal(num));
	}

	public int CompareTo(final BigDecimal num) {
		return bd.compareTo(num);
	}

	public int CompareTo(final PrecDouble num) {
		return this.CompareTo(num.parserToBigDecimal());
	}

	public int CompareTo(final int num) {
		return this.CompareTo(new BigDecimal(Integer.toString(num)));
	}

	public int CompareTo(final double num) {
		return this.CompareTo(new BigDecimal(Double.toString(num)));
	}

	public int CompareTo(final String num) {
		return this.CompareTo(new BigDecimal(num));
	}

	public boolean isMenor(final BigDecimal num) {
		return (bd.compareTo(num) < 0);
	}

	public boolean isMenor(final PrecDouble num) {
		return this.isMenor(num.parserToBigDecimal());
	}

	public boolean isMenor(final int num) {
		return this.isMenor(new BigDecimal(Integer.toString(num)));
	}

	public boolean isMenor(final double num) {
		return this.isMenor(new BigDecimal(Double.toString(num)));
	}

	public boolean isMenor(final String num) {
		return this.isMenor(new BigDecimal(num));
	}

	public boolean isMajor(final BigDecimal num) {
		return (bd.compareTo(num) > 0);
	}

	public boolean isMajor(final PrecDouble num) {
		return this.isMajor(num.parserToBigDecimal());
	}

	public boolean isMajor(final int num) {
		return this.isMajor(new BigDecimal(Integer.toString(num)));
	}

	public boolean isMajor(final double num) {
		return this.isMajor(new BigDecimal(Double.toString(num)));
	}

	public boolean isMajor(final String num) {
		return this.isMenor(new BigDecimal(num));
	}

	public double parserToDouble() {
		return bd.doubleValue();
	}

	public int parserToInteger() {
		return bd.intValueExact();
	}

	public BigDecimal parserToBigDecimal() {
		return bd;
	}

	public void setPrecisio(final int prec) {
		bd = bd.setScale(prec, BigDecimal.ROUND_HALF_UP);
	}

	public void setPrecisio(final int prec, final RoundingMode round) {
		bd = bd.setScale(prec, round);
	}

	public int getPrecisio() {
		return bd.scale();
	}

	@Override
	public String toString() { // Puede usar notacion cientifica plain no
		return bd.toString();
	}

	public String toPlainString() {
		return bd.toPlainString(); // no usa notacion cientifica
	}

	public String toString(final String patron) {
		String cad;
		final DecimalFormat formato = new DecimalFormat(patron);
		try {
			cad = formato.format(bd);
		} catch (final Exception e) {
			cad = bd.toString();
		}
		return cad;
	}

	@Override
	public PrecDouble clone() {
		final PrecDouble pd_clon = new PrecDouble(bd);
		return pd_clon;
	}

}
