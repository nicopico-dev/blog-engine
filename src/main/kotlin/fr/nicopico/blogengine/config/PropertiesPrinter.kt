package fr.nicopico.blogengine.config

import fr.nicopico.blogengine.infra.logInfo
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

/**
 * Print the content of `my.typesafe.properties` after the application is initialized.
 */
@Component
@Profile("dev")
class PropertiesPrinter(
    private val typeSafeProperties: TypeSafeProperties
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        logInfo { "PROPERTIES PRINTER: $typeSafeProperties" }
    }
}
