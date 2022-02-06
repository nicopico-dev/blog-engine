package fr.nicopico.blogengine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlogEngineApplication

fun main(args: Array<String>) {
    runApplication<BlogEngineApplication>(*args)
}
