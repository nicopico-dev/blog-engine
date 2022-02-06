package fr.nicopico.blogengine.infra

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

private val loggerRegistry = WeakHashMap<Class<out Any>, Logger>()

private val infraLogger = LoggerFactory.getLogger("fr.nicopico.blogengine.infra.Logging")

private fun Any.logger(): Logger {
    val clazz = this::class.java
    return loggerRegistry.getOrPut(clazz) {
        if (infraLogger.isTraceEnabled) {
            infraLogger.trace("Creating new logger for $clazz")
        }
        LoggerFactory.getLogger(clazz)
    }
}

fun Any.logTrace(throwable: Throwable? = null, message: () -> String) {
    val logger = logger()
    LogLevel.TRACE.handleLog(logger, throwable, message)
}

fun Any.logDebug(throwable: Throwable? = null, message: () -> String) {
    val logger = logger()
    LogLevel.DEBUG.handleLog(logger, throwable, message)
}

fun Any.logInfo(throwable: Throwable? = null, message: () -> String) {
    val logger = logger()
    LogLevel.INFO.handleLog(logger, throwable, message)
}

fun Any.logWarn(throwable: Throwable? = null, message: () -> String) {
    val logger = logger()
    LogLevel.WARN.handleLog(logger, throwable, message)
}

fun Any.logError(throwable: Throwable? = null, message: () -> String) {
    val logger = logger()
    LogLevel.ERROR.handleLog(logger, throwable, message)
}

private enum class LogLevel(
    private val isLevelEnabled: Logger.() -> Boolean,
    private val logMessage: Logger.(String) -> Unit,
    private val logMessageWithThrowable: Logger.(String, Throwable) -> Unit,
) {
    TRACE(Logger::isTraceEnabled, Logger::trace, Logger::trace),
    DEBUG(Logger::isDebugEnabled, Logger::debug, Logger::debug),
    INFO(Logger::isInfoEnabled, Logger::info, Logger::info),
    WARN(Logger::isWarnEnabled, Logger::warn, Logger::warn),
    ERROR(Logger::isErrorEnabled, Logger::error, Logger::error);

    fun handleLog(logger: Logger, throwable: Throwable? = null, message: () -> String) {
        if (logger.isLevelEnabled()) {
            if (throwable != null) {
                logger.logMessageWithThrowable(message(), throwable)
            } else {
                logger.logMessage(message())
            }
        }
    }
}
