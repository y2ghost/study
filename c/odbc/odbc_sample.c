#include <stdio.h>
#include <sql.h>
#include <sqlext.h>

#define DONE            -1
#define ROWS            20 
#define STATUS_LEN      6
#define OPENDATE_LEN    11

static int res[][3] = {
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_PRIOR, 0, 0},
    {SQL_FETCH_PRIOR, 0, 0},
    {SQL_FETCH_PRIOR, 0, 0},
    {SQL_FETCH_PRIOR, 0, 0},
    {SQL_FETCH_PRIOR, 0, 0},
    {SQL_FETCH_PRIOR, 0, 0},
    {SQL_FETCH_PRIOR, 0, 0},
    {SQL_FETCH_PRIOR, 0, 0},
    {SQL_FETCH_PRIOR, 0, 0},
    {SQL_FETCH_PRIOR, 0, 0},
    {SQL_FETCH_PRIOR, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_PRIOR, 0, 0},
    {SQL_FETCH_PRIOR, 0, 0},
    {SQL_FETCH_PRIOR, 0, 0},
    {SQL_FETCH_PRIOR, 0, 0},
    {SQL_FETCH_FIRST, 0, 0},
    {SQL_FETCH_ABSOLUTE, -100, 0},
    {SQL_FETCH_ABSOLUTE, -400, 0},
    {SQL_FETCH_ABSOLUTE, 200, 0},
    {SQL_FETCH_ABSOLUTE, 350, 0},
    {SQL_FETCH_LAST, 0, 0},
    {SQL_FETCH_NEXT, 0, 0},
    {SQL_FETCH_LAST, 0, 0},
    {SQL_FETCH_FIRST, 0, 0},
    {SQL_FETCH_RELATIVE, 20, 0},
    {SQL_FETCH_RELATIVE, 50, 0},
    {SQL_FETCH_RELATIVE, 101, 0},
    {SQL_FETCH_RELATIVE, -5, 0},
    {SQL_FETCH_RELATIVE, 60, 0},
    {SQL_FETCH_RELATIVE, -60, 0},
    {SQL_FETCH_RELATIVE, 0, 0},
    {SQL_FETCH_RELATIVE, 0, 0},
    {SQL_FETCH_NEXT, 0, -1},
};

static int PromptScroll(SQLUINTEGER *ort, SQLUINTEGER *offset)
{
    int ret = 0;
    static int count = 0;

    *ort = res[count][0];
    *offset = res[count][1];
    ret = res[count][2];
    count++;
    return ret;
}

static int DumpODBCLog(SQLHENV hEnv, SQLHDBC hDbc, SQLHSTMT hStmt)
{
    int rc = 0;
    SQLCHAR szError[501] = {'\0'};
    SQLCHAR szSqlState[10] = {'\0'};
    SQLINTEGER nNativeError = 0;
    SQLSMALLINT nErrorMsg = 0;

    if (NULL != hStmt) {
        while (1) {
            rc = SQLError(hEnv, hDbc, hStmt, szSqlState, &nNativeError,
                szError, 500, &nErrorMsg);
            if (SQL_SUCCESS != rc) {
                break;
            }
            
            printf("%s\n", szError);
        }
    }

    if (NULL != hDbc) {
        while (1) {
            rc = SQLError(hEnv, hDbc, 0, szSqlState, &nNativeError,
                szError, 500, &nErrorMsg);
            if (SQL_SUCCESS != rc) {
                break;
            }
            
            printf("%s\n", szError);
        }
    }

    if (NULL != hEnv) {
        while (1) {
            rc = SQLError(hEnv, 0, 0, szSqlState, &nNativeError,
                szError, 500, &nErrorMsg);
            if (SQL_SUCCESS != rc) {
                break;
            }
            
            printf("%s\n", szError);
        }
    }

    return 1;
}

static int Display(SQLUSMALLINT *rsa, SQLUINTEGER crow,
    SQLSMALLINT *sOrderID, SQLLEN *cbOrderID,
    SQLCHAR szOrderDate[][OPENDATE_LEN], SQLLEN *cbOrderDate,
    SQLCHAR szStatus[][STATUS_LEN], SQLLEN *cbStatus)
{
    int i = 0;

    printf("crow = %d\n", crow);
    for (i=0; i<crow; ++i) {
        printf("%d %d |%d:%ld|%s:%ld|%s:%ld\n", i,
            rsa[i], sOrderID[i], cbOrderID[i],
            szOrderDate[i], cbOrderDate[i],
            szStatus[i], cbStatus[i]);
    }

    return 0;
}

static void create_file(SQLHANDLE hstmt)
{
    int i = 0;
    SQLRETURN ret = 0;
    SQLCHAR *drop_sql = (SQLCHAR*)"drop table ctest";
    SQLCHAR *create_sql = (SQLCHAR*)"create table ctest ("
        "id integer, dt character(10), status character(5),"
        "other character varying(40))";
    
    ret = SQLExecDirect(hstmt, drop_sql, SQL_NTS);
    ret = SQLExecDirect(hstmt, create_sql, SQL_NTS);

    for (i=1; i<1000; ++i) {
        SQLCHAR sql[256] = {'\0'};

        snprintf((char*)sql, sizeof(sql),
            "insert into ctest values(%d, '%10d', '%05d', 'other line %d')",
            i, i, i, i);
        ret = SQLExecDirect(hstmt, sql, SQL_NTS);
        printf("%s - %d\n", sql, ret);
   }
}

static void cursor_test()
{
    SQLHENV henv = NULL;
    SQLHDBC hdbc = NULL;
    SQLHSTMT hstmt1 = NULL;
    SQLHSTMT hstmt2 = NULL;
    SQLRETURN retcode = 0;
    SQLCHAR szStatus[ROWS][STATUS_LEN] = {{'\0'}};
    SQLCHAR szOpenDate[ROWS][OPENDATE_LEN] = {{'\0'}};
    SQLSMALLINT sOrderID[ROWS] = {0};
    SQLLEN cbStatus[ROWS] = {0};
    SQLLEN cbOrderID[ROWS] = {0};
    SQLLEN cbOpenDate[ROWS] = {0};
    SQLUINTEGER FetchOrientation = 0;
    SQLUINTEGER crow = 0;
    SQLUINTEGER FetchOffset = 0;
    SQLUSMALLINT RowStatusArray[ROWS] = {0};

    SQLAllocHandle(SQL_HANDLE_ENV, SQL_NULL_HANDLE, &henv);
    SQLSetEnvAttr(henv, SQL_ATTR_ODBC_VERSION, (SQLPOINTER)SQL_OV_ODBC3, 0);
    SQLAllocHandle(SQL_HANDLE_DBC, henv, &hdbc);

    /* Specify the ODBC Cursor Library is always used then connect. */
    SQLSetConnectAttr(hdbc, SQL_ATTR_ODBC_CURSORS, (SQLPOINTER)SQL_CUR_USE_ODBC, 0);
    retcode = SQLConnect(hdbc, (SQLCHAR*)"yydb", SQL_NTS,
        (SQLCHAR*)"", SQL_NTS,
        (SQLCHAR*)"", SQL_NTS);
    DumpODBCLog(NULL, hdbc, NULL);

    if (SQL_SUCCESS==retcode || SQL_SUCCESS_WITH_INFO==retcode) {
        /* Allocate a statement handle for the result set and a statement */
        /* handle for a positioned update statement                       */
        SQLAllocHandle(SQL_HANDLE_STMT, hdbc, &hstmt1);
        SQLAllocHandle(SQL_HANDLE_STMT, hdbc, &hstmt2);

        /*
         * create the data
         */
        create_file(hstmt1);
        /* Specify an updateable statis cursor with 20 rows of data. Set */
        /* the cursor name, execute the SELECT statement, and bind the   */
        /* rowset buffer to result set columns in column-wise fashion    */
        SQLSetStmtAttr(hstmt1, SQL_ATTR_CONCURRENCY, (SQLPOINTER)SQL_CONCUR_VALUES, 0);
        SQLSetStmtAttr(hstmt1, SQL_ATTR_CURSOR_TYPE, (SQLPOINTER)SQL_CURSOR_STATIC, 0);
        SQLSetStmtAttr(hstmt1, SQL_ATTR_ROW_ARRAY_SIZE, (SQLPOINTER)ROWS, 20);
        SQLSetStmtAttr(hstmt1, SQL_ATTR_ROW_STATUS_PTR, RowStatusArray, 0);
        SQLSetStmtAttr(hstmt1, SQL_ATTR_ROWS_FETCHED_PTR, &crow, 0);
        SQLSetCursorName(hstmt1, (SQLCHAR*)"ORDERCURSOR", SQL_NTS);
        SQLPrepare(hstmt1, (SQLCHAR*)"select id, dt, status from ctest", SQL_NTS);

        {
            SQLCHAR cname[30] = {'\0'};

            retcode = SQLDescribeCol(hstmt1, 1, cname, sizeof(cname),
                NULL, NULL, NULL, NULL, NULL);
            printf("ret = %d %s\n", retcode, cname);
            DumpODBCLog(NULL, NULL, hstmt1);
        }

        SQLExecute(hstmt1);
        SQLBindCol(hstmt1, 1, SQL_C_SSHORT, sOrderID, 0, cbOrderID);
        SQLBindCol(hstmt1, 2, SQL_C_CHAR, szOpenDate, OPENDATE_LEN, cbOpenDate);
        SQLBindCol(hstmt1, 3, SQL_C_CHAR, szStatus, STATUS_LEN, cbStatus);
        FetchOrientation = SQL_FETCH_FIRST;
        FetchOffset = 0;

        do {
            int ret = 0;
            SQLLEN count = 0;

            printf("fetch %d %d\n", FetchOrientation, FetchOffset);
            ret = SQLFetchScroll(hstmt1, FetchOrientation, FetchOffset);
            SQLRowCount(hstmt1, &count);
            printf("ret = %d count = %ld\n", ret, count);
            Display(RowStatusArray, crow, sOrderID, cbOrderID,
                szOpenDate, cbOpenDate, szStatus, cbStatus);

            if (SQL_SUCCEEDED(ret)) {
                SQLLEN len = 0;
                char txt[50] = {'\0'};

                ret = SQLSetPos(hstmt1, 5, SQL_POSITION, SQL_LOCK_NO_CHANGE);
                ret = SQLGetData(hstmt1, 2, SQL_C_CHAR, txt, sizeof(txt), &len);
                printf("ret = %d %s:%ld\n", ret, txt, len);
            }

            retcode = PromptScroll(&FetchOrientation, &FetchOffset);
        } while (DONE != retcode);

        SQLCloseCursor(hstmt1);
        SQLFreeStmt(hstmt1, SQL_DROP);
        SQLFreeStmt(hstmt2, SQL_DROP);
        SQLDisconnect(hdbc);
        SQLFreeHandle(SQL_HANDLE_DBC, hdbc);
    }

    SQLFreeHandle(SQL_HANDLE_ENV, henv);
}

int main(void)
{
    cursor_test();
    return 0;
}
