# wHeReGotTimeFind_backend

Code for the backend server of our application, *wHeReGotTimeFind*.

- Written in Spring Boot
- Database in PostgreSQL
- Deployed on Heroku at https://safe-coast-45446.herokuapp.com

## Team 3D Members
 - [1005118 Ian Goh](https://github.com/iangohy)
 - [1005036 Tham Jit](https://github.com/asdfash)
 - [1005175 Jonah Yeo](https://github.com/J-onah)
 - [1005310 Tan Li Hui](https://github.com/t-lihui)
 - [1005155 Yong Zheng Yew](https://github.com/YongZhengYew)
 - [1005262 Kevin Teng](https://github.com/lemons4lyf)
 - [1004883 Yang Haocheng](https://github.com/yhc-666)

## Endpoints Used in the App

### Login​

`POST /login​`
###### Parameters:
|Name|Required?|Description|
|-|-|-|
|username|Yes|Exact username of requester.|
|passwordHash|Yes|Hash of user's actual password (hashed by app)|
###### Returns:
Login session information, most importantly a `temp_auth_token` that the user needs to send with every subsequent request.






### Sign Up​

`POST /signup​`
###### Parameters:
|Name|Required?|Description|
|-|-|-|
|newUsername|Yes|Exact new username of requester.|
|newPasswordHash|Yes|Hash of user's new password (hashed by app)|
###### Returns:
Login session information, most importantly a `temp_auth_token` that the user needs to send with every subsequent request.

> ==Important Note==: From here onwards, *every* request **must** include both `username` and `authToken` (which is just `temp_auth_token`) as parameters in order to validate it as being part of an active session. This will not be stated explicitly in subsequent endpoint documentation for purposes of brevity, except for situations where it plays a role apart from just authentication.

### Get full reviews by product name (fuzzy)​

`GET /reviews/productName/<partial product name>`
###### Path Variable:
(Partial) product name that can have some typo, such as "comptuer" for "computer".
###### Returns:
Array of full reviews whose product name matches the path variable.







### Get full reviews by vendor name (fuzzy)​

`GET /reviews/vendorName/<partial vendor name>`
###### Path Variable:
(Partial) vendor name that can have some typo, such as "Amazin" for "Amazon".​
###### Returns:
Array of full reviews whose vendor name matches the path variable.







### Get full reviews by list of usernames (fuzzy)​

`GET /reviews/username​`
###### Parameters:
> Note the 's' in usernames below! It's not the standard username parameter (which must also be sent, as with every normal request).

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
> Parameters to do with products and vendors for this request seem relatively complicated, but all it really boils down to is that:
> - Either a new product is added to the database along with this review, or an existing product is referenced; so only one of either `newProductName` *or* `existingProductId` is required.
> - Similarly, either a new vendor (with name, location and phone number) is added with the review, or an existing vendor is referenced by ID. This means that either `newVendorName`, `newVendorLocation` and `newVendorPhoneNo` **all** have to be present (since making a new vendor takes all three), *or* `existingVendorId` is required.
>
>Also note that:
> - If both `newProductName` and `existingProductId` are provided, `newProductname` will be ignored.
> - Ditto for vendors; `existingVendorId` takes priority if present.
> 
> Clashes (if these situations occur, the entire request will not perform any database mutation, and will simply return `null`):
> - If `newProductName` clashes with the name of an existing name in the database
> - Ditto for `newVendorName` (vendor location and phone number are not enforced to be unique)
> - No name in `newTagNames` can clash with an existing tag name.

`POST /reviews​`
###### Parameters:
|Name|Required?|Description|
|-|-|-|
|existingUserId|Yes|ID of an existing user who wishes to post the review
|existingProductId|Yes, if no newProductName provided|ID of an existing product in the database
|newProductName|Yes, if no existingProductId provided|Name of a new product not in the database
|existingVendorId| Yes, if any of newVendorName, newVendorLocation, newVendorLocation are not provided|ID of an existing vendor in the database
|newVendorName|Yes, if existingVendorId not provided|Name of a new vendor not in the database
|newVendorLocation|Yes, if existingVendorId not provided|Location of a new vendor not in the database
|newVendorPhoneNo|Yes, if existingVendorId not provided|Phone number of a new vendor not in the database
|imagesData|No|A list of base64 strings that represent image data in .png format
|existingTagIds|No|A list of IDs of tags that already exist in the database
|newTagNames|No|A list of names of tags that are not in the database
|rating|Yes|Rating as an integer from 0 to 5
|unit|Yes|String description of the unit quantity that the product comes in (e.g. "bundle", "pack of 5", etc)
|unitsPurchased|Yes|Integer representing how many units were purchased
|pricePerUnit|Yes|Float representing monetary cost of 1 unit
|comments|No|Body text of review







### Delete a review by ID

`DELETE /reviews/<review id>​`

###### Path Variable:
ID of review to be deleted.
###### Parameters:
|Name|Required?|Description|
|-|-|-|
|debug|No|Boolean value; if left out or set to false, then the server will refuse to delete the review if the username of the requester does not match that of the review writer. If set to true, removes this protection. This flag is meant for us to debug conveniently with something like Postman.
###### Returns:
The status "deleted successfully" as simple JSON if deleted successfully, null otherwise.



### Logout
`POST /logout`
> Note that `authToken` is required when logging out! We can't allow people to logout for each other, after all.
###### Parameters:
|Name|Required?|Description|
|-|-|-|
|username|Yes|The username of the requester|
###### Returns:
Not very important, but returns the user object in JSON format upon logout, just to signal success.


## Running Locally

You need:
- Java
- Maven
- Heroku CLI
- PostgreSQL

#### PostgreSQL
You need to download it for your system, and ensure that it explicitly takes in a username and password when launching (I tinkered around with `pg_hba.conf` to do that, but there's probably a GUI method), or else Spring Boot may complain and throw weird PostgreSQL errors on launch.

Start running the database server and import `deployed.sql` into it.

Let us assume for this README that:
- username is `postgres` (the default)
- database name is also `postgres` (default)
- password is `XXXXXX`

```sh
$ sudo service postgresql start
$ sudo -u postgres psql postgres < deployed.sql
```

You can also import `insertTestData.sql` if you want some mock data to experiment with.

#### Server

```sh
$ export JDBC_DATABASE_URL="jdbc:postgresql://localhost/postgres?user=postgres&password=XXXXXX"
$ git clone https://github.com/YongZhengYew/wHeReGotTimeFind_backend
$ cd wHeReGotTimeFind_backend
$ mvn install
$ heroku local
```

> I suggest making this export
> ```sh
> export JDBC_DATABASE_URL="jdbc:postgresql://localhost/postgres?user=postgres&password=XXXXXX"
> ``` 
> permanent for the duration that you will be running this server regularly. For example, by appending it to the back of `~/.bashrc` if running wsl or Linux proper.

Now the server should be reachable by Postman on `http://localhost:5000/`! If you imported `insertTestData.sql`, you can try
```
GET http://localhost:5000/products/productName/brass?username=pedro&authToken=tat
```
You should get
```
[{"name":"grass","id":1}]
```

Let us know if you face any difficulty in setup or the server doesn't seem to work on first try, so we can help to troubleshoot. There's some finnicky, version/system-specific stuff up there, especially with the PostgreSQL configuration.

## Deploying to Heroku

```sh
$ heroku create
$ git push heroku main
```
## Credit
