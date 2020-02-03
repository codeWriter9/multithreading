# Multi-threading

Collection of some multi-threading problems and how to tackle them 

## Getting Started

Do a git clone 


### Prerequisites

You should only need a git client like git bash, an IDE, maven 3.x and certainly the Java platform with sdk


## Running the tests

run mvn clean install

### Break down into end to end tests

Current Tests are as follows

1. [Colors Test](https://github.com/codeWriter9/multithreading/blob/master/src/test/java/com/concepts/concurrency/multithreading/ColorTest.java) : A JUnit 
that tests if ColorPrinter prints colors Red Green Blue in sequence inspite of them being run by three different threads

```
ColorTest.checkColorsInSequence using pre-Java 5 wait and notify
ColorTest.checkColorsInSequenceThroughExecutorService using Java 5 execution service
```

2. [Prime Numer Generator Test](https://github.com/codeWriter9/multithreading/blob/master/src/test/java/com/concepts/concurrency/multithreading/PrimeGeneratorTest.java):
A Junit that uses the Cyclic Barrier to use multiple threads to generate prime numbers

```
PrimeGeneratorTest.testPrimeGenerators using Cyclic Barrier - generate primes till 20,000
PrimeGeneratorTest.testPrimeGeneratorsTiny using Cyclic Barrier - generate primes till 500
PrimeGeneratorTest.testPrimeGeneratorsLarge using Cyclic Barrier - generate primes till 1000,000
```


## Built With

* [Eclipse](https://www.eclipse.org/downloads/) - Integrated development Environment
* [Maven](https://maven.apache.org/) - Dependency Management


## Contributing

By Invitation only

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Sanjay Ghosh** - *Initial work* - [Multithreading](https://github.com/codeWriter9/multithreading)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details


