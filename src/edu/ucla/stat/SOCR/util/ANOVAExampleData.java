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

package edu.ucla.stat.SOCR.util;

import javax.swing.JTable;


/** Example data generator for ANOVA & other statistical applets. */
public class ANOVAExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator"); 
		 // system-independent line terminator
	public JTable dataTable;

	/** constructor method for t-Test example data */
	public  ANOVAExampleData(int index) {
		columnNames = new String[2];

		switch (index) {
		  case 1 :  columnNames[0]="Control Formula";
				columnNames[1] = "New! Improved Formula!";
		    break;
		  case 2 :  columnNames[0]="Unexposed stength";
				columnNames[1] = "Strength after exposure!";
		    break;
		  case 3 : 	columnNames[0]="Stabilizer A";
				columnNames[1] = "Stabilizer B";
		    break;
		  case 4 : 	columnNames[0]="Our Formula";
				columnNames[1] = "Competitive Formula";
		    break;
		  case 5 :  columnNames[0]="Lab Sample";
				columnNames[1] = "Production Sample";
		    break;
		  case 6 : 	columnNames[0]="New Lot";
				columnNames[1] = "Old Lot";
		    break;
		  case 7 : 	columnNames[0]="Dick's formula";
				columnNames[1] = "Warren's formula";
		    break;
		}
		
		// Now we generate some data
		int i = 0;
		int size = (int) (Math.random() * 15) + 3;
		example = new String[2][size];

		double theMiddle = Math.random() * 10 + 5;
		double center1 = theMiddle + Math.random() * 2 - 1;
		double center2 = theMiddle + Math.random() * 2 - 1;
		double spread1 = Math.random() * 2;
		double spread2 = Math.random() * 2.5;
		Double dummy = new Double(0);
		for (i = 0; (i <= size); i++) {
			 dummy = new Double(center1 + Math.random() * spread1);
			 example[0][i] = dummy.toString();
			 dummy = new Double(center2 + Math.random() * spread2);
			 example[1][i] = dummy.toString();
		}
		dataTable = new JTable(example, columnNames); 
	}

	/** Constructor method for simple data generation for regression/correlation tests*/
	public  ANOVAExampleData() {
		StringBuffer f3 = new StringBuffer();
		
		//now let's generate some data
		int i = 0;
		int size = (int) (Math.random() * 15) + 3;
		example = new String[2][size];
		columnNames = new String[2];

		columnNames[0]="Y";
		columnNames[1] = "X";

		double b = Math.random()*2;
		double c = Math.random()*.05;
		double d = Math.random()*0.05-0.025;
		double e = Math.random()*0.05-0.025;
		double coeff = Math.random()*3+1;
		double x = 0;
		double y = 0;
		double yy = 0;
		double center2 = 0;
		double spread2 = 0;
		Double dummy = new Double(0);

		for (i = 0; i < size*coeff; i=i+(int)coeff) {
			 x = Math.abs(Math.random()*10-5 + Math.pow(c*i,2) + (b+Math.random()*0.5-0.25)*i);
			 y = Math.abs(i + Math.random()*4-2);
			 yy = y + e*Math.pow(x,2) + d*Math.pow(x,3);
			 example[0][(int)(i/coeff)] = String.valueOf(x);
			 dummy = new Double(center2 + Math.random() * spread2);
			 example[1][(int)(i/coeff)] = String.valueOf(yy);
		}
		dataTable = new JTable(example, columnNames); 
	}

	/** Constructor method for sampel data for ANOVA analysis.*/
	public  ANOVAExampleData(int index, int dummy) {
		
		switch (index) {
		  case 1 : {example = new String[12][2];
				columnNames = new String[2];

				columnNames[0]="Dependent";
				columnNames[1] = "Factor A";


		  		//example =
		    		//"An example one-way ANOVA with replication" + newln +
		    		//"(note that there are not an equal number of 
				//replicates for each level)" + newln +
				example[0][0]  = "7.0"; example[0][1] = "1";
				example[1][0]  = "11.0"; example[1][1] = "1";
				example[2][0]  = "9.0"; example[2][1] = "1";
				example[3][0]  = "4.0"; example[3][1] = "2";
				example[4][0]  = "6.0"; example[4][1] = "2";
				example[5][0]  = "8.0"; example[5][1] = "2";
				example[6][0]  = "5.0"; example[6][1] = "2";
				example[7][0]  = "2.0"; example[7][1] = "2";
				example[8][0]  = "10.0"; example[8][1] = "3";
				example[9][0]  = "8.0"; example[9][1] = "3";
				example[10][0]  = "6.0"; example[10][1] = "3";
				example[11][0]  = "8.0"; example[11][1] = "3";

				dataTable = new JTable(example, columnNames); 
			    }
			break;

		  case 2 : {example = new String[40][3];
				columnNames = new String[3];

				columnNames[0]="Dependent";
				columnNames[1] = "Factor A";
				columnNames[2] = "Factor B";

		    		//example =
		    		//"An example two-way ANOVA with replication" + newln +
				//"Dependent  Factor A  Factor B " + newln +
				example[0][0] = "6.0"; example[0][1] = "1"; example[0][2] = "1";
				example[1][0] = "4.0"; example[1][1] = "1"; example[1][2] = "1";
				example[2][0] = "5.0"; example[2][1] = "1"; example[2][2] = "1";
				example[3][0] = "5.0"; example[3][1] = "1"; example[3][2] = "1";
				example[4][0] = "4.0"; example[4][1] = "1"; example[4][2] = "1";
				example[5][0] = "5.0"; example[5][1] = "1"; example[5][2] = "2";
				example[6][0] = "7.0"; example[6][1] = "1"; example[6][2] = "2";
				example[7][0] = "4.0"; example[7][1] = "1"; example[7][2] = "2";
				example[8][0] = "6.0"; example[8][1] = "1"; example[8][2] = "2";
				example[9][0] = "8.0"; example[9][1] = "1"; example[9][2] = "2";
				example[10][0] = "10.0"; example[10][1] = "2"; example[10][2] = "1";
				example[11][0] = "8.0"; example[11][1] = "2"; example[11][2] = "1";
				example[12][0] = "7.0"; example[12][1] = "2"; example[12][2] = "1";
				example[13][0] = "7.0"; example[13][1] = "2"; example[13][2] = "1";
				example[14][0] = "9.0"; example[14][1] = "2"; example[14][2] = "1";
				example[15][0] = "7.0"; example[15][1] = "2"; example[15][2] = "2";
				example[16][0] = "9.0"; example[16][1] = "2"; example[16][2] = "2";
				example[17][0] = "12.0"; example[17][1] = "2"; example[17][2] = "2";
				example[18][0] = "8.0"; example[18][1] = "2"; example[18][2] = "2";
				example[19][0] = "8.0"; example[19][1] = "2"; example[19][2] = "2";
				example[20][0] = "7.0"; example[20][1] = "3"; example[20][2] = "1";
				example[21][0] = "5.0"; example[21][1] = "3"; example[21][2] = "1";
				example[22][0] = "6.0"; example[22][1] = "3"; example[22][2] = "1";
				example[23][0] = "5.0"; example[23][1] = "3"; example[23][2] = "1";
				example[24][0] = "9.0"; example[24][1] = "3"; example[24][2] = "1";
				example[25][0] = "9.0"; example[25][1] = "3"; example[25][2] = "2";
				example[26][0] = "9.0"; example[26][1] = "3"; example[26][2] = "2";
				example[27][0] = "7.0"; example[27][1] = "3"; example[27][2] = "2";
				example[28][0] = "5.0"; example[28][1] = "3"; example[28][2] = "2";
				example[29][0] = "4.0"; example[29][1] = "3"; example[29][2] = "2";
				example[30][0] = "8.0"; example[30][1] = "4"; example[30][2] = "1";
				example[31][0] = "4.0"; example[31][1] = "4"; example[31][2] = "1";
				example[32][0] = "6.0"; example[32][1] = "4"; example[32][2] = "1";
				example[33][0] = "5.0"; example[33][1] = "4"; example[33][2] = "1";
				example[34][0] = "5.0"; example[34][1] = "4"; example[34][2] = "1";
				example[35][0] = "5.0"; example[35][1] = "4"; example[35][2] = "2";
				example[36][0] = "7.0"; example[36][1] = "4"; example[36][2] = "2";
				example[37][0] = "9.0"; example[37][1] = "4"; example[37][2] = "2";
				example[38][0] = "7.0"; example[38][1] = "4"; example[38][2] = "2";
				example[39][0] = "10.0"; example[39][1] = "4"; example[39][2] = "2";

				dataTable = new JTable(example, columnNames); 
			   }
		    break;

		  case 3 : {example = new String[12][3];
				columnNames = new String[3];

				columnNames[0]="Dependent";
				columnNames[1] = "Factor A";
				columnNames[2] = "Factor B";

		  		//example =
		  		//"An example two-way ANOVA without replication"+ newln +
				//"Dependent  Factor A  Factor B " + newln +
				example[0][0] = "4.5"; example[0][1] = "1"; example[0][2] = "1";
				example[1][0] = "6.4"; example[1][1] = "1"; example[1][2] = "2";
				example[2][0] = "7.2"; example[2][1] = "1"; example[2][2] = "3";
				example[3][0] = "6.7"; example[3][1] = "1"; example[3][2] = "4";
				example[4][0] = "8.8"; example[4][1] = "2"; example[4][2] = "1";
				example[5][0] = "7.8"; example[5][1] = "2"; example[5][2] = "2";
				example[6][0] = "9.6"; example[6][1] = "2"; example[6][2] = "3";
				example[7][0] = "7.0"; example[7][1] = "2"; example[7][2] = "4";
				example[8][0] = "5.9"; example[8][1] = "3"; example[8][2] = "1";
				example[9][0] = "6.8"; example[9][1] = "3"; example[9][2] = "2";
				example[10][0] = "5.7"; example[10][1] = "3"; example[10][2] = "3";
				example[11][0] = "5.2"; example[11][1] = "3"; example[11][2] = "4";

				dataTable = new JTable(example, columnNames); 
			   }
			break;

		  case 4 : {example = new String[50][3];
				columnNames = new String[3];

				columnNames[0]="Dependent";
				columnNames[1] = "Factor A";
				columnNames[2] = "Factor B";

		    		//example =
		    		//"A TOTALLY RANDOMLY GENERATED example of a two-way ANOVA
				// with replication" + newln +
				
				for (int k = 0; k < 5; k++)
				{  for (int i = 0; i < 2; i++)
				   {	for (int replicate=0; replicate <5; replicate ++)
					{  int temp = (int) (Math.random()*100 -50);

					   example[k*10+i*5+replicate][0] = String.valueOf(temp);
				  	   example[k*10+i*5+replicate][1] = String.valueOf(k); 
					   example[k*10+i*5+replicate][2] = String.valueOf(i); 
					 }
				    }
				 }
				
				dataTable = new JTable(example, columnNames); 
			   }
		    break;

		}
	}

	/** returns a JTable object containing the Example Data */
	public JTable getExample()
	{
		return dataTable;
	 }

}
