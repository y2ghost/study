es_tgz="elasticsearch-7.12.1-darwin-x86_64.tar.gz"
kibana_tgz="kibana-7.12.1-darwin-x86_64.tar.gz"

curl -fsSL -o ${es_tgz} https://artifacts.elastic.co/downloads/elasticsearch/${es_tgz}
tar xfvz ${es_tgz}
mv elasticsearch-7.12.1 elasticsearch

curl -fsSL -o ${kibana_tgz} https://artifacts.elastic.co/downloads/kibana/${kibana_tgz}
tar xfvz ${kibana_tgz}
mv kibana-7.12.1-darwin-x86_64 kibana
exit 0

