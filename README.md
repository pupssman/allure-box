Allure report box
=================

A simplish container aimed at generating allure reports in a unifor way and displaying them


Usage
-----

* run container with `docker run -p <myport>:80 pupssman/allure-box http://how.i.will.expose.container:<myport>/` -- container listens on HTTP port 80 inside, you should expose it somehow and pass the exposed base URL as a launch parameter
* send `POST` request with `multipart/form-data` body of report's files to `/generate` handle of the container
* receive back JSON similar to `{"result": "OK", "url": "http://how.i.xxx:<myport>/3/output/index.html"}`
* navigate your browser to said URL and examine the report

Minimalistic python client
--------------------------

This serves as an example for a simple client to this container. With little work it can be reduced to a bash-usable one-liner.

```python
In [13]: import os

In [14]: import requests

In [15]: d = '/trash/areports/'

In [17]: print requests.post('http://localhost:8083/generate', files={f: open(os.path.join(d, f), 'rb').read() for f in os.listdir(d) if os.path.isfile(os.path.join(d, f))}).json()['url']
http://localhost:8083//5/output/index.html
```

Running tests
-------------

* install `pytest` and `requests` python packages
* build and deploy container as in `Usage`
* run `py.test --boxurl=<container_endpoint_url> tests.py`
