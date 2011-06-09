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
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import ch.sfdr.fractals.Version;
import ch.sfdr.fractals.fractals.ComplexEscapeFractal;
import ch.sfdr.fractals.fractals.ComplexOrbitCycleFinder;
import ch.sfdr.fractals.fractals.ComplexOrbitCycleListener;
import ch.sfdr.fractals.fractals.FractalFactory;
import ch.sfdr.fractals.fractals.StatisticsObserver;
import ch.sfdr.fractals.fractals.StepFractalFunction;
import ch.sfdr.fractals.fractals.StepFractalFunctionWithConst;
import ch.sfdr.fractals.gui.component.AdvFileChooser;
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
	implements AreaSelectionListener, StatisticsObserver,
		ComplexOrbitCycleListener
{
	private static final long serialVersionUID = 1L;

	// the displaying component
	private DisplayArea displayArea;

	// info panel
	private JLabel lblX;
	private JLabel lblY;
	private JLabel lblZoomValue;
	private JLabel lblMilliSec;
	private JLabel lblCyclesCount;

	// progress bar
	private JProgressBar prgBar;
	private volatile int progress;
	private Runnable prgRun = new Runnable() {
		@Override
		public void run()
		{
			boolean vis = progress < 100;
			prgBar.setVisible(vis);
			lblStepCount.setVisible(!vis);
			prgBar.setValue(progress);
		}
	};
	// cycle finder progress bar
	private JProgressBar prgCycles;
	private volatile int cycleProgress;
	private Runnable cycleProgressRun = new Runnable() {
		@Override
		public void run()
		{
			boolean vis = cycleProgress < 100;
			prgCycles.setVisible(vis);
			lblCyclesCount.setVisible(!vis);
			prgCycles.setValue(cycleProgress);
		}
	};

	private AdvFileChooser chooser;

	// fractals tab
	private JComboBox cbFractals;
	private SpinnerNumberModel snmIterations;
	private JComboBox cbColor;
	private JComboBox cbSetColor;
	private JComboBox cbOrbitColor;
	private JCheckBox chkAuto;
	private SpinnerNumberModel snmDelay;
	private JButton btnDraw;
	private JButton btnReset;
	private JRadioButton rbtnZoom;
	private JRadioButton rbtnOrbit;
	private JLabel lblStepCount;
	private JButton btnClearOrbits;
	private JFormattedTextField ftfStartReal;
	private JFormattedTextField ftfStartImag;
	private JButton btnDrawOrbit;
	private SpinnerNumberModel snmCycleLength;
	private JButton btnFindCycles;
	private SpinnerNumberModel snmCycleDelay;
	private JCheckBox chkCycleFull;
	// constant panel
	private JPanel pnlConst;
	private JFormattedTextField ftfConstReal;
	private JFormattedTextField ftfConstImag;
	private DefaultFormatterFactory fmtFactory;

	// scaler and fractal core
	private Scaler scaler;
	private ComplexEscapeFractal fractal;
	private ComplexOrbitCycleFinder cycleFinder;

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
		displayArea.setPreferredSize(new Dimension(500, 420));
		displayArea.setMinimumSize(displayArea.getPreferredSize());
		displayArea.setSelectionListner(this);

		JPanel pnlInfo = new JPanel(new GridBagLayout());
		pnlInfo.setBorder(BorderFactory.createTitledBorder("Info"));
		JPanel pnlClick = new JPanel(new GridBagLayout());
		pnlClick.setBorder(BorderFactory.createTitledBorder("Click action"));
		btnDraw = new JButton("Draw");
		btnReset = new JButton("Reset");
		Dimension btnDim = new Dimension(100, btnDraw.getMinimumSize().height);
		btnDraw.setPreferredSize(btnDim);
		btnReset.setPreferredSize(btnDim);
		JButton btnSave = new JButton("Save as...");
		btnSave.setPreferredSize(btnDim);

		pnlTop.add(displayArea,			GBC.get(0, 0, 1, 5, 1.0, 1.0, 'b', "nw"));
		pnlTop.add(pnlInfo,				GBC.get(1, 0, 1, 1));
		pnlTop.add(pnlClick,			GBC.get(1, 1, 1, 1, 'h'));
		pnlTop.add(new JPanel(), 		GBC.get(1, 2, 1, 1, 0.0, 1.0, 'v', "nw"));
		pnlTop.add(btnSave,				GBC.get(1, 3, 1, 1, "nw"));

		// Panel Info
		JLabel lblVisible = new JLabel("Coordinate");
		lblVisible.setFont(bold);
		lblX = new JLabel("(none)");
		// avoid resizing later
		lblY = new JLabel("0.099999999999999999i");
		lblVisible.setPreferredSize(lblY.getMinimumSize());
		lblVisible.setMinimumSize(lblVisible.getPreferredSize());
		lblY.setText("(none)i");
		JLabel lblZoom = new JLabel("Zoom");
		lblZoom.setFont(bold);
		lblZoomValue = new JLabel("100%");
		JLabel lblTime = new JLabel("Time to draw");
		lblTime.setFont(bold);
		lblMilliSec = new JLabel("281ms");
		JLabel lblSteps = new JLabel("Steps calculated");
		lblSteps.setFont(bold);
		lblStepCount = new JLabel("0");
		prgBar = new JProgressBar(0, 100);
		prgBar.setStringPainted(true);
		prgBar.setPreferredSize(prgBar.getMinimumSize());
		lblStepCount.setPreferredSize(prgBar.getMinimumSize());
		JLabel lblCyclesFound = new JLabel("Cycles found");
		lblCyclesFound.setFont(bold);
		lblCyclesCount = new JLabel("-");
		prgCycles = new JProgressBar(0, 100);
		prgCycles.setStringPainted(true);
		prgCycles.setPreferredSize(prgCycles.getMinimumSize());
		lblCyclesCount.setPreferredSize(prgCycles.getMinimumSize());
		prgCycles.setVisible(false);

		pnlInfo.add(lblVisible,			GBC.get(0, 0, 1, 1));
		pnlInfo.add(lblX,				GBC.get(0, 1, 1, 1));
		pnlInfo.add(lblY,				GBC.get(0, 2, 1, 1));
		pnlInfo.add(lblZoom,			GBC.get(0, 3, 1, 1));
		pnlInfo.add(lblZoomValue,		GBC.get(0, 4, 1, 1));
		pnlInfo.add(lblTime,			GBC.get(0, 5, 1, 1));
		pnlInfo.add(lblMilliSec,		GBC.get(0, 6, 1, 1));
		pnlInfo.add(lblSteps,			GBC.get(0, 7, 1, 1));
		pnlInfo.add(lblStepCount,		GBC.get(0, 8, 1, 1, 1.0, 0.0, 'h'));
		pnlInfo.add(prgBar,				GBC.get(0, 9, 1, 1, 1.0, 0.0, 'h'));
		pnlInfo.add(lblCyclesFound,		GBC.get(0, 10, 1, 1));
		pnlInfo.add(lblCyclesCount,		GBC.get(0, 11, 1, 1, 1.0, 0.0, 'h'));
		pnlInfo.add(prgCycles,			GBC.get(0, 12, 1, 1, 1.0, 0.0, 'h'));

		// Panel Click Action
		rbtnZoom = new JRadioButton("Zoom");
		rbtnOrbit = new JRadioButton("Orbit drawing");

		ButtonGroup clickGroup = new ButtonGroup();
		clickGroup.add(rbtnZoom);
		clickGroup.add(rbtnOrbit);
		rbtnZoom.setSelected(true);

		pnlClick.add(rbtnZoom,			GBC.get(0, 0, 1, 1, 1.0, 0.0, 'h', "nw"));
		pnlClick.add(rbtnOrbit,			GBC.get(0, 1, 1, 1, "nw"));

		// Panel Bottom
		JPanel pnlFractals = new JPanel(new GridBagLayout());
		JPanel pnlOrbitDraw = new JPanel(new GridBagLayout());

		pnlBottom.add(pnlFractals,	GBC.get(0, 0, 1, 1, 0.5, 0.0, 0,0,0,2, 'h', "nw"));
		pnlBottom.add(pnlOrbitDraw,	GBC.get(1, 0, 1, 1, 0.5, 1.0, 0,2,0,0, 'b', "nw"));

		// Panel Fractals
		cbFractals = new JComboBox(FractalFactory.getFractalFunctionsNames());

		pnlConst = new JPanel(new GridBagLayout());
		JLabel lblConstReal = new JLabel("Const Real/Imag:");
		ftfConstReal = createDoubleTextField();
		JLabel lblConstImag = new JLabel("i");
		ftfConstImag = createDoubleTextField();

		pnlConst.add(lblConstReal,	GBC.get(0, 1, 1, 1, 0.5, 0.0, 'v', "nw"));
		pnlConst.add(ftfConstReal,	GBC.get(1, 1, 1, 1, 0.5, 0.0, 'h', "ne"));
		pnlConst.add(ftfConstImag,	GBC.get(2, 1, 1, 1, 0.5, 0.0, 'h', "ne"));
		pnlConst.add(lblConstImag,	GBC.get(3, 1, 1, 1));

		JLabel lblIterations = new JLabel("Max. iterations");
		snmIterations = new SpinnerNumberModel(200, 50, 500, 10);
		JSpinner spinIterations = new JSpinner(snmIterations);
		JPanel pnlColor = new JPanel(new GridBagLayout());
		pnlColor.setBorder(BorderFactory.createTitledBorder("Colorization"));

		pnlFractals.add(cbFractals,		GBC.get(0, 0, 1, 1, 0.5, 0.0, 'h', "nw"));
		pnlFractals.add(lblIterations,	GBC.get(1, 0, 1, 1));
		pnlFractals.add(spinIterations,	GBC.get(2, 0, 1, 1, "ne"));
		pnlFractals.add(pnlConst,		GBC.get(0, 1, 3, 1, 'h', "nw"));
		pnlFractals.add(pnlColor,		GBC.get(0, 2, 3, 1, 1.0, 0.0, 0,0,0,0, 'h', "nw"));
		pnlFractals.add(btnReset,		GBC.get(0, 3, 1, 1, 'n', "nw"));
		pnlFractals.add(btnDraw,		GBC.get(1, 3, 2, 1, 'n', "ne"));

		// Panel Colorization
		cbColor = new JComboBox(ColorMapFactory.getNames());
		JLabel lblSetColor = new JLabel("Set color");
		cbSetColor = new JComboBox(ColorSelection.getNames());
		cbSetColor.setSelectedIndex(cbSetColor.getItemCount() - 1);

		pnlColor.add(cbColor,			GBC.get(0, 0, 1, 1, 1.0, 0.0, 'h', "nw"));
		pnlColor.add(lblSetColor,		GBC.get(1, 0, 1, 1, 'v', "nw"));
		pnlColor.add(cbSetColor,		GBC.get(2, 0, 1, 1, 1.0, 0.0, 'h', "nw"));

		// Panel Orbit Drawing
		pnlOrbitDraw.setBorder(BorderFactory.createTitledBorder("Orbit drawing"));
		JPanel pnlOrbSet = new JPanel(new GridBagLayout());
		JPanel pnlOrbManual = new JPanel(new GridBagLayout());
		JPanel pnlOrbCycle = new JPanel(new GridBagLayout());

		pnlOrbitDraw.add(pnlOrbSet,		GBC.get(0, 0, 1, 1, 1.0, 0.0, 'h'));
		pnlOrbitDraw.add(pnlOrbManual,	GBC.get(0, 1, 1, 1, 1.0, 0.0, 'h'));
		pnlOrbitDraw.add(pnlOrbCycle,	GBC.get(0, 2, 1, 1, 1.0, 0.0, 'h'));

		// orbit settings
		cbOrbitColor = new JComboBox(ColorSelection.getNames());
		chkAuto = new JCheckBox("Auto-cycle");
		chkAuto.setSelected(true);
		JLabel lblDelay = new JLabel("Step delay (ms)");
		snmDelay = new SpinnerNumberModel(20, 0, 250, 10);
		JSpinner spinDelay = new JSpinner(snmDelay);
		btnClearOrbits = new JButton("Clear orbits");
		btnClearOrbits.setPreferredSize(btnDim);
		btnClearOrbits.setMinimumSize(btnDim);

		pnlOrbSet.add(cbOrbitColor,		GBC.get(0, 0, 1, 1, 0.25, 0.0, 'h'));
		pnlOrbSet.add(chkAuto,			GBC.get(1, 0, 1, 1));
		pnlOrbSet.add(lblDelay,			GBC.get(2, 0, 1, 1));
		pnlOrbSet.add(spinDelay,		GBC.get(3, 0, 1, 1));
		pnlOrbSet.add(btnClearOrbits,	GBC.get(4, 0, 1, 1, "e"));

		// manual orbit drawing
		JLabel lblStart = new JLabel("Manual Real/Imag:");
		ftfStartReal = createDoubleTextField();
		ftfStartReal.setValue(0.0D);
		JLabel lblStartImag = new JLabel("i");
		ftfStartImag = createDoubleTextField();
		ftfStartImag.setValue(0.0D);
		btnDrawOrbit = new JButton("Draw orbit");
		btnDrawOrbit.setPreferredSize(btnDim);
		btnDrawOrbit.setMinimumSize(btnDim);

		pnlOrbManual.add(lblStart,		GBC.get(0, 0, 1, 1));
		pnlOrbManual.add(ftfStartReal,	GBC.get(1, 0, 1, 1, 0.5, 0.0, 'h'));
		pnlOrbManual.add(ftfStartImag,	GBC.get(2, 0, 1, 1, 0.5, 0.0, 'h'));
		pnlOrbManual.add(lblStartImag,	GBC.get(3, 0, 1, 1));
		pnlOrbManual.add(btnDrawOrbit,	GBC.get(4, 0, 1, 1));

		// Orbit cycles
		JLabel lblCycle = new JLabel("Find cycles of length");
		snmCycleLength = new SpinnerNumberModel(3, 2, 11, 1);
		JSpinner spinCycleLength = new JSpinner(snmCycleLength);
		JLabel lblCycleDelay = new JLabel("Delay (ms)");
		snmCycleDelay = new SpinnerNumberModel(500, 50, 1500, 10);
		JSpinner spinCycleDelay = new JSpinner(snmCycleDelay);
		chkCycleFull = new JCheckBox("Full orbit");
		chkCycleFull.setSelected(true);
		btnFindCycles = new JButton("Find");
		btnFindCycles.setPreferredSize(btnDim);
		btnFindCycles.setMinimumSize(btnDim);

		pnlOrbCycle.add(lblCycle,		GBC.get(0, 0, 1, 1, 'v', "w"));
		pnlOrbCycle.add(spinCycleLength,GBC.get(1, 0, 1, 1, "c"));
		pnlOrbCycle.add(lblCycleDelay,	GBC.get(2, 0, 1, 1, 'v', "w"));
		pnlOrbCycle.add(spinCycleDelay,	GBC.get(3, 0, 1, 1, "c"));
		pnlOrbCycle.add(chkCycleFull,	GBC.get(4, 0, 1, 1, 1.0, 0.0, 'h'));
		pnlOrbCycle.add(btnFindCycles,	GBC.get(5, 0, 1, 1, "e"));

		pack();

		// set minimum size of the whole window, bottom area
		setMinimumSize(getPreferredSize());
		pnlBottom.setMinimumSize(pnlBottom.getSize());
		pnlBottom.setPreferredSize(pnlBottom.getSize());
		pnlConst.setVisible(false);

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

				reset();
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
				reset();
			}
		});

		// Clear orbits handler
		btnClearOrbits.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				clearOrbits();
			}
		});

		// Draw orbit handler
		btnDrawOrbit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				double re = ((Double) ftfStartReal.getValue()).doubleValue();
				double im = ((Double) ftfStartImag.getValue()).doubleValue();
				ComplexNumber c = new ComplexNumber(re, im);
				drawOrbit(c);
			}
		});

		// Color scheme handler
		ActionListener colorActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent)
			{
				fractal.setColorMap(ColorMapFactory.getMap(
					cbColor.getSelectedIndex()));

				// ensure orbit and set color are not the same
				if (cbSetColor.getSelectedIndex() == cbOrbitColor.getSelectedIndex())
					cyclePathColor();
				fractal.setSetColor(ColorSelection.getColor(
					cbSetColor.getSelectedIndex()));

				drawFractal();
			}
		};
		cbColor.addActionListener(colorActionListener);
		cbSetColor.addActionListener(colorActionListener);

		cbOrbitColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int idx = cbSetColor.getSelectedIndex();
				if (cbOrbitColor.getSelectedIndex() == idx) {
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
		rbtnOrbit.addActionListener(clickActionListener);

		// cycle finder
		btnFindCycles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				cycleFinder.findAllCycles(fractal.getFunction(),
					snmCycleLength.getNumber().intValue(),
					snmCycleDelay.getNumber().longValue());
			}
		});

		// save button
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				saveImage();
			}
		});

		// mouse actions in display area
		MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (rbtnZoom.isSelected() && e.getButton() == 3) {
					scaler.zoomOut(e.getX(), e.getY(), 3);
					drawFractal();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				if (rbtnOrbit.isSelected() && e.getButton() == 1) {
					drawOrbit(e.getX(), e.getY());
				}
			}

			@Override
			public void mouseDragged(MouseEvent e)
			{
				if (rbtnOrbit.isSelected()) {
					fractal.drawLiveOrbit(e.getX(), e.getY(),
						snmIterations.getNumber().intValue());
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
		fractal = new ComplexEscapeFractal(displayArea.getLayeredImage(), scaler,
			FractalFactory.getFractalFunction(cbFractals.getSelectedIndex()),
			ColorMapFactory.getMap(cbColor.getSelectedIndex()));

		fractal.setStatObserver(this);

		cycleFinder = new ComplexOrbitCycleFinder(this);

		cycleFinder.setStatObserver(this);

		// draw the fractal immediately
		drawFractal();
	}

	private void cyclePathColor()
	{
		int idx = cbOrbitColor.getSelectedIndex();
		do {
			idx = (idx + 1) % cbOrbitColor.getItemCount();
		} while (idx == cbSetColor.getSelectedIndex());
		cbOrbitColor.setSelectedIndex(idx);
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
		delayedDrawTimer.schedule(delayedDrawTask, 100);
	}

	private void drawFractal()
	{
		displayArea.createImages();
		setFractalFunctionConstant();
		scaler.setDimension(displayArea.getLayeredImage().getImageWidth(),
			displayArea.getLayeredImage().getImageHeight());
		fractal.drawFractal(snmIterations.getNumber().intValue());
		fractal.redrawAllOrbits();
	}

	private void drawOrbit(int x, int y)
	{
		ComplexNumber c = new ComplexNumber(scaler.scaleX(x), scaler.scaleY(y));
		ftfStartReal.setValue(c.getReal());
		ftfStartImag.setValue(c.getImaginary());

		drawOrbit(c);
	}

	private void drawOrbit(ComplexNumber c)
	{
		drawOrbit(c, snmIterations.getNumber().intValue(),
			snmDelay.getNumber().intValue());
	}

	private void drawOrbit(ComplexNumber c, int iterations, long delay)
	{
		fractal.drawOrbit(c, iterations,
			ColorSelection.getColor(cbOrbitColor.getSelectedIndex()), delay);

		// auto-cycle color
		if (chkAuto.isSelected()) {
			cyclePathColor();
		}
	}

	private void clearOrbits()
	{
		cycleFinder.stop();
		fractal.clearOrbits();
		ftfStartReal.setValue(0.0D);
		ftfStartImag.setValue(0.0D);
		lblCyclesCount.setText("-");
		lblCyclesCount.setVisible(true);
		prgCycles.setVisible(false);
	}

	private void reset()
	{
		clearOrbits();
		scaler.resetZoom();
		drawFractal();
	}

	private void saveImage()
	{
		// lazy-initialize chooser
		if (chooser == null) {
			chooser = new AdvFileChooser();
			chooser.addFileType("JPEG Images", "jpg", "jpeg");
			chooser.addFileType("PNG Images", "png");
		}

		int status = chooser.showSaveDialog(this);
		if (status == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			String ext = chooser.getType();

			try {
				displayArea.getLayeredImage().saveImage(file, ext);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Cannot save image: " +
					e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	@Override
	public void areaSelected(Rectangle rect)
	{
		scaler.zoomIn(rect);
		drawFractal();
	}

	@Override
	public void statisticsDataAvailable(Object source)
	{
		if (source == fractal) {
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

		if (source == cycleFinder) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run()
				{
					lblCyclesCount.setText(
						cycleFinder.getCyclesFound() + " cycles of length " +
						cycleFinder.getCycleLength());
				}
			});
		}
	}

	@Override
	public void updateProgess(Object source, int percent)
	{
		if (source == fractal) {
			progress = percent;
			EventQueue.invokeLater(prgRun);
		}
		if (source == cycleFinder) {
			cycleProgress = percent;
			EventQueue.invokeLater(cycleProgressRun);
		}
	}

	@Override
	public boolean cycleFound(final ComplexNumber start, final int length)
	{
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				drawOrbit(start, chkCycleFull.isSelected() ? 2 * length : 0, 0);
			}
		});
		return true;
	}
}
