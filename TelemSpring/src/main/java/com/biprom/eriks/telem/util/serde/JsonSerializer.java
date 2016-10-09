package com.biprom.eriks.telem.util.serde;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author Kristof
 *         Created on 09/10/16.
 */
public class JsonSerializer<T> implements Serializer<T> {

	private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

	@Override
	public void configure(Map<String, ?> map, boolean b) {

	}

	@Override
	public byte[] serialize(String s, T t) {
		return gson.toJson(t).getBytes(Charset.forName("UTF-8"));
	}

	@Override
	public void close() {

	}
}
