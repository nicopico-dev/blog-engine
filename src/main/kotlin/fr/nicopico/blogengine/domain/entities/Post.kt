package fr.nicopico.blogengine.domain.entities

import javax.persistence.*

@Entity
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,
    @Column(name = "title")
    val title: String,
    @Column(name = "content")
    val content: String,
    @OneToMany(
        mappedBy = "post",
        orphanRemoval = true,
    )
    val comments : List<Comment> = emptyList(),
    @Embedded
    val contentMetadata: ContentMetadata,
    @Column(name = "permalink")
    val permalink: String = "test",
)
