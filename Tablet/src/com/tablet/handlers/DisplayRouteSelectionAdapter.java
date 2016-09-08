package com.tablet.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.tablet.model.City;
import com.tablet.model.Distance;

public class DisplayRouteSelectionAdapter extends SelectionAdapter {

	@Inject
	private Shell shell;
	private Job job;
	private TableViewer viewer;
	private Browser browser;
	private City city;
	private City nextCity;
	private Table table;
	private JsonParser parser = new JsonParser();
	private Label distanceLabel;
	private Text durationLabel;
	private Button stop;
	protected boolean intrerupt;
	private int currentIndex;
	private int size;

	public DisplayRouteSelectionAdapter(TableViewer viewer, Browser browser, Label distanceLabel, Text duration,
			Button stop) {
		this.viewer = viewer;
		this.browser = browser;
		size = viewer.getTable().getItemCount();
		table = viewer.getTable();
		this.distanceLabel = distanceLabel;
		this.stop = stop;
		this.durationLabel = duration;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {

		job = new Job("DisplayRouteSelectionAdapter") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				initialize();
				while (currentIndex < size && !intrerupt) {

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

			Thread.sleep(3000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void syncWithUi(int index) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				intrerupt = stop.getSelection();
				if (!intrerupt) {
					table.setSelection(currentIndex);
					currentIndex++;
				}
			}
		});

	}

	private void displayCity() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				city = (City) ((StructuredSelection) viewer.getSelection()).iterator().next();
				nextCity = city;
				int tableIndex = table.getSelectionIndex();

				if (tableIndex < table.getItemCount() - 1)
					nextCity = (City) table.getItem(tableIndex + 1).getData();

				// display on map the route between two cities
				browser.setUrl("https://www.google.ro/maps/dir/" + city.getName() + "/" + nextCity.getName());

				creteaRequest(nextCity);
			}

		});

	}

	private HttpURLConnection createConnecion() throws MalformedURLException, IOException {
		String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
				+ generateNameForMaps(city.getName()) + "&destination=" + generateNameForMaps(nextCity.getName())
				+ "&key=AIzaSyBCOV90QNdxcBQQ3-bUAn21RhXf7zK0-kA";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		return con;
	}

	private void creteaRequest(City nextCity) {

		try {
			HttpURLConnection connection = createConnecion();

			connection.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine = null;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);

			getDistance(response);
			getDuration(response);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getRoutes(StringBuffer response) throws JsonSyntaxException {
		JsonObject jsonObj = (JsonObject) parser.parse(response.toString());
		JsonArray routesArray = jsonObj.get("routes").getAsJsonArray();

		String routes = routesArray.toString();
		routes = routes.substring(1, routes.length() - 1);

		return routes;
	}

	private String getLegs(String routes) throws JsonSyntaxException {
		JsonObject jsonObject = (JsonObject) parser.parse(routes);
		JsonArray legs = jsonObject.get("legs").getAsJsonArray();
		String legsString = legs.toString();
		legsString = legsString.substring(1, legsString.length() - 1);

		return legsString;
	}

	private void getDistance(StringBuffer response) throws JsonSyntaxException {
		String routes = getRoutes(response);
		String legsString = getLegs(routes);
		JsonObject jsonObject = (JsonObject) parser.parse(legsString);
		JsonObject distanceJson = jsonObject.get("distance").getAsJsonObject();
		Distance distance = new Gson().fromJson(distanceJson, Distance.class);

		distanceLabel.setText(distance.text);
	}

	private void getDuration(StringBuffer response) {
		String routes = getRoutes(response);
		String legsString = getLegs(routes);
		JsonObject jsonObject = (JsonObject) parser.parse(legsString);

		String duration = jsonObject.get("duration").getAsJsonObject().get("text").getAsString();

		durationLabel.setText(duration);
	}

	private void endTask() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				MessageDialog.openInformation(shell, "Trip", "You reached the destination");
			}
		});
	}

	private String generateNameForMaps(String cityName) {
		if (cityName.contains(" "))
			cityName = cityName.replace(" ", "+");

		return cityName;
	}

	private void initialize() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				currentIndex = 0;
				distanceLabel.setText("0");
				intrerupt = false;
			}
		});
	}

}
