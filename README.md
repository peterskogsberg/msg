# msg

Simple test of messaging app...
+ REST API
+ POST/GET operations
+ JSON response
+ SQLite database

## 3rd party libraries and dependencies
+ Maven project archetype "jersey-quickstart-grizzly"
+ Jersey - used for REST API scaffolding
+ json-lib - used for fluent JSON response building
+ sqlite-jdbc - SQLite library for persisting messages, users and metadata into tables
+ JUnit - set up in project but no "real" test cases yet

## Sample calls
### POST:
+`curl --data "recipient=Alice&message=Hellooo from curl" -i http://localhost:8080/messaging/createNewMessage`

+`curl --data "id=3&id=4&id=1" -i http://localhost:8080/messaging/deleteMessage`


### GET:
+http://localhost:8080/messaging/getNewMessages?recipient=Bob
+http://localhost:8080/messaging/getMessagesWithinRange?recipient=Alice&start=1468171843820


Sample response to the last one:

```
{
  "messages": [
    {
      "id": "1",
      "message": "How are you doing, Alice?",
      "timestamp": "1468267068022"
    },
    {
      "id": "3",
      "message": "Hellooo",
      "timestamp": "1468267100415"
    },
    {
      "id": "4",
      "message": "Hellooo dude",
      "timestamp": "1468267109321"
    }
  ]
}
```

## Running
Import Maven project and in root folder, run
`mvn exec:java`
This should launch the Grizzly/Glassfish server defined as a project dependency.
Then the base URL to the webapp is http://localhost:8080/messaging/

## Improvement/Limitations
A lot of stuff...
+ Client (for example JS based) consuming JSON responses from the API
+ (Real) JUnit test cases
+ JSON generated from POJOs instead (ORM mapping)
+ Index for SQLite column timestamp
+ CONSTANTS for SQLite query strings
+ Authentication/Authorization
+ Moving Strings to properties file / or even i18n
+ JavaDoc
