package fr.nicopico.blogengine.api.blog.request

import fr.nicopico.blogengine.domain.request.blog.post.create.CreatePostRequest

@JvmInline
value class CreatePostRequestDTO(
    val request: CreatePostRequest
)

fun CreatePostRequestDTO.toRequest(): CreatePostRequest = request
