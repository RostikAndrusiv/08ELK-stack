package com.rostik.andrusiv.mesaging.serviceimpl;

import com.rostik.andrusiv.mesaging.exception.IndexException;
import com.rostik.andrusiv.mesaging.serviceapi.IndexService;
import com.rostik.andrusiv.mesaging.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class IndexServiceImpl implements IndexService {

    private static final String FAILED_TO_CREATE_INDEX_WITH_NAME = "Failed to create index with name ";
    @Autowired
    private RestHighLevelClient client;

    public Boolean createIndex(String indexName) {
        boolean indexExists = false;
        final String settings = Util.loadAsString("static/es-settings.json");
        final String mappings = Util.loadAsString("static/mappings/" + indexName + ".json");

        if (null==settings || null == mappings){
            throw new IndexException(FAILED_TO_CREATE_INDEX_WITH_NAME + indexName + ". Settings either mappings is null.");
        }
        try {
            indexExists = client
                    .indices()
                    .exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
            if (!indexExists) {
                final CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
                createIndexRequest.settings(settings, XContentType.JSON);
                createIndexRequest.mapping(mappings, XContentType.JSON);
                client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
                return true;
            }
        } catch (IOException e) {
            throw new IndexException(FAILED_TO_CREATE_INDEX_WITH_NAME + indexName + ". IO Exception while reading settings");
        }
        throw new IndexException(FAILED_TO_CREATE_INDEX_WITH_NAME + indexName + ". Index already exist.");
    }
}
