package cloudcomputinginha.demo.web.session;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class InMemorySessionManager<T> implements SessionManager<T> {

    private final Map<String,T> sessionMap = new ConcurrentHashMap<String,T>();

    @Override
    public void put(String sessionId, T sessionInfo) {
        if(sessionId == null){
            throw new IllegalArgumentException("sessionId cannot be null");
        }
        if(sessionInfo == null){
            throw new IllegalArgumentException("sessionInfo cannot be null");
        }
        sessionMap.put(sessionId, sessionInfo);
    }

    @Override
    public T remove(String sessionId) {
        return sessionMap.remove(sessionId);
    }

    @Override
    public T get(String sessionId) {
        if(sessionId == null){
            return null;
        }
        return sessionMap.get(sessionId);
    }

    @Override
    public List<T> getAll() {
        return List.copyOf(sessionMap.values());
    }

    @Override
    public List<T> getByPredicate(Predicate<T> predicate) {
        return sessionMap.values().stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public void clear() {
        sessionMap.clear();
    }
}
