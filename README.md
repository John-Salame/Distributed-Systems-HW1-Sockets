# Distributed-Systems-HW1-Sockets

## Group
John Salame

## Considerations 
1. Do we want to complilcate things and marshall things efficiently? Maybe even cutting things off at bit level such as the item category?
Or do we instead want to do something bigger but more human-readable like JSON?
2. How do we handle exceptions from the server? What kind of message would the Socket return in order to indicate that an exception occurred?
3. Do we disallow long strings or simply truncate them?
4. How do we determine borders when we have short strings for example?
Do we leave the rest of the characters in the limit empty and do uniform sized blocks of data?
