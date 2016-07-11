package com.skogsberg.endpoints;

import java.sql.SQLException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.skogsberg.DB;
import com.skogsberg.MessageUtils;

@Path("createNewMessage")
public class CreateNewMessage {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String createNewMessage(MultivaluedMap<String, String> formParams) {
		String message = formParams.getFirst("message");
		String recipient = formParams.getFirst("recipient");

		String stmt = String.format("INSERT INTO messages VALUES(null, '%s', '%s', '%s')", message, recipient,
				System.currentTimeMillis());

		// Query does not produce ResultSet; using return value for success/error message instead
		
		String ret = null;
		try {
			DB.rawQueryWithoutResponse(stmt);
			ret = String.format("Message sent to %s!", recipient);
		} catch (SQLException e) {
			e.printStackTrace();
			ret = "Error: could not sent message";
		}
		return MessageUtils.writeStatus(ret);

	}
}
