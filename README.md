# Distributed-Systems-HW1-Sockets

## Group
John Salame (solo)

## What I Completed
I completed around half of the API for both buyers and sellers. I have sockets for client-server buyer, clinet-server seller, and server to Customer/Product databases.
The customer database is complete with buyers, sellers, and sessions. My product database only has items, without any quantity or associated sale.
I attempted to set up the timing study after I got my sockets working well, but I ran into great difficulty once I tried to have two clients connecting at once.
All of the server threads were using the same sockets to connect to the database, so data would be corrupted by multiple threads writing to the same socket. I am unsure whether mutex around the reads and writes would fix this.  
Thus, I was unable to create a testing framework for the timing studies, and many of my databases, such as cart, purchase, review, and sale listing, have also not been created.  
If for any reason something seems missing, check the GitHub repo I made for the project and check for branches associated with the programming assignment https://github.com/John-Salame/Distributed-Systems-HW1-Sockets

## Running
On 5 terminals, run these commands:
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
*  * EDIT: Now, it doesn't even act cleanly anymore. I'm not sure why that's the case. It used to act cleanly. Now it sometimes hangs and I'm not completely sure why. I think it has to do with corrupt data.
* Each database has its own socket connection. On the server, Buyer, Seller, and Session each have their own connections with their own source ports despite all having the same destination port.
*  * I'm not sure if this is really a bug, but it does mean that each of the database connections will lazily reconnect to the database after it goes down.
*  * The database also cleanly closes the connection on each of these threads if the server shuts down. Strangely, I was able to have multiple of these threads at once even with the database listener set to 1 maximum connection.
*
* When I run more than one client at a time, a lot of the data going over the sockets gets corrupted and the server gets caught in infinite loops that cause both the server and the client to hang.
*  * I think the reason is that all of the server threads are using the same socket connection to speak to the databases.
*  * I tried to fix the issue using factories for on-demand creation of sockets. It didn't fix the issue.
*  * I think I may need a factory on the listener as well which would ensure that the server socket threads are not using a shared buyer/seller interface, but then I'm not sure how I would close all the socket connections to the DB that it would create.

## Style
I essentially implemented at most once RPCs with sockets. If there is a connection failure during a client API call, 
I retry the connection 5 times.
