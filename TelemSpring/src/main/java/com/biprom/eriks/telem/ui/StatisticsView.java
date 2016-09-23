package com.biprom.eriks.telem.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;

@SpringView(name = StatisticsView.VIEW_NAME)
public class StatisticsView extends StatisticsDesign implements View {

	public static final String VIEW_NAME = "Statistics";


	public StatisticsView() {


	}


	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
