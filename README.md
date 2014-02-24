1. Make .jar package

    mvn package

2. Install the facet the ElasticSearch

    cd /Users/ondrej/projects/elasticsearch

    bin/plugin --url file:///Users/ondrej/projects/elasticsearch-facet/target/elasticsearch-facet-1.0-SNAPSHOT.jar --install example-facet

3. After updating the plugin, run:

    mvn clean package

    bin/plugin --remove example-facet

    bin/plugin --url file:///Users/ondrej/projects/elasticsearch-facet/target/elasticsearch-facet-1.0-SNAPSHOT.jar --install example-facet

