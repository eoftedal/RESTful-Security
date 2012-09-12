package restful.insecurity.resources;

import java.lang.reflect.Field;
import java.util.Hashtable;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import restful.insecurity.HackeFilter;

import com.sun.jersey.spi.container.ResourceFilters;

@Path("/users")
@Component
@Scope("request")
@ResourceFilters({HackeFilter.class })
public class UserResource {
	private static Hashtable<Long, User> users = new Hashtable<Long, User>();
	private static long id = 0;
	
	public static User getUser(String username)  {
		for (User u : users.values()) {
			if (u.username.equals(username)) {
				return u;
			}
		}
		return null;
	}
	
	static {
		addUser("erlend", "erlend@oftedal.no", "Erlend", "Oftedal");
	}
	private static void addUser(String username, String email, String firstName, String lastName) {
		User user = new User(id++, username, email, firstName, lastName);
		user.created = DateTime.now().toLocalDateTime().toString();
		users.put(user.id, user);
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("{id}")
	public User show(@PathParam("id") long id) {
		if (users.containsKey(id)) {
			return users.get(id);
		} else {
			throw new WebApplicationException(404);
		}
	}
	
	@POST
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("{id}")
	public User update(@PathParam("id") long id, User user) {
		try {
		if (users.containsKey(id)) {
			User u = users.get(id);
			for(Field f: User.class.getFields()) {
				if (f.get(user) != null) {
					f.set(u, f.get(user));
				}
			}
			return u;
		} else {
			throw new WebApplicationException(404);
		}
		} catch(Exception ex) {
			System.err.println(ex);
			throw new WebApplicationException(500);
		}
	}
	
}
