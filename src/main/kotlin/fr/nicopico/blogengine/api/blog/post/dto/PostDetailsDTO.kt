package fr.nicopico.blogengine.api.blog.post.dto

import fr.nicopico.blogengine.api.blog.shared.dto.MetadataDTO
import fr.nicopico.blogengine.api.blog.shared.dto.toDTO
import fr.nicopico.blogengine.domain.entities.Post

data class PostDetailsDTO(
    val id: Long,
    val title: String,
    val content: String,
    val metadata: MetadataDTO,
)

fun Post.toDetailsDTO() = PostDetailsDTO(
    id = id!!,
    title = title,
    content = content,
    metadata = contentMetadata.toDTO(),
)
