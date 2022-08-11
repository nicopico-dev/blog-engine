package fr.nicopico.blogengine.test

import fr.nicopico.blogengine.domain.entities.*
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*

fun anyId(): Arb<Long> = Arb.long(min = 1)

fun anyName(): Arb<String> = Arb.string(minSize = 1).filterNot {
    it.contains('\\') || it.contains('"')
}

fun anyEmailStr(): Arb<String> = Arb.email()

fun anyEmail(): Arb<Email> = anyEmailStr().map { Email(it) }

fun anyAuthor(withId: Boolean = true): Arb<Author> = arbitrary {
    val id = if (withId) anyId().bind() else null
    val name = anyName().orNull().bind()
    val email = anyEmail().bind()
    Author(id, name, email)
}

fun anyPost(
    author: Author? = null,
    withId: Boolean = true,
    withComments: Boolean = false,
): Arb<Post> = arbitrary {
    val id = if (withId) anyId().bind() else null
    val title = Arb.string().bind()
    val content = Arb.string().bind()
    val comments: List<Comment> = if (withComments) Arb.list(anyComment()).bind() else emptyList()
    val metadata = anyMetadata(author).bind()
    Post(id, title, content, comments, metadata)
}

fun anyComment(
    author: Author? = null,
    post: Post? = null,
    withId: Boolean = true,
): Arb<Comment> = arbitrary {
    val id = if (withId) anyId().bind() else null
    val text = Arb.string().bind()

    @Suppress("NAME_SHADOWING")
    val post = post ?: anyPost().bind()
    val metadata = anyMetadata(author).bind()
    Comment(id, text, post, metadata)
}

fun anyMetadata(author: Author?): Arb<ContentMetadata> = arbitrary {
    @Suppress("NAME_SHADOWING")
    val author = author ?: anyAuthor().bind()
    val creationDate = Arb.instant().bind()
    val modificationDate = Arb.instant().orNull().bind()
    ContentMetadata(author, creationDate, modificationDate)
}
