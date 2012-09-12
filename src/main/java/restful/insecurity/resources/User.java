package restful.insecurity.resources;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
	public Long id;
	public String username;
	public String first_name;
	public String last_name;
	public String email;
	public String created;

	public User() {
		
	}
	
	public User(long id, String username, String email, String firstName, String lastName) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.first_name = firstName;
		this.last_name = lastName;
	}

}
