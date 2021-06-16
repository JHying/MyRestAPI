package tw.hyin.mySpringBoot.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author H-yin on 2020.
 */
@NoArgsConstructor
@Transactional
@SuppressWarnings("unchecked")
public class SqlQueryUtil<T> {

    protected Map<String, Object> parameters = new HashMap();

    @Getter
    private Class<T> resultClass;//準備接收查詢結果

    /**
     * 查詢結果 = T (resultClass)
     *
     * @param resultClass 查詢結果 class
     */
    public SqlQueryUtil(Class<T> resultClass) {
        this.resultClass = resultClass;
    }

    /**
     * 檢查variable是否有值
     *
     * @param variable
     * @param sql
     * @return
     */
    private String checkVariableNull(Object variable, String sql) {
        return " " + ((variable != null && !variable.equals("")) ? sql : "") + " ";
    }

    public String addParameter(Object variable, String sql, String parameterKey, Object parameterValue) {
        if (variable != null && !variable.toString().equals("")) {
            this.parameters.put(parameterKey, parameterValue);
        }
        return checkVariableNull(variable, sql);
    }

    public void setParameters(Query query) {

        for (String key : this.parameters.keySet()) {
            query.setParameter(key, this.parameters.get(key));
        }
    }

    public void setParameters(Query query, ResultTransformer resultTransformer) {
        query.setResultTransformer(resultTransformer);

        for (String key : this.parameters.keySet()) {
            query.setParameter(key, this.parameters.get(key));
        }
    }

    public List<T> getResultList(Session session, String sql) {
        Query query = session.createSQLQuery(sql);
        this.setParameters(query, Transformers.aliasToBean(resultClass));
        return query.list();
    }

    public List<T> getResultList(Session session, String sql, Integer max) {
        Query query = session.createSQLQuery(sql).setMaxResults(max);
        this.setParameters(query, Transformers.aliasToBean(resultClass));
        return query.list();
    }

    public T getUniqueList(Session session, String sql) {
        Query query = session.createSQLQuery(sql);
        this.setParameters(query, Transformers.aliasToBean(resultClass));
        return (T) query.uniqueResult();
    }
}
