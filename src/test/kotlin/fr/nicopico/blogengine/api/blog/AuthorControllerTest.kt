package fr.nicopico.blogengine.api.blog

import fr.nicopico.blogengine.api.blog.author.AuthorController
import fr.nicopico.blogengine.domain.entities.Author
import fr.nicopico.blogengine.domain.entities.Email
import fr.nicopico.blogengine.domain.request.Dispatcher
import fr.nicopico.blogengine.domain.request.blog.author.AuthorQueries
import fr.nicopico.blogengine.domain.request.blog.author.create.CreateAuthorRequest
import fr.nicopico.blogengine.domain.request.blog.author.create.CreateAuthorRequestHandler
import fr.nicopico.blogengine.test.*
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import kotlin.random.Random
import kotlin.random.nextInt

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
        checkAllBlocking(Arb.list(anyAuthor())) { authors ->
            every { queries.findAll() } returns authors

            mvc.perform(get("/blog/authors"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath<List<*>>("$", hasSize(authors.size)))
                .andExpect {
                    if (authors.isNotEmpty()) {
                        val index = Random.nextInt(authors.indices)
                        jsonPath("$[$index].id", equalTo(authors[index].id), Long::class.java)
                        jsonPath("$[$index].name", equalTo(authors[index].name))
                        jsonPath("$[$index].email", equalTo(authors[index].email.address))
                    }
                }
        }
    }

    @Test
    fun `GET by id should provide the corresponding author`() {
        checkAllBlocking(anyId(), anyAuthor()) { authorId, author ->
            val authorIdSlot = slot<Long>()
            every { queries.getById(capture(authorIdSlot)) } returns author

            mvc.perform(get("/blog/authors/{id}", authorId))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(author.id), Long::class.java))
                .andExpect(jsonPath("$.name", equalTo(author.name)))
                .andExpect(jsonPath("$.email", equalTo(author.email.address)))

            assertThat(authorIdSlot.captured).isEqualTo(authorId)
        }
    }

    @Test
    fun `POST should create a new named author`() {
        checkAllBlocking(anyId(), anyName(), anyEmailStr()) { authorId, authorName, authorEmail ->
            val requestSlot = slot<CreateAuthorRequest>()
            every { dispatcher.dispatch(CreateAuthorRequestHandler::class, capture(requestSlot)) } returns Author(
                authorId,
                authorName,
                Email(authorEmail)
            )

            mvc.perform(
                post("/blog/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"name": "$authorName", "email": "$authorEmail"}""")
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id", equalTo(authorId), Long::class.java))
                .andExpect(jsonPath("$.name", equalTo(authorName)))
                .andExpect(jsonPath("$.email", equalTo(authorEmail)))

            requestSlot.captured.also { request ->
                assertAll("request",
                    { assertThat(request.name).isEqualTo(authorName) },
                    { assertThat(request.email.address).isEqualTo(authorEmail) }
                )
            }
        }
    }

    @Test
    fun `POST should create a new anonymous author`() {
        checkAllBlocking(anyId(), anyEmailStr()) { authorId, authorEmail ->
            val createdAuthor = Author(id = authorId, email = Email(authorEmail))
            val requestSlot = slot<CreateAuthorRequest>()
            every { dispatcher.dispatch(CreateAuthorRequestHandler::class, capture(requestSlot)) } returns createdAuthor

            mvc.perform(
                post("/blog/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"email": "$authorEmail"}""")
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id", equalTo(authorId), Long::class.java))
                .andExpect(jsonPath("$.name", `is`(emptyOrNullString())))
                .andExpect(jsonPath("$.email", equalTo(authorEmail)))

            requestSlot.captured.also { request ->
                assertAll("request",
                    { assertThat(request.name).isNull() },
                    { assertThat(request.email.address).isEqualTo(authorEmail) }
                )
            }
        }
    }
}
