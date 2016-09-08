package com.tablet.handlers;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TableSelectionAdapter extends SelectionAdapter {
	@Inject
	private Shell shell;

	private TableViewer viewer;
	private int size;
	private int currentIndex;

	public TableSelectionAdapter(TableViewer viewer) {
		this.viewer = viewer;
		size = viewer.getTable().getItemCount();
		currentIndex = 0;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {

		Job job = new Job("TableSelectionAdapter") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				while (currentIndex < size) {
					waitTime();
					syncWithUi(size);
					waitTime();
				}
				endTask();
				currentIndex = 0;
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

	private void syncWithUi(int index) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {

				viewer.getTable().setSelection(currentIndex);
				currentIndex++;
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
}
