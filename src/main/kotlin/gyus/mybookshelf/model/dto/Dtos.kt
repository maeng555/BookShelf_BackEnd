package gyus.mybookshelf.model.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import gyus.mybookshelf.model.Author
import gyus.mybookshelf.model.BaseMember
import gyus.mybookshelf.model.Member
import gyus.mybookshelf.model.Post
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

class PostDTO(
    val id: Long? = 0,
    val title: String? = null,
    val content: String? = null,
    val isEpisode: Boolean = false,
    val memberName: String? = null,
    val createdDt: Instant = Instant.now(),
    var updatedDt: Instant = Instant.now()
) {
    constructor(
        id: Long,
        title: String,
        content: String,
        isEpisode: Boolean,
        member: BaseMember,
        createdDt: Instant,
        updatedDt: Instant
    ) : this(
        id = id,
        title = title,
        content = content,
        isEpisode = isEpisode,
        memberName = member.name,
        createdDt = createdDt,
        updatedDt = updatedDt
    )

    companion object {
        fun toDto(entity: Post): PostDTO {
            return PostDTO(
                entity.id,
                entity.title,
                entity.content,
                entity.isEpisode,
                entity.member.name,
                entity.createdDt,
                entity.updatedDt
            )
        }
    }
}

@Schema(name = "멤버 DTO", description = "멤버 정보를 담은 DTO")
class MemberDTO(
    @Schema(description = "멤버의 고유 ID")
    val id: Long? = 0,
    @Schema(description = "멤버의 이름")
    val name: String,
    @Schema(description = "멤버의 이메일")
    val email: String,
    @JsonIgnore
    val password: String = "",
    @Schema(description = "멤버의 타입 NORMAL | AUTHOR | UNKNOWN")
    val type: String = "NORMAL"
) {
    companion object {
        fun toDto(entity: BaseMember): MemberDTO {
            val type = when (entity) {
                is Member -> "NORMAL"
                is Author -> "AUTHOR"
                else -> "UNKNOWN"
            }
            return MemberDTO(
                id = entity.id,
                name = entity.name,
                email = entity.email,
                type = type,
                password = entity.password
            )
        }
    }
}