package fr.nicopico.blogengine.api.blog

import fr.nicopico.blogengine.domain.entities.Author
import fr.nicopico.blogengine.domain.entities.Email
import fr.nicopico.blogengine.domain.request.Dispatcher
import fr.nicopico.blogengine.domain.request.blog.author.AuthorQueries
import fr.nicopico.blogengine.domain.request.blog.author.create.CreateAuthorRequestHandler
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.*
import org.mockito.Mockito.verifyNoInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@WebMvcTest(AuthorController::class)
internal class AuthorControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @MockBean
    private lateinit var dispatcher: Dispatcher

    @MockBean
    private lateinit var queries: AuthorQueries

    @Test
    fun `GET should provide list of all authors`() {
        val authors = listOf(
            Author(id = 1, name = "JK Rowling", email = Email("jk.row@gmail.com")),
            Author(id = 2, name = "Stephen King", email = Email("king@tower.com")),
            Author(id = 3, email = Email("anonymous@internet.com")),
        )
        given(queries.findAll()).willReturn(authors)

        mvc.perform(get("/blog/authors"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath<List<*>>("$", hasSize(3)))
            .andExpect(jsonPath("$[0].name", `is`("JK Rowling")))
            .andExpect(jsonPath("$[2].name", `is`("Unknown")))

        verifyNoInteractions(dispatcher)
    }

    @Test
    fun `GET should return a 500 server error when an exception occurs`() {
        given(queries.findAll()).willThrow(RuntimeException::class.java)

        mvc.perform(get("/blog/authors"))
            .andExpect(status().is5xxServerError)

        verifyNoInteractions(dispatcher)
    }

    @Test
    fun `GET by id should provide the corresponding author`() {
        val author = Author(id = 1, name = "JK Rowling", email = Email("jk.row@gmail.com"))
        given(queries.getById(1)).willReturn(author)

        mvc.perform(get("/blog/authors/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name", `is`("JK Rowling")))

        verifyNoInteractions(dispatcher)
    }

    @Test
    fun `GET by id should return a 404 error when the author id is not found`() {
        given(queries.getById(anyLong())).willThrow(NoSuchElementException::class.java)

        mvc.perform(get("/blog/authors/1"))
            .andExpect(status().isNotFound)

        verifyNoInteractions(dispatcher)
    }

    @Test
    fun `GET by id should return a 500 server error when an exception occurs`() {
        given(queries.getById(anyLong())).willThrow(RuntimeException::class.java)

        mvc.perform(get("/blog/authors/312"))
            .andExpect(status().is5xxServerError)

        verifyNoInteractions(dispatcher)
    }

    @Test
    fun `POST should create a new author`() {
        val createdAuthor = Author(id = 46, name = "authorName", email = Email("author@mail"))
        given(dispatcher.dispatch(eq(CreateAuthorRequestHandler::class), any()))
            .willReturn(createdAuthor)

        mvc.perform(
            post("/blog/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name": "G.R.R. Martin", "email": "grr@martin.co"}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id", `is`(createdAuthor.id)))
            .andExpect(jsonPath("$.name", `is`(createdAuthor.name)))
            .andExpect(jsonPath("$.email", `is`(createdAuthor.email)))

        // TODO Check the request

        verifyNoInteractions(queries)
    }

    // TODO Create un-named author
    // TODO Prevent creating duplicate authors
    // TODO 500 mapping
}
