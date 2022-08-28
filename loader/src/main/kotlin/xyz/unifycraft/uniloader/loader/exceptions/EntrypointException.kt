package xyz.unifycraft.uniloader.loader.exceptions

class EntrypointException(
    message: String,
    cause: Throwable
) : RuntimeException(message, cause)
