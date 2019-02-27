# klayground
==========================================

This project is a Playground for Kotlin, MVI, Clean Architecture and other things.

 - This project uses [Marvel API](https://developer.marvel.com/) _Data provided by Marvel. © 2016 MARVEL_
 - It is highly inspired / reimplementation of [mosby-mvi](https://github.com/sockeqwe/mosby/tree/master/sample-mvi) sample for now. Will improve in future. MVI on top of MVP
 - Kotlin implementation is inspired from [MVI For Android Video](https://www.youtube.com/watch?v=64rQ9GKphTg) by oldergod

### Clean Architecture

Currently divided into three module :
 - api : For API entities
 - data : For logic, Repositories, Uses Cases, Mappers
 - presentation : Views and Presenters
 - cache module (Future): Using Room in future


    ```
    presentation --> data --> api/cache
    ```


### Branches

Contains two branches
 - [mvi] MVP with MVI implementation

### Libraries

This project uses

 - Clean Architecture
 - Retrofit for API with Moshi Converter
 - Dagger for Dependency Injection
 - Timber for logging
 - Reclaim for RecyclerView Adapters
 - Data Binding
 - RxBinding
 - Leak Canary
 - Architecture Components - `ViewModel` and `LiveData`
 - Spek Framework for testing

### To-do
 - More Features
 - Improve Testing
 - Improve Documentation

### Contributing

You are welcome to open PRs / issues, Give suggestions and feedback
