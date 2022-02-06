package fr.nicopico.blogengine.api.blog

import fr.nicopico.blogengine.api.blog.dto.AuthorDTO
import fr.nicopico.blogengine.api.blog.dto.toDTO
import fr.nicopico.blogengine.api.blog.request.CreateAuthorRequestDTO
import fr.nicopico.blogengine.api.blog.request.toRequest
import fr.nicopico.blogengine.domain.entities.Author
import fr.nicopico.blogengine.domain.request.Dispatcher
import fr.nicopico.blogengine.domain.request.blog.author.AuthorQueries
import fr.nicopico.blogengine.domain.request.blog.author.create.CreateAuthorRequestHandler
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/blog/authors")
class AuthorController(
    private val dispatcher: Dispatcher,
    private val authorQueries: AuthorQueries
) {

    @GetMapping
    fun getAuthors(): Iterable<AuthorDTO> =
        authorQueries.findAll()
            .map(Author::toDTO)

    @GetMapping("/{id}")
    fun getAuthor(@PathVariable id: Long): AuthorDTO =
        authorQueries.getById(id)
            .let(Author::toDTO)

    @PostMapping
    fun createAuthor(@RequestBody request: CreateAuthorRequestDTO): AuthorDTO =
        dispatcher.dispatch(CreateAuthorRequestHandler::class, request.toRequest())
            .toDTO()
}
