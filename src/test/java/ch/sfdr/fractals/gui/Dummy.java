package ch.sfdr.fractals.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import ch.sfdr.fractals.gui.component.AreaSelectionListener;
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
	implements AreaSelectionListener
{
	private static final long serialVersionUID = 1L;

	private DisplayArea displayArea;
	private JButton startBtn;

	public Dummy()
	{
		super("Dummy GUI for testing");

		setMinimumSize(new Dimension(600, 500));

		displayArea = new DisplayArea(2);
		displayArea.setMinimumSize(new Dimension(590, 450));
		displayArea.setBackground(Color.BLACK);
		displayArea.setSelectionListner(this);
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

				long start = System.currentTimeMillis();

				/*
				 * This shows to how independently draw lines (a red and a black
				 * one) with transparency and layers.
				 */
				Image img = displayArea.createImage();
				Graphics g = img.getGraphics();
				g.setColor(Color.BLACK);
				g.drawLine(0, 0, displayArea.getWidth(), displayArea.getHeight());
				displayArea.updateImage(img, 0);

				Image img2 = displayArea.createImage();
				Graphics g2 = img2.getGraphics();
				g2.setColor(Color.RED);
				g2.drawLine(0, displayArea.getHeight(), displayArea.getWidth(), 0);
				displayArea.updateImage(img2, 0);

				// draw something on second layer
				Image img3 = displayArea.createImage();
				Graphics g3 = img3.getGraphics();
				g3.setColor(Color.BLUE);
				g3.drawLine(0, displayArea.getHeight()/2, displayArea.getWidth()/2, 0);
				displayArea.updateImage(img3, 1);

				// draw something on the layer below
				g.setColor(Color.GREEN);
				g.fillRect(
					displayArea.getWidth() / 8, displayArea.getHeight() / 8,
					3 * displayArea.getWidth() / 8, 3 * displayArea.getHeight() / 8);
				displayArea.updateImage(img, 0);

				long dur = System.currentTimeMillis() - start;
				System.out.println(dur + "ms");
			}
		});
	}


	@Override
	public void areaSelected(Rectangle rect)
	{
		System.out.println(rect);
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
