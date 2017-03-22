
public class UsuarioFTP {
	
	private String user; //guillermo
	private String pass; //ftp
	private boolean logged; //true si se ha logueado con exito
	
	public UsuarioFTP(String u, String p) {
		user = u;
		pass = p;
		logged = false;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public void setLogged(boolean var) {
		logged = var;
	}
	
	public boolean getLogged() {
		return logged;
	}

}
