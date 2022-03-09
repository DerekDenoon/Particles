package particles2;

import java.awt.EventQueue;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ParticleWrapper extends JFrame {

	public final int PANEL_SIZE = 600; //1500 for surface, 640 otherwise
	public final int BORDER_SPACE = 30;
	public final int NUM_PARTICLES = 30;
	
	public ParticleWrapper() {
        setSize(PANEL_SIZE, PANEL_SIZE + BORDER_SPACE);
		add(new ParticlePanel(PANEL_SIZE, PANEL_SIZE, NUM_PARTICLES));
        setResizable(false);
        setTitle("Drawing particles");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ParticleWrapper go = new ParticleWrapper();
                go.setVisible(true);
            }
        });
	}

}
