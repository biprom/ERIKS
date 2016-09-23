package com.biprom.eriks.telem.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;

@SpringView(name = UserView.VIEW_NAME)
public class UserView extends UserDesign implements View {

	public static final String VIEW_NAME = "User";

	public UserView() {

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
