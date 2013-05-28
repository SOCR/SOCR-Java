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
package edu.ucla.stat.SOCR.analyses.data;

import edu.ucla.stat.SOCR.util.*;
   public class SurvivalObject extends DataCase{ //why extends DataCase? to be sorted.
	public static final int CENSORED_CONSTANT = 0;
	public static final int DEAD_CONSTANT = 1;

		private byte censor;
		private int index; // original index given by the user.
		public  SurvivalObject(double time, byte censor, int index) {
			this.value = time;
			this.censor = censor;
			this.index = index;
			////System.out.println("new object: time =  " + time + ", censor = " + censor + ", index = " + index);
		}
		public double getTime() { // value (from super class DataCase), is time.
			return this.value;
		}
		public byte getCensor() {
			return this.censor;
		}
		public int getIndex() {
			return this.index;
		}
		public void setCensorNotations(String censored, String dead) {

		}

   }
