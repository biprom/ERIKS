import io.mappedbus.MappedBusMessage;
import io.mappedbus.MemoryMappedFile;

public class TemperatureReading implements MappedBusMessage {
	
	/**
	 * @author Biprom
	 *         Created on 18/12/16.
	 */

	
	private int maxTemp;
	
	private int minTemp;
	
	public TemperatureReading(int minTemp, int maxTemp){
		this.minTemp = minTemp;
		this.maxTemp = maxTemp;
	}
	
	public setMinTemp (int tempmin){
		this.minTemp = tempmin;
	}
	
	public setMaxTemp (int tempmax){
		this.maxTemp = tempmax;
	}
	
	public getMinTemp(){
		return minTemp;
	}
	
	public getMaxTemp(){
		return maxTemp;
	}
	
	@Override
	public void write(MemoryMappedFile mem, long pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void read(MemoryMappedFile mem, long pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public int type() {
		// TODO Auto-generated method stub
		return 0;
	}

}
