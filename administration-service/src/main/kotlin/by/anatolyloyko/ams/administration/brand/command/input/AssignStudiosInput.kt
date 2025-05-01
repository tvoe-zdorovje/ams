package by.anatolyloyko.ams.administration.brand.command.input

data class AssignStudiosInput(
    val brandId: Long,
    val studios: List<Long>
)
