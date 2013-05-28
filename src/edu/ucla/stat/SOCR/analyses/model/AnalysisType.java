/****************************************************
Statistics Online Computational Resource (SOCR)
http://www.StatisticsResource.org
 
All SOCR programs, materials, tools and resources are developed by and freely disseminated to the entire community.
Users may revise, extend, redistribute, modify under the terms of the Lesser GNU General Public License
as published by the Open Source Initiative http://opensource.org/licenses/. All efforts should be made to develop and distribute
factually correct, useful, portable and extensible resource all available in all digital formats for free over the Internet.
 
SOCR resources are distributed in the hope that they will be useful, but without
any warranty; without any explicit, implicit or implied warranty for merchantability or
fitness for a particular purpose. See the GNU Lesser General Public License for
more details see http://opensource.org/licenses/lgpl-license.php.
 
http://www.SOCR.ucla.edu
http://wiki.stat.ucla.edu/socr
 It s Online, Therefore, It Exists! 
****************************************************/
package edu.ucla.stat.SOCR.analyses.model;

import edu.ucla.stat.SOCR.analyses.exception.WrongAnalysisException;

public class AnalysisType {
	public static final short SIMPLE_LINEAR_REGRESSION = 11;
	public static final short MULTI_LINEAR_REGRESSION = 12;
	public static final short LOGISTIC_REGRESSION = 13;
        public static final short Clustering = 14;

	public static final short ANOVA_ONE_WAY = 21;
	public static final short ANOVA_TWO_WAY = 22;
	public static final short ANOVA_TWO_WAY_INTERACTION = 23;
	public static final short ANOVA_THREE_WAY = 24;
	public static final short ANCOVA = 29;

	public static final short NORMAL_POWER = 49;

	public static final short ONE_T = 50;
        public static final short ONE_Z = 51;
	public static final short TWO_INDEPENDENT_T = 52;
	public static final short TWO_PAIRED_T = 53;
	public static final short TWO_INDEPENDENT_WILCOXON = 54;
	public static final short TWO_PAIRED_SIGNED_RANK = 55; // Wilcoxon Signed Rank
	public static final short TWO_PAIRED_SIGN_TEST = 56; // Sign-Test
	public static final short TWO_INDEPENDENT_KRUSKAL_WALLIS = 57; // Kruskal-Wallis
	public static final short TWO_INDEPENDENT_FRIEDMAN = 58; // Friedman
        
        public static final short PRINCIPAL_COMPONENT_ANALYSIS = 59; // PCA
        
	public static final short CHI_SQUARE_MODEL_FIT = 61;
	public static final short DICHOTOMOUS_PROPORTION = 62; // DICHOTOMOUS_PROPORTION_TEST
	public static final short CHI_SQUARE_CONTINGENCY_TABLE = 63;
	public static final short FISHER_EXACT = 64;

	public static final short SURVIVAL = 71;
	public static final short TIME_SERIES_ARIMA = 72;
	public static final short KOLMOGOROV_SMIRNOFF = 73;
	public static final short FLIGNER_KILLEEN = 74;
	
	public static final short CI = 81;

	public static String lookup(short type) throws WrongAnalysisException {
		String analysisClass = "edu.ucla.stat.SOCR.analyses.model.";
		//change this to read from XML file
		switch (type) {
			case SIMPLE_LINEAR_REGRESSION:
				analysisClass = analysisClass + "SimpleLinearRegression";
				break;
			case MULTI_LINEAR_REGRESSION:
				analysisClass = analysisClass + "MultiLinearRegression";
				break;
			case LOGISTIC_REGRESSION:
				analysisClass = analysisClass + "LogisticRegression";
				break;
			case ANOVA_ONE_WAY:
				analysisClass = analysisClass + "AnovaOneWay";
				break;

			case ANOVA_TWO_WAY:
				analysisClass = analysisClass + "AnovaTwoWay";
				break;
/*
			case ANOVA_TWO_WAY_INTERACTION:
				analysisClass = analysisClass + "SimpleAnova";
				break;
*/


			case NORMAL_POWER:
				analysisClass = analysisClass + "NormalPower";
				break;
			case ONE_T:
				analysisClass = analysisClass + "OneT";
				break;
                        case ONE_Z:
				analysisClass = analysisClass + "OneZ";
				break;
			case SURVIVAL:
				analysisClass = analysisClass + "Survival";
				break;
			case TWO_INDEPENDENT_T:
				analysisClass = analysisClass + "TwoIndependentT";
				break;
			case TWO_INDEPENDENT_WILCOXON:
				analysisClass = analysisClass + "TwoIndependentWilcoxon";
				break;
			case TWO_PAIRED_T:
				analysisClass = analysisClass + "TwoPairedT";
				break;
			case TWO_PAIRED_SIGNED_RANK:
				analysisClass = analysisClass + "TwoPairedSignedRank";
				break;
			case TWO_PAIRED_SIGN_TEST:
				analysisClass = analysisClass + "TwoPairedSignTest";
				break;
			case TWO_INDEPENDENT_KRUSKAL_WALLIS:
				analysisClass = analysisClass + "TwoIndependentKruskalWallis";
				break;
			case TWO_INDEPENDENT_FRIEDMAN:
				analysisClass = analysisClass + "TwoIndependentFriedman";
				break;
			case DICHOTOMOUS_PROPORTION:
				analysisClass = analysisClass + "DichotomousProportion";
				break;
			case CHI_SQUARE_CONTINGENCY_TABLE:
				analysisClass = analysisClass + "ChiSquareContingencyTable";
				break;

			case FISHER_EXACT:
				analysisClass = analysisClass + "FisherExact";
				break;
			case KOLMOGOROV_SMIRNOFF:
				analysisClass = analysisClass + "KolmogorovSmirnoff";
				break;
			case FLIGNER_KILLEEN:
				analysisClass = analysisClass + "FlignerKilleen";
				break;
			case CI:
				analysisClass = analysisClass + "ConfidenceInterval";
				break;
				
			default: analysisClass = null;

		}
		//System.out.println("In AnalysisType " + analysisClass);
		if (analysisClass == null)
			throw new WrongAnalysisException("The analysis requestged does not exist.");
		return analysisClass;
	}

}
