package com.biprom.eriks.telem.ui;

import com.biprom.eriks.telem.dao.CpuConfigRepository;
import com.biprom.eriks.telem.model.CpuConfigBean;
import com.vaadin.data.Container;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.Collection;

@SpringView(name = HardwareView.VIEW_NAME)
public class HardwareView extends HardwareDesign implements View {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	CpuConfigRepository reposi;

	public static final String VIEW_NAME = "hardware";

	public static final double minSliderValue = 0;

	public static final double maxSliderValue = 8;

	BeanItemContainer<CpuConfigBean> beanItemContainer =
			new BeanItemContainer<CpuConfigBean>(CpuConfigBean.class);


	public HardwareView() {


		//Set up grid
		// TODO Auto-generated method stub

		//grid.addColumn("IO- Card");
		//grid.addColumn("IO- Number");
		//grid.addColumn("IO- Name");


		//set scope of sliders from 0 tot 8
		digoutSlider.setMin(minSliderValue);
		digoutSlider.setMax(maxSliderValue);
		digoutSlider.addValueChangeListener(new ValueChangeListener() {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Notification.show("Attention! Sildervalue has changed to " + event.getProperty().getValue());


			}

		});

		diginSlider.setMin(minSliderValue);
		diginSlider.setMax(maxSliderValue);
		diginSlider.addValueChangeListener(new ValueChangeListener() {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {

				Notification.show("Attention! Sildervalue has changed to " + event.getProperty().getValue());


			}

		});

		anainpSlider.setMin(minSliderValue);
		anainpSlider.setMax(maxSliderValue);
		anainpSlider.addValueChangeListener(new ValueChangeListener() {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Notification.show("Attention! Sildervalue has changed to " + event.getProperty().getValue());


			}

		});

		anaoutputSlider.setMin(minSliderValue);
		anaoutputSlider.setMax(maxSliderValue);
		anaoutputSlider.addValueChangeListener(new ValueChangeListener() {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Notification.show("Attention! Sildervalue has changed to " + event.getProperty().getValue());


			}

		});


		// Handle the events with an anonymous class
		hardwareButton.addClickListener(new Button.ClickListener() {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// set parameters of writeDBBean through setters
				// The quickest way to confirm
				// The quickest way to confirm
				ConfirmDialog.show(UI.getCurrent(), "Changes will have big effect on the Database, please contact the IT manager! Press cancel to undo", new ConfirmDialog.Listener() {

					/**
					 *
					 */
					private static final long serialVersionUID = 1L;

					@SuppressWarnings("unchecked")
					@Autowired
					@Override
					public void onClose(ConfirmDialog dialog) {

						// TODO Auto-generated method stub
						if (dialog.isConfirmed()) {
							// Confirmed to continue
							// DO STUFF
							final Container.Indexed dataSource = grid.getContainerDataSource();
							final Collection<CpuConfigBean> itemIds = (Collection<CpuConfigBean>) dataSource.getItemIds();
							if (itemIds != null) {
								for (CpuConfigBean iid : itemIds) {


									System.out.println("iid = " + iid.toString());

									System.out.println("Cardnumber: " + iid.getCardNumber());
									System.out.println("ioName: " + iid.getIoName());
									System.out.println("ioNumber: " + iid.getIoNumber());


									System.out.println("params are written in bean");

									CpuConfigBean test = reposi.save(iid);

									System.out.println("params are written to database. (ID: " + test.getId() + ")");
								}
							}
						} else {
							// User did not confirm
							// CANCEL STUFF
						}


					}

				});

			}
		});

		configurationButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				for (double i = 0; i < digoutSlider.getValue(); i++) {


					beanItemContainer.addBean(new CpuConfigBean("digital out card " + i, "output 1", "name"));
					beanItemContainer.addBean(new CpuConfigBean("digital out card " + i, "output 2", "name"));
					beanItemContainer.addBean(new CpuConfigBean("digital out card " + i, "output 3", "name"));
					beanItemContainer.addBean(new CpuConfigBean("digital out card " + i, "output 4", "name"));
					beanItemContainer.addBean(new CpuConfigBean("digital out card " + i, "output 5", "name"));
					beanItemContainer.addBean(new CpuConfigBean("digital out card " + i, "output 6", "name"));
					beanItemContainer.addBean(new CpuConfigBean("digital out card " + i, "output 7", "name"));
					beanItemContainer.addBean(new CpuConfigBean("digital out card " + i, "output 8", "name"));

				}

				for (double i = 0; i < diginSlider.getValue(); i++) {
					beanItemContainer.addBean(new CpuConfigBean("digital in card " + i, "input 1", "name"));
					beanItemContainer.addBean(new CpuConfigBean("digital in card " + i, "input 2", "name"));
					beanItemContainer.addBean(new CpuConfigBean("digital in card " + i, "input 3", "name"));
					beanItemContainer.addBean(new CpuConfigBean("digital in card " + i, "input 4", "name"));
					beanItemContainer.addBean(new CpuConfigBean("digital in card " + i, "input 5", "name"));
					beanItemContainer.addBean(new CpuConfigBean("digital in card " + i, "input 6", "name"));
					beanItemContainer.addBean(new CpuConfigBean("digital in card " + i, "input 7", "name"));
					beanItemContainer.addBean(new CpuConfigBean("digital in card " + i, "input 8", "name"));

				}

				for (double i = 0; i < anainpSlider.getValue(); i++) {
					beanItemContainer.addBean(new CpuConfigBean("analog in card " + i, "input 1", "name"));
					beanItemContainer.addBean(new CpuConfigBean("analog in card " + i, "input 2", "name"));
					beanItemContainer.addBean(new CpuConfigBean("analog in card " + i, "input 3", "name"));
					beanItemContainer.addBean(new CpuConfigBean("analog in card " + i, "input 4", "name"));
//					

				}

				for (double i = 0; i < anaoutputSlider.getValue(); i++) {
					beanItemContainer.addBean(new CpuConfigBean("analog out card " + i, "output 1", "name"));
//					


				}

				grid.setContainerDataSource(beanItemContainer);


			}
		});


	}

	@Override
	public void enter(ViewChangeEvent event) {


		//BeanItemContainer container = new BeanItemContainer(CpuConfigBean.class);
		//container = (BeanItemContainer) repo.findAll();
		//grid.setContainerDataSource(container);
	}

}
