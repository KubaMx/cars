package com.app.cars.converter;

import java.util.Optional;

public sealed interface JsonConverter<T> permits JsonConverterImpl {

    void to(T element);
    Optional<T> from();
}
