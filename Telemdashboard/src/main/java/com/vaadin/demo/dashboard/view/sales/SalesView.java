package com.vaadin.demo.dashboard.view.sales;

import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.timeline.Timeline;
import com.vaadin.data.Container;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.demo.dashboard.DashboardUI;
import com.vaadin.demo.dashboard.domain.MeasuredValues;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.maddon.ListContainer;

import java.util.*;
import java.util.Calendar;

@SuppressWarnings("serial")
public class SalesView extends VerticalLayout implements View {

	private final Timeline timeline;

	private ComboBox parameterSelect;

	private static final SolidColor[] COLORS = new SolidColor[]{
			new SolidColor(52, 154, 255), new SolidColor(242, 81, 57),
			new SolidColor(255, 201, 35), new SolidColor(83, 220, 164)};

	private static final SolidColor[] COLORS_ALPHA = new SolidColor[]{
			new SolidColor(52, 154, 255, 0.3),
			new SolidColor(242, 81, 57, 0.3),
			new SolidColor(255, 201, 35, 0.3),
			new SolidColor(83, 220, 164, 0.3)};

	private int colorIndex = -1;

	public SalesView() {
		setSizeFull();
		addStyleName("sales");

		addComponent(buildHeader());

		timeline = buildTimeline();
		addComponent(timeline);
		setExpandRatio(timeline, 1);

		initMovieSelect();
		// Add first 2 by default
		List<String> subList = new ArrayList<String>(DashboardUI
				.getDataProvider().getParameters()).subList(0, 1);
		for (String p : subList) {
			addDataSet(p);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -2);
		if (timeline.getGraphDatasources().size() > 0) {
			timeline.setVisibleDateRange(calendar.getTime(), new Date());
		}
	}

	private void initMovieSelect() {
		Collection<String> paras = DashboardUI.getDataProvider().getParameters();

//		Parameters testpara = new Parameters();
//
//		for (Iterator<String> iterator = paras.iterator(); iterator.hasNext(); ) {
//			testpara = (Parameters) iterator.next();
//			System.out.println("Doorgegeven ID : " + testpara.get_id());
//			System.out.println("Doorgegeven PARA : " + testpara.getTitle());
//		}


		Container movieContainer = new ListContainer<String>(String.class, paras);

		parameterSelect.setContainerDataSource(movieContainer);

	}

	private Component buildHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		Responsive.makeResponsive(header);

		Label titleLabel = new Label("Eriks Telemetrie");
		titleLabel.setSizeUndefined();
		titleLabel.addStyleName(ValoTheme.LABEL_H1);
		titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponents(titleLabel, buildToolbar());

		return header;
	}

	private Component buildToolbar() {
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.addStyleName("toolbar");
		toolbar.setSpacing(true);

		parameterSelect = new ComboBox();
//		parameterSelect.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
//		parameterSelect.setItemCaptionPropertyId("title");
		parameterSelect.addShortcutListener(new ShortcutListener("Add",
				KeyCode.ENTER, null) {
			@Override
			public void handleAction(final Object sender, final Object target) {
				addDataSet((String) parameterSelect.getValue());
			}
		});

		final Button add = new Button("Add");
		add.setEnabled(false);
		add.addStyleName(ValoTheme.BUTTON_PRIMARY);

		CssLayout group = new CssLayout(parameterSelect, add);
		group.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		toolbar.addComponent(group);

		parameterSelect.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(final ValueChangeEvent event) {
				add.setEnabled(event.getProperty().getValue() != null);
			}
		});

		final Button clear = new Button("Clear");
		clear.addStyleName("clearbutton");
		clear.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				timeline.removeAllGraphDataSources();
				initMovieSelect();
				clear.setEnabled(false);
			}
		});
		toolbar.addComponent(clear);

		add.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				addDataSet((String) parameterSelect.getValue());
				clear.setEnabled(true);
			}
		});

		return toolbar;
	}

	private Timeline buildTimeline() {
		Timeline result = new Timeline();
		result.setDateSelectVisible(true);
		result.setChartModesVisible(true);
		result.setGraphShadowsEnabled(false);
		result.setZoomLevelsVisible(false);
		result.setSizeFull();
		result.setNoDataSourceCaption("<span class=\"v-label h2 light\">Add a data set from the dropdown above</span>");
		return result;
	}

	private void addDataSet(final String p) {
		parameterSelect.removeItem(p);
		parameterSelect.setValue(null);

		Collection<MeasuredValues> dailyRevenue = DashboardUI.getDataProvider()
				.fetchParaMeasuredValues(p);

		System.out.println("vergelijkend parameter voor doorgeving : " + p);

		ListContainer<MeasuredValues> dailyRevenueContainer = new TempMovieRevenuesContainer(
				dailyRevenue);


		dailyRevenueContainer.sort(new Object[]{"timestamp"},
				new boolean[]{true});
		timeline.addGraphDataSource(dailyRevenueContainer, "timestamp",
				"value");
		colorIndex = (colorIndex >= COLORS.length - 1 ? 0 : ++colorIndex);
		timeline.setGraphOutlineColor(dailyRevenueContainer, COLORS[colorIndex]);
		timeline.setBrowserOutlineColor(dailyRevenueContainer,
				COLORS[colorIndex]);
		timeline.setBrowserFillColor(dailyRevenueContainer,
				COLORS_ALPHA[colorIndex]);
		timeline.setGraphCaption(dailyRevenueContainer, p);
		timeline.setEventCaptionPropertyId("date");
		timeline.setVerticalAxisLegendUnit(dailyRevenueContainer, "");
	}

	@Override
	public void enter(final ViewChangeEvent event) {
	}

	private class TempMovieRevenuesContainer extends
			ListContainer<MeasuredValues> {

		public TempMovieRevenuesContainer(
				final Collection<MeasuredValues> collection) {
			super(MeasuredValues.class, collection);
		}

		// This is only temporarily overridden until issues with
		// BeanComparator get resolved.
		@Override
		public void sort(final Object[] propertyId, final boolean[] ascending) {
			final boolean sortAscending = ascending[0];
			Collections.sort(getBackingList(), new Comparator<MeasuredValues>() {
				@Override
				public int compare(final MeasuredValues o1, final MeasuredValues o2) {
					int result = o1.getTimestamp().compareTo(o2.getTimestamp());
					if (!sortAscending) {
						result *= -1;
					}
					return result;
				}
			});
		}

	}
}
