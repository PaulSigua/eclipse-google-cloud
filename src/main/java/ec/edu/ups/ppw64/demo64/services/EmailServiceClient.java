package ec.edu.ups.ppw64.demo64.services;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class EmailServiceClient {

    // URLs de los servidores de correo
    private static final String SERVER_URL_1 = "http://35.184.173.35:8080/mail/rs/email/enviar";
    private static final String SERVER_URL_2 = "http://34.121.27.10:8080/mail/rs/email/enviar";

    private RestTemplate restTemplate = new RestTemplate();

    public EmailServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    public void enviarCorreo() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Construir el cuerpo de la solicitud
        HttpEntity<String> entity = new HttpEntity<>(headers);

        boolean enviado = false;
        
        try {
            // Intentar enviar la solicitud al SERVER_URL1
            ResponseEntity<String> response = restTemplate.postForEntity(SERVER_URL_1, entity, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("Correo enviado correctamente usando SERVER_URL1");
                enviado = true;
            } else {
                System.out.println("Error al enviar el correo usando SERVER_URL1: " + response.getBody());
            }
        } catch (HttpClientErrorException e1) {
            // Si hay un error al enviar la solicitud al SERVER_URL1, enviar al SERVER_URL2
            System.out.println("Error al enviar el correo usando SERVER_URL1: " + e1.getMessage());
        }

        if (!enviado) {
            try {
                // Intentar enviar la solicitud al SERVER_URL2 si SERVER_URL1 no funcionó
                ResponseEntity<String> response = restTemplate.postForEntity(SERVER_URL_2, entity, String.class);

                if (response.getStatusCode() == HttpStatus.CREATED) {
                    System.out.println("Correo enviado correctamente usando SERVER_URL2");
                } else {
                    System.out.println("Error al enviar el correo usando SERVER_URL2: " + response.getBody());
                }
            } catch (HttpClientErrorException e2) {
                // Si hay un error también con SERVER_URL2, mostrar el mensaje de error correspondiente
                System.out.println("Error al enviar el correo usando SERVER_URL2: " + e2.getMessage());
            }
        }
    }
}
