# Food Delivery Service


###  After Matching

Real time location update for driver and client after a match
- Driver app has a websocket connection to `LocationSharing` server
- Driver app continuously push connection to the server
- Client has the same websocket connection `LocationSharing` service
- Location is periodically pushed to client app
- Client app updates driver location on the screen


Refs
- https://github.com/gitgik/distributed-system-design/blob/master/designing_uber_backend.md
- https://github.com/qlint/geolocation-service-delivery