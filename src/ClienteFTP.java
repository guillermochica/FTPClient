import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class ClienteFTP {
	
	private UsuarioFTP user; //Usuario FTP con el que me voy a conectar
	
	//Atributos de socket
	Socket s;
	Socket sData;
	BufferedReader r;
	PrintWriter p;
	PrintWriter pData;
	
	int dataPort;
	InetAddress ip;
	
	
	public ClienteFTP(UsuarioFTP u) throws IOException { 
		user = u;
		
	}
	
	
	public void desconectar() throws IOException {
		p.println("QUIT");
		p.flush();
		System.out.println(r.readLine());
	}
	
	
	public void subirFichero(String fichero, InetAddress ip) throws IOException {
		
		
		File file = new File(fichero);
		BufferedReader reader = null;
		try{
		 reader = new BufferedReader(new FileReader(file));
		}catch(IOException e){
			System.out.println("No existe el archivo "+fichero);
		}
		
		if(reader!=null){
			p.println("STOR " + fichero);
			p.flush();

			sData = new Socket(ip, dataPort);
			
			pData = new PrintWriter(sData.getOutputStream());
			System.out.println(r.readLine());

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
		

		if (r.readLine().equals("530 Login or password incorrect!")) {
			throw new LoginException(user, pass);
		} else {
			this.user.setLogged(true);
		}
	}
	
	public void conectar(InetAddress ip, int port) throws IOException {
		this.ip = ip;
		s = new Socket(ip, port);
		r = new BufferedReader(new InputStreamReader(s.getInputStream()));
		p = new PrintWriter(s.getOutputStream());
		
		System.out.println(r.readLine());
		
		//Hace login
		try{
			log(user.getUser(), user.getPass());
			
		}catch(LoginException e) {
			System.out.println(e);
		}
		
		
		if(this.user.getLogged()) {
			
			
			p.println("TYPE I");
			p.flush();
			System.out.println(r.readLine());
			
			p.println("PASV");
			p.flush();
			String resp = r.readLine();
			System.out.println(resp);
			String a[] = resp.split(",");
			dataPort = Integer.parseInt(a[4])*256 + Integer.parseInt(a[5].replace(")", ""));

			System.out.println("Data port: "+dataPort);
			
		}

	}
	
}
