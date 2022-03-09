package particles2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.lang.reflect.Array;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class ParticlePanel extends JPanel implements ActionListener {

	private Particle[] particles;
	private Timer timer;
	
	private final double WALL_FRICTION = -0.96; //percent of velocity that remains after bouncing off a wall
	private final double FORCE_STRENGTH = 13.9; //nerfs speed
	private final double CLOSE_TOL = 10.0;	   //how close particles get before they are considered to have collided

	public ParticlePanel(int xDim, int yDim, int numPart) {
        setBackground(Color.BLACK);
		//addMouseListener(new MAdapter());
		//addMouseMotionListener(new MAdapter());
		setFocusable(true);
		setDoubleBuffered(true);

		particles = new Particle[numPart];
		timer = new Timer(1, this);
		timer.start();
		double totalSum = 0;
		//initialize particles here
		for (int i = 0; i < numPart; i++) {
			Point2D.Double loc = new Point2D.Double(Math.random() * 600,Math.random() * 600);
			double mag = Math.random() ;
			particles[i] = new Particle(loc.x,loc.y,Math.random(),Math.random(),mag);
			particles[i].activate(); //and lastly, activate them
		}
	}
	
    public void paintComponent(Graphics g)  	                 // draw graphics in the panel
    {
        super.paintComponent(g);                              	 // call superclass to make panel display correctly
		drawParticles(g);
    }

    public void drawParticles(Graphics g) {		//pretty short this time!
    	for (Particle p : particles) {
    		p.draw(g);
    	}
    }
    
    //updates ACTIVE particles by checking if their velocity would send them past the edge of the viewing window
    //and then adjusting their velocity or position appropriately if necessary
    //and finally using addVel to actually relocate the particle according to its new location and velocity
    public void updateParticles() {
		boolean xInWindow;
		boolean yInWindow;

		Point2D.Double newLoc;
    	for (Particle p : particles) {
    		if (p.isActive()){
    			newLoc = new Point2D.Double(p.getLoc().x + p.getVel().x,p.getLoc().y + p.getVel().y);
				xInWindow = Math.abs(newLoc.x - 300) < 300;
				yInWindow = Math.abs(newLoc.y - 300) < 300;
				if(!xInWindow || !yInWindow){
					if (!xInWindow && !yInWindow){
						p.setVel( WALL_FRICTION * p.getVel().x, WALL_FRICTION * p.getVel().y);
						p.setLoc(newLoc.x,newLoc.y);
						// x out
						// y out
					}else if (xInWindow){
						p.setVel(p.getVel().x, WALL_FRICTION * p.getVel().y);
						p.setLoc(newLoc.x,newLoc.y);
						// x in
						// y out
					}else {
						p.setVel( WALL_FRICTION * p.getVel().x,p.getVel().y);
						p.setLoc(newLoc.x,newLoc.y);
//						if (newLoc.x - 600 > 0){
//							p.setLoc(600 - (newLoc.x - 600),newLoc.y);
//						}else{
//							p.setLoc(-newLoc.x,newLoc.y);
//						}
						// x out
						// y in
					}

				}else{

				}

				p.addVel();



			}


    	}
    }

    //given two particles and the square of their distance, return the appropriate force calculation
    public double forceMag(Particle p1, Particle p2, double distSq) {
		return (p1.getMag() * p2.getMag()) / distSq * FORCE_STRENGTH;
    }
    
    //for gravitational simulation, pick a particle to make more massive, deactivate the other one
    //for electrical simulation, pick the stronger particle, weaken it by the other's magnitude, deactivate the other one
    //for electrical simulation, if the two particles are equal in magnitude, deactivate both of them
    //for both, the remaining (if there is one) particle's velocity needs to be the sum of the two particle's velocities
    private void mergeParticles(Particle p1, Particle p2) {
		double newMag = p1.getMag() + p2.getMag();
		p2.deactivate();
		p1.setVel(
				((p1.getVel().x * p1.getMag()) + (p2.getVel().x * p2.getMag()))/(newMag),
				((p1.getVel().y * p1.getMag()) + (p2.getVel().y * p2.getMag()))/(newMag));
		p1.setMag(newMag);
    }

    //the heart of the simulation - using the force calculation to inform every particle's new velocity
    //uses a vector sum of the effect of EVERY OTHER ACTIVE particle to calculate the new change in velocity (acceleration)
    //and then actually adjusts every particle's velocity based on that calculation
    public void updateVelocities() {
    	double x = 0, y = 0;		//the vector sum force calculation variables
    	double fm = 0;				//the force magnitude
    	double angle = 0;			//the bearing from one particle to another particle, in radians
    	double distSq= 0;			//the square of the distance between particles p1 and p2 (for loop variable names)
    	//lots of nested for and if statements ahead!
    	//hint 1: you should calculate distSq before calculating forceMag since you may want/need to merge particles instead
    	//hint 2: the following three lines of code need to be in your code, in one contiguous block
    	//		angle = Math.atan2(p2.getLoc().y - p1.getLoc().y, p2.getLoc().x - p1.getLoc().x); //sweet, sweet trigonometry
    	//		x += fm * Math.cos(angle);		//the horizontal component of the force vector
    	//		y += fm * Math.sin(angle);    	//the vertical component of the force vector					
    	
    	//your code goes here

		Particle p1;
		Particle p2;
		double distance;

		for (int i = 0; i < particles.length; i++) {
			p1 = particles[i];
			if (p1.isActive()) {
				x = 0;
				y = 0;
				for (int j = 0; j < particles.length; j++) {
					p2 = particles[j];
					if (i != j && p2.isActive()) {
						distance = p1.getLoc().distance(p2.getLoc());
						if (distance < CLOSE_TOL) {
							mergeParticles(p1, p2);
						} else {
							distSq = Math.pow(distance, 2);
							fm = forceMag(p1, p2, distSq);
							angle = Math.atan2(p2.getLoc().y - p1.getLoc().y, p2.getLoc().x - p1.getLoc().x); //sweet, sweet trigonometry
							x += fm * Math.cos(angle);        //the horizontal component of the force vector
							y += fm * Math.sin(angle);//the vertical component of the force vector

							p1.setVel(p1.getVel().x + x/p1.getMag(),p1.getVel().y + y/p1.getMag());

						}
					}
				}
			}
		}
    }
    
	@Override
	public void actionPerformed(ActionEvent arg0) {
		updateVelocities();
		updateParticles();
		repaint();
	}
}
