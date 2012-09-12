package restful.insecurity.resources;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.util.encoders.Hex;

@XmlRootElement
public class Comment {
	public static final String KEY = "gaffel";

	public Long id;
	public String username;
	public String body;
	public String title;
	public Date date = new Date();
	public String signedUrl;

	public Comment() {
		
	}
	
	public void setId(Long id) {
		this.id = id;
		signedUrl = getSignedUrl();
	}

	private String getSignedUrl() {
		byte[] signData = (KEY + "id" + id).getBytes();
		String b64 = digest(signData);
		return "/rest/comments/show?id=" + id + "&signature=" + b64;

	}

	public static String digest(byte[] signData) {
		try {
			MD5Digest digest = new MD5Digest();
			digest.update(signData, 0, signData.length);
			byte[] signed = new byte[digest.getDigestSize()];
			digest.doFinal(signed, 0);
			return new String(Hex.encode(signed));
		} catch (Exception ex) {
			return "error";
		}
	}
}
