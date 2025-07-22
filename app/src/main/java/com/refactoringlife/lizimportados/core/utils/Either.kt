sealed class Either<out L, out R> {
    data class Success<out L>(val value: L) : Either<L, Nothing>()
    data class Error<out R>(val value: R) : Either<Nothing, R>()
} 