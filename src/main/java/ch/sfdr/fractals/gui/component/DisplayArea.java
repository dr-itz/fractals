package ch.sfdr.fractals.gui.component;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * The fractal display area, thread safe
 */
public class DisplayArea
	extends JPanel
{
	private static final long serialVersionUID = 1L;

	private static final Color SEL_COLOR = new Color(192, 192, 192, 128);
	private static final Color SEL_BORDER_COLOR = new Color(255, 255, 255, 255);

	private LayeredImage layeredImage;

	private SelectionRect selectionRect;
	private boolean selectionMode = true;
	private AreaSelectionListener selectionListener;

	private Runnable repaintRun = new Runnable() {
		@Override
		public void run()
		{
			repaint();
		};
	};

	/**
	 * creates the DisplayArea
	 */
	public DisplayArea(int numLayers)
	{
		super(null, true);
		layeredImage = new LayeredImage(numLayers) {
			@Override
			protected void newImageAvailable()
			{
				if (EventQueue.isDispatchThread()) {
					repaint();
					return;
				}
				EventQueue.invokeLater(repaintRun);
			}
		};

		selectionRect = new SelectionRect();
		selectionRect.setVisible(false);
		add(selectionRect);

		MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (!selectionMode || e.getButton() != 1)
					return;
				selectionRect.start(e.getX(), e.getY());
			}

			@Override
			public void mouseDragged(MouseEvent e)
			{
				if (!selectionMode)
					return;
				selectionRect.drag(e.getX(), e.getY());
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				if (!selectionMode)
					return;
				selectionRect.end();
			}
		};
		addMouseListener(ma);
		addMouseMotionListener(ma);

		// make ESC cancel a selection in progress
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
			new KeyEventDispatcher()
		{
			@Override
			public boolean dispatchKeyEvent(KeyEvent e)
			{
				if (selectionRect.inSelection &&
					e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					selectionRect.cancel();
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * creates the buffered images backing this component
	 */
	public synchronized void createImages()
	{
		layeredImage.createImages(getWidth(), getHeight());
	}

	/**
	 * @param selectionMode the selectionMode to set
	 */
	public void setSelectionMode(boolean selectionMode)
	{
		this.selectionMode = selectionMode;
	}

	/**
	 * @return the selectionMode
	 */
	public boolean isSelectionMode()
	{
		return selectionMode;
	}


	/**
	 * @param keepAspectRatio the keepAspectRatio to set
	 */
	public void setKeepAspectRatio(boolean keepAspectRatio)
	{
		selectionRect.keepAspectRatio = keepAspectRatio;
	}

	/**
	 * @return the keepAspectRatio
	 */
	public boolean isKeepAspectRatio()
	{
		return selectionRect.keepAspectRatio;
	}

	/**
	 * @param selectionListner the selectionListner to set
	 */
	public void setSelectionListner(AreaSelectionListener selectionListner)
	{
		this.selectionListener = selectionListner;
	}

	/**
	 * @return the selectionListner
	 */
	public AreaSelectionListener getSelectionListner()
	{
		return selectionListener;
	}

	@Override
	public void paintComponent(Graphics g)
	{
		if (layeredImage == null) {
			super.paintComponent(g);
			return;
		}

		g.drawImage(layeredImage.getBackingImage(), 0, 0, null);
	}

	/**
	 * Returns the backing layered image.
	 * @return the layeredImage
	 */
	public ImageDisplay getLayeredImage()
	{
		return layeredImage;
	}

	/**
	 * A panel on top to draw a semi-transparent selection
	 */
	private class SelectionRect
		extends JComponent
	{
		private static final long serialVersionUID = 1L;
		private boolean inSelection = false;
		private Rectangle selection;
		private boolean keepAspectRatio = true;

		public SelectionRect()
		{
			super();
			setOpaque(false);
		}

		@Override
		public void paint(Graphics g)
		{
			int w = getWidth();
			int h = getHeight();
			g.setColor(SEL_COLOR);
			g.fillRect(1, 1, w - 2, h - 2);
			g.setColor(Color.BLACK);
			g.setXORMode(SEL_BORDER_COLOR);
			g.drawRect(0, 0, w - 1, h - 1);
		}

		public void start(int x, int y)
		{
			inSelection = true;
			selection = new Rectangle(x, y, 0, 0);
			setLocation(x, y);
			setSize(0, 0);
			setVisible(true);
		}

		public void drag(int x, int y)
		{
			if (!inSelection)
				return;

			int cwidth = x - selection.x;
			int cheight = y - selection.y;

			if (keepAspectRatio) {
				int w = layeredImage.getImageWidth();
				int h = layeredImage.getImageHeight();
				double r = Math.max((double) Math.abs(cwidth) / w,
					(double) Math.abs(cheight) / h);
				cwidth = (int) (w * r * Math.signum(cwidth));
				cheight = (int) (h * r * Math.signum(cheight));
			}
			selection.setSize(cwidth, cheight);

			int cx = selection.x;
			int cy = selection.y;
			if (cwidth < 0) {
				cx = selection.x + cwidth;
				cwidth = -cwidth;
			}
			if (cheight < 0) {
				cy = selection.y + cheight;
				cheight = -cheight;
			}
			setBounds(cx, cy, cwidth, cheight);
		}

		public void end()
		{
			if (!inSelection)
				return;
			cancel();

			Rectangle sel = getSelection();
			if (sel.width > 0 && sel.height > 0 && selectionListener != null)
				selectionListener.areaSelected(sel);
		}

		public void cancel()
		{
			inSelection = false;
			setVisible(false);
		}

		public Rectangle getSelection()
		{
			int cx = selection.x;
			int cy = selection.y;
			int cwidth = selection.width;
			int cheight = selection.height;
			if (cwidth < 0) {
				cx = selection.x + cwidth;
				cwidth = -cwidth;
			}
			if (cheight < 0) {
				cy = selection.y + cheight;
				cheight = -cheight;
			}
			return new Rectangle(cx, cy, cwidth, cheight);
		}
	}
}
