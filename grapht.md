user owns r_server info
user wants give some of r_server's info to c_server
a_server responsible for provind secure means to accomplish that



RS creates RS entity on AS server
RS creates RS-Client on AS server

# Setup

## Client Registration
Client acquires registration access token to have the ability to create Registered Client on the authorization server
    
    Client > a_server.getClientRegistrationAccessToken() 
        | client_reg_access_token 

Client registers a client on the authorization server, when users issues an according request or the server does it automatically after the user was registered. 

    Client > a_server.registerClient(client_reg_access_token) 
        | client_id, client_secret

## RS Entity Registration - Skip
    
## RS-Client Registration


# User Links its AS account to RS




user > r_server.createAccount()
user | r_server creds
user > r_server.createAccountOnAuthServer() | a_server credentials
       r_server > a_server.createAccount()  | a_server credentials 

user | {r_server, a_server} creds
r_server | a_server creds

user > c_server.getResourceFromResourceServer()   | r_server resource
       c_server > r_server.getProtectedResource() | r_server resource 
                < declined ! access token is required 
                > a_server.getAccessToken()               | access token
                    user > a_server.login(a_server creds) | access token

user | {r_serv, a_server} creds, r_serve info



-> c_server -> r_server.getProtected() -> a_server.getBearer()
    -> -> a_server -> user.login() -> user.allowScopes() | return bearer
    -> c_server -> r_server.getProtected(bearer) | return resourse

user.login() // user logins into a_server that is responsible
             // for providing authorization tokens to r_server

r_server -> a_server.createUserAccoutn() | user account credentials






