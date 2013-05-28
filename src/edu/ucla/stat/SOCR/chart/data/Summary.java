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
package edu.ucla.stat.SOCR.chart.data;

import java.text.DecimalFormat;
import java.util.List;
import java.util.StringTokenizer;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.statistics.Statistics;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;

/**
 * this class handles the summary calculation for different types of dataset
 * @author jenny
 *
 */
public class Summary {

	/**
	 * @uml.property  name="dELIMITERS"
	 */
	protected final String DELIMITERS = ",;\t ";

	double zScale =1;
	/**
	 * @uml.property  name="seriesCount"
	 */
	int seriesCount;
	/**
	 * @uml.property  name="categoryCount"
	 */
	int categoryCount;	
	int[] sampleSize;
	/**
	 * @uml.property  name="mean" multiplicity="(0 -1)" dimension="1"
	 */
	double[] mean;
	/**
	 * @uml.property  name="median" multiplicity="(0 -1)" dimension="1"
	 */
	double[] median;
	/**
	 * @uml.property  name="stdDev" multiplicity="(0 -1)" dimension="1"
	 */
	double[] stdDev;
	/**
	 * @uml.property  name="skew" multiplicity="(0 -1)" dimension="1"
	 */
	double[] skew;
	/**
	 * @uml.property  name="kurt" multiplicity="(0 -1)" dimension="1"
	 */
	double[] kurt;
	// in categoryDataset cat_# is for category summary
	// in xyDataset cat_# is for X summary
	int[] cat_sampleSize;
	/**
	 * @uml.property  name="cat_mean" multiplicity="(0 -1)" dimension="1"
	 */
	double[] cat_mean;
	// in categoryDataset cat_# is for category summary
	// in xyDataset cat_# is for X summary
	/**
	 * @uml.property  name="cat_median" multiplicity="(0 -1)" dimension="1"
	 */
	double[] cat_median;
	// in categoryDataset cat_# is for category summary
	// in xyDataset cat_# is for X summary
	/**
	 * @uml.property  name="cat_stdDev" multiplicity="(0 -1)" dimension="1"
	 */
	double[] cat_stdDev;
	// in categoryDataset cat_# is for category summary
	// in xyDataset cat_# is for X summary
	/**
	 * @uml.property  name="cat_skew" multiplicity="(0 -1)" dimension="1"
	 */
	double[] cat_skew;
	// in categoryDataset cat_# is for category summary
	// in xyDataset cat_# is for X summary
	/**
	 * @uml.property  name="cat_kurt" multiplicity="(0 -1)" dimension="1"
	 */
	double[] cat_kurt;
	/**
	 * @uml.property  name="seriesName" multiplicity="(0 -1)" dimension="1"
	 */
	String[] seriesName;
	/**
	 * @uml.property  name="categoryName" multiplicity="(0 -1)" dimension="1"
	 */
	String[] categoryName;
	/**
	 * @uml.property  name="values_storage" multiplicity="(0 -1)" dimension="2"
	 */
	String [][] values_storage;

/**
 * calculate the statistical summary for the given dataset
 * @param dataset
 */
	public Summary(CategoryDataset dataset){

		seriesCount = dataset.getRowCount();
		categoryCount = dataset.getColumnCount();
		sampleSize = new int[seriesCount];
		mean = new double[seriesCount];
		median = new double[seriesCount];
		stdDev = new double[seriesCount];
		skew = new double[seriesCount];
		kurt = new double[seriesCount];
		
		cat_sampleSize = new int[categoryCount];
		cat_mean = new double[categoryCount];
		cat_median = new double[categoryCount];
		cat_stdDev = new double[categoryCount];
		cat_skew = new double[categoryCount];
		cat_kurt = new double[categoryCount];

		seriesName = new String[seriesCount];
		categoryName = new String[categoryCount];


		for (int i=0; i<seriesCount; i++){
			seriesName[i] = dataset.getRowKey(i).toString();
			Double[] values = new Double[categoryCount];
			List<Double> valueList = new java.util.ArrayList<Double>();
			for (int j=0; j<categoryCount; j++){
				double v;
				if (dataset.getValue(i,j)!=null){
					v= dataset.getValue(i,j).doubleValue();			
					values[j]=new Double(v);
					valueList.add(new Double(v));
				}
			}
			sampleSize[i] = valueList.size();
			mean[i] = Statistics.calculateMean(values, false);
			median[i] = Statistics.calculateMedian(valueList);
			stdDev[i] = Statistics.getStdDev(values);
			skew[i] = Statistics.calculateSkewness(values);
			kurt[i] = Statistics.calculateKurtosis(values);
		}
		
		for (int j=0; j<categoryCount; j++){
			categoryName[j] = dataset.getColumnKey(j).toString();
			Double[] values = new Double[seriesCount];
			List<Double> valueList = new java.util.ArrayList<Double>();
			for (int i=0; i<seriesCount; i++){
								double v;
				if (dataset.getValue(i,j)!=null){
					v = dataset.getValue(i,j).doubleValue();			
					values[i]=new Double(v);
					valueList.add(new Double(v));
				}
			}
			cat_sampleSize[j]= valueList.size();
			cat_mean[j] = Statistics.calculateMean(values, false);
			cat_median[j] = Statistics.calculateMedian(valueList);
			cat_stdDev[j] = Statistics.getStdDev(values);
			cat_skew[j] = Statistics.calculateSkewness(values);
			cat_kurt[j] = Statistics.calculateKurtosis(values);
		}
	
		return;
	}

	/**
	 * calculate the statistical summary for the given dataset
	 * @param dataset
	 */
	public Summary(PieDataset dataset){

		seriesCount = dataset.getItemCount();
		categoryCount = 1;
		
		cat_sampleSize = new int[categoryCount];
		cat_mean = new double[categoryCount];
		cat_median = new double[categoryCount];
		cat_stdDev = new double[categoryCount];
		cat_skew = new double[categoryCount];
		cat_kurt = new double[categoryCount];

		seriesName = new String[seriesCount];
		categoryName = new String[categoryCount];

		categoryName[0] = "Value";
		Double[] values = new Double[seriesCount];
		List<Double> valueList = new java.util.ArrayList<Double>();
		for (int i=0; i<seriesCount; i++){

			double v;
			if (dataset.getValue(i)!=null)
				v= dataset.getValue(i).doubleValue();	
			else v=0.0;
			values[i]=new Double(v);
			valueList.add(new Double(v));
		}
		
		cat_sampleSize[0]=valueList.size();
		cat_mean[0] = Statistics.calculateMean(values, false);
		cat_median[0] = Statistics.calculateMedian(valueList);
		cat_stdDev[0] = Statistics.getStdDev(values);
		cat_skew[0] = Statistics.calculateSkewness(values);
		cat_kurt[0] = Statistics.calculateKurtosis(values);
	
		return;
	}
	
	/**
	 * calculate the statistical summary for the given dataset
	 * @param dataset
	 */
public Summary(XYZDataset dataset){

		seriesCount = dataset.getSeriesCount();
		sampleSize = new int[seriesCount];
		mean = new double[seriesCount];
		median = new double[seriesCount];
		stdDev = new double[seriesCount];
		skew = new double[seriesCount];
		kurt = new double[seriesCount];

		seriesName = new String[seriesCount];
		int count = 0;
		for (int i=0; i<seriesCount; i++){
			int rowCount = dataset.getItemCount(i);
			seriesName[i] = dataset.getSeriesKey(i).toString();
			Double[] values = new Double[rowCount];
			List<Double> valueList = new java.util.ArrayList<Double>();
			for (int j=0; j<rowCount; j++){
				double v = dataset.getXValue(i,j);
				if (!Double.isNaN(v)){
					values[count]=new Double(v);
					valueList.add(new Double(v));
					count++;
				}
			}
			sampleSize[i]= valueList.size();
			mean[i] = Statistics.calculateMean(values, false);
			median[i] = Statistics.calculateMedian(valueList);
			stdDev[i] = Statistics.getStdDev(values);
			skew[i] = Statistics.calculateSkewness(values);
			kurt[i] = Statistics.calculateKurtosis(values);
		}
		return;
	}

public void setZScale(double in){
	zScale = in;
}

/**
 * calculate the statistical summary for the given dataset
 * @param dataset
 */
public Summary(XYDataset dataset){

		seriesCount = dataset.getSeriesCount();
		categoryCount = seriesCount;
		sampleSize = new int[seriesCount];
		mean = new double[seriesCount];
		median = new double[seriesCount];
		stdDev = new double[seriesCount];
		skew = new double[seriesCount];
		kurt = new double[seriesCount];

		cat_sampleSize = new int[seriesCount];
		cat_mean = new double[seriesCount];
		cat_median = new double[seriesCount];
		cat_stdDev = new double[seriesCount];
		cat_skew = new double[seriesCount];
		cat_kurt = new double[seriesCount];

		seriesName = new String[seriesCount];
		categoryName = new String[seriesCount];

		for (int i=0; i<seriesCount; i++){
			int rowCount = dataset.getItemCount(i);
			seriesName[i] = dataset.getSeriesKey(i).toString(); //+".Y";
			Double[] values = new Double[rowCount];
			List<Double> valueList = new java.util.ArrayList<Double>();
			int count =0;
			for (int j=0; j<rowCount; j++){
				double v = dataset.getYValue(i,j);
				if (!Double.isNaN(v)){
					values[count]=new Double(v);
					valueList.add(new Double(v));
					count++;
				}
			}
			sampleSize[i]= valueList.size();
			mean[i] = Statistics.calculateMean(values, false);
			median[i] = Statistics.calculateMedian(valueList);
			stdDev[i] = Statistics.getStdDev(values);
			skew[i] = Statistics.calculateSkewness(values);
			kurt[i] = Statistics.calculateKurtosis(values);
		}
		
		for (int i=0; i<seriesCount; i++){
			int rowCount = dataset.getItemCount(i);
			categoryName[i] = dataset.getSeriesKey(i).toString(); //+".X";
			Double[] values = new Double[rowCount];
			List<Double> valueList = new java.util.ArrayList<Double>();
			int count =0;
			for (int j=0; j<rowCount; j++){
				double v = dataset.getXValue(i,j);
				if (!Double.isNaN(v)){
					values[count]=new Double(v);
					valueList.add(new Double(v));
					count++;
				}
			}
			cat_sampleSize[i]= valueList.size();
			cat_mean[i] = Statistics.calculateMean(values, false);
			cat_median[i] = Statistics.calculateMedian(valueList);
			cat_stdDev[i] = Statistics.getStdDev(values);
			cat_skew[i] = Statistics.calculateSkewness(values);
			cat_kurt[i] = Statistics.calculateKurtosis(values);
		}
	
		return;
	}

	public Summary(CategoryDataset dataset, String[][] vs, int sCount, int cCount){
		//System.out.println("sCount ="+sCount+" cCount="+cCount);
		values_storage = new String[sCount][cCount];
		seriesCount = sCount; 
		categoryCount = cCount;	
		for (int s=0; s<sCount; s++)
			 for (int c=0; c<cCount; c++)
				 values_storage[s][c] = vs[s][c];	
	}

	/**
	 *  return the statistical summary for the given serie
	 * @param dataset
	 * @param serieIndex
	 * @return
	 */
	public String getXYZSummary(XYZDataset dataset, int serieIndex){
		
		//System.out.println("stdDev="+stdDev[serieIndex]);

		String info = "";
		for (int ind=0; ind<3; ind++){
			if (ind==0)
				info +="\n   Y:";
			else if (ind==1) {
				info +="\n   X:";
				for (int i=0; i<seriesCount; i++){
					int rowCount = dataset.getItemCount(i);
					seriesName[i] = dataset.getSeriesKey(i).toString();
					Double[] values = new Double[rowCount];
					List<Double> valueList = new java.util.ArrayList<Double>();
					int count=0;
					for (int j=0; j<rowCount; j++){
						double v = dataset.getYValue(i,j);
						if (!Double.isNaN(v)){
							values[count]=new Double(v);
							valueList.add(new Double(v));
							count++;
						}
					}
					sampleSize[i]= valueList.size();
					mean[i] = Statistics.calculateMean(values, false);
					median[i] = Statistics.calculateMedian(valueList);
					stdDev[i] = Statistics.getStdDev(values);
					skew[i] = Statistics.calculateSkewness(values);
					kurt[i] = Statistics.calculateKurtosis(values);
				}
			}
			else if (ind==2){
				info +="\n   Z:";
				for (int i=0; i<seriesCount; i++){
					int rowCount = dataset.getItemCount(i);
					seriesName[i] = dataset.getSeriesKey(i).toString();
					Double[] values = new Double[rowCount];
					List<Double> valueList = new java.util.ArrayList<Double>();
					int count=0;
					for (int j=0; j<rowCount; j++){
						double v = dataset.getZValue(i,j);
						if (!Double.isNaN(v)){
							values[count]=new Double(v)/zScale;
							valueList.add(new Double(v)/zScale);
							count++;
						}
					}
					sampleSize[i]= valueList.size();
					mean[i] = Statistics.calculateMean(values, false);
					median[i] = Statistics.calculateMedian(valueList);
					stdDev[i] = Statistics.getStdDev(values);
					skew[i] = Statistics.calculateSkewness(values);
					kurt[i] = Statistics.calculateKurtosis(values);
				}
			}
			
			info += " SampleSize ="+setInfo(sampleSize[serieIndex]);
			info += " Mean="+setInfo(mean[serieIndex]);
			info += " Median="+setInfo(median[serieIndex]);
			info += " stdDev="+setInfo(stdDev[serieIndex]);
			info += " Skewness="+setInfo(skew[serieIndex]);
			info += " Kurtosis="+setInfo(kurt[serieIndex]);
		}
		info +="\n";
		return info;
	}
/**
 * return summary for each table cell of  the given serie
 * @param dataset
 * @param serieIndex
 * @return
 */
	public String getCellSummary(CategoryDataset dataset, int serieIndex){
		String info ="";
		
		double mean, median, stdDev, skew, kurt;
		int sampleSize;


            for (int c = 0; c < categoryCount; c++) {

                Double[] values = createValueList(values_storage[serieIndex][c]);
				List<Double> valueList = new java.util.ArrayList<Double>();
				for (int i=0; i<values.length; i++)
					if(!Double.isNaN(values[i]))
					valueList.add(values[i]);
				sampleSize = valueList.size();
				mean = Statistics.calculateMean(values, false);
				median= Statistics.calculateMedian(valueList);
				stdDev = Statistics.getStdDev(values);
				skew = Statistics.calculateSkewness(values);
				kurt = Statistics.calculateKurtosis(values);

				String k = dataset.getRowKey(serieIndex).toString();
				if(k.length()>0)
					info += "["+k+".";
				else 
					info += "[";
				info += dataset.getColumnKey(c).toString()+"]:";
				info += " SampleSize="+setInfo(sampleSize);
				info += " Mean="+setInfo(mean);
				info += " Median="+setInfo(median);
				info += " stdDev="+setInfo(stdDev);
				info += " Skewness="+setInfo(skew);
				info += " Kurtosis="+setInfo(kurt);
				info +="\n";
            }
			return info;
	}

	/**
	 * return serie summary
	 * this is also for getting Y summary for XYDataset
	 */ 
	public String getSeriesSummary(int serieIndex){
		//System.out.println("stdDev="+stdDev[serieIndex]);

		String info = "";
		info += " SampleSize="+setInfo(sampleSize[serieIndex]);
		info += " Mean="+setInfo(mean[serieIndex]);
		info += " Median="+setInfo(median[serieIndex]);
		info += " stdDev="+setInfo(stdDev[serieIndex]);
		info += " Skewness="+setInfo(skew[serieIndex]);
		info += " Kurtosis="+setInfo(kurt[serieIndex]);
		info +="\n";
		return info;
	}

	/**
	 *  return Y summary for XYDataset
	 * @param serieIndex
	 * @return
	 */
	public String getYSummary(int serieIndex){
		return seriesName[serieIndex]+":"+getSeriesSummary(serieIndex);
	}

	/**
	 * return X summary for XYDataset
	 * @param serieIndex
	 * @return
	 */
	public String getXSummary(int serieIndex){

		String info = categoryName[serieIndex]+":";
		info += " SampleSize="+setInfo(cat_sampleSize[serieIndex]);
		info += " Mean="+setInfo(cat_mean[serieIndex]);
		info += " Median="+setInfo(cat_median[serieIndex]);
		info += " stdDev="+setInfo(cat_stdDev[serieIndex]);
		info += " Skewness="+setInfo(cat_skew[serieIndex]);
		info += " Kurtosis="+setInfo(cat_kurt[serieIndex]);
		info +="\n";
		return info;
	}

	/**
	 *  return both X and Y  Summary
	 * @param serieIndex
	 * @return
	 */
	public String getQQSummary(int serieIndex){

		String info = getYSummary(serieIndex);
		info+=getXSummary(serieIndex);
		return info;
	}

	/**
	 * return category summary
	 * @param catIndex
	 * @return
	 */
	public String getCategorySummary(int catIndex){
		//System.out.println("stdDev="+stdDev[serieIndex]);

		String info = categoryName[catIndex]+":";
		info += " SampleSize="+setInfo(cat_sampleSize[catIndex]);
		info += " Mean="+setInfo(cat_mean[catIndex]);
		info += " Median="+setInfo(cat_median[catIndex]);
		info += " stdDev="+setInfo(cat_stdDev[catIndex]);
		info += " Skewness="+setInfo(cat_skew[catIndex]);
		info += " Kurtosis="+setInfo(cat_kurt[catIndex]);
		info +="\n";
		return info;
	}

	public String getSerieName(int serieIndex){
		return seriesName[serieIndex];
	}

	public String getCategoryName(int catIndex){
		return categoryName[catIndex];
	}
	public int getSeriesCount(){
		return seriesCount;
	}
	public int getCategoryCount(){
		return categoryCount;
	}


	protected String setInfo(double value){
		
		DecimalFormat f=	new DecimalFormat("#0.#####");
		String info="";

		if (Double.isNaN(value))
			info += value;
		else info += f.format(value);

		return info;
	}

	//same as the one in SuperCategoryChart_Stat_Raw class
	protected Double[] createValueList(String in){

		   StringTokenizer st = new StringTokenizer(in,DELIMITERS);
		   int count = st.countTokens();
		   String[] values = new String[count];
		   for (int i=0; i<count; i++)
			   values[i]=st.nextToken();

		   Double[] result = new Double[count];
		 
			   for (int i = 0; i < count; i++) {
				   if (values[i]!= null){
					   try{
						   double v = Double.parseDouble(values[i]); 
						   result[i] = new Double(v);   
					   }catch(NumberFormatException e){
						   result[i] = Double.NaN;
					   }
					  
				   }else  result[i] = Double.NaN;
		  
			   }

	        return result;
    }

}
