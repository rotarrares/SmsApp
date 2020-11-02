# SmsApp
App for intercepting sms and sending it to a server.

## Requirements
* User can add message stored in local database
* User can delete message stored in local database
* App pings the server and sends all the messages in local database
* App handles server response appropiately (displaying error messages correctly)
* App runs in background
* App intercepts SMS received on the device and stores them in local database

## Message class contains:

- text
- received_from
- sent_timestamp
- received timestamp

![schema_example](Untitled.png)
