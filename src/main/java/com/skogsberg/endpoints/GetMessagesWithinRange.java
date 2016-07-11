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

@Path("getMessagesWithinRange")
public class GetMessagesWithinRange {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getMessagesWithinRange(@Context UriInfo uriInfo) throws SQLException {

		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();

		// Parameters
		// TODO validation
		String recipient = queryParameters.getFirst("recipient");
		String start = queryParameters.getFirst("start");
		String end = queryParameters.getFirst("end");

		// TODO use Prepared Statement
		String queryString = MessageUtils.buildMessagesQueryString(recipient, start, end);
		ResultSet rs = DB.rawQuery(queryString);
		
		// TODO use ORM mapping with JAXB or similar instead of manual JSON generation
		return MessageUtils.writeMessages(rs);
	}
}
