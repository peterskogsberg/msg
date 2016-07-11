package com.skogsberg.endpoints;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.skogsberg.DB;
import com.skogsberg.MessageUtils;

@Path("getNewMessages")
public class GetNewMessages {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getNewMessages(@Context UriInfo uriInfo) throws SQLException {

		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		String recipient = queryParameters.getFirst("recipient");

		// 1. Check 'last synced' timestamp for this recipient
		String lastSync = getLastSyncForRecipient(recipient);

		// 2. Get only new messages (timestamp > lastSync)
		String queryString = MessageUtils.buildMessagesQueryString(recipient, lastSync, null);
		ResultSet rs = DB.rawQuery(queryString);
		
		// 3. Update 'last synced' timestamp for this recipient
		setLastSyncForRecipient(recipient);

		// 4. Return messages as JSON
		return MessageUtils.writeMessages(rs);
	}

	private String getLastSyncForRecipient(String recipient) {
		String queryString = String.format("SELECT timestamp FROM lastsync WHERE name == '%s'", recipient);
		String ret = null;

		try {
			ResultSet rs = DB.rawQuery(queryString);
			while (rs.next()) {
				ret = rs.getString("timestamp");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			ret = "0";
		}
		return ret;
	}

	private void setLastSyncForRecipient(String recipient) {
		String queryString = String.format("UPDATE lastsync SET timestamp = '%s' WHERE name == '%s'",
				System.currentTimeMillis(), recipient);
		try {
			DB.rawQueryWithoutResponse(queryString);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
