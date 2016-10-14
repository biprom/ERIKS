package com.biprom.workcycle;

import io.mappedbus.MappedBusMessage;
import io.mappedbus.MemoryMappedFile;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * @author Kristof
 *         Created on 12/10/16.
 */
public class SensorReading implements MappedBusMessage {

	private Date time;

	private String type;

	private Double value;

	public SensorReading(Date time, String type, Double value) {
		this.time = time;
		this.type = type;
		this.value = value;
	}

	public SensorReading() {
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public void write(MemoryMappedFile mem, long pos) {
		mem.putLong(pos, time.getTime());
		mem.setBytes(pos + 8, toByteArray(value), 0, 8);
		mem.putInt(pos + 8 + 8, type.getBytes().length);
		mem.setBytes(pos + 8 + 8 + 4, type.getBytes(), 0, type.getBytes().length);
	}

	public void read(MemoryMappedFile mem, long pos) {
		setTime(new Date(mem.getLong(pos)));

		byte[] bytes = new byte[8];
		mem.getBytes(pos + 8, bytes, 0, 8);
		setValue(toDouble(bytes));

		final int typeLength = mem.getInt(pos + 8 + 8);
		byte[] typeBytes = new byte[typeLength];
		mem.getBytes(pos + 8 + 8, typeBytes, 0, typeLength);
	}

	public int type() {
		return 0;
	}


	public static byte[] toByteArray(double value) {
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes).putDouble(value);
		return bytes;
	}

	public static double toDouble(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getDouble();
	}
}
