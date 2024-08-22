package gyus.mybookshelf

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

enum class MyErrorCode(val statusCode:Int, val message:String) {
    SERVER_ERROR(99999, "서버에러")
}

data class MyErrorResponse(
    val statusCode: MyErrorCode,
    val errorMessage: String,
)

class MyBookShelfException(message:String): RuntimeException(message)

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(MyBookShelfException::class)
    fun handleMyBookShelfException(ex: MyBookShelfException): ResponseEntity<MyErrorResponse> {
        val myErrorResponse = MyErrorResponse(statusCode = MyErrorCode.SERVER_ERROR, errorMessage =  ex.message ?: "알 수 없는 에러")
        return ResponseEntity.ok().body(myErrorResponse)
    }
}