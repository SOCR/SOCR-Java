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

/*
 * This class provides a tester/driver for the FDR class
 * 
 * 
 * USAGE: java -ms500m -mx1000m -cp SOCR_core.jar:SOCR_plugin.jar 
 * 		edu.ucla.stat.SOCR.analyses.command.volume.Test_FDR [-v] -input InputFile 
 * 		-output OutputFile -number NumberOfEntries -type ZERO4ASCII_ONE4BINARY
 * 		-fdr_rate value -byteorder string
 * 
 * 			-v:						verbose (avoid, as it prints out too much info)
 * 			-input:					name of input file (comlete name, including the path)
 * 			-output:				name of output file (comlete name, including the path)
 * 			-number:				number of p-values included in the filename
 * 			-type:					type: 0 (ZERO) for ASCII files and 1 (ONE) for BINARY files
 * 			-fdr_rate:				fdr_rate a double, the False Discovery Rate level (e.g., 0.05)
 * 			-byteorder:				big (java.nio.ByteOrder.BIG_ENDIAN)
 * 									little (java.nio.ByteOrder.LITTLE_ENDIAN)
 * 									system (default system byteorder, java.nio.ByteOrder.nativeOrder())
 * 			-mask [Mask-filename]:  specify a mask-file (0 or 1 intensities) restricting the voxels
 * 										where the regression models are computed (optional),
 * 									* Mask for shape/geometry files are of the same type as the text 
 * 										p-values file (e.g., if the p-values file contains 1,000 p-values
 * 										1 per line, then the mask must contain 1,000 lines of 0's and 1's
 * 									* Masks for raw binary volumes must be binary 1-byte volumes of the 
 * 										exact same dimensions as the input volume of raw 4-byte floating p-values.
 * 									* Dependence: If this option is included, it must be preceeded by "-type"
 * 
 * Documentation: 
 * 	http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineFDR_Correction
 * 
 */

public class Test_FDR {
	
	static double[] p_values =  new double[]{0.01, 0.05, 0.1, 0.067, 0.015, 0.02, 
			0.025, 0.009, 0.08, 0.075, 0.01, 0.0001};
	static double [] maskedPValues, resultsPValues;
	
				// p-value =  a double array, the vector of p-values
	static double 	fdr_rate = 0.05;	// fdr_rate a double, the False Discovery Rate level (default is 0.05)
	double 			pid;	// p-value threshold based on independence or positive dependence
	double 			pn;	// Nonparametric p-value threshold
	FDR 			f;		// FDR computing class
	static boolean 	verbose=false;
	int i;
	static String 	inputVolume=null;
	static String 	outputVolume=null;
	static int 		numberOfPValues=0;
	static int  	type=0;		// ZERO4ASCII_ONE4BINARY
							// Type = 0 ==> Input is ascii Floats
							// Type = 1 ==> Input is Binary Floats

	static double 	[] thresholdedPMap;
	static String 	byteorder = "system";
	
	static String 	maskInputVolume=null;	// mask input volume
	static String 	maskInputShape=null;		// mask input shape (for shape p-values)
	static boolean [] maskVolumeBoolean;
	static boolean [] maskShapeBoolean;
	
	/**
	* reportUsage() method
	* Report the usage of this class
	*/
	static public void reportUsage() {
		System.out.println("USAGE: java -ms500m -mx1000m -cp SOCR_core.jar:SOCR_plugin.jar "
				+ "edu.ucla.stat.SOCR.analyses.command.volume.Test_FDR [-v] -input InputFile "
				+ "-output OutputFile -mask MaskFileName -number NumberOfEntries "
				+"-type ZERO4ASCII_ONE4BINARY -fdr_rate value -byteorder string");
		System.out.println("Documentation: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineFDR_Correction");
	}

	/**
	* FDR object constructor
	*/
	public Test_FDR () {		
		//f = new FDR(p_values, fdr_rate);
		f = new FDR(maskedPValues, fdr_rate);
		pid = f.getThreshold();
		pn = f.getNThreshold();
		
		System.out.println("p-value threshold based on independence or "+
				"positive dependence: "+ pid);
		System.out.println("Nonparametric p-value threshold: "+ pn);
		// thresholdedPMap = f.getThresholdedArrayNP();  // Very conservative
		thresholdedPMap = f.getThresholdedArray(); 
				
		/* if (verbose) {
			System.out.print("Raw P-values:\n(");
			for (i=0; i<f.data.length; i++)
				System.out.print(f.data[i]+", ");
			System.out.println(")\n");
		
			System.out.print("FDR Thresholded P-values:\n(");
			for (i=0; i<thresholdedPMap.length; i++)
				System.out.print(thresholdedPMap[i]+", ");
			System.out.println(")\nDone!");
		}
		*/
	}
	
	/**
	* FDR object main method
	* @param args a String array of command-line call arguments
	*/
	public static void main(String[] args) {
		/* 
		 * Call Syntax:
		 * java -ms500m -mx1000m -cp DIR\SOCR2.5\jars\SOCR_core.jar:DIR\SOCR2.5\jars\SOCR_plugin.jar 
		 * edu.ucla.stat.SOCR.analyses.command.volume.Test_FDR [-v] -input InputFile -mask MaskFileName
		 * -output OutputFile -number NumberOfEntries -type ZERO4ASCII_ONE4BINARY -fdr_rate value
		 */
		int i;
		maskVolumeBoolean=new boolean[1];			// [Zmax][Ymax][Xmax]
		maskVolumeBoolean[0] = false;
		
		for (i=0; i<args.length; i++) {
			//System.err.println("args["+i+"]="+args[i]);
			
			if (args[i].startsWith("-v")) verbose=true;
			else if (args[i].compareToIgnoreCase("-input")==0) {
				inputVolume = args[++i];
				System.err.println("InputVolume="+inputVolume);
			} else if (args[i].compareToIgnoreCase("-output")==0) {
				outputVolume = args[++i];
				System.err.println("OutputVolume="+outputVolume);
			} else if (args[i].compareToIgnoreCase("-number")==0) {
				numberOfPValues = Integer.parseInt(args[++i]);
				System.err.println("Number of p-values ="+numberOfPValues);
			} else if (args[i].compareToIgnoreCase("-type")==0) {
				type = Integer.parseInt(args[++i]);
				if (type==0)
					System.err.println("Input Data Type is 0 (ASCII input Float data)");
				else if (type==1)
					System.err.println("Input Data Type is 1 (BINARY input FLoat data)");
				else 
					System.err.println("Input Data Type must be 0 (ASCII input Float data)"
							+" or 1 (BINARY input Float data)");
			} else if (args[i].compareToIgnoreCase("-fdr_rate")==0) {
				fdr_rate = Double.parseDouble(args[++i]);
				System.err.println("fdr_rate ="+fdr_rate);
			} else if (args[i].compareToIgnoreCase("-byteorder")==0) { 
				byteorder = args[++i];
				System.out.println("byteorder="+byteorder);
			} else if (args[i].startsWith("-h")) {
				reportUsage();
				System.exit(1);
			} else if (args[i].compareToIgnoreCase("-mask")==0) {
				if (type==1) {		// Binary Volume
					maskInputVolume = args[++i];
					System.out.println("maskInputVolume="+maskInputVolume);
				}
				else if (type==0) {	// ASCII shape
					maskInputShape = args[++i];
					System.out.println("maskInputShape="+maskInputShape);
				}
			}
		}
		
		if (type==0) { 
			readAsciiData();
			if (maskInputShape!=null) {
				readAsciiMaskShape();
				// Extract the data under the mask!
	        	// p_values * mask -> MaskedPvalues --FDR--> thresholdedPMap -> FuseWithMask -> Results
			}
        	maskPValues();
		}
		else if (type==1) { 
			readBinaryData();
	        if (maskInputVolume!=null) {
	        	readBinaryMaskVolume();       	
				// Extract the data under the mask!
	        	// p_values * mask -> MaskedPvalues --FDR--> thresholdedPMap -> FuseWithMask -> Results
	        }
        	maskPValues();
		}
		else {
			System.out.println("Incorrect data type!\n Enter correct type <-type ZERO4ASCII_ONE4BINARY>");
			System.exit(1);
		}
		
		Test_FDR t = new Test_FDR();
		
		fuseFDRResultsWithMask();	// Fuse the thresholded (FDR-corrected) p-values with initial mask
		
		if (verbose)  {
			System.out.println("Listing of all Raw-Pvalues (Left) and FDR Thresholded-Pvalues (Right)\n");
			for (i=0; i<resultsPValues.length; i++)
				System.out.println("Index="+i+"\t Raw="+p_values[i]+"\t FDR_CorrPValue="+resultsPValues[i]);
			System.out.println(")\nDone!");
		}
			
		if (type==0) { 
			writeAsciiData();
			// writes thresholdedPMap array as ASCII file
		}
		else if (type==1) { 
			writeBinaryData();
			// writes thresholdedPMap array as Binary File
		}
	}
	
	/**
	* readAsciiData() method
	* Reads data from an ASCII text file (one number, probability-value, per line)
	*/
	static public void readAsciiData() {
		String line = null, stringTmp=null;
		StringTokenizer st;
		Vector<Double> inputData = new Vector<Double>();	
		
		try {
			BufferedReader bReader  = new BufferedReader(new FileReader(inputVolume));
			while ( (line = bReader.readLine()) != null) {
				st = new StringTokenizer(line, ",; \t" );
				while (st.hasMoreElements()) {
					stringTmp = st.nextToken().trim();
					inputData.addElement(Double.valueOf(stringTmp));
				}
			}
		} catch (Exception e) {
			System.out.println("Error: "+e);
		}
		
		p_values = new double[inputData.size()];
		for (int i=0; i < inputData.size(); i++) {
			p_values[i] = inputData.get(i);
			if (p_values[i]==0 || Float.isNaN((float)p_values[i]) || Float.isInfinite((float)p_values[i]))
				p_values[i] = 1.0;
			//System.err.println("p_values["+i+"]="+p_values[i]);
		}
		
		//System.err.println("p_values.length="+p_values.length);
		// Check correct number of Inout data (p-values)
		if (p_values.length != numberOfPValues)
			System.out.println("WARNING 1: numberOfPValues declared to be: "+numberOfPValues+
					"...\n Actual number of data read in from file ("+inputVolume+
					") is "+ p_values.length);
	}

	/**
	* readAsciiMaskShape() method
	* Reads shape mask from an ASCII text file (one number, probability-value, per line)
	*/
	static public void readAsciiMaskShape() {
		String line = null, stringTmp=null;
		StringTokenizer st;
		Vector<Double> inputData = new Vector<Double>();	
		
		maskShapeBoolean = new boolean [numberOfPValues];
		int counter = 0;
		
		try {
			BufferedReader bReader  = new BufferedReader(new FileReader(maskInputShape));
			while ( (line = bReader.readLine()) != null) {
				st = new StringTokenizer(line, ",; \t" );
				while (st.hasMoreElements()) {
					stringTmp = st.nextToken().trim();
					if (Double.valueOf(stringTmp)>0) 
						maskShapeBoolean[counter]=true;
					else maskShapeBoolean[counter]=false;
					counter++;
				}
			}
		} catch (Exception e) {
			System.out.println("Error: "+e);
		}
		
		// Check correct number of Input data (p-values)
		if (counter != numberOfPValues)
			System.out.println("WARNING 2: numberOfPValues declared to be: "+numberOfPValues+
					"...\n Actual number of data read in from file ("+inputVolume+
					") is "+ counter);
	}

	/**
	* writeAsciiData() method
	* Writes results to an ASCII text file (one number, probability-value, per line)
	*/
	static public void writeAsciiData() {
		try {
			BufferedWriter bWriter  = new BufferedWriter(new FileWriter(outputVolume));
			//for (int i=0; i<thresholdedPMap.length; i++)
				//bWriter.write(Double.toString(thresholdedPMap[i])+"\n"); resultsPValues
			for (int i=0; i<resultsPValues.length; i++)
				bWriter.write(Double.toString(resultsPValues[i])+"\n");
			bWriter.flush();
			bWriter.close();
		} catch (Exception e) {
			System.out.println("Error: "+e);
		}
	}

	/**
	* readBinaryData() method
	* Reads data from a Binary file (4-byte floats)
	*/
	static public void readBinaryData() {
		p_values = new double[numberOfPValues];
		
		FileInputStream fis;
		BufferedInputStream bis;
		
		try { 
			fis = new FileInputStream(inputVolume);
			bis = new BufferedInputStream(fis);
			MemoryCacheImageInputStream mciis = new MemoryCacheImageInputStream(bis);
			//mciis.setByteOrder(java.nio.ByteOrder.nativeOrder());
			setByteOrder(mciis, byteorder);
			
			p_values = new double[numberOfPValues];
			for (int i=0; i < p_values.length; i++) {
				p_values[i] = mciis.readFloat();
				if (p_values[i]==0 || Float.isNaN((float)p_values[i]) || Float.isInfinite((float)p_values[i]))
					p_values[i] = 1.0;
			}
			System.out.println("Finished Reading Binary Input\n"
					+"p_values[0]="+p_values[0]
					+"\t p_values["+(p_values.length/2)+"]="
					+p_values[p_values.length/2]);
			
			/***
			 * One thing to note is that this method seems to keep in memory all the data it has read from disk.  
			 * So I would think that you would have to call:
			 * mciis.flushBefore(mciis.getStreamPosition());
			 */

		} catch (Exception e) {
			System.out.println("Error in readBinaryData(): "+e);
		}
		
		return;
	}
	
	/**
	* readBinaryMaskVolume() method
	* Reads volume-mask from a Binary file (1-byte character array)
	*/
	static public void readBinaryMaskVolume() {
		maskVolumeBoolean = new boolean [numberOfPValues];
		FileInputStream fis;
		BufferedInputStream bis;
		
		try { 
			fis = new FileInputStream(maskInputVolume);
			bis = new BufferedInputStream(fis);
			MemoryCacheImageInputStream mciis = new MemoryCacheImageInputStream(bis);
			setByteOrder(mciis, byteorder);
			
			for (int i=0; i < numberOfPValues; i++) {
				if (mciis.readUnsignedByte()>0) maskVolumeBoolean[i]=true;
				else maskVolumeBoolean[i]=false;
			}
			
		} catch (Exception e) {
			System.out.println("Error in readBinaryData(): "+e);
		}
		return;
	}
	
	/**
	* writeBinaryData() method
	* Writes results to a Binary file (4-byte float)
	*/
	static public void writeBinaryData() {
		FileOutputStream fos;
		BufferedOutputStream bos;
		
		try { 
			fos = new FileOutputStream(outputVolume);
			bos = new BufferedOutputStream(fos);
			MemoryCacheImageOutputStream mcios = new MemoryCacheImageOutputStream(bos);
			// mcios.setByteOrder(java.nio.ByteOrder.LITTLE_ENDIAN);
			setByteOrder(mcios, byteorder);
			
			//for (int i=0; i < p_values.length; i++) 
				//mcios.writeFloat((float)thresholdedPMap[i]);
			for (int i=0; i < resultsPValues.length; i++) 
				mcios.writeFloat((float)resultsPValues[i]);
				
			System.out.println("Finished Writing out Binary Output\n"
					+"resultsPValues[0]="+resultsPValues[0]
					+"\t resultsPValues["+(resultsPValues.length/2)+"]="
					+resultsPValues[resultsPValues.length/2]);
			mcios.flush();
			mcios.close();
			bos.flush();
			bos.close();
			fos.flush();
			fos.close();
			
		} catch (Exception e) {
			System.out.println("Error in writeBinaryData(): "+e);
		}
		
		return;
	}

	/**
	* maskPValues() method
	* This method extracts only the pValues that are under the mask (shrinks the pvalues array)
	* Extract the data under the mask!
	* p_values * mask -> MaskedPvalues --FDR--> thresholdedPMap -> FuseWithMask -> Results
	*/
	static public void maskPValues() {
		// maskedPValues_temp is a temp array to count how many p-values are 
		// really in the masked raw p-value array
		double[] maskedPValues_temp = new double [p_values.length];
		int counter = 0;
		
		if (type==0) {	// ASCII Mask
			if (maskInputShape!=null) {	// mask is provided
				for (int i=0; i<p_values.length; i++) {
					if (maskShapeBoolean[i]) {
						maskedPValues_temp[counter] = p_values[i];
						counter++;
					}
				}
			} else {						// NO MASK!
				counter = p_values.length;
				for (int i=0; i<counter; i++) maskedPValues_temp[i] = p_values[i];
			}
		} else if (type==1){	// Type =1 Volume
			if (maskInputVolume!=null) {	// mask is provided
				for (int i=0; i<p_values.length; i++) {
					if (maskVolumeBoolean[i]) {
						maskedPValues_temp[counter] = p_values[i];
						counter++;
					}
				}
			} else {						// NO MASK!
				counter = p_values.length;
				for (int i=0; i<counter; i++) maskedPValues_temp[i] = p_values[i];
			}
		}
		
		System.out.println("Masked-P-Values Array Size is: "+ counter+"\n");
		maskedPValues = new double [counter];
		for (int i=0; i<counter; i++) maskedPValues[i] = maskedPValues_temp[i];
	}

	/**
	* fuseFDRResultsWithMask() method
	* This method fuses back the FDR-corrected pValues with the mask (
	* to produce an output of the same dimension as the initial array of p-values
	* p_values * mask -> MaskedPvalues --FDR--> thresholdedPMap -> FuseWithMask -> Results
	*/
	static public void fuseFDRResultsWithMask() {
		resultsPValues = new double [p_values.length];
		int counter = 0;
		
		if (type==0) {	// ASCII Mask
			if (maskInputShape!=null) {	// mask is provided
				for (int i=0; i<p_values.length; i++) {
					if (maskShapeBoolean[i]) { // if the mask is 1 ==> set result to thresholdedPMap[counter]
						resultsPValues[i] = thresholdedPMap[counter];
						counter++;
					} else resultsPValues[i] = 1.0; // If mask is zero ==> set result to 1.0
				}
			} else {						// NO MASK!
				for (int i=0; i<thresholdedPMap.length; i++) 
					resultsPValues[i] = thresholdedPMap[i];
			}
		} else if (type==1){	// Type =1 Volume
			if (maskInputVolume!=null) {	// mask is provided
				for (int i=0; i<p_values.length; i++) {
					if (maskVolumeBoolean[i]) { // if the mask is 1 ==> set result to thresholdedPMap[counter]
						resultsPValues[i] = thresholdedPMap[counter];
						counter++;
					} else resultsPValues[i] = 1.0; 
					// if the mask is 1 ==> set result to thresholdedPMap[counter]
				}
			} else {						// NO MASK!
				for (int i=0; i<thresholdedPMap.length; i++) 
					resultsPValues[i] = thresholdedPMap[i];
			}
		}
	}

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

}
