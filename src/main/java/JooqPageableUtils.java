import org.jooq.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Utility class providing methods to modify jOOQ queries to include ordering, offset, and limit
 * based on the provided {@link Pageable} parameter.
 */
public final class JooqPageableUtils {

  private JooqPageableUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  /**
   * Enhances a jOOQ query with pagination and sorting based on a {@link Pageable} parameter.
   *
   * <p>This method modifies the given jOOQ query to:
   *
   * <ul>
   *   <li>Apply sorting using the {@link Sort} specification in the {@link Pageable}.
   *   <li>Apply offset and limit for pagination.
   * </ul>
   *
   * @param table The jOOQ {@link Table} representing the table being queried.
   * @param select The jOOQ {@link SelectOrderByStep} query to enhance.
   * @param pageable The {@link Pageable} object containing pagination and sorting information.
   * @return The enhanced jOOQ query with sorting, offset, and limit applied.
   */
  public static SelectLimitPercentAfterOffsetStep<?> enhanceQuery(
      Table<?> table, SelectOrderByStep<?> select, Pageable pageable) {
    return select
        .orderBy(orderByClause(table, pageable))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());
  }

  /**
   * Creates an ORDER BY clause for the jOOQ query based on the sorting information from {@link
   * Pageable}.
   *
   * @param table The jOOQ {@link Table} to derive fields from.
   * @param pageable The {@link Pageable} object containing sorting information.
   * @return A list of jOOQ {@link Field} objects with the specified sort directions.
   */
  private static List<? extends Field<?>> orderByClause(Table<?> table, Pageable pageable) {
    return pageable.getSort().stream().map(sortOrder -> extractField(table, sortOrder)).toList();
  }

  /**
   * Extracts a jOOQ {@link Field} from the given {@link Table} based on the sorting property and
   * applies the sort direction.
   *
   * <p>This method validates the existence of the field corresponding to the sort property in the
   * {@link Table}. If the field does not exist, an {@link IllegalArgumentException} is thrown.
   *
   * @param table The jOOQ {@link Table} to derive the field from.
   * @param sortOrder The {@link Sort.Order} specifying the property and direction for sorting.
   * @return The jOOQ {@link Field} with the specified sort direction applied.
   * @throws IllegalArgumentException if the sort property does not exist in the {@link Table}.
   */
  private static Field<?> extractField(Table<?> table, Sort.Order sortOrder) {
    Field<?> field = table.field(sortOrder.getProperty());

    if (field == null) {
      throw new IllegalArgumentException("Unknown sort property: " + sortOrder.getProperty());
    }

    SortOrder order = sortOrder.isAscending() ? SortOrder.ASC : SortOrder.DESC;

    field.sort(order);
    return field;
  }
}
