# TreeFTP

Par <ins>**Rayane Hamani**</ins>, le <ins>**03 février 2021**</ins>

TreeFTP est un projet simple ayant pour but de réaliser la commande tree sur des serveurs FTP.

# Pré-requis

Le programme a été réalisé sous Java 11. Pour lancer le programme, il va donc falloir cette version de JRE. Elle est trouvable sur [le site officiel d'Oracle](https://www.oracle.com/fr/java/technologies/javase-downloads.html) avec [leur documentation d'installation](https://docs.oracle.com/en/java/javase/11/install/installation-jdk-linux-platforms.html).

Le projet a été construit à partir de Maven. Maven est installable via la ligne de commande `sudo apt install maven` sur Linux ou bien sur Windows en téléchargeant l'archive sur [le site officiel d'Apache](https://maven.apache.org/download.cgi) et en suivant [leur documentation d'installation](https://maven.apache.org/install.html).

# Utilisation

Avant de pouvoir lancer le programme, il faut le compiler avec `mvn package`.

Le programme se lance ensuite en ligne de commande avec 5 arguments dans cet ordre :

- L'adresse du serveur auquel se connecter
- Le path à partir duquel commencer le tree
- Le nombre de récursion max souhaité
- Le nom d'utilisateur
- Le mot de passe

Par défaut, le path est défini à `/`, le nombre de récursion à `1`, l'utilisateur à `anonymous` et son mot de passe à une chaîne de caractères vide. Ce qui rend que l'adresse du serveur obligatoire comme argument.

Exemple d'utilisation : `java -jar target/treeftp-1.0-SNAPSHOT.jar ftp.ubuntu.com cdimage 0`

Dans cet exemple, le programme va se connecter au serveur `ftp.ubuntu.com` et commencer le tree dans le dossier `/cdimage` et faire `0` récursion (c'est à dire qu'il n'entrera dans aucun dossier qu'il trouvera). Il se connectera en tant que `anonymous` avec mot de passe vide.

# Architecture

Le projet a été construit à partir de Maven.

L'archive du projet, après la compilation, est `target/treeftp-1.0-SNAPSHOT.jar`.

Le code se situe dans le dossier `src/main/java/treeftp` depuis la racine du projet.

Les tests se situent dans le dossier `src/test/java/treeftp` depuis la racine du projet.

Le projet contient les classes suivantes :

- **Client** -> le client qui va communiquer avec le serveur
- **ClientUtil** -> la classe contenant les fonctions nécessaires au client
- **Color** -> la classe contenant les outils d'amélioration visuelle de l'affichage du tree
- **Command** -> la classe contenant les commandes que le client va envoyer au serveur
- **Error** -> la classe contenant les messages d'erreurs à afficher lorsqu'une commande ne réussit pas
- **Main** -> la boucle principal du programme qui lance les principales fonctions du client
- **NoArgsException** -> l'exception qui sera renvoyé si l'utilisateur n'a pas renseigné d'adresse pour le serveur
- **SuccessCode** -> la classe contenant les codes de réussite des retours des commandes
- **TreeUtil** -> la classe contenant les fonctions nécessaires au tree

Il n'est pas nécessaire de créer des packages pour encapsuler les fichiers. Le seul package envisagé a été celui pour encapsuler les exceptions puisque leur gestion peut très vite déboucher sur énormément de fichiers.

Cependant, dans le cas où le sujet aurait demandé plus de fonctionnalités, la création de packages aurait été inévitable.

# Quelques exemples de code

```java
/**
 * Create the right client according to the arguments the user used
 * 
 * @param args the arguments the user used
 * 
 * @return the right client
 * 
 * @throws NoArgsException if the user used the program without arguments
 */
public static Client createClient(String[] args) throws NoArgsException
{
    Client client;
    
    switch(args.length)
    {
        case 0 :
            throw new NoArgsException(Error.usage);
        case 1 :
            client = new Client(args[0], "anonymous", "");
            break;
        case 2 :
            client = new Client(args[0], "anonymous", "");
            break;
        case 3 :
            client = new Client(args[0], "anonymous", "");
            break;
        case 4 :
            client = new Client(args[0], args[3], "");
            break;
        default :
            client = new Client(args[0], args[3], args[4]);
            break;
    }
    
    return client;
}
```

La méthode `createClient` de la classe `ClientUtil` est utilisée afin d'instancier un Client. L'avantage de son implémentation est qu'elle `ignore tous les arguments superflus` à l'ordre énoncé.

```java
/**
 * Throw an IOException if the response is not positive
 * 
 * @param response the response sent by the ftp server
 * @param code the code the response should start with
 * @param errorIntroduction the message which introduces the error
 * 
 * @throws IOException if the response doesn't start with the code
 */
public static void ifNotASuccessfulResponse(String response, String successCode, String errorIntroduction) throws IOException
{
    if(!response.startsWith(successCode))
        throw new IOException(errorIntroduction + response);
}
```

La méthode `ifNotASuccessfulResponse` de la classe `ClientUtil` est utilisée afin de lever une exception lorsqu'une réponse du serveur est strictement négative.

```java
/**
 * Send a command and return the response received
 * 
 * @param command the command sent
 * @return the response received
 * 
 * @throws IOException if an IO error occurs during the read
 */
public String send(String command) throws IOException
{
    commandWriter.println(command);
    
    String response;
    
    if(command.equals(Command.list()))
        response = commandReader.readLine() + " " + commandReader.readLine();
    else
        response = commandReader.readLine();
        
    return response;
}
```

La méthode `send` de la classe `Client` est utilisée afin d'envoyer une commande au serveur et récupérer rapidement sa réponse. L'avantage de son implémentation est qu'elle `permet de compacter le code` en évitant de devoir println/readLine à chaque fois. Elle `gère aussi les retours multiples` comme ceux de la commande list en les fusionnant en un.

```java
/**
 * Open the directory and a connection to list its files
 * 
 * @param directory the directory to open
 * 
 * @throws UnknownHostException if the address while connecting in passive mode cannot be resolved
 * @throws IOException if an IO error occurs 
 */
public void tree(String directory) throws UnknownHostException, IOException
{
    String response;
    
    response = send(Command.cwd(directory));
    
    /* We skip the directories that we fail to open and notice the user in the console */
    if(!response.startsWith(SuccessCode.cwd))
    {
        System.out.println(response);
        return;
    }
    
    Socket dataSocket = connectionInPassiveMode();
    
    response = send(Command.list());
    ClientUtil.ifNotASuccessfulResponse(response, SuccessCode.list, Error.list);

    TreeUtil.printTheList(new BufferedReader(new InputStreamReader(dataSocket.getInputStream())));
    
    dataSocket.close();
}
```

La méthode `tree` de la classe `Client` est utilisée afin de préparer l'affichage de l'arborescence. L'avantage de son implémentation est qu'elle `affiche lorsqu'un dossier n'est pas consultable et retourne à la boucle précédente`.

```java
/**
 * Read each file in the list in depth and print it with a color which depends of its type
 * 
 * @param dataReader the file reader
 * 
 * @throws UnknownHostException if an address cannot be resolved during the depth first search
 * @throws IOException if an IO error occurs during the read of the files
 */
public static void printTheList(BufferedReader dataReader) throws IOException
{
    String file;
    while((file = dataReader.readLine()) != null)
    {
        String filetype = TreeUtil.getFiletype(file);
        String filename = TreeUtil.getFilename(file, filetype);

        System.out.println(indentation(Main.getRecursion() - Main.getRecursionRemaining()) + "├─── " + Color.colorText(filetype, filename) + Color.ANSI_RESET);
        
        depthFirstSearch(filename, filetype);
    }
}
```

La méthode `printTheList` de la classe `TreeUtil` est utilisée afin d'afficher l'arborescence d'un dossier. L'avantage de son implémentation est qu'elle `affiche l'arborescence proprement, de façon similaire à la vraie commande tree` (couleur, indentation, etc...).

# Les tests

Les tests qui ont été réalisés portent surtout sur le traitement des retours que nous envoie le serveur.

Les méthodes faisant appel à des interactions avec le serveur n'ont pas été testées.

# Bonus

Les exigences du sujet étaient :

- le programme doit respecter les consignes communes de projet
- le programme doit fonctionner avec n'importe quel serveur FTP accessible via internet (par exemple ftp.ubuntu.com, ftp.free.fr, etc...)

Ce programme respecte bien ces exigences.

En bonus, ce programme possède quelques features supplémentaires :

- ce programme supporte un paramètre supplémentaire permettant de `fixer la profondeur maximale de l'arborescence à afficher`
- ce programme supporte un paramètre supplémentaire permettant de `commencer l'affichage de l'arborescence à partir d'un chemin donné` depuis la racine du serveur
- ce programme `colore les fichiers en fonction de leur type` (par exemple bleu pour les dossiers, vert pour les symlinks, etc...)

# Ce que j'aurai amélioré avec le temps

J'aurais `amélioré la structure` du projet.

J'aurais `utilisé le design pattern Singleton` pour le client.

J'aurais `suivi les symlinks` et `coloré le fichier visé` en fonction de son type.

J'aurais également `amélioré et enrichi la gestion des retours` du serveur par le client.

;)
