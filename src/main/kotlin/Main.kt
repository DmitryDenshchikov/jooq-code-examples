package denshchikov.dmitry

import jooq.generated.Tables
import jooq.generated.tables.pojos.User
import org.jooq.impl.DSL
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

/*

In the main method you can find an example of JooqPageableUtils usage.

The DB that is used for this app contains only one table.
Below you can see the DDL for the table:

CREATE TABLE "user" (
   id uuid NOT NULL PRIMARY KEY,
   "name" varchar NOT NULL,
   status varchar NOT NULL,
   created_on timestamp NOT NULL
);

 */
fun main() {
    val dslContext = DSL.using("jdbc:postgresql://localhost:5432/my_db", "test", "test")

    val sort = Sort.by(
        Sort.Order.asc("created_on"),
        Sort.Order.asc("status"),
        Sort.Order.desc("name")
    )

    val pageable = PageRequest.of(2, 10, sort);

    val select = dslContext.select()
        .from(Tables.USER)

    val result = JooqPageableUtils.enhanceQuery(Tables.USER, select, pageable)
        .fetchInto(User::class.java)

    println(result)
}