import android.util.Patterns

// object означает, что нам не нужно будет создавать экземпляр этого класса
object AuthValidator {

    fun validateEmail(email: String): String? {
        val trimmedEmail = email.trim()
        return when {
            trimmedEmail.isEmpty() -> "Введите email"
            !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() -> "Некорректный формат почты"
            else -> null // Ошибок нет
        }
    }


    fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "Введите пароль"
            password.length < 6 -> "Пароль слишком короткий (минимум 6 символов)"
            else -> null
        }
    }
}