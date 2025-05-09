package com.dra.todo_app.constant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class AppConstantTest {

    @Test
    void constructor_shouldThrowException() throws NoSuchMethodException, SecurityException {
        Constructor<AppConstant> constructor = AppConstant.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance();
        });
        assertTrue(thrown.getCause() instanceof UnsupportedOperationException);
        assertEquals("Utility class", thrown.getCause().getMessage());
    }

}
