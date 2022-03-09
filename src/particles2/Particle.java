package particles2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class Particle {

	public static int drawSize = 20; //the size of the particles, relevant for drawing

	//private variables
	private Point2D.Double location;
	private Point2D.Double velocity;
	private double magnitude;
	private boolean active;
	//constructors

	public Particle() {
		//default
		this.location = new Point2D.Double(0.0,0.0);
		this.velocity = new Point2D.Double(0.0,0.0);
		this.magnitude = 0.0;
		this.active = false;
	}

	public Particle(Point2D.Double location, Point2D.Double velocity, double magnitude, boolean active) {
		// Points as Points
		this.location = location;
		this.velocity = velocity;
		this.magnitude = magnitude;
		this.active = active;
	}

	public Particle(double xloc, double yloc, double xvel, double yvel,double magnitude){
		this.location = new Point2D.Double(xloc,yloc);
		this.velocity = new Point2D.Double(xvel,yvel);
		this.magnitude = magnitude;
		this.active = true;
	}

	//get and set pairs for private variables


	public Point2D.Double getLoc() {
		return location;
	}

	public void setLoc(double xloc,double yloc) {
		this.location = new Point2D.Double(xloc,yloc);
	}

	public Point2D.Double getVel() {
		return velocity;
	}

	public void setVel(double xvel,double yvel) {
		this.velocity = new Point2D.Double(xvel,yvel);
	}

	public void addVel(){
		location.setLocation(location.x + velocity.x, location.y + velocity.y);
	}



	public double getMag() {
		return magnitude;
	}

	public void setMag(double magnitude) {
		this.magnitude = magnitude;
	}

	public boolean isActive() {
		return active;
	}

	public void activate(){
		this.active = true;
	}

	public void deactivate(){
		this.active = false;
	}

	//the draw function, with errors until you write the above stuff
	public void draw(Graphics g) {
		if (isActive()) {
			int primColor = Math.min(255, 160 + (int)(Math.abs(getMag() * 3)));
			int secColor = Math.max(0, 60 - 5 * (int)(Math.abs(getMag())));
    		if (getMag() > 0) {
    	    	g.setColor(new Color (primColor, secColor, 0));    			
    		} else {
    	    	g.setColor(new Color (0, secColor, primColor));    			
    		}
    		//centering the circle at getLoc requires a tiny bit of math
    		g.fillOval((int)(getLoc().x - drawSize / 2), (int)(getLoc().y - drawSize / 2), drawSize, drawSize);
		}
	}

	//addVel updates your location variable
	
}
