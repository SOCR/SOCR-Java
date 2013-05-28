package JSci.Demos.wavelet;


import JSci.maths.*;
import JSci.maths.wavelet.*;
import JSci.maths.wavelet.daubechies2.*;

/********************************************
* This class illustrates how to do
* signal processing with dau2 wavelets
* @author Daniel Lemire
*********************************************/
public class DougJamesDau2 {
        public static void main(String[] arg) {
                Daubechies2  ondelette = new Daubechies2();
                double[] d=new double[32];
                for(int k=0;k<d.length;k++) {
                        d[k]=k;
                }
                Signal  s = new Signal(d);
                s.setFilter(ondelette);
                int level=1;
                FWTCoef sCoef = s.fwt(level); // for some level int
                ArrayMath.print(sCoef.getCoefs()[0]);
                ArrayMath.print(sCoef.getCoefs()[1]);
                /******************************
                * We now have to check if we
                * can get the signal back!
                *******************************/
                ArrayMath.print(sCoef.rebuildSignal(ondelette).evaluate(0));
        }
}

