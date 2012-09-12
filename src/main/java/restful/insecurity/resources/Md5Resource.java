package restful.insecurity.resources;

import java.net.URLEncoder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.bouncycastle.crypto.digests.GeneralDigest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import restful.insecurity.DigestExtender;
import restful.insecurity.HackeFilter;

import com.sun.jersey.spi.container.ResourceFilters;


@Path("/md5")
@Component
@Scope("request")
@ResourceFilters({HackeFilter.class })
public class Md5Resource {

	@GET
	public Response get(@QueryParam("md5") String md5, @QueryParam("d") String newData, @QueryParam("l") int length) {
		try {
			GeneralDigest digester = new MD5Digest();
			DigestExtender extender = new DigestExtender();
			byte[] newDigest = extender.extend(digester, Hex.decode(md5), newData.getBytes());
			Md5Hash hash = new Md5Hash();
			hash.newHash = new String(Hex.encode(newDigest));
			hash.pad = URLEncoder.encode(urlEncode(extender.getPad(digester, length))); 
			return Response.ok().entity(hash).build();
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
			return Response.serverError().build();
		}
	}

	private String urlEncode(byte[] pad) {
		StringBuilder result = new StringBuilder();
		for (byte b : pad) {
			if ((b >= 48 && b <= 57) || (b >= 65 && b <= 90) || (b >= 97 && b <= 122)) {
				result.append((char)b);
			} else {
				String h = new String(Hex.encode(new byte[] {b}));
				result.append("%" + h);
			}
		}
		return result.toString();
	}
}
