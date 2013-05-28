package JSci.physics;

/**
* The ClassicalParticle2D class provides an object for
* encapsulating classical particles that live in 2D.
* @version 1.0
* @author Mark Hale
*/
public class ClassicalParticle2D extends Particle {
        /**
        * Mass.
        */
        protected double mass;
        /**
        * Position coordinates.
        */
        protected double x,y;
        /**
        * Velocity coordinates.
        */
        protected double vx,vy;
        /**
        * Constructs a classical particle.
        */
        public ClassicalParticle2D() {}
        public void setMass(double m) {
                mass=m;
        }
        public double getMass() {
                return mass;
        }
        public void setPosition(double xPos,double yPos) {
                x=xPos;
                y=yPos;
        }
        public void setXPosition(double xPos) {
                x=xPos;
        }
        public double getXPosition() {
                return x;
        }
        public void setYPosition(double yPos) {
                y=yPos;
        }
        public double getYPosition() {
                return y;
        }
        public void setVelocity(double xVel,double yVel) {
                vx=xVel;
                vy=yVel;
        }
        public double getXVelocity() {
                return vx;
        }
        public double getYVelocity() {
                return vy;
        }
        public double speed() {
                return Math.sqrt(vx*vx+vy*vy);
        }
        public void setMomentum(double xMom,double yMom) {
                vx=xMom/mass;
                vy=yMom/mass;
        }
        public double getXMomentum() {
                return mass*vx;
        }
        public double getYMomentum() {
                return mass*vy;
        }
        /**
        * Returns the kinetic energy.
        */
        public double energy() {
                return mass*(vx*vx+vy*vy)/2.0;
        }
        /**
        * Evolves the particle forward according to its kinematics.
        */
        public ClassicalParticle2D move(double dt) {
                x+=vx*dt;
                y+=vy*dt;
                return this;
        }
        public ClassicalParticle2D translate(double dt) {
                x+=vx*dt;
                y+=vy*dt;
                return this;
        }
        public ClassicalParticle2D accelerate(double ax,double ay,double dt) {
                vx+=ax*dt;
                vy+=ay*dt;
                x+=vx*dt;
                y+=vy*dt;
                return this;
        }
        public ClassicalParticle2D applyForce(double Fx,double Fy,double dt) {
                vx+=Fx*dt/mass;
                vy+=Fy*dt/mass;
                x+=vx*dt;
                y+=vy*dt;
                return this;
        }
        public ClassicalParticle2D applyForce(Force2D F,double dt) {
                return applyForce(F.getXComponent(),F.getYComponent(),dt);
        }
        public ClassicalParticle2D gravitate(ClassicalParticle2D p,double dt) {
                final double dx=p.x-x;
                final double dy=p.y-y;
                final double rr=dx*dx+dy*dy;
                final double r=Math.sqrt(rr);
                final double g=p.mass/rr;
                final double pg=mass/rr;
                vx-=g*dx*dt/r;
                vy-=g*dy*dt/r;
                x+=vx*dt;
                y+=vy*dt;
                p.vx+=pg*dx*dt/r;
                p.vy+=pg*dy*dt/r;
                p.x+=p.vx*dt;
                p.y+=p.vy*dt;
                return this;
        }
        /**
        * Collides this particle with another (elastic collision).
        * For the purposes of this method, the particles are
        * assumed to have a finite size.
        */
        public ClassicalParticle2D collide(ClassicalParticle2D p) {
                // transform to the rest frame of this body
                // then solve the 1D collision problem
                final double meanMass=(mass+p.mass)/2.0;
                final double deltaVx=p.vx-vx;
                final double deltaVy=p.vy-vy;
                vx+=p.mass*deltaVx/meanMass;
                vy+=p.mass*deltaVy/meanMass;
                p.vx-=mass*deltaVx/meanMass;
                p.vy-=mass*deltaVy/meanMass;
                return this;
        }
}

