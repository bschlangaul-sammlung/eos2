package de.lathanda.eos.robot.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowWorldEditor implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		new WorldEditor().setVisible(true);		
	}
}
