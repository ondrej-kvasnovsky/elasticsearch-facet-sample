package com.example;

import org.elasticsearch.plugins.AbstractPlugin;
import org.elasticsearch.search.facet.FacetModule;

public class ExampleFacetPlugin extends AbstractPlugin {

	@Override
	public String name() {
		return "example-facet";
	}

	@Override
	public String description() {
		return "Facet for clustering geo points";
	}

	public void onModule(FacetModule module) {
		module.addFacetProcessor(ExampleFacetParser.class);
		InternalExampleFacet.registerStreams();
	}
}
