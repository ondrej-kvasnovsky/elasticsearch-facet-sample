=======
elasticsearch-facet-sample
==========================

A simple facet just to help with learning. It makes total sum of double values.

    GET /myindex/mytype/_search
    {
      
      "facets" : {
            "places" : { 
                "example" : {
                    "field" : "cogs",
                    "factor" : 0.5
                }
            }
        }
    }

1. Make .jar package

    mvn package

2. Install the facet the ElasticSearch

    cd /Users/ondrej/projects/elasticsearch

    bin/plugin --url file:///Users/ondrej/projects/elasticsearch-facet/target/elasticsearch-facet-1.0-SNAPSHOT.jar --install example-facet

3. After updating the plugin, run:

    mvn clean package

    bin/plugin --remove example-facet

    bin/plugin --url file:///Users/ondrej/projects/elasticsearch-facet/target/elasticsearch-facet-1.0-SNAPSHOT.jar --install example-facet



