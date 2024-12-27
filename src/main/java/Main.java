import jooq.generated.Tables;
import jooq.generated.tables.pojos.User;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;

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
public class Main {

  public static void main(String[] args) {
    DSLContext dslContext = DSL.using("jdbc:postgresql://localhost:5432/my_db", "test", "test");

    Sort sort = Sort.by(asc("created_on"), asc("status"), desc("name"));
    PageRequest pageable = PageRequest.of(2, 10, sort);

    SelectJoinStep<Record> select = dslContext.select().from(Tables.USER);

    List<User> result =
        JooqPageableUtils.enhanceQuery(Tables.USER, select, pageable).fetchInto(User.class);

    System.out.println(result);
  }
}
