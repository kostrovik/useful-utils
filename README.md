## useful-utils

Набор пользовательских утилит. Используется как подключаемый модуль.

#### Использование `InstanceLocator` для создания логгера

Создать свой класс унаследованный от `LoggerConfigImpl`. Реализовать получение `FileHandler`.

```
import com.github.kostrovik.useful.utils.LoggerConfigImpl;

import java.util.Objects;
import java.util.logging.FileHandler;

public class ApplicationLogger extends LoggerConfigImpl {
    private static FileHandler fileHandler;

    protected FileHandler getFileHandler() {
        if (Objects.isNull(fileHandler)) {
            if (Objects.isNull(fileHandler)) {
                fileHandler = createLoggerFile("logs");
            }
        }
        return fileHandler;
    }
}
```