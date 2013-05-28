package JSci.maths.wavelet;

/********************************************************
* This exception is used to indicate that the
* current chosen multiresolution is not appropriate
* because the number of scaling functions is incompatible
* (with the multiresolution).
* @author Daniel Lemire
**********************************************************/
public class IllegalScalingException extends IllegalArgumentException {
	public IllegalScalingException () {}
	public IllegalScalingException (String s) {super(s);}
	public IllegalScalingException (int n0,int min) {
		super("The length parameter "+n0+" must be at least "+min+". Please change the wavelet or the number of iterations.");
	}

}
