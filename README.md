# Code Fellowship application 1.0

## Running the Application

- To start this application run `./gradlew bootRun` in the terminal
- Listening at http://localhost:8080

## Routes 

-  `/ ` This is the home page , where the user can read the application info, and select to login or sign up to the page.
-  `/signup` : The user can sign up, by inputting the application's basic information
- `/login` : An existing user can log in.  

## Code Fellowship application 2.0

- Can redirect user to its profile page, and add a post.
- User can view or user's posts and profile. 

## Routes

-  `/profile-user/{id} ` This is how to look for a user
-  `/my-profile` : The user is redirected to its profile after login
- `/error` : Custom error page  

## Code Fellowship application 3.0

- Has a News Feed page
- Has a Users page, where a user can view every profile in a page
- A user can follow another user
- A profile will view the followers and following

##  New Routes

- `/users` View all users
- `feed` View all posts