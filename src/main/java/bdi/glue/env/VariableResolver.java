package bdi.glue.env;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class VariableResolver {
    private Map<String, String> variables = new HashMap<>();
    private Pattern variablePattern = Pattern.compile("\\$\\{([^}]+)\\}");

    public VariableResolver declareVariable(String name, String value) {
        variables.put(name, value);
        return this;
    }

    public VariableResolver declareVariables(Map<String, String> variables) {
        this.variables.putAll(variables);
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

    public String resolve(String text, Map<String, String> variables) {
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

    private String lookup(Map<String, String> variables, String key) {
        String s = variables.get(key);
        //System.out.println("VariableResolver.lookup:: " + key + " -> <" + s + ">");
        return s;
    }
}
