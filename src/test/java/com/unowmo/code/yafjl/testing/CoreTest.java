package com.unowmo.code.yafjl.testing;

import org.junit.Test;
import static com.unowmo.code.yafjl.Core.adapt;
import static com.unowmo.code.yafjl.Core.in;

public class CoreTest {

    @Test
    public void testCore() {
        // ...

        in(adapt("a", "b", "c", "a"))
            .derive((item) -> item.equals("a") ? 1 : 0)
            .each((item) -> {
                System.out.println("Found '" + item + "'");
            })
            .reduce((item, total) -> {
                return total += item;
            }, 0);

        // ...

        in(adapt("a", "b", "c", "a"))
            .filter((item) -> item.equals("a"))
            .each((item) -> {
                System.out.println("Found '" + item + "'");
            })
            .all();

        // ...

        in(adapt("a", "b", "c", "a"))
            .filter((item) -> item.equals("a"))
            .each((item) -> {
                System.out.println("Found '" + item + "'");
            })
            .one();
    }

}
