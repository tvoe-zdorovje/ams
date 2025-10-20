package by.anatolyloyko.ams.common.infrastructure.exception

class AccessDeniedException(
    reason: String,
    cause: Throwable? = null
) : RuntimeException("Access denied: $reason", cause)
