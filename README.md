# wHeReGotTimeFind_backend

Code for the backend server of our application, *wHeReGotTimeFind*.

- Written in Spring Boot
- Database in PostgreSQL
- Deployed on Heroku at https://safe-coast-45446.herokuapp.com

## Endpoints Used in the App






#### Get full reviews by product name (fuzzy)​

`GET /reviews/productName/<partial product name>`
###### Path Variable:
(Partial) product name that can have some typo, such as "comptuer" for "computer".
###### Returns:
Array of full reviews whose product name matches the path variable.







#### Get full reviews by vendor name (fuzzy)​

`GET /reviews/vendorName/<partial vendor name>`
###### Path Variable:
(Partial) vendor name that can have some typo, such as "Amazin" for "Amazon".​
###### Returns:
Array of full reviews whose vendor name matches the path variable.







#### Get full reviews by list of usernames (fuzzy)​

`GET /reviews/username/?usernames​`
###### Parameters:
|Name|Required?|Description|
|-|-|-|
|usernames|Yes|A list of usernames whose reviews are desired. Names can contain typos, e.g. "Pteer" for "Peter".|
###### Returns:
Array of full reviews whose username matches any name in the parameter.







### Get vendors by vendor name (fuzzy)​

`GET /vendors/vendorName/<partial vendor name>`

###### Path Variable:
(Partial) vendor name that can have some typo, such as "Amazin" for "Amazon".​
###### Returns:
List of vendors that match the path variable.








### Get products by product name (fuzzy)​

`GET /products/productName/<partial product name>`​
###### Path Variable:
(Partial) vendor name that can have some typo, such as "comptuer" for "computer".​
###### Returns:
List of products that match the path variable.






### Post a review​

`POST /reviews?(many options)​`








### Delete a review by ID

`DELETE /reviews/<review id>?debug​`

###### Path Variable:
ID of review to be deleted.
###### Parameters:
|Name|Required?|Description|
|-|-|-|
|debug|No|Boolean value; if left out or set to false, then the server will refuse to delete the review if the username of the requester does not match that of the review writer. If set to true, removes this protection. This flag is meant for us to debug conveniently with something like Postman.
###### Returns:
The string "deleted successfully" if deleted successfully. This is because it is meant only for 






### Login​

`POST /login​`








### Sign Up​

`POST /signup?newUsername&newHash​`



## Running Locally

Make sure you have Java and Maven installed.  Also, install the [Heroku CLI](https://cli.heroku.com/).

```sh
$ git clone https://github.com/YongZhengYew/infosys1D
$ cd infosys1D
$ mvn install
$ heroku local:start
```

Your app should now be running on [localhost:5000](http://localhost:5000/).

If you're going to use a database, ensure you have a local `.env` file that reads something like this:

```
JDBC_DATABASE_URL=jdbc:postgresql://localhost:5432/java_database_name
```

## Deploying to Heroku

```sh
$ heroku create
$ git push heroku main
$ heroku open
```

## Documentation

For more information about using Java on Heroku, see these Dev Center articles:

- [Java on Heroku](https://devcenter.heroku.com/categories/java)
