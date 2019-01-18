#　需要
1. windows环境下java8 （支持linux，需要修改es等）
2. es(https://github.com/xjtushilei/InformationSystemModeling/releases/download/es-6.2.4/elasticsearch-6.2.4.zip) （下载这个版本，里面已经添加了中文ik分词支持）

# 运行

1. 打开es的bin目录，运行“elasticsearch.bat”
2. 下载该分支的源码
3. 编译工程，运行 `gradlew build`，生成了一个jar文件。在“build/libs/search-engine.jar”
4. 到该目录下运行该jar文件即可 `java -jar build\libs\search-engine.jar`
5. 浏览器浏览 "http://localhost:8089/"

# 说明
0. 刚开始什么文档都没有，需要使用“索引文件的api”把爬虫到的图片索引到es中才行
1. html文件目录：`src\main\resources\static`
2. 索引文件的api文档：http://localhost:8089/swagger-ui.html#/es-controller/indexUsingPOST