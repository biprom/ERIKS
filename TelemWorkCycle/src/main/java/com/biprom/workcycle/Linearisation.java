package com.biprom.workcycle;

public class Linearisation {
	
	public Linearisation(){
		
	}
	
	//12 bit conversion (signed) min and max value are -2048 to 2048
	//total scope of 4096
	
	//scope from 4 tot 20mA is 16mA (starting from 4mA)

	public static double amplification12bit4to20mA(double val){
		final double mesValue = val+2048;	// 4 to 20mA /total X range on 18 bit dataconversion = 4+ (16/262142)
		double value = 4 + 16*(mesValue/4095);
		return value;
		
	}
	
	public static double amplification12bit0to10V(double val){
		final double scope = val+2048;	// 4 to 20mA /total X range on 18 bit dataconversion = 4+ (16/262142)
		double value = 10*(scope/4095);
		return value;
		
	}
	
	public double convertAnalog4To20mAToValue(double val, double rangemin, double rangemax, double offset){
			double mesValue = val*((rangemax - rangemin)/16);
		return mesValue;
	}
	
	public double convertAnalog0To10VToValue(double val, double rangemin, double rangemax, double offset){
		double mesValue = val*((rangemax - rangemin)/10);
	return mesValue;
}
	
}
