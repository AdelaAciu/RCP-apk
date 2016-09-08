package com.tablet.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class Contacts {
	@Inject
	private Composite parent;

	@PostConstruct
	private void create() {
		new Label(parent, SWT.CENTER).setText("Don't have any contacts yet");
	}
}
