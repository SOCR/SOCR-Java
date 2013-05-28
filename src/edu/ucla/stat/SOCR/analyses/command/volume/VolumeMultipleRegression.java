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
 * This class provides an image-volume based MLR analysis for 1D, 2D and 3D imaging volumes, based
 * on user specified regressors/predictors. These are provided as a Tab-Separated input
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
 * 			edu.ucla.stat.SOCR.analyses.command.volume.VolumeMultipleRegression
 * 			-dm DesignMatrix.txt -h -regressors [name1,name2,...,name_k]
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
 * 			-regressors [name1,name2,...,name_k]: specify which columns/variables should be used 
 * 						as regressors/covariates. Note on INTERACTION:
 * 						For interactions, each interaction variable must be composed of individual
 * 						regressor variables that are also included in the regressor list; 
 * 						for instance, if we have an interaction variable A*B*C,
 * 						we must have (minimally): -regressors A,B,C,A*B,B*C,A*C,A*B*C
 * 						See; http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression
 * 			-dim Zmax Ymax XMax: 	specify the dimension-sizes (for 2D images use ZMax=1, for 1D, Zmax=Y_Max=1
 *
 * 			http://en.wikipedia.org/wiki/Partial_correlation
 * 			-intercept [Base_Filename] Base Filename for the 4 Intercept Estimates.
 * 									This base filename will be appended with:
 * 									_Intercept_Pvalue.img
 *									_Intercept_Beta.img
 *									_Intercept_RPartCorr.img
 *									_Intercept_TStat.img
 *			-i Base_Filename]		Identical to -intercept [Base_Filename]
 * 			-p [Pvalue_Filename]: 	output the p-value volume
 * 			-b [B_Filename]:		output the effect-sizes/betas
 * 			-r [Rvalue_Filename]:	output the partial-correlation volume
 * 			-t [Tstat_Filename]:	output the T-Statistics for the effect-size Beta
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
 * 	http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression
 *
 */
public class VolumeMultipleRegression {

    private static final String MISSING_MARK = ".";
    static FileInputStream[] fi;
    static BufferedInputStream[] bi;
    static MemoryCacheImageInputStream[] mciis;
    static FileOutputStream[] iFOS, pFOS, bFOS, rFOS, tFOS;
    static BufferedOutputStream[] iBOS, pBOS, bBOS, rBOS, tBOS;
    static MemoryCacheImageOutputStream[] iMciis, pMciis, bMciis, rMciis, tMciis;
    static boolean byteswap = false;
    static String byteorder = "system";
    //static String[] regressors;
    static ArrayList<Regressor> regressorList;
    static boolean outputInterceptVolumes = false;
    static boolean outputPValuesVolumes = false;
    static boolean outputBEffectSizeVolumes = false;
    static boolean outputRPartialCorrVolumes = false;
    static boolean outputTStatVolumes = false;
    // First the Constant term (separately)
    static double[] constantIntercept_P_B_R_T; //4 Intercept values: pValue, Beta, R_PartCorr and TStat
    static double[] pValue;
    static double[] beta;
    static double[] rPartCor;
    static double[] tStat;
    static int bufferSize = 8192;		// This contains the size of the Input/Output Buffer

    public static void main(String[] args) {
        //FileHelper fileHelper1 = new FileHelper();
        //fileHelper.openReader("data1.txt");
        String designMatrixInputFile = null; //"C:\\STAT\\SOCR\\test\\dataOne.txt\\"
        String maskInputVolume = null;		// mask input volume

        String intercept_Filename = null;
        String pValue_Filename = null;
        String bValue_Filename = null;
        String rValue_Filename = null;
        String tStat_Filename = null;

//		regressors = new String[1];
//			regressors[0]="";
        regressorList = new ArrayList<Regressor>();

        HashMap<String, Integer> regressorColumnIndices;				// These indices contain the column-indices of the user-specified regressor columns

        int[] dim = new int[3];		// the sizes of the 3D volume dimensions

        boolean[][][] maskVolumeBoolean = new boolean[1][1][1];			// [Zmax][Ymax][Xmax]
        maskVolumeBoolean[0][0][0] = false;

        int data_type = 4;							//image intensities data type

//		ArrayList<String> inputAnalyzeFilenames = new ArrayList<String>();
        // The names of the Analyze Input Files
        // stored in column 2 of the Design-Matrix
        String regressorString = "";

        boolean header = false;
        boolean filesLoaded = false;

        int i, j, k;

        System.out.println(
                "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression");

        try {
            for (i = 0; i < args.length; i++) {
                if (args[i].compareToIgnoreCase("-h") == 0) {
                    header = true;
                } else if (args[i].compareToIgnoreCase("-data_type") == 0) {
                    data_type = Integer.parseInt(args[++i]);
                    if (data_type != 0 && data_type != 1 && data_type != 2 && data_type != 3 && data_type != 4) {
                        System.err.println("Data Type must be 0(unsigned byte volume), 1(signed byte volume), "
                                + "2(unsigned short int) 3(signed short int) or 4(float volume)!\n");
                        System.exit(1);
                    }
                    System.out.println("Data Type=" + data_type);
                } else if (args[i].compareToIgnoreCase("-dim") == 0) {
                    dim[2] = Integer.parseInt(args[++i]);
                    dim[1] = Integer.parseInt(args[++i]);
                    dim[0] = Integer.parseInt(args[++i]);
                    // Sept 10, 2009, commented out this line to fix a problem with odd dimension sizes
                    // which generated a buffer-flush problem in this method: getNextInputDataBuffer()
                    bufferSize = dim[0] * dim[1];
                    System.out.println("dim[2]=" + dim[2] + "\tdim[1]=" + dim[1] + "\tdim[0]=" + dim[0]
                            + "\tbufferSize=" + bufferSize);
                } else if (args[i].compareToIgnoreCase("-intercept") == 0
                        || args[i].compareToIgnoreCase("-i") == 0) {
                    outputInterceptVolumes = true;
                    intercept_Filename = args[++i];
                    System.out.println("Intercept_Base_Filename=" + intercept_Filename);
                } else if (args[i].compareToIgnoreCase("-p") == 0) {
                    outputPValuesVolumes = true;
                    pValue_Filename = args[++i];
                    System.out.println("pValue_Filename=" + pValue_Filename);
                } else if (args[i].compareToIgnoreCase("-b") == 0) {
                    outputBEffectSizeVolumes = true;
                    bValue_Filename = args[++i];
                    System.out.println("BValue_Filename=" + bValue_Filename);
                } else if (args[i].compareToIgnoreCase("-r") == 0) {
                    outputRPartialCorrVolumes = true;
                    rValue_Filename = args[++i];
                    System.out.println("rValue_Filename=" + rValue_Filename);
                } else if (args[i].compareToIgnoreCase("-t") == 0) {
                    outputTStatVolumes = true;
                    tStat_Filename = args[++i];
                    System.out.println("tStat_Filename=" + tStat_Filename);
                } else if (args[i].compareToIgnoreCase("-byteswap") == 0) {
                    byteswap = true;
                    System.out.println("Byteswap=" + byteswap);
                } else if (args[i].compareToIgnoreCase("-byteorder") == 0) {
                    byteorder = args[++i];
                    System.out.println("byteorder=" + byteorder);
                } else if (args[i].compareToIgnoreCase("-regressors") == 0) {
                    regressorString = args[++i];
                    System.out.println("regressorString=" + regressorString);
                    // Parse the regressors list
                    StringTokenizer Tok = new StringTokenizer(regressorString, ",");
                    if (!parseRegressors(Tok)) {
                        System.err.println("The regressor list is invalid; make sure that the component variables\nof each interaction are also included as regressors");
                        System.exit(1);
                    }
                } else if (args[i].compareToIgnoreCase("-dm") == 0) {
                    designMatrixInputFile = args[++i];
                    filesLoaded = true;
                    System.out.println("designMatrixInputFile=" + designMatrixInputFile);
                } else if (args[i].compareToIgnoreCase("-help") == 0) {
                    System.out.println("Please see the Web syntax for involking this tool: \n"
                            + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression");
                    return;
                } else if (args[i].compareToIgnoreCase("-mask") == 0) {
                    maskInputVolume = args[++i];
                    System.out.println("maskInputVolume=" + maskInputVolume);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Incorrect call 1! Please see the syntax for invoking this tool\n"
                    + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression");
        }

        if (!filesLoaded) {
            System.err.println("Incorrect call 2! Please see the syntax for involking this tool\n"
                    + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression");
            return;
        }

        // stores the number of regressors
        int independentLength = regressorList.size();
//                System.out.println("independentLength = " + independentLength);

        if (independentLength < 1) {
            System.err.println("No Independent Variable Specified (independentLength=" + independentLength
                    + ")\n Cannot run the regression\n"
                    + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression");
            return;
        }

        MultiLinearRegressionResult result = null;

        StringTokenizer st = null;

        int designMatrixNumberColumns = 1000;	// Maximum number of DesignMatrix columns

        String[] input = new String[independentLength];

        // stores total number of subjects in the design matrix
        int xLength = 0;

        // stores actual number of subjects (after subjects with missing values have been removed)
        int actualLength = 0;

//		String[] varHeader = new String[1];
        regressorColumnIndices = new HashMap<String, Integer>();

        ArrayList<Double>[] xList = new ArrayList[independentLength];
        double[][] xDataArray = new double[independentLength][xLength];

        for (i = 0; i < xList.length; i++) {
            xList[i] = new ArrayList<Double>();
        }

        String line = null, stringTmp;

        boolean read = true;
        if (header) {
            read = false;
        }

        ArrayList<String>[] designMatrix = null;

        int row = 0;

        // keep track of subjects that have missing values; should be excluded from regression analysis
        ArrayList<Integer> subjectsToExclude = new ArrayList<Integer>();

        // read in values from design matrix
        try {
            BufferedReader bReader = new BufferedReader(new FileReader(designMatrixInputFile));
            boolean exclude = false;
            while ((line = bReader.readLine()) != null) {
                exclude = false;
//                System.out.println("line = " + line);
                st = new StringTokenizer(line, ",; \t");

                if (regressorColumnIndices.isEmpty() && header) {		// Read Header Row
                    int n = st.countTokens();

                    designMatrixNumberColumns = n;
//                    System.out.println("Initializing designMatrix data structure");
                    designMatrix = new ArrayList[n];
                    System.out.println("Reading DM Header!\t designMatrixNumberColumns="
                            + designMatrixNumberColumns);

                    st = new StringTokenizer(line, ",; \t");
                    // extract the column headers from the design matrix
//                        varHeader = new String[n];
                    String dmHeader = "";
                    for (k = 0; k < designMatrixNumberColumns; k++) {
                        dmHeader = st.nextToken().trim();
//                            varHeader[k] = dmHeader;
                        regressorColumnIndices.put(dmHeader, k);
                    }

                    // record which column in the design matrix each regressor corresponds to
//                        for (j = 0; j < independentLength; j++) {
//                            for (k = 0; k < designMatrixNumberColumns; k++) {
//                                if (regressorList.get(j).getRegressorString().compareToIgnoreCase(varHeader[k]) == 0) {
//                                    System.err.println("varHeader[" + k + "]=" + varHeader[k]
//                                            + "\t regressorColumnIndices[" + j + "] =" + k);
//                                    break;      // only consider first column that matches
//                                }
//                            }
//                        }

                    // Include a test to make sure this array is defined, as if there is
                    // no variable match in the DM, then there is no need for any analysis.
                    for (j = 0; j < independentLength; j++) {
                        if (regressorColumnIndices.size() < 2) {
                            System.err.println("\nNo variable (" + regressorList.get(j).getRegressorString() + ") match!!!\n");
                            System.exit(1);
                        }
                    }

                    read = true;
                } else if (!header) {		// Must have a header row to deduce the predictors
                    System.err.println("No Independent Variable Specified 1. Cannot run the regression\n"
                            + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression");
                    return;
                } else {
                    // READ DATA From Design-Matrix
                    if (designMatrix == null) {
                        System.err.println("The design matrix has not been initialized; this means the header line was not read in");
                    }

                    int column = 0;
                    while (st.hasMoreTokens()) {
                        if (designMatrix[column] == null) {
                            designMatrix[column] = new ArrayList<String>();
                        }
                        String tmp = st.nextToken();
                        designMatrix[column].add(tmp);
                        column++;
                    }

                    int regCounter = 0;
                    boolean missing;
                    double[] values = new double[independentLength];
                    for (Regressor regressor : regressorList) {
                        missing = false;
                        if (regressor.getComponentCount() > 1) {
                            double product = 1.0;
                            for (String str : regressor.getComponents()) {
                                // check for missing value
                                if (str.equalsIgnoreCase(MISSING_MARK)){
                                    missing = true;
                                    break;
                                } else {
                                    int index = regressorColumnIndices.get(str);
                                    ArrayList<String> colVals = designMatrix[index];
                                    String strVal = colVals.get(row);
                                    double doubleVal = Double.parseDouble(strVal);
                                    product = product * doubleVal;
                                }
                            }
                            values[regCounter] = product;
                        } else {
                            String firstComponent = regressor.getComponents().get(0);
                            int index = regressorColumnIndices.get(firstComponent);
                            ArrayList<String> colVals = designMatrix[index];
                            String strVal = colVals.get(row);
                            if (strVal.equalsIgnoreCase(MISSING_MARK)) {
                                missing = true;
                            } else {
                                double doubleVal = Double.parseDouble(strVal);
                                values[regCounter] = doubleVal;
                            }
                        }
                        if (missing){
                            exclude = true;
                            break;
                        }
                        regCounter++;
                    }
                    if (exclude){
                        subjectsToExclude.add(row);
                    }else{
                        for (int reg = 0; reg < independentLength; reg++){
                            xList[reg].add(values[reg]);
                        }
                    }
                    row++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//            int subjectCount = row;


//                if (n == regressorColumnIndices[m]) {
//                    input[m] = stringTmp;
//                    if (read && !input[m].equalsIgnoreCase(MISSING_MARK)) {
//                        xList[m].add(input[m]);
//                    }
//                    System.out.println("Reading DM: column=" + n
//                            + "\t inputPredictorValue[m=" + m + ";variable="
//                            + regressors[m] + "]=" + input[m]);
//                }


        if (!header) {
            System.err.println("No Independent Variable Specified in Design-Matrix2. Cannot run the regression\n"
                    + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression");
            return;
        }

        xLength = designMatrix[1].size();
        actualLength = xLength - subjectsToExclude.size();
        System.out.println("Total number of volumes in design matrix = " + xLength);
        System.out.println("Actual number of volumes after missing values have been removed = " + actualLength);

        // xData array will store the predictor values
        double[] xData = null;
        //String [] xData = null;

        // yData array will store the voxel intensity data
        double[] yData = new double[actualLength];
        // Open Volume Files
        //java.nio.ByteOrder.nativeOrder();

        //create streams of the Analyze Image files
        fi = new FileInputStream[actualLength];
        bi = new BufferedInputStream[actualLength];
        mciis = new MemoryCacheImageInputStream[actualLength];

        // open input streams from each of the Analyze image files in the Design Matrix
        int index = 0;
        for (i = 0; i < xLength; i++) {
            try {
                if (!subjectsToExclude.contains(i)) {
                    System.out.println("designMatrix[1].get(" + i + ") = " + designMatrix[1].get(i));
                    fi[index] = new FileInputStream(designMatrix[1].get(i));
                    bi[index] = new BufferedInputStream(fi[index]);
                    mciis[index] = new MemoryCacheImageInputStream(bi[index]);
                    setByteOrder(mciis[index], byteorder);
                    index++;
                }
            } catch (FileNotFoundException e) {
                System.err.println(e);
            }
        }
        System.out.println("Done opening INPUT volume streams!");

        // read the mask volume (if there is one) into a three-dimensional boolean array
        if (maskInputVolume != null) {
            try {
                maskVolumeBoolean = new boolean[dim[2]][dim[1]][dim[0]];
                FileInputStream fis = new FileInputStream(maskInputVolume);
                BufferedInputStream bis = new BufferedInputStream(fis);
                MemoryCacheImageInputStream dis = new MemoryCacheImageInputStream(bis);
                setByteOrder(dis, byteorder);

                int tmpByte;
                for (i = 0; i < dim[2]; i++) {
                    for (j = 0; j < dim[1]; j++) {
                        for (k = 0; k < dim[0]; k++) {
                            try {
                                tmpByte = dis.readUnsignedByte(); //readUnsignedByte()
                                if (tmpByte > 0) {
                                    maskVolumeBoolean[i][j][k] = true;
                                } else {
                                    maskVolumeBoolean[i][j][k] = false;
                                }
                            } catch (IOException e) {
                                maskVolumeBoolean[i][j][k] = false;
                                System.err.println("Exception: maskVolumeBoolean[" + i + "][" + j + "][" + k + "] = false\n");
                            }
                        }
                    }
                }
                dis.close();
                bis.close();
                fis.close();
            } catch (IOException e) {
                System.err.println("Exception: Can't open the mask input volume: " + maskInputVolume);
            }
        }

        Data data;
        constantIntercept_P_B_R_T = new double[4];	//4 Intercept values: pValue, Beta, R_PartCorr and TStat
        pValue = new double[independentLength];
        beta = new double[independentLength];
        rPartCor = new double[independentLength];
        tStat = new double[independentLength];

        // Create File Output Streams
        try {
            iFOS = new FileOutputStream[4]; // intercept
            iBOS = new BufferedOutputStream[4];
            iMciis = new MemoryCacheImageOutputStream[4];

            pFOS = new FileOutputStream[independentLength];
            pBOS = new BufferedOutputStream[independentLength];
            pMciis = new MemoryCacheImageOutputStream[independentLength];

            bFOS = new FileOutputStream[independentLength];
            bBOS = new BufferedOutputStream[independentLength];
            bMciis = new MemoryCacheImageOutputStream[independentLength];

            rFOS = new FileOutputStream[independentLength];
            rBOS = new BufferedOutputStream[independentLength];
            rMciis = new MemoryCacheImageOutputStream[independentLength];

            tFOS = new FileOutputStream[independentLength];
            tBOS = new BufferedOutputStream[independentLength];
            tMciis = new MemoryCacheImageOutputStream[independentLength];

            // intercept P_B_R_T; //4 Intercept values: pValue, Beta, R_PartCorr and TStat
            if (outputInterceptVolumes == true) {
                iFOS[0] = new FileOutputStream(intercept_Filename + "_Intercept_Pvalue" + ".img");
                iBOS[0] = new BufferedOutputStream(iFOS[0], bufferSize);
                iMciis[0] = new MemoryCacheImageOutputStream(iBOS[0]);
                setByteOrder(iMciis[0], byteorder);

                iFOS[1] = new FileOutputStream(intercept_Filename + "_Intercept_Beta" + ".img");
                iBOS[1] = new BufferedOutputStream(iFOS[1], bufferSize);
                iMciis[1] = new MemoryCacheImageOutputStream(iBOS[1]);
                setByteOrder(iMciis[1], byteorder);

                iFOS[2] = new FileOutputStream(intercept_Filename + "_Intercept_RPartCorr" + ".img");
                iBOS[2] = new BufferedOutputStream(iFOS[2], bufferSize);
                iMciis[2] = new MemoryCacheImageOutputStream(iBOS[2]);
                setByteOrder(iMciis[2], byteorder);

                iFOS[3] = new FileOutputStream(intercept_Filename + "_Intercept_TStat" + ".img");
                iBOS[3] = new BufferedOutputStream(iFOS[3], bufferSize);
                iMciis[3] = new MemoryCacheImageOutputStream(iBOS[3]);
                setByteOrder(iMciis[3], byteorder);
            }

            for (int regLength = 0; regLength < independentLength; regLength++) {
                if (outputPValuesVolumes == true) {
                    pFOS[regLength] = new FileOutputStream(pValue_Filename
                            + "_Reg_" + regressorList.get(regLength).getRegressorString().replace('*','x') + ".img");
                    pBOS[regLength] = new BufferedOutputStream(pFOS[regLength], bufferSize);
                    pMciis[regLength] = new MemoryCacheImageOutputStream(pBOS[regLength]);
                    setByteOrder(pMciis[regLength], byteorder);
                }

                if (outputBEffectSizeVolumes == true) {
                    bFOS[regLength] = new FileOutputStream(bValue_Filename
                            + "_Reg_" + regressorList.get(regLength).getRegressorString().replace('*','x') + ".img");
                    bBOS[regLength] = new BufferedOutputStream(bFOS[regLength], bufferSize);
                    bMciis[regLength] = new MemoryCacheImageOutputStream(bBOS[regLength]);
                    setByteOrder(bMciis[regLength], byteorder);
                }

                if (outputRPartialCorrVolumes == true) {
                    rFOS[regLength] = new FileOutputStream(rValue_Filename
                            + "_Reg_" + regressorList.get(regLength).getRegressorString().replace('*','x') + ".img");
                    rBOS[regLength] = new BufferedOutputStream(rFOS[regLength], bufferSize);
                    rMciis[regLength] = new MemoryCacheImageOutputStream(rBOS[regLength]);
                    setByteOrder(rMciis[regLength], byteorder);
                }

                if (outputTStatVolumes) {
                    tFOS[regLength] = new FileOutputStream(tStat_Filename
                            + "_TStat_" + regressorList.get(regLength).getRegressorString().replace('*','x') + ".img");
                    tBOS[regLength] = new BufferedOutputStream(tFOS[regLength], bufferSize);
                    tMciis[regLength] = new MemoryCacheImageOutputStream(tBOS[regLength]);
                    setByteOrder(tMciis[regLength], byteorder);
                }
            }

            System.out.println("Done opening OUTPUT result streams!");
            System.out.println("Beginning the stat analyses ... ");

            boolean writeResultsOrDefaults = true; // true==results; false=Default
            int counter = 0;	// integer counter of the voxel index

            boolean flushCounter = false;

            bufferSize = (10 * bufferSize) / actualLength;

            for (i = 0; i < dim[2]; i++) {
                for (j = 0; j < dim[1]; j++) {
                    for (k = 0; k < dim[0]; k++) {
                        counter++;
                        if ((counter % (bufferSize)) == 0) {
                            System.err.print(".");
                            flushCounter = true;
                            //System.err.println("counter="+counter);
                        } else {
                            flushCounter = false;
                        }

                        result = null;

                        // if NO mask is provided OR (mask exists and (k,j,i) xoxel is inside the mask)
                        if (maskInputVolume == null || (maskInputVolume != null && maskVolumeBoolean[i][j][k] == true)) {
                            data = new Data();
                            //System.err.println("Before Reading X and Y data, counter="+counter);
                            //Get Y-data
//                            if ((counter % bufferSize) == 0)
//                                System.err.println("After Reading X and Y data, counter="+counter);
                            
                            yData = getNextInputDataBuffer(data_type, actualLength, flushCounter);
                            

                            //if (counter%100==0)
                            //System.err.println("Inside Mask: Counter="+counter+"\tbufferSize="+bufferSize+
                            //"\tcounter%(100*bufferSize)="+(counter%(100*bufferSize)));
                            // flushCounter++;

                            data.appendY("ImagingIntensity", yData, DataType.QUANTITATIVE);
                            //System.err.println("Done Reading Y data, counter="+counter);

                            //Get X-data
                            //System.out.println("Input Data Check:");
                            for (int regLength = 0; regLength < independentLength; regLength++) {
                                xData = new double[actualLength];

                                for (int fileList = 0; fileList < actualLength; fileList++) {
                                    try {
                                        xData[fileList] = xList[regLength].get(fileList);
                                    } catch (NumberFormatException e) {
                                        System.err.print(e);
                                        return;
                                    }
                                }
                                xDataArray[regLength] = xData;
                                // OLD data.appendX(varHeader[regressorColumnIndices[regLength]], xData, DataType.QUANTITATIVE);
                                // TEST IVO 08/20/09!!!!!!!!!!!!!!!!!!!!
//                                System.out.println("regressorList.get("+regLength+").getRegressorString() = " + regressorList.get(regLength).getRegressorString());
                                data.appendX(regressorList.get(regLength).getRegressorString(), xData, DataType.QUANTITATIVE);
                                /** System.out.println("Are these the same strings????\n"+
                                "regressors["+regLength+"]="+regressors[regLength]+
                                " ?==? varHeader["+regressorColumnIndices[regLength]+
                                "]="+varHeader[regressorColumnIndices[regLength]]);
                                 **/
                            }

                            //System.err.println("Done Reading X data, counter="+counter);

                            /**** Check Input!!!! ****/
//							try {
//								if (k==85 && j==173 && i==110) {
//									System.out.println("Checking INPUT ...");
//									for (int regLength = 0; regLength < independentLength; regLength++) {
//										System.out.print("(85,173,110) X_Data ["+regLength+"] = {");
//										for (int ind = 0; ind < xLength; ind++)
//											System.out.print(xDataArray[regLength][ind]+", ");
//										System.out.print("\n");
//									}
//									System.out.print("} \n (85,173,110) Y_Data_Image_Data = {");
//									for (int ind = 0; ind < yData.length; ind++)
//										System.out.print(yData[ind]+", ");
//									System.out.print("} \n!!!!!!!!!!!!!!!!!!!!!!!!");
//								}
//								else if (k==110 && j==110 && i==110) {
//									System.out.println("Checking INPUT ...");
//									for (int regLength = 0; regLength < independentLength; regLength++) {
//										System.out.print("(110,110,100) X_Data ["+regLength+"] = {");
//										for (int ind = 0; ind < xLength; ind++)
//											System.out.print(xDataArray[regLength][ind]+", ");
//										System.out.print("\n");
//									}
//									System.out.print("} \n (110,110,100) Y_Data_Image_Data = {");
//									for (int ind = 0; ind < yData.length; ind++)
//										System.out.print(yData[ind]+", ");
//									System.out.print("} \n!!!!!!!!!!!!!!!!!!!!!!!!");
//								}
//							} catch (Exception e) {
//								System.err.println("Cannot print debugging results for voxels: "+
//										"(85,173,110) and (110,110,100) ");
//							}
                            /****/
                            /************* Run Regression Analysis: X is 2, Y is 1 ************/
                            try {
                                result = (MultiLinearRegressionResult) data.getAnalysis(AnalysisType.MULTI_LINEAR_REGRESSION);
                            } catch (Exception e) {
                                System.err.println("Results Problem (6): " + e);
                            }
                            if (result == null) {
                                System.err.print("Results Problem (7)");
                                return;
                            }

                            //System.err.println("Done computing results, counter="+counter);
                            // The first "result" estimate-value and p-value are for the CONSTANT term.
                            //constantIntercept_P_B_R_T; //4 Intercept values: pValue, Beta, R_PartCorr and TStat
                            if (outputInterceptVolumes == true) {
                                constantIntercept_P_B_R_T[0] = result.getBetaPValue()[0];
                                constantIntercept_P_B_R_T[1] = result.getBeta()[0];

                                // Correlation between the constant mu intercept (beta) and the Y data
                                double[] staticInterceptConstant = new double[yData.length];
                                for (int i1 = 0; i1 < yData.length; i1++) {
                                    staticInterceptConstant[i1] = constantIntercept_P_B_R_T[1];
                                }
                                try {
                                    constantIntercept_P_B_R_T[2] =
                                            edu.ucla.stat.SOCR.util.AnalysisUtility.sampleCorrelation(staticInterceptConstant, yData);
                                } catch (edu.ucla.stat.SOCR.analyses.exception.DataIsEmptyException e) {
                                    System.err.println("Results Problem (7_0): " + e);
                                }
                                constantIntercept_P_B_R_T[3] = result.getBetaTStat()[0];
                            }

                            // Next, we report only estimates/effect-sizes and p-values for
                            // user-specified predictors/covariates only!
                            // independentLength should equal to beta.length
//                            System.out.println("independentLength = " + independentLength);
                            for (int regLength = 0; regLength < independentLength; regLength++) {
                                // Determine the CORRECT correspondence between regressor/predictor variables
                                // and the actual order of the reported MLR results
                                for (int regressorIndex = 0; regressorIndex <= independentLength; regressorIndex++) {
//                                    System.out.println("regressorList.get("+regLength+").getRegressorString() = " + regressorList.get(regLength).getRegressorString());
//                                    System.out.println("result.getVariableList()["+regressorIndex+"]) = " + result.getVariableList()[regressorIndex]);
                                    if (regressorList.get(regLength).getRegressorString().compareToIgnoreCase(
                                            result.getVariableList()[regressorIndex]) == 0) {

                                        pValue[regLength] = result.getBetaPValue()[regressorIndex];
                                        beta[regLength] = result.getBeta()[regressorIndex];
//                                        System.out.println("pValue["+regLength+"] = " + pValue[regLength]);
//                                        System.out.println("beta["+regLength+"] = " + beta[regLength]);


                                        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                        // See: http://en.wikipedia.org/wiki/Partial_correlation
                                        //rPartCor[regLength] = result.getBetaPValue()[regLength+1]/
                                        // result.getBetaSE()[regLength+1];
                                        try {
                                            rPartCor[regLength] =
                                                    edu.ucla.stat.SOCR.util.AnalysisUtility.sampleCorrelation(xDataArray[regLength], yData);
                                        } catch (edu.ucla.stat.SOCR.analyses.exception.DataIsEmptyException e) {
                                            System.err.println("Results Problem (7): " + e);
                                        }

                                        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                                        tStat[regLength] = result.getBetaTStat()[regressorIndex];

                                        /**** Check output ****/
//										if (k==85 && j==173 && i==110) {
//											System.out.println("Checking OUTPUT ...");
//											System.out.println("Voxel 1: (85,173,110)\t result.getVariableList()["+
//												(regressorIndex)+"]=" + result.getVariableList()[regressorIndex]+
//												"\tRegressors["+regLength+"]="+regressorList.get(regLength).getRegressorString()+
//												"\tBeta["+regLength+"]="+beta[regLength]+
//												"\t p-value["+regLength+"]="+pValue[regLength]+
//												"\t T-Stat["+regLength+"]="+tStat[regLength]+
//												"\t rPartCorr["+regLength+"]="+rPartCor[regLength]);
//										} else if (k==110 && j==110 && i==110) {
//											System.out.println("Checking OUTPUT ...");
//											System.out.println("Voxel 2: (110,110,110)\t result.getVariableList()["+
//												(regressorIndex)+"]=" + result.getVariableList()[regressorIndex]+
//												"\tRegressors["+regLength+"]="+regressorList.get(regLength).getRegressorString()+
//												"\tBeta["+regLength+"]="+beta[regLength]+
//												"\t p-value["+regLength+"]="+pValue[regLength]+
//												"\t T-Stat["+regLength+"]="+tStat[regLength]+
//												"\t rPartCorr["+regLength+"]="+rPartCor[regLength]);
//										} 	// end of else if
                                        break;
                                    }	// end of if (regressors[regressorIndex].compareToIgnoreCase(
                                }		// end of for (int regressorIndex = 0;
                            }			// end for (int regLength

                            // Write Results out
                            writeResultsOrDefaults = true;
                            if ((counter % bufferSize) == 0) {
                                putNextOutputDataBuffer(constantIntercept_P_B_R_T, pValue, beta, rPartCor, tStat,
                                        independentLength, writeResultsOrDefaults, true);
                            } else {
                                putNextOutputDataBuffer(constantIntercept_P_B_R_T, pValue, beta, rPartCor, tStat,
                                        independentLength, writeResultsOrDefaults, false);
                            }

                            //System.err.println("Done Writing out result, counter="+counter);

                        } // End of IF No-mask OR (mask exists AND (k,j,i) xoxel is inside the mask)
                        else { // if (maskInputVolume!=null && maskVolumeBoolean[i][j][k]==false) {
                            //if ((counter%(bufferSize))==0)
                            //System.err.println(" ... Outside the Mask Voxel counter(bufferSize="+bufferSize+")="+counter);
                            //if (counter%1000==0)
                            //System.err.println("\t Outside Mask: Counter="+counter+"\tbufferSize="+bufferSize+
                            //"\tcounter%(100*bufferSize)="+(counter%(100*bufferSize)));

                            if ((counter % bufferSize) == 0) {
                                skipNextInputDataBuffer(data_type, actualLength, flushCounter);
                                writeResultsOrDefaults = false;
                                putNextOutputDataBuffer(constantIntercept_P_B_R_T, pValue, beta, rPartCor, tStat,
                                        independentLength, writeResultsOrDefaults, true);
                            } else {
                                skipNextInputDataBuffer(data_type, actualLength, flushCounter);
                                writeResultsOrDefaults = false;
                                putNextOutputDataBuffer(constantIntercept_P_B_R_T, pValue, beta, rPartCor, tStat,
                                        independentLength, writeResultsOrDefaults, false);
                            }
                            //System.err.println("Done Skipping over outside-mask voxels, counter="+counter);
                        } // END: if mask exists and (k,j,i) xoxel is OUTSIDE the mask

                    } // Close dim[2], dim[1] and dim[0] Loops
                }
            }	// Close all 3 Loops

            System.out.println("Done WRITING all results to output streams!");

            // Close all INPUT Streams
            for (int fileList = 0; fileList < actualLength; fileList++) {
                mciis[fileList].close();
                bi[fileList].close();
                fi[fileList].close();
            }
            // Close all OUTPUT Streams
            if (outputInterceptVolumes == true) { // Intercept
                for (int indexIntVol = 0; indexIntVol < 4; indexIntVol++) {
                    iMciis[indexIntVol].flush();
                    iMciis[indexIntVol].close();
                    iBOS[indexIntVol].flush();
                    iBOS[indexIntVol].close();
                    iFOS[indexIntVol].flush();
                    iFOS[indexIntVol].close();
                    if (indexIntVol == 0) {
                        System.out.println("Intercept Output File[" + indexIntVol + "] ="
                                + intercept_Filename + "_Intercept_Pvalue" + ".img");
                    } else if (indexIntVol == 1) {
                        System.out.println("Intercept Output File[" + indexIntVol + "] ="
                                + intercept_Filename + "_Intercept_Beta" + ".img");
                    } else if (indexIntVol == 2) {
                        System.out.println("Intercept Output File[" + indexIntVol + "] ="
                                + intercept_Filename + "_Intercept_RPartCorr" + ".img");
                    } else if (indexIntVol == 3) {
                        System.out.println("Intercept Output File[" + indexIntVol + "] ="
                                + intercept_Filename + "_Intercept_TStat" + ".img");
                    }
                }
            }

            for (int regLength = 0; regLength < independentLength; regLength++) {
                if (outputPValuesVolumes == true) {
                    pMciis[regLength].flush();
                    pMciis[regLength].close();
                    pBOS[regLength].flush();
                    pBOS[regLength].close();
                    pFOS[regLength].flush();
                    pFOS[regLength].close();
                    System.out.println("p-Value Output File[" + regLength + "] =" + pValue_Filename
                            + "_Reg_" + regressorList.get(regLength).getRegressorString().replace('*', 'x') + ".img");
                }
                if (outputBEffectSizeVolumes == true) {
                    bMciis[regLength].flush();
                    bMciis[regLength].close();
                    bBOS[regLength].flush();
                    bBOS[regLength].close();
                    bFOS[regLength].flush();
                    bFOS[regLength].close();
                    System.out.println("Beta-Value Output File[" + regLength + "] =" + bValue_Filename
                            + "_Reg_" + regressorList.get(regLength).getRegressorString().replace('*', 'x') + ".img");
                }
                if (outputRPartialCorrVolumes == true) {
                    rMciis[regLength].flush();
                    rMciis[regLength].close();
                    rBOS[regLength].flush();
                    rBOS[regLength].close();
                    rFOS[regLength].flush();
                    rFOS[regLength].close();
                    System.out.println("Effect-Size Output File[" + regLength + "] =" + rValue_Filename
                            + "_Reg_" + regressorList.get(regLength).getRegressorString().replace('*', 'x') + ".img");
                }
                if (outputTStatVolumes) {
                    tMciis[regLength].flush();
                    tMciis[regLength].close();
                    tBOS[regLength].flush();
                    tBOS[regLength].close();
                    tFOS[regLength].flush();
                    tFOS[regLength].close();
                    System.out.println("T-Stats Output File[" + regLength + "] =" + tStat_Filename
                            + "_TStat_" + regressorList.get(regLength).getRegressorString().replace('*', 'x') + ".img");
                }
            }

            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!\n!!!!!!Complete!!!!!!!\n!!!!!!!!!!!!!!!!!!!!!!!!!!");

        } catch (Exception e) {
            System.err.println("VolumeMultipleRegression Error!!!!!!!!!!!!!!");
        }

    } // End of Main()

    /** check regressor list for interactions
     * in particular, each interaction variable must be composed of individual regressor variables
     * that are also included in the regressor list; for instance, if we have an interaction variable A*B*C,
     * we must have (minimally): -regressors A,B,C,A*B,B*C,A*C,A*B*C
     */
    public static boolean parseRegressors(StringTokenizer st) {
        HashSet<String> validated = new HashSet<String>();

        int regressorIndex = 0;
        String reg;
        while (st.hasMoreTokens()) {
            reg = st.nextToken();
//            System.out.println("reg = " + reg);
            regressorList.add(new Regressor(reg));
            regressorIndex++;
        }

        // impose an order on these regressors (least interactions to most)
        // at that point, we can iterate through them and just check the HashSet of already validated
        // regressors in order to validate a given regressor
        Collections.sort(regressorList, new RegressorComparator());
        boolean isValid = true;
        for (Regressor regressor : regressorList) {
            isValid = isRegressorValid(validated, regressor);
            if (isValid) {
                validated.add(regressor.getRegressorString());
            } else {
                return false;
            }
        }

        return true;
    }

    /** this method helps the parseRegressors method above
     * in particular, it takes a list of validated regressor variables and a not-yet-validated regressor variable
     * if the new variable is valid, this method returns true
     * if not, then it returns false; the criteria for validity is described in the description of parseRegressors
     */
    public static boolean isRegressorValid(HashSet<String> validRegressors, Regressor newRegressor) {
        if (newRegressor.getComponentCount() == 1) {
            return true;
        }

        ArrayList<String> regressorComponents = newRegressor.getComponents();
        for (String s : regressorComponents) {
            if (!validRegressors.contains(newRegressor.leaveComponentOut(s))) {
                return false;
            }
        }

        return true;
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
        if (byteorder.compareToIgnoreCase("little") == 0) {
            dis.setByteOrder(java.nio.ByteOrder.LITTLE_ENDIAN);
        } else if (byteorder.compareToIgnoreCase("big") == 0) {
            dis.setByteOrder(java.nio.ByteOrder.BIG_ENDIAN);
        } else {
            dis.setByteOrder(java.nio.ByteOrder.nativeOrder());
        }
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
        if (byteorder.compareToIgnoreCase("little") == 0) {
            dos.setByteOrder(java.nio.ByteOrder.LITTLE_ENDIAN);
        } else if (byteorder.compareToIgnoreCase("big") == 0) {
            dos.setByteOrder(java.nio.ByteOrder.BIG_ENDIAN);
        } else {
            dos.setByteOrder(java.nio.ByteOrder.nativeOrder());
        }
        return;
    }

    /**
     * This method READS IN a bufferSize chunk of all input volumes and returns it as a double[]
     * @param data_type - type of the input data
     * @param xLength - number of volumes/subjects in the second-column (file-names) in the Design-matrix
     * @param flush -	boolean variable indicating whether the buffer should be flushed as the stream keeps
     * 					in memory all the data it has read from disk. This will free up memory, but too frequent
     * 					calls (frequent flushing will slow down the process!)
     */
    public static double[] getNextInputDataBuffer(int data_type, int xLength, boolean flush) {
        double[] newInputData = new double[xLength];

        //if ((flush%(bufferSize))==0)
        //System.err.println(" ... getNextInputDataBuffer Flush Request(bufferSize="+bufferSize+")="+flush);
        //if ((flush%(bufferSize))==1)
        //System.err.println(" ... getNextInputDataBuffer Flush Request(bufferSize="+bufferSize+")="+(flush+1));

        //System.err.println("Beginning (getNextInputDataBuffer), xLength="+xLength+"\tflush="+flush);
        if (data_type == 0) {			// Unsigned BYTE input
            for (int fileList = 0; fileList < xLength; fileList++) {
                try {
                    newInputData[fileList] = mciis[fileList].readUnsignedByte();
                    /* 	long position = mciis[fileList].getStreamPosition();
                    if (flush && position > bufferSize)
                    mciis[fileList].flushBefore(position-bufferSize);
                     */
                    if (flush) {
                        mciis[fileList].flush();
                    }
                } catch (Exception e) {
                    newInputData[fileList] = 0;
                    System.err.println("(0) newInputData[" + fileList + "]= 0\n");
                }
            }
        } else if (data_type == 1) {			// Signed BYTE input
            for (int fileList = 0; fileList < xLength; fileList++) {
                try {
                    newInputData[fileList] = mciis[fileList].readByte();
                    if (flush) {
                        mciis[fileList].flush();
                    }
                } catch (Exception e) {
                    newInputData[fileList] = 0;
                    System.err.println("(1) newInputData[" + fileList + "]= 0\n");
                }
            }
        } else if (data_type == 2) {			// SHORT Unsigned Integer input
            for (int fileList = 0; fileList < xLength; fileList++) {
                try {
                    if (byteswap == false) {
                        newInputData[fileList] = mciis[fileList].readUnsignedShort();
                    } else {
                        newInputData[fileList] = swapShort(
                                shortToBytes((short) mciis[fileList].readUnsignedShort()), 0);
                    }
                    if (flush) {
                        mciis[fileList].flush();
                    }
                } catch (Exception e) {
                    newInputData[fileList] = 0;
                    System.err.println("(2) newInputData[" + fileList + "]= 0\n");
                    //System.err.println("Exception: "+e.printStackTrace());
                    System.err.println("Exception: " + e);
                    System.err.println("mciis[" + fileList + "]=" + mciis[fileList].toString());
                    System.err.println("getCause=" + e.getCause()
                            + "\t getLocalizedMessage=" + e.getLocalizedMessage()
                            + "\t getStackTrace=" + e.getMessage());
                    try {
                        System.err.println("mciis[fileList].getStreamPosition()="
                                + mciis[fileList].getStreamPosition());
                    } catch (IOException e1) {
                        System.err.println("IOException e1=" + e1);
                    }
                }
            }
        } else if (data_type == 3) {			// SHORT Signed-Integer input
            for (int fileList = 0; fileList < xLength; fileList++) {
                try {
                    if (byteswap == false) {
                        newInputData[fileList] = mciis[fileList].readShort();
                    } else {
                        newInputData[fileList] = swapShort((short) mciis[fileList].readShort());
                    }
                    if (flush) {
                        mciis[fileList].flush();
                    }
                } catch (Exception e) {
                    newInputData[fileList] = 0;
                    System.err.println("(3) newInputData[" + fileList + "]= 0\n");
                }
            }
        } else if (data_type == 4) {				// FLOAT Input
            for (int fileList = 0; fileList < xLength; fileList++) {
                try {
                    if (byteswap == false) {
                        newInputData[fileList] = mciis[fileList].readFloat();
                    } else {
                        newInputData[fileList] = swapFloat(mciis[fileList].readFloat());
                    }
                    if (flush) {
                        mciis[fileList].flush();
                    }
                } catch (Exception e) {
                    newInputData[fileList] = 0;
                    System.err.println("(4) newInputData[" + fileList + "]= 0 \t (flush="
                            + flush + ", flush=" + flush + ")\n");
                }
            }
        } else {
            System.err.println("Incorrect Input-Volume Data Type: " + data_type + ". Cannot run the regression\n"
                    + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression");
            return null;
        }
        //System.err.println("End (getNextInputDataBuffer), flush="+flush);
        return newInputData;
    }	// End: getNextInputDataBuffer

    /**
     * This method WRITES OUT bufferSize chunks of all output volumes
     * @param constantIntercept_P_B_R_T contains the pValue, Beta, R_PartCorr and TStat parameters of the intercept in the GLM
     * @param pValue is a Probability value outputed by (MultiLinearRegressionResult) Result object
     * @param beta is a beta/regression value outputted by (MultiLinearRegressionResult) Result object
     * @param rPartCor is a partial-correlation outputted by (MultiLinearRegressionResult) Result object
     * @param tStat is a T-statistics as outputed by (MultiLinearRegressionResult) Result object
     * @param independentLength - integer value representing the number of Independent X Variables
     * @param writeResultsOrDefaults - true==results; false=Default
     * @param flush -	Boolean variable indicating whether the buffer should be flushed as teh ...keeps
     * 					in memory all the data it has read from disk. This will free up memory, but too frequent
     * 					calls (flush==true) will slow down the process!
     */
    //public static void putNextOutputDataBuffer(MultiLinearRegressionResult result,
    public static void putNextOutputDataBuffer(double[] constantIntercept_P_B_R_T, double[] pValue,
            double[] beta, double[] rPartCor, double[] tStat, int independentLength,
            boolean writeResultsOrDefaults, boolean flush) {

        float tempFloat = 0;			// A temporary Float Variable

        if (writeResultsOrDefaults == false) {    // || result==null) {
            // if mask exists and (k,j,i) xoxel is OUTSIDE the mask write trivial output)
            //System.out.println("\t\t Writing out trivial Defaults (not results!)");

            // 1. First write out the Intercept parameters
            if (outputInterceptVolumes == true) {
                for (int i = 0; i < 4; i++) {
                    if (i == 0) {
                        tempFloat = (float) 1.0; // trivial p-values are 1's
                    } else {
                        tempFloat = (float) 0.0;		// trivial b, r, t values are 0's
                    }
                    try {
                        if (byteswap == false) {
                            iMciis[i].writeFloat(tempFloat);
                        } else {
                            iMciis[i].writeFloat(swapFloat(tempFloat));
                        }
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                }
            }

            // 2. Then write out the P, b, R, T estimates for all predictors (X-variables)
            for (int regLength = 0; regLength < independentLength; regLength++) {
                tempFloat = (float) 0.0;
                if (outputPValuesVolumes == true) {
                    try {
                        //pDataOS[regLength].writeFloat(tempFloat+1);
                        if (byteswap == false) {
                            pMciis[regLength].writeFloat(tempFloat + 1);
                        } else {
                            pMciis[regLength].writeFloat(swapFloat(tempFloat + 1));
                        }
                        //if (flush) pMciis[regLength].flush();
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                }
                if (outputBEffectSizeVolumes == true) {
                    try {
                        //bDataOS[regLength].writeFloat(tempFloat);
                        if (byteswap == false) {
                            bMciis[regLength].writeFloat(tempFloat);
                        } else {
                            bMciis[regLength].writeFloat(swapFloat(tempFloat));
                        }
                        //if (flush) bMciis[regLength].flushBefore(bMciis[regLength].getStreamPosition());
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                }
                if (outputRPartialCorrVolumes == true) {
                    try {
                        //rDataOS[regLength].writeFloat(tempFloat);
                        if (byteswap == false) {
                            rMciis[regLength].writeFloat(tempFloat);
                        } else {
                            rMciis[regLength].writeFloat(swapFloat(tempFloat));
                        }
                        //if (flush) rMciis[regLength].flushBefore(rMciis[regLength].getStreamPosition());
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                }
                if (outputTStatVolumes == true) {
                    try {
                        //tDataOS[regLength].writeFloat(tempFloat);
                        if (byteswap == false) {
                            tMciis[regLength].writeFloat(tempFloat);
                        } else {
                            tMciis[regLength].writeFloat(swapFloat(tempFloat));
                        }
                        //if (flush) tMciis[regLength].flushBefore(tMciis[regLength].getStreamPosition());
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                }
            }
        } // END: writeResultsOrDefaults==false <==>
        // if mask exists and (k,j,i) xoxel is OUTSIDE the mask
        else {  // if(writeResultsOrDefaults=true) {	// write out real results
            // The first estimate-value and p-value are for the CONSTANT term.
            // Thus, we report only estimates/effect-sizes and p-values for
            // user-specified predictors/covariates only!
            // independentLength should equal to beta.length

            // 1. First write out the Intercept parameters
            if (outputInterceptVolumes == true) {
                for (int i = 0; i < 4; i++) {
                    tempFloat = (float) constantIntercept_P_B_R_T[i];
                    try {
                        if (byteswap == false) {
                            iMciis[i].writeFloat(tempFloat);
                        } else {
                            iMciis[i].writeFloat(swapFloat(tempFloat));
                        }
                    } catch (IOException e) {
                        System.err.print(e);
                    }
                }
            }

            // 2. Then write out the P, b, R, T estimates for all predictors (X-variables)
            for (int regLength = 0; regLength < independentLength; regLength++) {
                if (outputPValuesVolumes) {
                    tempFloat = (float) pValue[regLength];
                    try {
                        //pDataOS[regLength].writeFloat(tempFloat);
                        if (byteswap == false) {
                            pMciis[regLength].writeFloat(tempFloat);
                        } else {
                            pMciis[regLength].writeFloat(swapFloat(tempFloat));
                        }
                        //if (flush) pMciis[regLength].flush();
                    } catch (IOException e) {
                        System.err.print(e);
                    }
                }
                if (outputBEffectSizeVolumes) {
                    tempFloat = (float) beta[regLength];
                    try {
                        //bDataOS[regLength].writeFloat(tempFloat);
                        if (byteswap == false) {
                            bMciis[regLength].writeFloat(tempFloat);
                        } else {
                            bMciis[regLength].writeFloat(swapFloat(tempFloat));
                        }
                        //if (flush) bMciis[regLength].flushBefore(bMciis[regLength].getStreamPosition());
                    } catch (IOException e) {
                        System.err.print(e);
                    }
                }
                if (outputRPartialCorrVolumes) {
                    tempFloat = (float) rPartCor[regLength];
                    try {
                        //rDataOS[regLength].writeFloat(tempFloat);
                        if (byteswap == false) {
                            rMciis[regLength].writeFloat(tempFloat);
                        } else {
                            rMciis[regLength].writeFloat(swapFloat(tempFloat));
                        }
                        //if (flush) rMciis[regLength].flushBefore(rMciis[regLength].getStreamPosition());
                    } catch (IOException e) {
                        System.err.print(e);
                    }
                }
                if (outputTStatVolumes) {
                    tempFloat = (float) tStat[regLength];
                    try {
                        //tDataOS[regLength].writeFloat(tempFloat);
                        if (byteswap == false) {
                            tMciis[regLength].writeFloat(tempFloat);
                        } else {
                            tMciis[regLength].writeFloat(swapFloat(tempFloat));
                        }
                        //if (flush) tMciis[regLength].flushBefore(tMciis[regLength].getStreamPosition());
                    } catch (IOException e) {
                        System.err.print(e);
                    }
                }
            }
            //System.err.println("\t\t Completed First Iteration!");
        } 	// End of IF writeResultsOrDefaults=true <==>
        // No-mask OR (mask exists AND (k,j,i) xoxel is inside the mask)
    } // END: putNextOutputDataBuffer

    /**
     * This method SKIPS in reading data from the bufferSize chunk of all input volumes
     * @param data_type - type of the input data
     * @param xLength - number of volumes/subjects in the second-column (file-names) in the Design-matrix
     * @param flush -	boolean variable indicating whether the buffer should be flushed as the stream keeps
     * 					in memory all the data it has read from disk. This will free up memory, but too frequent
     * 					calls (frequent flushing will slow down the process!)
     */
    public static void skipNextInputDataBuffer(int data_type, int xLength, boolean flush) {
        //long position;

        if (data_type == 0 || data_type == 1) {			// BYTE input
            for (int fileList = 0; fileList < xLength; fileList++) {
                try {
                    mciis[fileList].skipBytes(1);
                    /* if (mciis[fileList].getStreamPosition() % bufferSize == 1)
                    mciis[fileList].flush();
                    position = mciis[fileList].getStreamPosition();
                    if (flush && position > bufferSize)
                    mciis[fileList].flushBefore(position-bufferSize);
                     */
                    //if (flush) mciis[fileList].flush();
                } catch (IOException e) {
                    System.err.print(e);
                }
            }
        } else if (data_type == 2 || data_type == 3) {			// SHORT 2-Byte Integer input
            for (int fileList = 0; fileList < xLength; fileList++) {
                try {
                    //if (flush && mciis[fileList].getStreamPosition()>bufferSize) {
                    //	System.err.println("skipNextInput: flush="+flush+": mciis[fileList].getStreamPosition()="+mciis[fileList].getStreamPosition());
                    //	mciis[fileList].flushBefore(mciis[fileList].getStreamPosition());
                    // }
                    mciis[fileList].skipBytes(2);
                    //if (flush) mciis[fileList].flush();
        			/*  if (mciis[fileList].getStreamPosition() % bufferSize == 1)
                    mciis[fileList].flush();
                    position = mciis[fileList].getStreamPosition();
                    if (flush && position > 2*bufferSize)
                    mciis[fileList].flushBefore(position-bufferSize);
                     */
                } catch (IOException e) {
                    System.err.print(e);
                }
            }
        } else if (data_type == 4) {				// FLOAT 4-byte Input
            for (int fileList = 0; fileList < xLength; fileList++) {
                try {
                    mciis[fileList].skipBytes(4);
                    /* if (mciis[fileList].getStreamPosition() % bufferSize == 1)
                    mciis[fileList].flush();
                    position = mciis[fileList].getStreamPosition();
                    if (flush && position > bufferSize)
                    mciis[fileList].flushBefore(position-bufferSize);
                     */
                    //if (flush) mciis[fileList].flush();
                } catch (IOException e) {
                    System.err.print(e);
                }
            }
        } else {
            System.err.println("Incorrect Input-Volume Data Type: " + data_type + ". Cannot run the regression\n"
                    + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolumeMultipleRegression");
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
        int low = b[offset] & 0xff;
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
            accum |= shiftedval;
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
            accum |= shiftedval;
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
        int low = b[offset] & 0xff;
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

        buffer[0] = (byte) l;
        buffer[1] = (byte) (l >> 8);
        buffer[2] = (byte) (l >> 16);
        buffer[3] = (byte) (l >> 24);

        //return (Float.intBitsToFloat(l));
        //return (Float.intBitsToFloat(byteArrayToInt(buffer, 0)));
        v = Float.intBitsToFloat(byteArrayToInt(buffer, 0));
        if (Float.isInfinite(v) || Float.isNaN(v)) {
            v = 0;
        }
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
        byte[] b = new byte[2];
        int allbits = 255;
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
        byte[] b = new byte[4];
        int allbits = 255;
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
        byte[] b = new byte[8];
        long allbits = 255;
        for (int i = 0; i < 8; i++) {
            b[7 - i] = (byte) ((v & (allbits << i * 8)) >> i * 8);
        }
        return b;
    }
}
