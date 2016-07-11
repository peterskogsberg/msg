package com.skogsberg;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.util.JSONBuilder;

public class MessageUtils {

	/*
	 * JSON related methods
	 */
	public static String writeMessages(ResultSet rs) throws SQLException {
		StringWriter sw = new StringWriter();
		JSONBuilder jb = new JSONBuilder(sw)
		
		// 1. Wrapping JSON object
		.object()
		.key("messages")
	    .array();
				
		// 2. Iterate over ResultSet rows, insert messages into the array
		while (rs.next()) {
			String id = rs.getString("id");
			String msg = rs.getString("message");
			String timestamp = rs.getString("timestamp");
			
			jb
			.object()
				.key("id").value(id)
				.key("message").value(msg)
				.key("timestamp").value(timestamp)
			.endObject();
		}
		
		// 3. Close the wrapping object and serialize to String
		
	    jb.endArray()
		.endObject();
	    return sw.toString();	
	}
	
	public static String writeStatus(String s) {
		StringWriter sw = new StringWriter();
		@SuppressWarnings("unused")
		JSONBuilder jb = new JSONBuilder(sw)
		.object()
			.key("status")
			.value(s)
		.endObject();
		
		return sw.toString();
	}
	
	/*
	 * SQLite related functionality
	 */
	
	public static String buildMessagesQueryString(String recipient, String start, String end) {
		String ret = String.format("SELECT * FROM messages WHERE recipient == '%s'", recipient);

		if (start != null) {
			ret += String.format(" AND timestamp >= %s", start);

			if (end != null) {
				ret += String.format(" AND timestamp <= %s", end);
			}
		}

		ret += " ORDER BY timestamp ASC";
		return ret;
	}	
}
