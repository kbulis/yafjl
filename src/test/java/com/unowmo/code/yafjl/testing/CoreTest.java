package com.unowmo.code.yafjl.testing;

import static com.unowmo.code.yafjl.Core.*;

import java.util.ArrayList;
import org.junit.Test;

public class CoreTest {

    @Test
    public void testCore() {
        Iterable<String> sample = new ArrayList<String>() {{
            this.add("testing");
            this.add("foo");
            this.add("testing");
        }};

        try {
            find("testing").in(sample).then(match -> {
                throw new Exception("Oops!");
            })
            .none(() -> System.out.println("Nothing!"))
            .oops(System.out::println)
            .oops(System.out::println);
        }
        catch (final Exception eX) {
            System.err.println(eX.getMessage());
        }
    }

}
