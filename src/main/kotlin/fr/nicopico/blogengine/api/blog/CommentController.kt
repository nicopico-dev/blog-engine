package fr.nicopico.blogengine.api.blog

import fr.nicopico.blogengine.api.blog.dto.CommentDTO
import fr.nicopico.blogengine.api.blog.dto.toDTO
import fr.nicopico.blogengine.api.blog.request.CreateCommentRequestDTO
import fr.nicopico.blogengine.api.blog.request.toRequest
import fr.nicopico.blogengine.domain.entities.Comment
import fr.nicopico.blogengine.domain.request.Dispatcher
import fr.nicopico.blogengine.domain.request.blog.comment.CommentQueries
import fr.nicopico.blogengine.domain.request.blog.comment.create.CreateCommentRequestHandler
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/blog/posts")
class CommentController(
    private val dispatcher: Dispatcher,
    private val commentQueries: CommentQueries,
) {

    @GetMapping("/{id}/comments")
    fun getPostComments(@PathVariable id: Long): Iterable<CommentDTO> =
        commentQueries.findAllByPostId(id)
            .map(Comment::toDTO)

    @PostMapping("/{id}/comments")
    fun createComment(@PathVariable id: Long, @RequestBody request: CreateCommentRequestDTO): CommentDTO =
        dispatcher.dispatch(CreateCommentRequestHandler::class, request.toRequest(postId = id))
            .toDTO()
}
