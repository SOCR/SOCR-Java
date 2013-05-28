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
 * This class provides an image-volume based Kruskal-Wallis analysis for 1D, 2D and 3D imaging volumes, based
 * on user specified factor-column. The data are provided as a Tab-Separated input
 * Design-matrix file in this format:
 * 
 * This class is a "Memory-Optimized" version (using BufferedInputStream, instead of arrays)
 * of this older class: edu.ucla.stat.SOCR.analyses.command.VolumeMultipleRegression
 * 
 * #SUBJECT ID	FILENAME												SEX	GROUP_ID	AGE		CDR	MMSE
 * 002_S_0413	/ifs/adni/3T/PERMS/IVO/JACOBIANS/3T_bl_002_S_0413.img	F	Normal		76.38	0	29
 * 
 * Building: via the SOCR build.xml file, as part of SOCR Analyses
 * 
 * Invocation/Call: 
 * java -cp /ifs/ccb/CCB_SW_Tools/others/Statistics/SOCR_Statistics/bin/SOCR_core.jar:
 * 			/ifs/ccb/CCB_SW_Tools/others/Statistics/SOCR_Statistics/bin/SOCR_plugin.jar 
 * 			edu.ucla.stat.SOCR.analyses.command.volume.VolumeMultipleRegression.java 
 * 			-dm DesignMatrix.txt -h -factor columnName
 * 			-dim Zmax Ymax XMax [-p filename] [-r filename] [-t filename] -data_type [0,1,2,3,4]
 * 			[-byteorder little]
 * 
 * Options:
 * 			-help: print usage
 * 			-dm [DesignMatrix.txt]: specify a tab-separated text file containing the design matrix
 * 			-mask [Mask-volume.img]: specify a mask-volume (0 or 1 intensities) restricting the voxels
 * 										where the regression models are computed (optional),
 * 										1Byte Analyze format volume of the same dimensions as the data 
 * 			-h: 					DesignMatrix contains a header (first row)
 * 			-factor columnName1: 	specify which columns/variables contains the different levels of the 1-way factor
 * 			-dim Zmax Ymax XMax: 	specify the dimension-sizes (for 2D images use ZMax=1, for 1D, Zmax=Y_Max=1
 * 
 * 			http://en.wikipedia.org/wiki/Partial_correlation
 * 			-p [Pvalue_Filename]: 	output the p-value volume
 * 			-t [Tstat_Filename]:	output the Kruskal-Wallis-Statistics for the Significant differences
 * 									of the different levels of the specified factor-variable
 * 
 * 			-data_type [0,1,2,3,4]:	Type=0 is for Unsigned Byte input volumes; 
 * 									Type=1 is for Signed Byte input volumes; 
 * 									Type=2 is for Unsigned-short integers
 * 									Type=3 is for Signed-short integers
 * 									Type=4 is for 4Byte=Float Volumes
 * 			-byteswap:				(deprecated) Only enter this flag if you want the input data to be read in and byteswapped! 
 * 									Note that -byteswap effects: input data, mask-volume and output results!
 * 			-byteorder:				big (java.nio.ByteOrder.BIG_ENDIAN)
 * 									little (java.nio.ByteOrder.LITTLE_ENDIAN)
 * 									system (default system byteorder, java.nio.ByteOrder.nativeOrder())
 * 
 * Documentation: 
 * http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeKruskalWallis
 * 
 */

public class Volume_KruskalWallisAnalysis {
	private static final String MISSING_MARK = ".";

    static FileInputStream[] fi;
    static BufferedInputStream[] bi;
    static MemoryCacheImageInputStream[] mciis;
    
	static FileOutputStream pFOS, tFOS;
	static BufferedOutputStream pBOS, tBOS;
	static MemoryCacheImageOutputStream pMciis, tMciis;

	static boolean byteswap = false;
	static String byteorder = "system";

	static boolean outputPValuesVolumes=false;
	static boolean outputKWStatVolumes=false;

	static double pValue;
	static double tStat;
	
	static 	int bufferSize = 8192;		// This contains the size of the Input/Output Buffer
	
	public static void main(String[] args) {
		//FileHelper fileHelper1 = new FileHelper();
		//fileHelper.openReader("data1.txt");
		String designMatrixInputFile = null; //"C:\\STAT\\SOCR\\test\\dataOne.txt\\"
		String maskInputVolume=null;		// mask input volume
		
		String pValue_Filename=null;
		String tStat_Filename=null;
		
		int factorColumnIndex = 1;	// These index containing the column
									// of the user-specified factor column
		
		int[] dim = new int[3];		// the sizes of the 3D volume dimensions
						
		boolean [][][] maskVolumeBoolean=new boolean[1][1][1];			// [Zmax][Ymax][Xmax]
		maskVolumeBoolean[0][0][0] = false;
		
		int data_type = 4;							//image intensities data type
		
		Vector<String> inputAnalyzeFilenames = new Vector<String>();	
													// The names of the Analyze Input Files 
													// stored in column 2 of the Design-Matrix
		
		HashMap<String, Integer> inputFactorLevels = new HashMap<String,Integer>();
		int factorLevels = 0;
//		int[] factorLevelSizes = new int[1];
		
		String factorString = "";
		
		boolean header=false;
		boolean filesLoaded = false;
		
		int i, j,k;
		
		System.out.println(
		"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeKruskalWallis");

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
					// Sept 10, 2009, commented out this line to fix a problem with odd dimension sizes
					// which generated a buffer-flush problem in this method: getNextInputDataBuffer()
					bufferSize = dim[0]*dim[1];
					System.out.println("dim[2]="+dim[2]+"\tdim[1]="+dim[1]+"\tdim[0]="+dim[0]+
							"\tbufferSize="+bufferSize);
				} else if (args[i].compareToIgnoreCase("-p")==0) { 
					outputPValuesVolumes=true;
					pValue_Filename = args[++i];
					System.out.println("pValue_Filename="+pValue_Filename);
				} else if (args[i].compareToIgnoreCase("-t")==0) { 
					outputKWStatVolumes=true;
					tStat_Filename = args[++i];
					System.out.println("tStat_Filename="+tStat_Filename);
				} else if (args[i].compareToIgnoreCase("-byteswap")==0) { 
					byteswap=true;
					System.out.println("Byteswap="+byteswap);
				} else if (args[i].compareToIgnoreCase("-byteorder")==0) { 
					byteorder = args[++i];
					System.out.println("byteorder="+byteorder);
				} else if (args[i].compareToIgnoreCase("-factor")==0) {
					factorString = args[++i];
					System.err.println("factorString="+factorString);
				} else if (args[i].compareToIgnoreCase("-dm")==0) {
					designMatrixInputFile = args[++i];
					filesLoaded = true;
					System.out.println("designMatrixInputFile="+designMatrixInputFile);
				} else if (args[i].compareToIgnoreCase("-help")==0) {
					System.out.println("Please see the Web syntax for involking this tool: \n"+
					"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeKruskalWallis");
					return;
				}  else if (args[i].compareToIgnoreCase("-mask")==0) {
					maskInputVolume = args[++i];
					System.out.println("maskInputVolume="+maskInputVolume);
				}
			}
		} catch (Exception e) {
			System.out.println("Incorrect call 1! Please see the syntax for involking this tool\n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeKruskalWallis");
		}
		
		if (!filesLoaded) {
			System.out.println("Incorrect call 2! Please see the syntax for involking this tool\n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeKruskalWallis");
			return;
		}
						
		TwoIndependentKruskalWallisResult result = null;

		StringTokenizer st = null;
		
		int designMatrixNumberColumns = 1000;	// Maximum number of DesignMatrix columns
		
		String input = "";
		int xLength=0;
		
		String[] varHeader = new String[1];
		//varHeader[1] = "var";
		
		ArrayList<String>[] xList = new ArrayList [1];
		
		for (i = 0; i < xList.length; i++) 
			xList[i] = new ArrayList<String>();

		String line = null, stringTmp;
		
		boolean read=true;
		if (header)
			read=false;

		//System.err.println("\n header = " + header);
		try {
			BufferedReader bReader  = new BufferedReader(new FileReader(designMatrixInputFile));
            int row = 0;
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
							if (varHeader[k].compareToIgnoreCase(factorString)==0) {
								factorColumnIndex = k;
								System.err.println("varHeader["+k+"]="+varHeader[k]+
											"\t factorColumnIndex ="+ k);
							}
						}
				   		
				   		// Include a test to make sure this array is defined, as if there is
				   		// no variable match in the DM, then there is no need for any analysis.
				   		if (factorColumnIndex<2) {
				   			System.out.println("\nNo variable ("+factorString+") match!!!\n"); 
				   			System.exit(0); 
				   		}
				   		
				   		read=true;
					} else if (!header) {		// Must have a header row to deduce the predictors
						System.out.println("No Independent Variable Specified 1. Cannot run the regression\n"+
						"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeKruskalWallis");
						return;
					} else {					// READ DATA From Design-Matrix
						try {
							int n = 0;	// Column Index							
							while (st.hasMoreElements()) {
								stringTmp = st.nextToken().trim();
								if (n==0) { ; } // Skip first column of Design-Matrix - it contains SubjectID 
								else if (n==1) // Read IMG/ANALYZE File Name
									inputAnalyzeFilenames.addElement(stringTmp);
								else { 
									if (n==factorColumnIndex) {
										input = stringTmp;
                                        int instances;
										if (read && !input.equalsIgnoreCase(MISSING_MARK) ) {
											xList[0].add(input);
                                            if (!inputFactorLevels.containsKey(input)){
                                                inputFactorLevels.put(input, 1);
                                                System.err.println("Factor-Level["+factorLevels+"]="+inputFactorLevels.get(factorLevels));
                                                factorLevels++;
                                            }else{
                                                instances = inputFactorLevels.get(input);
                                                inputFactorLevels.put(input, ++instances);
                                            }
										}
										System.out.println("Reading DM: column="+n+"\tValue="+input);
									}
								} 
								++n;
							}
							read=true;
						} catch (NoSuchElementException e) {
							//e.printStackTrace();
							System.out.println("Volume Kruskal-Wallis Analysis: 1");
							return;
						} catch (Exception e) {
							//e.printStackTrace();
							System.out.println("Volume Kruskal-Wallis Analysis: 2: "+e);
							return;
						}
					}
                    row++;
			}
			
			// Determine the number of distinct factorLevels and their Lables
//			factorLevels = 0;
//			System.err.println("Determining the Factor Levels ...");
//			for (int ind1 = 0; ind1 < xList[0].size(); ind1++) {
//				if (!inputFactorLevels.contains(xList[0].get(ind1))){
//					inputFactorLevels.add(xList[0].get(ind1));
//					System.err.println("Factor-Level["+factorLevels+"]="+inputFactorLevels.get(factorLevels));
//					factorLevels++;
//				}
//			}
			// Determine the X-data size at each factor-level (factorLevelSizes)
//			factorLevelSizes = new int [inputFactorLevels.size()];
//			for (int ind2=0; ind2<inputFactorLevels.size(); ind2++) factorLevelSizes[ind2]=0;
//
//			for (int ind1 = 0; ind1 < xList[0].size(); ind1++) {
//				for (int ind2=0; ind2<inputFactorLevels.size(); ind2++) {
//					if (xList[0].get(ind1).equalsIgnoreCase(inputFactorLevels.get(ind2)))
//						factorLevelSizes[ind2]++;
//						//System.out.println("ind1 is " + ind1 + ", ind2 is " + ind2 + ", xList[0].get(ind1) is " + xList[0].get(ind1) + ", and inputFactorLevels.get(ind2) is " + inputFactorLevels.get(ind2));
//				}
//			}
//			for (int ind2=0; ind2<inputFactorLevels.size(); ind2++)
//				System.err.println("\t Factor="+inputFactorLevels.get(ind2)+
//						"\t factorLevelSizes["+ind2+"]="+factorLevelSizes[ind2]);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		if (!header)
		{	System.out.println("No Independent Variable Specified in Design-Matrix2. Cannot run the analysis\n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeKruskalWallis");
			return;
		}
		
		xLength=inputAnalyzeFilenames.size();
		System.out.println("Number of volumes = " + xLength);

        HashMap<String, ArrayList<Double>> factorLevelMap = null;
		double[] xData = null;
		double[] yData = new double [xLength];
		double[][] xDataArray = new double[factorLevels][1];
		
        //create streams of the Analyze Image files
        fi = new FileInputStream[xLength];
        bi = new BufferedInputStream[xLength];
        mciis = new MemoryCacheImageInputStream[xLength];
                
        for (i = 0; i< xLength; i++) {
        	try { 
        		fi[i] = new FileInputStream(inputAnalyzeFilenames.elementAt(i));
        		bi[i] = new BufferedInputStream(fi[i], bufferSize);
        		mciis[i] = new MemoryCacheImageInputStream(bi[i]);
        		setByteOrder(mciis[i], byteorder);
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
		pValue = 1.0;
		tStat = 0.0;

		// Create File Output Streams
		try { 
			
			if (outputPValuesVolumes==true) {
				pFOS = new FileOutputStream(pValue_Filename+
					"_Reg_"+factorString+".img");
				pBOS = new BufferedOutputStream(pFOS, bufferSize);
				pMciis = new MemoryCacheImageOutputStream(pBOS);
				setByteOrder(pMciis, byteorder);
			} 
			if (outputKWStatVolumes) {
					tFOS = new FileOutputStream(tStat_Filename+
						"_TStat_"+factorString+".img");
					tBOS = new BufferedOutputStream(tFOS, bufferSize);
					tMciis = new MemoryCacheImageOutputStream(tBOS);
					setByteOrder(tMciis, byteorder);
			}
		
	        System.err.println("Done opening OUTPUT result streams!");
	        System.err.println("Beginning the stat analyses ... ");
	        
	        boolean writeResultsOrDefaults = true; // true==results; false=Default
	        int counter = 0;	// integer counter of the voxel index
	        
	        edu.ucla.stat.SOCR.distributions.StudentDistribution studentT = 
	        	new edu.ucla.stat.SOCR.distributions.StudentDistribution(xLength);
	        
	        int flushCounter = 0;
	        
			for (i=0; i< dim[2]; i++) {
				for(j = 0; j < dim[1]; j++) {
					for(k = 0; k < dim[0]; k++) {
				        counter++;
						result = null;
					
						// if NO mask is provided OR (mask exists and (k,j,i) xoxel is inside the mask)
				        if (maskInputVolume==null || (maskInputVolume!=null && maskVolumeBoolean[i][j][k]==true)) {
							// Get all volume intensities at the given voxel (k,j,i)
							if ((counter%(100*bufferSize))==0) {
								yData = getNextInputDataBuffer(data_type, xLength, flushCounter);
								System.err.println("After Reading imaging data, counter="+counter);
							} else yData = getNextInputDataBuffer(data_type, xLength, flushCounter);
							flushCounter++;
							
							data = new Data();

                            //to make sure the order of varHeader matches that of each xData array
                            //sort the factors in the inputFactorLevels map
                            TreeSet sortedFactors = new TreeSet(inputFactorLevels.keySet());
                            varHeader = (String[])sortedFactors.toArray(new String[0]);

                            factorLevelMap = new HashMap<String, ArrayList<Double>>();
                            for (int ind = 0; ind < xLength; ind++){
                            	//System.out.println(ind + " for each file, store the voxel intensity under appropriate factor level");
                                ArrayList<Double> currList = factorLevelMap.get(xList[0].get(ind));
                                if (currList != null){
                                    currList.add(yData[ind]);
                                }else{
                                    ArrayList<Double> newList = new ArrayList<Double>();
                                    newList.add(yData[ind]);
                                    factorLevelMap.put(xList[0].get(ind), newList);
                                }
                            }

                            //use the ordering of factors from above to extract intensities for each xData array
                            ArrayList<Double> intensityList;
                            Double[] tmp;
                            //System.out.println("factorLevels: "+factorLevels);
                            //System.out.println("for each factor level, construct an array of doubles");
                            
                            for (int ind = 0; ind < factorLevels; ind++){
                            	//System.out.println("varHeader["+ind+"]:"+varHeader[ind]);
                            	//System.out.println("inputFactorLevel.get("+varHeader[ind]+"):"+inputFactorLevels.get(varHeader[ind]));
                               	xData = new double[inputFactorLevels.get(varHeader[ind])];
                                //retrieve the list of intensity values corresponding to a given factor level
                                intensityList = factorLevelMap.get(varHeader[ind]);
                                //System.out.println("checkpoint2");
                                tmp = (Double[])intensityList.toArray(new Double[0]);
                                //convert from Double object to double primitive
                                //System.out.println("converting Doubles to doubles");
                                for (int ind2 = 0; ind2 < tmp.length; ind2++){
                                	//System.out.println("tmp["+ind2+"].doubleValue:"+tmp[ind2].doubleValue());
                                	xData[ind2] = tmp[ind2].doubleValue();
                                	//System.out.println("checkpoint3");
                                }
                                //append xData
                                //System.out.println("appending Xdata");
                                data.appendX(varHeader[ind], xData, DataType.QUANTITATIVE);
                            }
							 				
							/************* Run Analysis: ************/
							try {
								result = (TwoIndependentKruskalWallisResult)
    								data.getAnalysis(AnalysisType.TWO_INDEPENDENT_KRUSKAL_WALLIS);
							} catch (Exception e) {	System.out.println("Results Problem (6): "+e); }
							if (result==null) { System.err.print("Results Problem (7)"); return; }
							
							//System.err.println("Done computing results, counter="+counter);
							
							tStat = result.getTStat();
							if (tStat>=0) pValue = 1-studentT.getCDF(tStat);
							else pValue = studentT.getCDF(tStat);
															;
								/**** Check output ****/
								if (k==85 && j==173 && i==110) {
									System.out.println("Checking OUTPUT ...");
									System.out.println("Voxel 1: (85,173,110)\t result.getVariableList()="+
										"\t Factor="+factorString+
										"\t p-value[="+pValue+
										"\t T-Stat="+tStat);
								} else if (k==110 && j==110 && i==110) {
									System.out.println("Checking OUTPUT ...");
									System.out.println("Voxel 1: (110,110,110)\t result.getVariableList()="+
										"\t Factor="+factorString+
										"\t p-value[="+pValue+
										"\t T-Stat="+tStat);
								} 	// end of else if 
							
							// Write Results out
							writeResultsOrDefaults=true;
							if ((counter%bufferSize)==0) 
								putNextOutputDataBuffer(pValue, tStat, writeResultsOrDefaults);
							else 
								putNextOutputDataBuffer(pValue, tStat, writeResultsOrDefaults);							
						} // End of IF No-mask OR (mask exists AND (k,j,i) xoxel is inside the mask)
				        
						else { // if (maskInputVolume!=null && maskVolumeBoolean[i][j][k]==false) {
							pValue = 1.0;
							tStat = 0.0;
							if ((counter%bufferSize)==0) {
								skipNextInputDataBuffer(data_type, xLength, true);
								writeResultsOrDefaults=false;
								putNextOutputDataBuffer(pValue, tStat, writeResultsOrDefaults);
							} else {
								skipNextInputDataBuffer(data_type, xLength, false);
								writeResultsOrDefaults=false;
								putNextOutputDataBuffer(pValue, tStat, writeResultsOrDefaults);
							}
							//System.err.println("Done Skipping over outside-mask voxels, counter="+counter);
						} // END: if mask exists and (k,j,i) xoxel is OUTSIDE the mask
					} // Close dim[2], dim[1] and dim[0] Loops
				}	
			}	// Close all 3 Loops
			
	        System.err.println("Done WRITING all results to output streams!");

	        	// Close all INPUT Streams
	        for (int fileList=0; fileList<xLength; fileList++) {
	        	mciis[fileList].close();
    			bi[fileList].close();
    			fi[fileList].close();
	        }
				// Close all OUTPUT Streams
			if (outputPValuesVolumes==true) {
					pMciis.flush();
					pMciis.close();
					pBOS.flush();
					pBOS.close();
					pFOS.flush();
					pFOS.close();
					System.out.println("p-Value Output File="+pValue_Filename+
							"_Reg_"+factorString+".img");
			}
			if (outputKWStatVolumes) {
					tMciis.flush();
					tMciis.close();
					tBOS.flush();
					tBOS.close();
					tFOS.flush();
					tFOS.close();
					System.out.println("T-Stats Output File ="+tStat_Filename+
							"_TStat_"+factorString+".img");
			}
			
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!\n!!!!!!Complete!!!!!!!\n!!!!!!!!!!!!!!!!!!!!!!!!!!");
			
		} catch (Exception e) {
			System.err.println(e.getCause());
			System.err.println(e.getLocalizedMessage());
			System.err.println("Volume Kruskal-Wallis Analysis Error!!!!!!!!!!!!!!");
			System.err.println(e.getStackTrace());
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
	 * @param xLength - number of volumes/subjects in the second-column (file-names) in the Design-matrix
	 * @param flush -	variable indicating whether the buffer should be flushed as the stream keeps
	 * 					in memory all the data it has read from disk. This will free up memory, but too frequent 
	 * 					calls (flush==true) will slow down the process!
	 */
	public static double[] getNextInputDataBuffer(int data_type, int xLength, int flush) {
		double[] newInputData = new double[xLength];
		
		//System.err.println("Beginning (getNextInputDataBuffer), xLength="+xLength+"\tflush="+flush);
        if (data_type==0) {			// Unsigned BYTE input
        	for (int fileList = 0; fileList< xLength; fileList++) {
        		try { 
        			newInputData[fileList] = mciis[fileList].readUnsignedByte();
        			/* 	long position = mciis[fileList].getStreamPosition();
        				if (flush && position > bufferSize) 
        				mciis[fileList].flushBefore(position-bufferSize);
        			*/
        			if (flush % bufferSize == 1) mciis[fileList].flush();
        		} catch (Exception e) {
        			newInputData[fileList] = 0;
        			System.err.println("(0) newInputData["+fileList+"]= 0\n");
        		}
			}
        } else if (data_type==1) {			// Signed BYTE input
        	for (int fileList = 0; fileList< xLength; fileList++) {
        		try { 
        			newInputData[fileList] = mciis[fileList].readByte();
        			if (flush % bufferSize == 1) mciis[fileList].flush();
        		} catch (Exception e) {
        			newInputData[fileList] = 0;
        			System.err.println("(1) newInputData["+fileList+"]= 0\n");
        		}
			}
        } else if (data_type==2) {			// SHORT Unsigned Integer input
        	for (int fileList = 0; fileList< xLength; fileList++) {
        		try { 
        			if (byteswap==false) newInputData[fileList] = mciis[fileList].readUnsignedShort();
        			else newInputData[fileList] = swapShort(
        					shortToBytes((short)mciis[fileList].readUnsignedShort()),0);
        			if (flush % bufferSize == 1) mciis[fileList].flush();       			
        		} catch (Exception e) {
        			newInputData[fileList] = 0;
        			System.err.println("(2) newInputData["+fileList+"]= 0\n");
        			//System.err.println("Exception: "+e.printStackTrace());
        			System.err.println("Exception: "+e);
        			System.err.println("mciis["+fileList+"]="+mciis[fileList].toString());
        			System.err.println("getCause="+e.getCause()
        					+"\t getLocalizedMessage="+e.getLocalizedMessage()
        					+"\t getStackTrace="+e.getMessage());      
        			try { System.err.println("mciis[fileList].getStreamPosition()="+
        					mciis[fileList].getStreamPosition());
        			} catch (IOException e1) {
        				System.err.println("IOException e1="+e1);
        			}
        		}
			}
        } else if (data_type==3) {			// SHORT Signed-Integer input
        	for (int fileList = 0; fileList< xLength; fileList++) {
        		try { 
        			if (byteswap==false) newInputData[fileList] = mciis[fileList].readShort();
        			else newInputData[fileList] = swapShort((short)mciis[fileList].readShort());
        			if (flush % bufferSize == 1) mciis[fileList].flush();
        		} catch (Exception e) {
        			newInputData[fileList] = 0;
        			System.err.println("(3) newInputData["+fileList+"]= 0\n");
        		}
			}
        } else if (data_type==4) {				// FLOAT Input
        	for (int fileList = 0; fileList< xLength; fileList++) {
        		try { 
        			if (byteswap==false) newInputData[fileList] = mciis[fileList].readFloat();
        			else newInputData[fileList] = swapFloat(mciis[fileList].readFloat());
        			if (flush % bufferSize == 1) mciis[fileList].flush();
        		} catch (Exception e) {
        			newInputData[fileList] = 0;
        			System.err.println("(4) newInputData["+fileList+"]= 0\n");
        		}
			}
        } else {
        	System.out.println("Incorrect Input-Volume Data Type: "+data_type+". Cannot run the regression\n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeKruskalWallis");
			return null;
        }
        //System.err.println("End (getNextInputDataBuffer), flush="+flush);
        return newInputData;
	}	// End: getNextInputDataBuffer
	
	/**
	 * This method WRITES OUT bufferSize chunks of all output volumes
	 * @param result is a (MultiLinearRegressionResult) Result object
	 * @param beta is a beta/regression value outputted by (MultiLinearRegressionResult) Result object
	 * @param pValue is a Probability value outputed by (MultiLinearRegressionResult) Result object
	 * @param tStat is a T-statistics as outputed by (MultiLinearRegressionResult) Result object
	 * @param independentLength - integer value representing the number of Independent X Variables
	 * @param writeResultsOrDefaults - true==results; false=Default
	 */
	//public static void putNextOutputDataBuffer(MultiLinearRegressionResult result, 
	public static void putNextOutputDataBuffer(double pValue, double tStat,	boolean writeResultsOrDefaults) {
		
		float tempFloat = 0;			// A temporary Float Variable

    	if (writeResultsOrDefaults==false) {    // || result==null) {
    		// if mask exists and (k,j,i) xoxel is OUTSIDE the mask write trivial output)
    		//System.out.println("\t\t Writing out trivial Defaults (not results!)");
    		tempFloat=(float)0.0;
    		    		
    		if (outputPValuesVolumes==true) {
    			try {
    				if (byteswap==false) pMciis.writeFloat(tempFloat+1);
    				else pMciis.writeFloat(swapFloat(tempFloat+1));
    					//if (flush) pMciis[regLength].flush();
    			} catch (IOException e) {System.err.println(e); }
    		}
    		if (outputKWStatVolumes==true) {
    			try {
    				if (byteswap==false) tMciis.writeFloat(tempFloat);
    				else tMciis.writeFloat(swapFloat(tempFloat));
    				//if (flush) tMciis[regLength].flushBefore(tMciis[regLength].getStreamPosition());
    			} catch (IOException e) {System.err.println(e); }
    		}
		} 	// END: writeResultsOrDefaults==false <==>
			// if mask exists and (k,j,i) xoxel is OUTSIDE the mask	
		else {  // if(writeResultsOrDefaults=true) {	// write out real results	
			// The first estimate-value and p-value are for the CONSTANT term.
			// Thus, we report only estimates/effect-sizes and p-values for
			// user-specified predictors/covariates only!
			// independentLength should equal to beta.length
			if (outputPValuesVolumes) {
				tempFloat = (float)pValue;
				try {
					//pDataOS[regLength].writeFloat(tempFloat);
					if (byteswap==false) pMciis.writeFloat(tempFloat);
					else pMciis.writeFloat(swapFloat(tempFloat));
					//if (flush) pMciis[regLength].flush();
				} catch (IOException e) {System.err.print(e); }
			}
			if (outputKWStatVolumes) {
				tempFloat = (float)tStat;
				try {
					//tDataOS[regLength].writeFloat(tempFloat);
					if (byteswap==false) tMciis.writeFloat(tempFloat);
					else tMciis.writeFloat(swapFloat(tempFloat));
					//if (flush) tMciis[regLength].flushBefore(tMciis[regLength].getStreamPosition());
				} catch (IOException e) { System.err.print(e); }
			}
			//System.err.println("\t\t Completed First Iteration!");
		} 	// End of IF writeResultsOrDefaults=true <==> 
			// No-mask OR (mask exists AND (k,j,i) xoxel is inside the mask)	
   	} // END: putNextOutputDataBuffer
	
	/**
	 * This method SKIPS in reading data from the bufferSize chunk of all input volumes
	 * @param data_type - type of the input data
	 * @param xLength - number of volumes/subjects in the second-column (file-names) in the Design-matrix
	 * @param flush -	Boolean variable indicating whether the buffer should be flushed as teh ...keeps
	 * 					in memory all the data it has read from disk. This will free up memory, but too frequent 
	 * 					calls (flush==true) will slow down the process!
	 */
	public static void skipNextInputDataBuffer(int data_type, int xLength, boolean flush) {
		//long position;
		
		if (data_type==0 || data_type==1) {			// BYTE input
        	for (int fileList = 0; fileList< xLength; fileList++) {
        		try { 	
        			mciis[fileList].skipBytes(1); 
        			/* if (mciis[fileList].getStreamPosition() % bufferSize == 1)
        				mciis[fileList].flush();
        			    position = mciis[fileList].getStreamPosition();
        				if (flush && position > bufferSize) 
        				mciis[fileList].flushBefore(position-bufferSize);
        			*/
        		}
        		catch (IOException e) { System.err.print(e); }
        	}
        } else if (data_type==2 || data_type==3) {			// SHORT 2-Byte Integer input
        	for (int fileList = 0; fileList< xLength; fileList++) {
        		try { 
        			mciis[fileList].skipBytes(2);
        			/*  if (mciis[fileList].getStreamPosition() % bufferSize == 1)
        				mciis[fileList].flush();
	        			position = mciis[fileList].getStreamPosition();
        				if (flush && position > 2*bufferSize) 
        				mciis[fileList].flushBefore(position-bufferSize);
        			*/
        		}
        		catch (IOException e) { System.err.print(e); }
        	}
        } else if (data_type==4) {				// FLOAT 4-byte Input
        	for (int fileList = 0; fileList< xLength; fileList++) {
        		try { 
        			mciis[fileList].skipBytes(4); 
        			/* if (mciis[fileList].getStreamPosition() % bufferSize == 1)
        				mciis[fileList].flush();
        				position = mciis[fileList].getStreamPosition();
        				if (flush && position > bufferSize) 
        				mciis[fileList].flushBefore(position-bufferSize);
        			*/ 
        		}
        		catch (IOException e) { System.err.print(e); }
        	}
        } else {
        	System.out.println("Incorrect Input-Volume Data Type: "+data_type+". Cannot run the regression\n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeKruskalWallis");
			return;
        }
        return;
	}	// End: skiptNextInputDataBuffer

    /**
     * Returns the short resulting from swapping 2 bytes at a specified
     * offset in a byte array.
     *
     * @param  b       the byte array
     * @param  offset  the offset of the first byte
     * @return         the short represented by the bytes
     *                 <code>b[offset+1], b[offset]</code>
     */
    static public short swapShort(byte[] b, int offset) {
        // 2 bytes
        int low  = b[offset] & 0xff;
        int high = b[offset + 1] & 0xff;
        return (short) (high << 8 | low);
    }

    /**
     * Returns the int resulting from reversing 4 bytes at a specified
     * offset in a byte array.
     *
     * @param  b       the byte array
     * @param  offset  the offset of the first byte
     * @return         the int represented by the bytes
     *                 <code>b[offset+3], b[offset+2], ..., b[offset]</code>
     */
    static public int swapInt(byte[] b, int offset) {
        // 4 bytes
        int accum = 0;
        for (int shiftBy = 0, i = offset; shiftBy < 32; shiftBy += 8, i++) {
            accum |= (b[i] & 0xff) << shiftBy;
        }
        return accum;
    }

    /**
     * Returns the long resulting from reversing 8 bytes at a specified
     * offset in a byte array.
     *
     * @param  b       the byte array
     * @param  offset  the offset of the first byte
     * @return         the long represented by the bytes
     *                 <code>b[offset+7], b[offset+6], ..., b[offset]</code>
     */
    static public long swapLong(byte[] b, int offset) {
        // 8 bytes
        long accum = 0;
        long shiftedval;
        for (int shiftBy = 0, i = offset; shiftBy < 64; shiftBy += 8, i++) {
            shiftedval = ((long) (b[i] & 0xff)) << shiftBy;
            accum      |= shiftedval;
        }
        return accum;
    }

    /**
     * Returns the float resulting from reversing 4 bytes at a specified
     * offset in a byte array.
     *
     * @param  b       the byte array
     * @param  offset  the offset of the first byte
     * @return         the float represented by the bytes
     *                 <code>b[offset+3], b[offset+2], ..., b[offset]</code>
     */
    static public float swapFloat(byte[] b, int offset) {
        int accum = 0;
        for (int shiftBy = 0, i = offset; shiftBy < 32; shiftBy += 8, i++) {
            accum |= (b[i] & 0xff) << shiftBy;
        }
        return Float.intBitsToFloat(accum);
    }

    /**
     * Returns the double resulting from reversing 8 bytes at a specified
     * offset in a byte array.
     *
     * @param  b       the byte array
     * @param  offset  the offset of the first byte
     * @return         the double represented by the bytes
     *                 <code>b[offset+7], b[offset+6], ..., b[offset]</code>
     */
    static public double swapDouble(byte[] b, int offset) {
        long accum = 0;
        long shiftedval;
        for (int shiftBy = 0, i = offset; shiftBy < 64; shiftBy += 8, i++) {
            shiftedval = ((long) (b[i] & 0xff)) << shiftBy;
            accum      |= shiftedval;
        }
        return Double.longBitsToDouble(accum);
    }

    /**
     * Returns the char resulting from swapping 2 bytes at a specified
     * offset in a byte array.
     *
     * @param  b       the byte array
     * @param  offset  the offset of the first byte
     * @return         the char represented by the bytes
     *                 <code>b[offset+1], b[offset]</code>
     */
    static public char swapChar(byte[] b, int offset) {
        // 2 bytes
        int low  = b[offset] & 0xff;
        int high = b[offset + 1] & 0xff;
        return (char) (high << 8 | low);
    }

    /**
     * Returns the short resulting from swapping 2 bytes of a specified
     * short.
     *
     * @param  s       input value for which byte reversal is desired
     * @return         the value represented by the bytes of <code>s</code>
     *                 reversed
     */
    static public short swapShort(short s) {
        return (swapShort(shortToBytes(s), 0));
    }

    /**
     * Returns the int resulting from reversing 4 bytes of a specified
     * int.
     *
     * @param  v       input value for which byte reversal is desired
     * @return         the value represented by the bytes of <code>v</code>
     *                 reversed
     */
    static public int swapInt(int v) {
        return (swapInt(intToBytes(v), 0));
    }

    /**
     * Returns the long resulting from reversing 8 bytes of a specified
     * long.
     *
     * @param  l       input value for which byte reversal is desired
     * @return         the value represented by the bytes of <code>l</code>
     *                 reversed
     */
    static public long swapLong(long l) {
        return (swapLong(longToBytes(l), 0));
    }

    /**
     * Returns the float resulting from reversing 4 bytes of a specified
     * float.
     *
     * @param  v       input value for which byte reversal is desired
     * @return         the value represented by the bytes of <code>v</code>
     *                 reversed
     */
    static public float swapFloat(float v) {
        //int l = swapInt(Float.floatToRawIntBits(v));
    	int l = Float.floatToRawIntBits(v);
        
        byte[] buffer = new byte[4];
            
        buffer[0] = (byte)l;
		buffer[1] = (byte)(l>>8);
		buffer[2] = (byte)(l>>16);
		buffer[3] = (byte)(l>>24);
 
        //return (Float.intBitsToFloat(l));
		//return (Float.intBitsToFloat(byteArrayToInt(buffer, 0)));
		v = Float.intBitsToFloat(byteArrayToInt(buffer, 0));
		if (Float.isInfinite(v) || Float.isNaN(v)) v=0;			
		return v;
    }

    /**
     * Convert the byte array to an int starting from the given offset.
     * @param b The byte array
     * @param offset The array offset
     * @return The integer
     */
    public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }

    /**
     * Returns the double resulting from reversing 8 bytes of a specified
     * double.
     *
     * @param  v       input value for which byte reversal is desired
     * @return         the value represented by the bytes of <code>v</code>
     *                 reversed
     */
    static public double swapDouble(double v) {
        long l = swapLong(Double.doubleToLongBits(v));
        return (Double.longBitsToDouble(l));
    }

    /**
     * Convert a short to an array of 2 bytes.
     *
     * @param  v       input value
     * @return         the corresponding array of bytes
     */
    static public byte[] shortToBytes(short v) {
        byte[] b       = new byte[2];
        int    allbits = 255;
        for (int i = 0; i < 2; i++) {
            b[1 - i] = (byte) ((v & (allbits << i * 8)) >> i * 8);
        }
        return b;
    }

    /**
     * Convert an int to an array of 4 bytes.
     *
     * @param  v       input value
     * @return         the corresponding array of bytes
     */
    static public byte[] intToBytes(int v) {
        byte[] b       = new byte[4];
        int    allbits = 255;
        for (int i = 0; i < 4; i++) {
            b[3 - i] = (byte) ((v & (allbits << i * 8)) >> i * 8);
        }
        return b;
    }

    /**
     * Convert a long to an array of 8 bytes.
     *
     * @param  v       input value
     * @return         the corresponding array of bytes
     */
    static public byte[] longToBytes(long v) {
        byte[] b       = new byte[8];
        long   allbits = 255;
        for (int i = 0; i < 8; i++) {
            b[7 - i] = (byte) ((v & (allbits << i * 8)) >> i * 8);
        }
        return b;
    }

}
