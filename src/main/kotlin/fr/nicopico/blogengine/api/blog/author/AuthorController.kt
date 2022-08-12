package fr.nicopico.blogengine.api.blog.author

import fr.nicopico.blogengine.api.blog.author.dto.AuthorDTO
import fr.nicopico.blogengine.api.blog.author.dto.CreateAuthorRequestDTO
import fr.nicopico.blogengine.api.blog.author.dto.toDTO
import fr.nicopico.blogengine.api.blog.author.dto.toRequest
import fr.nicopico.blogengine.domain.entities.Author
import fr.nicopico.blogengine.domain.request.Dispatcher
import fr.nicopico.blogengine.domain.request.blog.author.AuthorQueries
import fr.nicopico.blogengine.domain.request.blog.author.create.CreateAuthorRequestHandler
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/blog/authors")
class AuthorController(
    private val dispatcher: Dispatcher,
    private val authorQueries: AuthorQueries
) {

    @GetMapping
    @Operation(summary = "Return all known authors")
    fun getAuthors(): Iterable<AuthorDTO> =
        authorQueries.findAll()
            .map(Author::toDTO)

    @GetMapping("/{id}")
    @Operation(summary = "Get a particular author by its ID")
    fun getAuthor(@PathVariable id: Long): AuthorDTO =
        authorQueries.getById(id)
            .let(Author::toDTO)

    @PostMapping
    @Operation(summary = "Create a new author")
    fun createAuthor(@RequestBody request: CreateAuthorRequestDTO): AuthorDTO =
        dispatcher.dispatch(CreateAuthorRequestHandler::class, request.toRequest())
            .toDTO()
}
