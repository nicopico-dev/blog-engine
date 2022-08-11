package fr.nicopico.blogengine.api.blog

import fr.nicopico.blogengine.domain.entities.Author
import fr.nicopico.blogengine.domain.entities.Email
import fr.nicopico.blogengine.domain.request.Dispatcher
import fr.nicopico.blogengine.domain.request.blog.author.AuthorQueries
import fr.nicopico.blogengine.domain.request.blog.author.create.CreateAuthorRequestHandler
import io.mockk.*
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@WebMvcTest(AuthorController::class)
internal class AuthorControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    //region Dependencies
    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun dispatcher() = mockk<Dispatcher>()
        @Bean
        fun queries() = mockk<AuthorQueries>()
    }

    @Autowired
    private lateinit var dispatcher: Dispatcher

    @Autowired
    private lateinit var queries: AuthorQueries
    //endregion

    @AfterEach
    internal fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `GET should provide list of all authors`() {
        val authors = listOf(
            Author(id = 1, name = "JK Rowling", email = Email("jk.row@gmail.com")),
            Author(id = 2, name = "Stephen King", email = Email("king@tower.com")),
            Author(id = 3, email = Email("anonymous@internet.com")),
        )
        every { queries.findAll() } returns authors

        mvc.perform(get("/blog/authors"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath<List<*>>("$", hasSize(3)))
            .andExpect(jsonPath("$[0].name", `is`("JK Rowling")))
            .andExpect(jsonPath("$[2].name", `is`("Unknown")))

        verify { dispatcher wasNot Called}
    }

    @Test
    fun `GET should return a 500 server error when an exception occurs`() {
        every { queries.findAll() } throws RuntimeException("ERROR!")

        mvc.perform(get("/blog/authors"))
            .andExpect(status().is5xxServerError)

        verify { dispatcher wasNot Called}
    }

    @Test
    fun `GET by id should provide the corresponding author`() {
        val author = Author(id = 1, name = "JK Rowling", email = Email("jk.row@gmail.com"))
        every { queries.getById(1) } returns author

        mvc.perform(get("/blog/authors/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name", `is`("JK Rowling")))

        verify { dispatcher wasNot Called}
    }

    @Test
    fun `GET by id should return a 404 error when the author id is not found`() {
        every { queries.getById(any()) } throws NoSuchElementException("Author not found")

        mvc.perform(get("/blog/authors/1"))
            .andExpect(status().isNotFound)

        verify { dispatcher wasNot Called}
    }

    @Test
    fun `GET by id should return a 500 server error when an exception occurs`() {
        every { queries.getById(any()) } throws RuntimeException("ERROR!")

        mvc.perform(get("/blog/authors/312"))
            .andExpect(status().is5xxServerError)

        verify { dispatcher wasNot Called}
    }

    @Test
    fun `POST should create a new author`() {
        val createdAuthor = Author(id = 46, name = "authorName", email = Email("author@mail"))
        every { dispatcher.dispatch(CreateAuthorRequestHandler::class, any()) } returns createdAuthor

        mvc.perform(
            post("/blog/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name": "G.R.R. Martin", "email": "grr@martin.co"}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id", equalTo(createdAuthor.id), Long::class.java))
            .andExpect(jsonPath("$.name", equalTo(createdAuthor.name)))
            .andExpect(jsonPath("$.email", equalTo(createdAuthor.email.address)))

        // TODO Check the request

        verify { queries wasNot Called }
    }

    // TODO Create un-named author
    // TODO Prevent creating duplicate authors
    // TODO 500 mapping
}
