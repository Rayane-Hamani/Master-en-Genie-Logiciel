package treeftp;

public class Error
{
	public static final String usage =
			"Vous devez au moins renseigner l'adresse d'un serveur ftp pour pouvoir vous y connecter. \n" +
			"Les arguments du programme sont, dans cet ordre : \n" +
			"\t1 - l'adresse du serveur ftp auquel vous souhaitez vous connecter \n" +
			"\t2 - le nombre de récursion max désiré (3 par défaut) \n" +
			"\t3 - votre nom d'utilisateur \n" +
			"\t4 - votre mot de passe";	
	
	
	
	public static final String waitingForEntry =
			"Une erreur est survenue en tentant d'établir la connexion : ";
	
	public static final String user =
			"Une erreur est survenue en envoyant le nom d'utilisateur : ";
	
	public static final String pass =
			"Une erreur est survenue en envoyant le mot de passe : ";
	
	public static final String pasv =
			"Une erreur est survenue en envoyant l'entrée en mode passif : ";
	
	public static final String list =
			"Une erreur est survenue en envoyant la demande de listage : ";
	
	public static final String cwd =
			"Une erreur est survenue en envoyant la demande de changement de dossier : ";
	
	public static final String quit =
			"Une erreur est survenue en envoyant la déconnexion : ";
}
