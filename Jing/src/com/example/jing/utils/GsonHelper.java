package com.example.jing.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GsonHelper {
	public static String getAsString(JsonElement elem, String defaultValue) {
		if (elem != null)
			return elem.getAsString();
		return defaultValue;
	}

	public static int getAsInt(JsonElement elem, int defaultValue) {
		if (elem != null)
			return elem.getAsInt();
		return defaultValue;
	}

	public static long getAsLong(JsonElement elem, long defaultValue) {
		if (elem != null)
			return elem.getAsLong();
		return defaultValue;
	}

	public static JsonObject getAsJsonObject(JsonElement elem,
			JsonObject defaultValue) {
		if (elem != null)
			return elem.getAsJsonObject();
		return defaultValue;
	}

}
