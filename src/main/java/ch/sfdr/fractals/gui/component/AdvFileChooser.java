package ch.sfdr.fractals.gui.component;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;


/**
 * An advanced file chooser. The default JFileChooser is limited. This one:
 * - automatically adds the extension
 * - confirmation before overwriting
 *
 * DO NOT use setFileFilter() or addChoosaveFileFiler() because this chooser
 * relies on FileNameExtensionFilter entirely.
 * @author D.Ritz
 */
public class AdvFileChooser
	extends JFileChooser
{
	private static final long serialVersionUID = 1L;

	private String ext;

	@Override
	protected void setup(FileSystemView view)
	{
		super.setup(view);
		setAcceptAllFileFilterUsed(false);
	}

	/**
	 * Adds the given file type with description and possible extensions.
	 * @param desc description of the type
	 * @param exts list of extensions
	 */
	public void addFileType(String desc, String... exts)
	{
		FileNameExtensionFilter filter = new FileNameExtensionFilter(desc, exts);
		addChoosableFileFilter(filter);
	}

	@Override
	public void approveSelection()
	{
		File file = getSelectedFile();

		// auto-select right filter
		boolean ok = false;
		String ext = ((FileNameExtensionFilter) getFileFilter()).getExtensions()[0];
		for (FileFilter filter : getChoosableFileFilters()) {
			FileNameExtensionFilter extFilter = (FileNameExtensionFilter) filter;
			if (extFilter.accept(file)) {
				ext = extFilter.getExtensions()[0];
				setFileFilter(extFilter);
				ok = true;
				break;
			}
		}

		// no filter matches...add extension of active one
		if (!ok)
			file = new File(file + "." + ext);

		// confirm overwrite
		if (file.exists()) {
			int res = JOptionPane.showConfirmDialog(this,
		        "Target file (" + file + ") exists. Overwrite?", "File exists",
		        JOptionPane.YES_NO_CANCEL_OPTION);
			switch (res) {
			case JOptionPane.NO_OPTION:
				return;
			case JOptionPane.CANCEL_OPTION:
				cancelSelection();
				return;
			case JOptionPane.YES_OPTION:
				break;
			}
		}

		this.ext = ext;
		setSelectedFile(file);
		super.approveSelection();
	}

	/**
	 * Returns the type extension, the first from the list of the file type
	 * @return the type extension
	 */
	public String getType()
	{
		return ext;
	}
}
