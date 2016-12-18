package com.biprom.eriks.telem.ui;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Notification;

@SpringView(name = StatisticsView.VIEW_NAME)
public class StatisticsView extends StatisticsDesign implements View {

	public static final String VIEW_NAME = "Statistics";


	public StatisticsView() {

		//set scope of sliders from 0 tot 8
		mintempSlider.setMin(10);
		mintempSlider.setMax(70);
		mintempSlider.addValueChangeListener(new ValueChangeListener() {

					/**
					 *
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Notification.show("Attention! Temp min has changed to " + event.getProperty().getValue());


					}

				});
		
		
		//set scope of sliders from 0 tot 8
				maxtempSlider.setMin(10);
				maxtempSlider.setMax(70);
				maxtempSlider.addValueChangeListener(new ValueChangeListener() {

							/**
							 *
							 */
							private static final long serialVersionUID = 1L;

							@Override
							public void valueChange(ValueChangeEvent event) {
								Notification.show("Attention! temp Max changed to " + event.getProperty().getValue());


							}

						});


	}


	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
