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
package edu.ucla.stat.SOCR.analyses.command.volume;
import java.io.*;
import java.util.*;

import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.result.*;

/*
 * This class provides an image-volume based 2-independent-samples T-test analysis for 1D, 2D and 3D imaging volumes, based
 * on user specified regressors/predictors. These are provided as a Tab-Separated input
 * Design-matrix file in this format:
 * 
 * 
 * #SUBJECT ID	FILENAME												SEX	GROUP_ID	AGE		CDR	MMSE
 * 002_S_0413	/ifs/adni/3T/PERMS/IVO/JACOBIANS/3T_bl_002_S_0413.img	0	1			76.38	0	29
 * 
 * Building: via the SOCR build.xml file, as part of SOCR Analyses
 * 
 * Invocation/Call: 
 * java -cp /ifs/ccb/CCB_SW_Tools/others/Statistics/SOCR_Statistics/bin/SOCR_core.jar:
 * 			/ifs/ccb/CCB_SW_Tools/others/Statistics/SOCR_Statistics/bin/SOCR_plugin.jar 
 * 			edu.ucla.stat.SOCR.analyses.command.volume.Volume_2IndepSample_T_test 
 * 			-dm DesignMatrix.txt -h -regressors [name1=Label1,Label2]
 * 			-dim Zmax Ymax XMax [-p filename] [-t filename] -data_type [0,1,2,3,4]
 * 
 * Options:
 * 			-help: print usage
 * 			-dm [DesignMatrix.txt]: specify a tab-separated text file containing the design matrix
 * 			-mask [Mask-volume.img]: specify a mask-volume (0 or 1 intensities) restricting the voxels
 * 										where the regression models are computed (optional),
 * 										1Byte Analyze format volume of the same dimensions as the data 
 * 			-h: 					DesignMatrix contains a header (first row)
 * 			-regressors [name1=Label]: specify which column/variable should be used and what is its value!
 * 			-dim Zmax Ymax XMax: 	specify the dimension-sizes (for 2D images use ZMax=1, for 1D, Zmax=Y_Max=1
 * 			-p [Pvalue_Filename]: 	output the p-value volume
 * 			-t [Tstat_Filename]:	output the T-Statistics for the effect-size Beta
 * 			-data_type [0,1,2,3,4]:	Type=0 is for Unsigned Byte input volumes; 
 * 									Type=1 is for Signed Byte input volumes; 
 * 									Type=2 is for Unsigned-short integers
 * 									Type=3 is for Signed-short integers
 * 									Type=4 is for 4Byte=Float Volumes
 * 			-byteorder:				big (java.nio.ByteOrder.BIG_ENDIAN)
 * 									little (java.nio.ByteOrder.LITTLE_ENDIAN)
 * 									system (default system byteorder, java.nio.ByteOrder.nativeOrder())
 * 			-byteswap:				(deprecated) Only enter this flag if you want the input data to be read in and byteswapped! 
 * 									Note that -byteswap effects: input data, mask-volume and output results!
 * 
 * Documentation: 
 * 	http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test
 * 
 */

public class Volume_2IndepSample_T_test {
	private static final String MISSING_MARK = ".";

    static FileInputStream[] fi_Grp1, fi_Grp2;
    static BufferedInputStream[] bi_Grp1, bi_Grp2;
    static MemoryCacheImageInputStream[] in_Grp1, in_Grp2;
    
	static FileOutputStream pFOS, tFOS;
	static BufferedOutputStream pBOS, tBOS;
	static MemoryCacheImageOutputStream pMciis, tMciis;

	static boolean byteswap = false;
	static String byteorder = "system";

	static String regressors;
	static String[] regressorLabels = new String[2];
	
	static boolean outputPValuesVolumes=false;
	static boolean outputTStatVolumes=false;
	
	static 	int bufferSize = 8192;		// This contains the size of the Input/Output Buffer

	public static void main(String[] args) {
		String designMatrixInputFile = null;
		String maskInputVolume=null;		// mask input volume
		
		String pValue_Filename=null;
		String tStat_Filename=null;
		
		regressors = "";
		regressorLabels[0] = ""; regressorLabels[1] = ""; 
		
		int regressorColumnIndex=2;				// These index of column containing specified variable
		
		int[] dim = new int[3];		// the sizes of the 3D volume dimensions
						
		boolean [][][] maskVolumeBoolean=new boolean[1][1][1];			// [Zmax][Ymax][Xmax]
		maskVolumeBoolean[0][0][0] = false;
		
		int data_type = 4;							//image intensities data type
		
		Vector<String> inputGrp1AnalyzeFilenames = new Vector<String>();	
		Vector<String> inputGrp2AnalyzeFilenames = new Vector<String>();	
													// The names of the Grp1 and Grp2 Analyze Input Files 
													// stored in column 2 of the Design-Matrix
		String regressorString = "";
		
		boolean header=false;
		boolean filesLoaded = false;
				
		int i, j,k;
		
		System.out.println(
		"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");

		try {
			for (i=0; i<args.length; i++) {
				if (args[i].compareToIgnoreCase("-h")==0) header=true;
				else if (args[i].compareToIgnoreCase("-data_type")==0) {
					data_type = Integer.parseInt(args[++i]);
					if (data_type!=0 && data_type!=1 && data_type!=2 && data_type!=3 && data_type!=4) {
						System.out.println("Data Type must be 0(unsigned byte volume), 1(signed byte volume), "+
								"2(unsigned short int) 3(signed short int) or 4(float volume)!\n");
						System.exit(1);
					}
				} else if (args[i].compareToIgnoreCase("-dim")==0) {
					dim[2] = Integer.parseInt(args[++i]);
					dim[1] = Integer.parseInt(args[++i]);
					dim[0] = Integer.parseInt(args[++i]);
					//bufferSize = dim[0]*dim[1];
					System.out.println("dim[2]="+dim[2]+"\tdim[1]="+dim[1]+"\tdim[0]="+dim[0]+
							"\tbufferSize="+bufferSize);
				} else if (args[i].compareToIgnoreCase("-p")==0) { 
					outputPValuesVolumes=true;
					pValue_Filename = args[++i];
					System.out.println("pValue_Filename="+pValue_Filename);
				} else if (args[i].compareToIgnoreCase("-byteswap")==0) { 
					byteswap=true;
					System.out.println("Byteswap="+byteswap);
				} else if (args[i].compareToIgnoreCase("-byteorder")==0) { 
					byteorder = args[++i];
					System.out.println("byteorder="+byteorder);
				} else if (args[i].compareToIgnoreCase("-t")==0) { 
					outputTStatVolumes=true;
					tStat_Filename = args[++i];
					System.out.println("tStat_Filename="+tStat_Filename);
				} else if (args[i].compareToIgnoreCase("-regressors")==0) {
					regressorString = args[++i];
					System.err.println("regressorString="+regressorString);
					// Parse the regressors list
					StringTokenizer Tok = new StringTokenizer(regressorString, "=");
			        regressors = "";
			   		// Get each pair name=Label
					// Assign each pair to regressors (name) and regressorLabels (label/value)
					// Syntqax is: -regressors [name1=Label]
					regressors = Tok.nextToken();
					String allRegressorLabels = Tok.nextToken();
					try {
						StringTokenizer Tok1 = new StringTokenizer(allRegressorLabels, ",");
						regressorLabels[0] = Tok1.nextToken();
						regressorLabels[1] = Tok1.nextToken();
						System.out.println("regressorColumnName="+regressors+
							"\t Group1Label="+regressorLabels[0]+"\t Group2Label="+regressorLabels[1]);
					} catch (Exception e) {
						System.out.println("Regressor entry syntax incorrect. Please see online docs: \n"+
						"http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
						return;
					}
				} else if (args[i].compareToIgnoreCase("-dm")==0) {
					designMatrixInputFile = args[++i];
					filesLoaded = true;
					System.out.println("designMatrixInputFile="+designMatrixInputFile);
				} else if (args[i].compareToIgnoreCase("-help")==0) {
					System.out.println("Please see the Web syntax for involking this tool: \n"+
					"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
					return;
				}  else if (args[i].compareToIgnoreCase("-mask")==0) {
					maskInputVolume = args[++i];
					System.out.println("maskInputVolume="+maskInputVolume);
				}
			}
		} catch (Exception e) {
			System.out.println("Incorrect call 1! Please see the syntax for involking this tool\n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
		}
		
		if (!filesLoaded) {
			System.out.println("Incorrect call 2! Please see the syntax for involking this tool\n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
			return;
		}
				
		int independentLength=1;
		
		if (independentLength<1)
		{	System.out.println("No Independent Variable Specified (independentLength="+independentLength+
				")\n Cannot run the regression\n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
			return;
		}
		
		TwoIndependentTResult result = null;

		StringTokenizer st = null;
		
		int designMatrixNumberColumns = 1000;	// Maximum number of DesignMatrix columns
		
		String[] input = new String [independentLength];
		int xLengthGrp1=0, xLengthGrp2=0;
		
		String[] varHeader = new String[1];

		String line = null, stringTmp;
		
		boolean read=true;
		if (header)
			read=false;

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
							if (varHeader[k].compareToIgnoreCase(regressors)==0) {
								regressorColumnIndex = k;
								System.err.println("varHeader["+k+"]="+varHeader[k]+"\t regressorColumnIndex ="+ k);
							}
						}
				   		read=true;
					} else if (!header) {		// Must have a header row to deduce the predictors
						System.out.println("No Independent Variable Specified 1. Cannot run the regression\n"+
						"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
						return;
					} else {					// READ DATA From Design-Matrix
						try {
							int n = 0;	// column index in design-matrix
							int m=0;	// row index in the design-matrix
							
							while (st.hasMoreElements()) {
								// Skip first column of Design-Matrix - it contains SubjectID
								stringTmp = st.nextToken().trim();
								if (n==1)	{ // Read IMG/ANALYZE File Name
									inputGrp1AnalyzeFilenames.addElement(stringTmp);
									inputGrp2AnalyzeFilenames.addElement(stringTmp);
								} else if (n==regressorColumnIndex) {
									input[m] = stringTmp;			
									// If the value of the variable != the specified Group1 or Group2 labels, remove this input file!
									if (!input[m].equalsIgnoreCase(regressorLabels[0]))
										inputGrp1AnalyzeFilenames.removeElementAt(inputGrp1AnalyzeFilenames.size()-1);
									else System.out.println("ADDED NEW Group-1 INPUT FILE: inputGrp1AnalyzeFilenames["+
											inputGrp1AnalyzeFilenames.size()+"]="+
											inputGrp1AnalyzeFilenames.get(inputGrp1AnalyzeFilenames.size()-1));
										
									if (!input[m].equalsIgnoreCase(regressorLabels[1]))
										inputGrp2AnalyzeFilenames.removeElementAt(inputGrp2AnalyzeFilenames.size()-1);
									else System.out.println("ADDED NEW Group-2 INPUT FILE: inputGrp2AnalyzeFilenames["+
											inputGrp2AnalyzeFilenames.size()+"]="+
											inputGrp2AnalyzeFilenames.get(inputGrp2AnalyzeFilenames.size()-1));

									System.out.println("Check: input[m]="+input[m]+
											" Group1_Label="+regressorLabels[0]+"; Group2_Label="+regressorLabels[1]);
									System.out.println("inputGrp1AnalyzeFilenames.size()="+
											inputGrp1AnalyzeFilenames.size()+"; inputGrp2AnalyzeFilenames.size()="+
											inputGrp2AnalyzeFilenames.size()+"\tcolumn="+n+"\t input["+m+"]="+input[m]);
									m++;
								} 
								++n;
							}
							read=true;
						} catch (NoSuchElementException e) {
							//e.printStackTrace();
							System.out.println("Volume 1-Sample T-test Analysis: 1");
							return;
						} catch (Exception e) {
							//e.printStackTrace();
							System.out.println("Volume 1-Sample T-test  Analysis: 2");
							return;
						}
					}
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		if (!header)
		{	System.out.println("No Independent Variable Specified in Design-Matrix. Cannot run the analysis\n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
			return;
		}
		
		xLengthGrp1=inputGrp1AnalyzeFilenames.size();
		xLengthGrp2=inputGrp2AnalyzeFilenames.size();
		System.out.println("Number of volumes (meeting the paired criteria: variable="+
				regressors+"; Group_1("+regressorLabels[0]+")= "+ xLengthGrp1+
				"; Group_2("+regressorLabels[1]+")= "+ xLengthGrp2);

		//double[] xData = null;
		double[] yDataGrp1 = new double[xLengthGrp1];
		double[] yDataGrp2 = new double[xLengthGrp2];
		// Open Volume Files
		
        //create streams of the 2 Independent groups of Analyze Image files
        fi_Grp1 = new FileInputStream[xLengthGrp1];
        bi_Grp1 = new BufferedInputStream[xLengthGrp1];
        in_Grp1 = new MemoryCacheImageInputStream[xLengthGrp1];
        
        fi_Grp2 = new FileInputStream[xLengthGrp2];
        bi_Grp2 = new BufferedInputStream[xLengthGrp2];
        in_Grp2 = new MemoryCacheImageInputStream[xLengthGrp2];
        
        for (i = 0; i< xLengthGrp1; i++) {
        	try { 
        		fi_Grp1[i] = new FileInputStream(inputGrp1AnalyzeFilenames.elementAt(i));
        		bi_Grp1[i] = new BufferedInputStream(fi_Grp1[i], bufferSize);
        		in_Grp1[i] = new MemoryCacheImageInputStream(bi_Grp1[i]);
        		setByteOrder(in_Grp1[i], byteorder);
        	} catch (FileNotFoundException e) { 
        		System.err.println(e);
        	}
        }
        for (i = 0; i< xLengthGrp2; i++) {
        	try { 
        		fi_Grp2[i] = new FileInputStream(inputGrp2AnalyzeFilenames.elementAt(i));
        		bi_Grp2[i] = new BufferedInputStream(fi_Grp2[i], bufferSize);
        		in_Grp2[i] = new MemoryCacheImageInputStream(bi_Grp2[i]);
        		setByteOrder(in_Grp2[i], byteorder);
        	} catch (FileNotFoundException e) { 
        		System.err.println(e);
        	}
        }
        
        System.err.println("Done opening INPUT volume streams!");
        
        if (maskInputVolume!=null) { 
        	try {
        		maskVolumeBoolean = new boolean [dim[2]][dim[1]][dim[0]];
        		FileInputStream fis = new FileInputStream(maskInputVolume);
        		BufferedInputStream bis = new BufferedInputStream(fis);
        		MemoryCacheImageInputStream dis = new MemoryCacheImageInputStream(bis);
        		setByteOrder(dis, byteorder);
        		int tmpByte;
            
        		for (i=0; i< dim[2]; i++) {
        			for(j = 0; j < dim[1]; j++) {
        				for(k = 0; k < dim[0]; k++) {
        					try { 
        						tmpByte = dis.readUnsignedByte(); //readUnsignedByte()
        						if (tmpByte > 0) maskVolumeBoolean[i][j][k] = true;
        						else maskVolumeBoolean[i][j][k] = false;
          					} catch (IOException e) {
        						maskVolumeBoolean[i][j][k] = false;
        						System.err.println("Exception: maskVolumeBoolean["+i+"]["+j+"]["+k+"] = false\n");
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
                
 		Data data;
		float tStat, pValue;
		
		// Create File Output Streams
		try { 
			if (outputPValuesVolumes==true) {
					pFOS = new FileOutputStream(pValue_Filename+
						"_Reg_"+regressors+"_"+regressorLabels[0]+"_"+regressorLabels[1]+".img");
					pBOS = new BufferedOutputStream(pFOS, bufferSize);
					pMciis = new MemoryCacheImageOutputStream(pBOS);
					setByteOrder(pMciis, byteorder);
			}
			if (outputTStatVolumes) {
					tFOS = new FileOutputStream(tStat_Filename+
						"_TStat_"+regressors+"_"+regressorLabels[0]+"_"+regressorLabels[1]+".img");
					tBOS = new BufferedOutputStream(tFOS, bufferSize);
					tMciis = new MemoryCacheImageOutputStream(tBOS);
					setByteOrder(tMciis, byteorder);
			}
		
	        System.err.println("Done opening OUTPUT result streams!");
	        System.err.println("Beginning the stat analyses ... ");
	        	
	        int flushCounter = 0;
	        
			for (i=0; i< dim[2]; i++) {
				for(j = 0; j < dim[1]; j++) {
					for(k = 0; k < dim[0]; k++) {
				        //if (k==0 && j==0 && i==0)
				        //System.err.println("\t maskInputVolume="+maskInputVolume);

						// if NO mask is provided OR (mask exists and (k,j,i) xoxel is inside the mask)
				        if (maskInputVolume==null || (maskInputVolume!=null && maskVolumeBoolean[i][j][k]==true)) {
							data = new Data();
				        	//Get Y-data
							yDataGrp1 = getNextInputDataBuffer(data_type, 0, xLengthGrp1, flushCounter);
							yDataGrp2 = getNextInputDataBuffer(data_type, 1, xLengthGrp2, flushCounter);
							flushCounter++;
							
							data.appendX("Group1Intensities", yDataGrp1, DataType.QUANTITATIVE);	
							data.appendY("Group2Intensities", yDataGrp2, DataType.QUANTITATIVE);	
							
							result = null;
							try {
								result = (TwoIndependentTResult)data.getAnalysis(AnalysisType.TWO_INDEPENDENT_T);
							} catch (Exception e) { System.out.println(e); }

							tStat = (float)result.getTStatPooled(); 
							pValue = (float)result.getPValueTwoSidedPooled();
							//pValue = AnalysisUtility.enhanceSmallNumber((float)result.getPValueTwoSidedPooled());
							
							putNextOutputDataBuffer(tStat, pValue);
						} // End of IF No-mask OR (mask exists AND (k,j,i) voxel is inside the mask)
				        
						else { // if (maskInputVolume!=null && maskVolumeBoolean[i][j][k]==false) {
							skipNextInputDataBuffer(data_type, 0, xLengthGrp1);
							skipNextInputDataBuffer(data_type, 1, xLengthGrp2);
							putNextOutputDataBuffer();
						} // END: if mask exists and (k,j,i) xoxel is OUTSIDE the mask
					} // Close dim[2], dim[1] and dim[0] Loops
				}	
			}	// Close all 3 Loops
			
	        System.err.println("Done WRITING all results to output streams!");

	        	// Close all INPUT Streams
	        for (int fileList=0; fileList<xLengthGrp1; fileList++) {
    			in_Grp1[fileList].close();
    			bi_Grp1[fileList].close();
    			fi_Grp1[fileList].close();
	        }
	        for (int fileList=0; fileList<xLengthGrp2; fileList++) {
    			in_Grp2[fileList].close();
    			bi_Grp2[fileList].close();
    			fi_Grp2[fileList].close();
	        }
	        
				// Close all OUTPUT Streams
			if (outputPValuesVolumes==true) {
				pMciis.flush();
				pMciis.close();
				pBOS.flush();
				pBOS.close();
				pFOS.flush();
				pFOS.close();
				System.out.println("p-Value Output File ="+pValue_Filename+
					"_Reg_"+regressors+"_"+regressorLabels[0]+"_"+regressorLabels[1]+".img");
			}
			if (outputTStatVolumes) {
				tMciis.flush();
				tMciis.close();
				tBOS.flush();
				tBOS.close();
				tFOS.flush();
				tFOS.close();
				System.out.println("T-Stats Output File ="+tStat_Filename+
					"_TStat_"+regressors+"_"+regressorLabels[0]+"_"+regressorLabels[1]+".img");
			}
			
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!\n!!!!!!Complete!!!!!!!\n!!!!!!!!!!!!!!!!!!!!!!!!!!");
			
		} catch (Exception e) {
			System.err.println("Volume1Sample_T_test Error!!!!!!!!!!!!!!");
		}

	} // End of Main()
	
	/**
	 * This method sets the Bite-Order for a MemoryCacheImageInputStream
	 * @param dis - MemoryCacheImageInputStream
	 * @param byteorder - String representation of byteorder. Options are
	 * 						big (java.nio.ByteOrder.BIG_ENDIAN)
	 * 						little (java.nio.ByteOrder.LITTLE_ENDIAN)
	 * 						system (default system byteorder, java.nio.ByteOrder.nativeOrder())
	 */
	public static void setByteOrder(MemoryCacheImageInputStream dis, String byteorder) {
		if (byteorder.compareToIgnoreCase("little")==0) dis.setByteOrder(java.nio.ByteOrder.LITTLE_ENDIAN);
		else if (byteorder.compareToIgnoreCase("big")==0) dis.setByteOrder(java.nio.ByteOrder.BIG_ENDIAN);
		else dis.setByteOrder(java.nio.ByteOrder.nativeOrder());
		return;
	}

	/**
	 * This method sets the Bite-Order for a MemoryCacheImageOutputStream
	 * @param dis - MemoryCacheImageOutputStream
	 * @param byteorder - String representation of byteorder. Options are
	 * 						big (java.nio.ByteOrder.BIG_ENDIAN)
	 * 						little (java.nio.ByteOrder.LITTLE_ENDIAN)
	 * 						system (default system byteorder, java.nio.ByteOrder.nativeOrder())
	 */
	public static void setByteOrder(MemoryCacheImageOutputStream dos, String byteorder) {
		if (byteorder.compareToIgnoreCase("little")==0) dos.setByteOrder(java.nio.ByteOrder.LITTLE_ENDIAN);
		else if (byteorder.compareToIgnoreCase("big")==0) dos.setByteOrder(java.nio.ByteOrder.BIG_ENDIAN);
		else dos.setByteOrder(java.nio.ByteOrder.nativeOrder());
		return;
	}

	/**
	 * This method READS IN a bufferSize chunk of all input volumes and returns it as a double[]
	 * @param data_type - type of the input data
	 * @param group - group index (group 0 or 1)
	 * @param xLength - number of volumes/subjects in the second-column (file-names) in the Design-matrix
	 * @param flush -	variable indicating whether the buffer should be flushed as the stream keeps
	 * 					in memory all the data it has read from disk. This will free up memory, but too frequent 
	 * 					calls (flush==true) will slow down the process!
	 */
	public static double[] getNextInputDataBuffer(int data_type, int group, int xLength, int flush) {
		double[] newInputData = new double[xLength];
		
        if (data_type==0) {			// Unsigned BYTE input
        	if (group == 0) {
        		for (int fileList = 0; fileList< xLength; fileList++) {
        			try { 
        				if (byteswap==false) newInputData[fileList] = in_Grp1[fileList].readUnsignedByte();
        				else newInputData[fileList] = swap((byte)in_Grp1[fileList].readUnsignedByte());
        				if (flush % bufferSize == 1) in_Grp1[fileList].flush();
        			} catch (IOException e) {
        				newInputData[fileList] = 0;
        				System.err.println("(Group 0, type 0) newInputData["+fileList+"]= 0\n");
        			}
        		}
        	} else if (group == 1) {
        		for (int fileList = 0; fileList< xLength; fileList++) {
        			try { 
        				if (byteswap==false) newInputData[fileList] = in_Grp2[fileList].readUnsignedByte();
        				else newInputData[fileList] = swap((byte)in_Grp2[fileList].readUnsignedByte());
        				if (flush % bufferSize == 1) in_Grp2[fileList].flush();
        			} catch (IOException e) {
        				newInputData[fileList] = 0;
        				System.err.println("(Group 1, type 0) newInputData["+fileList+"]= 0\n");
        			}
        		}
        	}
        } else if (data_type==1) {			// Signed BYTE input
        	if (group==0) {
        		for (int fileList = 0; fileList< xLength; fileList++) {
        			try { 
        				if (byteswap==false) newInputData[fileList] = in_Grp1[fileList].readByte();
        				else newInputData[fileList] = swap((byte)in_Grp1[fileList].readByte());
        				if (flush % bufferSize == 1) in_Grp1[fileList].flush();
        			} catch (IOException e) {
        				newInputData[fileList] = 0;
        				System.err.println("(Group 0, type 1) newInputData["+fileList+"]= 0\n");
        			}
        		}
        	} else if (group==1) {
        		for (int fileList = 0; fileList< xLength; fileList++) {
        			try { 
        				if (byteswap==false) newInputData[fileList] = in_Grp2[fileList].readByte();
        				else newInputData[fileList] = swap((byte)in_Grp2[fileList].readByte());
        				if (flush % bufferSize == 1) in_Grp2[fileList].flush();
        			} catch (IOException e) {
        				newInputData[fileList] = 0;
        				System.err.println("(Group 1, type 1) newInputData["+fileList+"]= 0\n");
        			}
        		}
        	}  
        } else if (data_type==2) {			// SHORT Unsigned Integer input
        	if (group==0) {
        		for (int fileList = 0; fileList< xLength; fileList++) {
        			try { 
        				if (byteswap==false) newInputData[fileList] = in_Grp1[fileList].readUnsignedShort();
        				else newInputData[fileList] = swap((short)in_Grp1[fileList].readUnsignedShort());
        				if (flush % bufferSize == 1) in_Grp1[fileList].flush();
        			} catch (IOException e) {
        				newInputData[fileList] = 0;
        				System.err.println("(Group 0, type 2) newInputData["+fileList+"]= 0\n");
        			}
        		}
			} else if (group==1) {
				for (int fileList = 0; fileList< xLength; fileList++) {
        			try { 
        				if (byteswap==false) newInputData[fileList] = in_Grp2[fileList].readUnsignedShort();
        				else newInputData[fileList] = swap((short)in_Grp2[fileList].readUnsignedShort());
        				if (flush % bufferSize == 1) in_Grp2[fileList].flush();
        			} catch (IOException e) {
        				newInputData[fileList] = 0;
        				System.err.println("(Group 1, type 2) newInputData["+fileList+"]= 0\n");
        			}
        		}
			} 
        } else if (data_type==3) {			// SHORT Signed-Integer input
        	if (group==0) {
        		for (int fileList = 0; fileList< xLength; fileList++) {
        			try { 
        				if (byteswap==false) newInputData[fileList] = in_Grp1[fileList].readShort();
        				else newInputData[fileList] = swap((short)in_Grp1[fileList].readShort());
        				if (flush % bufferSize == 1) in_Grp1[fileList].flush();
        			} catch (IOException e) {
        				newInputData[fileList] = 0;
        				System.err.println("(Group 0, type 3) newInputData["+fileList+"]= 0\n");
        			}
        		}
        	} else if (group==1) {
        		for (int fileList = 0; fileList< xLength; fileList++) {
        			try { 
        				if (byteswap==false) newInputData[fileList] = in_Grp2[fileList].readShort();
        				else newInputData[fileList] = swap((short)in_Grp2[fileList].readShort());
        				if (flush % bufferSize == 1) in_Grp2[fileList].flush();
        			} catch (IOException e) {
        				newInputData[fileList] = 0;
        				System.err.println("(Group 1, type 3) newInputData["+fileList+"]= 0\n");
        			}
        		}
        	} 	
        } else if (data_type==4) {				// FLOAT Input
        	if (group==0) {
        		for (int fileList = 0; fileList< xLength; fileList++) {
        			try { 
        				if (byteswap==false) newInputData[fileList] = in_Grp1[fileList].readFloat();
        				else newInputData[fileList] = swap(in_Grp1[fileList].readFloat());
        				if (flush % bufferSize == 1) in_Grp1[fileList].flush();
        			} catch (IOException e) {
        				newInputData[fileList] = 0;
        				System.err.println("(Group 0, type 4) newInputData["+fileList+"]= 0\n");
        			}
        		}
        	} else if (group==1) {
        		for (int fileList = 0; fileList< xLength; fileList++) {
        			try { 
        				if (byteswap==false) newInputData[fileList] = in_Grp2[fileList].readFloat();
        				else newInputData[fileList] = swap(in_Grp2[fileList].readFloat());
        				if (flush % bufferSize == 1) in_Grp2[fileList].flush();
        			} catch (IOException e) {
        				newInputData[fileList] = 0;
        				System.err.println("(Group 1, type 4) newInputData["+fileList+"]= 0\n");
        			}
        		}
        	} 
        } else {
        	System.out.println("Incorrect Input-Volume Data Type: "+data_type+". Cannot run the regression\n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
			return null;
        }
        return newInputData;
	}	// End: getNextInputDataBuffer
	
	/**
	 * This method WRITES OUT bufferSize chunks of t-stats and p-values for all output volumes
	 * @param tStat t-statistics
	 * @param pValue - p-value
	 */
	public static void putNextOutputDataBuffer(float tStat, float pValue) {
		if (outputPValuesVolumes) {
			try {
				//pMciis.writeFloat(pValue);
				if (byteswap==false) pMciis.writeFloat(pValue);
				else pMciis.writeFloat(swap(pValue));
			} catch (IOException e) { System.err.println(e);  }
		}
		if (outputTStatVolumes) {
			try {
				//tMciis.writeFloat(tStat);
				if (byteswap==false) tMciis.writeFloat(tStat);
				else tMciis.writeFloat(swap(tStat));
			} catch (IOException e) { System.err.println(e);  }
		}
	} // END: putNextOutputDataBuffer

	/**
	 * This method WRITES OUT bufferSize chunks of background (trivial) values for all output volumes
	 */
	public static void putNextOutputDataBuffer() {
		float tempFloat=(float)0.0;
    	if (outputPValuesVolumes==true) {
    		try {
    			if (byteswap==false) pMciis.writeFloat(tempFloat+1);
    			else pMciis.writeFloat(swap(tempFloat+1));
    		} catch (IOException e) { System.err.println(e); }
    	}
    	if (outputTStatVolumes==true) {
    		try {
    			if (byteswap==false) tMciis.writeFloat(tempFloat);
    			else tMciis.writeFloat(swap(tempFloat));
    		} catch (IOException e) { System.err.println(e); }
    	}
	} 

	/**
	 * This method SKIPS in reading data from the bufferSize chunk of all input volumes
	 * @param data_type - type of the input data
	 * @param group - group (0 or 1) index
	 * @param xLength - number of volumes/subjects in the second-column (file-names) in the Design-matrix
	 */
	public static void skipNextInputDataBuffer(int data_type, int group, int xLength) {
		if (group==0) {		// GROUP 1
			if (data_type==0 || data_type==1) {			// BYTE input
				for (int fileList = 0; fileList< xLength; fileList++) {
					try { in_Grp1[fileList].skipBytes(1); 
    					if (in_Grp1[fileList].getStreamPosition() % bufferSize == 1)
    						in_Grp1[fileList].flush();
					}
					catch (IOException e) { System.err.print(e); }
				}
			} else if (data_type==2 || data_type==3) {			// SHORT 2-Byte Integer input
				for (int fileList = 0; fileList< xLength; fileList++) {
					try { in_Grp1[fileList].skipBytes(2); 
						if (in_Grp1[fileList].getStreamPosition() % bufferSize == 1)
							in_Grp1[fileList].flush();
					}
					catch (IOException e) { System.err.print(e); }
				}
			} else if (data_type==4) {				// FLOAT 4-byte Input
				for (int fileList = 0; fileList< xLength; fileList++) {
					try { in_Grp1[fileList].skipBytes(4); 
						if (in_Grp1[fileList].getStreamPosition() % bufferSize == 1)
							in_Grp1[fileList].flush();
					}
					catch (IOException e) { System.err.print(e); }
				}
			} else {
				System.out.println("Incorrect Input-Volume Data Type: "+data_type+". Cannot run the analysis\n"+
				"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
				return;
			}
		} else if (group==1) {	// GROUP 2
			if (data_type==0 || data_type==1) {			// BYTE input
				for (int fileList = 0; fileList< xLength; fileList++) {
					try { in_Grp2[fileList].skipBytes(1); 
						if (in_Grp2[fileList].getStreamPosition() % bufferSize == 1)
							in_Grp2[fileList].flush();
					}
					catch (IOException e) { System.err.print(e); }
				}
			} else if (data_type==2 || data_type==3) {			// SHORT 2-Byte Integer input
				for (int fileList = 0; fileList< xLength; fileList++) {
					try { in_Grp2[fileList].skipBytes(2); 
						if (in_Grp2[fileList].getStreamPosition() % bufferSize == 1)
							in_Grp2[fileList].flush();
					}
					catch (IOException e) { System.err.print(e); }
				}
			} else if (data_type==4) {				// FLOAT 4-byte Input
				for (int fileList = 0; fileList< xLength; fileList++) {
					try { in_Grp2[fileList].skipBytes(4); 
						if (in_Grp2[fileList].getStreamPosition() % bufferSize == 1)
							in_Grp2[fileList].flush();
					}
					catch (IOException e) { System.err.print(e); }
				}
			} else {
				System.out.println("Incorrect Input-Volume Data Type: "+data_type+". Cannot run the analysis\n"+
				"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
				return;
			}
		} else {
			System.out.println("Incorrect Group-Index: "+group+". Cannot run the analysis\n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
			return;
		}
        return;
	}	// End: skiptNextInputDataBuffer

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