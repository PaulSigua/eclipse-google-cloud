package ec.edu.ups.ppw64.demo64.services;

import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import config.ConfigJaeger;
import ec.edu.ups.ppw64.demo64.business.GestionMensajesUsuarios;
import ec.edu.ups.ppw64.demo64.model.MensajesUsuarios;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("mensaje")
public class MensajeUsuarioServices {

	private final Tracer tracer = GlobalTracer.get();
	
	@Inject
	private GestionMensajesUsuarios gMsjUsuarios;
	
	@Inject
	private ConfigJaeger configjaeger;
	
	private static final String SERVER_URL = "http://servidor-de-correos:puerto/email/enviar"; // Ajusta la URL y el puerto según tu configuración

    private CloseableHttpClient httpClient = HttpClients.createDefault();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crear(MensajesUsuarios mensajesUsuarios) {
        Span span = tracer.buildSpan("creacion_de_mensajes").start();
        try {
            gMsjUsuarios.guardarMensajesUsuarioss(mensajesUsuarios); // Suponiendo que esta línea guarda el mensaje de usuario

            // Aquí se envía el correo solo si se guardó el mensaje correctamente

            HttpPost request = new HttpPost(SERVER_URL);

            CloseableHttpResponse response = httpClient.execute(request);

            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 201) {
                    ErrorMessage error = new ErrorMessage(1, "Mensaje creado y correo enviado correctamente");
                    return Response.status(Response.Status.CREATED).entity(error).build();
                } else {
                    ErrorMessage error = new ErrorMessage(99, "Error al enviar el correo: " + response.getStatusLine().getReasonPhrase());
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(error)
                            .build();
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            ErrorMessage error = new ErrorMessage(99, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(error)
                    .build();
        } finally {
            span.finish();
        }
    }
	
/*	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response crear (MensajesUsuarios mensajesUsuarios) {
		Span span = tracer.buildSpan("creacion de mensajes").start();
		try{
			gMsjUsuarios.guardarMensajesUsuarioss(mensajesUsuarios);
			ErrorMessage error = new ErrorMessage(1, "ok");
			return Response.status(Response.Status.CREATED).entity(error).build();
		}catch (Exception e) {
			// TODO: handle exception
			ErrorMessage error = new ErrorMessage(99, e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(error)
					.build();
			
		} finally {
			span.finish();
		}
	}
*/
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response actualizar(MensajesUsuarios mensajeusuarios) {
		Span span = tracer.buildSpan("actualizar mensajes").start();
		try{
			gMsjUsuarios.actualizarMensajesUsuarios(mensajeusuarios);
			return Response.ok(mensajeusuarios).build();
		}catch (Exception e) {
			// TODO: handle exception
			ErrorMessage error = new ErrorMessage(99, e.getMessage());
			return Response.status(Response.Status.NOT_FOUND)
					.entity(error)
					.build();
		} finally {
			span.finish();
		}
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response borrar(@QueryParam("id") int codigo) {
		Span span = tracer.buildSpan("borrar mensajes").start();
	    try {
	        gMsjUsuarios.borrarCliente(codigo);
	        return Response.ok("OK, se borró el Usuario").build();
	    } catch (Exception e) {
	        ErrorMessage error = new ErrorMessage(99, "Error al eliminar el Usuario: " + e.getMessage());
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                .entity(error)
	                .build();
	    }finally {
			span.finish();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("list")
	public Response getClientes(){
		List<MensajesUsuarios> mensajes = gMsjUsuarios.getMensajes();
		if(mensajes.size()>0)
			return Response.ok(mensajes).build();
		
		ErrorMessage error = new ErrorMessage(6, "No se registran clientes");
		return Response.status(Response.Status.NOT_FOUND)
				.entity(error)
				.build();
	}
	
}
