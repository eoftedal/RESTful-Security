package restful.insecurity;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.util.FeaturesAndProperties;

public class Main {

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost/").port(9998).build();
	}

	public static final URI BASE_URI = getBaseURI();

	protected static HttpServer startServer() throws IOException {
		System.out.println("Starting grizzly...");
		ResourceConfig rc = new PackagesResourceConfig("restful.insecurity.resources");
		HashMap<String, Object> config = new HashMap<String, Object>();
		config.put(FeaturesAndProperties.FEATURE_DISABLE_XML_SECURITY, Boolean.TRUE);
		config.put(FeaturesAndProperties.FEATURE_FORMATTED, Boolean.TRUE);
		rc.setPropertiesAndFeatures(config);
		
		return GrizzlyServerFactory.createHttpServer(BASE_URI, rc);
	}
	

	public static void main(String[] args) throws IOException {
		System.err.println("Bruk mvn jetty:run -Djetty.port=9998");
		System.exit(1);
		HttpServer httpServer = startServer();
		/*JspServlet servlet = new JspServlet();
		WebappContext ctx = new WebappContext("J-to-the-S-to-the-P", "/", "src/main/webapp");
		ServletRegistration reg = ctx.addServlet("JayEssPee", servlet);
		reg.addMapping("*.jsp");
		FilterRegistration filterReg = ctx.addFilter("AuthFilter", new AuthFilter());

		filterReg.addMappingForUrlPatterns(null , "/app/*");
		ctx.deploy(httpServer);
		*/
		System.out
				.println(String
						.format("Jersey app started with WADL available at "
								+ "%sapplication.wadl\nTry out %shelloworld\nHit enter to stop it...",
								BASE_URI, BASE_URI));
		System.in.read();		
		httpServer.stop();
	}
}