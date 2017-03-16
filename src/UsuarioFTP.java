
public class UsuarioFTP {
	
	private String user; //guillermo
	private String pass; //ftp
	
	public UsuarioFTP(String u, String p) {
		user = u;
		pass = p;
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

}
