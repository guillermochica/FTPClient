import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class ClienteFTP {

	private UsuarioFTP user; // Usuario FTP con el que me voy a conectar
	boolean modo; // true activo, false pasivo
	boolean conectado;

	// Atributos de socket
	Socket s; // socket de comandos
	Socket sData; // socket de datos
	ServerSocket sActive; // socket de modo activo
	BufferedReader r;
	PrintWriter p; // para enviar comandos
	PrintWriter pData; // para enviar datos

	int dataPort; // puerto destino para modo pasivo
	InetAddress ip;

	public ClienteFTP(UsuarioFTP u, boolean m) throws IOException {
		user = u;
		modo = m;
		conectado = false;
	}

	public void conectar(InetAddress ip, int port) throws IOException {

		this.ip = ip;
		s = new Socket(ip, port);
		r = new BufferedReader(new InputStreamReader(s.getInputStream()));
		p = new PrintWriter(s.getOutputStream());

		System.out.println(r.readLine());

		// Hace login

		try {
			log(user.getUser(), user.getPass());

		} catch (LoginException e) {
			System.out.println(e);
		}

		if (modo) {
			conectarActivo(ip, port);
		} else {
			this.conectarPasv(ip, port);
		}

	}

	public void desconectar() throws IOException {
		p.println("QUIT");
		p.flush();
		System.out.println(r.readLine());
		s.close();

	}

	public void subirFichero(String fichero, InetAddress ip) throws IOException {

		if (modo) { // modo activo
			p.println("STOR " + fichero);
			p.flush();
			System.out.println(r.readLine());

			this.sActive = new ServerSocket(10 * 256 + 1);
			System.out.println("Esperando conexion en el puerto " + 10 * 256 + 1);
			sData = sActive.accept();
			System.out.println("Conectado servidor");
			pData = new PrintWriter(sData.getOutputStream());

		} else { // modo pasivo

			sData = new Socket(ip, dataPort);

			pData = new PrintWriter(sData.getOutputStream());

		}

		File file = new File(fichero);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (IOException e) {
			System.out.println("No existe el archivo " + fichero);
		}

		if (reader != null) {
			if (!this.modo) { // si estamos en modo activo ya se ha enviado este
								// comando
				p.println("STOR " + fichero);
				p.flush();
				System.out.println(r.readLine());
			}

			String text = null;
			while ((text = reader.readLine()) != null) {

				pData.println(text);
				pData.flush();
			}

			reader.close();
			sData.close();

			System.out.println(r.readLine());
		}

	}

	public void log(String user, String pass) throws LoginException, IOException {

		// Envia login
		p.println("USER " + user);
		p.flush();
		System.out.println("Enviado " + "USER " + user);
		s.setSoTimeout(10000);
		System.out.println(r.readLine());

		// Envia contraseña
		p.println("PASS " + pass);
		p.flush();
		String respuesta = r.readLine();
		System.out.println(respuesta);

		if (respuesta.equals("530 Login or password incorrect!")) {
			throw new LoginException(user, pass);
		} else {
			this.user.setLogged(true);

		}
	}

	private void conectarPasv(InetAddress ip, int port) throws IOException {

		if (this.user.getLogged()) {
			System.out.println("Inicio conexion pasiva");

			p.println("TYPE I");
			p.flush();
			System.out.println(r.readLine());

			p.println("PASV");
			p.flush();
			String resp = r.readLine();
			System.out.println(resp);
			String a[] = resp.split(",");
			dataPort = Integer.parseInt(a[4]) * 256 + Integer.parseInt(a[5].replace(")", ""));

			System.out.println("Data port: " + dataPort);

		}

	}

	private void conectarActivo(InetAddress ip, int port) throws IOException {

		if (this.user.getLogged()) {
			System.out.println("Inicio conexion activa");
			p.println("TYPE I");
			p.flush();
			System.out.println(r.readLine());

			// Comando port en la forma: PORT d1,d2,d3,d4,d5,d6
			// los cuatro primeros digitos son los digitos de la dir ip cliente
			// los dos ultimos valores para obtener el puerto de datos cliente
			// puerto de datos cliente= d5*256 + d6. > que 1024

			p.println("PORT 192,168,4,2,10,1");
			p.flush();
			System.out.println(r.readLine());

		}
	}

}
