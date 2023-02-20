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
* Attempting to log in while the database is unreachable will cause the server to crash without closing the socket and the client to hang because it tries to operate on a null string (the session token created by the sessionDao).
* * Perhaps I should handle this more gracefully. I could maybe return an empty string and then validate on the client side that an empty string means an error.
* * I could also just close the server socket upon triggering a NullPointerException, but this would also cause the client process to end.

## Style
I essentially implemented at most once RPCs with sockets. If there is a connection failure during a client API call, 
I retry the connection 5 times.
