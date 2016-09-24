package com.biprom.eriks.telem.ui;

import com.biprom.eriks.telem.dao.CpuConfigRepository;
import com.biprom.eriks.telem.model.CpuConfigBean;
import com.vaadin.data.Item;
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

import java.util.Iterator;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

@SpringView(name = HardwareView.VIEW_NAME)
public class HardwareView extends HardwareDesign implements View {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	CpuConfigRepository reposi;
	
	CpuConfigBean test;

	public static final String VIEW_NAME = "hardware";

	public static final double minSliderValue = 0;

	public static final double maxSliderValue = 8;
	
	BeanItemContainer<CpuConfigBean> beanItemContainer =
		    new BeanItemContainer<CpuConfigBean>(CpuConfigBean.class);
	

	public HardwareView() {


		//Set up grid
		
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
							beanItemContainer =  (BeanItemContainer<CpuConfigBean>) grid.getContainerDataSource();
							CpuConfigBean cpuBean = new CpuConfigBean("init","init","init");
							for( Iterator<CpuConfigBean> i = beanItemContainer.getItemIds().iterator(); i.hasNext();){
								
								
								//get the current item identifier , which is an Integer
								
								CpuConfigBean iid=  i.next();
						
								
								//now get the actual item from the container
								
								Item item = beanItemContainer.getItem(iid);
								System.out.println("iid = "+ item.toString());
								
								System.out.println("cardnumber = "+ item.getItemProperty("cardNumber").getValue());
								System.out.println("ioname = "+ item.getItemProperty("ioName").getValue());
								System.out.println("ionumber = "+ item.getItemProperty("ioNumber").getValue());
								
								
								cpuBean.setCardNumber((String)item.getItemProperty("cardNumber").getValue()) ;   
								cpuBean.setIoName((String)item.getItemProperty("ioName").getValue()) ; 
								cpuBean.setIoNumber((String)item.getItemProperty("ioNumber").getValue()) ;
						
									reposi.deleteAll();
									//test = reposi.save(cpuBean);
									//Assert.assertNotNull(test.getId());
								
								System.out.println("params are written to database");
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
