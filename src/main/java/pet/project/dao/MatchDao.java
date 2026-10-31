package pet.project.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pet.project.entities.Matches;

import java.util.List;

@Component // Для DAO можно использовать специальную стереотипную аннотацию — @Repository
public class MatchDao {

    // Нет интерфейса для этого класса.
        // Это нарушение Принципа инверсии зависимостей (Dependency Inversion Principle):
        // Принцип гласит, что модули верхних уровней не должны зависеть от модулей нижних уровней,
        // а также они должны зависеть от абстракций. В данном случае вышестоящие модули (сервисы)
        // напрямую зависят от конкретных реализаций репозиториев, что делает систему жёстко связанной.
        // В небольших проектах это не критично, но в крупных — предпочтительно использовать интерфейсы.

    // TODO: Слой репозиториев должен перехватывать специфичные для технологии исключения
        // и оборачивать их в свои исключения слоя доступа к данным.
        // Это скрывает детали реализации от верхних слоёв и делает их независимыми от деталей реализации репозиториев.
        // Тело каждого метода стоит обернуть в try-catch и отлавливать исключения при работе с БД.
        // Либо добавить механизм автоматической трансляции исключений.
        // Например, это можно сделать создав бин PersistenceExceptionTranslationPostProcessor.
        // Тогда Spring будет сам оборачивать их в исключения из иерархии DataAccessException
        // (например, DataIntegrityViolationException, JpaSystemException и др).

    // Как и в PlayerDao, методы в этом классе тоже можно написать в текучем (fluent) стиле.

    // Константы в Java принято объявлять первыми (самыми верхними) в классе

    // В HQL запросах используется ORDER BY m.id ASC.
        // При таком подходе последний сыгранный матч окажется последним в списке и
        // пользователю придётся листать до последней страницы, чтобы найти его в списке завершённых матчей.
        // Лучше использовать сортировку по убыванию — `ORDER BY m.id DESC`.
        // Это улучшит пользовательский опыт.

    // Для HQL-констант используется префикс SQL. Лучше использовать HQL или JPQL.

    private final SessionFactory sessionFactory;
    private static final String SQL_FIND_FINISHED_MATCHES = """
            SELECT m FROM Matches m
         JOIN FETCH m.player1
         JOIN FETCH m.player2
         JOIN FETCH m.winner
         """;
    private static final String SQL_FIND_FINISHED_MATCHES_BY_NAME ="""
             WHERE m.player1.name =:playerName
            OR m.player2.name =:playerName
            """;
    private static final String SQL_COUNT_FINISHED_MATCHES = "SELECT COUNT(m) FROM Matches m";
    private static final String SQL_SORT = " ORDER BY m.id ASC";

    // Если конструктор единственный в классе, то @Autowired над ним можно не писать
    @Autowired
    public MatchDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Matches matches){
        Session session = sessionFactory.getCurrentSession();
        session.persist(matches);
    }

    public List<Matches> findFinishedMatches(int limit, int offset){
        Session session = sessionFactory.getCurrentSession();
        Query<Matches> query = session.createQuery(SQL_FIND_FINISHED_MATCHES + SQL_SORT, Matches.class);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public List<Matches> findFinishedMatchesByPlayerName(int limit, int offset, String playerName){
        Session session = sessionFactory.getCurrentSession();
        Query<Matches> query = session.createQuery(SQL_FIND_FINISHED_MATCHES + SQL_FIND_FINISHED_MATCHES_BY_NAME + SQL_SORT, Matches.class);
        query.setParameter("playerName", playerName);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    // Можно просто countAll
    public Long countByFindFinishedMatches(){
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(SQL_COUNT_FINISHED_MATCHES, Long.class).getSingleResult();
    }

    // Можно просто countAllByPlayerName
    public Long countByFindFinishedMatchesByPlayerName(String playerName){
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery(SQL_COUNT_FINISHED_MATCHES + SQL_FIND_FINISHED_MATCHES_BY_NAME, Long.class);
        query.setParameter("playerName", playerName);
        return query.getSingleResult();
    }
}
