+++
pre = "<b>5.2. </b>"
title = "SQL 解析"
weight = 2
chapter = true
+++

## DatabaseTypedSQLParserFacade

### 全限定类名

[`org.apache.shardingsphere.sql.parser.spi.DialectSQLParserFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/spi/src/main/java/org/apache/shardingsphere/sql/parser/spi/DialectSQLParserFacade.java)

### 定义

配置用于 SQL 解析的词法分析器和语法分析器入口

### 已知实现

| *配置标识*      | *详细说明*                    | *全限定类名*                                                                                                                                                                                                                                                                |
|-------------|---------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| MySQL       | 基于 MySQL 的 SQL 解析器入口      | [`org.apache.shardingsphere.sql.parser.mysql.parser.MySQLParserFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/dialect/mysql/src/main/java/org/apache/shardingsphere/sql/parser/mysql/parser/MySQLParserFacade.java)                          |
| PostgreSQL  | 基于 PostgreSQL 的 SQL 解析器入口 | [`org.apache.shardingsphere.sql.parser.postgresql.parser.PostgreSQLParserFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/dialect/postgresql/src/main/java/org/apache/shardingsphere/sql/parser/postgresql/parser/PostgreSQLParserFacade.java) |
| openGauss   | 基于 openGauss 的 SQL 解析器入口  | [`org.apache.shardingsphere.sql.parser.opengauss.parser.OpenGaussParserFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/dialect/opengauss/src/main/java/org/apache/shardingsphere/sql/parser/opengauss/parser/OpenGaussParserFacade.java)      |
| Oracle      | 基于 Oracle 的 SQL 解析器入口     | [`org.apache.shardingsphere.sql.parser.oracle.parser.OracleParserFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/dialect/oracle/src/main/java/org/apache/shardingsphere/sql/parser/oracle/parser/OracleParserFacade.java)                     |
| SQLServer   | 基于 SQLServer 的 SQL 解析器入口  | [`org.apache.shardingsphere.sql.parser.sqlserver.parser.SQLServerParserFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/dialect/sqlserver/src/main/java/org/apache/shardingsphere/sql/parser/sqlserver/parser/SQLServerParserFacade.java)      |
| ClickHouse  | 基于 ClickHouse 的 SQL 解析器入口  | [`org.apache.shardingsphere.sql.parser.clickhouse.parser.ClickHouseParserFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/dialect/clickhouse/src/main/java/org/apache/shardingsphere/sql/parser/clickhouse/parser/ClickHouseParserFacade.java) |
| Doris       | 基于 Doris 的 SQL 解析器入口  | [`org.apache.shardingsphere.sql.parser.doris.parser.DorisParserFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/dialect/doris/src/main/java/org/apache/shardingsphere/sql/parser/doris/parser/DorisParserFacade.java)                          |
| Hive        | 基于 Hive 的 SQL 解析器入口  | [`org.apache.shardingsphere.sql.parser.hive.parser.HiveParserFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/dialect/hive/src/main/java/org/apache/shardingsphere/sql/parser/hive/parser/HiveParserFacade.java)                               |
| Presto      | 基于 Presto 的 SQL 解析器入口  | [`org.apache.shardingsphere.sql.parser.presto.parser.PrestoParserFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/dialect/presto/src/main/java/org/apache/shardingsphere/sql/parser/presto/parser/PrestoParserFacade.java)         |
| SQL92       | 基于 SQL92 的 SQL 解析器入口      | [`org.apache.shardingsphere.sql.parser.sql92.parser.SQL92ParserFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/dialect/sql92/src/main/java/org/apache/shardingsphere/sql/parser/sql92/parser/SQL92ParserFacade.java)                          |


## SQLStatementVisitorFacade

### 全限定类名

[`org.apache.shardingsphere.sql.parser.spi.SQLStatementVisitorFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/spi/src/main/java/org/apache/shardingsphere/sql/parser/spi/SQLStatementVisitorFacade.java)

### 定义

SQL 语法树访问器入口

### 已知实现

| *配置标识*     | *详细说明*                       | *全限定类名*                                                                                                                                                                                                                                                                                                          |
|------------|------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| MySQL      | 基于 MySQL 的 SQL 语法树访问器入口      | [`org.apache.shardingsphere.sql.parser.mysql.visitor.statement.MySQLStatementVisitorFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/dialect/mysql/src/main/java/org/apache/shardingsphere/sql/parser/mysql/visitor/statement/MySQLStatementVisitorFacade.java)                          |
| PostgreSQL | 基于 PostgreSQL 的 SQL 语法树访问器入口 | [`org.apache.shardingsphere.sql.parser.postgresql.visitor.statement.PostgreSQLStatementVisitorFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/dialect/postgresql/src/main/java/org/apache/shardingsphere/sql/parser/postgresql/visitor/statement/PostgreSQLStatementVisitorFacade.java) |
| SQLServer  | 基于 SQLServer 的 SQL 语法树访问器入口  | [`org.apache.shardingsphere.sql.parser.sqlserver.visitor.statement.SQLServerStatementVisitorFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/dialect/sqlserver/src/main/java/org/apache/shardingsphere/sql/parser/sqlserver/visitor/statement/SQLServerStatementVisitorFacade.java)      |
| Oracle     | 基于 Oracle 的 SQL 语法树访问器入口     | [`org.apache.shardingsphere.sql.parser.oracle.visitor.statement.OracleStatementVisitorFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/dialect/oracle/src/main/java/org/apache/shardingsphere/sql/parser/oracle/visitor/statement/OracleStatementVisitorFacade.java)                     |
| SQL92      | 基于 SQL92 的 SQL 语法树访问器入口      | [`org.apache.shardingsphere.sql.parser.sql92.visitor.statement.SQL92StatementVisitorFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/dialect/sql92/src/main/java/org/apache/shardingsphere/sql/parser/sql92/visitor/statement/SQL92StatementVisitorFacade.java)                          |
| openGauss  | 基于 openGauss 的 SQL 语法树访问器入口  | [`org.apache.shardingsphere.sql.parser.opengauss.visitor.statement.OpenGaussStatementVisitorFacade`](https://github.com/apache/shardingsphere/blob/master/parser/sql/dialect/opengauss/src/main/java/org/apache/shardingsphere/sql/parser/opengauss/visitor/statement/OpenGaussStatementVisitorFacade.java)      |
