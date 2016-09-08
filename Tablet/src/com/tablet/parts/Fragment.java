package com.tablet.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class Fragment {

	@Inject
	private Composite parent;
	@Inject
	private Shell shell;

	@PostConstruct
	private void create() {
		Button display = new Button(parent, SWT.PUSH);
		display.setText("Press me! :D");
		
		display.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageDialog.openInformation(shell, "Info", "BOOOM");
				MessageDialog.openInformation(shell, "Hello", "I'm a fragment");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
}
