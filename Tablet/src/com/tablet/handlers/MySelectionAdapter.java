
package com.tablet.handlers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.tablet.model.City;

public class MySelectionAdapter extends SelectionAdapter {
	@Inject
	private Shell shell;

	private TableViewer viewer;
	private Browser browser;
	private City city;
	private Button stop;
	protected boolean intrerupt;
	private int size;
	private int currentIndex;

	public MySelectionAdapter(TableViewer viewer, Browser browser, Button stop) {
		this.viewer = viewer;
		this.browser = browser;
		this.stop = stop;
		size = viewer.getTable().getItemCount();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {

		Job job = new Job("MySelectionAdapter") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				initialize();

				while (currentIndex < size && !intrerupt) {
					waitTime();
					syncWithUi(size);
					displayCity();
					waitTime();
				}
				if (!intrerupt) {
					endTask();
				}

				initialize();

				// use this to open a Shell in the UI thread
				return Status.OK_STATUS;
			}

		};
		job.setUser(true);
		job.schedule();
	}

	private void waitTime() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void displayCity() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				city = (City) ((StructuredSelection) viewer.getSelection()).iterator().next();

				try {

					browser.setUrl("https://www.google.com/maps/place/" + URLEncoder.encode(city.getName(), "UTF-8")
							+ "/&output=embed");

				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}

			}
		});

	}

	private void syncWithUi(int index) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				intrerupt = stop.getSelection();

				if (!intrerupt) {
					viewer.getTable().setSelection(currentIndex);
					currentIndex++;
				}
			}
		});

	}

	private void endTask() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				MessageDialog.openInformation(shell, "Trip", "You reached the destination");
			}
		});
	}

	private void initialize() {
		currentIndex = 0;
		intrerupt = false;
	}
}