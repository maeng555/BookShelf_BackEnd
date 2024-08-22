package gyus.mybookshelf.validator

import gyus.mybookshelf.controller.CreateMemberRequest
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Component
class MemberValidator: Validator {
    override fun supports(clazz: Class<*>): Boolean {
        return CreateMemberRequest::class.java == clazz
    }

    override fun validate(target: Any, errors: Errors) {
        val createMemberRequest = target as CreateMemberRequest
        if (createMemberRequest.name.length > 20) {
            errors.rejectValue("name","MEMBER_NAME_LENGTH_ERROR", "이름의 길이는 20자 이하여야 합니다.")
        }
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,6}\\\$")
        if (createMemberRequest.email == "" || !emailRegex.matches(createMemberRequest.email)) {
            errors.rejectValue("email", "EMAIL_FORMAT_ERROR", "이메일 정보가 올바르지 않습니다.")
        }
    }
}