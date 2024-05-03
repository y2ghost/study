es_tgz="elasticsearch-8.11.3-darwin-x86_64.tar.gz"
kibana_tgz="kibana-8.11.3-darwin-x86_64.tar.gz"
logstash_tgz="logstash-8.11.3-darwin-x86_64.tar.gz"
temp_dir="tmp_elk_$$"

mkdir ${temp_dir}
cd ${temp_dir}

curl -fsSL -o ${es_tgz} https://artifacts.elastic.co/downloads/elasticsearch/${es_tgz}
tar xfvz ${es_tgz}
rm -f ${es_tgz}
mv elasticsearch-* elasticsearch
echo "xpack.security.enabled: false" >> elasticsearch/config/elasticsearch.yml

curl -fsSL -o ${kibana_tgz} https://artifacts.elastic.co/downloads/kibana/${kibana_tgz}
tar xfvz ${kibana_tgz}
rm -f ${kibana_tgz}
mv kibana-* kibana

curl -fsSL -o ${logstash_tgz} https://artifacts.elastic.co/downloads/logstash/${logstash_tgz}
tar xfvz ${logstash_tgz}
rm -f ${logstash_tgz}
mv logstash-* logstash

exit 0
