package fr.nicopico.blogengine.domain.entities

import javax.persistence.*

@Entity
class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,
    @Column(name = "name")
    val name: String? = null,
    @Column(name = "email")
    val email: Email,
)
