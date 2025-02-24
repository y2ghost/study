#include <zdb.h>
#include <stdio.h>

int main(int ac, char **av)
{
    URL_T url = URL_new("mysql://localhost/test?user=root&password=");
    ConnectionPool_T pool = ConnectionPool_new(url);
    
    ac = ac;
    av = av;
    ConnectionPool_start(pool);
    Connection_T conn = ConnectionPool_getConnection(pool);

    TRY {
        int i = 0;

        Connection_execute(conn, "create table bleach(name varchar(255), created_at timestamp)");
        PreparedStatement_T p = Connection_prepareStatement(conn, "insert into bleach values (?,?)");
        const char *bleach[] = {"as", "df", "gh", "jk", NULL};

        for (i=0; NULL!=bleach[i]; ++i) {
            PreparedStatement_setString(p, 1, bleach[i]);
            PreparedStatement_setTimestamp(p, 2, time(NULL));
            PreparedStatement_execute(p);
        }
        
        ResultSet_T r = Connection_executeQuery(conn, "select name, created_at from bleach");

        while (ResultSet_next(r)) {
            printf("%-22s\t %s\n", ResultSet_getString(r, 1), ResultSet_getString(r, 2));
        }

        Connection_execute(conn, "drop table bleach");
    } CATCH(SQLException) {
        printf("SQLException -- %s\n", Exception_frame.message);
    } FINALLY {
        Connection_close(conn);
    }
    END_TRY;

    ConnectionPool_free(&pool);
    URL_free(&url);
    return 0;
}
