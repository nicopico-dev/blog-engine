package fr.nicopico.blogengine.api.blog.author.dto

import fr.nicopico.blogengine.domain.request.blog.author.create.CreateAuthorRequest

@JvmInline
value class CreateAuthorRequestDTO(
    val request: CreateAuthorRequest
)

fun CreateAuthorRequestDTO.toRequest(): CreateAuthorRequest = request
