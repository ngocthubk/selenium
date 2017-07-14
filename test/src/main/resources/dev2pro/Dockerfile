FROM registry.services.infra.automic/awi-plugin/base
MAINTAINER _Engineering.ARA.Ignite _Engineering.ARA.Ignite@automic.com
ADD ./config/ /tmp/config/
ADD ./testdata/ /tmp/testdata/
ADD webui-plugin-actionbuilder-test.jar /tmp/webui-plugin-actionbuilder-test.jar
ADD start_test.sh /tmp/start_test.sh
RUN sudo chmod +x /tmp/start_test.sh
WORKDIR /tmp
CMD ./start_test.sh
