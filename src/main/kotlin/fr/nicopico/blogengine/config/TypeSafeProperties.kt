package fr.nicopico.blogengine.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("my.typesafe.properties")
data class TypeSafeProperties(
    val text: String,
    val number: Int,
    val flag: Boolean
)
