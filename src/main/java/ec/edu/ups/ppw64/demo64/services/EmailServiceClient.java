package ec.edu.ups.ppw64.demo64.services;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class EmailServiceClient {

    private static final String SERVER_URL = "http://35.184.173.35:8080/mail/rs/email/enviar"; // Ajusta la URL y el puerto según tu configuración
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
            ResponseEntity<String> response = restTemplate.postForEntity(SERVER_URL, entity, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("Correo enviado correctamente");
            } else {
                System.out.println("Error al enviar el correo: " + response.getBody());
            }
        } catch (HttpClientErrorException e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }
}
