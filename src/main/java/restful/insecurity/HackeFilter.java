package restful.insecurity;


import java.util.Hashtable;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class HackeFilter implements ResourceFilter, ContainerRequestFilter, ContainerResponseFilter{

	public static Hashtable<String, String> headers = new Hashtable<String, String>();
	static {
		headers.put("Access-Control-Allow-Origin", "*");
		headers.put("Access-Control-Allow-Credentials", "true");
		headers.put("Access-Control-Allow-Headers", "accept, content-type, origin, authorization");

	}
	
	@Override
	public ContainerRequest filter(ContainerRequest request) {
		MultivaluedMap<String, String> headers = request.getRequestHeaders();
		String s = request.getHeaderValue("Content-Type");
		if ("text/plain".equals(s)) { //Let's pretend we accept text/plain as XML
			headers.remove("Content-Type");
			headers.add("Content-Type", "application/xml");
			InBoundHeaders iheaders = new InBoundHeaders();
			for(String k: headers.keySet()) {
				iheaders.add(k, headers.getFirst(k));
			}
			request.setHeaders(iheaders);
		}
		return request;
	}

	@Override
	public ContainerRequestFilter getRequestFilter() {
		return this;
	}

	@Override
	public ContainerResponseFilter getResponseFilter() {
		return this;
	}

	@Override
	public ContainerResponse filter(ContainerRequest request,
			ContainerResponse response) {
		for (String s : headers.keySet()) {
			response.getHttpHeaders().add(s, headers.get(s));	
		}
		return response;
	}


}
