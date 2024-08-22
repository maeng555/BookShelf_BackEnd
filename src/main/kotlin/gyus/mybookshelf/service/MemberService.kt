package gyus.mybookshelf.service

import gyus.mybookshelf.controller.UpdateMemberRequest
import gyus.mybookshelf.model.Member
import gyus.mybookshelf.model.dto.MemberDTO
import gyus.mybookshelf.repository.EpisodeRepository
import gyus.mybookshelf.repository.MemberRepository
import gyus.mybookshelf.repository.PostRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository,
    private val episodeRepository: EpisodeRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun createMember(memberDTO: MemberDTO): MemberDTO {
        val member = Member(name = memberDTO.name, email = memberDTO.email, password=passwordEncoder.encode(memberDTO.password))
        memberRepository.save(member)
        return MemberDTO.toDto(member)
    }

    fun memberList(pagable: Pageable): Page<MemberDTO> {
        return memberRepository.findAll(pagable).map {
            MemberDTO.toDto(it)
        }
    }

    fun getMember(id: Long): MemberDTO {
        val member = memberRepository.findById(id).orElseThrow {
            throw IllegalArgumentException("해당 멤버가 없습니다.")
        }
        return MemberDTO.toDto(member)
    }

    @Transactional
    fun updateMember(updateMemberRequest: UpdateMemberRequest): MemberDTO {
        val member = memberRepository.findById(updateMemberRequest.id).orElseThrow {
            throw IllegalArgumentException("해당 멤버가 없습니다.")
        }
        member.email = updateMemberRequest.email
        return MemberDTO.toDto(member)
    }

    @Transactional
    fun deleteMember(id: Long) {
        val member = memberRepository.findById(id).orElseThrow {
            throw IllegalArgumentException("해당 멤버가 없습니다.")
        }
        // 멤버가 작성한 포스트를 찾아서 모두 삭제
        postRepository.findByMember(member).forEach {post ->
            if (post.isEpisode) {
                episodeRepository.findByPost(post).let {
                    if (it != null) {
                        episodeRepository.deleteById(it.id)
                    }
                }
            }
            postRepository.deleteById(post.id)
        }
        memberRepository.deleteById(id)
    }
}