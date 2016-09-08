package com.tablet.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class DisplayHandler {

	@Inject
	EPartService partService;

	@Execute
	private void execute(MHandledToolItem item) {
		MPart part;
		String name = item.getLabel();

		try {
			part = partService.findPart(name);

			hideActiveParts();
			part.setVisible(true);
			partService.bringToTop(part);

		} catch (Exception e) {
			System.out.println("Part not found");
		}
	}

	private void hideActiveParts() {
		partService.getParts().forEach(x -> {
			if (x.isVisible()) {
				x.setVisible(false);
			}
		});
	}

}
