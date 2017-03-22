
public class LoginException extends Exception{
	
	String pass;
	String user;
	
	public LoginException (String user, String pass) {
		this.pass= pass;
		this.user = user;
		
	}
	
	public String toString(){
		return "La contraseña "+pass+" o el usuario "+user+" es incorrecto. Adiós.";
	}
}
