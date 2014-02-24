package com.example;

import org.apache.lucene.index.AtomicReaderContext;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.index.fielddata.DoubleValues;
import org.elasticsearch.index.fielddata.plain.DoubleArrayIndexFieldData;
import org.elasticsearch.search.facet.FacetExecutor;
import org.elasticsearch.search.facet.InternalFacet;

import java.io.IOException;
import java.util.List;

public class ExampleExecutor extends FacetExecutor {

	private final DoubleArrayIndexFieldData indexFieldData;
	private final double factor;
    private List<Double> entries = Lists.newArrayList();

    public ExampleExecutor(DoubleArrayIndexFieldData indexFieldData, double factor) {
		this.indexFieldData = indexFieldData;
		this.factor = factor;
	}

	@Override
	public FacetExecutor.Collector collector() {
		return new Collector();
	}

	@Override
	public InternalFacet buildFacet(String facetName) {
		return new InternalExampleFacet(facetName, factor, entries);
	}

	private class Collector extends FacetExecutor.Collector {

		private DoubleValues values;

		@Override
		public void setNextReader(AtomicReaderContext context) throws IOException {
			values = indexFieldData.load(context).getDoubleValues();
		}

		@Override
		public void collect(int docId) throws IOException {
			final int length = values.setDocument(docId);
			for (int i = 0; i < length; i++) {
                double value = values.nextValue();
                System.out.println("COLLECTED VALUE: " + value);
                entries.add(value);
			}
		}

		@Override
		public void postCollection() {

		}
	}
}
