package com.tablet.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class Music {

	@Inject
	private Composite parent;

	@PostConstruct
	private void addComponents() {
		new Label(parent, SWT.CENTER).setText("Empty");
	}
}
