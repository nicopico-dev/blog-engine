package fr.nicopico.blogengine.api.blog.comment.dto

import fr.nicopico.blogengine.api.blog.shared.dto.MetadataDTO
import fr.nicopico.blogengine.api.blog.shared.dto.toDTO
import fr.nicopico.blogengine.domain.entities.Comment

data class CommentDTO(
    val id: Long,
    val text: String,
    val metadata: MetadataDTO
)

fun Comment.toDTO() = CommentDTO(
    id = id!!,
    text = text,
    metadata = contentMetadata.toDTO()
)
