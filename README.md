# REST api for shop app

REST API for shop app with mailing system. 
Repository and service layers are tested.
Integration tests in future.

API is role based, user can be:

public static final String SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String MANAGER = "ROLE_MANAGER";
    public static final String USER = "ROLE_USER";
    public static final String GUEST = "ROLE_GUEST";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

| ROLE       	  | 
|:---------------:|
| SUPER ADMIN     | 
| ADMIN           |
| MANAGER         |
| USER            |
| GUEST           |
| ANONYMOUS       |

## Used tools

- Java
- SpringBoot
- SpringSecurity
- JUint
- Maven (build)
- MySQL
- XAMPP

## Requestes

### Make order

#### Request param: shoppingCartCode

`POST /api/orders/create?shoppingCartCode=123`

## Add Product

`POST /api/products/add`

### Request Body

``` json
{
	"name": "default",
	"description": "default",
	"price": 22.20,
	"inStock": 23,
	"imgUrl": "defaultURL"
}
```

## Delete Wish List

`DELETE /api/whishlists/delete`

## Add Product To Shopping Cart

### Request Params: shoppingCartCode, productCode

`POST /api/carts/item/add?shoppingCartCode=213&productCode=121`