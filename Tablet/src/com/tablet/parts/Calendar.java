package com.tablet.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

public class Calendar {
	@Inject
	private Composite parent;

	@PostConstruct
	private void addComponents() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		parent.setLayout(gridLayout);

		DateTime calendar = new DateTime(parent, SWT.CALENDAR);
	}
}
