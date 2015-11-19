package no.nb.microservices.catalogcontentsearch.core.metadata.service;

import java.util.concurrent.Future;

import no.nb.microservices.catalogcontentsearch.core.search.service.TracableId;
import no.nb.microservices.catalogmetadata.model.struct.StructMap;

public interface MetadataService {

    Future<StructMap> getStructById(TracableId id);

}
