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

import java.io.*;

import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

/*
 * This class provides the functionality to construct Masks of SOCR Analysis resutls given 
 * a user-specified threshold value:
 * 
 * Building: via the SOCR build.xml file, as part of SOCR Analyses
 * 
 * Invocation/Call: 
 * java -cp /ifs/ccb/CCB_SW_Tools/others/Statistics/SOCR_Statistics/bin/SOCR_core.jar:
 * 			/ifs/ccb/CCB_SW_Tools/others/Statistics/SOCR_Statistics/bin/SOCR_plugin.jar 
 * 			edu.ucla.stat.SOCR.analyses.command.SplitMaskPositiveNegativeAnalysisResults 
 * 			-dim Zmax Ymax XMax -input filename -mask mask_filename -threshold Value 
 * 			[-below filename] [-above filename] -data_type [0,1,2,3,4]
 * 
 * Options:
 * 			-help: print usage
 * 			-mask [Mask-volume.img]: specify a mask-volume (0 or 1 intensities) restricting the voxels
 * 										where the regression models are computed (optional),
 * 										1Byte Analyze format volume of the same dimensions as the data 
 * 			-dim Zmax Ymax XMax: 	specify the dimension-sizes (for 2D images use ZMax=1, for 1D, Zmax=Y_Max=1
 * 			-below [Filename]: 		output mask of the intensities, where input <= threshold (1-Byte) 
 * 			-above [Filename]:		output mask of the intensities, where input > threshold (1-Byte) 
 * 			-threshold [threshold_value]:	threshold value separating the below/above intensities of the input file
 * 			-input  [Filename]:		input file-name
 * 			-data_type [0,1,2,3,4]:	Type=0 is for Unsinged Byte input volumes; 
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
 * 	http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression#Volume_Multiple_Linear_Regression_Usage
 * 
 */

public class SplitMaskPositiveNegativeAnalysisResults {

	static String byteorder = "system";

	public static void main(String[] args) {
		String inputFile = null; 			//"C:\\STAT\\SOCR\\test\\dataOne.txt\\"
		String maskInputVolume=null;		// mask input volume
		
		String belowFilename=null;
		String aboveFilename=null;
		
		float threshold = 0;
				
		int[] dim = new int[3];		// the sizes of the 3D volume dimensions
		boolean belowOutputVolume=false;
		boolean aboveOutputVolume=false;
		
		float [][][] inputData;					// [Zmax][Ymax][Xmax]
		
		boolean [][][] maskVolumeBoolean=new boolean[1][1][1];			// [Zmax][Ymax][Xmax]
		maskVolumeBoolean[0][0][0] = false;
		
		int data_type = 4;							//image intensities data type
		
		boolean byteswap = false;
				
		int i, j,k;
		
		System.out.println(
		"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression#Volume_Multiple_Linear_Regression_Usage");

		try {
			for (i=0; i<args.length; i++) {
				if (args[i].compareToIgnoreCase("-data_type")==0) {
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
				} else if (args[i].compareToIgnoreCase("-below")==0) { 
					belowOutputVolume=true;
					belowFilename = args[++i];
					System.out.println("belowFilename="+belowFilename);
				} else if (args[i].compareToIgnoreCase("-byteswap")==0) { 
					byteswap=true;
					System.out.println("Byteswap="+byteswap);
				} else if (args[i].compareToIgnoreCase("-byteorder")==0) { 
					byteorder = args[++i];
					System.out.println("byteorder="+byteorder);
				} else if (args[i].compareToIgnoreCase("-above")==0) { 
					aboveOutputVolume=true;
					aboveFilename = args[++i];
					System.out.println("aboveFilename="+aboveFilename);
				} else if (args[i].compareToIgnoreCase("-help")==0) {
					System.out.println("Please see the Web syntax for involking this tool: \n"+
					"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression");
					return;
				} else if (args[i].compareToIgnoreCase("-mask")==0) {
					maskInputVolume = args[++i];
					System.out.println("maskInputVolume="+maskInputVolume);
				} else if (args[i].compareToIgnoreCase("-input")==0) {
					inputFile = args[++i];
					System.out.println("inputFile="+inputFile);
				} else if (args[i].compareToIgnoreCase("-threshold")==0) {
					threshold = Float.valueOf(args[++i]);
					System.out.println("threshold="+threshold);
				}
			}
		} catch (Exception e) {
			System.out.println("Incorrect call 1! Please see the syntax for involking this tool\n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression#Volume_Multiple_Linear_Regression_Usage");
		}
									
        //create streams of the Analyze Image files
        FileInputStream fi;
        BufferedInputStream bi;
        MemoryCacheImageInputStream mciis;
        
        try { 
        	fi = new FileInputStream(inputFile);
        	bi = new BufferedInputStream(fi);
        	mciis = new MemoryCacheImageInputStream(bi);
        	setByteOrder(mciis, byteorder);

        	inputData = new float[dim[2]][dim[1]][dim[0]];
       
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
        							tmpByte = dis.readUnsignedByte();
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
        		for (i=0; i< dim[2]; i++) {
        			for(j = 0; j < dim[1]; j++) {
        				for(k = 0; k < dim[0]; k++) {
        					try { 
        						inputData[i][j][k] = mciis.readUnsignedByte();
        					} catch (IOException e) {
        						inputData[i][j][k] = 0;
        						System.err.println("(1) inputData[i][j][k] = 0\n");
        					}
        				}
        			}	
        		}
        		try { 
        			mciis.close();
        			bi.close();
        			fi.close();
        		} catch (IOException e) {
        			System.err.println("Can't close the Input Stream!\n");
        		}
        	} else if (data_type==1) {			// Signed BYTE input
        		for (i=0; i< dim[2]; i++) {
        			for(j = 0; j < dim[1]; j++) {
        				for(k = 0; k < dim[0]; k++) {
        					try { 
        					inputData[i][j][k] = mciis.readByte();
        					} catch (IOException e) {
        						inputData[i][j][k] = 0;
        						System.err.println("(2) inputData[i][j][k] = 0\n");
        					}
        				}
        			}	
        		}
        		try { 
        			mciis.close();
        			bi.close();
        			fi.close();
        		} catch (IOException e) {
        			System.err.println("Can't close the Input Stream!\n");
        		}
        	} else if (data_type==2) {			// SHORT Unsigned Integer input
        		for (i=0; i< dim[2]; i++) {
        			for(j = 0; j < dim[1]; j++) {
        				for(k = 0; k < dim[0]; k++) {
        					try { 
        						if (byteswap==false) inputData[i][j][k] = mciis.readUnsignedShort();
        						else inputData[i][j][k] = swap((short)mciis.readUnsignedShort());
        					} catch (IOException e) {
        						inputData[i][j][k] = 0;
        						System.err.println("(3) inputData[i][j][k] = 0\n");
        					}
        				}
        			}	
        		}
        		try { 
        			mciis.close();
        			bi.close();
        			fi.close();
        		} catch (IOException e) {
        			System.err.println("Can't close the Input Stream!\n");
        		}
        	} else if (data_type==3) {			// SHORT Signed-Integer input
        		for (i=0; i< dim[2]; i++) {
        			for(j = 0; j < dim[1]; j++) {
        				for(k = 0; k < dim[0]; k++) {
        					try { 
        						if (byteswap==false) inputData[i][j][k] = mciis.readShort();
        						else inputData[i][j][k] = swap((short)mciis.readShort());
        					} catch (IOException e) {
        						inputData[i][j][k] = 0;
        						System.err.println("(4) inputData[i][j][k] = 0\n");
        					}
        				}
        			}	
        		}
        		try { 
        			mciis.close();
        			bi.close();
        			fi.close();
        		} catch (IOException e) {
        			System.err.println("Can't close the Input Stream!\n");
        		}
        	} else if (data_type==4) {				// FLOAT Input
        		for (i=0; i< dim[2]; i++) {
        			for(j = 0; j < dim[1]; j++) {
        				for(k = 0; k < dim[0]; k++) {
        					try { 
        						if (byteswap==false) inputData[i][j][k] = mciis.readFloat();
        						else inputData[i][j][k] = swap(mciis.readFloat());
        					} catch (IOException e) {
        						inputData[i][j][k] = 0;
        						System.err.println("(5) inputData["+i+"]["+j+"]["+k+"] = 0\n");
        					}
        				}
        			}	
        		}
        		try { 
        			mciis.close();
        			bi.close();
        			fi.close();
        		} catch (IOException e) {
        			System.err.println("Can't close the Input Stream!\n");
        		}
        	} else {
        	System.out.println("Incorrect Input-Volume Data Type. \n"+
			"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression#Volume_Multiple_Linear_Regression_Usage");
			return;
        	}
        
        	System.err.println("Done opening and reading the input data volume: "+inputFile+"!");
       
        	// Create File Output Streams
        	FileOutputStream posFOS, negFOS;
        	BufferedOutputStream posBOS, negBOS;
        	MemoryCacheImageOutputStream posDataOS, negDataOS;
			
        	try { 			
        		if (belowOutputVolume==true) {
					negFOS = new FileOutputStream(belowFilename+"_Below_"+threshold+".img");
					negBOS = new BufferedOutputStream(negFOS);
					negDataOS = new MemoryCacheImageOutputStream(negBOS);
        		} else {
        			negFOS = new FileOutputStream("./tmp");
        			negBOS = new BufferedOutputStream(negFOS);
        			negDataOS = new MemoryCacheImageOutputStream(negBOS);
        		}
        		if (aboveOutputVolume==true) {
					posFOS = new FileOutputStream(aboveFilename+"_Above_"+threshold+".img");
					posBOS = new BufferedOutputStream(posFOS);
					posDataOS = new MemoryCacheImageOutputStream(posBOS);
        		} else {
        			posFOS = new FileOutputStream("./tmp1");
        			posBOS = new BufferedOutputStream(posFOS);
        			posDataOS = new MemoryCacheImageOutputStream(posBOS);
        		}
        		
        		setByteOrder(negDataOS, byteorder);
        		setByteOrder(posDataOS, byteorder);
        		
        		System.err.println("Done opening output streams!");

        		for (i=0; i< dim[2]; i++) {
        			for(j = 0; j < dim[1]; j++) {
        				for(k = 0; k < dim[0]; k++) {
        					if (maskInputVolume==null ||
								(maskInputVolume!=null && maskVolumeBoolean[i][j][k]==true)) {
								// if NO mask is provided OR (mask exists and (k,j,i) xoxel is inside the mask)
        						if (belowOutputVolume==true) {
        							if (inputData[i][j][k] < threshold) {
        								try {
        									negDataOS.writeByte(1);
        								} catch (IOException e) {; }
        							} else if (inputData[i][j][k] >= threshold) {
        								try {
        									negDataOS.writeByte(0);
        								} catch (IOException e) {; }
        							} else { // If NaN values
        								try {
        									negDataOS.writeByte(0);
        								} catch (IOException e) {; }
        							}
        						}
        						if (aboveOutputVolume==true) {
        							if (inputData[i][j][k] > threshold) {
        								try {
        									posDataOS.writeByte(1);
        								} catch (IOException e) {; }
        							} else if (inputData[i][j][k] <= threshold) {
        								try {
        									posDataOS.writeByte(0);
        								} catch (IOException e) {; }
        							} else {	// If NaN values
        								try {
        									posDataOS.writeByte(0);
        								} catch (IOException e) {; }
        							}
        						}
        					} // End of IF No-mask OR (mask exists AND (k,j,i) xoxel is inside the mask)
				        
        					else if (maskInputVolume!=null && maskVolumeBoolean[i][j][k]==false) {
        						// if mask exists and (k,j,i) xoxel is OUTSIDE the mask write trivial output)
        						if (belowOutputVolume==true) {
        							try {
        								negDataOS.writeByte(0);
        							} catch (IOException e) {; }
        						}
        						if (aboveOutputVolume==true) {
        							try {
        								posDataOS.writeByte(0);
        							} catch (IOException e) {; }
        						}
        					} // END: if mask exists and (k,j,i) xoxel is OUTSIDE the mask
						
        				} // Close dim[2], dim[1] and dim[0] Loops
        			}	
        		}	// Close all 3 Loops
			
        		System.err.println("Done WRITING to all output streams!");

				// CLose all output Streams
        		if (belowOutputVolume==true) {
					negDataOS.flush();
					negDataOS.close();
					negBOS.flush();
					negBOS.close();
					negFOS.flush();
					negFOS.close();
					System.out.println("BelowOutputVolume File: "+belowFilename+"_Below_"+threshold+".img");
        		}
        		if (aboveOutputVolume==true) {
        			posDataOS.flush();
        			posDataOS.close();
        			posBOS.flush();
        			posBOS.close();
        			posFOS.flush();
        			posFOS.close();
        			System.out.println("AboveOutputVolume File: "+aboveFilename+"_Above_"+threshold+".img");
        		}
        		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!\n!!!!!!Complete!!!!!!!\n!!!!!!!!!!!!!!!!!!!!!!!!!!");	
        	} catch (Exception e) {
        		System.err.println("VolumeMultipleRegression Error!!!!!!!!!!!!!!");
        	}
        } catch (FileNotFoundException e) { 
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
