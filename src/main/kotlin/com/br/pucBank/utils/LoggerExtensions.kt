package com.br.pucBank.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Logger {
    private fun getCallerLogger(): Logger {
        val element = Throwable().stackTrace[2]
        return LoggerFactory.getLogger(element.className)
    }

    fun d(msg: () -> String) {
        val log = getCallerLogger()
        if (log.isDebugEnabled) log.debug(msg())
    }

    fun i(msg: () -> String) {
        val log = getCallerLogger()
        if (log.isInfoEnabled) log.info(msg())
    }

    fun w(msg: () -> String) {
        val log = getCallerLogger()
        if (log.isWarnEnabled) log.warn(msg())
    }

    fun e(msg: String?) {
        val log = getCallerLogger()
        if (log.isErrorEnabled) log.error(msg)
    }
}