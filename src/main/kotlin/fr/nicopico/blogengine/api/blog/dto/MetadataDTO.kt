package fr.nicopico.blogengine.api.blog.dto

import fr.nicopico.blogengine.domain.entities.ContentMetadata
import java.time.Instant

data class MetadataDTO(
    val author: AuthorDTO,
    val creationDate: Instant,
    val modificationDate: Instant?,
)

fun ContentMetadata.toDTO() = MetadataDTO(
    author = author.toDTO(),
    creationDate = creationDate,
    modificationDate = modificationDate,
)
