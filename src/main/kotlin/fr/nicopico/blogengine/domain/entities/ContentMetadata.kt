package fr.nicopico.blogengine.domain.entities

import java.time.Instant
import javax.persistence.*

@Embeddable
class ContentMetadata(
    @ManyToOne
    @JoinColumn(name = "author_id")
    val author: Author,
    @Column(name = "creation_date")
    val creationDate: Instant,
    @Column(name = "modification_date")
    val modificationDate: Instant? = null,
)
