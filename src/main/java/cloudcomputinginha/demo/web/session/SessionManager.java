package cloudcomputinginha.demo.web.session;

import java.util.List;

// 세션 관리 추상화(추후 다른 세션 관리를 위해)
public interface SessionManager<T> {

    void put(String sessionId, T sessionInfo);

    T remove(String sessionId);

    T get(String sessionId);

    List<T> getAll();

    List<T> getByPredicate(java.util.function.Predicate<T> predicate);

    void clear();
}