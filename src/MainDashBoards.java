import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class MainDashBoards extends Composite {
	private Text text;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MainDashBoards(Composite parent, int style) {
		super(parent, style);
		
		text = new Text(this, SWT.BORDER);
		text.setBounds(22, 23, 365, 19);
		
		Button btnSearch = new Button(this, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnSearch.setBounds(405, 19, 68, 23);
		btnSearch.setText("Search");
		
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setBounds(0, 79, 521, 401);
		
		TabItem tbtmMeaning = new TabItem(tabFolder, SWT.NONE);
		tbtmMeaning.setText("Meaning");
		
		TabItem tbtmSysnonysms = new TabItem(tabFolder, SWT.NONE);
		tbtmSysnonysms.setText("Sysnonysms");
		
		TabItem tbtmWiki = new TabItem(tabFolder, SWT.NONE);
		tbtmWiki.setText("Wiki");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
