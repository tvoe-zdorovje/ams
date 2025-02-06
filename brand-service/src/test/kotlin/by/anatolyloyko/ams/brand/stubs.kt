package by.anatolyloyko.ams.brand

import by.anatolyloyko.ams.brand.model.Brand

const val BRAND_ID = 1L

val NEW_BRAND = Brand(
    name = "Lidskoe",
    description = "We brew the best beer around Lida",
)

val BRAND = NEW_BRAND.copy(id = BRAND_ID)
