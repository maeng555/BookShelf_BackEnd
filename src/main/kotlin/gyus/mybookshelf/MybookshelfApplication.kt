package gyus.mybookshelf

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MybookshelfApplication

fun main(args: Array<String>) {
    runApplication<MybookshelfApplication>(*args)
}
