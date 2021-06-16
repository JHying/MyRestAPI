package tw.hyin.mySpringBoot.utils;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author H-yin on 2020.
 */
@Setter
@Getter
@SuppressWarnings("unchecked")
public class QueryUtil<T> {

    private Class<T> entity;//準備接收查詢主表
    private Class<?> resultClass;//準備接收結果類別 (查詢結果)
    private Map<String, Object> criteriaMap;
    private CriteriaBuilder cb;
    private CriteriaQuery<?> criteriaQuery;
    private Root<T> root;
    private Set<Predicate> predicateSet = new HashSet<>();//條件之集合
    private List<Selection<?>> colList = new ArrayList<>();//查詢欄位之集合

    /**
     * 預設為 cb.createQuery(entity) = select * from T(entity)
     * <br>查詢結果 = T(entity)
     *
     * @param session hibernate session
     * @param entity  查詢主表 entity class
     */
    public QueryUtil(Session session, Class<T> entity) {
        //預設為 cb.createQuery(entity) = select * from T(entity)
        //查詢結果 = T(entity)
        this.entity = entity;
        this.cb = session.getCriteriaBuilder();
        this.criteriaQuery = cb.createQuery(entity);
        this.root = criteriaQuery.from(entity);
    }

    /**
     * 客製 criteriaQuery -> 查詢結果直接 binding 成 pojo
     * <br>需設定查詢的欄位 colList，且 pojo 要有對應此查詢欄位的建構子
     *
     * @param session     hibernate session
     * @param entity      查詢主表 entity class
     * @param resultClass 查詢結果 result class
     */
    public QueryUtil(Session session, Class<T> entity, Class<?> resultClass) {
        this.entity = entity;
        this.resultClass = resultClass;
        this.cb = session.getCriteriaBuilder();
        this.criteriaQuery = cb.createQuery(resultClass);
        this.root = criteriaQuery.from(entity);
    }

    /**
     * 客製 criteriaQuery -> 查詢結果直接 binding 成 pojo
     * <br>需設定查詢的欄位 colList，且 pojo 要有對應此查詢欄位的建構子
     * <br>加入查詢條件的 map (map 的 key 需與 entity 的 column property 相同)
     * <br>此 utils 中各種 ByMap 的方法皆需使用 map
     *
     * @param session hibernate session
     * @param entity  查詢主表 entity class
     */
    public QueryUtil(Session session, Class<T> entity, Map<String, Object> criteriaMap) {
        this.entity = entity;
        this.cb = session.getCriteriaBuilder();
        this.criteriaQuery = cb.createQuery(entity);
        this.root = criteriaQuery.from(entity);
        this.criteriaMap = criteriaMap;
    }

    /**
     * 客製 criteriaQuery -> 查詢結果直接 binding 成 pojo
     * <br>需設定查詢的欄位 colList，且 pojo 要有對應此查詢欄位的建構子
     * <br>加入查詢條件的 map (map 的 key 需與 entity 的 column property 相同)
     * <br>此 utils 中各種 ByMap 的方法皆需使用 map
     *
     * @param session     hibernate session
     * @param entity      查詢主表 entity class
     * @param resultClass 查詢結果 result class
     * @param criteriaMap 查詢條件 map
     */
    public QueryUtil(Session session, Class<T> entity, Class<?> resultClass, Map<String, Object> criteriaMap) {
        this.entity = entity;
        this.resultClass = resultClass;
        this.cb = session.getCriteriaBuilder();
        this.criteriaQuery = cb.createQuery(resultClass);
        this.root = criteriaQuery.from(entity);
        this.criteriaMap = criteriaMap;
    }

    /**
     * 取得多筆查詢結果 List (resultClass)
     *
     * @param session  hibernate session
     * @param distinct 是否 distinct
     */
    public List<?> getResultList(Session session, boolean distinct, Integer... max) {
        if (max.length > 0) {
            return session.createQuery(this.getCriterias(distinct))
                    .setMaxResults(max[0]).getResultList();
        } else {
            return session.createQuery(this.getCriterias(distinct)).getResultList();
        }
    }

    /**
     * 取得單筆查詢結果 resultClass
     *
     * @param session  hibernate session
     * @param distinct 是否 distinct
     */
    public Object getUniqueList(Session session, boolean distinct) {
        return session.createQuery(this.getCriterias(distinct))
                .getSingleResult();
    }

    /**
     * 組合所有查詢條件
     *
     * @param distinct 是否 distinct
     */
    public CriteriaQuery getCriterias(boolean distinct) {
        Predicate[] predicates = new Predicate[predicateSet.size()];
        if (!predicateSet.isEmpty() && !colList.isEmpty()) {
            if (predicateSet.size() == 1) {
                criteriaQuery.multiselect(colList)
                        .where(predicateSet.iterator().next()).distinct(distinct);
            } else {
                criteriaQuery.multiselect(colList)
                        .where(cb.and(predicateSet.toArray(predicates))).distinct(distinct);
            }
        } else if (!predicateSet.isEmpty()) {
            if (predicateSet.size() == 1) {
                criteriaQuery.where(predicateSet.iterator().next()).distinct(distinct);
            } else {
                criteriaQuery.where(cb.and(predicateSet.toArray(predicates))).distinct(distinct);
            }
        } else if (!colList.isEmpty()) {
            criteriaQuery.multiselect(colList).distinct(distinct);
        } else {
            criteriaQuery.distinct(distinct);
        }
        return criteriaQuery;
    }

    public void addQueryCol(Selection<?>... colPaths) {
        this.colList.addAll(Arrays.asList(colPaths));
    }

    public void addEqual(Path colPath, Object value) {
        //加入 equal 條件
        if (value != null) {
            String replace = value.toString().replace("[", "[[]");
            predicateSet.add(cb.equal(colPath, replace));
        }
    }

    public void addEqual(String rootColName, Object value) {
        //加入 equal 條件
        if (value != null) {
            String replace = value.toString().replace("[", "[[]");
            predicateSet.add(cb.equal(root.get(rootColName), replace));
        }
    }

    public void addLike(String rootColName, Object value) {
        //加入 like 條件
        if (value != null) {
            String replace = value.toString().replace("[", "[[]");
            predicateSet.add(cb.like(root.get(rootColName), "%" + replace + "%"));
        }
    }

    public void addIsNull(String... rootColNames) {
        //加入 is null 條件
        for (String col : rootColNames) {
            predicateSet.add(cb.isNull(root.get(col)));
        }
    }

    public void addIsNotNull(String... rootColNames) {
        //加入 is null 條件
        for (String col : rootColNames) {
            predicateSet.add(cb.isNotNull(root.get(col)));
        }
    }

    public void addIn(String rootColName, Object values) {
        //加入 is null 條件
        if (values != null) {
            predicateSet.add(cb.in(root.get(rootColName)).value(values));
        }
    }

    public void addIsNull(Join alias, String... aliasColNames) {
        //加入 equal 條件
        for (String col : aliasColNames) {
            predicateSet.add(cb.isNull(alias.get(col)));
        }
    }

    public void addEqualByMap(String... mapKeys) {
        //加入 equal 條件
        String replace;
        for (String param : mapKeys) {
            if (criteriaMap.get(param) != null && !criteriaMap.get(param).equals("")) {
                replace = criteriaMap.get(param).toString().replace("[", "[[]");
                predicateSet.add(cb.equal(root.get(param), replace));
            }
        }
    }

    public void addEqualByMap(Join alias, String aliasColName, String mapKey) {
        //加入 equal 條件
        String replace;
        if (criteriaMap.get(mapKey) != null && !criteriaMap.get(mapKey).equals("")) {
            replace = criteriaMap.get(mapKey).toString().replace("[", "[[]");
            predicateSet.add(cb.equal(alias.get(aliasColName), replace));
        }
    }

    public void addEqualByMap(Join alias, String... mapKeys) {
        //加入 equal 條件
        String replace;
        for (String param : mapKeys) {
            if (criteriaMap.get(param) != null && !criteriaMap.get(param).equals("")) {
                replace = criteriaMap.get(param).toString().replace("[", "[[]");
                predicateSet.add(cb.equal(alias.get(param), replace));
            }
        }
    }

    public void addLikeByMap(String... mapKeys) {
        //加入 like 條件
        String replace;
        for (String param : mapKeys) {
            if (criteriaMap.get(param) != null && !criteriaMap.get(param).equals("")) {
                replace = criteriaMap.get(param).toString().replace("[", "[[]");
                predicateSet.add(cb.like(root.get(param), "%" + replace + "%"));
            }
        }
    }

    public void addLikeByMap(Join alias, String... mapKeys) {
        //加入 like 條件
        String replace;
        for (String param : mapKeys) {
            if (criteriaMap.get(param) != null && !criteriaMap.get(param).equals("")) {
                replace = criteriaMap.get(param).toString().replace("[", "[[]");
                predicateSet.add(cb.like(alias.get(param), "%" + replace + "%"));
            }
        }
    }

    public void addGeDoubleByMap(String... mapKeys) {
        //加入 equal 條件
        for (String param : mapKeys) {
            if (criteriaMap.get(param) != null && !criteriaMap.get(param).equals("")) {
                predicateSet.add(cb.ge(root.get(param), (Double) criteriaMap.get(param)));
            }
        }
    }

    public void addGeDoubleByMap(Boolean notGe, Double geDouble, String... mapKeys) {
        //加入 equal 條件
        for (String param : mapKeys) {
            if (notGe) {
                predicateSet.add(cb.or(
                        cb.isNull(root.get(param)),
                        cb.lessThan(root.get(param), geDouble)
                ));
            } else {
                predicateSet.add(cb.ge(root.get(param), geDouble));
            }
        }
    }

    public void addOrEqualByMap(String mapKey, String... rootColNames) {
        //加入 or equal 條件
        Set<Predicate> equalPredicate = new HashSet<>();
        if (criteriaMap.get(mapKey) != null && !criteriaMap.get(mapKey).equals("")) {
            String replace;
            for (String col : rootColNames) {
                replace = criteriaMap.get(mapKey).toString().replace("[", "[[]");
                equalPredicate.add(cb.equal(root.get(col), replace));
                //                pathSet.add(root.get(col));
            }
        }
        if (!equalPredicate.isEmpty()) {
            Predicate[] predicates = new Predicate[equalPredicate.size()];
            predicateSet.add(cb.or(equalPredicate.toArray(predicates)));
        }
    }

    public void addOrEqualByMap(String mapKey, Path... colPaths) {
        //加入 or like 條件
        Set<Predicate> equalPredicate = new HashSet<>();
        if (criteriaMap.get(mapKey) != null && !criteriaMap.get(mapKey).equals("")) {
            String replace;
            for (Path colPath : colPaths) {
                replace = criteriaMap.get(mapKey).toString().replace("[", "[[]");
                equalPredicate.add(cb.equal(colPath, replace));
            }
        }
        if (!equalPredicate.isEmpty()) {
            Predicate[] predicates = new Predicate[equalPredicate.size()];
            predicateSet.add(cb.or(equalPredicate.toArray(predicates)));
        }
    }

    public void addOrLikeByMap(String mapKey, String... rootColNames) {
        //加入 or like 條件
        Set<Predicate> likePredicate = new HashSet<>();
        if (criteriaMap.get(mapKey) != null && !criteriaMap.get(mapKey).equals("")) {
            String replace;
            for (String col : rootColNames) {
                replace = criteriaMap.get(mapKey).toString().replace("[", "[[]");
                likePredicate.add(cb.like(root.get(col), "%" + replace + "%"));

            }
        }
        if (!likePredicate.isEmpty()) {
            Predicate[] predicates = new Predicate[likePredicate.size()];
            predicateSet.add(cb.or(likePredicate.toArray(predicates)));
        }
    }

    public void addOrLikeByMap(String mapKey, Expression... colPaths) {
        //加入 or like 條件
        Set<Predicate> likePredicate = new HashSet<>();
        if (criteriaMap.get(mapKey) != null && !criteriaMap.get(mapKey).equals("")) {
            String replace = "";
            char[] stringchar = criteriaMap.get(mapKey).toString()
                    .replaceAll(" ", "").toCharArray();
            for (char charStr : stringchar) {
                replace += "%" + charStr;
            }
            for (Expression colPath : colPaths) {
                replace += "%";
                replace = replace.replace("[", "[[]");
                likePredicate.add(cb.like(colPath, replace));
            }
        }
        if (!likePredicate.isEmpty()) {
            Predicate[] predicates = new Predicate[likePredicate.size()];
            predicateSet.add(cb.or(likePredicate.toArray(predicates)));
        }
    }

    public void addDateBetween(String colName, Date sDate, Date eDate, Join... alias) {
        Path path;
        if (alias.length > 0) {
            path = alias[0];
        } else {
            path = root;
        }
        if (sDate != null && eDate != null) {
            predicateSet.add(cb.between(path.get(colName), sDate, eDate));
        } else if (sDate != null) {
            predicateSet.add(cb.greaterThanOrEqualTo(path.get(colName), sDate));
        } else if (eDate != null) {
            predicateSet.add(cb.lessThanOrEqualTo(path.get(colName), eDate));
        }
    }

}
