package org.cloudfoundry.support.supportservices;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

@Component
public class MessageLogErrorHandler extends DefaultResponseErrorHandler {

	private static Logger logger = Logger
			.getLogger(MessageLogErrorHandler.class);

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		StringWriter stringWriter = new StringWriter();
		IOUtils.copy(response.getBody(), stringWriter);
		logger.error(stringWriter.toString());
		super.handleError(response);
	}

}
