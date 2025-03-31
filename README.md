
# JTempl 
>> A html/Template to java class translation tool

## Why?
Hard to find standalone tools that do this which are not intergrated with some system

## Features
- Simple Syntax (Uses %% instead of context based tags unlike jsp (<= vs <% etc))
- Translates to readable java code
- Repl support


## Usage
```sh
$ jtmpl -p com.your.webapp -d src/com/your/web/app index.xhtml
```
This will create a file called Index.java with the template code inside a static method called generate. 

You can use the template like this in your code, especially when using BlazingwebX. 

```java

@Get("/")
public static home(BlazingResponse response) {
    var page = IndexTemplate.generate();
    response.setHeader("Content-Type", "text/html");
    response.sendResponse(page);
}
```

## Examples
JTempl has a syntax similar to JSP and uses `%%` to start and end template insertions. Examples are available in the <a href="./examples">examples</a> directory. 

