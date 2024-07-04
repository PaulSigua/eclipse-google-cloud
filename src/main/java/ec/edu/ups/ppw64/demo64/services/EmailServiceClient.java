package ec.edu.ups.ppw64.demo64.services;

public class EmailServiceClient {

    public void enviarCorreo(Email email) {
        try {
            EmailProducer.sendEmailToQueue(email);
            System.out.println("Correo enviado correctamente a la cola");
        } catch (Exception e) {
            System.out.println("Error al enviar el correo a la cola: " + e.getMessage());
        }
    }
}
