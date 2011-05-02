package ch.sfdr.fractals.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import ch.sfdr.fractals.gui.component.DisplayArea;
import ch.sfdr.fractals.gui.component.GBC;


/**
 * A useless dummy GUI used for testing of other components.
 * Provides absolutely no user experience and is a horrible hack.
 * => Will go away soon.
 * @author D.Ritz
 */
public class Dummy
	extends JFrame
{
	private static final long serialVersionUID = 1L;

	private DisplayArea displayArea;
	private JButton startBtn;

	public Dummy()
	{
		super("Dummy GUI for testing");

		setMinimumSize(new Dimension(600, 500));

		displayArea = new DisplayArea();
		displayArea.setMinimumSize(new Dimension(590, 450));
		displayArea.setBackground(Color.BLACK);
		startBtn = new JButton("Start");

		JPanel p = new JPanel(new GridBagLayout());
		p.add(displayArea,	GBC.get(0, 0, 1, 1, 1.0, 1.0, 'b', "nw"));
		p.add(startBtn,		GBC.get(0, 1, 1, 1, 'h', "nw"));

		setContentPane(p);

		// handle windowClosing event
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				dispose();
			}
		});

		// add handler for button
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				displayArea.createImages();

				/*
				 * This shows to independently draw lines (a red and a black
				 * one) with transparency.
				 */
				Image img = displayArea.createImage();
				Graphics g = img.getGraphics();
				g.setColor(Color.BLACK);
				g.drawLine(0, 0, displayArea.getWidth(), displayArea.getHeight());
				displayArea.updateImage(img);

				Image img2 = displayArea.createImage();
				Graphics g2 = img2.getGraphics();
				g2.setColor(Color.RED);
				g2.drawLine(0, displayArea.getHeight(), displayArea.getWidth(), 0);
				displayArea.updateImage(img2);
			}
		});
	}

	/**
	 * a main() method to make it startable
	 * @param args
	 */
	public static void main(String[] args)
	{
		try {
			// the java LAF is just ugly...
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Error setting system LookAndFeel: " + e);
		}
		Dummy myself = new Dummy();
		myself.setVisible(true);
	}
}
