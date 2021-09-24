# FTP Server

Par <ins>**Rayane Hamani**</ins>, le <ins>**06 mars 2021**</ins>  
Par <ins>**Florian Dendoncker**</ins>, le <ins>**06 mars 2021**</ins>

# Pré-requis

Le programme a été réalisé sous Java 11. Pour lancer le programme, il va donc falloir cette version de JRE. Elle est trouvable sur [le site officiel d'Oracle](https://www.oracle.com/fr/java/technologies/javase-downloads.html) avec [leur documentation d'installation](https://docs.oracle.com/en/java/javase/11/install/installation-jdk-linux-platforms.html).

Le projet a été construit à partir de Maven. Maven est installable via la ligne de commande `sudo apt install maven` sur Linux ou bien sur Windows en téléchargeant l'archive sur [le site officiel d'Apache](https://maven.apache.org/download.cgi) et en suivant [leur documentation d'installation](https://maven.apache.org/install.html).

# Utilisation

Avant de pouvoir lancer le programme, il faut le compiler avec `mvn package`.

Le programme se lance ensuite en ligne de commande sans argument.

Par défaut, le path du programme est défini à `/home/user/` avec `user` votre compte Linux. Le programme ne devrait pas être capable d'aller en dessous de cette racine imposée.

Exemple d'utilisation : `sudo java -jar target/ftpserver-1.0-SNAPSHOT.jar 1024`

Le `sudo` est obligatoire afin de pouvoir écouter sur le port 21. Si vous souhaitez utiliser un autre port, vous pouvez le renseigner en premier argument du programme. Par défaut, le programme se lancera sur le port 21 si vous n'en renseignez pas. Nous n'avons pas bloqué l'utilisation des ports réservés (0 à 1023 inclu) mais utilisez de préférence un port >= 1024.

Afin d'arrêter le serveur, vous pouvez soit écrire `stop` dans le terminal de lancement ou bien faire un `Ctrl+C` dessus ou bien encore envoyez un signal `SIGKILL` au processus.

# Architecture

Le projet a été construit à partir de Maven.

L'archive du projet, après la compilation, est `target/ftpserver-1.0-SNAPSHOT.jar`.

Le code se situe dans le dossier `src/main/java/ftpserver` depuis la racine du projet.

Les tests se situent dans le dossier `src/test/java/ftpserver` depuis la racine du projet.

Le projet contient les packages suivants :

- **account** -> Le package relatif aux comptes pouvant se utiliser le serveur
- **command** -> Le package relatif aux commandes utilisables
- **exception** -> Le package relatif aux exceptions pouvant être levées
- **server** -> Le package relatif au serveur et clients pouvant se connecter

# Quelques exemples de code

```java
/**
 * Check if the username if registered
 * 
 * @param username the username to try
 * 
 * @return the status of how the command went
 */
public static String execute(String username)
{
    if(AccountUtil.isRegistered(username))
        return "331 Please specify the password.";
    else
        return "530 User not registered. This server can be accessed by anonymous";
}
```
Le serveur `effectue une vérification` lorsque l'utilisateur entre son nom de compte. S'il n'a pas de compte, il lui indique qu'il peut tout de même accéder au serveur via le compte anonymous.

```java
private static Server server;
public static Server getServer() { return server; }



public static void main(String[] args)
{
    /* on remplit la "base de données" de compte pour la démo */
    
    AccountUtil.setAccounts("rayane", new Account("rayane", "hamani"));
    AccountUtil.setAccounts("florian", new Account("florian", "dendoncker"));
    AccountUtil.setAccounts("anonymous", new Account("anonymous", ""));
    
    /* on démarre le serveur */
    
    try
    {
        if(args.length>0)
            server = new Server(Integer.parseInt(args[0]));
        else
            server = new Server(21);
        
        new Thread(server).start();
    }
    
    catch(IOException e) { System.out.println("Failed to open the server."); }
    
    /* on attend l'instruction de l'utilisateur fermer le serveur */
    
    Scanner scanner = new Scanner(System.in);
    while(true)
    {
        if(scanner.next().toLowerCase().equals("stop"))
        {
            scanner.close();
            System.exit(0);
        }
    }
}
```
Le serveur `peut être stoppé` une fois lancé en rentrant simplement le mot `stop` dans le terminal de lancement.

```java
/**
 * Accept connections (multi-threaded)
 */
public void run()
{
    while(true)
    {
        try
        {
            new Thread(new Connection(serverSocket.accept())).start();
            System.out.println("Connexion établie !");
        }
        
        catch(IOException e)
        {
            // nothing, we can to continue accepting connections
        }
    }
}
```
Le serveur accepte les connexions de cette façon. Les connexions `implements Runnable`.

```java
/**
 * Run the connection
 * Ask to login first before letting the user execute other commands
 * 
 */
public void run()
{
    String response;

    response = "220 Service ready for a new user.";
    send(response);

    /* On force la connexion */

    while (isRunning && !isLogged) {
        response = login();
        send(response);
    }

    /* On exécute les commandes de l'utilisateur */

    while (isRunning && isLogged) {
        response = execute();
        send(response);
    }
    
    /* on ferme les ressources liées à cette connexion */
    
    Quit.execute(this);
}
```
Les connexions effectuent cette boucle. Elles sont `obligées de s'authentifier` avant de pouvoir utiliser toutes les commandes.

```java
/**
 * Receive and execute the only acceptable commands when a user is not logged
 * yet. The only acceptable commands are AUTH, USER, PASS and QUIT. They should
 * be enough for the user to log in
 * 
 * @return the status of how the command went
 */
public String login()
{
    String response;

    try
    {
        String[] command = reader.readLine().split(" ");

        switch(command[0].toUpperCase())
        {
        case "AUTH":
            response = Auth.execute();
            break;
        case "USER":
            response = User.execute(command[1]);
            if (User.isSuccessful(response))
                this.username = command[1];
            break;
        case "PASS":
            response = Pass.execute(this.username, command[1]);
            if(Pass.isSuccessful(response))
                this.isLogged = true;
            else
                this.isRunning = false;
            break;
        case "QUIT":
            response = Quit.execute(this.isRunning);
            break;
        default:
            response = "530 Please login with USER and PASS.";
            break;
        }
    }

    catch(IOException e)
    {
        response = "500 The read of the command failed. Try again please.";
    }
    catch(NullPointerException e)
    {
        response = Quit.execute(this.isRunning);
    }

    return response;
}
```
Les connexions `ne peuvent utiliser que ces commandes tant qu'elles ne sont pas authentifiées`. A savoir qu'elles ``peuvent accéder au serveur en tant qu'anonymous`` en renseignant un mot de passe non-pertient ici. Si une mot de passe est renseigné et qu'il n'est pas bon, on rejette directement la connexion.

# Les tests

Les tests qui ont été réalisés portent surtout sur le traitement des commandes faites par le serveur.

Les méthodes faisant appel à des interactions avec les clients n'ont pas été testées (à cause notamment de l'erreur `connection denied` lors de l'ouverture d'un socket/serversocket lors des tests).

# Ce que j'aurai amélioré avec le temps

J'aurais `mieux designé les modes passif et actif` du serveur.

J'aurais `implémenté plus en profondeur les différents types` (ascii, binary, etc...).

J'aurais aussi `ajouté un système d'autorisation` de sorte à ce que `certains comptes puissent faire plus` que d'autres (en l'occurance j'avais un système d'admins à l'esprit). Bon j'aurai vraiment fait ça en very toute fin mais j'ai pensé mon système de compte de la sorte (j'aurai pu rajouter une liste de commande utilisable par un et pas par un autre).

;)