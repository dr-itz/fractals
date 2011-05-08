package ch.sfdr.fractals.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ch.sfdr.fractals.gui.component.DisplayArea;
import ch.sfdr.fractals.gui.component.GBC;

/**
 * Fractals main application window
 * @author S.Freihofer
 */
public class MainFrame
	extends JFrame
{
	private static final long serialVersionUID = 1L;

	private DisplayArea displayArea;
	private JLabel lblX;
	private JLabel lblY;
	private JLabel lblPercent;
	private JLabel lblMilliSec;


	public MainFrame()
	{
		super("Fractals");
		createGUI();
	}

	private void createGUI()
	{
		setMinimumSize(new Dimension(600, 500));
		JPanel pane = new JPanel(new GridBagLayout());
		setContentPane(pane);

		Font bold = pane.getFont().deriveFont(Font.BOLD);

		JPanel pnlTop = new JPanel(new GridBagLayout());
		JPanel pnlBottom = new JPanel(new GridBagLayout());

		pane.add(pnlTop,			GBC.get(0, 0, 1, 1, 1.0, 1.0, 'b', "nw"));
		pane.add(pnlBottom,			GBC.get(0, 1, 1, 1, 'h', "nw"));

		// Panel Top
		displayArea = new DisplayArea(1);
		displayArea.setBackground(Color.BLACK);
		JLabel lblInfo = new JLabel("Info");
		lblInfo.setFont(bold);
		JPanel pnlInfo = new JPanel(new GridBagLayout());
		pnlInfo.setBorder(BorderFactory.createLoweredBevelBorder());
		JLabel lblClick = new JLabel("Click Action");
		lblClick.setFont(bold);
		JPanel pnlClick = new JPanel(new GridBagLayout());
		pnlClick.setBorder(BorderFactory.createLoweredBevelBorder());
		JButton btnDraw = new JButton("Draw");
		JButton btnReset = new JButton("Reset");

		pnlTop.add(displayArea,		GBC.get(0, 0, 1, 7, 1.0, 1.0, 'b', "nw"));
		pnlTop.add(lblInfo,			GBC.get(1, 0, 1, 1));
		pnlTop.add(pnlInfo,			GBC.get(1, 1, 1, 1));
		pnlTop.add(lblClick,		GBC.get(1, 2, 1, 1));
		pnlTop.add(pnlClick,		GBC.get(1, 3, 1, 1));
		pnlTop.add(new JPanel(), 	GBC.get(1, 4, 1, 1, 0.0, 1.0, 'v', "nw"));
		pnlTop.add(btnDraw,			GBC.get(1, 5, 1, 1, 'n', "se"));
		pnlTop.add(btnReset,		GBC.get(1, 6, 1, 1, 'n', "se"));

		// Panel Info
		JLabel lblVisible = new JLabel("Visible Area");
		lblVisible.setFont(bold);
		lblX = new JLabel("x blub");
		lblY = new JLabel("y blub");
		JLabel lblZoom = new JLabel("Zoom");
		lblZoom.setFont(bold);
		lblPercent = new JLabel("100%");
		JLabel lblTime = new JLabel("Time to draw");
		lblTime.setFont(bold);
		lblMilliSec = new JLabel("281ms");

		pnlInfo.add(lblVisible,		GBC.get(0, 0, 1, 1));
		pnlInfo.add(lblX,			GBC.get(0, 1, 1, 1));
		pnlInfo.add(lblY,			GBC.get(0, 2, 1, 1));
		pnlInfo.add(lblZoom,		GBC.get(0, 3, 1, 1));
		pnlInfo.add(lblPercent,		GBC.get(0, 4, 1, 1));
		pnlInfo.add(lblTime,		GBC.get(0, 5, 1, 1));
		pnlInfo.add(lblMilliSec,	GBC.get(0, 6, 1, 1));

		// Panel Click Action
		JRadioButton rbtnZoom = new JRadioButton("Zoom");
		JRadioButton rbtnPath = new JRadioButton("Draw path");

		ButtonGroup clickGroup = new ButtonGroup();
		clickGroup.add(rbtnZoom);
		clickGroup.add(rbtnPath);
		rbtnZoom.setSelected(true);

		pnlClick.add(rbtnZoom,		GBC.get(0, 0, 1, 1));
		pnlClick.add(rbtnPath,		GBC.get(0, 1, 1, 1));

		// Panel Bottom

	}
}
