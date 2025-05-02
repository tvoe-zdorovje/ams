package by.anatolyloyko.ams.studio

import by.anatolyloyko.ams.studio.model.Studio

const val STUDIO_ID = 1000000000171342100

val NEW_STUDIO = Studio(
    name = "Beauty Studio",
    description = "Will make you a bit better",
)

val STUDIO = NEW_STUDIO.copy(id = STUDIO_ID)
