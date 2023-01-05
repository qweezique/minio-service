package qwee.zique.minioservice.exception

class BucketNotFoundException(s: String) : RuntimeException(s)
class ObjectNotFoundException(s: String) : RuntimeException(s)
class OriginalFilenameNotExistException(s: String) : RuntimeException(s)