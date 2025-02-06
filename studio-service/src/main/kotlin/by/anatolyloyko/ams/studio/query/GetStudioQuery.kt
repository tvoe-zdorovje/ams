package by.anatolyloyko.ams.studio.query

import by.anatolyloyko.ams.infrastructure.service.query.BaseQuery
import by.anatolyloyko.ams.studio.model.Studio

/**
 * A query to retrieve a studio {@link Studio} by ID {@link Long}.
 *
 * @param input the ID of the studio to retrieve.
 */
class GetStudioQuery(
    input: Long
) : BaseQuery<Long, Studio?>(input)
