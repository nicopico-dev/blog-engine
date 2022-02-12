package fr.nicopico.blogengine.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * These properties can be set in `application.yaml` or equivalent files:
 * my.typesafe.properties.text: hello!
 *
 * They can also be overridden by environment variable:
 * MY_TYPESAFE_PROPERTIES_TEXT=bye!
 */
@ConstructorBinding
@ConfigurationProperties("my.typesafe.properties")
data class TypeSafeProperties(
    val text: String,
    val number: Int,
    val flag: Boolean
)
