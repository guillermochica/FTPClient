import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Principal {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		UsuarioFTP u = new UsuarioFTP("guillermo", "ftp");
		
		InetAddress ip = InetAddress.getLocalHost();
		ClienteFTP c = new ClienteFTP(u);
		
		c.conectar(ip, 21);
		c.subirFichero("paraEnviar.txt");
		c.desconectar();
		
		
		

	}

}
