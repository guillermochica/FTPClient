import java.net.*;

public class ClienteFTP {
	
	private UsuarioFTP user; //Usuario FTP con el que me voy a conectar
	private InetAddress ip; //dir IP del servidor
	//Atributos de socket
	
	
	public ClienteFTP(UsuarioFTP u, InetAddress ip) { 
		user = u;
		this.ip = ip;
	}
	
	public void subirFichero(String fichero) {
		
	}
}
