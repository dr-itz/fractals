package ch.sfdr.fractals.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import ch.sfdr.fractals.Version;
import ch.sfdr.fractals.fractals.ComplexEscapeFractal;
import ch.sfdr.fractals.fractals.FractalFactory;
import ch.sfdr.fractals.fractals.StatisticsObserver;
import ch.sfdr.fractals.fractals.StepFractalFunction;
import ch.sfdr.fractals.fractals.StepFractalFunctionWithConst;
import ch.sfdr.fractals.gui.component.AreaSelectionListener;
import ch.sfdr.fractals.gui.component.ColorMapFactory;
import ch.sfdr.fractals.gui.component.ColorSelection;
import ch.sfdr.fractals.gui.component.DisplayArea;
import ch.sfdr.fractals.gui.component.GBC;
import ch.sfdr.fractals.math.ComplexNumber;
import ch.sfdr.fractals.math.Scaler;

/**
 * Fractals main application window
 * @author S.Freihofer
 */
public class MainFrame
	extends JFrame
	implements AreaSelectionListener, StatisticsObserver
{
	private static final long serialVersionUID = 1L;

	// the displaying component
	private DisplayArea displayArea;

	// info panel
	private JLabel lblX;
	private JLabel lblY;
	private JLabel lblZoomValue;
	private JLabel lblMilliSec;

	// bottom pane
	private JTabbedPane paneType;

	// fractals tab
	private JPanel pnlFractalsTab;
	private JComboBox cbFractals;
	private SpinnerNumberModel snmIterations;
	private SpinnerNumberModel snmThreads;
	private JPanel pnlColor;
	private JPanel pnlPathDraw;
	private JComboBox cbColor;
	private JComboBox cbSetColor;
	private JComboBox cbPathColor;
	private JCheckBox chkAuto;
	private SpinnerNumberModel snmDelay;
	private JButton btnDraw;
	private JButton btnReset;
	private JRadioButton rbtnZoom;
	private JRadioButton rbtnPath;
	private JLabel lblStepCount;
	private JButton btnClearOrbits;
	// constant panel
	private JPanel pnlConst;
	private JFormattedTextField ftfConstReal;
	private JFormattedTextField ftfConstImag;
	private DefaultFormatterFactory fmtFactory;

	// scaler and fractal core
	private Scaler scaler;
	private ComplexEscapeFractal fractal;

	// timer for delayed drawing on resize
	private Timer delayedDrawTimer = new Timer();
	private TimerTask delayedDrawTask;

	// formatting
	private static DecimalFormat decimalFmt =
		new DecimalFormat("###,###,###,###");
	private static DecimalFormat doubleFmt =
		new DecimalFormat("0.0###############");

	public MainFrame()
	{
		super(Version.getVersion());
		createGUI();
		initialize();
	}

	private void createGUI()
	{
		// the formatting used in all formatted text fields
		DefaultFormatter fmt = new NumberFormatter(
			new DecimalFormat("#0.0#################"));
		fmt.setValueClass(Double.class);
		fmt.setAllowsInvalid(false);
		fmtFactory = new DefaultFormatterFactory(fmt, fmt, fmt);

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
		displayArea.setPreferredSize(new Dimension(600, 400));
		displayArea.setSelectionListner(this);

		JPanel pnlInfo = new JPanel(new GridBagLayout());
		pnlInfo.setBorder(BorderFactory.createTitledBorder("Info"));
		JPanel pnlClick = new JPanel(new GridBagLayout());
		pnlClick.setBorder(BorderFactory.createTitledBorder("Click Action"));
		btnDraw = new JButton("Draw");
		btnReset = new JButton("Reset");
		Dimension btnDim = new Dimension(80, btnDraw.getMinimumSize().height);
		btnDraw.setPreferredSize(btnDim);
		btnReset.setPreferredSize(btnDim);

		pnlTop.add(displayArea,			GBC.get(0, 0, 1, 5, 1.0, 1.0, 'b', "nw"));
		pnlTop.add(pnlInfo,				GBC.get(1, 0, 1, 1));
		pnlTop.add(pnlClick,			GBC.get(1, 1, 1, 1, 'h'));
		pnlTop.add(new JPanel(), 		GBC.get(1, 2, 1, 1, 0.0, 1.0, 'v', "nw"));
		pnlTop.add(btnDraw,				GBC.get(1, 3, 1, 1, 'n', "sw"));
		pnlTop.add(btnReset,			GBC.get(1, 4, 1, 1, 'n', "sw"));

		// Panel Info
		JLabel lblVisible = new JLabel("Coordinate                  ");
		lblVisible.setFont(bold);
		lblX = new JLabel("x blub");
		lblY = new JLabel("y blub");
		JLabel lblZoom = new JLabel("Zoom");
		lblZoom.setFont(bold);
		lblZoomValue = new JLabel("100%");
		JLabel lblTime = new JLabel("Time to draw");
		lblTime.setFont(bold);
		lblMilliSec = new JLabel("281ms");
		JLabel lblSteps = new JLabel("Steps calculated");
		lblSteps.setFont(bold);
		lblStepCount = new JLabel("0");

		pnlInfo.add(lblVisible,			GBC.get(0, 0, 1, 1));
		pnlInfo.add(lblX,				GBC.get(0, 1, 1, 1));
		pnlInfo.add(lblY,				GBC.get(0, 2, 1, 1));
		pnlInfo.add(lblZoom,			GBC.get(0, 3, 1, 1));
		pnlInfo.add(lblZoomValue,		GBC.get(0, 4, 1, 1));
		pnlInfo.add(lblTime,			GBC.get(0, 5, 1, 1));
		pnlInfo.add(lblMilliSec,		GBC.get(0, 6, 1, 1));
		pnlInfo.add(lblSteps,			GBC.get(0, 7, 1, 1));
		pnlInfo.add(lblStepCount,		GBC.get(0, 8, 1, 1));

		// Panel Click Action
		rbtnZoom = new JRadioButton("Zoom");
		rbtnPath = new JRadioButton("Draw path");

		ButtonGroup clickGroup = new ButtonGroup();
		clickGroup.add(rbtnZoom);
		clickGroup.add(rbtnPath);
		rbtnZoom.setSelected(true);

		pnlClick.add(rbtnZoom,			GBC.get(0, 0, 1, 1, 1.0, 0.0, 'h', "nw"));
		pnlClick.add(rbtnPath,			GBC.get(0, 1, 1, 1, "nw"));

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

		pnlConst = new JPanel(new GridBagLayout());
		pnlConst.setVisible(false);
		JLabel lblConstReal = new JLabel("Const Real:");
		ftfConstReal = createDoubleTextField();
		JLabel lblConstImag = new JLabel("Const Imag:");
		ftfConstImag = createDoubleTextField();
		pnlConst.add(lblConstReal,	GBC.get(0, 1, 1, 1, 0.5, 0.0, 'v', "nw"));
		pnlConst.add(ftfConstReal,	GBC.get(1, 1, 1, 1, 0.5, 0.0, 'h', "ne"));
		pnlConst.add(lblConstImag,	GBC.get(0, 2, 1, 1));
		pnlConst.add(ftfConstImag,	GBC.get(1, 2, 1, 1, 0.5, 0.0, 'h', "ne"));

		JLabel lblIterations = new JLabel("Max. # of Iterations");
		snmIterations = new SpinnerNumberModel(200, 50, 500, 10);
		JSpinner spinIterations = new JSpinner(snmIterations);
		JLabel lblThreads = new JLabel("# Concurrent Threads");
		snmThreads = new SpinnerNumberModel(2, 1, 10, 1);
		JSpinner spinThreads = new JSpinner(snmThreads);

		pnlFractals.add(cbFractals,		GBC.get(0, 0, 2, 1, 0.5, 0.0, 'h', "nw"));
		pnlFractals.add(pnlConst,		GBC.get(0, 1, 2, 1, 'h', "nw"));
		pnlFractals.add(lblIterations,	GBC.get(0, 2, 1, 1));
		pnlFractals.add(spinIterations,	GBC.get(1, 2, 1, 1, "ne"));
		pnlFractals.add(lblThreads,		GBC.get(0, 3, 1, 1));
		pnlFractals.add(spinThreads,	GBC.get(1, 3, 1, 1, "ne"));

		// Panel Settings
		pnlColor = new JPanel(new GridBagLayout());
		pnlColor.setBorder(BorderFactory.createTitledBorder("Colorization"));
		pnlPathDraw = new JPanel(new GridBagLayout());
		pnlPathDraw.setBorder(BorderFactory.createTitledBorder("Path drawing"));

		pnlSettings.add(pnlColor,		GBC.get(0, 0, 1, 1, 1.0, 0.0, 'h', "nw"));
		pnlSettings.add(pnlPathDraw,	GBC.get(0, 1, 1, 1, "nw"));

		// Panel Colorization
		cbColor = new JComboBox(ColorMapFactory.getNames());
		JLabel lblSetColor = new JLabel("Set color");
		cbSetColor = new JComboBox(ColorSelection.getNames());
		cbSetColor.setSelectedIndex(cbSetColor.getItemCount() - 1);

		pnlColor.add(cbColor,			GBC.get(0, 0, 1, 1, 1.0, 0.0, 'h', "nw"));
		pnlColor.add(lblSetColor,		GBC.get(1, 0, 1, 1, 'v', "nw"));
		pnlColor.add(cbSetColor,		GBC.get(2, 0, 1, 1, 1.0, 0.0, 'h', "nw"));

		// Panel Path Drawing
		cbPathColor = new JComboBox(ColorSelection.getNames());
		chkAuto = new JCheckBox("Auto-cycle");
		chkAuto.setSelected(true);
		btnClearOrbits = new JButton("Clear Orbits");
		JLabel lblDelay = new JLabel("Step delay (ms)");
		snmDelay = new SpinnerNumberModel(20, 0, 250, 10);
		JSpinner spinDelay = new JSpinner(snmDelay);

		pnlPathDraw.add(cbPathColor,	GBC.get(0, 0, 1, 1));
		pnlPathDraw.add(chkAuto,		GBC.get(1, 0, 1, 1));
		pnlPathDraw.add(lblDelay,		GBC.get(0, 1, 1, 1));
		pnlPathDraw.add(spinDelay,		GBC.get(1, 1, 1, 1));
		pnlPathDraw.add(btnClearOrbits,	GBC.get(2, 1, 1, 1));

		pack();
		setMinimumSize(getPreferredSize());

		cbFractals.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				StepFractalFunction fct = FractalFactory.getFractalFunction(
					cbFractals.getSelectedIndex());
				fractal.setFractalFunction(fct);

				if (fct instanceof StepFractalFunctionWithConst) {
					pnlConst.setVisible(true);
					StepFractalFunctionWithConst cfct = (StepFractalFunctionWithConst) fct;
					ComplexNumber constant = cfct.getConstant();
					ftfConstReal.setValue(constant.getReal());
					ftfConstImag.setValue(constant.getImaginary());
				} else {
					pnlConst.setVisible(false);
				}

				btnReset.doClick();
			}
		});

		// Drawing handler
		btnDraw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				drawFractal();
			}
		});

		// Reset handler
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				scaler.resetZoom();
				drawFractal();
			}
		});

		// Clear orbits handler
		btnClearOrbits.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				displayArea.clearLayer(1);
			}
		});

		// Color scheme handler
		ActionListener colorActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent)
			{
				fractal.setColorMap(ColorMapFactory.getMap(
					cbColor.getSelectedIndex()));

				// ensure path and set color are not the same
				if (cbSetColor.getSelectedIndex() == cbPathColor.getSelectedIndex())
					cyclePathColor();
				fractal.setSetColor(ColorSelection.getColor(
					cbSetColor.getSelectedIndex()));

				drawFractal();
			}
		};
		cbColor.addActionListener(colorActionListener);
		cbSetColor.addActionListener(colorActionListener);

		cbPathColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int idx = cbSetColor.getSelectedIndex();
				if (cbPathColor.getSelectedIndex() == idx) {
					cbSetColor.setSelectedIndex(
						(idx + 1) % cbSetColor.getItemCount());
				}
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

		// mouse actions in display area
		MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (rbtnZoom.isSelected() && e.getButton() == 3) {
					scaler.zoomOut(e.getX(), e.getY(), 3);
					drawFractal();

				} else if (rbtnPath.isSelected() && e.getButton() == 1) {
					drawOrbit(e.getX(), e.getY());
				}
			}

			@Override
			public void mouseMoved(MouseEvent e)
			{
				lblX.setText(doubleFmt.format(scaler.scaleX(e.getX())));
				lblY.setText(doubleFmt.format(scaler.scaleY(e.getY())) + "i");
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				lblX.setText("(none)");
				lblY.setText("(none)i");
			}
		};
		displayArea.addMouseListener(ma);
		displayArea.addMouseMotionListener(ma);

		// automatic resizing
		displayArea.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e)
			{
				delayedDrawFractal();
			}
		});
	}

	private JFormattedTextField createDoubleTextField()
	{
		JFormattedTextField ret = new JFormattedTextField(fmtFactory);;
		ret.setHorizontalAlignment(JFormattedTextField.RIGHT);
		return ret;
	}

	private void initialize()
	{
		scaler = new Scaler();
		fractal = new ComplexEscapeFractal(displayArea, scaler,
			FractalFactory.getFractalFunction(cbFractals.getSelectedIndex()),
			ColorMapFactory.getMap(cbColor.getSelectedIndex()));

		fractal.setStatObserver(this);

		// Simulate click to draw the fractal immediately
		btnDraw.doClick();
	}

	private void cyclePathColor()
	{
		int idx = cbPathColor.getSelectedIndex();
		do {
			idx = (idx + 1) % cbPathColor.getItemCount();
		} while (idx == cbSetColor.getSelectedIndex());
		cbPathColor.setSelectedIndex(idx);
	}

	private void setFractalFunctionConstant()
	{
		StepFractalFunction fct = fractal.getFunction();
		if (fct instanceof StepFractalFunctionWithConst) {
			StepFractalFunctionWithConst cfct = (StepFractalFunctionWithConst) fct;
			double re = ((Double) ftfConstReal.getValue()).doubleValue();
			double im = ((Double) ftfConstImag.getValue()).doubleValue();
			ComplexNumber constant = new ComplexNumber(re, im);
			cfct.setConstant(constant);
		}
	}

	private void delayedDrawFractal()
	{
		// cancel previous task if one exists
		if (delayedDrawTask != null)
			delayedDrawTask.cancel();

		// TimerTasks are a one-time thing, so create a new one
		delayedDrawTask = new TimerTask() {
			@Override
			public void run()
			{
				drawFractal();
			}
		};
		// schedule with 50ms delay
		delayedDrawTimer.schedule(delayedDrawTask, 50);
	}

	private void drawFractal()
	{
		displayArea.createImages();
		setFractalFunctionConstant();
		fractal.drawFractal(snmIterations.getNumber().intValue());
	}

	private void drawOrbit(int x, int y)
	{
		fractal.drawOrbit(x, y,
			snmIterations.getNumber().intValue(),
			ColorSelection.getColor(cbPathColor.getSelectedIndex()),
			snmDelay.getNumber().intValue());

		// auto-cycle color
		if (chkAuto.isSelected()) {
			cyclePathColor();
		}
	}

	@Override
	public void areaSelected(Rectangle rect)
	{
		scaler.zoomIn(rect);
		setFractalFunctionConstant();
		fractal.drawFractal(snmIterations.getNumber().intValue());
	}

	@Override
	public void statisticsDataAvailable()
	{
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				lblStepCount.setText(decimalFmt.format(
					fractal.getStepCount()));
				lblZoomValue.setText(decimalFmt.format(
					Math.round(scaler.getZoom())) + "x");
				lblMilliSec.setText(decimalFmt.format(
					fractal.getDrawTime()) + "ms");
			}
		});
	}
}
