# Distributed-Systems-HW1-Sockets

## Group
John Salame (solo)

## GitHub Repo
If for any reason something seems missing, check the GitHub repo I made for the project and check for branches associated with the programming assignment https://github.com/John-Salame/Distributed-Systems-HW1-Sockets

## Challenges Faced
I attempted to set up the timing study after I got my sockets working well, but I ran into great difficulty once I tried to have two clients connecting at once.
All of the server threads were using the same sockets to connect to the database, so data would be corrupted by multiple threads writing to the same socket.
I eventually fixed this by creating one connection to the database per user.
Thus, I was unable to create a testing framework for the timing studies, and I am missing many API functions.  
After all that, I had a lot of trouble with building the program in Java. It was easy when there were no dependencies.
However, I am not familiar with Maven and I had to spend a lot of time figuring it out, plus the protoc compiler for GRPC. I spent hours just figuring out how to compile my project.
Then the dependencies caused my trouble since I had no classpath to them and I could not run my java files the same way I had done before.  
My solution was to create one pom.xml for each program and set the main class (the entrypoint) for each.
I have used both REST and GRPC before in CSCI 5253 Datacenter Scale Computing, but that was in Python and so I was learning everything along the way for this project.

## What I Completed
I completed around half of the API for both buyers and sellers.
The customer database is complete with buyers, sellers, and sessions. My product database only has items, without any quantity or associated sale.
No shopping cart, sale listing, purchase, or review exists yet.
### Sockets
The program supports multithreading on both the client and the server. Each client has its own thread and spawns a server thread when the server accepts the connection.
Each server thread has its own connection to the databases.
### REST
I did not have time to implement REST. For the REST client, I planned to use the built-in tools from java.net.http.
For the REST server, I planned to use Spring.
### GRPC
I have GRPC working for the Buyer database on the Customer database machine. I did not have time to implement error handling from status codes.
### SOAP
I did not have time to research or attempt SOAP.

## Running
On 6 terminals, run these commands:
* make run_db_customer
* make run_db_product
* make run_buyer_server
* make run_seller_server
* make run_buyer_client
* make run_seller_client

## Search Function
My search used the Levenshtein distance against the keywords in each item.  
The Levenshtein distance is an edit distance for strings which includes character insertions, deletions, and substitution.  
Each keyword in the search query calculates its distance to each keyword in the item and keeps its best distance. We do this for all keywords in the search query and sum up the results.  
Then we normalize the edit distance a bit so it's more or less based on the percentage of the string that is correct, with some boosting for shorter strings.

## Bugs
* Server sockets know when their peer disappears and they will cleanly close the socket and end the associated thread. The client socket will not know the peer has disappeared until it tries to send a message.
*  * This is not a bug, but it's good to highlight this behavior for later reference. From the client's view, there are no hiccups whatsoever in connecting to the server and getting a response as long as the databases are alive.
* Each database has its own socket connection. On the server, Buyer, Seller, and Session each have their own connections with their own source ports despite all having the same destination port.
*  * I'm not sure if this is really a bug, but it does mean that there are a lot of database connections, and each of the database connections will lazily connect to the database (wait until an associated API function is called before connecting to DB)
*  * The database also cleanly closes the connection on each of these threads if the server processor shuts down. Strangely, I was able to have multiple of these threads at once even with the database listener set to 1 maximum connection.
*
* (Fixed) When I run more than one client at a time, a lot of the data going over the sockets gets corrupted and the server gets caught in infinite loops that cause both the server and the client to hang.
*  * I think the reason is that all of the server threads are using the same socket connection to speak to the databases. (Confirmed)
*  * I fixed the issue using factories for on-demand creation of sockets for each thread created by the server.

* Upon receiving a request, the server creates database connection sockets for the user. However, none of these sockets are ever closed unless the server is shut down.
*  * My plan to fix this is to create a closeConnection() function on each DAO, which the business logic part of the server will call in logout() after clearing the user's session.
*
* Due to lack of synchronization code, the database has plenty of unprotected critical sections, such as user account creation. During one run, I had an instance where two buyers received buyer ID 1.
*  * I have checks preventing somebody from receiving a userID if that ID already exists for a user with a different username and password. This proves that it is an issue of synchronization.
*
* The seller program stops if the wrong user tries to change the price of an item. It should continue until a logout, but it does not.

## Socket Solution
I essentially implemented at most once RPCs with sockets. If there is a connection failure during a client API call, 
I retry the connection 5 times (once every second) if the peer is unreachable.
