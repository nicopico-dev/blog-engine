package fr.nicopico.blogengine.api.blog

import fr.nicopico.blogengine.api.blog.dto.*
import fr.nicopico.blogengine.api.blog.request.CreatePostRequestDTO
import fr.nicopico.blogengine.api.blog.request.toRequest
import fr.nicopico.blogengine.domain.entities.Post
import fr.nicopico.blogengine.domain.request.Dispatcher
import fr.nicopico.blogengine.domain.request.blog.post.PostQueries
import fr.nicopico.blogengine.domain.request.blog.post.delete.DeletePostRequest
import fr.nicopico.blogengine.domain.request.blog.post.create.CreatePostRequestHandler
import fr.nicopico.blogengine.domain.request.blog.post.delete.DeletePostRequestHandler
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/blog/posts")
class PostController(
    private val dispatcher: Dispatcher,
    private val postQueries: PostQueries,
) {

    @GetMapping
    fun getPosts(): Iterable<PostDTO> =
        postQueries.findAll()
            .map(Post::toDTO)

    @GetMapping("/{id}")
    fun getPostDetails(@PathVariable id: Long): PostDetailsDTO =
        postQueries.getById(id)
            .let(Post::toDetailsDTO)

    @PostMapping
    fun createPost(@RequestBody request: CreatePostRequestDTO): PostDTO =
        dispatcher.dispatch(CreatePostRequestHandler::class, request.toRequest())
            .toDTO()

    @DeleteMapping("/{id}")
    fun deletePost(@PathVariable id: Long): Unit =
        dispatcher.dispatch(DeletePostRequestHandler::class, DeletePostRequest(postId = id))
}
