package Services;

public class QueryBuilder {
    private StringBuilder query;

    public QueryBuilder() {
        query = new StringBuilder();
    }

    public QueryBuilder(String string) {
        query = new StringBuilder(string);
    }


    @Override
    public String toString() {
        return query.toString();
    }
}
