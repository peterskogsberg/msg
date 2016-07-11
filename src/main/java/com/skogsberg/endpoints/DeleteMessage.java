package com.skogsberg.endpoints;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang3.StringUtils;

import com.skogsberg.DB;
import com.skogsberg.MessageUtils;

@Path("deleteMessage")
public class DeleteMessage {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteMessage(MultivaluedMap<String, String> formParams) {
		// As per requirement, handling multiple ID's
		List<String> s = formParams.get("id");
		String joinedIds = StringUtils.join(s, ",");
		
		String stmt = String.format("DELETE FROM messages WHERE id IN (%s)", joinedIds);

		// Query does not produce ResultSet; using return value for success/error message instead
		String ret = null;
		try {
			DB.rawQueryWithoutResponse(stmt);
			ret = "Message(s) deleted!";
		} catch (SQLException e) {
			e.printStackTrace();
			ret = "Error: could not delete message(s)";
		}
		return MessageUtils.writeStatus(ret);

	}
}
