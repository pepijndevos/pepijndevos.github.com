---
layout: post
title: Writing a web server
categories:
- http
---

A colleague asked what would be an interesting exercise to learn  more about Perl. I think a HTTP server is a good thing to build because it’s a small project that helps you understand web development a lot better.

This post serves as a broad outline of how a HTTP server works, and as a collection of resources to get started.

There is of course the [HTTP specification](http://www.w3.org/Protocols/rfc2616/rfc2616.html) itself. It’s good for looking up specific things, but otherwise not very easy reading.

HTTP is a relatively simple text-based protocol on top of TCP. It consists of a request and a response, both of which are made up of a status line, a number of headers, a blank line, and the request/response body.

What I recommend doing is playing with a simple working server to see what happens.

Lets create a file and start a simple server.

    $ echo 'Hello, world!' > test
    $ python -m SimpleHTTPServer

This will serve the current directory at port 8000. We can now use `curl` to request the file we created. Use the `-v` flag to see the HTTP request and response.

    $ curl -v http://localhost:8000/test
    > GET /test HTTP/1.1
    > User-Agent: curl/7.30.0
    > Host: localhost:8000
    > Accept: */*
    > 
    < HTTP/1.0 200 OK
    < Server: SimpleHTTP/0.6 Python/2.7.6
    < Date: Wed, 12 Mar 2014 17:51:26 GMT
    < Content-type: application/octet-stream
    < Content-Length: 14
    < Last-Modified: Wed, 12 Mar 2014 17:51:06 GMT
    < 
    Hello, world!

Take a while to look up all the headers to see what each one does. Explain what happens to a friend, cat or plant.

Now you can in turn take the role of the client or server. Can you get Python to return you the file using netcat?

    $ nc localhost 8000
    <enter request here>

Now can you get `curl` to talk to you? Start listening with

    $ nc -l 1234

Now in another terminal run

    $ curl http://localhost:1234/test

You’ll see the request in the `netcat` window. Try writing a response. Remember to set `Content-Length` correctly.

Now it is time to actually write the server in the language of choice. Whichever one you use, it is probably loosely based on the Unix C API. To find out more about that, run

    man socket

You’re looking for an `PF_INET`(IPv4) socket of the `SOCK_STREAM`(TCP) type. But other types exist.

Be sure to check out the `SEE ALSO` section for functions for working with the socket.

The basic flow for the web server is as folows.

1. Create the `socket`.
2. `bind` it to a port.
3. Start to `listen`.
4. `accept` an incoming connection. (will block)
5. `read` the request.
6. `write` the response.
7. `close` the connection.
8. Go back to `accept`.

Note that what you do after `accept` is subject to much debate. The simple case outlined above will handle only one request at a time. A few other options.

* Start a new thread to handle the request.
* Use a queue and a fixed pool of threads or processes to handle the requests. Apache does this.
* Handle many requests asynchronously with `select`, `epoll`(linux) or `kqueue`(BSD). Node.js does this.

After you have a basic request/response working, there are many things you could explore.

* Serve static files.
* Add compression with gzip.
* Support streaming requests and responses.
* Run a CGI script.
* Implement most of HTTP 1.0
* Implement some HTTP 1.1 parts.
* Look into pipelining and `Keep-Alive`.
* Look into caching.

