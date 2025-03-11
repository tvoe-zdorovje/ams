package by.anatolyloyko.ams.brand.query

import by.anatolyloyko.ams.brand.model.Brand
import by.anatolyloyko.ams.common.infrastructure.service.query.BaseQuery

/**
 * A query to retrieve a brand {@link Brand} by ID {@link Long}.
 *
 * @param input the ID of the brand to retrieve.
 */
class GetBrandQuery(
    input: Long
) : BaseQuery<Long, Brand?>(input)
