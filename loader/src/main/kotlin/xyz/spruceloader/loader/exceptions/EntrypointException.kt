package xyz.spruceloader.loader.exceptions

class EntrypointException(
    message: String,
    cause: Throwable
) : RuntimeException(message, cause)
