package com.biprom.eriks.telem.model;

import com.google.gson.annotations.Expose;
import io.mappedbus.MappedBusMessage;
import io.mappedbus.MemoryMappedFile;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * @author Kristof
 *         Created on 06/10/16.
 */
@Document(collection = "reading")
public class SensorReading implements MappedBusMessage {

	@Id
	@Expose
	private String id;

	@Indexed
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Field("d")
	@Expose
	private Date time;

	@Indexed
	@Field("t")
	@Expose
	private String type;

	@Field("v")
	@Expose
	private Double value;

	@Field("s")
	private Boolean synched;

	public SensorReading(Date d, String t, Double m) {
		this.time = d;
		this.type = t;
		this.value = m;
	}

	public SensorReading() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Boolean getSynched() {
		return synched;
	}

	public void setSynched(Boolean synched) {
		this.synched = synched;
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
		mem.getBytes(pos + 8 + 8 + 4, typeBytes, 0, typeLength);
		setType(new String(typeBytes));
	}

	public static byte[] toByteArray(double value) {
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes).putDouble(value);
		return bytes;
	}

	public static double toDouble(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getDouble();
	}

	public int type() {
		return 0;
	}
}
