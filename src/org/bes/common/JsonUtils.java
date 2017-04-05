package org.bes.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	
	private static DateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static DateFormat df;
	
	private static ObjectMapper createObjectMapper(DateFormat df) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		objectMapper.setDateFormat(df == null ? defaultDateFormat : df);
		return objectMapper;
	}
	
	public void setDateFormat(DateFormat df) {
		JsonUtils.df = df;
	}

	public static String fromObject(Object object) {
		try {
			return createObjectMapper(df).writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			df = null;
		}
	}
	
	public static String fromArray(Collection<?> collect) {
		if (collect == null) {
			return "[]";
		}
		try {
			return fromObject(collect);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			df = null;
		}
	}
	
	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return createObjectMapper(df).readValue(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			df = null;
		}
	}
	
	public static <T> T fromJson(String json, TypeReference<T> typeRef) {
		try {
			return createObjectMapper(df).readValue(json, typeRef);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			df = null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseJSON2Map(String json) {
		try {
			return createObjectMapper(df).readValue(json, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			df = null;
		}
	}
	
}
