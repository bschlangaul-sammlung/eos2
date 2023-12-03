package de.lathanda.eos.robot.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

import de.lathanda.eos.robot.World;
import static de.lathanda.eos.base.icons.Icons.*;
import static de.lathanda.eos.robot.text.Text.TEXT;
/**
 * Fenster für die Welt.
 * 
 *
 * @author Peter (Lathanda) Schneider
 */
public class WorldFrame extends JFrame implements WindowListener {
	private static final long serialVersionUID = 5001020279686211090L;
	
    Component view;
	public WorldFrame(World world) {
		super(TEXT.getString("Robot.Title"));
        this.setIconImage(LOGO);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        
        if (view == null) {
        	try {
        		view = new WorldPanelOpenGLNoShader(world);
        	} catch (Throwable t) {}
        }
        if (view == null) {
        	try {
        		view = new WorldPanelSoftware(world);
        	} catch (Throwable t) {}
        }
        
        setLocation(0, 0);
    	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    	setSize(new Dimension(screen.width/2, screen.height/2));
    	getContentPane().add(view, BorderLayout.CENTER);
	}
	@Override
	public void windowActivated(WindowEvent e) { }
	@Override
	public void windowClosed(WindowEvent e) { }
	@Override
	public void windowClosing(WindowEvent e) {
		setState(ICONIFIED);		
	}
	@Override
	public void windowDeactivated(WindowEvent e) { }
	@Override
	public void windowDeiconified(WindowEvent e) { }
	@Override
	public void windowIconified(WindowEvent e) { }
	@Override
	public void windowOpened(WindowEvent e) { }
}