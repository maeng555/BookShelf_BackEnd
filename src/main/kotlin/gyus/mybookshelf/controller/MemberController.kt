package gyus.mybookshelf.controller

import gyus.mybookshelf.MyBookShelfException
import gyus.mybookshelf.MyErrorResponse
import gyus.mybookshelf.model.dto.MemberDTO
import gyus.mybookshelf.service.MemberService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

interface OnCreateError

data class CreateMemberRequest (
    @get:Size(min=1, max=20, message = "이름은 1자 이상 20자 이하 이여야 합니다.", groups = [OnCreateError::class])
    val name:String,
    @get:Email(message = "이메일 포맷이 맞지 않습니다.")
    val email:String
) {
    fun toMemberDTO(): MemberDTO {
        return MemberDTO(name = name, email = email)
    }
}

data class UpdateMemberRequest(
    val id: Long,
    val email:String
)

@Tag(name="멤버", description = "멤버 API")
@RequestMapping("/members")
@RestController
class MemberController(
    private val memberService: MemberService,
) {

    @PostMapping
    @Operation(summary = "멤버 생성", description = "createMemberRequest객체에 있는 name, email을 사용해서 멤버를 생성한다.")
    @ApiResponses(
        ApiResponse(
            responseCode = "201",
            description = "멤버 생성 성공",
            content = [Content(schema= Schema(implementation = MemberDTO::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = [Content(schema= Schema(implementation = MyErrorResponse::class))]
        ),
    )
    @Parameter(
        name= "createMemberRequest",
        description = "멤버 생성 요청 객체",
        content = [Content(schema= Schema(implementation = CreateMemberRequest::class))]
    )
    fun createMember(@Validated(OnCreateError::class) @RequestBody createMemberRequest:CreateMemberRequest, bindingResult: BindingResult): MemberDTO {
        if (bindingResult.hasErrors()) {
            val errorMessage = bindingResult.allErrors.joinToString { it.defaultMessage ?: "검증 에러" }
            throw MyBookShelfException(errorMessage)
        }
        return memberService.createMember(createMemberRequest.toMemberDTO())
    }

    @GetMapping
    fun memberList(@RequestParam page:Int = 1, @RequestParam  size:Int = 10): Page<MemberDTO> {
        val pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"))
        return memberService.memberList(pageable)
    }

    @GetMapping("/{id}")
    fun getMember(@PathVariable id:Long): MemberDTO {
        return memberService.getMember(id)
    }

    @PutMapping("/{id}")
    fun updateMember(@PathVariable id:Long, @RequestParam email:String): MemberDTO {
        val updateMemberRequest = UpdateMemberRequest(id, email)
        return memberService.updateMember(updateMemberRequest)
    }

    @DeleteMapping("/{id}")
    fun deleteMember(@PathVariable id:Long): String {
        memberService.deleteMember(id)
        return "OK"
    }
}