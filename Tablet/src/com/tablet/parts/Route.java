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
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.tablet.handlers.DisplayRouteSelectionAdapter;
import com.tablet.handlers.MySelectionAdapter;
import com.tablet.model.City;
import com.tablet.model.CityNamelLabelProvider;

public class Route {

	@Inject
	private Composite parent;
	private TableViewer viewer;
	private Button displayCurrentCityBtn;
	private Browser browser;
	private Button displayRouteBtn;
	private Label distance;
	private Text duration;
	private List<City> cities;
	protected boolean intrerupt;

	@PostConstruct
	private void createPart() {
		GridLayout layout = new GridLayout(4, false);
		parent.setLayout(layout);

		createTable();
		addCityList();
		createMap();
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
		table.setVisible(true);

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

	private void createMap() {
		browser = new Browser(parent, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		displayCurrentCityBtn = new Button(parent, SWT.PUSH);
		displayCurrentCityBtn.setText("Display current city");

		displayRouteBtn = new Button(parent, SWT.PUSH);
		displayRouteBtn.setText("Display route");

		distance = new Label(parent, SWT.TOP);
		distance.setText("0				");

		Button stop = new Button(parent, SWT.CHECK);
		stop.setText("Stop");

		duration = new Text(parent, SWT.NONE);
		duration.setEditable(false);
		duration.setText("Duration between cities");
		duration.setSize(64, 32);
		
		displayCurrentCityBtn.addSelectionListener(new MySelectionAdapter(viewer, browser, stop));
		displayRouteBtn
				.addSelectionListener(new DisplayRouteSelectionAdapter(viewer, browser, distance, duration, stop));

	}

	@Focus
	public void onFocus() {
		viewer.getTable().setFocus();
	}
}
