package bdi.glue;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */

import bdi.TestSettings;
import bdi.glue.http.testdefs.HttpFeatures;
import bdi.glue.jdbc.testdefs.JdbcFeatures;
import bdi.glue.proc.testdefs.ProcFeatures;
import bdi.glue.ssh.testdefs.SshFeatures;
import com.itextpdf.text.DocumentException;
import gutenberg.itext.ITextContext;
import gutenberg.itext.SimpleEmitter;
import gutenberg.itext.model.Markdown;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tzatziki.analysis.exec.gson.JsonIO;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.support.TagView;
import tzatziki.analysis.exec.tag.TagFilter;
import tzatziki.analysis.java.Grammar;
import tzatziki.analysis.java.GrammarParser;
import tzatziki.analysis.tag.TagDictionaryLoader;
import tzatziki.pdf.support.Configuration;
import tzatziki.pdf.support.DefaultPdfReportBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static tzatziki.pdf.support.DefaultPdfReportBuilder.Overview.TagViews;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        HttpFeatures.class,
        JdbcFeatures.class,
        ProcFeatures.class,
        SshFeatures.class
})
public class TestSuite {

    public static class GlueAndFeature {
        public final List<FeatureExec> features;
        public final Grammar grammar;

        public GlueAndFeature(List<FeatureExec> features, Grammar grammar) {
            this.features = features;
            this.grammar = grammar;
        }
    }

    private static GlueAndFeature load(String pkg) throws IOException {
        TestSettings settings = new TestSettings();
        File sourceDir = new File(settings.projectDir(), "src/main/java/bdi/glue/" + pkg);
        File buildDir = new File(settings.buildDir());
        Grammar grammar = new GrammarParser().usingSourceDirectory(sourceDir).process();

        List<FeatureExec> features = loadFeatures(buildDir, "/" + pkg);
        return new GlueAndFeature(features, grammar);
    }

    @AfterClass
    public static void generateReport() throws IOException, DocumentException {
        TestSettings settings = new TestSettings();
        File buildDir = new File(settings.buildDir());
        File fileOut = new File(buildDir, "features.pdf");


        GlueAndFeature httpGF = load("http");
        GlueAndFeature jdbcGF = load("jdbc");
        GlueAndFeature procGF = load("proc");
        GlueAndFeature sshGF = load("ssh");

        new DefaultPdfReportBuilder()
                .using(new Configuration()
                                .displayFeatureTags(false)
                                .displayScenarioTags(true)
                                .declareProperty("imageDir",
                                        "file://" + settings.projectDir() + "/src/test/resources/bdi/glue/images")
                )
                .title("bidij")
                .subTitle("Behavior Driven Infrastructure support for Java")
                .markup(Markdown.fromUTF8Resource("/bdi/glue/00-preambule.md"))
                        // --- HTTP
                .markup(Markdown.fromUTF8Resource("/bdi/glue/http/testdefs/00-http.md"))
                        //.overview(httpFeatures, 1, FeatureSummary)
                .emit(grammar(httpGF.grammar))
                .features(httpGF.features, 1)
                        // --- JDBC
                .markup(Markdown.fromUTF8Resource("/bdi/glue/jdbc/testdefs/00-jdbc.md"))
                .emit(grammar(jdbcGF.grammar))
                .features(jdbcGF.features, 1)
                        // --- Proc
                .markup(Markdown.fromUTF8Resource("/bdi/glue/proc/testdefs/00-proc.md"))
                .emit(grammar(procGF.grammar))
                .features(procGF.features, 1)
                        // --- Ssh
                .markup(Markdown.fromUTF8Resource("/bdi/glue/ssh/testdefs/00-ssh.md"))
                .emit(grammar(sshGF.grammar))
                .features(sshGF.features, 1)
                        // ---
                .overview(TagViews)
                .tagDictionary(new TagDictionaryLoader().fromUTF8PropertiesResource("/bdi/glue/tags.properties"))
                .tagViews(
                        new TagView("Jdbc", TagFilter.from("~@wip", "@jdbc")),
                        new TagView("Http", TagFilter.from("~@wip", "@http"))
                )
                .sampleSteps()
                .generate(fileOut);
    }

    private static SimpleEmitter grammar(Grammar grammar) {
        return new SimpleEmitter() {
            @Override
            public void emit(ITextContext context) {
                context.emit(grammar);
            }
        };
    }

    private static List<FeatureExec> loadFeatures(File buildDir, String resourcePath) throws IOException {
        try (InputStream in = new FileInputStream(new File(buildDir, resourcePath + "/exec.json"))) {
            return new JsonIO().load(in);
        }
    }
}
