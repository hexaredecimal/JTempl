<div align="center">
    <img src="./assets/jtempl.png" width="40%" />
</div>


# JTempl 
>> A templating library for JAVA inspired by the one seen on tsoding streams 
>> used to created templates for c.

## Why?
- Learn how templating engines work
- I got free time on my hands. 
- Why not. 

## Usage
```sh
$ java -jar JTempl-dist.jar index.jsp
```
This will create a file called IndexTemplate.java with the template code inside a static method called generate. 

You can use the template like this in your code, especially when using BlazingwebX. 

```java

@Get("/")
public static home(BlazingResponse response) {
    var page = IndexTemplate.generate();
    response.setHeader("Content-Type", "text/html");
    response.sendResponse(page);
}
```

## TODO
- Support receiving user arguments on the generate method. 
- Make this a library as well. 

