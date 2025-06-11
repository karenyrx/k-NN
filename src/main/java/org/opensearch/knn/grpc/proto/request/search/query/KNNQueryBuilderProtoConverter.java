/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.knn.grpc.proto.request.search.query;

import org.opensearch.index.query.QueryBuilder;
import org.opensearch.plugin.transport.grpc.proto.request.search.query.QueryBuilderProtoConverter;
import org.opensearch.protobufs.QueryContainer;

/**
 * Converter for KNN queries.
 * This class implements the QueryBuilderProtoConverter interface to provide KNN query support
 * for the gRPC transport plugin.
 */
public class KNNQueryBuilderProtoConverter implements QueryBuilderProtoConverter {

    @Override
    public boolean canHandle(QueryContainer queryContainer) {
        return queryContainer != null && queryContainer.getKnnCount() > 0;
    }

    @Override
    public QueryBuilder fromProto(QueryContainer queryContainer) {
        if (!canHandle(queryContainer)) {
            throw new IllegalArgumentException("QueryContainer does not contain a KNN query");
        }

        // Get the first entry from the map
        // KNN queries are represented as a map with field name as key and KnnField as value
        String fieldName = queryContainer.getKnnMap().keySet().iterator().next();
        return KNNQueryBuilderProtoUtils.fromProto(fieldName, queryContainer.getKnnMap().get(fieldName));
    }
}
