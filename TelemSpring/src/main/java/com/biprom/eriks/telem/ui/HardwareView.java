package com.biprom.eriks.telem.ui;

import com.biprom.eriks.telem.dao.CpuConfigRepository;
import com.biprom.eriks.telem.model.CpuConfigBean;
import com.biprom.eriks.telem.model.MeasuredValues;
import com.google.gwt.thirdparty.guava.common.collect.Table;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.UI;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

@SpringView(name = HardwareView.VIEW_NAME)
public class HardwareView extends HardwareDesign implements View {
	
	@Autowired
	CpuConfigRepository reposi;

	public static final String VIEW_NAME = "hardware";

	public static final double minSliderValue = 0;

	public static final double maxSliderValue = 8;
	
	public Container indexedContainer = new IndexedContainer();
	

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

					@Autowired
					@Override
					public void onClose(ConfirmDialog arg0) {
						ConfirmDialog dialog = new ConfirmDialog();
						// TODO Auto-generated method stub
						if (dialog.isConfirmed()) {
							// Confirmed to continue
							// DO STUFF
							Container beanContainerEdited = new IndexedContainer();
							beanContainerEdited  = (IndexedContainer) grid.getContainerDataSource();
							
							CpuConfigBean bean = new CpuConfigBean();
							bean.setCardNumber("woord1");
							bean.setIoName("woord2");
							bean.setIoNumber("woord3");
							
							
							final CpuConfigBean save = reposi.save(bean);
							Assert.assertNotNull(save.getId());
							
							
																		
							
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
				
				indexedContainer.addContainerProperty("name", String.class, "defaultValue");
				indexedContainer.addContainerProperty("output", String.class, "defaultoutput");
				
				for (double i = 0; i < digoutSlider.getValue(); i++) {
					
//					cpuConfBeans.addBean(new CpuConfigBean("digital out card " + i, "output 2", "name"));
//					cpuConfBeans.addBean(new CpuConfigBean("digital out card " + i, "output 3", "name"));
//					cpuConfBeans.addBean(new CpuConfigBean("digital out card " + i, "output 4", "name"));
//					cpuConfBeans.addBean(new CpuConfigBean("digital out card " + i, "output 5", "name"));
//					cpuConfBeans.addBean(new CpuConfigBean("digital out card " + i, "output 6", "name"));
//					cpuConfBeans.addBean(new CpuConfigBean("digital out card " + i, "output 7", "name"));
//					cpuConfBeans.addBean(new CpuConfigBean("digital out card " + i, "output 8", "name"));
					
				}
				
				for (double i = 0; i < diginSlider.getValue(); i++) {
//					cpuConfBeans.addBean(new CpuConfigBean("digital in card " + i, "input 1", "name"));
//					cpuConfBeans.addBean(new CpuConfigBean("digital in card " + i, "input 2", "name"));
//					cpuConfBeans.addBean(new CpuConfigBean("digital in card " + i, "input 3", "name"));
//					cpuConfBeans.addBean(new CpuConfigBean("digital in card " + i, "input 4", "name"));
//					cpuConfBeans.addBean(new CpuConfigBean("digital in card " + i, "input 5", "name"));
//					cpuConfBeans.addBean(new CpuConfigBean("digital in card " + i, "input 6", "name"));
//					cpuConfBeans.addBean(new CpuConfigBean("digital in card " + i, "input 7", "name"));
//					cpuConfBeans.addBean(new CpuConfigBean("digital in card " + i, "input 8", "name"));
//					
				}
				
				for (double i = 0; i < anainpSlider.getValue(); i++) {
//					cpuConfBeans.addBean(new CpuConfigBean("analog in card " + i, "input 1", "name"));
//					cpuConfBeans.addBean(new CpuConfigBean("analog in card " + i, "input 2", "name"));
//					cpuConfBeans.addBean(new CpuConfigBean("analog in card " + i, "input 3", "name"));
//					cpuConfBeans.addBean(new CpuConfigBean("analog in card " + i, "input 4", "name"));
//					

				}
				
				for (double i = 0; i < anaoutputSlider.getValue(); i++) {
//					cpuConfBeans.addBean(new CpuConfigBean("analog out card " + i, "output 1", "name"));
//					


				}
				
				grid.setContainerDataSource((Indexed) indexedContainer);
				
				
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
