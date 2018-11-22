module com.github.kostrovik.useful.utils {
    requires java.logging;

    exports com.github.kostrovik.useful.utils;
    exports com.github.kostrovik.useful.interfaces;
    exports com.github.kostrovik.useful.models;

    uses com.github.kostrovik.useful.interfaces.LoggerConfigInterface;
}