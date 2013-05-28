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
package edu.ucla.stat.SOCR.analyses.command;
import java.io.BufferedReader;
import java.io.*;
import java.util.*;

import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.result.*;

/*
 * This class provides an image-volume based MLR analysis for 1D, 2D and 3D imaging volumes, based
 * on user specified regressors/predictors. These are provided as a Tab-Separated input
 * Design-matrix file in this format:
 * 
 * #SUBJECT ID	FILENAME												SEX	GROUP_ID	AGE		CDR	MMSE
 * 002_S_0413	/ifs/adni/3T/PERMS/IVO/JACOBIANS/3T_bl_002_S_0413.img	F	Normal		76.38	0	29
 * 
 * Building: via the SOCR build.xml file, as part of SOCR Analyses
 * 
 * Invocation/Call: 
 * java -cp /ifs/ccb/CCB_SW_Tools/others/Statistics/SOCR_Statistics/bin/SOCR_core.jar:
 * 			/ifs/ccb/CCB_SW_Tools/others/Statistics/SOCR_Statistics/bin/SOCR_plugin.jar 
 * 			edu.ucla.stat.SOCR.analyses.command.VolumeMultipleRegression 
 * 			-dm DesignMatrix.txt -h -regressors [name1,name2,...name_k]
 * 			-dim Zmax Ymax XMax [-p filename] [-r filename] -data_type [1,4]
 * 
 * Options:
 * 			-help: print usage
 * 			-dm [DesignMatrix.txt]: specify a tab-separated text file containing the desing matrix
 * 			-mask [Mask-volume.img]: specify a mask-volume (0 or 1 intensities) restricting the voxels
 * 										where the regression models are computed (optional),
 * 										1Byte Analyze format volume of the same dimensions as the data 
 * 			-h: 					DesignMatrix contains a header (first row)
 * 			-regressors [name1,name2,...,name_k]: specify which columns/variables should be used as regressors/covariates
 * 			-dim Zmax Ymax XMax: 	specify the dimension-sizes (for 2D images use ZMax=1, for 1D, Zmax=Y_Max=1
 * 			-p [Pvalue_Filename]: 	output the p-value volume
 * 			-r [Rvalue_Filename]:	output the effect-size/correlation volume
 * 			-t [Tstat_Filename]:	output the T-Statistics for the effect-size Beta
 * 			-data_type [0,1,2,3,4]:	Type=0 is for Unsinged Byte input volumes; 
 * 									Type=1 is for Signed Byte input volumes; 
 * 									Type=2 is for Unsigned-short integers
 * 									Type=3 is for Signed-short integers
 * 									Type=4 is for 4Byte=Float Volumes
 * 			-byteswap:				Only enter this flag if you want the input data to be read in and byteswapped! 
 * 									Note that -byteswap effects: input data, mask-volume and output results!
 * 
 * Documentation: 
 * 	http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression#Volume_Multiple_Linear_Regression_Usage
 * 
 */

public class VolumeMultipleRegression {
	private static final String MISSING_MARK = ".";

	public static void main(String[] args) {
		//FileHelper fileHelper1 = new FileHelper();
		//fileHelper.openReader("data1.txt");
		String designMatrixInputFile = null; //"C:\\STAT\\SOCR\\test\\dataOne.txt\\"
		String maskInputVolume=null;		// mask input volume
		
		String pValue_Filename=null;
		String rValue_Filename=null;
		String tStat_Filename=null;
		
		String[] regressors;
			regressors = new String[1];
			regressors[0]="";
		int[] regressorColumnIndices;				// These indices contain the column-indices
			regressorColumnIndices = new int[1];	// of the user-specified regressor columns
			regressorColumnIndices[0] = 1;
		
		int[] dim = new int[3];		// the sizes of the 3D volume dimensions
		boolean outputPValuesVolumes=false;
		boolean outputREffectSizeVolumes=false;
		boolean outputTStatVolumes=false;
		
		float [][][][] imagingData;					// [xLength][Zmax][Ymax][Xmax]
		//double[][][][] pValueVolumes;				// [NumberPredictors][Zmax][Ymax][Xmax]
		//double[][][][] rEffectSizesVolumes;			// [NumberPredictors][Zmax][Ymax][Xmax]
		
		boolean [][][] maskVolumeBoolean=new boolean[1][1][1];			// [Zmax][Ymax][Xmax]
		maskVolumeBoolean[0][0][0] = false;
		
		int data_type = 4;							//image intensities data type
		
		Vector<String> inputAnalyzeFilenames = new Vector<String>();	
													// The names of the Analyze Input Files 
													// stored in column 2 of the Design-Matrix
		String regressorString = "";
		
		boolean header=false;
		boolean byteswap = false;
		boolean filesLoaded = false;
		
		int i, j,k;
		
		System.out.println(
		"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression#Volume_Multiple_Linear_Regression_Usage");

		try {
			for (i=0; i<args.length; i++) {
				if (args[i].compareToIgnoreCase("-h")==0) header=true;
				else if (args[i].compareToIgnoreCase("-data_type")==0) {
					data_type = Integer.parseInt(args[++i]);
					if (data_type!=0 && data_type!=1 && data_type!=2 && data_type!=3 && data_type!=4) {
						System.out.println("Data Type must be 0(unsigned byte volume), 1(signed byte volume), "+
								"2(unsinged short int) 3(singed short int) or 4(float volume)!\n");
						System.exit(1);
					}
				} else if (args[i].compareToIgnoreCase("-dim")==0) {
					dim[2] = Integer.parseInt(args[++i]);
					dim[1] = Integer.parseInt(args[++i]);
					dim[0] = Integer.parseInt(args[++i]);
					System.out.println("dim[2]="+dim[2]+"\tdim[1]="+dim[1]+"\tdim[0]="+dim[0]);
				} else if (args[i].compareToIgnoreCase("-p")==0) { 
					outputPValuesVolumes=true;
					pValue_Filename = args[++i];
					System.out.println("pValue_Filename="+pValue_Filename);
				} else if (args[i].compareToIgnoreCase("-byteswap")==0) { 
					byteswap=true;
					System.out.println("Byteswap="+byteswap);
				} else if (args[i].compareToIgnoreCase("-r")==0) { 
					outputREffectSizeVolumes=true;
					rValue_Filename = args[++i];
					System.out.println("rValue_Filename="+rValue_Filename);
				} else if (args[i].compareToIgnoreCase("-t")==0) { 
					outputTStatVolumes=true;
					tStat_Filename = args[++i];
					System.out.println("tStat_Filename="+tStat_Filename);
				} else if (args[i].compareToIgnoreCase("-regressors")==0) {
					regressorString = args[++i];
					System.err.println("regressorString="+regressorString);
					// Parse the regressors list
					StringTokenizer Tok = new StringTokenizer(regressorString, ",");
			        int n=0;
			        while (Tok.hasMoreElements()) { 
			        	n++;
			        	Tok.nextElement();
			        }
			        System.err.println("regressorString_length="+n);
			        
			        Tok = new StringTokenizer(regressorString, ",");
			   		regressors = new String[n];
			   		regressorColumnIndices = new int[n];
					for (k=0; k< n; k++) { 
						regressors[k] = Tok.nextToken();
						regressorColumnIndices[k] = 2+k; // First 2 columns are SubjectID/Filename
					}
					System.out.println("regressors[0]="+regressors[0]);
				} else if (args[i].compareToIgnoreCase("-dm")==0) {
					designMatrixInputFile = args[++i];
					filesLoaded = true;
					System.out.println("designMatrixInputFile="+designMatrixInputFile);
				} else if (args[i].compareToIgnoreCase("-help")==0) {
					System.out.println("Please see the Web syntax for involking this tool: \n"+
					"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression");
					return;
				}  else if (args[i].compareToIgnoreCase("-mask")==0) {
					maskInputVolume = args[++i];
					System.out.println("maskInputVolume="+maskInputVolume);
				}
			}
		} catch (Exception e) {
			System.out.println("Incorrect call 1! Please see the syntax for involking this tool\n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression#Volume_Multiple_Linear_Regression_Usage");
		}
		
		if (!filesLoaded) {
			System.out.println("Incorrect call 2! Please see the syntax for involking this tool\n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression#Volume_Multiple_Linear_Regression_Usage");
			return;
		}
				
		int independentLength=regressors.length;
		
		if (independentLength<1)
		{	System.out.println("No Independent Variable Specified (independentLength="+independentLength+
				")\n Cannot run the regression\n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression#Volume_Multiple_Linear_Regression_Usage");
			return;
		}
		
		MultiLinearRegressionResult result = null;

		StringTokenizer st = null;
		
		int designMatrixNumberColumns = 1000;	// Maximum number of DesignMatrix columns
		
		String[] input = new String [independentLength];
		int xLength=0;
		
		String[] varHeader = new String[1];
		//varHeader[1] = "var";
		
		ArrayList<String>[] xList = new ArrayList [independentLength];
		double[][] xDataArray = new double[independentLength][xLength];
		
		for (i = 0; i < xList.length; i++) 
			xList[i] = new ArrayList<String>();

		String line = null, stringTmp;
		
		boolean read=true;
		if (header)
			read=false;

		//System.err.println("\n header = " + header);
		try {
			BufferedReader bReader  = new BufferedReader(new FileReader(designMatrixInputFile));
			while ( (line = bReader.readLine()) != null) {
					st = new StringTokenizer(line, ",; \t" );
					
					if (varHeader[0]==null && header) {		// Read Header Row
						int n=0;
				        while (st.hasMoreElements()) { 
				        	n++;
				        	st.nextToken();
				        }
				        designMatrixNumberColumns = n;
				        System.err.println("Reading DM Header!\t designMatrixNumberColumns="+
				        		designMatrixNumberColumns);
						
				        st = new StringTokenizer(line, ",; \t" );
				        varHeader = new String[n];
				   		for (k=0; k< designMatrixNumberColumns; k++) { 
				   			varHeader[k] = st.nextToken().trim();
							for (j=0; j<independentLength; j++) { 
								if (varHeader[k].compareToIgnoreCase(regressors[j])==0) {
									regressorColumnIndices[j] = k;
									System.err.println("varHeader["+k+"]="+varHeader[k]+
											"\t regressorColumnIndices["+j+"] ="+ k);
								}
							}
						}
				   		read=true;
					} else if (!header) {		// Must have a header row to deduce the predictors
						System.out.println("No Independent Variable Specified 1. Cannot run the regression\n"+
						"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression#Volume_Multiple_Linear_Regression_Usage");
						return;
					} else {					// READ DATA From Design-Matrix
						try {
							int n = 0, m=0;
							
							while (st.hasMoreElements()) {
								// Skip first column of Design-Matrix - it contains SubjectID
								stringTmp = st.nextToken().trim();
								if (n==1)	{ // Read IMG/ANALYZE File Name
									inputAnalyzeFilenames.addElement(stringTmp);
								}
								else if (n==regressorColumnIndices[m]) {
									input[m] = stringTmp;
									if (read && !input[m].equalsIgnoreCase(MISSING_MARK) ) {
										xList[m].add(input[m]);
									}
									// System.out.println("column="+n+"\t input["+m+"]="+input[m]);
									m++;
								} 
								++n;
							}
							read=true;
						} catch (NoSuchElementException e) {
							//e.printStackTrace();
							System.out.println(Utility.getErrorMessage("Volume Multiple Regression Analysis: 1"));
							return;
						} catch (Exception e) {
							//e.printStackTrace();
							System.out.println(Utility.getErrorMessage("Volume Multiple Regression Analysis: 2"));
							return;
						}
					}
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		if (!header)
		{	System.out.println("No Independent Variable Specified 2. Cannot run the regression\n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression#Volume_Multiple_Linear_Regression_Usage");
			return;
		}
		
		xLength=inputAnalyzeFilenames.size();
		System.out.println("Sample size(number of volumes!) = " + xLength);

		double[] xData = null;
		double[] yData = new double[xLength];
		// Open VOlume Files
		
        //create streams of the Analyze Image files
        FileInputStream[] fi = new FileInputStream[xLength];
        BufferedInputStream[] bi = new BufferedInputStream[xLength];
        DataInputStream[] in = new DataInputStream[xLength];
        for (i = 0; i< xLength; i++) {
        	try { 
        		fi[i] = new FileInputStream(inputAnalyzeFilenames.elementAt(i));
        		bi[i] = new BufferedInputStream(fi[i]);
        		in[i] = new DataInputStream(bi[i]);
        	} catch (FileNotFoundException e) { 
        		
        	}
        }
        imagingData = new float[xLength][dim[2]][dim[1]][dim[0]];
        if (maskInputVolume!=null) { 
        	try {
        		maskVolumeBoolean = new boolean [dim[2]][dim[1]][dim[0]];
        		FileInputStream fis = new FileInputStream(maskInputVolume);
        		BufferedInputStream bis = new BufferedInputStream(fis);
        		DataInputStream dis = new DataInputStream(bis);
        		int tmpByte;
            
        		for (i=0; i< dim[2]; i++) {
        			for(j = 0; j < dim[1]; j++) {
        				for(k = 0; k < dim[0]; k++) {
        					try { 
        						if (byteswap==false) tmpByte = dis.readUnsignedByte();
        						else tmpByte = swap((byte)dis.readUnsignedByte());
        						//System.err.println("Mask_tmpByte="+tmpByte+" ");
        						if (tmpByte > 0) maskVolumeBoolean[i][j][k] = true;
        						else maskVolumeBoolean[i][j][k] = false;
        					} catch (IOException e) {
        						maskVolumeBoolean[i][j][k] = false;
        						System.err.println("Exception: maskVolumeBoolean[i][j][k] = false\n");
        					}
        				}
        			}	
        		}
        		dis.close();
        		bis.close();
        		fis.close();
        	} catch (IOException e) {
        		System.err.println("Exception: Can't open the mask input volume: "+maskInputVolume);
        	}
        }
        
        if (data_type==0) {			// Unsigned BYTE input
        	for (int fileList = 0; fileList< xLength; fileList++) {
        		for (i=0; i< dim[2]; i++) {
        			for(j = 0; j < dim[1]; j++) {
        				for(k = 0; k < dim[0]; k++) {
        					try { 
        						if (byteswap==false) imagingData[fileList][i][j][k] = in[fileList].readUnsignedByte();
        						else imagingData[fileList][i][j][k] = swap((byte)in[fileList].readUnsignedByte());
        					} catch (IOException e) {
        						imagingData[fileList][i][j][k] = 0;
        						System.err.println("imagingData[fileList][i][j][k] = 0\n");
        					}
        				}
        			}	
        		}
        		try { 
        			in[fileList].close();
        			bi[fileList].close();
        			fi[fileList].close();
        		} catch (IOException e) {
        			System.err.println("Can't close the Input Streams!\n");
				}
        	}
        } else if (data_type==1) {			// Signed BYTE input
        	for (int fileList = 0; fileList< xLength; fileList++) {
        		for (i=0; i< dim[2]; i++) {
        			for(j = 0; j < dim[1]; j++) {
        				for(k = 0; k < dim[0]; k++) {
        					try { 
        						if (byteswap==false) imagingData[fileList][i][j][k] = in[fileList].readByte();
        						else imagingData[fileList][i][j][k] = swap((byte)in[fileList].readByte());
        					} catch (IOException e) {
        						imagingData[fileList][i][j][k] = 0;
        						System.err.println("imagingData[fileList][i][j][k] = 0\n");
        					}
        				}
        			}	
        		}
        		try { 
        			in[fileList].close();
        			bi[fileList].close();
        			fi[fileList].close();
        		} catch (IOException e) {
        			System.err.println("Can't close the Input Streams!\n");
				}
        	}
        } else if (data_type==2) {			// SHORT Unsigned Integer input
        	for (int fileList = 0; fileList< xLength; fileList++) {
        		for (i=0; i< dim[2]; i++) {
        			for(j = 0; j < dim[1]; j++) {
        				for(k = 0; k < dim[0]; k++) {
        					try { 
        						if (byteswap==false) imagingData[fileList][i][j][k] = in[fileList].readUnsignedShort();
        						else imagingData[fileList][i][j][k] = swap((short)in[fileList].readUnsignedShort());
        					} catch (IOException e) {
        						imagingData[fileList][i][j][k] = 0;
        						System.err.println("imagingData[fileList][i][j][k] = 0\n");
        					}
        				}
        			}	
        		}
        		try { 
        			in[fileList].close();
        			bi[fileList].close();
        			fi[fileList].close();
        		} catch (IOException e) {
        			System.err.println("Can't close the Input Streams!\n");
				}
        	}
        } else if (data_type==3) {			// SHORT Signed-Integer input
        	for (int fileList = 0; fileList< xLength; fileList++) {
        		for (i=0; i< dim[2]; i++) {
        			for(j = 0; j < dim[1]; j++) {
        				for(k = 0; k < dim[0]; k++) {
        					try { 
        						if (byteswap==false) imagingData[fileList][i][j][k] = in[fileList].readShort();
        						else imagingData[fileList][i][j][k] = swap((short)in[fileList].readShort());
        					} catch (IOException e) {
        						imagingData[fileList][i][j][k] = 0;
        						System.err.println("imagingData[fileList][i][j][k] = 0\n");
        					}
        				}
        			}	
        		}
        		try { 
        			in[fileList].close();
        			bi[fileList].close();
        			fi[fileList].close();
        		} catch (IOException e) {
        			System.err.println("Can't close the Input Streams!\n");
				}
        	}
        } else if (data_type==4) {				// FLOAT Input
        	for (int fileList = 0; fileList< xLength; fileList++) {
        		for (i=0; i< dim[2]; i++) {
        			for(j = 0; j < dim[1]; j++) {
        				for(k = 0; k < dim[0]; k++) {
        					try { 
        						if (byteswap==false) imagingData[fileList][i][j][k] = in[fileList].readFloat();
        						else imagingData[fileList][i][j][k] = swap(in[fileList].readFloat());
        					} catch (IOException e) {
        						imagingData[fileList][i][j][k] = 0;
        						System.err.println("imagingData[fileList][i][j][k] = 0\n");
        					}
        				}
        			}	
        		}
        		try { 
        			in[fileList].close();
        			bi[fileList].close();
        			fi[fileList].close();
        		} catch (IOException e) {
        			System.err.println("Can't close the Input Streams!\n");
				}
        	}
        } else {
        	System.out.println("Incorrect Input-Volume Data Type. Cannot run the regression\n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression#Volume_Multiple_Linear_Regression_Usage");
			return;
        }
        
        System.err.println("Done opening and reading all input data volumes!");
       
		Data data;
		double[] beta = null;
		double[] tStat = null;
		double[] pValue = null;
		double[] previousBeta = new double[regressors.length];	// These store the P-value and the beta of the previous voxel!
		double[] previousTStat = new double[regressors.length];	// These are used for corrections, where the analysis fails - it's unclear why it
		double[] previousPValue = new double[regressors.length];// sometimes (randomly) fails. It needs to be investigated, eventhough it's rare!
		float tempFloat = 0;			// A temporary Float Variable
		
		System.gc();	// Garbage Collect!
		
		for (int ind=0; ind < regressors.length; ind++) {
			previousBeta[ind] = 0;
			previousTStat[ind] = 0;
			previousPValue[ind] = 1;
		}
		
		// Create File Output Streams
		FileOutputStream[] pFOS, rFOS, tFOS;
		BufferedOutputStream[] pBOS, rBOS, tBOS;
		DataOutputStream[] pDataOS, rDataOS, tDataOS;
			
		try { 
			pFOS = new FileOutputStream[independentLength];
			pBOS = new BufferedOutputStream[independentLength];
			pDataOS = new DataOutputStream[independentLength];
	    
			rFOS = new FileOutputStream[independentLength];
			rBOS = new BufferedOutputStream[independentLength];
			rDataOS = new DataOutputStream[independentLength];
			
			tFOS = new FileOutputStream[independentLength];
			tBOS = new BufferedOutputStream[independentLength];
			tDataOS = new DataOutputStream[independentLength];
			
			for (int regLength = 0; regLength < independentLength; regLength++) {
				if (outputPValuesVolumes==true) {
					pFOS[regLength] = new FileOutputStream(pValue_Filename+
						"_Reg_"+regressors[regLength]+".img");
					pBOS[regLength] = new BufferedOutputStream(pFOS[regLength]);
					pDataOS[regLength] = new DataOutputStream(pBOS[regLength]);
				}
		    
				if (outputREffectSizeVolumes==true) {
					rFOS[regLength] = new FileOutputStream(rValue_Filename+
						"_Reg_"+regressors[regLength]+".img");
					rBOS[regLength] = new BufferedOutputStream(rFOS[regLength]);
					rDataOS[regLength] = new DataOutputStream(rBOS[regLength]);
				}
				
				if (outputTStatVolumes) {
					tFOS[regLength] = new FileOutputStream(tStat_Filename+
						"_TStat_"+regressors[regLength]+".img");
					tBOS[regLength] = new BufferedOutputStream(tFOS[regLength]);
					tDataOS[regLength] = new DataOutputStream(tBOS[regLength]);
				}
			}
		
	        System.err.println("Done opening output streams!");

			for (i = 0; i < dim[2]; i++) {
				for(j = 0; j < dim[1]; j++) {
					for(k = 0; k < dim[0]; k++) {
				        //if (k==0 && j==0 && i==0)
				        //System.err.println("\t maskInputVolume="+maskInputVolume);

				        if (maskInputVolume==null ||
								(maskInputVolume!=null && maskVolumeBoolean[i][j][k]==true)) {
								// if NO mask is provided OR (mask exists and (k,j,i) xoxel is inside the mask)
							data = new Data();
							for(int fileList=0; fileList<xLength; fileList++) {   
								yData[fileList] = (double)imagingData[fileList][i][j][k];
							}
							
							data.appendY("ImagingIntensity", yData, DataType.QUANTITATIVE);
								
							for (int regLength = 0; regLength < independentLength; regLength++) {
								xData = new double[xLength];
								for(int fileList=0; fileList<xLength; fileList++) { 
									try {
										xData[fileList] = (Double.valueOf((String)xList[regLength].
												get(fileList))).doubleValue();
									} catch (NumberFormatException e) { return; }
								}
								xDataArray[regLength]=xData;
								data.appendX(varHeader[regressorColumnIndices[regLength]], 
											xData, DataType.QUANTITATIVE);
							}
    				
							/***************** Run Regression Analysis: X is 2, Y is 1 *****************/
							try {
								result = (MultiLinearRegressionResult)
    								data.getAnalysis(AnalysisType.MULTI_LINEAR_REGRESSION);
							} catch (Exception e) {	System.err.println(e); }
							if (result==null) {
								return;
							}
		
							beta = null; tStat = null; pValue = null;
														
							try { beta = (double[]) (result.getBeta()); } 
							catch (NullPointerException e) { System.err.println(e); }
							try { tStat = (double[]) (result.getBetaTStat()); } 
							catch (NullPointerException e) { System.err.println(e); }
							try { pValue = (double[]) (result.getBetaPValue()); }
							catch (NullPointerException e) { System.err.println(e); }
						
							// The first estimate-value and p_value are for the CONSTANT term.
							// Thus, we report only estimates/effect-sizes and p-values for
							// user-specified predictors/covariates only!
														
							if (regressors.length != (beta.length-1)) {
								System.err.println("\t Problem: regressors.length="+regressors.length+
										" != (beta.length-1) = "+(beta.length-1));
								System.exit(0);
							}
							
							// For each regressor/predictor
							for (int regLength = 1; regLength < beta.length; regLength++) {
								//System.err.println("\t Outputting stats for regressor="+(regLength-1)+
										//"\t: "+regressors[regLength-1]);
								if (outputPValuesVolumes==true) {
									tempFloat = (float)(pValue[regLength]);
									try {
										//pDataOS[regLength-1].writeFloat(tempFloat);
										if (byteswap==false) pDataOS[regLength-1].writeFloat(tempFloat);
										else pDataOS[regLength-1].writeFloat(swap(tempFloat));
									} catch (IOException e) {; }
								}
								if (outputREffectSizeVolumes==true) {
									tempFloat = (float)(beta[regLength]);
									try {
										//rDataOS[regLength-1].writeFloat(tempFloat);
										if (byteswap==false) rDataOS[regLength-1].writeFloat(tempFloat);
										else rDataOS[regLength-1].writeFloat(swap(tempFloat));
									} catch (IOException e) {; }
								}
								if (outputTStatVolumes==true) {
									tempFloat = (float)(tStat[regLength]);
									try {
										//tDataOS[regLength-1].writeFloat(tempFloat);
										if (byteswap==false) tDataOS[regLength-1].writeFloat(tempFloat);
										else tDataOS[regLength-1].writeFloat(swap(tempFloat));
									} catch (IOException e) {; }
								}
							}							
							//System.err.println("\t\t Completed First Iteration!");
						} // End of IF No-mask OR (mask exists AND (k,j,i) xoxel is inside the mask)
				        
						else if (maskInputVolume!=null && maskVolumeBoolean[i][j][k]==false) {
							// if mask exists and (k,j,i) xoxel is OUTSIDE the mask write trivial output)
							for (int regLength = 0; regLength < regressors.length; regLength++) {
								tempFloat=(float)0.0;
								if (outputPValuesVolumes==true) {;
									try {
										//pDataOS[regLength].writeFloat(tempFloat+1);
										if (byteswap==false) pDataOS[regLength].writeFloat(tempFloat+1);
										else pDataOS[regLength].writeFloat(swap(tempFloat+1));
										//System.out.println("Done writing pDataOS["+(regLength-1)+"]");
									} catch (IOException e) {; }
								}
								if (outputREffectSizeVolumes==true) {
									try {
										//rDataOS[regLength].writeFloat(tempFloat);
										if (byteswap==false) rDataOS[regLength].writeFloat(tempFloat);
										else rDataOS[regLength].writeFloat(swap(tempFloat));
										//System.out.println("Done writing rDataOS["+(regLength-1)+"]");
									} catch (IOException e) {; }
								}
								if (outputTStatVolumes==true) {
									try {
										//tDataOS[regLength].writeFloat(tempFloat);
										if (byteswap==false) tDataOS[regLength].writeFloat(tempFloat);
										else tDataOS[regLength].writeFloat(swap(tempFloat));
										//System.out.println("Done writing rDataOS["+(regLength-1)+"]");
									} catch (IOException e) {; }
								}
							}
						} // END: if mask exists and (k,j,i) xoxel is OUTSIDE the mask
						
				        //System.err.println("\t\t\t Incrementing k-index!");
					} // Close dim[2], dim[1] and dim[0] Loops
					System.gc();
				}	
			}	// Close all 3 Loops
			
	        System.err.println("Done WRITING to all output streams!");

				// CLose all output Streams
			for (int regLength = 0; regLength < independentLength; regLength++) {
				if (outputPValuesVolumes==true) {
					pDataOS[regLength].flush();
					pDataOS[regLength].close();
					pBOS[regLength].flush();
					pBOS[regLength].close();
					pFOS[regLength].flush();
					pFOS[regLength].close();
					System.out.println("p-Value Output File["+regLength+"] ="+pValue_Filename+
							"_Reg_"+regressors[regLength]+".img");
				}
				if (outputREffectSizeVolumes==true) {
					rDataOS[regLength].flush();
					rDataOS[regLength].close();
					rBOS[regLength].flush();
					rBOS[regLength].close();
					rFOS[regLength].flush();
					rFOS[regLength].close();
					System.out.println("Effect-Size Output File["+regLength+"] ="+rValue_Filename+
							"_Reg_"+regressors[regLength]+".img");
				}
				if (outputTStatVolumes) {
					tDataOS[regLength].flush();
					tDataOS[regLength].close();
					tBOS[regLength].flush();
					tBOS[regLength].close();
					tFOS[regLength].flush();
					tFOS[regLength].close();
					System.out.println("T-Stats Output File["+regLength+"] ="+tStat_Filename+
							"_TStat_"+regressors[regLength]+".img");
				}
			}
			
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!\n!!!!!!Complete!!!!!!!\n!!!!!!!!!!!!!!!!!!!!!!!!!!");
			
		} catch (Exception e) {
			System.err.println("VolumeMultipleRegression Error!!!!!!!!!!!!!!");
		}

	} // End of Main()
	
	 /**
	   * Byte swap a single SHORT INT value.
	   * 
	   * @param value  Value to byte swap.
	   * @return       Byte swapped representation.
	   */
	  public static short swap (short value)
	  {
	    int b1 = value & 0xff;
	    int b2 = (value >> 8) & 0xff;

	    return (short) (b1 << 8 | b2 << 0);
	  }

	  /**
	   * Byte swap a single INTEGER value.
	   * 
	   * @param value  Value to byte swap.
	   * @return       Byte swapped representation.
	   */
	  public static int swap (int value)
	  {
	    int b1 = (value >>  0) & 0xff;
	    int b2 = (value >>  8) & 0xff;
	    int b3 = (value >> 16) & 0xff;
	    int b4 = (value >> 24) & 0xff;

	    return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;
	  }

	  /**
	   * Byte swap a single LONG INTEGER value.
	   * 
	   * @param value  Value to byte swap.
	   * @return       Byte swapped representation.
	   */
	  public static long swap (long value)
	  {
	    long b1 = (value >>  0) & 0xff;
	    long b2 = (value >>  8) & 0xff;
	    long b3 = (value >> 16) & 0xff;
	    long b4 = (value >> 24) & 0xff;
	    long b5 = (value >> 32) & 0xff;
	    long b6 = (value >> 40) & 0xff;
	    long b7 = (value >> 48) & 0xff;
	    long b8 = (value >> 56) & 0xff;

	    return b1 << 56 | b2 << 48 | b3 << 40 | b4 << 32 |
	           b5 << 24 | b6 << 16 | b7 <<  8 | b8 <<  0;
	  }
	  	  
	  /**
	   * Byte swap a single FLOAT value.
	   * 
	   * @param value  Value to byte swap.
	   * @return       Byte swapped representation.
	   */
	  public static float swap (float value)
	  {
	    int intValue = Float.floatToIntBits (value);
	    intValue = swap (intValue);
	    return Float.intBitsToFloat (intValue);
	  }

	  /**
	   * Byte swap a single DOUBLE value.
	   * 
	   * @param value  Value to byte swap.
	   * @return       Byte swapped representation.
	   */
	  public static double swap (double value)
	  {
	    long longValue = Double.doubleToLongBits (value);
	    longValue = swap (longValue);
	    return Double.longBitsToDouble (longValue);
	  }
}
