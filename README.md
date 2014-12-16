monitoring
==========

Currently not working...

This is just a little java web application that provides monitoring via a webcam. At this point it is just a war that needs to be deployed to tomcat or other application server. Vaadin with Valo is used for the front end, and the http://webcam-capture.sarxos.pl/ library is used to control the webcam. Also using Guava EventBus as the glue.

TODO:
1. IP cameras
2. Recent photos captured via motion detection
3. Config page
4. Alert via email, etc.
5. Arduino integration???
