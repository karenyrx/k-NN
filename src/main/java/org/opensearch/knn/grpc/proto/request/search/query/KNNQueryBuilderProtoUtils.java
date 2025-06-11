/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.knn.grpc.proto.request.search.query;

import org.opensearch.core.xcontent.XContentParser;
import org.opensearch.index.query.QueryBuilder;
import org.opensearch.knn.index.query.KNNQueryBuilder;
import org.opensearch.knn.index.query.parser.KNNQueryBuilderParser;
import org.opensearch.protobufs.KnnField;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Utility class for converting KNN Protocol Buffers to OpenSearch objects.
 * This class provides methods to transform Protocol Buffer representations of KNN queries
 * into their corresponding OpenSearch KNNQueryBuilder implementations for search operations.
 */
public class KNNQueryBuilderProtoUtils {

    private KNNQueryBuilderProtoUtils() {
        // Utility class, no instances
    }

    /**
     * Converts a Protocol Buffer KnnField to an OpenSearch KNNQueryBuilder.
     * Similar to {@link KNNQueryBuilderParser#fromXContent(XContentParser)}, this method
     * parses the Protocol Buffer representation and creates a properly configured
     * KNNQueryBuilder with the appropriate field name, vector, k, and other settings.
     *
     * @param fieldName The name of the field to search
     * @param knnFieldProto The Protocol Buffer KnnField to convert
     * @return A configured KNNQueryBuilder instance
     */
    protected static QueryBuilder fromProto(String fieldName, KnnField knnFieldProto) {
        // Extract the vector from the KnnField
        List<Float> vectorList = knnFieldProto.getVectorList();
        float[] vector = new float[vectorList.size()];
        for (int i = 0; i < vectorList.size(); i++) {
            vector[i] = vectorList.get(i);
        }

        // Create a builder for the KNNQueryBuilder
        KNNQueryBuilder.Builder builder = KNNQueryBuilder.builder().fieldName(fieldName).vector(vector);

        // Set k
        builder.k(knnFieldProto.getK());

        // Set boost
        builder.boost(knnFieldProto.getBoost());

        // Set method parameters if present
        if (knnFieldProto.getMethodParametersCount() > 0) {
            Map<String, Object> methodParameters = new HashMap<>();
            for (Map.Entry<String, Integer> entry : knnFieldProto.getMethodParametersMap().entrySet()) {
                methodParameters.put(entry.getKey(), entry.getValue());
            }
            builder.methodParameters(methodParameters);
        }

        // Set max distance if present (maxDistance is always present, but we check if it's not the default value)
        if (knnFieldProto.getMaxDistance() != 0) {
            builder.maxDistance(knnFieldProto.getMaxDistance());
        }

        // Set min score if present (minScore is always present, but we check if it's not the default value)
        if (knnFieldProto.getMinScore() != 0) {
            builder.minScore(knnFieldProto.getMinScore());
        }

        // TODO: The KnnField class doesn't have methods for queryName or expandNested. Add to protos

        return builder.build();
    }
}
