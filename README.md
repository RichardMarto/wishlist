# Wishlist
## Description
This application provides an API for user's wishlists.

The suported features are:

- Adding products to a wishlist
- Removing products from a wishlist  
- Getting a wishlist
- Verifying if a product is already in a wishlist

## Requirements
### Containers
This project supports [Docker Compose](https://docs.spring.io/spring-boot/how-to/docker-compose.html), [Development Containers](https://containers.dev/) and [Testcontainers](https://testcontainers.com/), so there is no need for development environment preparation, the only tool you'll need is [Docker](https://www.docker.com/).
> Please, note that currently **Intellij Comunity** does not suports development containers.
#### Development container related docs: 
- [IntelliJ Ultimate](https://www.jetbrains.com/help/idea/connect-to-devcontainer.html)
- [VSCode](https://code.visualstudio.com/docs/devcontainers/containers)
### Without development containers
For local setup, you'll need install a JDK 22 manually.
> Note that you'll still need docker due to the **MongoDB Testcontainer**.
## Gradle
### Setup:
`$./gradlew clean install`
### Run the application:
`$ ./gradlew bootRun`
### Run the application in development mode:
`$ ./gradlew bootRun --args='--spring.profiles.active=dev'`

### Run all tests:
`$ ./gradlew :test'`

### Run a single test suite tests:
`$ ./gradlew :test --tests "br.com.labs.wishlist.wishlist.implementation.unit.WishlistServiceUnitTest"`

### Run a single test case tests:
`$ ./gradlew :test --tests "br.com.labs.wishlist.wishlist.implementation.unit.WishlistServiceUnitTest.addIfNotFull_withEmptyWishlist"`