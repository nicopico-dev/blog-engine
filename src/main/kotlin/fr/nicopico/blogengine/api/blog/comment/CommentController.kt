package fr.nicopico.blogengine.api.blog.comment

import fr.nicopico.blogengine.api.blog.comment.dto.CommentDTO
import fr.nicopico.blogengine.api.blog.comment.dto.CreateCommentRequestDTO
import fr.nicopico.blogengine.api.blog.comment.dto.toDTO
import fr.nicopico.blogengine.api.blog.comment.dto.toRequest
import fr.nicopico.blogengine.domain.entities.Comment
import fr.nicopico.blogengine.domain.request.Dispatcher
import fr.nicopico.blogengine.domain.request.blog.comment.CommentQueries
import fr.nicopico.blogengine.domain.request.blog.comment.create.CreateCommentRequestHandler
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/blog/posts")
class CommentController(
    private val dispatcher: Dispatcher,
    private val commentQueries: CommentQueries,
) {

    @GetMapping("/{id}/comments")
    @Operation(summary = "Get all comments for the post")
    fun getPostComments(@PathVariable id: Long): Iterable<CommentDTO> =
        commentQueries.findAllByPostId(id)
            .map(Comment::toDTO)

    @PostMapping("/{id}/comments")
    @Operation(summary = "Create a new comment for the post")
    fun createComment(@PathVariable id: Long, @RequestBody request: CreateCommentRequestDTO): CommentDTO =
        dispatcher.dispatch(CreateCommentRequestHandler::class, request.toRequest(postId = id))
            .toDTO()
}
