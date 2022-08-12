package fr.nicopico.blogengine.api.blog.post.dto

import fr.nicopico.blogengine.api.blog.shared.dto.MetadataDTO
import fr.nicopico.blogengine.api.blog.shared.dto.toDTO
import fr.nicopico.blogengine.domain.entities.Post

data class PostDTO(
    val id: Long,
    val title: String,
    val metadata: MetadataDTO,
)

fun Post.toDTO(): PostDTO = PostDTO(
    id = id!!,
    title = title,
    metadata = contentMetadata.toDTO(),
)
