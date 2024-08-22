package gyus.mybookshelf.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import gyus.mybookshelf.model.BaseMember
import gyus.mybookshelf.model.Post
import gyus.mybookshelf.model.dto.PostDTO
import gyus.mybookshelf.model.QPost
import jakarta.persistence.QueryHint
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

interface PostRepository : JpaRepository<Post, Long>, PostRepositoryCustom {
    fun findByMember(member: BaseMember): List<Post>


    @EntityGraph(attributePaths = ["member"])
    override fun findAll(): List<Post>

    @QueryHints(value = [QueryHint(name = "org.hibernate.readOnly", value = "true")])
    @Query("SELECT p FROM Post p JOIN FETCH p.member")
    fun findAllWithMember(): List<Post>

}

interface PostRepositoryCustom {
    fun findPostsWithTitleLike(title: String): List<Post>
    fun findPostsSelectTitle(): List<PostDTO>
    fun findPostPagingWithSort(pageable: Pageable): List<PostDTO>
    fun findPostsByCondition(condition: String, value: String, pageable: Pageable): List<PostDTO>
}

class PostRepositoryImpl : PostRepositoryCustom, QuerydslRepositorySupport(Post::class.java) {
    private val post = QPost.post

    override fun findPostsWithTitleLike(title: String): List<Post> {
        return from(post)
            .where(post.title.contains(title))
            .fetch()
    }

    override fun findPostsSelectTitle(): List<PostDTO> {
        return from(post)
            .select(
                Projections.constructor(
                    PostDTO::class.java,
                    post.id,
                    post.title,
                    post.content,
                    post.isEpisode,
                    post.member,
                    post.createdDt,
                    post.updatedDt
                )
            )
            .fetch()
    }

    override fun findPostPagingWithSort(pageable: Pageable): List<PostDTO> {
        return from(post)
            .select(
                Projections.constructor(
                    PostDTO::class.java,
                    post.id,
                    post.title,
                    post.content,
                    post.isEpisode,
                    post.member,
                    post.createdDt,
                    post.updatedDt
                )
            )
            .orderBy(post.createdDt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
    }

    override fun findPostsByCondition(
        condition: String,
        value: String,
        pageable: Pageable
    ): List<PostDTO> {
        val builder = BooleanBuilder()

        when (condition) {
            "id" -> builder.and(post.id.eq(value.toLong()))
            "title" -> builder.and(post.title.contains(value))
        }

        return from(post)
            .select(
                Projections.constructor(
                    PostDTO::class.java,
                    post.id,
                    post.title,
                    post.content,
                    post.isEpisode,
                    post.member,
                    post.createdDt,
                    post.updatedDt
                )
            )
            .where(builder)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(getOrderBy(pageable.sort))
            .fetch()
    }

    private fun getOrderBy(sort: Sort): com.querydsl.core.types.OrderSpecifier<*> {
        val order = sort.iterator().next()
        val path = Expressions.stringPath(order.property)

        return if (order.isAscending) {
            path.asc()
        } else {
            path.desc()
        }
    }

}
