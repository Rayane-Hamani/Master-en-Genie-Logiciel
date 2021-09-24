# Agent FlopBox

Par <ins>**Rayane Hamani**</ins> et <ins>**Florian Dendoncker**</ins>, le <ins>**29 avril 2021**</ins>

# Pré-requis

Le programme a été réalisé sous Java 11. Pour lancer le programme, il va donc falloir cette version de JRE. Elle est trouvable sur [le site officiel d'Oracle](https://www.oracle.com/fr/java/technologies/javase-downloads.html) avec [leur documentation d'installation](https://docs.oracle.com/en/java/javase/11/install/installation-jdk-linux-platforms.html).

Le projet a été construit à partir de Maven. Maven est installable via la ligne de commande `sudo apt install maven` sur Linux ou bien sur Windows en téléchargeant l'archive sur [le site officiel d'Apache](https://maven.apache.org/download.cgi) et en suivant [leur documentation d'installation](https://maven.apache.org/install.html).

# Utilisation

Avant de pouvoir lancer le programme, il faut le `compiler` avec :

```sh
mvn package assembly:single
```

Il faut également `lancer un serveur FTP en local` afin de pouvoir réaliser certaines opérations nécessitant des droits d'écriture. Un a été fourni à la racine du projet Flopbox et peut être lancer depuis la racine de ce projet avec :

```sh
python3 server.py
```

Le programme `nécessite ensuite que la plateforme Flopbox soit lancée` et celle-ci peut-être exécutée, sans argument, toujours depuis la racine du projet Flopbox avec :

```sh
mvn exec:java
```

Le programme `nécessite aussi que certains serveurs aient déjà été enregistré` sur la plateforme Flopbox. Pour en ajouter, fiez-vous aux scripts et au README du projet Flopbox. La présentation de comment faire a volontairement été rendu accessible de part ces moyens.

Une fois tout cela effectué, il devient possible de lancer le programme avec :

```sh
java -jar target/simple-service-1.0-SNAPSHOT-jar-with-dependencies.jar 
```

Le programme tourne ensuite indéfiniment en vérifiant si des changements en local ont été effectués et si tel est le cas, il les envoie au serveur s'il en a les droits.

# Architecture

Le projet a été construit à partir de Maven en utilisant la commande suivante :

```sh
mvn archetype:generate
```

L'archive du projet, après la compilation, est `java -jar target/simple-service-1.0-SNAPSHOT-jar-with-dependencies.jar`.

Le code se situe dans le dossier `src/main/java/agentflopbox` depuis la racine du projet.

Les tests se situent dans le dossier `src/test/java/agentflopbox` depuis la racine du projet.

Le projet ne contient aucun package cette fois.

# Quelques exemples de code

```java
/**
 * Load the servers registered on Flopbox in the agent.
 * 
 * @param servers The list to fill with all the servers registered on Flopbox.
 * 
 * @return If the list has correctly been filled.
 */
public static void loadServers(ArrayList<Server> servers)
{
    GetMethod request = new GetMethod(Main.FLOPBOX);
    
    send(request);
    
    Scanner scanner = new Scanner(receive(request));
    
    while(scanner.hasNextLine())
    {
        String[] line = scanner.nextLine().split(" -> ");

        String name = line[0].toLowerCase();
        
        servers.add(new Server(name));
    }
    
    scanner.close();
}
```

Cette méthode permet à l'agent de `récupérer les serveurs enregistrés` sur la plateforme Flopbox.

```java
/**
 * Load the port and user to use for the servers specified in the config file.
 * 
 * @param servers The list of servers registered on Flopbox.
 */
public static void loadPortsAndUsers(ArrayList<Server> servers)
{
    File config = new File("config.ini");
    
    try
    {
        Scanner scanner = new Scanner(config);
        
        while(scanner.hasNextLine())
        {
            String[] line = scanner.nextLine().split(" ");
            
            String name     = line[0].toLowerCase();
            String port     = line[1];
            String username = line[2];
            String password = line[3];
            
            Server server = findServerByName(name);
            
            if(server != null)
            {
                server.setPort(port);
                server.setUser(new User(username, password));
            }
        }
        
        scanner.close();
    }
    
    catch(FileNotFoundException e)
    {
        // do nothing
    }
}
```

Cette méthode permet à l'agent de `parser le fichier de configuration` et de `renseigner le port ainsi que le compte à utiliser` lorsqu'il communiquera avec les serveurs via la plateforme.

```java
	public Server(String name, String port, User user)
	{
		this.name = name;
		this.port = port;
		this.user = user;
	}
	
	public Server(String name, String port)
	{
		this.name = name;
		this.port = port;
		this.user = new User("anonymous", "anonymous");
	}
	
	public Server(String name)
	{
		this.name = name;
		this.port = "21";
		this.user = new User("anonymous", "anonymous");
	}
```

Ces méthodes sont les `constructeurs des objets serveur`. Par défaut à leur construction, `tous possède le port 21 et le compte anonymous`. C'est `après que le fichier de configuration ait été lu` que `ces informations sont modifiées` et rendues correctes/utilisables.

```java
/**
 * Set the headers of the request.
 * 
 * @param request The request whose headers will be set.
 * @param name The name of the server.
 * @param from The value of the "from" header.
 * @param to The value of the "to" header.
 * 
 * @return If the headers have been set.
 */	
public static boolean setHeaders(HttpMethodBase request, String name, String from, String to)
{
    Server server = findServerByName(name);
    
    if(server == null)
        return false;
    
    request.setRequestHeader("port", server.getPort());
    request.setRequestHeader("username", server.getUser().getUsername());
    request.setRequestHeader("password", server.getUser().getPassword());
    
    if(from != null)
        request.setRequestHeader("from", from);
    
    if(to != null)
        request.setRequestHeader("to", to);
    
    return true;
}
```

Cette méthode `renseigne les headers des commandes`.

```java
/**
 * List the files of a server.
 * 
 * @param server The sever.
 * 
 * @return How the command went.
 */
public static String list(Server server)
{
    GetMethod request = new GetMethod(Main.FLOPBOX + server.getName() + "/");
    
    AgentUtil.setHeaders(request, server.getName(), null, null);
    
    AgentUtil.send(request);
    
    return AgentUtil.receive(request);
}
```

Cette méthode `construit, paramètre et envoie une commande list` à un serveur via la plateforme. Les `autres commandes sont construits très similairement` à celle-ci.

# Les tests

Aucun test n'a été réalisé cette fois-ci.

Les efforts ont été concentrés à rendre la plateforme fonctionnel.

# Ce que j'aurais amélioré avec le temps

J'aurais `rendu l'agent fonctionnel`.