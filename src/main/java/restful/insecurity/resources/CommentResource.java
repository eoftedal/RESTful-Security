package restful.insecurity.resources;

import java.util.Collection;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import restful.insecurity.HackeFilter;

import com.sun.jersey.spi.container.ResourceFilters;

@Path("/comments")
@Component
@Scope("request")
@ResourceFilters({HackeFilter.class })
public class CommentResource {
	private static Hashtable<Long, Comment> comments = new Hashtable<Long, Comment>();
	private static long id = 0;
	
	static {
		createComment("First!", "Oh yes sir!");
		createComment("Nice post", "I like this");
	}

	private static void createComment(String title, String body) {
		Comment comment = new Comment();
		comment.setId(id++);
		comment.title = title;
		comment.body = body;
		comment.username = "erlend";
		comments.put(comment.id, comment);
	}
	
	@Context UriInfo uriInfo;
	@Context HttpServletRequest request;
	
	@POST
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Comment create(Comment newComment ) {
		String username = request.getSession().getAttribute("username").toString();
		newComment.setId(id++);
		newComment.username = username;
		comments.put(newComment.id, newComment);
		return newComment;
	}

	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Collection<Comment> index() {
		return comments.values();
	}

	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("{id}")
	public Comment show(@QueryParam("id") long id) {
		if (comments.containsKey(id)) {
			return comments.get(id);
		} else {
			throw new WebApplicationException(404);
		}
	}

	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("show")
	public Comment showSingle(@QueryParam("id") Long id) {
		return show(id == null ? -1 : id);
	}
	
}
