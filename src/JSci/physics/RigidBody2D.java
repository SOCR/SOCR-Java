package JSci.physics;

import JSci.maths.NumericalConstants;

/**
* The RigidBody2D class provides an object for
* encapsulating rigid bodies that live in 2D.
* @version 1.0
* @author Mark Hale
*/
public class RigidBody2D extends ClassicalParticle2D {
        /**
        * Moment of inertia.
        */
        protected double angMass;
        /**
        * Angle (orientation).
        */
        protected double ang;
        /**
        * Angular velocity.
        */
        protected double angVel;
        /**
        * Constructs a rigid body.
        */
        public RigidBody2D() {}
        public void setMomentOfInertia(double MoI) {
                angMass=MoI;
        }
        public double getMomentOfInertia() {
                return angMass;
        }
        /**
        * Sets the angle (orientation) of this body.
        * @param angle an angle in radians
        */
        public void setAngle(double angle) {
                ang=angle;
        }
        /**
        * Returns the angle (orientation) of this body.
        * @return an angle in radians
        */
        public double getAngle() {
                return ang;
        }
        public void setAngularVelocity(double angleVel) {
                angVel=angleVel;
        }
        public double getAngularVelocity() {
                return angVel;
        }
        public void setAngularMomentum(double angleMom) {
                angVel=angleMom/angMass;
        }
        public double getAngularMomentum() {
                return angMass*angVel;
        }
        /**
        * Returns the kinetic and rotational energy.
        */
        public double energy() {
                return (mass*(vx*vx+vy*vy)+angMass*angVel*angVel)/2.0;
        }
        /**
        * Evolves the particle forward according to its kinematics.
        */
        public ClassicalParticle2D move(double dt) {
                x+=vx*dt;
                y+=vy*dt;
                ang+=angVel*dt;
                if(ang>NumericalConstants.TWO_PI)
                        ang-=NumericalConstants.TWO_PI;
                return this;
        }
        public RigidBody2D rotate(double dt) {
                ang+=angVel*dt;
                if(ang>NumericalConstants.TWO_PI)
                        ang-=NumericalConstants.TWO_PI;
                return this;
        }
        public RigidBody2D angularAccelerate(double a,double dt) {
                angVel+=a*dt;
                ang+=angVel*dt;
                if(ang>NumericalConstants.TWO_PI)
                        ang-=NumericalConstants.TWO_PI;
                return this;
        }
        public RigidBody2D applyTorque(double T,double dt) {
                angVel+=T*dt/angMass;
                ang+=angVel*dt;
                if(ang>NumericalConstants.TWO_PI)
                        ang-=NumericalConstants.TWO_PI;
                return this;
        }
        public ClassicalParticle2D applyForce(Force2D F,double dt) {
                super.applyForce(F,dt);
                return applyTorque(F.getTorque(),dt);
        }
        /**
        * Collides this particle with another.
        * @param e coefficient of restitution
        */
        public RigidBody2D collide(RigidBody2D p,double e) {
                // transform to the rest frame of this body
                // then solve the 1D collision problem
                final double meanMass=(mass+p.mass)/(e+1.0);
                final double deltaVx=p.vx-vx;
                final double deltaVy=p.vy-vy;
                vx+=p.mass*deltaVx/meanMass;
                vy+=p.mass*deltaVy/meanMass;
                p.vx-=mass*deltaVx/meanMass;
                p.vy-=mass*deltaVy/meanMass;
                return this;
        }
        /**
        * Collides this particle with another.
        * @param e coefficient of restitution
        */
        public RigidBody2D angularCollide(RigidBody2D p,double e) {
                final double meanMass=(angMass+p.angMass)/(e+1.0);
                final double delta=p.angVel-angVel;
                angVel+=p.angMass*delta/meanMass;
                p.angVel-=angMass*delta/meanMass;
                return this;
        }
}

