package no.nb.microservices.catalogcontentsearch.core.metadata.service;

import java.util.concurrent.Future;

import org.apache.htrace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import no.nb.microservices.catalogcontentsearch.core.metadata.repository.MetadataRepository;
import no.nb.microservices.catalogcontentsearch.core.search.service.SecurityInfo;
import no.nb.microservices.catalogcontentsearch.core.search.service.TracableId;
import no.nb.microservices.catalogmetadata.model.struct.StructMap;

@Service
public class MetadataServiceImpl implements MetadataService {

    MetadataRepository metadataRepository;
    
    @Autowired
    public MetadataServiceImpl(MetadataRepository metadataRepository) {
        super();
        this.metadataRepository = metadataRepository;
    }

    @Override
    @Async
    public Future<StructMap> getStructById(TracableId id) {
      Trace.continueSpan(id.getSpan());
      SecurityInfo securityInfo = id.getSecurityInfo();
      StructMap struct = metadataRepository.getStructById(id.getId(), securityInfo.getxHost(), securityInfo.getxPort(), securityInfo.getxRealIp(), securityInfo.getSsoToken());
      return new AsyncResult<StructMap>(struct);
    }

}
