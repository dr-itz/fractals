package ch.sfdr.fractals.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;

import ch.sfdr.fractals.Version;
import ch.sfdr.fractals.fractals.ComplexEscapeFractal;
import ch.sfdr.fractals.fractals.FractalFactory;
import ch.sfdr.fractals.gui.component.AreaSelectionListener;
import ch.sfdr.fractals.gui.component.ColorMapFactory;
import ch.sfdr.fractals.gui.component.DisplayArea;
import ch.sfdr.fractals.gui.component.GBC;
import ch.sfdr.fractals.math.Scaler;

/**
 * Fractals main application window
 * @author S.Freihofer
 */
public class MainFrame
	extends JFrame
	implements AreaSelectionListener
{
	private static final long serialVersionUID = 1L;

	private DisplayArea displayArea;
	private JLabel lblX;
	private JLabel lblY;
	private JLabel lblPercent;
	private JLabel lblMilliSec;
	private JTabbedPane paneType;
	private JPanel pnlFractalsTab;
	private JComboBox cbFractals;
	private SpinnerNumberModel snmIterations;
	private SpinnerNumberModel snmThreads;
	private JPanel pnlColor;
	private JPanel pnlPathDraw;
	private JComboBox cbColor;
	private JComboBox cbPathColor;
	private JCheckBox chkAuto;
	private SpinnerNumberModel snmDelay;
	private JButton btnDraw;
	private JRadioButton rbtnZoom;
	private JRadioButton rbtnPath;

	private Scaler scaler;
	private ComplexEscapeFractal fractal;

	public MainFrame()
	{
		super(Version.getVersion());
		createGUI();
		initialize();
	}

	private void createGUI()
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel pane = new JPanel(new GridBagLayout());
		setContentPane(pane);

		Font bold = pane.getFont().deriveFont(Font.BOLD);

		JPanel pnlTop = new JPanel(new GridBagLayout());
		JPanel pnlBottom = new JPanel(new GridBagLayout());

		pane.add(pnlTop,				GBC.get(0, 0, 1, 1, 1.0, 1.0, 'b', "nw"));
		pane.add(pnlBottom,				GBC.get(0, 1, 1, 1, 'h', "nw"));

		// Panel Top
		displayArea = new DisplayArea(2);
		displayArea.setBackground(Color.BLACK);
		displayArea.setPreferredSize(new Dimension(350, 350));
		displayArea.setSelectionListner(this);

		JPanel pnlInfo = new JPanel(new GridBagLayout());
		pnlInfo.setBorder(BorderFactory.createTitledBorder("Info"));
		JPanel pnlClick = new JPanel(new GridBagLayout());
		pnlClick.setBorder(BorderFactory.createTitledBorder("Click Action"));
		btnDraw = new JButton("Draw");
		JButton btnReset = new JButton("Reset");
		Dimension btnDim = new Dimension(80, btnDraw.getMinimumSize().height);
		btnDraw.setPreferredSize(btnDim);
		btnReset.setPreferredSize(btnDim);

		pnlTop.add(displayArea,			GBC.get(0, 0, 1, 5, 1.0, 1.0, 'b', "nw"));
		pnlTop.add(pnlInfo,				GBC.get(1, 0, 1, 1));
		pnlTop.add(pnlClick,			GBC.get(1, 1, 1, 1));
		pnlTop.add(new JPanel(), 		GBC.get(1, 2, 1, 1, 0.0, 1.0, 'v', "nw"));
		pnlTop.add(btnDraw,				GBC.get(1, 3, 1, 1, 'n', "sw"));
		pnlTop.add(btnReset,			GBC.get(1, 4, 1, 1, 'n', "sw"));

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

		pnlInfo.add(lblVisible,			GBC.get(0, 0, 1, 1));
		pnlInfo.add(lblX,				GBC.get(0, 1, 1, 1));
		pnlInfo.add(lblY,				GBC.get(0, 2, 1, 1));
		pnlInfo.add(lblZoom,			GBC.get(0, 3, 1, 1));
		pnlInfo.add(lblPercent,			GBC.get(0, 4, 1, 1));
		pnlInfo.add(lblTime,			GBC.get(0, 5, 1, 1));
		pnlInfo.add(lblMilliSec,		GBC.get(0, 6, 1, 1));

		// Panel Click Action
		rbtnZoom = new JRadioButton("Zoom");
		rbtnPath = new JRadioButton("Draw path");

		ButtonGroup clickGroup = new ButtonGroup();
		clickGroup.add(rbtnZoom);
		clickGroup.add(rbtnPath);
		rbtnZoom.setSelected(true);

		pnlClick.add(rbtnZoom,			GBC.get(0, 0, 1, 1));
		pnlClick.add(rbtnPath,			GBC.get(0, 1, 1, 1));

		// Panel Bottom
		paneType = new JTabbedPane();
		pnlFractalsTab = new JPanel(new GridBagLayout());
		paneType.add("Fractals", pnlFractalsTab);

		pnlBottom.add(paneType,			GBC.get(0, 0, 1, 1, 1.0, 0.0, 'b', "nw"));

		// Panel FractalsTab
		JPanel pnlFractals = new JPanel(new GridBagLayout());
		JPanel pnlSettings = new JPanel(new GridBagLayout());

		pnlFractalsTab.add(pnlFractals,	GBC.get(0, 0, 1, 1, 0.5, 0.0, 'h', "nw"));
		pnlFractalsTab.add(pnlSettings,	GBC.get(1, 0, 1, 1, 0.5, 0.0, 'h', "nw"));

		// Panel Fractals
		cbFractals = new JComboBox(FractalFactory.getFractalFunctionsNames());
		JLabel lblIterations = new JLabel("Max. # of Iterations");
		snmIterations = new SpinnerNumberModel(200, 50, 500, 10);
		JSpinner spinIterations = new JSpinner(snmIterations);
		JLabel lblThreads = new JLabel("# Concurrent Threads");
		snmThreads = new SpinnerNumberModel(2, 1, 10, 1);
		JSpinner spinThreads = new JSpinner(snmThreads);

		pnlFractals.add(cbFractals,		GBC.get(0, 0, 2, 1, 0.5, 0.0, 'h', "nw"));
		pnlFractals.add(lblIterations,	GBC.get(0, 1, 1, 1));
		pnlFractals.add(spinIterations,	GBC.get(1, 1, 1, 1, "ne"));
		pnlFractals.add(lblThreads,		GBC.get(0, 2, 1, 1));
		pnlFractals.add(spinThreads,	GBC.get(1, 2, 1, 1, "ne"));

		// Panel Settings
		pnlColor = new JPanel(new GridBagLayout());
		pnlColor.setBorder(BorderFactory.createTitledBorder("Colorization"));
		pnlPathDraw = new JPanel(new GridBagLayout());
		pnlPathDraw.setBorder(BorderFactory.createTitledBorder("Path drawing"));

		pnlSettings.add(pnlColor,		GBC.get(0, 0, 1, 1, 1.0, 0.0, 'h', "nw"));
		pnlSettings.add(pnlPathDraw,	GBC.get(0, 1, 1, 1, "nw"));

		// Panel Colorization
		cbColor = new JComboBox(ColorMapFactory.getNames());

		pnlColor.add(cbColor,			GBC.get(0, 0, 1, 1, 1.0, 0.0, 'h', "nw"));

		// Panel Path Drawing
		cbPathColor = new JComboBox(new String[] {"Red"});
		chkAuto = new JCheckBox("Auto-cycle");
		JLabel lblDelay = new JLabel("Step delay (ms)");
		snmDelay = new SpinnerNumberModel(20, 0, 250, 10);
		JSpinner spinDelay = new JSpinner(snmDelay);

		pnlPathDraw.add(cbPathColor,	GBC.get(0, 0, 1, 1));
		pnlPathDraw.add(chkAuto,		GBC.get(1, 0, 1, 1));
		pnlPathDraw.add(lblDelay,		GBC.get(0, 1, 1, 1));
		pnlPathDraw.add(spinDelay,		GBC.get(1, 1, 1, 1));

		pack();
		setMinimumSize(getPreferredSize());

		// Drawing handler
		btnDraw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				displayArea.createImages();
				fractal.drawFractal(snmIterations.getNumber().intValue());
			}
		});

		// Reset handler
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				scaler.resetZoom();
				displayArea.createImages();
				fractal.drawFractal(snmIterations.getNumber().intValue());
			}
		});

		// Color scheme handler
		cbColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent)
			{
				fractal.setColorMap(ColorMapFactory.getMap(
					cbColor.getSelectedIndex()));
				displayArea.createImages();
				fractal.drawFractal(snmIterations.getNumber().intValue());
			}
		});

		// Click handler
		ActionListener clickActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				displayArea.setSelectionMode(rbtnZoom.isSelected());
			}
		};

		rbtnZoom.addActionListener(clickActionListener);
		rbtnPath.addActionListener(clickActionListener);

		MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (!rbtnPath.isSelected())
					return;
				if (e.getButton() != 1)
					return;

				fractal.drawOrbit(e.getX(), e.getY(),
					snmIterations.getNumber().intValue(), Color.RED,
					snmDelay.getNumber().intValue());
			}
		};
		displayArea.addMouseListener(ma);
	}

	private void initialize()
	{
		scaler = new Scaler();
		fractal = new ComplexEscapeFractal(displayArea, scaler,
			FractalFactory.getFractalFunction(cbFractals.getSelectedIndex()),
			ColorMapFactory.getMap(cbColor.getSelectedIndex()));

		// Simulate click to draw the fractal immediately
		btnDraw.doClick();
	}

	@Override
	public void areaSelected(Rectangle rect)
	{
		scaler.zoomIn(rect);
		fractal.drawFractal(snmIterations.getNumber().intValue());
	}
}
