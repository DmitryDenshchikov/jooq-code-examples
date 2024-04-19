package denshchikov.dmitry

import org.jooq.OrderField
import org.jooq.SelectOrderByStep
import org.jooq.SortOrder
import org.jooq.Table
import org.springframework.data.domain.Pageable

/**
 * Utility class for enhancing jOOQ queries with pagination support.
 */
object JooqPageableUtils {

    /**
     * Enhances the provided jOOQ [select] query with pagination parameters from the [pageable] object.
     *
     * @param select the jOOQ SELECT query to enhance.
     * @param pageable the pagination parameters.
     * @return the enhanced jOOQ SELECT query.
     */
    fun enhanceQuery(table: Table<*>, select: SelectOrderByStep<*>, pageable: Pageable) =
        select.orderBy(orderByClause(pageable, table))
            .offset(pageable.offset)
            .limit(pageable.pageSize)

    /**
     * Constructs an ORDER BY clause for the given [pageable] object and table.
     *
     * @param pageable the pagination parameters.
     * @param table the jOOQ Table object to sort by.
     * @return a list of jOOQ OrderField objects representing the ORDER BY clause.
     * @throws IllegalArgumentException if the sort property is unknown.
     */
    fun orderByClause(pageable: Pageable, table: Table<*>): List<OrderField<*>> {

        return pageable.sort.map {
            val field = table.field(it.property)
                ?: throw IllegalArgumentException("Unknown sort property ${it.property}")
            val order = if (it.isDescending) SortOrder.DESC else SortOrder.ASC
            field.sort(order)
        }.toList()
    }

}