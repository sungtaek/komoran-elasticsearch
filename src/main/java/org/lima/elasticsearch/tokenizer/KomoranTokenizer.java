package org.lima.elasticsearch.tokenizer;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;

public class KomoranTokenizer extends Tokenizer {
	public static String name = "komoran_tokenizer";
	public static String defaultModels = "models_light";

	private Komoran komoran;
	
	private List<Token> tokens;
	private int idx;
	
	private CharTermAttribute charTermAttribute;
	private TypeAttribute typeAttribute;
	
	public KomoranTokenizer(Komoran komoran) {
		this.komoran = komoran;

		tokens = null;
		idx = 0;

		this.charTermAttribute = addAttribute(CharTermAttribute.class);
		this.typeAttribute = addAttribute(TypeAttribute.class);
	}

	@Override
	public boolean incrementToken() throws IOException {
		clearAttributes();
		
		// read document & tagging
		if(tokens == null) {
			String doc = getDocument();
			tokens = komoran.analyze(doc).getTokenList();
			idx = 0;
		}
		
		// set token/tag
		if(tokens != null && tokens.size() > idx) {
			Token token = tokens.get(idx);
			charTermAttribute.append(token.getMorph());
			typeAttribute.setType(token.getPos());
			idx++;
			return true;
		}
		
		initValues();
		return false;
	}
	
	@Override
	public final void reset() throws IOException {
		super.reset();
		initValues();
	}
	
	private void initValues() {
		tokens = null;
		idx = 0;
	}

	private String getDocument() throws IOException {
		StringBuilder doc = new StringBuilder();
		char[] tmp = new char[1024];
		int len;
		while((len = input.read(tmp)) != -1) {
			doc.append(new String(tmp, 0, len));
		}
		return doc.toString();
	}
}
