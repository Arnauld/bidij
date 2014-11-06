package bdi.glue.env;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class VariableResolver {
    private final Map<String, Supplier<String>> variables = new HashMap<>();
    private Pattern variablePattern = Pattern.compile("\\$\\{([^}]+)\\}");

    public VariableResolver declareVariable(String name, Supplier<String> valueSupplier) {
        variables.put(name, valueSupplier);
        return this;
    }

    public VariableResolver declareVariable(String name, String value) {
        variables.put(name, () -> value);
        return this;
    }

    public VariableResolver declareVariables(Map<String, String> variables) {
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            declareVariable(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public VariableResolver declareVariables(Properties properties) {
        for (String name : properties.stringPropertyNames()) {
            declareVariable(name, properties.getProperty(name));
        }
        return this;
    }

    public VariableResolver variablePattern(String pattern) {
        this.variablePattern = Pattern.compile(pattern);
        return this;
    }

    public String resolve(String value) {
        return resolve(value, variables);
    }

    public static Map<String, Supplier<String>> convert(Map<String, String> variables) {
        Map<String, Supplier<String>> map = new HashMap<>();
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            map.put(entry.getKey(), entry::getValue);
        }
        return map;
    }

    public String resolve(String text, Map<String, Supplier<String>> variables) {
        StringBuilder resolved = new StringBuilder();

        Matcher matcher = variablePattern.matcher(text);
        int prev = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (start > prev) {
                resolved.append(text, prev, start);
            }
            String var = lookup(variables, matcher.group(1));
            if (var != null) {
                resolved.append(var);
            } else {
                resolved.append(text, start, end);
            }
            prev = end;
        }
        if (prev < text.length()) {
            resolved.append(text, prev, text.length());
        }

        return resolved.toString();
    }

    private String lookup(Map<String, Supplier<String>> variables, String key) {
        Supplier<String> supplier = variables.get(key);
        if (supplier == null)
            return null;
        return supplier.get();
    }
}
