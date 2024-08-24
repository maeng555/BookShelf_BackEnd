package gyus.mybookshelf.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.time.Instant

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "member")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler"])
abstract class BaseMember(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0,
    @Column(length = 20)
    open val name: String,
    @Column(unique = true)
    open var email: String,
    open var providerId: String,
    open var role:String = "ROLE_MEMBER",
    open var password: String = "1234",
    open val createdDt: Instant = Instant.now(),
    open var updatedDt: Instant = Instant.now()
) {
    override fun toString(): String {
        return "Member(id : $id, name : $name, email : $email,  $role, $createdDt, $updatedDt)"
    }
}

@Entity
@DiscriminatorValue("NORMAL")
class Member(
    name: String,
    email: String,
    password: String,
    providerId: String = ""
) : BaseMember(name = name, email = email, password =  password, role= "ROLE_MEMBER", providerId =  providerId)

@Entity
@DiscriminatorValue("AUTHOR")
class Author(
    name: String,
    email: String,
    password: String,
    providerId: String = ""
) : BaseMember(name = name, email = email, password =  password, role= "ROLE_AUTHOR", providerId =  providerId)


@Entity
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var title: String,
    var content: String,
    var isEpisode: Boolean,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: BaseMember,
    val createdDt: Instant = Instant.now(),
    var updatedDt: Instant = Instant.now()
)

@Entity
class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val content: String,
    @ManyToOne
    @JoinColumn(name = "member_id")
    val author: Author,
    val authorName:String,
    val title:String,
    val publisher: String,
    val isbn: String
)

@Entity
class Episode(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = true)
    val book: Book? = null,
    @OneToOne
    @JoinColumn(name = "post_id")
    val post: Post
)

@Entity
class BookShelf(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @ManyToOne
    @JoinColumn(name = "member_id")
    val member: BaseMember,
    @ManyToOne
    @JoinColumn(name = "book_id")
    val book: Book
)