import java.io.IOException;
import java.util.Scanner;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Principal {

	public static void main(String[] args) throws IOException {
		Scanner s=new Scanner(System.in);
		
		System.out.print("Ingrese usuario: ");
		String user = s.next();
		System.out.print("Ingrese contraseña: ");
		String pass = s.next();
		System.out.print("Escriba p para modo pasivo o a para activo: ");
		String m = s.next();
		
		boolean modo=false;
		
		if(m.equals("p")) {
			modo=false;
		}
		else if(m.equals("a")){
			modo=true;
		}
		else{
			System.out.println("Error: conectando en modo pasivo por defecto");
			modo=false;
		}

		UsuarioFTP u = new UsuarioFTP(user, pass);
		
		InetAddress ip = InetAddress.getLocalHost();
		ClienteFTP c = new ClienteFTP(u,modo);
		
		c.conectar(ip, 21);
		
		//Si nos hemos logueado con exito
		if(u.getLogged()) {
			System.out.print("Ingrese el nombre del fichero (p. ej.: paraEnviar.txt): ");
			String fichero = s.next();
			c.subirFichero(fichero, ip);
			c.desconectar();
		}
		
		
		
		

	}

}
