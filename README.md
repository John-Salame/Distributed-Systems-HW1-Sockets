# Distributed-Systems-HW1-Sockets

## Group
John Salame (solo)

## What I Completed
I completed around half of the API for both buyers and sellers. I have sockets for client-server buyer, clinet-server seller, and server to Customer database.  
The customer database currently only has Sellers stored on it. All of my other databases are stored on the servers in-memory, which means the buyers and sellers do not have the same view of state.  
I also ran out of time to do the timing study, as I spent the last of my time creating the sockets framework and pushing my Sellers onto the Customer database.  
My code design also does not support one socket connection per buyer or seller (it allows everybody to use the same socket), so I do not have any mechanism in place to do the timing study yet.  
Thus, I was unable to create a testing framework for the timing studies, and many of my databases, such as cart, item, and sale listing, have also not been created.  
Since I did not have time to make my item database, I also was unable to test my search function.  
Although I did not have time to complete many featuers, my codebase is around 3,000 lines of code total. In the coming days, I may be able to finish my sockets 
and business logic and perform timing studies before I move onto true RPC-style for Programming Assignment 2.
If for any reason something seems missing, check the GitHub repo I made for the project https://github.com/John-Salame/Distributed-Systems-HW1-Sockets

## Running
On 5 terminals, run these commands:
* make run_db_customer
* make run_seller_server
* make run_seller_client
* make run_buyer_server
* make run_buyer_client

## Search Function
My search used the Levenshtein distance against the keywords in each item.  
The Levenshtein distance is an edit distance for strings which includes character insertions, deletions, and substitution.  
Each keyword in the search query calculates its distance to each keyword in the item and keeps its best distance. We do this for all keywords in the search query and sum up the results.  
Then we normalize the edit distance a bit so it's more or less based on the percentage of the string that is correct, with some boosting for shorter strings.

## Bugs
* Attempting to log in while the database is unreachable will cause the client program to receive an Exception over the network, as expected.
* The first time you connect from a client to a server after the database goes down, the client immediately receives a "Connection reset by peer" from the server. The client still retains its connection with the server, which is good.
*  * The server only attempts to reconnect to the database using retries after the client sends another request. So, the first client request results in the server immediately saying "I can't reach the database", and the following requests attempt a retry.
*  * The "Connection reset" is a big issue because it could potentially prevent a "create user" or a login and make every request afterward useless.
*  * This is an issue with the "client socket" on the server that initiates a connection with the database.
* Each database has its own socket connection. On the server, Buyer, Seller, and Session each have their own connections with their own source ports despite all having the same destination port.
*  * I'm not sure if this is really a bug, but it does mean that each of the database connections will lazily reconnect to the database after it goes down.
*  * The database also cleanly closes the connection on each of these threads if the server shuts down. Strangely, I was able to have multiple of these threads at once even with the database listener set to 1 maximum connection.


## Style
I essentially implemented at most once RPCs with sockets. If there is a connection failure during a client API call, 
I retry the connection 5 times.
