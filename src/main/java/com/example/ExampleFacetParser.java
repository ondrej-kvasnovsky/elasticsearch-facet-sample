package com.example;

import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.fielddata.plain.DoubleArrayIndexFieldData;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.search.facet.FacetExecutor;
import org.elasticsearch.search.facet.FacetExecutor.Mode;
import org.elasticsearch.search.facet.FacetParser;
import org.elasticsearch.search.facet.FacetPhaseExecutionException;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;

public class ExampleFacetParser extends AbstractComponent implements FacetParser {

    @Inject
    public ExampleFacetParser(Settings settings) {
        super(settings);
        InternalExampleFacet.registerStreams();
    }

    @Override
    public String[] types() {
        return new String[]{
                ExampleFacet.TYPE
        };
    }

    @Override
    public Mode defaultMainMode() {
        return Mode.COLLECTOR;
    }

    @Override
    public Mode defaultGlobalMode() {
        return Mode.COLLECTOR;
    }

    @Override
    public FacetExecutor parse(String facetName, XContentParser parser, SearchContext context) throws IOException {

        String fieldName = null;
        double factor = 0.1;

        String currentName = parser.currentName();
        XContentParser.Token token;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentName = parser.currentName();
            } else if (token.isValue()) {
                if ("field".equals(currentName)) {
                    fieldName = parser.text();
                } else if ("factor".equals(currentName)) {
                    factor = parser.doubleValue();
                }
            }
        }

//        if (factor < 0.0 || factor > 1.0) {
//            throw new FacetPhaseExecutionException(facetName, "value [" + factor + "] is not in range [0.0, 1.0]");
//        }
        FieldMapper<?> fieldMapper = context.smartNameFieldMapper(fieldName);
        if (fieldMapper == null) {
            throw new FacetPhaseExecutionException(facetName, "failed to find mapping for [" + fieldName + "]");
        }
        DoubleArrayIndexFieldData indexFieldData = context.fieldData().getForField(fieldMapper);

        return new ExampleFacetExecutor(indexFieldData, factor);
    }
}
