# Half URL
URL shortener API made with Java Spring

### Requirements
Java 1.8

### Run

./mvnw spring-boot:run

### Usage

Endpoints:

(GET) /{KEY} 
returns a JSON object containing the URL saved with KEY

(POST) /save/{URL}
Saves URL and returns a key

(POST) /save/{URL}/{KEY} 
Saved URl with the key KEY

Example usage: 

POST /save/www.example.com -> $ { "key": "5C2C5D" }

GET  /5C2C5D               -> $ { "url": "www.example.com" }

#### TODO

* More documentation (Javadocs)
