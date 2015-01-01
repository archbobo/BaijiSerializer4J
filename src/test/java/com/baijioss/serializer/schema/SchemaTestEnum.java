package com.baijioss.serializer.schema;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class SchemaTestEnum extends SchemaTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]
                        {"{\"type\": \"enum\", \"name\": \"Test\", \"symbols\": [\"A\", \"B\"]}",
                                new String[]{"A", "B"},
                                new HashMap<String, Integer>() {{
                                    put("A", null);
                                    put("B", null);
                                }},
                        },
                new Object[]
                        {"{\"type\": \"enum\", \"name\": \"Test\", \"symbols\": [\"A\", \"B\", {\"name\": \"C\", \"value\": 5}, \"D\", {\"name\": \"E\", \"value\": 1}, {\"name\": \"F\", \"value\": 7}]}",
                                new String[]{"A", "B", "C", "D", "E", "F"},
                                new HashMap<String, Integer>() {{
                                    put("A", null);
                                    put("B", null);
                                    put("C", 5);
                                    put("D", null);
                                    put("E", 1);
                                    put("F", 7);
                                }}
                        }
        });
    }

    private final String _schema;
    private final String[] _symbols;
    private final Map<String, Integer> _symbolValues;

    public SchemaTestEnum(String schema, String[] symbols, Map<String, Integer> symbolValues) {
        _schema = schema;
        _symbols = symbols;
        _symbolValues = symbolValues;
    }

    @Test
    public void testEnum() {
        Schema sc = Schema.parse(_schema);
        Assert.assertTrue(sc instanceof EnumSchema);
        Assert.assertEquals(SchemaType.ENUM, sc.getType());
        EnumSchema es = (EnumSchema) sc;
        Assert.assertEquals(_symbols.length, es.size());

        int i = 0;
        int lastValue = -1;
        for (String symbol : es.getSymbols()) {
            String expectedSymbol = _symbols[i++];
            Assert.assertEquals(expectedSymbol, symbol);
            Integer expectedValue = _symbolValues.get(expectedSymbol);
            expectedValue = expectedValue != null ? expectedValue.intValue() : lastValue + 1;
            Assert.assertEquals((int) expectedValue, es.ordinal(symbol));
            lastValue = expectedValue;
        }

        testEquality(_schema, sc);
        testToString(sc);
    }
}
