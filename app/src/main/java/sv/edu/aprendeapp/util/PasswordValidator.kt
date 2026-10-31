package sv.edu.aprendeapp.util

import sv.edu.aprendeapp.R

object PasswordValidator {
    private const val ESPECIALES = "!@#$%^&*"

    fun errores(p: String): List<Int> {
        val e = mutableListOf<Int>()
        if (p.length < 12) e += R.string.pw_min
        if (p.none { it.isUpperCase() }) e += R.string.pw_mayus
        if (p.none { it.isLowerCase() }) e += R.string.pw_minus
        if (p.none { it.isDigit() }) e += R.string.pw_num
        if (p.none { it in ESPECIALES }) e += R.string.pw_esp
        return e
    }
}
