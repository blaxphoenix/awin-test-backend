# awin-test-backend

A test backend service for a mock Awin frontend.

## TODO – general

- [x] git repository
- [x] java 25
- [x] .sdkrc
- [x] maven
- [x] db
    - [x] postgres
    - [x] flyway
- [x] GitHub actions
- [x] hexagonal architecture → layers
- [x] http files for testing
- [ ] application.yml → separate profiles (local, deployed, test, etc.)
- [ ] spring security + JWT?
- [ ] rest client → http interfaces and circuit breaker?
- [x] exception handling
- [ ] thread local context?
- [x] MDC for logging + logback configs
- [ ] swagger + UI?
- [x] dto validations
- [x] unit/integration tests
    - [ ] test containers?
- [ ] caching?
- [ ] virtual threads?

## TODO – features

- [x] overall api design (e.g., u2m/v1)
    - [x] spring boot 4 api versioning? 
- [ ] login?
- [x] user info
    - [x] delete it from all other tables too (foreign key)
    - [x] currency?
- [x] todo list
- [ ] performance
    - [x] transactions
    - [x] revenue 
    - [ ] clicks
- [ ] reports/metrics?