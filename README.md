Allure report box
=================

A simplish container aimed at generating allure reports in a unifor way and displaying them


Usage
-----

* run container with `docker run -p <myport>:80 pupssman/allure-box http://how.i.will.expose.container:<myport>/` -- container listens on HTTP port 80 inside, you should expose it somehow and pass the exposed base URL as a launch parameter
* send `POST` request with `multipart/form-data` body of report's files to `/generate` handle of the container
* receive back JSON similar to `{"result": "OK", "url": "http://how.i.xxx:<myport>/3/output/index.html"}`
* navigate your browser to said URL and examine the report 
