package JSci.physics;

/**
* The ClassicalParticle class provides an object for
* encapsulating classical particles.
* @version 1.0
* @author Mark Hale
*/
public class ClassicalParticle extends Particle {
        protected double mass;
        protected double x[];
        protected double v[];
        /**
        * Constructs a classical particle.
        */
        public ClassicalParticle(int n) {
                x=new double[n];
                v=new double[n];
        }
        public void setMass(double m) {
                mass=m;
        }
        public double getMass() {
                return mass;
        }
        public void setPosition(double pos[]) {
                x=pos;
        }
        public double[] getPosition() {
                return x;
        }
        public void setVelocity(double vel[]) {
                v=vel;
        }
        public double[] getVelocity() {
                return v;
        }
        private double speedSqr() {
                double vv=v[0]*v[0];
                for(int i=1;i<v.length;i++)
                        vv+=v[i]*v[i];
                return vv;
        }
        public double speed() {
                return Math.sqrt(speedSqr());
        }
        public void setMomentum(double mom[]) {
                for(int i=0;i<v.length;i++)
                        v[i]=mom[i]/mass;
        }
        public double[] getMomentum() {
                double mom[]=new double[v.length];
                for(int i=0;i<v.length;i++)
                        mom[i]=mass*v[i];
                return mom;
        }
        public double energy() {
                return mass*speedSqr()/2.0;
        }
        public ClassicalParticle move(double dt) {
                for(int i=0;i<x.length;i++)
                        x[i]+=v[i]*dt;
                return this;
        }
        public ClassicalParticle accelerate(double a[],double dt) {
                for(int i=0;i<x.length;i++) {
                        v[i]+=a[i]*dt;
                        x[i]+=v[i]*dt;
                }
                return this;
        }
        public ClassicalParticle applyForce(double F[],double dt) {
                for(int i=0;i<x.length;i++) {
                        v[i]+=F[i]*dt/mass;
                        x[i]+=v[i]*dt;
                }
                return this;
        }
        /**
        * Collides this particle with another (elastic collision).
        * For the purposes of this method, the particles are
        * assumed to have a finite size.
        */
        public ClassicalParticle collide(ClassicalParticle p) {
                // transform to the rest frame of this body
                // then solve the 1D collision problem
                final double meanMass=(mass+p.mass)/2.0;
                double delta;
                for(int i=0;i<v.length;i++) {
                        delta=p.v[i]-v[i];
                        v[i]+=p.mass*delta/meanMass;
                        p.v[i]-=mass*delta/meanMass;
                }
                return this;
        }
}

