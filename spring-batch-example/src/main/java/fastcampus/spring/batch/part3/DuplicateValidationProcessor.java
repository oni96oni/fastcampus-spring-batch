package fastcampus.spring.batch.part3;

import org.springframework.batch.item.ItemProcessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class DuplicateValidationProcessor<T> implements ItemProcessor<T, T> {

    private final Map<String, Object> keyPool = new ConcurrentHashMap<>();
    private final Function<T, String> keyExtractor;
    private final boolean allowDuplicate;

    public DuplicateValidationProcessor(Function<T, String> keyExtractor, boolean allowDuplicate) {
        this.keyExtractor = keyExtractor;
        this.allowDuplicate = allowDuplicate;
    }

    @Override
    public T process(T item) throws Exception {
        if (allowDuplicate) {
            return item;
        }

        String key = keyExtractor.apply(item); // 해당 item으로 key를 추출

        if (keyPool.containsKey(key)) { // keyPool에 key가 존재하면 중복이 되었다는 의미
            return null; // null을 반환
        }

        keyPool.put(key, key); // keyPool에 key를 넣어준다.

        return item;
    }
}
