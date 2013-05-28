package JSci.physics;

import JSci.GlobalSettings;
import JSci.physics.relativity.*;

/**
* The RelativisticParticle class provides an object for
* encapsulating relativistic particles.
* @version 1.0
* @author Mark Hale
*/
public abstract class RelativisticParticle extends Particle {
        /**
        * Constructs a relativistic particle.
        */
        public RelativisticParticle() {}
        /**
        * Rest mass.
        */
        public abstract double restMass();
        /**
        * Position 4-vector.
        */
        public Rank1Tensor position=new Rank1Tensor(0.0,0.0,0.0,0.0);
        /**
        * Momentum 4-vector.
        */
        public Rank1Tensor momentum=new Rank1Tensor(0.0,0.0,0.0,0.0);
        /**
        * Compares two particles for equality.
        * @param p a relativistic particle
        */
        public boolean equals(Object p) {
                return (p!=null) && (p instanceof RelativisticParticle) &&
                        (position.equals(((RelativisticParticle)p).position)) &&
                        (momentum.equals(((RelativisticParticle)p).momentum)) &&
                        (Math.abs(restMass()-((RelativisticParticle)p).restMass())<=GlobalSettings.ZERO_TOL);
        }
}

