package fr.nicopico.blogengine.domain.entities

import javax.persistence.*

@Entity
class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String? = null,
    val email: Email,
)
