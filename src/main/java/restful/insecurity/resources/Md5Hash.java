package restful.insecurity.resources;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Md5Hash {
	public String newHash;
	public String pad;
	
	public Md5Hash() {
		
	}
	
}
