package com.example;

import org.elasticsearch.common.Strings;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.bytes.HashedBytesArray;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentBuilderString;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.InternalFacet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class InternalExampleFacet extends InternalFacet implements ExampleFacet, Iterable<Double> {

    private static final BytesReference STREAM_TYPE = new HashedBytesArray(Strings.toUTF8Bytes("example"));

    private static Stream STREAM = new Stream() {

        @Override
        public Facet readFacet(StreamInput in) throws IOException {
            return readGeoClusterFacet(in);
        }
    };

    public static void registerStreams() {
        Streams.registerStream(STREAM, STREAM_TYPE);
    }

    private double factor;
    private List<Double> entries;
    private double total;

    InternalExampleFacet() {
    }

    InternalExampleFacet(String name, double total) {
        super(name);
        this.total = total;
    }

    public InternalExampleFacet(String name, double factor, List<Double> entries) {
        super(name);
        this.factor = factor;
        this.entries = entries;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public BytesReference streamType() {
        return STREAM_TYPE;
    }

    @Override
    public Facet reduce(ReduceContext context) {
        List<Facet> facets = context.facets();
        System.out.println(facets.size());
        if (facets.size() == 1) {
            Facet facet = facets.get(0);
            return facet;
        }
        double total = 0;

        for (Facet facet : facets) {
            InternalExampleFacet tsFacet = (InternalExampleFacet) facet;
            for (Double entry : tsFacet) {
                total += entry;
                System.out.println("FACET: " + entry);
            }
        }
        System.out.println("SUB TOTAL: " + total);
        return new InternalExampleFacet(getName(), total);
    }

    public static InternalExampleFacet readGeoClusterFacet(StreamInput in) throws IOException {
        InternalExampleFacet facet = new InternalExampleFacet();
        facet.readFrom(in);
        return facet;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        factor = in.readDouble();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
    }

    @Override
    public Iterator<Double> iterator() {
        return entries.iterator();
    }

    private interface Fields {

        final XContentBuilderString _TYPE = new XContentBuilderString("_type");
        final XContentBuilderString FACTOR = new XContentBuilderString("factor");
        final XContentBuilderString CLUSTERS = new XContentBuilderString("clusters");
        final XContentBuilderString TOTAL = new XContentBuilderString("total");
        final XContentBuilderString CENTER = new XContentBuilderString("center");
        final XContentBuilderString TOP_LEFT = new XContentBuilderString("top_left");
        final XContentBuilderString BOTTOM_RIGHT = new XContentBuilderString("bottom_right");
        final XContentBuilderString LAT = new XContentBuilderString("lat");
        final XContentBuilderString LON = new XContentBuilderString("lon");
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(getName());
        builder.field(Fields.TOTAL, total);
        System.out.println("TOTAL: " + total);
        builder.endObject();
        return builder;
    }

}
