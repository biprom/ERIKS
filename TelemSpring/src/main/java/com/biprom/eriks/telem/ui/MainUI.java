package com.biprom.eriks.telem.ui;


import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
@Theme("mytheme")
public class MainUI extends UI {
	//commentaar
	private Navigator navigator;

	@Autowired
	private SpringViewProvider viewProvider;


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void init(VaadinRequest request) {
		// TODO Auto-generated method stub
		setContent(new Label("Hello from ui"));

//
		navigator = new Navigator(this, this);
		navigator.addProvider(viewProvider);
//
//		navigator.addView(LoginView.VIEW_NAME, viewProvider.getView(LoginView.VIEW_NAME));
//		navigator.addView(MainView.VIEW_NAME, viewProvider.getView(MainView.VIEW_NAME));


       /* //set and reset digital output
		DigOutput_PCF digiout = new DigOutput_PCF(I2CBus.BUS_1, PCF8574GpioProvider.PCF8574A_0x3F);
        digiout.setOutputHigh(digiout.d1);
        digiout.setOutputLow(digiout.d1);
		
        //set and reset digital input module
        DigInput_PCF digiin = new DigInput_PCF(I2CBus.BUS_1, PCF8574GpioProvider.PCF8574A_0x3E);
        */

	}

}
