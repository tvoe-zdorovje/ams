package by.anatolyloyko.ams.auth.kafka

import by.anatolyloyko.ams.auth.IDP_USER_ID
import by.anatolyloyko.ams.auth.USER_ID

val CODE_TO_TOKEN_KEYCLOAK_EVENT_PAYLOAD = """
    {
      "id": "49a84070-5eb4-449d-9dcc-dbf62567294f",
      "time": 1787044489271,
      "type": "CODE_TO_TOKEN",
      "userId": "$IDP_USER_ID",
      "error": null,
      "truncated": "...",
      "details": {
        "truncated": "..."
      }
    }
    """.trimIndent()

val REFRESH_TOKEN_KEYCLOAK_EVENT_PAYLOAD = """
    {
      "id": "281d4804-df02-41be-8af3-fa9ad16d61ba",
      "time": 1787044647368,
      "type": "REFRESH_TOKEN",
      "userId": "$IDP_USER_ID",
      "error": null,
      "truncated": "...",
      "details": {
        "truncated": "..."
      }
    }
    """.trimIndent()

val UPDATE_ROLES_ADMINISTRATION_USER_EVENT_PAYLOAD = """
    {
      "type": "UPDATE_ROLES",
      "userId": "$USER_ID",
      "truncated": "..."
    }
""".trimIndent()
