package com.tablet.parts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.GestureEvent;
import org.eclipse.swt.events.GestureListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.tablet.handlers.TableSelectionAdapter;
import com.tablet.model.City;
import com.tablet.model.CityNamelLabelProvider;

public class CurrentPosition {

	@Inject
	private Composite parent;
	private TableViewer viewer;
	private List<City> cities;

	@PostConstruct
	private void addComponents() {
		parent.setLayout(new GridLayout(3, true));
		createTable();
		addCityList();

		Button start = new Button(parent, SWT.BOTTOM);
		start.setText("Start");
		start.addSelectionListener(new TableSelectionAdapter(viewer));
	}

	private void createTable() {
		viewer = new TableViewer(parent);

		GridData gridData = new GridData();
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		gridData.verticalAlignment = SWT.FILL;

		Table table = viewer.getTable();
		table.setLayoutData(gridData);

		TableViewerColumn cityName = new TableViewerColumn(viewer, SWT.CENTER);
		cityName.getColumn().setText("City");
		cityName.getColumn().setWidth(100);

		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new CityNamelLabelProvider());
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	private void addCityList() {
		cities = new ArrayList<>();

		City c1 = new City("Timisoara");
		City c2 = new City("Oradea");
		City c3 = new City("Cluj");
		City c4 = new City("Turda");
		City c5 = new City("Luduș");
		City c6 = new City("TG Mures");
		City c7 = new City("Sighisoara");
		City c8 = new City("Brasov");
		City c9 = new City("Ploiesti");
		City c10 = new City("Bucuresti");
		City c11 = new City("Constanta");
		City c12 = new City("Neptun");

		cities.add(c1);
		cities.add(c2);
		cities.add(c3);
		cities.add(c4);
		cities.add(c5);
		cities.add(c6);
		cities.add(c7);
		cities.add(c8);
		cities.add(c9);
		cities.add(c10);
		cities.add(c11);
		cities.add(c12);

		viewer.setInput(cities);
	}

	@Focus
	public void onFocus() {
		viewer.getTable().setFocus();
	}
	
}
