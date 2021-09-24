# FlopBox

Par <ins>**Rayane Hamani**</ins>, le <ins>**01 avril 2021**</ins>

# Pré-requis

Le programme a été réalisé sous Java 11. Pour lancer le programme, il va donc falloir cette version de JRE. Elle est trouvable sur [le site officiel d'Oracle](https://www.oracle.com/fr/java/technologies/javase-downloads.html) avec [leur documentation d'installation](https://docs.oracle.com/en/java/javase/11/install/installation-jdk-linux-platforms.html).

Le projet a été construit à partir de Maven. Maven est installable via la ligne de commande `sudo apt install maven` sur Linux ou bien sur Windows en téléchargeant l'archive sur [le site officiel d'Apache](https://maven.apache.org/download.cgi) et en suivant [leur documentation d'installation](https://maven.apache.org/install.html).

# Utilisation

Avant de pouvoir lancer le programme, il faut le `compiler` avec :

```sh
mvn package
```

Il faut également `lancer un serveur FTP en local` afin de pouvoir réaliser certaines opérations nécessitant des droits d'écriture. Un a été fourni à la racine du projet et peut être lancer avec :

```sh
python3 server.py
```

Le programme `se lance ensuite en ligne de commande`, sans argument, avec :

```sh
mvn exec:java
```

Des scripts ont été réalisés afin de `montrer le bon fonctionnement` des principales fonctionnalités. Ils se trouvent dans le dossier script à la racine du projet. N'hésitez pas à aller inspecter leur contenu afin d'avoir de plus amples informations sur la construction et l'enchaînement des commandes curls à utiliser pour faire fonctionner le programme.

Pour arrêter la plateforme proprement, vous pouvez simplement appuyer sur la touche `Entrée` de votre clavier. Sinon, vous pouvez faire un `Ctrl+C` sur le terminal de lancement ou bien envoyer un signal `SIGKILL` au processus.

# Commandes curl

- Enregistrer des serveurs FTP (doit être la toute première commande lancée).

```sh
curl --request PUT 'http://localhost:8080/flopbox/{nom du serveur}' --header 'host:{nom de domaine}'
```

- Afficher les serveurs enregistrés

```sh
curl --request GET 'http://localhost:8080/flopbox/'
```

- Supprimer des serveurs enregistrés

```sh
curl --request DELETE 'http://localhost:8080/flopbox/{nom du serveur}'
```

- Lister un fichier ou un dossier distant

```sh
curl --request GET 'http://localhost:8080/flopbox/{nom du serveur}/{chemin du fichier ou dossier}' --header 'port:{numéro de port dentrée du serveur}' --header 'username:{nom de compte}' --header 'password:{mot de passe}'
```

- Créer un dossier

```sh
curl --request PUT 'http://localhost:8080/flopbox/{nom du serveur}/{chemin du futur dossier}' --header 'port:{numéro de port dentrée du serveur}' --header 'username:{nom de compte}' --header 'password:{mot de passe}'
```

- Renommer un fichier ou un dossier

```sh
curl --request PATCH 'http://localhost:8080/flopbox/{nom du serveur}/{chemin du fichier ou dossier}' --header 'port:{numéro de port dentrée du serveur}' --header 'to:{nouveau nom du fichier ou dossier}' --header 'username:{nom de compte}' --header 'password:{mot de passe}'
```

- Supprimer un fichier ou un dossier

```sh
curl --request DELETE 'http://localhost:8080/flopbox/{nom du serveur}/{chemin du fichier ou dossier}' --header 'port:{numéro de port dentrée du serveur}' --header 'username:{nom de compte}' --header 'password:{mot de passe}'
```

- Mettre en ligne un fichier

```sh
curl --request POST 'http://localhost:8080/flopbox/{nom du serveur}/{nom du fichier mis en ligne}/upload' --header 'port:{numéro de port dentrée du serveur}' --header 'from:{chemin du fichier à mettre en ligne}' --header 'username:{nom de compte}' --header 'password:{mot de passe}'
```

- Télécharger un fichier

```sh
curl --request GET 'http://localhost:8080/flopbox/{nom du serveur}/{chemin du fichier à télécharger}/download' --header 'port:{numéro de port dentrée du serveur}' --header 'username:{nom de compte}' --header 'password:{mot de passe}'
```

# Architecture

Le projet a été construit à partir de Maven en utilisant la commande suivante :

```sh
mvn archetype:generate -DarchetypeGroupId=org.glassfish.jersey.archetypes -DarchetypeArtifactId=jersey-quickstart-grizzly2 -DarchetypeVersion=2.33
```

L'archive du projet, après la compilation, est `target/flopbox-1.0-SNAPSHOT.jar`.

Le code se situe dans le dossier `src/main/java/flopbox` depuis la racine du projet.

Les tests se situent dans le dossier `src/test/java/flopbox` depuis la racine du projet.

Le projet contient les packages suivants :

- **client** -> Le package relatif à l'initialisation du client et sa terminaison
- **command** -> Le package relatif aux commandes utilisables par la plateforme

# Quelques exemples de code

```java
/**
 * Register a server and its host in the platform.
 * 
 * @param server The server.
 * @param host The host of the server.
 * 
 * @return How the command went.
 */
public static String register(String server, String host)
{
    if(servers.containsKey(server))
        return "The server \"" + server + "\" is already registered.";
    
    servers.put(server, host);
    
    return "The association \"" + server + " -> " + host + "\" has been registered.";
}
```

La gestion des serveurs de la plateforme est très précise. `Le nom des fonctions dédiées à cet objectif représente bien ce qu'elles font`. Ici, l'enregistrement des serveurs sur la plateforme `n'empiète pas sur les autres serveurs` déjà enregistrés. Elle `ne gagne donc pas en ambiguïté avec la fonction modify` et `respecte bien le principe de responsabilité unique` (SRP).

```java
/**
 * Connect and log in the FTP server.
 * 
 * @param client The FTP client.
 * @param server The FTP server to connect to.
 * @param port The port of the FTP server to connect to.
 * @param username The username of the account to use.
 * @param password The password of the account to use.
 * 
 * @return True if the client could connect and log in the FTP server, otherwise false.
 */
public static boolean connectAndLogin(FTPClient client, String server, int port, String username, String password)
{
    try
    {
        client.connect(ServerCommands.getServers().get(server), port);
        
        if(FTPReply.isPositiveCompletion(client.getReplyCode()))
            if(client.login(username, password))
                return client.setFileType(FTPClient.BINARY_FILE_TYPE);
        
        return false;
    }
    
    catch(IOException e)
    {
        return false;
    }
}
```

`La connexion et identification est faite en premier` à chaque récupération de commande. `Le type binaire est également préféré` à celui de base, ascii, par soucis de compatibilité (de plus en plus de serveur le privilégie à l'instar d'ascii).

```java
/**
 * List information about a file or files in a directory on the FTP server.
 * 
 * @param server The FTP server to connect to.
 * @param port The port of the FTP server to connect to.
 * @param username The username of the account to use.
 * @param password The password of the account to use.
 * @param path The path of the file or directory.
 * 
 * @return Information about the file or files in the directory.
 */
@GET
@Path("{server}/{path:.*}")
@Produces(MediaType.TEXT_PLAIN)
public String list(@PathParam  ("server"  ) String server  ,   @HeaderParam("port"    ) int    port    ,
                   @HeaderParam("username") String username,   @HeaderParam("password") String password,
                   @PathParam  ("path"    ) String path    )
{
    FTPClient client = new FTPClient();
    
    String response;
    
    if(ClientUtil.connectAndLogin(client, server, port, username, password))
    {
        response = FtpCommands.list(client, path);
        ClientUtil.logoutAndDisconnect(client);
    }
    else
        response = "The connection to the server failed.";
    
    
    return response;
}
```

La gestion des commandes se fait de cette façon. `Toutes les commandes sont récupérées` avec une méthode de requête HTTP et un chemin et `leur traitement se fait dans une autre classe`. Cette implémentation a l'avantage d'être `plus claire, plus maintenable et plus facilement débuggable`.

```java
/**
 * List information about a file or files in a directory on the FTP server.
 * 
 * @param client The FTP client.
 * @param path The path of the file or directory.
 * 
 * @return Information about the file or files in the directory.
 */
public static String list(FTPClient client, String path)
{
    String response = "";
    
    try
    {
        client.enterLocalPassiveMode();
        
        FTPFile[] files = client.listFiles(path);
        
        for(FTPFile file : files)
        {
            response += file.getRawListing();
            response += "\n";
        }
        
        return response;
    }
    
    catch(IOException e)
    {
        response = "An IO error occured while sending the command or receiving its result.";
    }
    
    return response;
}
```

Le traitement des commandes est effectué dans des `classes regroupant les commandes de par leur objectif` (gestion de la plateforme, FTP, etc...). Ces classes sont dans le package `command` et sont donc séparées de Main et MyResource par soucis de structuration. Elles doivent donc récupérer le client initialisé lors de la récupération des commandes.

# Les tests

Aucun test n'a été réalisé cette fois-ci.

Les efforts ont été concentrés à rendre la plateforme fonctionnel.

# Ce que j'aurais amélioré avec le temps

J'aurais `terminé la généralisation fichier-dossier des commandes` afin de toutes les rendre universelles.

J'aurais `expérimenté le postionnement des fonctions dans le répertoire courant du fichier ou dossier visé` afin de voir les avantages que cela aurait pu apporter (maintenabilité, clarté, etc...).

J'aurais `rendu les retours encore plus précis` si une fonction n'avait pas rendu le résultat escompté.
