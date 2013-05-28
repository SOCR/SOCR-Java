package JSci.maths;

/**
* The Fourier math library.
* This class cannot be subclassed or instantiated because all methods are static.
* @planetmath FourierTransform
* @version 0.8
* @author Mark Hale
*/
public final class FourierMath extends AbstractMath implements NumericalConstants {
        private FourierMath() {}

        /**
        * Fourier transform.
        * @return an array containing positive frequencies in ascending order
        * followed by negative frequencies in ascending order.
        * @author Don Cross
        */
        public static Complex[] transform(final Complex data[]) {
                final int N=data.length;
                final double arrayRe[]=new double[N];
                final double arrayIm[]=new double[N];
                int i,j;

                if(!isPowerOf2(N))
                        throw new IllegalArgumentException("The number of samples must be a power of 2.");

                int numBits=numberOfBitsNeeded(N);
// Simultaneous data copy and bit-reversal ordering into output
                for(i=0;i<N;i++) {
                        j=reverseBits(i,numBits);
                        arrayRe[j]=data[i].real();
                        arrayIm[j]=data[i].imag();
                }
// FFT
                fft(arrayRe, arrayIm, TWO_PI);

                final Complex answer[]=new Complex[N];
                for(i=0;i<N;i++)
                        answer[i]=new Complex(arrayRe[i],arrayIm[i]);
                return answer;
        }
        /**
        * Fourier transform.
        * @return an array containing positive frequencies in ascending order
        * followed by negative frequencies in ascending order.
        * @author Don Cross
        */
        public static Complex[] transform(final double dataReal[], final double dataImag[]) {
                final int N=dataReal.length;
                final double arrayRe[]=new double[N];
                final double arrayIm[]=new double[N];
                int i,j;

                if(!isPowerOf2(N))
                        throw new IllegalArgumentException("The number of samples must be a power of 2.");

                int numBits=numberOfBitsNeeded(N);
// Simultaneous data copy and bit-reversal ordering into output
                for(i=0;i<N;i++) {
                        j=reverseBits(i,numBits);
                        arrayRe[j]=dataReal[i];
                        arrayIm[j]=dataImag[i];
                }
// FFT
                fft(arrayRe, arrayIm, TWO_PI);

                final Complex answer[]=new Complex[N];
                for(i=0;i<N;i++)
                        answer[i]=new Complex(arrayRe[i],arrayIm[i]);
                return answer;
        }
        /**
        * Fourier transform.
        * @return an array containing positive frequencies in ascending order
        * followed by negative frequencies in ascending order.
        */
        public static Complex[] transform(final double data[]) {
                final int N=data.length;
                final double arrayRe[]=new double[N];
                final double arrayIm[]=new double[N];
                int i,j;

                if(!isPowerOf2(N))
                        throw new IllegalArgumentException("The number of samples must be a power of 2.");

                int numBits=numberOfBitsNeeded(N);
// Simultaneous data copy and bit-reversal ordering into output
                for(i=0;i<N;i++) {
                        j=reverseBits(i,numBits);
                        arrayRe[j]=data[i];
                }
// FFT
                fft(arrayRe, arrayIm, TWO_PI);

                final Complex answer[]=new Complex[N];
                for(i=0;i<N;i++)
                        answer[i]=new Complex(arrayRe[i],arrayIm[i]);
                return answer;
        }
        /**
        * Inverse Fourier transform.
        * @return an array containing the positive time part of the signal
        * followed by the negative time part.
        * @author Don Cross
        */
        public static Complex[] inverseTransform(final Complex data[]) {
                final int N=data.length;
                final double arrayRe[]=new double[N];
                final double arrayIm[]=new double[N];
                int i,j;

                if(!isPowerOf2(N))
                        throw new IllegalArgumentException("Data length must be a power of 2.");

                int numBits=numberOfBitsNeeded(N);
// Simultaneous data copy and bit-reversal ordering into output
                for(i=0;i<N;i++) {
                        j=reverseBits(i,numBits);
                        arrayRe[j]=data[i].real();
                        arrayIm[j]=data[i].imag();
                }
// inverse FFT
                fft(arrayRe, arrayIm, -TWO_PI);
// Normalize
                final Complex answer[]=new Complex[N];
                final double denom=N;
                for(i=0;i<N;i++)
                        answer[i]=new Complex(arrayRe[i]/denom,arrayIm[i]/denom);
                return answer;
        }
        /**
        * Inverse Fourier transform.
        * @return an array containing the positive time part of the signal
        * followed by the negative time part.
        * @author Don Cross
        */
        public static Complex[] inverseTransform(final double dataReal[], final double dataImag[]) {
                final int N=dataReal.length;
                final double arrayRe[]=new double[N];
                final double arrayIm[]=new double[N];
                int i,j;

                if(!isPowerOf2(N))
                        throw new IllegalArgumentException("Data length must be a power of 2.");

                int numBits=numberOfBitsNeeded(N);
// Simultaneous data copy and bit-reversal ordering into output
                for(i=0;i<N;i++) {
                        j=reverseBits(i,numBits);
                        arrayRe[j]=dataReal[i];
                        arrayIm[j]=dataImag[i];
                }
// inverse FFT
                fft(arrayRe, arrayIm, -TWO_PI);
// Normalize
                final Complex answer[]=new Complex[N];
                final double denom=N;
                for(i=0;i<N;i++)
                        answer[i]=new Complex(arrayRe[i]/denom,arrayIm[i]/denom);
                return answer;
        }
        /**
        * Inverse Fourier transform.
        * @return an array containing the positive time part of the signal
        * followed by the negative time part.
        */
        public static Complex[] inverseTransform(final double data[]) {
                final int N=data.length;
                final double arrayRe[]=new double[N];
                final double arrayIm[]=new double[N];
                int i,j;

                if(!isPowerOf2(N))
                        throw new IllegalArgumentException("Data length must be a power of 2.");

                int numBits=numberOfBitsNeeded(N);
// Simultaneous data copy and bit-reversal ordering into output
                for(i=0;i<N;i++) {
                        j=reverseBits(i,numBits);
                        arrayRe[j]=data[i];
                }
// inverse FFT
                fft(arrayRe, arrayIm, -TWO_PI);
// Normalize
                final Complex answer[]=new Complex[N];
                final double denom=N;
                for(i=0;i<N;i++)
                        answer[i]=new Complex(arrayRe[i]/denom,arrayIm[i]/denom);
                return answer;
        }
        /**
        * Common FFT code.
        * @param twoPi TWO_PI for transform, -TWO_PI for inverse transform.
        */
        private static void fft(double arrayRe[], double arrayIm[], final double twoPi) {
                final int N=arrayRe.length;
                int i,j,k,n;
                double deltaAngle,angleRe,angleIm,tmpRe,tmpIm;
                double alpha,beta; // used in recurrence relation

                int blockEnd=1;
                for(int blockSize=2;blockSize<=N;blockSize<<=1) {
                        deltaAngle=twoPi/blockSize;
                        alpha=Math.sin(0.5*deltaAngle);
                        alpha=2.0*alpha*alpha;
                        beta=Math.sin(deltaAngle);
                        for(i=0;i<N;i+=blockSize) {
                                angleRe=1.0;angleIm=0.0;
                                for(j=i,n=0;n<blockEnd;j++,n++) {
                                        k=j+blockEnd;
                                        tmpRe=angleRe*arrayRe[k]-angleIm*arrayIm[k];
                                        tmpIm=angleRe*arrayIm[k]+angleIm*arrayRe[k];
                                        arrayRe[k]=arrayRe[j]-tmpRe;
                                        arrayIm[k]=arrayIm[j]-tmpIm;
                                        arrayRe[j]+=tmpRe;
                                        arrayIm[j]+=tmpIm;
                                        tmpRe=alpha*angleRe+beta*angleIm;
                                        tmpIm=alpha*angleIm-beta*angleRe;
                                        angleRe-=tmpRe;
                                        angleIm-=tmpIm;
                                }
                        }
                        blockEnd=blockSize;
                }
        }
        /**
        * Returns true if x is a power of 2.
        * @author Don Cross
        */
        private static boolean isPowerOf2(final int x) {
                final int BITS_PER_WORD=32;
                for(int i=1,y=2;i<BITS_PER_WORD;i++,y<<=1) {
                        if(x==y)
                                return true;
                }
                return false;
        }
        /**
        * Number of bits needed.
        * @author Don Cross
        */
        private static int numberOfBitsNeeded(final int pwrOf2) {
                if(pwrOf2<2)
                        throw new IllegalArgumentException();
                for(int i=0;;i++) {
                        if((pwrOf2&(1<<i))>0)
                                return i;
                }
        }
        /**
        * Reverse bits.
        * @author Don Cross
        */
        private static int reverseBits(int index,final int numBits) {
                int i,rev;
                for(i=rev=0;i<numBits;i++) {
                        rev=(rev<<1)|(index&1);
                        index>>=1;
                }
                return rev;
        }

        /**
        * Sorts the output from the Fourier transfom methods into
        * ascending frequency/time order.
        */
        public static Complex[] sort(final Complex output[]) {
                final Complex ret[]=new Complex[output.length];
                final int Nby2=output.length/2;
                for(int i=0;i<Nby2;i++) {
                        ret[Nby2+i]=output[i];
                        ret[i]=output[Nby2+i];
                }
                return ret;
        }
}

