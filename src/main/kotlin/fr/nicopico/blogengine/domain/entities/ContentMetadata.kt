package fr.nicopico.blogengine.domain.entities

import java.time.Instant
import javax.persistence.*

@Embeddable
class ContentMetadata(
    @ManyToOne
    val author: Author,
    val creationDate: Instant,
    val modificationDate: Instant? = null,
)
