package restful.insecurity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import restful.insecurity.resources.Comment;
import restful.insecurity.resources.UserResource;

public class AuthFilter implements Filter {

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest)request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			if (httpRequest.getMethod().equals("OPTIONS")) {
				httpResponse.setStatus(200);
				addAccessControlAndClose(httpResponse);
				return;
			}
			HttpSession session = httpRequest.getSession();
			if (session.getAttribute("username") == null) {
				//Super duper security for logins here... Just keeping things simple for demos
				String username = httpRequest.getParameter("username");
				String header = httpRequest.getHeader("Authorization");
				if (username != null ) { 
					if (UserResource.getUser(username) != null) {
						session.setAttribute("username", username);
					}
				} else if (header != null) {
					String u = header.replace("nosecurity ", "");
					if (UserResource.getUser(u) != null) {
						session.setAttribute("username", u);
					}
				} else {
					if ("/comments/show".equals(httpRequest.getPathInfo())) {
						Map params = httpRequest.getParameterMap();
						ArrayList<String> list = new ArrayList<String>();
						for(Object o : params.keySet()) { //Read out all params except signature
							if (!o.equals("signature"))
								list.add((String)o);
						}
						Collections.sort(list); //sort params
						ArrayList<Byte> signData = new ArrayList<Byte>(); 
						addBytes(signData, Comment.KEY);
						for(String s : list) { //Add params and values to data to be signed
							String v = ((String[])params.get(s))[0];
							byte[] val = urlDecode(v); //this line is a security bug - we are doing double url decoding
							addBytes(signData, s);
							addBytes(signData, val);
						}
						byte[] signBytes = toByteArray(signData);
						String hex = Comment.digest(signBytes);
						if (params.containsKey("signature")) {
							String inSignature = ((String[])params.get("signature"))[0];
							if (hex.equals(inSignature)) {
								chain.doFilter(request, response);
								return;
							}
						}
					}
					httpResponse.setStatus(401, "Not authenticated");
					httpResponse.addHeader("Location", "/login.jsp");
					addAccessControlAndClose(httpResponse);
					return;
				}
			}
		}
		chain.doFilter(request, response);
	}

	private void addAccessControlAndClose(HttpServletResponse httpResponse)
			throws IOException {
		for(String s : HackeFilter.headers.keySet()) {
			httpResponse.addHeader(s, HackeFilter.headers.get(s));
		}
		httpResponse.getOutputStream().flush();
		httpResponse.getOutputStream().close();
	}
	private void addBytes(ArrayList<Byte> list, String v) {
		addBytes(list, v.getBytes());
	}
	private void addBytes(ArrayList<Byte> list, byte[] v) {
		for(byte b : v) {
			list.add(b);
		}
	}
	
	private byte[] toByteArray(List<Byte> bytes) {
		byte[] result = new byte[bytes.size()];
		for (int j = 0; j < bytes.size(); j++) {
			result[j] = bytes.get(j);
		}
		return result;
		
	}
	
	//Url decode to byte array instead of string
	private byte[] urlDecode(String v) {
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		int i = 0;
		while(i < v.length()) {
			if (v.charAt(i) == '%') {
				bytes.add((byte)Integer.parseInt(v.substring(i + 1, i + 3), 16));
				i = i + 3;
			} else {
				bytes.add((byte)v.charAt(i));
				i++;
			}
		}
		return toByteArray(bytes);
	}
	
	

	@Override
	public void init(FilterConfig config) throws ServletException {
		
	}
	
}
