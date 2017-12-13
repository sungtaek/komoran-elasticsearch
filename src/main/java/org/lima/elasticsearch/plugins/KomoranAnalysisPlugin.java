package org.lima.elasticsearch.plugins;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;
import org.lima.elasticsearch.index.analysis.KomoranTokenizerFactory;
import org.lima.elasticsearch.tokenizer.KomoranTokenizer;


public class KomoranAnalysisPlugin extends Plugin implements AnalysisPlugin {

	@Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {

        Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> extraTokenizer = new HashMap<>();
        extraTokenizer.put(KomoranTokenizer.name, new AnalysisModule.AnalysisProvider<TokenizerFactory>() {
			@Override
			public TokenizerFactory get(IndexSettings indexSettings, Environment environment, String name,
					Settings settings) throws IOException {
				String pluginPath = environment.pluginsFile().toRealPath(LinkOption.NOFOLLOW_LINKS).toString();
				return new KomoranTokenizerFactory(indexSettings, name, settings, pluginPath);
			}
        });

        return extraTokenizer;
    }
}
