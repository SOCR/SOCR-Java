package JSci.maths;

import JSci.maths.groups.AbelianGroup;
import JSci.maths.fields.Ring;

/**
* The Supernumber class encapsulates supernumbers.
* They are actually implemented as elements of the Grassmann algebra <img border=0 alt="Lambda" src="doc-files/ulambda.gif"><sub>4</sub>
* rather than the full <img border=0 alt="Lambda" src="doc-files/ulambda.gif"><sub><img border=0 alt="infinity" src="doc-files/infinity.gif"></sub>.
* @planetmath Supernumber
* @version 0.1
* @author Mark Hale
*/
public final class Supernumber extends Object implements Ring.Member {
        private final int N=4;
        private Complex body=Complex.ZERO;
        private Complex soul1[]={Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ZERO};
        private Complex soul2[]={Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ZERO};
        private Complex soul3[]={Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ZERO};
        private Complex soul4=Complex.ZERO;
        /**
        * Constructs a supernumber.
        */
        public Supernumber() {}
        /**
        * Returns a string representing the value of this supernumber.
        */
        public String toString() {
                final StringBuffer buf=new StringBuffer(100);
                buf.append("(").append(body.toString()).append(")1+\n(");
                buf.append(soul1[0].toString()).append(", ");
                buf.append(soul1[1].toString()).append(", ");
                buf.append(soul1[2].toString()).append(", ");
                buf.append(soul1[3].toString()).append(")S1+\n(");
                buf.append(soul2[0].toString()).append(", ");
                buf.append(soul2[1].toString()).append(", ");
                buf.append(soul2[2].toString()).append(", ");
                buf.append(soul2[3].toString()).append(", ");
                buf.append(soul2[4].toString()).append(", ");
                buf.append(soul2[5].toString()).append(")S2+\n(");
                buf.append(soul3[0].toString()).append(", ");
                buf.append(soul3[1].toString()).append(", ");
                buf.append(soul3[2].toString()).append(", ");
                buf.append(soul3[3].toString()).append(")S3+\n(");
                buf.append(soul4.toString()).append(")S4");
                return buf.toString();
        }
        /**
        * Returns the body (rank 0) of this supernumber.
        */
        public Complex getBody() {
                return body;
        }
        /**
        * Sets the body (rank 0) of this supernumber.
        */
        public void setBody(final Complex b) {
                body=b;
        }
        /**
        * Returns the a-number soul (rank 1) of this supernumber.
        */
        public Complex getSoul1(final int i) {
                return soul1[i];
        }
        /**
        * Sets the a-number soul (rank 1) of this supernumber.
        */
        public void setSoul1(final int i,final Complex s) {
                soul1[i]=s;
        }
        /**
        * Returns the c-number soul (rank 2) of this supernumber.
        */
        public Complex getSoul2(final int i) {
                return soul2[i];
        }
        /**
        * Sets the c-number soul (rank 2) of this supernumber.
        */
        public void setSoul2(final int i,final Complex s) {
                soul2[i]=s;
        }
        /**
        * Returns the a-number soul (rank 3) of this supernumber.
        */
        public Complex getSoul3(final int i) {
                return soul3[i];
        }
        /**
        * Sets the a-number soul (rank 3) of this supernumber.
        */
        public void setSoul3(final int i,final Complex s) {
                soul3[i]=s;
        }
        /**
        * Returns the c-number soul (rank 4) of this supernumber.
        */
        public Complex getSoul4() {
                return soul4;
        }
        /**
        * Sets the c-number soul (rank 4) of this supernumber.
        */
        public void setSoul4(final Complex s) {
                soul4=s;
        }
        /**
        * Returns the dimension.
        */
        public int dimension() {
                return 1<<N;
        }
        /**
        * Returns the negative of this number.
        */
        public AbelianGroup.Member negate() {
                Supernumber ans=new Supernumber();
                ans.body=(Complex)body.negate();
                ans.soul4=(Complex)soul4.negate();
                for(int i=0;i<N;i++) {
                        ans.soul1[i]=(Complex)soul1[i].negate();
                        ans.soul3[i]=(Complex)soul3[i].negate();
                }
                for(int i=0;i<soul2.length;i++)
                        ans.soul2[i]=(Complex)soul2[i].negate();
                return ans;
        }

// ADDITION

        /**
        * Returns the addition of this number and another.
        */
        public AbelianGroup.Member add(final AbelianGroup.Member z) {
                if(z instanceof Supernumber)
                        return add((Supernumber)z);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the addition of this supernumber and another.
        * @param z a supernumber
        */
        public Supernumber add(Supernumber z) {
                Supernumber ans=new Supernumber();
                ans.body=body.add(z.body);
                ans.soul4=soul4.add(z.soul4);
                for(int i=0;i<N;i++) {
                        ans.soul1[i]=soul1[i].add(z.soul1[i]);
                        ans.soul3[i]=soul3[i].add(z.soul3[i]);
                }
                for(int i=0;i<soul2.length;i++)
                        ans.soul2[i]=soul2[i].add(z.soul2[i]);
                return ans;
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this number and another.
        */
        public AbelianGroup.Member subtract(final AbelianGroup.Member z) {
                if(z instanceof Supernumber)
                        return subtract((Supernumber)z);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the subtraction of this supernumber and another.
        * @param z a supernumber
        */
        public Supernumber subtract(Supernumber z) {
                Supernumber ans=new Supernumber();
                ans.body=body.subtract(z.body);
                ans.soul4=soul4.subtract(z.soul4);
                for(int i=0;i<N;i++) {
                        ans.soul1[i]=soul1[i].subtract(z.soul1[i]);
                        ans.soul3[i]=soul3[i].subtract(z.soul3[i]);
                }
                for(int i=0;i<soul2.length;i++)
                        ans.soul2[i]=soul2[i].subtract(z.soul2[i]);
                return ans;
        }

// MULTIPLICATION

        /**
        * Returns the multiplication of this number and another.
        */
        public Ring.Member multiply(final Ring.Member z) {
                if(z instanceof Supernumber)
                        return multiply((Supernumber)z);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the multiplication of this supernumber and another.
        * @param z a supernumber
        */
        public Supernumber multiply(Supernumber z) {
                Supernumber ans=new Supernumber();
                ans.body=body.multiply(z.body);
                ans.soul4=(body.multiply(z.soul4))
                        .add(soul1[0].multiply(z.soul3[1])).subtract(soul1[1].multiply(z.soul3[2]))
                        .add(soul1[2].multiply(z.soul3[3])).subtract(soul1[3].multiply(z.soul3[0]))
                                .add(soul2[0].multiply(z.soul2[2])).subtract(soul2[1].multiply(z.soul2[3]))
                                .add(soul2[2].multiply(z.soul2[0])).subtract(soul2[3].multiply(z.soul2[1]))
                                .subtract(soul2[4].multiply(z.soul2[5])).subtract(soul2[5].multiply(z.soul2[4]))
                        .add(soul3[0].multiply(z.soul1[3])).subtract(soul3[3].multiply(z.soul1[2]))
                        .add(soul3[2].multiply(z.soul1[1])).subtract(soul3[1].multiply(z.soul1[0]))
                        .add(soul4.multiply(z.body));
                for(int i=0;i<N;i++)
                        ans.soul1[i]=body.multiply(z.soul1[i]).add(soul1[i].multiply(z.body));
                ans.soul2[0]=(body.multiply(z.soul2[0]))
                        .add(soul1[0].multiply(z.soul1[1])).subtract(soul1[1].multiply(z.soul1[0]))
                        .add(soul2[0].multiply(z.body));
                ans.soul2[1]=(body.multiply(z.soul2[1]))
                        .add(soul1[1].multiply(z.soul1[2])).subtract(soul1[2].multiply(z.soul1[1]))
                        .add(soul2[1].multiply(z.body));
                ans.soul2[2]=(body.multiply(z.soul2[2]))
                        .add(soul1[2].multiply(z.soul1[3])).subtract(soul1[3].multiply(z.soul1[2]))
                        .add(soul2[2].multiply(z.body));
                ans.soul2[3]=(body.multiply(z.soul2[3]))
                        .add(soul1[3].multiply(z.soul1[0])).subtract(soul1[0].multiply(z.soul1[3]))
                        .add(soul2[3].multiply(z.body));
                ans.soul2[4]=(body.multiply(z.soul2[4]))
                        .add(soul1[0].multiply(z.soul1[2])).subtract(soul1[2].multiply(z.soul1[0]))
                        .add(soul2[4].multiply(z.body));
                ans.soul2[5]=(body.multiply(z.soul2[5]))
                        .add(soul1[1].multiply(z.soul1[3])).subtract(soul1[3].multiply(z.soul1[1]))
                        .add(soul2[5].multiply(z.body));
                ans.soul3[0]=(body.multiply(z.soul3[0]))
                        .add(soul1[0].multiply(z.soul2[1])).subtract(soul1[1].multiply(z.soul2[4]))
                        .add(soul1[2].multiply(z.soul2[0])).add(soul2[0].multiply(z.soul1[2]))
                        .subtract(soul2[4].multiply(z.soul1[1])).add(soul2[1].multiply(z.soul1[0]))
                        .add(soul3[0].multiply(z.body));
                ans.soul3[1]=(body.multiply(z.soul3[1]))
                        .add(soul1[1].multiply(z.soul2[2])).subtract(soul1[2].multiply(z.soul2[5]))
                        .add(soul1[3].multiply(z.soul2[1])).add(soul2[1].multiply(z.soul1[3]))
                        .subtract(soul2[5].multiply(z.soul1[2])).add(soul2[2].multiply(z.soul1[1]))
                        .add(soul3[1].multiply(z.body));
                ans.soul3[2]=(body.multiply(z.soul3[2]))
                        .add(soul1[2].multiply(z.soul2[3])).add(soul1[3].multiply(z.soul2[4]))
                        .add(soul1[0].multiply(z.soul2[2])).add(soul2[2].multiply(z.soul1[0]))
                        .add(soul2[4].multiply(z.soul1[3])).add(soul2[3].multiply(z.soul1[2]))
                        .add(soul3[2].multiply(z.body));
                ans.soul3[3]=(body.multiply(z.soul3[3]))
                        .add(soul1[3].multiply(z.soul2[0])).add(soul1[0].multiply(z.soul2[5]))
                        .add(soul1[1].multiply(z.soul2[3])).add(soul2[3].multiply(z.soul1[1]))
                        .add(soul2[5].multiply(z.soul1[0])).add(soul2[0].multiply(z.soul1[3]))
                        .add(soul3[3].multiply(z.body));
                return ans;
        }
}

