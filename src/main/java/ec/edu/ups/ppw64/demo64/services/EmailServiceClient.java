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

        try {
            // Intentar enviar la solicitud al primer servidor
            ResponseEntity<String> response = restTemplate.postForEntity(SERVER_URL_1, entity, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("Correo enviado correctamente");
                return; // Salir del método si el correo se envió correctamente
            } else {
                System.out.println("Error al enviar el correo desde " + SERVER_URL_1 + ": " + response.getBody());
            }
        } catch (HttpClientErrorException e) {
            System.out.println("Error al enviar el correo desde " + SERVER_URL_1 + ": " + e.getMessage());
        }

        // Si falla el primer intento, intentar con el segundo servidor
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(SERVER_URL_2, entity, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("Correo enviado correctamente");
            } else {
                System.out.println("Error al enviar el correo desde " + SERVER_URL_2 + ": " + response.getBody());
            }
        } catch (HttpClientErrorException e) {
            System.out.println("Error al enviar el correo desde " + SERVER_URL_2 + ": " + e.getMessage());
        }
    }
}
