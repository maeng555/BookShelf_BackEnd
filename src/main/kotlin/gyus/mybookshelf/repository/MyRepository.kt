package gyus.mybookshelf.repository

import gyus.mybookshelf.model.*
import jakarta.persistence.QueryHint
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints

interface MemberRepository : JpaRepository<BaseMember, Long> {
    fun findByName(name: String): BaseMember?

    @Query("SELECT m FROM BaseMember m")
    fun findAllMembers(pageable: Pageable): Page<BaseMember>

    fun findByEmail(email: String): BaseMember?

    @Query(value = "SELECT * FROM member WHERE email LIKE %:email%", nativeQuery = true)
    fun findByEmailContaining(email: String): List<BaseMember>

    // distinct 의 경우는 @Query를 사용하는게 좋다.
    @Query("SELECT COUNT(DISTINCT m.name) FROM BaseMember m WHERE m.name = :name")
    fun countDistinctByName(name: String): Int

    fun countByEmail(email: String): Int

    override fun findAll(pagable: Pageable): Page<BaseMember>

    @Query("select m from BaseMember m where m.id = :id and m.role = 'ROLE_AUTHOR'")
    fun findAuthorById(id: Long): Author?
}



interface EpisodeRepository : JpaRepository<Episode, Long> {
    fun findByPost(post: Post): Episode?
}


