package com.biprom.eriks.telem.ui;


import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = MainView.VIEW_NAME)
public class MainView extends MainDesign implements View {

	public static final String VIEW_NAME = "MAIN_VIEW";


	//create navigator for subviews
	@Autowired
	public MainView(SpringViewProvider viewProvider) {


		//create navigator for subviews

		Navigator subNavigator = new Navigator(UI.getCurrent(), scroll_panel);
		subNavigator.addProvider(viewProvider);
//
		statisticsButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				subNavigator.navigateTo(StatisticsView.VIEW_NAME);

			}
		});


		onlineButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				subNavigator.navigateTo(OnlineView.VIEW_NAME);


			}
		});


		userButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				subNavigator.navigateTo(UserView.VIEW_NAME);


			}
		});

		hardwareButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				subNavigator.navigateTo(HardwareView.VIEW_NAME);

			}
		});


	}


	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}