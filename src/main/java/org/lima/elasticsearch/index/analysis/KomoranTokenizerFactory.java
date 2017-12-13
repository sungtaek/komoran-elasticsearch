package org.lima.elasticsearch.index.analysis;

import java.io.File;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;
import org.lima.elasticsearch.tokenizer.KomoranTokenizer;

import kr.co.shineware.nlp.komoran.core.Komoran;

public class KomoranTokenizerFactory extends AbstractTokenizerFactory {
	private Komoran komoran;

	public KomoranTokenizerFactory(IndexSettings indexSettings, String name, Settings settings, String pluginPath) {
		super(indexSettings, name, settings);
		
		String models = settings.get("models");
		if(models == null) {
			models = KomoranTokenizer.defaultModels;
		}
		models = pluginPath + File.separator + KomoranTokenizer.name + File.separator + models;
		this.komoran = new Komoran(models);
	}

	@Override
	public Tokenizer create() {
		return new KomoranTokenizer(komoran);
	}

}
