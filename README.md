## minio adapter service
###### CRUD with buckets and objects
[More about minio in this demo-project](minio/minio-readme.md)
___
1. Run minio with [docker-compose](minio/docker-compose.yml)
2. Run application
___
##### To turn-ON email-notification:
1. Set property 
> spring.mail.notification.enabled: true

2. Fill environment variables:
> EMAIL_USER, EMAIL_PASSWORD, EMAIL_HOST

![Notification example](notification.png)
___
- [X] Recommended use [Swagger](http://localhost/swagger-ui/index.html) to upload files