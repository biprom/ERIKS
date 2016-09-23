package com.biprom.eriks.telem.ui;


import com.biprom.eriks.telem.dao.PersonsRepository;
import com.biprom.eriks.telem.model.Person;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;
import org.springframework.aop.framework.adapter.UnknownAdviceTypeException;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = LoginView.VIEW_NAME)
public class LoginView extends LoginDesign implements View {

	@Autowired
	private PersonsRepository personsRepository;

	public static final String VIEW_NAME = "";

	public LoginView() {

		validateButton.addClickListener(event -> {
			UI ui = UI.getCurrent();
			Navigator navigator = ui.getNavigator();

			navigator.navigateTo(MainView.VIEW_NAME);

		});
	}

	@Override
	public void enter(ViewChangeEvent event) {

		try {
			personsRepository.deleteAll();
			Person britney = new Person("Bram", "Vandenberghe", "Bram", "1112","bram@biprom.com");
			personsRepository.save(britney);


		} catch (UnknownAdviceTypeException e) {
			e.printStackTrace();
		}


	}

}
