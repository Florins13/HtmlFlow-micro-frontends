package org.xmlet.htmlapifaster

/**
 * Functional interface for Kotlin coroutine suspend consumers used in async HtmlFlow views.
 * Copied from xsd2poet-kotlin since that module is not part of this local build.
 */
interface SuspendConsumer<E, M> {
    suspend fun E.accept(model: M)
}
