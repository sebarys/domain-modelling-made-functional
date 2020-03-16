# Overview 
Notes from book "Domain Modeling Made Functional".

# Table of Contents
1. [Key concepts of DDD](#key-concepts)
    - [Good practices](#good-practices)
2. [Designing high level sketch](#architecture-sketch)
    - [C4](#c4)
    - [Bounded contexts](#bounded-contexts)


<a name="key-concepts"></a>
# Key concepts of DDD 
- A `domian` is an area of knowledge associated with the problem we're trying to solve (which a "domain expert" is expert in).
- A `Domain Model` is a set of simplifications that represent those aspects of a domain that are relevant to a particular problem. The domain model is a part of the solution space, while the domain that it represents is part of the problem space.
- The `Ubiquitous Language` is a set of concepts and vocalbuary that is associated with the domain and is shared by both the team members and the source code.
- A bounded context is a subsystem in the solution space with clear boundaries that distinguish it from other subsystems. A bounded context often corresponds to a subdomain in the problem space. A bounded context also has its own set of concepts and vocabulary, its own dialect of the Ubiquitous Language.
- A `Context Map` is a high level diagram showing a collection of bounded contexts and the relationships between them.
- A `Domain Event` is a record of something that happened in the system. It's always described in the past tense. An event often triggers additional activity.
- A `Command` is a request for some process to happen and is triggered by a person or another event. If the process succeeds, the state of the system changes and one or more Domain Events are recorded.

<a name="good-practices"></a>
## Good practices 
- Focus on events and processes rather than data.
- Partition the problem domain into smaller subdomains.
- Create a model of each subdomain in the solution,
- Develop an "everywhere language" that can be shared between everyone involved in the project.
- Don't start with implementation - human (domain expert) readable verion of domain description will ease communication with non technical people + is good starting point for documentation (see: DomianDescription.md)

<a name="architecture-sketch"></a>
# Designing high level sketch 

<a name="c4"></a>
## C4
After gathering reqirements and estabilishing bounded context etc. we're able to sketch high level overview of our software.
C4 (http://static.codingthearchitecture.com/c4.pdf) is a concept that introduce four levels of software architecture:
- `system context` - top level, representing the entire system
- system context comprises a number of `containers` which are separatable deployable units such as website, webserver, database etc.
- each container comprises a number of `components` - the major structural building blocks in the code
- each component comprises a number of `classes` (modules) - set of low-level methods or functions

<a name="bounded-contexts"></a>
## Bounded contexts
Bounded contexts are first and foremostly **logical** boundaries that could result in separately deployable units, but it is not requied.

If you have truly decoupled components you're receiving autonomous components. You can later start communicate directly by calling functions, synchronous or by using queueing mechanism. Each approach has its own benefits and don't come for free. First understand which components are communicating with each other, then try understand what type of commnication will be required to fulfill requirements. Truly decoupled components should allow you easily to change communication and you gives you possibility to not commit to particular solution on design phase.

### Communication between bounded contexts
Communication between bounded contexts should be by DTOs - exchange data, not logic.

Each bonded context acts as trust boundary,validate data on start and trust it inside bounded context workflow. Validate data when transforming DTO to domain object and pass inside only information that are required - not everyting that you will receive must be used. 

"Be conservative in what you send, be liberal in what you accept from others" (https://en.wikipedia.org/wiki/Robustness_principle)

### Contracts between bounded contexts
Who should decide about contract? Depends on relationship:
- `Shared Kernel` - two bounded contexts share common domain design, so both teams must collaborate. Changes in DTOs only in consultation with other affected bounded contexts
- `Consumer Driven Contract` (Customer/Supplier) - downstream (consumer) defines the contract that they want the upstream context to provide.
- `Conformist` - opposite to consumer driven, downstream accept contract defined by upstream.

### Anti coruption layer
To prevent domain model to be corrupted sometimes translation layer is required. If two context has different vocalbuary it gives us less coupled bounded contexts (only outer layer need to know about outside world and different vocabluary).

### Bounded context workflow

Each bonded context should work as component with defined workflows. Each workflow should define inputs, outputs, dependencies and steps to perform.

Each worklow could result in proces that perform steps in "railway pattern".

Using Onion architecture (domain code in core, I/O on edges) will allow you to not leak knowledge about external world to your domain. This apprach will allow you also to stay `persistence ignorant` - domain model is based only on domain concepts and shouldn't be aware of any persistence mechanism.
