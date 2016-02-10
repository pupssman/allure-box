FROM webratio/groovy:2.4.4

RUN grape -Dgrape.root=/graperoot install ru.yandex.qatools.allure allure-bundle 1.4.22
RUN grape -Dgrape.root=/graperoot install javax.servlet javax.servlet-api 3.0.1 
RUN grape -Dgrape.root=/graperoot install org.eclipse.jetty.aggregate jetty-all-server 8.1.8.v20121106

EXPOSE 80

ADD allure-box.groovy /

ENTRYPOINT ["groovy", "-Dgrape.root=/graperoot", "/allure-box.groovy"]
