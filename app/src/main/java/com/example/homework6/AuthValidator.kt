import android.util.Patterns //Проверит шаблон на email

// object означает, что нам не нужно будет создавать экземпляр этого класса
object AuthValidator {

    fun validateEmail(email: String): String? {
        val trimmedEmail = email.trim()
        //TODO: Проверки временно опущены
//        return when {
//            trimmedEmail.isEmpty() -> "Поле не может быть пустым"
//            !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() -> "Некорректный формат email"
//            else -> null // Ошибки нет
//        }
        return null
    }


    fun validatePassword(password: String): String? {

        //TODO: Проверки временно опущены
//        return when {
//            password.isEmpty() -> "Поле не может быть пустым"
//            password.length < 8 -> "Пароль должен содержать не менее 8 символов"
//            else -> null // Ошибки нет
//        }
//    }
        return null
    }
}