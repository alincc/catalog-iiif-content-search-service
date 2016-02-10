package no.nb.microservices.catalogcontentsearch.core.index.service;

import java.util.concurrent.Future;

import org.apache.htrace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import no.nb.microservices.catalogcontentsearch.core.index.repository.IndexRepository;
import no.nb.microservices.catalogcontentsearch.core.search.service.SecurityInfo;
import no.nb.microservices.catalogcontentsearch.core.search.service.TracableId;
import no.nb.microservices.catalogsearchindex.searchwithin.ContentSearchResource;

@Service
public class IndexServiceImpl implements IndexService {
    
    private IndexRepository indexRepository;

    @Autowired
    public IndexServiceImpl(IndexRepository indexRepository) {
        super();
        this.indexRepository = indexRepository;
    }

    @Override
    public Future<ContentSearchResource> contentSearch(TracableId tracableId,
            String q) {
        Trace.continueSpan(tracableId.getSpan());
        SecurityInfo securityInfo = tracableId.getSecurityInfo();
        ContentSearchResource contentSearchResult = indexRepository.contentSearch(tracableId.getId(), 
                q,
                securityInfo.getxHost(),
                securityInfo.getxPort(),
                securityInfo.getxRealIp(),
                securityInfo.getSsoToken());
        return new AsyncResult<ContentSearchResource>(contentSearchResult);
    }
    

}
