package com.baoge.pageplugin;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.PropertyException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Properties;

@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
)})
public class PagePlugin implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(PagePlugin.class);
    private static String dialect = "";
    private static String pageSqlId = "";

    public PagePlugin() {
    }

    public Object intercept(Invocation ivk) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, SQLException, InvocationTargetException {
        log.info("=====================:execute-query");
        MappedStatement mappedStatement = (MappedStatement) ivk.getArgs()[0];
        Object parameterObject = ivk.getArgs()[1];
        if (mappedStatement != null && mappedStatement.getId().matches(pageSqlId)) {
            BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
            String sql = boundSql.getSql();
            Page page = null;
            if (parameterObject instanceof Page) {
                page = (Page) parameterObject;
                page.setEntityOrField(true);
            } else {
                Field pageField = ReflectUtil.getFieldByFieldName(parameterObject, "page");
                if (pageField == null) {
                    throw new NoSuchFieldException(parameterObject.getClass().getName() + "不存在 page 属性！");
                }

                page = (Page) ReflectUtil.getValueByFieldName(parameterObject, "page");
                if (page == null) {
                    page = new Page();
                }

                page.setEntityOrField(false);
                ReflectUtil.setValueByFieldName(parameterObject, "page", page);
            }

            String pageSql = this.generatePageSql(sql, page);
            ReflectUtil.setValueByFieldName(boundSql, "sql", pageSql);
            this.resetSql2Invocation(ivk, pageSql);
        }

        return ivk.proceed();
    }

    private String generatePageSql(String sql, Page page) {
        if (page != null && StringUtils.isNotEmpty(dialect)) {
            StringBuffer pageSql = new StringBuffer();
            if ("mysql".equals(dialect)) {
                pageSql.append(sql);
                pageSql.append(" limit " + page.getCurrentResult() + "," + page.getShowCount());
            } else if ("oracle".equals(dialect)) {
                int count = page.getCurrentPage() * page.getShowCount();
                pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
                pageSql.append(sql);
                pageSql.append(") tmp_tb where ROWNUM<=");
                pageSql.append(count);
                pageSql.append(") where row_id>");
                pageSql.append(count - page.getShowCount());
            }

            return pageSql.toString();
        } else {
            return sql;
        }
    }

    private void resetSql2Invocation(Invocation invocation, String sql) throws SQLException {
        Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = statement.getBoundSql(parameterObject);
        MappedStatement newStatement = this.newMappedStatement(statement, new BoundSqlSqlSource(boundSql));
        MetaObject msObject = MetaObject.forObject(newStatement, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
        msObject.setValue("sqlSource.boundSql.sql", sql);
        args[0] = newStatement;
    }

    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            String[] var5 = ms.getKeyProperties();
            int var6 = var5.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                String keyProperty = var5[var7];
                keyProperties.append(keyProperty).append(",");
            }

            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }

        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    public void setProperties(Properties p) {
        dialect = p.getProperty("dialect");
        if (StringUtils.isEmpty(dialect)) {
            try {
                throw new PropertyException("dialect property is not found!");
            } catch (PropertyException var4) {
                log.error(var4.getMessage());
            }
        }

        pageSqlId = p.getProperty("pageSqlId");
        if (StringUtils.isEmpty(pageSqlId)) {
            try {
                throw new PropertyException("pageSqlId property is not found!");
            } catch (PropertyException var3) {
                log.error(var3.getMessage());
            }
        }

    }

    class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return this.boundSql;
        }
    }
}
