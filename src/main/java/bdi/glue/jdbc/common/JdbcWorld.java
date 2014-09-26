package bdi.glue.jdbc.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JdbcWorld {
    public static final String DEFAULT = "default";

    private final Map<String, JdbcConf> confs = new HashMap<>();
    private String currentConf = DEFAULT;
    private int maxFetchSize = 100;
    private Rows rows;

    public JdbcConf getConf(String confName) {
        return confs.get(confName);
    }

    public JdbcWorld defineConfAndSetAsCurrent(String confName, JdbcConf conf) {
        return defineConf(confName, conf).defineCurrentConf(confName);
    }

    public JdbcWorld defineConf(String confName, JdbcConf conf) {
        confs.put(confName, conf);
        return this;
    }

    public JdbcWorld defineCurrentConf(String confName) {
        currentConf = confName;
        return this;
    }

    public JdbcConf currentConf() {
        return getConf(currentConf);
    }

    public JdbcWorld defineMaxFetchSize(int maxFetchSize) {
        this.maxFetchSize = maxFetchSize;
        return this;
    }

    public int maxFetchSize() {
        return maxFetchSize;
    }

    public JdbcWorld lastResult(Rows rows) {
        this.rows = rows;
        return this;
    }

    public Rows lastResult() {
        return rows;
    }
}
